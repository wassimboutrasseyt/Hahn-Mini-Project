package com.whyboutrasseyt.miniproject.presentation;

import com.whyboutrasseyt.miniproject.application.command.CreateProjectCommand;
import com.whyboutrasseyt.miniproject.application.dto.ProjectDto;
import com.whyboutrasseyt.miniproject.application.service.ProjectService;
import com.whyboutrasseyt.miniproject.infrastructure.security.UserPrincipal;
import com.whyboutrasseyt.miniproject.presentation.request.CreateProjectRequest;
import com.whyboutrasseyt.miniproject.presentation.response.ProjectResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateProjectRequest request
    ) {
        ProjectDto dto = projectService.createProject(
                new CreateProjectCommand(principal.domainUser().id(), request.title(), request.description())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listProjects(@AuthenticationPrincipal UserPrincipal principal) {
        List<ProjectResponse> projects = projectService.listProjects(principal.domainUser().id())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        ProjectDto dto = projectService.getProject(principal.domainUser().id(), id);
        return ResponseEntity.ok(toResponse(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        projectService.deleteProject(principal.domainUser().id(), id);
        return ResponseEntity.noContent().build();
    }

    private ProjectResponse toResponse(ProjectDto dto) {
        double progress = dto.totalTasks() > 0
                ? (dto.completedTasks() * 100.0) / dto.totalTasks()
                : 0.0;
        return new ProjectResponse(
                dto.id(),
                dto.title(),
                dto.description(),
                dto.totalTasks(),
                dto.completedTasks(),
                Math.round(progress * 100.0) / 100.0
        );
    }
}
