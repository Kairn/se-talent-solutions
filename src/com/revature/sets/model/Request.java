package com.revature.sets.model;

import java.sql.Date;

public class Request {
	
	private int requestId;
	private int employeeId;
	private Date requestDate;
	private String reason;
	private String message;
	private double amount;
	private Resolution resolution;
	
	public Request(int requestId) {
		super();
		this.requestId = requestId;
	}

	public Request(int requestId, int employeeId, Date requestDate, double amount) {
		super();
		this.requestId = requestId;
		this.employeeId = employeeId;
		this.requestDate = requestDate;
		this.amount = amount;
	}

	public Request(int employeeId, Date requestDate, String reason, String message, double amount) {
		super();
		this.employeeId = employeeId;
		this.requestDate = requestDate;
		this.reason = reason;
		this.message = message;
		this.amount = amount;
	}

	public Request(int requestId, int employeeId, Date requestDate, String reason, String message, double amount) {
		super();
		this.requestId = requestId;
		this.employeeId = employeeId;
		this.requestDate = requestDate;
		this.reason = reason;
		this.message = message;
		this.amount = amount;
	}

	public Request(int requestId, int employeeId, Date requestDate, String reason, String message, double amount,
			Resolution resolution) {
		super();
		this.requestId = requestId;
		this.employeeId = employeeId;
		this.requestDate = requestDate;
		this.reason = reason;
		this.message = message;
		this.amount = amount;
		this.resolution = resolution;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Resolution getResolution() {
		return resolution;
	}

	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}

	@Override
	public String toString() {
		return "Request [requestId=" + requestId + ", employeeId=" + employeeId + ", requestDate=" + requestDate
				+ ", reason=" + reason + ", message=" + message + ", amount=" + amount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + requestId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (requestId != other.requestId)
			return false;
		return true;
	}

}
