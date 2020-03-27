package com.dilsad.project.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dilsad.project.manager.entitiy.Project;
import com.dilsad.project.manager.exception.ProjectIdException;
import com.dilsad.project.manager.repository.ProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public Project saveOrUpdateProject(Project project) {
		
		try {
			
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			return projectRepository.save(project);

			
		} catch (Exception e) {
			throw new ProjectIdException("Project ID '"+ project.getProjectIdentifier().toUpperCase() +"' already exists");
		}
	}
	
	public Project findProjectByIdentifier(String projectId) {
		
		Project project =projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) throw new ProjectIdException("Project ID '"+ projectId.toUpperCase() +"' does not exist");
		
		return project;
	}
	
	public Iterable<Project> findAllProjects(){
		
		return projectRepository.findAll();
	}
	
	public void deleteProjectByIdentifier(String projectId) {
		
		Project project =projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project ==null) throw new ProjectIdException("No project with ID '"+ projectId.toUpperCase() +"' exists");
		
		projectRepository.delete(project);
	}
	


}
