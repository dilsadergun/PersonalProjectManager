package com.dilsad.project.manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dilsad.project.manager.entitiy.Backlog;
import com.dilsad.project.manager.entitiy.ProjectTask;
import com.dilsad.project.manager.exception.ProjectNotFoundException;
import com.dilsad.project.manager.repository.BacklogRepository;
import com.dilsad.project.manager.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		
		try {
			projectTask.setBacklog(backlog);
			
			Integer BacklogSequence = backlog.getPTSequence();		
			BacklogSequence++;
			backlog.setPTSequence(BacklogSequence);
			
			projectTask.setProjectSequence(projectIdentifier+"-"+ BacklogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			
			if(projectTask.getPriority() == null || projectTask.getPriority() == 0) projectTask.setPriority(3);
			
			if(projectTask.getStatus() == null || projectTask.getStatus().equals("") ) projectTask.setStatus("TO_DO");
			
			return projectTaskRepository.save(projectTask);
			
		}catch(Exception e) {
			throw new ProjectNotFoundException("Project not found");
		}
		
		
		
		
	}
	
	public List<ProjectTask> findByBacklogId(String projectIdentifier) {
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		
	
		if(backlog == null) throw new ProjectNotFoundException("Project with ID "+ projectIdentifier+ " does not exist");
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);

	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		
		if(backlog == null) throw new ProjectNotFoundException("Project with ID "+ backlog_id+ " does not exist");
		
		 ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

	     if(projectTask == null) throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
	        
		
	     if(!projectTask.getProjectIdentifier().equals(backlog_id)) throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
	        

	     return projectTask;
	}
	
	public ProjectTask updatePTByProjectSequence(ProjectTask updatedTask,String backlog_id, String pt_id) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
		
	    projectTask = updatedTask;
	    
	    return projectTaskRepository.save(projectTask);
	}
	
	public void deleteProjectByProjectSequence(String backlog_id, String pt_id) {

		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
		 
		projectTaskRepository.delete(projectTask);
	}

}
