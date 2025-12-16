package com.whyboutrasseyt.miniproject.application.service;

import com.whyboutrasseyt.miniproject.application.command.CreateProjectCommand;
import com.whyboutrasseyt.miniproject.application.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto createProject(CreateProjectCommand command);

    List<ProjectDto> listProjects(Long ownerId);

    ProjectDto getProject(Long ownerId, Long projectId);

    void deleteProject(Long ownerId, Long projectId);
}
