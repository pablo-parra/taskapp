package com.example.taskapp.taskmanagement.dataaccess.repository;

import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Task Respository
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUserNameOrderByDueDateAsc(String userName, Pageable pageable);
}
