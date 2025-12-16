package com.whyboutrasseyt.miniproject.application.service.impl;

import com.whyboutrasseyt.miniproject.application.command.CreateProjectCommand;
import com.whyboutrasseyt.miniproject.application.dto.ProjectDto;
import com.whyboutrasseyt.miniproject.application.service.ProjectService;
import com.whyboutrasseyt.miniproject.domain.exception.NotFoundException;
import com.whyboutrasseyt.miniproject.domain.exception.ValidationException;
import com.whyboutrasseyt.miniproject.domain.model.Project;
import com.whyboutrasseyt.miniproject.domain.repository.ProjectRepository;
import com.whyboutrasseyt.miniproject.domain.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ProjectDto createProject(CreateProjectCommand command) {
        validateCreate(command);
        Project toSave = new Project(null, command.ownerId(), command.title().trim(), command.description());
        Project saved = projectRepository.save(toSave);
        return toDto(saved);
    }

    @Override
    public List<ProjectDto> listProjects(Long ownerId) {
        Objects.requireNonNull(ownerId, "ownerId is required");
        return projectRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getProject(Long ownerId, Long projectId) {
        Project project = findOwnedProject(ownerId, projectId);
        return toDto(project);
    }

    @Override
    public void deleteProject(Long ownerId, Long projectId) {
        findOwnedProject(ownerId, projectId);
        projectRepository.deleteById(projectId);
    }

    private void validateCreate(CreateProjectCommand command) {
        if (command == null) {
            throw new ValidationException("Command is required");
        }
        Objects.requireNonNull(command.ownerId(), "ownerId is required");
        if (command.title() == null || command.title().isBlank()) {
            throw new ValidationException("Project title is required");
        }
    }

    private Project findOwnedProject(Long ownerId, Long projectId) {
        Objects.requireNonNull(ownerId, "ownerId is required");
        Objects.requireNonNull(projectId, "projectId is required");
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project", projectId));
        if (!project.ownerId().equals(ownerId)) {
            throw new ValidationException("You do not own this project");
        }
        return project;
    }

    private ProjectDto toDto(Project project) {
        long totalTasks = taskRepository.countByProjectId(project.id());
        long completedTasks = taskRepository.countCompletedByProjectId(project.id());
        return new ProjectDto(
                project.id(),
                project.title(),
                project.description(),
                totalTasks,
                completedTasks
        );
    }
}
