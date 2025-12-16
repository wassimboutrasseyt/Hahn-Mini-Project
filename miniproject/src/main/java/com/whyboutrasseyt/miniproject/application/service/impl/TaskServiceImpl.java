package com.whyboutrasseyt.miniproject.application.service.impl;

import com.whyboutrasseyt.miniproject.application.command.CreateTaskCommand;
import com.whyboutrasseyt.miniproject.application.dto.TaskDto;
import com.whyboutrasseyt.miniproject.application.service.TaskService;
import com.whyboutrasseyt.miniproject.domain.exception.NotFoundException;
import com.whyboutrasseyt.miniproject.domain.exception.ValidationException;
import com.whyboutrasseyt.miniproject.domain.model.Project;
import com.whyboutrasseyt.miniproject.domain.model.Task;
import com.whyboutrasseyt.miniproject.domain.repository.ProjectRepository;
import com.whyboutrasseyt.miniproject.domain.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskDto createTask(Long ownerId, CreateTaskCommand command) {
        Project project = requireProject(ownerId, command.projectId());
        validateCreate(command);
        Task saved = taskRepository.save(new Task(
                null,
                project.id(),
                command.title().trim(),
                command.description(),
                command.dueDate(),
                false
        ));
        return toDto(saved);
    }

    @Override
    public List<TaskDto> listTasks(Long ownerId, Long projectId) {
        requireProject(ownerId, projectId);
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto markCompleted(Long ownerId, Long taskId) {
        Task task = findTask(taskId);
        requireProject(ownerId, task.projectId());
        if (task.completed()) {
            return toDto(task);
        }
        Task updated = taskRepository.save(new Task(
                task.id(),
                task.projectId(),
                task.title(),
                task.description(),
                task.dueDate(),
                true
        ));
        return toDto(updated);
    }

    @Override
    public void deleteTask(Long ownerId, Long taskId) {
        Task task = findTask(taskId);
        requireProject(ownerId, task.projectId());
        taskRepository.deleteById(taskId);
    }

    private void validateCreate(CreateTaskCommand command) {
        if (command == null) {
            throw new ValidationException("Command is required");
        }
        if (command.projectId() == null) {
            throw new ValidationException("projectId is required");
        }
        if (command.title() == null || command.title().isBlank()) {
            throw new ValidationException("Task title is required");
        }
    }

    private Project requireProject(Long ownerId, Long projectId) {
        Objects.requireNonNull(ownerId, "ownerId is required");
        Objects.requireNonNull(projectId, "projectId is required");
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project", projectId));
        if (!project.ownerId().equals(ownerId)) {
            throw new ValidationException("You do not own this project");
        }
        return project;
    }

    private Task findTask(Long taskId) {
        Objects.requireNonNull(taskId, "taskId is required");
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task", taskId));
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(
                task.id(),
                task.projectId(),
                task.title(),
                task.description(),
                task.dueDate(),
                task.completed()
        );
    }
}
