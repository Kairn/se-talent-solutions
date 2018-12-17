package com.revature.sets.model;

public class RestfulResponse {
	
	private int status;
	private String content;

	public RestfulResponse() {
		super();
	}

	public RestfulResponse(int status) {
		super();
		this.status = status;
	}

	public RestfulResponse(int status, String content) {
		super();
		this.status = status;
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
