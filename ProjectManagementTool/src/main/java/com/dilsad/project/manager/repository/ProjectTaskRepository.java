package com.dilsad.project.manager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dilsad.project.manager.entitiy.ProjectTask;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
	
	List<ProjectTask> findByProjectIdentifierOrderByPriority(String projectIdentifier);
	
	ProjectTask findByProjectSequence(String sequence);

}
