package com.dilsad.project.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dilsad.project.manager.entitiy.Backlog;
import com.dilsad.project.manager.entitiy.Project;
import com.dilsad.project.manager.entitiy.User;
import com.dilsad.project.manager.exception.ProjectIdException;
import com.dilsad.project.manager.exception.ProjectNotFoundException;
import com.dilsad.project.manager.repository.BacklogRepository;
import com.dilsad.project.manager.repository.ProjectRepository;
import com.dilsad.project.manager.repository.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	 @Autowired
	 private UserRepository userRepository;

	 public Project saveOrUpdateProject(Project project, String username){
		 
		 if(project.getId() != null){
	            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
	            if(existingProject !=null &&(!existingProject.getProjectLeader().equals(username))){
	                throw new ProjectNotFoundException("Project not found in your account");
	            }else if(existingProject == null){
	                throw new ProjectNotFoundException("Project with ID: '"+project.getProjectIdentifier()+"' cannot be updated because it doesn't exist");
	            }
	        }
		 
		 try{
			 User user = userRepository.findByUsername(username);
			 String projectIdentifier =project.getProjectIdentifier().toUpperCase();
	         project.setUser(user);
	         project.setProjectLeader(user.getUsername());
			
			project.setProjectIdentifier(projectIdentifier);
			
			if(project.getId()==null) {
				
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(projectIdentifier);
			} else {
				project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
			}
			return projectRepository.save(project);

			
		} catch (Exception e) {
			throw new ProjectIdException("Project ID '"+ project.getProjectIdentifier().toUpperCase() +"' already exists");
		}
	}
	
	public Project findProjectByIdentifier(String projectId, String username) {
		
		Project project =projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) throw new ProjectIdException("Project ID '"+ projectId.toUpperCase() +"' does not exist");
		
		if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }
		
		return project;
	}
	
	public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }
	
	public void deleteProjectByIdentifier(String projectId, String username){
		
		//Project project =projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		//if(project ==null) throw new ProjectIdException("No project with ID '"+ projectId.toUpperCase() +"' exists");
		
		 projectRepository.delete(findProjectByIdentifier(projectId, username));
	}
	


}
