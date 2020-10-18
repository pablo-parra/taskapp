package com.example.taskapp.common.mapper;

import com.example.taskapp.taskmanagement.dataaccess.dto.SearchCriteria;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskRequest;
import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Mapper utility
 */
@Component
public class Mapper {

    private ModelMapper mapper = new ModelMapper();

    Type taskListType = new TypeToken<List<Task>>() {
    }.getType();

    /**
     * The mapper
     *
     * @return mapper
     */
    public ModelMapper mapper() {
        return this.mapper;
    }

    /**
     * Generic mapper
     *
     * @param source      the source object
     * @param targetClass the target class
     * @param <S>         source
     * @param <T>         target
     * @return mapped object
     */
    public <S, T> T map(S source, Class<T> targetClass) {
        return this.mapper.map(source, targetClass);
    }

    /**
     * Map list
     *
     * @param taskList list of {@link Task}
     * @return list of {@link TaskTO}
     */
    public List<TaskTO> mapList(List<Task> taskList) {
        return this.mapper.map(taskList, taskListType);
    }

    /**
     * Merges a {@link TaskRequest} with a {@link Task} object, existing fields in request replace values in existing task
     *
     * @param task    the original task
     * @param request the upcoming request
     * @return the merged object
     */
    public Task merge(Task task, TaskRequest request) {

        if (null != request.getTitle()) {
            task.setTitle(request.getTitle());
        }

        if (null != request.getDescription()) {
            task.setDescription(request.getDescription());
        }

        if (null != request.getDueDate()) {
            task.setDueDate(request.getDueDate());
        }

        if (null != request.getDone()
                && request.getDone().equals(Boolean.TRUE)
                && null != task.getDone()
                && task.getDone().equals(Boolean.FALSE)) {
            task.setDone(request.getDone());
            task.setCompletionDate(LocalDateTime.now());
        }

        return task;
    }

    public Task toTaskByUserFilter(SearchCriteria criteria){
        Task task = this.map(criteria, Task.class);
        task.setUserId(getUserId());
        return task;
    }

    private Long getUserId(){
        //TODO implement real SecurityContextHolder
        return 1L;
    }
}
