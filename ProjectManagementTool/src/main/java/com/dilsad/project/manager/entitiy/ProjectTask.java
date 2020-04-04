package com.dilsad.project.manager.entitiy;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ProjectTask {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY )
	private Long id;
	
	@Column(updatable = false, unique = true)
	private String projectSequence;
	
	@NotBlank(message = "Please include a project summary")
	private String summary;
	
	private String acceptanceCriteria;
	
	private String status;
	
	private Integer priority;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date dueDate;
	
	//ManyToOne with Backlog
	
	@Column(updatable = false)
	private String projectIdentifier;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date created_At;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date updated_At;
	
	@PrePersist
	protected void onCreate() {
		this.created_At = new Date();
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "backlog_id", updatable = false, nullable=false)
	@JsonIgnore //infinite recursion problem solving, apply on child !
	private Backlog backlog;
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_At = new Date();
	}
	
	@Override
	public String toString() {
		
		return "ProjectTask{" +
		"id=" +id +
		", projectSequence'" + projectSequence + '\'' +
		", summary='" + summary + '\'' +
		", acceptanceCriteria='" + acceptanceCriteria + '\'' +
		", status='" + status + '\'' +
		", priority='" + priority + '\'' +
		", dueDate='" + dueDate + '\'' +
		", projectIdentifier='" + projectIdentifier + '\'' +
		", created_At='" + created_At + '\'' +
		", updated_At='" + updated_At + '\'' +
		"}";
		
		
	}
	
}
