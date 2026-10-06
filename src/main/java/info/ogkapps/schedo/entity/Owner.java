package info.ogkapps.schedo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "owners")
public class Owner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "owner_id")
	Integer ownerId;
	
	@Column(name = "owner_name")
	String ownerName;
	
	@Column(name = "owner_email")
	String ownerEmail;
	
	@Column(name = "owner_catagory")
	String ownerCatagory;
	
	@Column(name = "owner_time_span")
	Long ownerTimeSpan;
	
	@Column(name = "owner_start_time")
	Long ownerStartTime;
	
	@Column(name = "owner_end_time")
	Long ownerEndTime;
	
	@Column(name = "owner_start_break")
	Long ownerStartBreak;
	
	@Column(name = "owner_end_break")
	Long ownerEndBreak;
	
	@Column(name = "owner_created_at")
	Long ownerCreatedAt;
	
	@Column(name = "owner_updated_at")
	Long ownerUpdatedAt;

	public Owner() {
		super();
	}

	public Owner(String ownerName, String ownerEmail, String ownerCatagory, Long ownerCreatedAt, Long ownerUpdatedAt) {
		super();
		this.ownerName = ownerName;
		this.ownerEmail = ownerEmail;
		this.ownerCatagory = ownerCatagory;
		this.ownerCreatedAt = ownerCreatedAt;
		this.ownerUpdatedAt = ownerUpdatedAt;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getOwnerCatagory() {
		return ownerCatagory;
	}

	public void setOwnerCatagory(String ownerCatagory) {
		this.ownerCatagory = ownerCatagory;
	}

	public Long getOwnerTimeSpan() {
		return ownerTimeSpan;
	}

	public void setOwnerTimeSpan(Long ownerTimeSpan) {
		this.ownerTimeSpan = ownerTimeSpan;
	}

	public Long getOwnerStartTime() {
		return ownerStartTime;
	}

	public void setOwnerStartTime(Long ownerStartTime) {
		this.ownerStartTime = ownerStartTime;
	}

	public Long getOwnerEndTime() {
		return ownerEndTime;
	}

	public void setOwnerEndTime(Long ownerEndTime) {
		this.ownerEndTime = ownerEndTime;
	}

	public Long getOwnerStartBreak() {
		return ownerStartBreak;
	}

	public void setOwnerStartBreak(Long ownerStartBreak) {
		this.ownerStartBreak = ownerStartBreak;
	}

	public Long getOwnerEndBreak() {
		return ownerEndBreak;
	}

	public void setOwnerEndBreak(Long ownerEndBreak) {
		this.ownerEndBreak = ownerEndBreak;
	}

	public Long getOwnerCreatedAt() {
		return ownerCreatedAt;
	}

	public void setOwnerCreatedAt(Long ownerCreatedAt) {
		this.ownerCreatedAt = ownerCreatedAt;
	}

	public Long getOwnerUpdatedAt() {
		return ownerUpdatedAt;
	}

	public void setOwnerUpdatedAt(Long ownerUpdatedAt) {
		this.ownerUpdatedAt = ownerUpdatedAt;
	}

	@Override
	public String toString() {
		return "Owner [ownerName=" + ownerName + ", ownerEmail=" + ownerEmail + ", ownerCatagory=" + ownerCatagory
				+ "]";
	}
	
}
