package com.example.taskapp.taskmanagement.dataaccess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Task search criteria
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchCriteria {

    private String title;

    private String description;

    private LocalDateTime startRange;

    private LocalDateTime endRange;

    private LocalDateTime dueDate;

    private Boolean done;

    private Integer page;

    private Integer pageSize;
}
