package com.revature.sets.model;

import java.sql.Date;

public class Resolution {
	
	private int resolutionId;
	private int requestId;
	private int status;
	private int employeeId;
	private String employeeName;
	private Date resolutionDate;
	
	public Resolution(int resolutionId) {
		super();
		this.resolutionId = resolutionId;
	}

	public Resolution(int requestId, int status, int employeeId, Date resolutionDate) {
		super();
		this.requestId = requestId;
		this.status = status;
		this.employeeId = employeeId;
		this.resolutionDate = resolutionDate;
	}

	public Resolution(int status, String employeeName, Date resolutionDate) {
		super();
		this.status = status;
		this.employeeName = employeeName;
		this.resolutionDate = resolutionDate;
	}

	public Resolution(int status, int employeeId, String employeeName, Date resolutionDate) {
		super();
		this.status = status;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.resolutionDate = resolutionDate;
	}

	public Resolution(int resolutionId, int requestId, int status, int employeeId, Date resolutionDate) {
		super();
		this.resolutionId = resolutionId;
		this.requestId = requestId;
		this.status = status;
		this.employeeId = employeeId;
		this.resolutionDate = resolutionDate;
	}

	public int getResolutionId() {
		return resolutionId;
	}

	public void setResolutionId(int resolutionId) {
		this.resolutionId = resolutionId;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(Date resolutionDate) {
		this.resolutionDate = resolutionDate;
	}

	@Override
	public String toString() {
		return "Resolution [resolutionId=" + resolutionId + ", requestId=" + requestId + ", status=" + status
				+ ", employeeId=" + employeeId + ", resolutionDate=" + resolutionDate + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resolution other = (Resolution) obj;
		if (resolutionId != other.resolutionId)
			return false;
		return true;
	}

}
