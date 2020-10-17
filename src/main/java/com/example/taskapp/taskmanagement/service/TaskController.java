package com.example.taskapp.taskmanagement.service;

import com.example.taskapp.taskmanagement.dataaccess.dto.SearchCriteria;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.logic.api.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TaskController {

    @Autowired
    private TaskManager taskManager;

    @GetMapping("/tasks")
    public ResponseEntity<Object> searchTasks(SearchCriteria criteria){
        log.info("Request to [SEARCH] service with criteria: {}", criteria.toString());
        return ResponseEntity.ok().body(this.taskManager.getTaskList(criteria));
    }

    @PostMapping("/tasks")
    public ResponseEntity<Object> createTask(TaskRequest request){
        log.info("Request to [CREATE] service with details: {}", request);
        return ResponseEntity.ok().body(this.taskManager.create(request));
    }
}
