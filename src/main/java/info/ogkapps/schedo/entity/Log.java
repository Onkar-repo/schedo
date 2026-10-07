package info.ogkapps.schedo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "logs")
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	private Integer logId;
	
	//@Column(name = "log_owner_id")
	//private Integer logOwnerId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "log_owner_id", nullable = false)
	private Owner owner;
	
	@Column(name = "log_visitor_name")
	private String logVisitorName;
	
	@Column(name = "log_visitor_email")
	private String logVisitorEmail;
	
	@Column(name = "log_visitor_time")
	private Long logVisitorTime;
	
	@Column(name = "log_created_at")
	private Long logCreatedAt;

	public Log() {
		super();
	}

	public Log(Integer logId, Owner owner, String logVisitorName, String logVisitorEmail, Long logVisitorTime,
			Long logCreatedAt) {
		super();
		this.logId = logId;
		this.owner = owner;
		this.logVisitorName = logVisitorName;
		this.logVisitorEmail = logVisitorEmail;
		this.logVisitorTime = logVisitorTime;
		this.logCreatedAt = logCreatedAt;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getLogVisitorName() {
		return logVisitorName;
	}

	public void setLogVisitorName(String logVisitorName) {
		this.logVisitorName = logVisitorName;
	}

	public String getLogVisitorEmail() {
		return logVisitorEmail;
	}

	public void setLogVisitorEmail(String logVisitorEmail) {
		this.logVisitorEmail = logVisitorEmail;
	}

	public Long getLogVisitorTime() {
		return logVisitorTime;
	}

	public void setLogVisitorTime(Long logVisitorTime) {
		this.logVisitorTime = logVisitorTime;
	}

	public Long getLogCreatedAt() {
		return logCreatedAt;
	}

	public void setLogCreatedAt(Long logCreatedAt) {
		this.logCreatedAt = logCreatedAt;
	}
	
}
