package com.dilsad.project.manager.entitiy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Backlog {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY )
	private Long id;
	
	private Integer PTSequence=0;
	
	private String projectIdentifier;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project_id", nullable=false)
	@JsonIgnore //infinite recursion problem solving, apply on child !
	private Project project;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "backlog")
	private List<ProjectTask> projectTasks = new ArrayList<>();
	
	
	

}
