package com.example.taskapp.taskmanagement.dataaccess.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Task Response
 */
@Getter
@Setter
public class TaskResponse {

    private List<TaskTO> tasks;

    private Pagination pagination;

    public TaskResponse(List<TaskTO> tasks, int page, int pageSize, long totalItems, int totalPages){
        this.tasks = tasks;
        this.pagination = new Pagination(page, pageSize, totalItems, totalPages);
    }
}
