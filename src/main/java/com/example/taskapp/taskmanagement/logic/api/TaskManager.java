package com.example.taskapp.taskmanagement.logic.api;

import com.example.taskapp.taskmanagement.dataaccess.dto.SearchCriteria;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;

import java.util.List;

/**
 * Task management functionality definition
 */
public interface TaskManager {

    /**
     * Returns a list of tasks based on a search criteria
     *
     * @param criteria the search criteria
     * @return the list of {@link TaskTO}s that match the criteria
     */
    List<TaskTO> getTaskList(SearchCriteria criteria);

    /**
     * Returns the {@link TaskTO} by ID
     *
     * @param id the task ID
     * @return the {@link TaskTO} that matches the provided ID
     */
    TaskTO getTask(String id);

    /**
     * Creates a new task
     *
     * @param request the task details
     * @return the {@link TaskTO} created
     */
    TaskTO create(TaskRequest request);

    /**
     * Updates an existing task
     *
     * @param id      the task ID
     * @param request the task details to update
     * @return the {@link TaskTO} after the update
     */
    TaskTO update(String id, TaskRequest request);

    /**
     * Deletes a task
     *
     * @param id the task ID
     */
    void deleteTask(String id);

}
