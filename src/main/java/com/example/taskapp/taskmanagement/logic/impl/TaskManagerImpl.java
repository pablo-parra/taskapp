package com.example.taskapp.taskmanagement.logic.impl;

import com.example.taskapp.common.exception.BadRequestException;
import com.example.taskapp.common.exception.UnauthorizedException;
import com.example.taskapp.common.mapper.Mapper;
import com.example.taskapp.taskmanagement.dataaccess.dto.SearchCriteria;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskResponse;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import com.example.taskapp.taskmanagement.dataaccess.repository.TaskRepository;
import com.example.taskapp.taskmanagement.logic.api.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    @Value("${taskapp.page.default}")
    private int defaultPage;

    @Value("${taskapp.page-size.default}")
    private int defaultPageSize;


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResponse getTaskList(SearchCriteria criteria) {
        log.info("Searching tasks...");
        Example<Task> filter = Example.of(this.mapper.toTaskByUserFilter(criteria));
        int page = null != criteria.getPage() ? criteria.getPage() : this.defaultPage;
        int pageSize = null != criteria.getPageSize() ? criteria.getPageSize() : this.defaultPageSize;
        Pageable paging = PageRequest.of(page, pageSize, Sort.by("dueDate").ascending());
        Page<Task> taskList = this.taskRepository.findAll(filter, paging);
        log.info("{} tasks found", taskList.getContent().size());
        return new TaskResponse(this.mapper.mapList(taskList.getContent()), page, pageSize, taskList.getTotalElements(), taskList.getTotalPages());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTO getTask(long id) {
        log.info("Getting details of task {}", id);
        Optional<Task> task = this.taskRepository.findById(id);
        if (task.isPresent()) {
            belongsToUser(task.get());
            return this.mapper.map(task.get(), TaskTO.class);
        } else {
            return null;
        }
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
        task.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());

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
        if (originalTask.isPresent()) {
            belongsToUser(originalTask.get());
            Task task = this.mapper.merge(originalTask.get(), request);
            Task updatedTask = this.taskRepository.save(task);
            log.info("Task {} successfully updated.", id);
            return this.mapper.map(updatedTask, TaskTO.class);
        } else {
            throw new BadRequestException("Task not found.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(long id) {
        log.info("Deleting task {}...", id);
        Optional<Task> task = this.taskRepository.findById(id);
        if (task.isPresent()) {
            belongsToUser(task.get());
            this.taskRepository.deleteById(id);
            log.info("Task {} successfully deleted.", id);
        } else {
            throw new BadRequestException("Task not found.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void belongsToUser(Task task) {
        if (!task.getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new UnauthorizedException("No permissions over task ".concat(String.valueOf(task.getId())));
        }
    }
}
