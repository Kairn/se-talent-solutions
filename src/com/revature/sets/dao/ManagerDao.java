package com.revature.sets.dao;

import java.util.List;

import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;

public interface ManagerDao {
	
	List<Employee> getEmployeesByManagerId(int managerId);
	int addEmployee(Employee newEmployee);
	List<Request> getPendingRequestsByManagerId(int managerId);
	List<Request> getResolvedRequestsAsManager();
	int approveRequest(int requestId);
	int denyRequest(int requestId);

}
