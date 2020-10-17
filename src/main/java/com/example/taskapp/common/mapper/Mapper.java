package com.example.taskapp.common.mapper;

import com.example.taskapp.taskmanagement.dataaccess.dto.TaskTO;
import com.example.taskapp.taskmanagement.dataaccess.entity.Task;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Mapper utility
 */
@Component
public class Mapper {

    private ModelMapper mapper = new ModelMapper();

    Type taskListType = new TypeToken<List<Task>>(){}.getType();

    /**
     * The mapper
     * @return mapper
     */
    public ModelMapper mapper(){
        return this.mapper;
    }

    /**
     * Generic mapper
     *
     * @param source the source object
     * @param targetClass the target class
     * @param <S> source
     * @param <T> target
     * @return mapped object
     */
    public <S, T>  T map(S source, Class<T> targetClass){
        return this.mapper.map(source, targetClass);
    }

    /**
     * Map list
     * @param taskList list of {@link Task}
     * @return list of {@link TaskTO}
     */
    public List<TaskTO> mapList(List<Task> taskList){
        return this.mapper.map(taskList, taskListType);
    }
}
