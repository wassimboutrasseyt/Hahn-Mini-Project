package com.whyboutrasseyt.miniproject.application.service;

import com.whyboutrasseyt.miniproject.application.command.CreateTaskCommand;
import com.whyboutrasseyt.miniproject.application.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto createTask(Long ownerId, CreateTaskCommand command);

    List<TaskDto> listTasks(Long ownerId, Long projectId);

    TaskDto markCompleted(Long ownerId, Long taskId);

    TaskDto toggleCompleted(Long ownerId, Long taskId);

    void deleteTask(Long ownerId, Long taskId);
}
