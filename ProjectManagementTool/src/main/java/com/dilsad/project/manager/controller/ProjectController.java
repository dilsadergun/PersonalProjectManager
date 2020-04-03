package com.dilsad.project.manager.controller;


import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dilsad.project.manager.entitiy.Project;
import com.dilsad.project.manager.service.MapValidationErrorService;
import com.dilsad.project.manager.service.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	MapValidationErrorService mapValidationErrorService;
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping( produces = "application/json", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
	public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, 
													BindingResult result, Principal principal){
		
		ResponseEntity<?> errorMap =mapValidationErrorService.MapValidationService(result);
		
		if(errorMap !=null) return errorMap;
		
		Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
		return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
	}
	
	@RequestMapping( value= "/{projectId}",produces = "application/json", method = {RequestMethod.GET, RequestMethod.PUT})
	public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
		
		Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
		
		return new ResponseEntity<Project>(project, HttpStatus.OK);

	}
	
	@RequestMapping( value= "/all",produces = "application/json", method = {RequestMethod.GET, RequestMethod.PUT})
	public Iterable<Project> getAllProjects(Principal principal){return projectService.findAllProjects(principal.getName());}
	
	@RequestMapping( value= "/{projectId}",produces = "application/json", method = {RequestMethod.DELETE})
	public ResponseEntity<?> deleteProjectById(@PathVariable String projectId, Principal principal){
		
		projectService.deleteProjectByIdentifier(projectId, principal.getName());
		
		return new ResponseEntity<String>("Project with ID: "+ projectId.toUpperCase() + " deleted", HttpStatus.OK);
		
	}

}
