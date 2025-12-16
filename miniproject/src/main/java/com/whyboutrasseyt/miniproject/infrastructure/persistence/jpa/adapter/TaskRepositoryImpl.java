package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.adapter;

import com.whyboutrasseyt.miniproject.domain.model.Task;
import com.whyboutrasseyt.miniproject.domain.repository.TaskRepository;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.entity.TaskEntity;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.mapper.TaskMapper;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.repository.TaskJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;

    public TaskRepositoryImpl(TaskJpaRepository taskJpaRepository) {
        this.taskJpaRepository = taskJpaRepository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity saved = taskJpaRepository.save(TaskMapper.toEntity(task));
        return TaskMapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id).map(TaskMapper::toDomain);
    }

    @Override
    public List<Task> findByProjectId(Long projectId) {
        return taskJpaRepository.findByProjectId(projectId)
                .stream()
                .map(TaskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        taskJpaRepository.deleteById(id);
    }

    @Override
    public long countByProjectId(Long projectId) {
        return taskJpaRepository.countByProjectId(projectId);
    }

    @Override
    public long countCompletedByProjectId(Long projectId) {
        return taskJpaRepository.countByProjectIdAndCompletedTrue(projectId);
    }
}
