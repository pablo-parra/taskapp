package com.example.taskapp.taskmanagement.dataaccess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Task Request
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskRequest {

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private Boolean done;
}
