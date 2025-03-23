package com.malithi.taskmanagement.service;

import com.malithi.taskmanagement.dto.TaskDto;
import com.malithi.taskmanagement.exception.ResourceNotFoundException;
import com.malithi.taskmanagement.model.Task;
import com.malithi.taskmanagement.model.Task.TaskStatus;
import com.malithi.taskmanagement.model.User;
import com.malithi.taskmanagement.repository.TaskRepository;
import com.malithi.taskmanagement.repository.UserRepository;
import com.malithi.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = convertToEntity(taskDto);

        // Set timestamps
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return convertToDto(task);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return taskRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksByUserAndStatus(Long userId, TaskStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return taskRepository.findByUserAndStatus(user, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        // Update the fields
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setDueDate(taskDto.getDueDate());

        // Update user if changed
        if (taskDto.getUserId() != null && (task.getUser() == null || !task.getUser().getId().equals(taskDto.getUserId()))) {
            User user = userRepository.findById(taskDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + taskDto.getUserId()));
            task.setUser(user);
        }

        // Update timestamp
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        return convertToDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // Helper methods to convert between Entity and DTO
    private Task convertToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus() != null ? taskDto.getStatus() : Task.TaskStatus.TODO);
        task.setPriority(taskDto.getPriority() != null ? taskDto.getPriority() : Task.TaskPriority.MEDIUM);
        task.setDueDate(taskDto.getDueDate());

        // Set the user if userId is provided
        if (taskDto.getUserId() != null) {
            User user = userRepository.findById(taskDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + taskDto.getUserId()));
            task.setUser(user);
        }

        return task;
    }

    private TaskDto convertToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        taskDto.setPriority(task.getPriority());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setCreatedAt(task.getCreatedAt());
        taskDto.setUpdatedAt(task.getUpdatedAt());

        // Set the userId if user is not null
        if (task.getUser() != null) {
            taskDto.setUserId(task.getUser().getId());
        }

        return taskDto;
    }
}