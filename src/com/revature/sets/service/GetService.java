package com.revature.sets.service;

import java.util.List;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.dao.ExecutiveDao;
import com.revature.sets.dao.ExecutiveDaoImpl;
import com.revature.sets.dao.ManagerDao;
import com.revature.sets.dao.ManagerDaoImpl;
import com.revature.sets.model.Employee;
import com.revature.sets.utility.UtilityManager;

public class GetService {

	public GetService() {
		super();
	}
	
	public String fetchEmployeeJsonWithSession(String idString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		Employee employee = ad.getEmployeeById(Integer.parseInt(idString));
		
		if (employee != null) {
			return UtilityManager.toJsonStringJackson(employee);
		}
		
		return null;
		
	}
	
	public String fetchJuniorEmployeesAsManager(String idString) {
		
		ManagerDao md = new ManagerDaoImpl();
		
		List<Employee> employees = md.getEmployeesByManagerId(Integer.parseInt(idString));
		
		if (employees != null) {
			if (employees.isEmpty()) {
				return new String("");
			}
			else {
				return UtilityManager.toJsonStringJackson(employees);
			}
		}
		else {
			return null;
		}
		
	}
	
	public String fetchJuniorEmployeesAsExecutive() {
		
		ExecutiveDao ed = new ExecutiveDaoImpl();
		
		List<Employee> employees = ed.getEmployeesAsExecutive();
		
		if (employees != null) {
			if (employees.isEmpty()) {
				return new String("");
			}
			else {
				return UtilityManager.toJsonStringJackson(employees);
			}
		}
		else {
			return null;
		}
		
	}

}
