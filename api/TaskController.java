package com.whyboutrasseyt.miniproject.api;

import com.whyboutrasseyt.miniproject.api.request.CreateTaskRequest;
import com.whyboutrasseyt.miniproject.api.response.TaskResponse;
import com.whyboutrasseyt.miniproject.application.command.CreateTaskCommand;
import com.whyboutrasseyt.miniproject.application.dto.TaskDto;
import com.whyboutrasseyt.miniproject.application.service.TaskService;
import com.whyboutrasseyt.miniproject.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        TaskDto dto = taskService.createTask(
                principal.domainUser().id(),
                new CreateTaskCommand(request.projectId(), request.title(), request.description(), request.dueDate())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(dto));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> listTasks(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam Long projectId
    ) {
        List<TaskResponse> tasks = taskService.listTasks(principal.domainUser().id(), projectId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> markCompleted(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        TaskDto dto = taskService.markCompleted(principal.domainUser().id(), id);
        return ResponseEntity.ok(toResponse(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        taskService.deleteTask(principal.domainUser().id(), id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponse toResponse(TaskDto dto) {
        return new TaskResponse(
                dto.id(),
                dto.projectId(),
                dto.title(),
                dto.description(),
                dto.dueDate(),
                dto.completed()
        );
    }
}
