package com.revature.sets.dao;

import java.io.InputStream;
import java.util.List;

import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;

public interface AssociateDao {
	
	Employee getEmployeeByCredentials(String username, String password);
	Employee getEmployeeById(int employeeId);
	Employee getEmployeeByEmail(String email);
	List<Request> getRequestsByEmployeeId(int employeeId);
	Request getPendingRequestByEmployeeAndRequestId(int employeeId, int requestId);
	int submitRequest(Request request);
	int recallRequest(int requestId);
	int updateInformation(int employeeId, String firstName, String lastName, String email);
	int changeCredentials(int employeeId, String oldPassword, String newUsername, String newPassword);
	int attachFileToRequest(int requestId, String fileType, InputStream fileData);
	byte[] getFileById(int fileId);
	int obtainNewCredentials(String username, String email);

}
