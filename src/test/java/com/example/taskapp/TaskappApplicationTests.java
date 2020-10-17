package com.example.taskapp;

import com.example.taskapp.common.TestUtils;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import com.example.taskapp.taskmanagement.dataaccess.repository.TaskRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
	void findTasks_ShouldReturnExpectedList() throws Exception {

		MvcResult result = httpGet("/api/v1/tasks", new LinkedMultiValueMap<>()).andExpect(status().isOk()).andReturn();
		List<TaskTO> response = this.gson.fromJson(result.getResponse().getContentAsString(), List.class);
		Assertions.assertThat(response).size().isEqualTo(5);
	}

	@Test
	void createTask_CreatesExpectedTask() throws Exception {
		List<Task> tasksBefore = this.taskRepository.findAll();
		Assertions.assertThat(tasksBefore).size().isEqualTo(5);

		String newTasktitle = "this is my new task";
		TaskRequest request = new TaskRequest();
		request.setTitle(newTasktitle);

		MvcResult result = httpPost("/api/v1/tasks", request).andExpect(status().isOk()).andReturn();
		TaskTO response = this.gson.fromJson(result.getResponse().getContentAsString(), TaskTO.class);
		Assertions.assertThat(response.getTitle()).isEqualTo(newTasktitle);

		List<Task> tasksAfter = this.taskRepository.findAll();
		Assertions.assertThat(tasksAfter).size().isEqualTo(tasksBefore.size() + 1);

		cleanUpDB(response.getId());
	}

	private ResultActions httpGet(String url, MultiValueMap<String, String> params) throws Exception {
		return this.mvc.perform(get(url).header("Accept", "application/json").header("Content-Type", "application/json")
				/*.header("authorization", this.AUTH_TOKEN)*/.params(params));
	}

	private ResultActions httpPost(String url, TaskRequest request) throws Exception {
		return this.mvc.perform(post(url).header("Accept", "application/json").header("Content-Type", "application/json")
				/*.header("authorization", this.AUTH_TOKEN)*/.content(this.gson.toJson(request)));
	}

	private void cleanUpDB(long... ids){
		for(long id : ids){
			this.taskRepository.deleteById(id);
		}
	}

}