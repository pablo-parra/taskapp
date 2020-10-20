package com.example.taskapp.taskmanagement.dataaccess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Pagination Metadata
 */
@Getter
@Setter
@AllArgsConstructor
public class Pagination {

    private int page;

    private int pageSize;

    private long totalItems;

    private int totalPages;
}
