package com.example.taskapp.taskmanagement.dataaccess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Task Transfer Object
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskTO {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime creationDate;

    private LocalDateTime dueDate;

    private boolean done;

    private LocalDateTime completionDate;

}
