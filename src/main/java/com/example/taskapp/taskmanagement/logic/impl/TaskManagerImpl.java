package com.example.taskapp.taskmanagement.logic.impl;

import com.example.taskapp.common.mapper.Mapper;
import com.example.taskapp.taskmanagement.dataaccess.dto.SearchCriteria;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import com.example.taskapp.taskmanagement.dataaccess.repository.TaskRepository;
import com.example.taskapp.taskmanagement.logic.api.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation for the {@TaskManager} interface
 */
@Component
@Slf4j
public class TaskManagerImpl implements TaskManager {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private Mapper mapper;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskTO> getTaskList(SearchCriteria criteria) {
        log.info("Searching tasks...");
        List<Task> taskList = taskRepository.findAll();
        log.info("{} tasks found", taskList.size());
        return this.mapper.mapList(taskList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTO getTask(String id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTO create(TaskRequest request) {
        log.info("Creating task...");

        Task task = this.mapper.map(request, Task.class);
        task.setDone(false);
        task.setCreationDate(LocalDateTime.now());
        task.setUserId(1L);

        Task createdTask = this.taskRepository.save(task);
        log.info("Task successfully created");
        return this.mapper.map(createdTask, TaskTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTO update(String id, TaskRequest request) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(String id) {

    }
}
