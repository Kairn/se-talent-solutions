package com.revature.sets.model;

public class FileMeta {
	
	private int fileId;
	private String fileType;

	public FileMeta() {
		super();
	}

	public FileMeta(int fileId, String fileType) {
		super();
		this.fileId = fileId;
		this.fileType = fileType;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getFileType() {
		return fileType.toLowerCase();
	}

	public void setFileType(String fileType) {
		this.fileType = fileType.toLowerCase();
	}

}
