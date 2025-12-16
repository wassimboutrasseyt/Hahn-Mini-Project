package com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.adapter;

import com.whyboutrasseyt.miniproject.domain.model.Project;
import com.whyboutrasseyt.miniproject.domain.repository.ProjectRepository;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.entity.ProjectEntity;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.mapper.ProjectMapper;
import com.whyboutrasseyt.miniproject.infrastructure.persistence.jpa.repository.ProjectJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;

    public ProjectRepositoryImpl(ProjectJpaRepository projectJpaRepository) {
        this.projectJpaRepository = projectJpaRepository;
    }

    @Override
    public Project save(Project project) {
        ProjectEntity saved = projectJpaRepository.save(ProjectMapper.toEntity(project));
        return ProjectMapper.toDomain(saved);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return projectJpaRepository.findById(id).map(ProjectMapper::toDomain);
    }

    @Override
    public List<Project> findByOwnerId(Long ownerId) {
        return projectJpaRepository.findByOwnerId(ownerId)
                .stream()
                .map(ProjectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        projectJpaRepository.deleteById(id);
    }
}
