package com.revature.sets.dao;

import java.util.List;

import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;

public interface ExecutiveDao {
	
	List<Employee> getEmployeesAsExecutive();
	List<Request> getPendingRequestsAsExecutive();
	int changeEmployeeRole(int employeeId, int newUpGroup, int newDownGroup, int newAccessLevel);
	int deleteEmployee(int employeeId);

}
