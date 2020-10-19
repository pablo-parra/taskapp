package com.example.taskapp;

import com.example.taskapp.common.TestUtils;
import com.example.taskapp.common.exception.BadRequestException;
import com.example.taskapp.common.exception.UnauthorizedException;
import com.example.taskapp.common.mapper.Mapper;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import com.example.taskapp.taskmanagement.dataaccess.repository.TaskRepository;
import com.example.taskapp.taskmanagement.logic.api.TaskManager;
import com.example.taskapp.taskmanagement.logic.impl.TaskManagerImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {TaskManagerImpl.class, Mapper.class})
@ExtendWith(MockitoExtension.class)
public class TaskUnitTests {

    @Autowired
    private TaskManager taskManager;

    @MockBean
    private TaskRepository taskRepositoryMock;

    @BeforeEach
    void beforeEach(TestInfo testInfo) { TestUtils.testHeader(testInfo.getDisplayName());
    }

    @AfterEach
    void afterEach() {
        TestUtils.testEnd();
    }

    @Test
    @WithMockUser("john")
    void taskBelongsToUser(){
        Task task = new Task();
        task.setUserName("susan");
        UnauthorizedException expected = assertThrows(UnauthorizedException.class, () -> this.taskManager.belongsToUser(task));
        org.assertj.core.api.Assertions.assertThat(expected.getMessage()).contains("No permissions over task");
    }

    @Test
    void updateNonExistingTask_ThrowsExpectedException(){
        Mockito.doReturn(Optional.empty())
                .when(this.taskRepositoryMock)
                .findById(Mockito.anyLong());

        BadRequestException expected = assertThrows(BadRequestException.class, () -> this.taskManager.update(999, new TaskRequest()));
        Assertions.assertThat(expected.getMessage()).isEqualTo("Task not found.");
    }

    @Test
    void getNonExistingTask_ReturnsNull(){
        Mockito.doReturn(Optional.empty())
                .when(this.taskRepositoryMock)
                .findById(Mockito.anyLong());

        TaskTO task = this.taskManager.getTask(999);
        Assertions.assertThat(task).isNull();
    }

}
