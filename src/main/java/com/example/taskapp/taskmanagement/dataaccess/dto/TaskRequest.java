package com.example.taskapp.taskmanagement.dataaccess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.FutureOrPresent;
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

    @NotNull(message = "'title' is mandatory")
    private String title;

    private String description;

    @FutureOrPresent(message = "'dueDate' can't be in the past")
    private LocalDateTime dueDate;

    private Boolean done;
}
