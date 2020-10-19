package com.example.taskapp.taskmanagement.dataaccess.repository;

import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Task Respository
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserNameOrderByDueDateAsc(String userName);
}
