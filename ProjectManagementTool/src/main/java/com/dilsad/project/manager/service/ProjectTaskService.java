package com.dilsad.project.manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dilsad.project.manager.entitiy.Backlog;
import com.dilsad.project.manager.entitiy.ProjectTask;
import com.dilsad.project.manager.exception.ProjectNotFoundException;
import com.dilsad.project.manager.repository.BacklogRepository;
import com.dilsad.project.manager.repository.ProjectTaskRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
    private ProjectService projectService;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){
		
		Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);
		
		projectTask.setBacklog(backlog);
		
		Integer BacklogSequence = backlog.getPTSequence();		
		BacklogSequence++;
		backlog.setPTSequence(BacklogSequence);
		
		projectTask.setProjectSequence(projectIdentifier+"-"+ BacklogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
		if(projectTask.getPriority() == null || projectTask.getPriority() == 0) projectTask.setPriority(3);
		
		if(projectTask.getStatus() == null || projectTask.getStatus().equals("") ) projectTask.setStatus("TO_DO");
		
		return projectTaskRepository.save(projectTask);
			
			
		
		
		
		
		
	}
	
	public List<ProjectTask> findByBacklogId(String projectIdentifier, String username) {
		
		//Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		
	
		//if(backlog == null) throw new ProjectNotFoundException("Project with ID "+ projectIdentifier+ " does not exist");
		 projectService.findProjectByIdentifier(projectIdentifier, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);

	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
		
		/*Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		
		if(backlog == null) throw new ProjectNotFoundException("Project with ID "+ backlog_id+ " does not exist");*/
		
		 projectService.findProjectByIdentifier(backlog_id, username);
		
		 ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

	     if(projectTask == null) throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
	        
		
	     if(!projectTask.getProjectIdentifier().equals(backlog_id)) throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
	        

	     return projectTask;
	}
	
	public ProjectTask updatePTByProjectSequence(ProjectTask updatedTask,String backlog_id, String pt_id, String username) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id,username);
		
		updatedTask.setBacklog(projectTask.getBacklog());	
		
	    projectTask = updatedTask;
	      
	    return projectTaskRepository.save(projectTask);
	}
	
	public void deleteProjectByProjectSequence(String backlog_id, String pt_id, String username) {

		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		 
		projectTaskRepository.delete(projectTask);
	}

}
