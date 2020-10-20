package com.example.taskapp.taskmanagement.service;

import com.example.taskapp.common.exception.ErrorResponse;
import com.example.taskapp.taskmanagement.dataaccess.dto.SearchCriteria;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskResponse;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.logic.api.TaskManager;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/tasks")
@Slf4j
public class TaskController {

    @Autowired
    private TaskManager taskManager;

    /**
     * Task search service
     *
     * @param criteria the search criteria
     * @return the tasks that matches the criteria
     */
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TaskResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)
    })
    public ResponseEntity<Object> searchTasks(@RequestBody SearchCriteria criteria) {
        log.info("Request to [SEARCH] service with criteria: {}", criteria.toString());
        return ResponseEntity.ok().body(this.taskManager.getTaskList(criteria));
    }

    /**
     * Task details service
     *
     * @param id the ID of the task
     * @return the task details
     */
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TaskTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)
    })
    public ResponseEntity<Object> getTask(@PathVariable("id") long id) {
        log.info("Request to [GET] details of task {}", id);
        return ResponseEntity.ok().body(this.taskManager.getTask(id));
    }

    /**
     * Create task service
     *
     * @param request the task details
     * @return the created task
     */
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TaskResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)
    })
    public ResponseEntity<Object> createTask(@Valid @RequestBody TaskRequest request) {
        log.info("Request to [CREATE] service with details: {}", request);
        return ResponseEntity.ok().body(this.taskManager.create(request));
    }

    /**
     * Update task service
     *
     * @param id      the ID of the task to update
     * @param request the data to be updated
     * @return the updated task
     */
    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TaskResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)
    })
    public ResponseEntity<Object> updateTask(@PathVariable("id") long id, @RequestBody TaskRequest request) {
        log.info("Request to [UPDATE] task {} with values: {}", id, request);
        return ResponseEntity.ok().body(this.taskManager.update(id, request));
    }

    /**
     * Delete task service
     *
     * @param id the ID of the task to delete
     * @return the delete result
     */
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TaskResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)
    })
    public ResponseEntity<Object> deleteTask(@PathVariable("id") long id) {
        log.info("Request to [DELETE] service -> task {}", id);
        this.taskManager.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}
