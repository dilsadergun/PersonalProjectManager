package com.dilsad.project.manager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dilsad.project.manager.entitiy.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	Project findByProjectIdentifier(String projectIdentifier);
	
	
	Iterable<Project> findAll();
	
	 Iterable<Project> findAllByProjectLeader(String username);

}
