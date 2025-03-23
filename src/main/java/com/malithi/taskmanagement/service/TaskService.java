package com.malithi.taskmanagement.service;

import com.malithi.taskmanagement.dto.TaskDto;
import com.malithi.taskmanagement.model.Task.TaskStatus;
import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);
    TaskDto getTaskById(Long id);
    List<TaskDto> getAllTasks();
    List<TaskDto> getTasksByUser(Long userId);
    List<TaskDto> getTasksByUserAndStatus(Long userId, TaskStatus status);
    TaskDto updateTask(Long id, TaskDto taskDto);
    void deleteTask(Long id);
}