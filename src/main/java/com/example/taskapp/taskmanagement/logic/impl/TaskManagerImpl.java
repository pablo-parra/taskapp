package com.example.taskapp.taskmanagement.logic.impl;

import com.example.taskapp.common.exception.BadRequestException;
import com.example.taskapp.common.mapper.Mapper;
import com.example.taskapp.taskmanagement.dataaccess.dto.SearchCriteria;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import com.example.taskapp.taskmanagement.dataaccess.repository.TaskRepository;
import com.example.taskapp.taskmanagement.logic.api.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Example<Task> filter = Example.of(this.mapper.toTaskByUserFilter(criteria));
        List<Task> taskList = this.taskRepository.findAll(filter, Sort.by("dueDate").ascending());
        log.info("{} tasks found", taskList.size());
        return this.mapper.mapList(taskList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTO getTask(long id) {
        log.info("Getting details of task {}", id);
        Optional<Task> task = this.taskRepository.findById(id);
        return task.isPresent() ? this.mapper.map(task.get(), TaskTO.class) : new TaskTO();
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
    public TaskTO update(long id, TaskRequest request) {
        log.info("Updating task {}...", id);
        Optional<Task> originalTask = this.taskRepository.findById(id);
        if(originalTask.isPresent()){
            Task task = this.mapper.merge(originalTask.get(), request);
            Task updatedTask = this.taskRepository.save(task);
            log.info("Task {} successfully updated.", id);
            return this.mapper.map(updatedTask, TaskTO.class);
        }else{
            throw new BadRequestException("Task not found.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(long id) {
        log.info("Deleting task {}...", id);
        if(this.taskRepository.findById(id).isPresent()){
            this.taskRepository.deleteById(id);
            log.info("Task {} successfully deleted.", id);
        }else{
            throw new BadRequestException("Task not found.");
        }
    }
}
