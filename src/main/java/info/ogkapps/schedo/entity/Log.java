package info.ogkapps.schedo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "logs")
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	Integer logId;
	
	@Column(name = "log_owner_id")
	Integer logOwnerId;
	
	@Column(name = "log_visitor_name")
	String logVisitorName;
	
	@Column(name = "log_visitor_email")
	String logVisitorEmail;
	
	@Column(name = "log_visitor_time")
	Long logVisitorTime;
	
	@Column(name = "log_created_at")
	Long logCreatedAt;

	public Log() {
		super();
	}

	public Log(Integer logOwnerId, String logVisitorName, String logVisitorEmail, Long logVisitorTime,
			Long logCreatedAt) {
		super();
		this.logOwnerId = logOwnerId;
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

	public Integer getLogOwnerId() {
		return logOwnerId;
	}

	public void setLogOwnerId(Integer logOwnerId) {
		this.logOwnerId = logOwnerId;
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

	@Override
	public String toString() {
		return "Log [logId=" + logId + ", logOwnerId=" + logOwnerId + ", logVisitorName=" + logVisitorName
				+ ", logVisitorTime=" + logVisitorTime + "]";
	}
	
}
