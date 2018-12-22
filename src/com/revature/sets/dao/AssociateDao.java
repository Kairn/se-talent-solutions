package com.revature.sets.dao;

import java.io.InputStream;
import java.util.List;

import com.revature.sets.model.Employee;
import com.revature.sets.model.FileMeta;
import com.revature.sets.model.Request;

public interface AssociateDao {
	
	Employee getEmployeeByCredentials(String username, String password);
	Employee getEmployeeById(int employeeId);
	Employee getEmployeeByEmail(String email);
	String getFullNameByEmployeeId(int employeeId);
	List<Request> getRequestsByEmployeeId(int employeeId);
	Request getPendingRequestByEmployeeAndRequestId(int employeeId, int requestId);
	int submitRequest(Request request);
	int recallRequest(int requestId);
	int updateInformation(int employeeId, String firstName, String lastName, String email);
	int changeCredentials(int employeeId, String oldPassword, String newUsername, String newPassword);
	int attachFileToRequest(int requestId, String fileType, InputStream fileData);
	byte[] getFileById(int fileId);
	List<FileMeta> getFilesByRequestId(int requestId);
	int obtainNewCredentials(String username, String email);
	int getRequestIdByFileId(int fileId);

}
