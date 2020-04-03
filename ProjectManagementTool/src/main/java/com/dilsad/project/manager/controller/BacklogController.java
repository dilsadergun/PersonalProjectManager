package com.dilsad.project.manager.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dilsad.project.manager.entitiy.ProjectTask;
import com.dilsad.project.manager.service.MapValidationErrorService;
import com.dilsad.project.manager.service.ProjectTaskService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/backlog")
@Slf4j
public class BacklogController {
	
	@Autowired
	private ProjectTaskService projectTaskService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@RequestMapping( value= "/{backlog_id}",produces = "application/json", method = { RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<?> getProjectById(@Valid @RequestBody ProjectTask projectTask, 
			BindingResult result,@PathVariable String backlog_id, Principal principal){
		
		ResponseEntity<?> errorMap =mapValidationErrorService.MapValidationService(result);
		
		if(errorMap != null) return errorMap;
		
		ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());
		
		
		return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

  
	}	
	
	@RequestMapping( value= "/{backlog_id}",produces = "application/json", method = {RequestMethod.GET})
	public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id, Principal principal){
		
		return (projectTaskService.findByBacklogId(backlog_id, principal.getName()));
	}
	
	@RequestMapping( value= "/{backlog_id}/{pt_id}",produces = "application/json", method = {RequestMethod.GET})
	public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal ){
		
		ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, pt_id, principal.getName());
		
		return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
	}
	
	@RequestMapping( value= "/{backlog_id}/{pt_id}",produces = "application/json", method = {RequestMethod.PATCH})
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
												@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal ){
		
		ResponseEntity<?> errorMap =mapValidationErrorService.MapValidationService(result);
		
		if(errorMap != null) return errorMap;
				
		ProjectTask updatedTask= projectTaskService.updatePTByProjectSequence(projectTask, backlog_id, pt_id, principal.getName());
		
		return new ResponseEntity<ProjectTask>(updatedTask, HttpStatus.OK);
	}
	
	@RequestMapping( value= "/{backlog_id}/{pt_id}",produces = "application/json", method = {RequestMethod.DELETE})
	public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal ){
		
		projectTaskService.deleteProjectByProjectSequence(backlog_id, pt_id, principal.getName()); 
		
		return new ResponseEntity<String>("Project task with ID: "+ pt_id + " deleted", HttpStatus.OK);
	}
	
}
