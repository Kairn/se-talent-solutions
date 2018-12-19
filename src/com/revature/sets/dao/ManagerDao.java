package com.revature.sets.dao;

import java.sql.Connection;
import java.util.List;

import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;

public interface ManagerDao {
	
	List<Employee> getEmployeesByManagerId(int managerId);
	int addEmployee(Employee newEmployee);
	int addNewCredentials(Connection conn, String newEmail);
	List<Request> getPendingRequestsByManagerId(int managerId);
	List<Request> getResolvedRequestsAsManager();
	int approveRequest(int requestId, int managerId);
	int denyRequest(int requestId, int managerId);

}
