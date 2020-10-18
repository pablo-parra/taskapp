package com.example.taskapp;

import com.example.taskapp.common.TestUtils;
import com.example.taskapp.common.exception.ErrorResponse;
import com.example.taskapp.taskmanagement.dataaccess.dto.SearchCriteria;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import com.example.taskapp.taskmanagement.dataaccess.repository.TaskRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TaskappApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
			(JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> LocalDateTime.parse(json.getAsString())).create();

	@Autowired
	private TaskRepository taskRepository;

	@BeforeEach
	void beforeEach(TestInfo testInfo) {
		if(null == this.mvc){
			this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
					/* .addFilters(this.springSecurityFilterChain) */.build();
		}
		TestUtils.testHeader(testInfo.getDisplayName());
	}

	@AfterEach
	void afterEach() {
		TestUtils.testSuccess();
	}

	@Test
	void findTask_ReturnsExpectedTask() throws Exception {
		String taskId = "1";
		MvcResult result = httpGet(TestUtils.BASE_URL.concat("/").concat(taskId), new SearchCriteria()).andExpect(status().isOk()).andReturn();
		TaskTO response = this.gson.fromJson(result.getResponse().getContentAsString(), TaskTO.class);
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getId()).isEqualTo(Long.valueOf(taskId));
	}

	@Test
	void findTasks_ShouldReturnExpectedList() throws Exception {

		MvcResult result = httpGet(TestUtils.BASE_URL, new SearchCriteria()).andExpect(status().isOk()).andReturn();
		List<TaskTO> response = this.gson.fromJson(result.getResponse().getContentAsString(), List.class);
		Assertions.assertThat(response).size().isEqualTo(5);
	}

	@Test
	void filterByStatus_ReturnsExpectedTasks() throws Exception {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setDone(true);
		MvcResult result = httpGet(TestUtils.BASE_URL, criteria).andExpect(status().isOk()).andReturn();
		List<TaskTO> response = this.gson.fromJson(result.getResponse().getContentAsString(), List.class);
		Assertions.assertThat(response).size().isEqualTo(2);
	}

	@Test
	void createTask_CreatesExpectedTask() throws Exception {
		List<Task> tasksBefore = this.taskRepository.findAll();
		Assertions.assertThat(tasksBefore).size().isEqualTo(5);

		String newTaskTitle = "this is my new task";
		TaskRequest request = new TaskRequest();
		request.setTitle(newTaskTitle);

		MvcResult result = httpPost(TestUtils.BASE_URL, request).andExpect(status().isOk()).andReturn();
		TaskTO response = this.gson.fromJson(result.getResponse().getContentAsString(), TaskTO.class);
		Assertions.assertThat(response.getTitle()).isEqualTo(newTaskTitle);

		List<Task> tasksAfter = this.taskRepository.findAll();
		Assertions.assertThat(tasksAfter).size().isEqualTo(tasksBefore.size() + 1);

		cleanUpDB(response.getId());
	}

	@Test
	void createTaskWithoutTitle_ThrowsExpectedException() throws Exception {
		MvcResult result = httpPost(TestUtils.BASE_URL, new TaskRequest()).andExpect(status().isBadRequest()).andReturn();
		ErrorResponse response = this.gson.fromJson(result.getResponse().getContentAsString(), ErrorResponse.class);
		Assertions.assertThat(response.getErrorMessage()).isEqualTo("'title' is mandatory");
	}

	@Test
	void deleteTask_DeletesExpectedTask() throws Exception {
		Task task = new Task();
		task.setTitle("hello world");

		Task createdTask = this.taskRepository.save(task);
		List<Task> tasksBeforeDelete = this.taskRepository.findAll();

		MvcResult result = httpDelete(TestUtils.BASE_URL, String.valueOf(createdTask.getId())).andExpect(status().isOk()).andReturn();

		List<Task> tasksAfterDelete = this.taskRepository.findAll();

		Assertions.assertThat(tasksAfterDelete).size().isEqualTo(tasksBeforeDelete.size() - 1);
		Assertions.assertThat(tasksAfterDelete.stream().anyMatch(t -> t.getId().equals(createdTask.getId()))).isFalse();
	}

	@Test
	void deleteTaskWrongId_ThrowsExpectedException() throws Exception {
		httpDelete(TestUtils.BASE_URL, "not-a-number").andExpect(status().isBadRequest()).andReturn();
	}

	@Test
	void deleteNonExistingTask_ThrowsExpectedException() throws Exception {
		MvcResult result = httpDelete(TestUtils.BASE_URL, "999999").andExpect(status().isBadRequest()).andReturn();
		ErrorResponse response = this.gson.fromJson(result.getResponse().getContentAsString(), ErrorResponse.class);
		Assertions.assertThat(response.getErrorMessage()).isEqualTo("Task not found.");
	}

	@Test
	void updateTask_UpdatesTheTaskData() throws Exception {
		Task originalTask = new Task();
		originalTask.setTitle("Title version 1");
		originalTask.setDescription("This is a description");
		originalTask.setDone(false);
		Task createdTask = this.taskRepository.save(originalTask);

		TaskRequest taskToUpdate = new TaskRequest();
		taskToUpdate.setTitle("Title version 2");
		taskToUpdate.setDone(true);

		MvcResult result = httpPut(TestUtils.BASE_URL, String.valueOf(createdTask.getId()), taskToUpdate).andExpect(status().isOk()).andReturn();
		TaskTO response = this.gson.fromJson(result.getResponse().getContentAsString(), TaskTO.class);
		Assertions.assertThat(response.getTitle()).isEqualTo("Title version 2");
		Assertions.assertThat(response.getDescription()).isEqualTo("This is a description");
		Assertions.assertThat(response.isDone()).isTrue();
		Assertions.assertThat(response.getCompletionDate()).isNotNull();

		cleanUpDB(createdTask.getId());
	}

	private ResultActions httpGet(String url, SearchCriteria criteria) throws Exception {
		return this.mvc.perform(get(url).header("Accept", "application/json").header("Content-Type", "application/json")
				/*.header("authorization", this.AUTH_TOKEN)*/.content(this.gson.toJson(criteria)));
	}

	private ResultActions httpPost(String url, TaskRequest request) throws Exception {
		return this.mvc.perform(post(url).header("Accept", "application/json").header("Content-Type", "application/json")
				/*.header("authorization", this.AUTH_TOKEN)*/.content(this.gson.toJson(request)));
	}

	private ResultActions httpPut(String url, String id, TaskRequest request) throws Exception {
		return this.mvc.perform(put(url.concat("/").concat(id)).header("Accept", "application/json").header("Content-Type", "application/json")
				/*.header("authorization", this.AUTH_TOKEN)*/.content(this.gson.toJson(request)));
	}

	private ResultActions httpDelete(String url, String id) throws Exception {
		return this.mvc.perform(delete(url.concat("/").concat(id)).header("Accept", "application/json").header("Content-Type", "application/json")
				/*.header("authorization", this.AUTH_TOKEN)*/);
	}

	private void cleanUpDB(long... ids){
		for(long id : ids){
			this.taskRepository.deleteById(id);
		}
	}

}