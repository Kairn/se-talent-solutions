package com.revature.sets.service;

import java.util.List;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.dao.ExecutiveDao;
import com.revature.sets.dao.ExecutiveDaoImpl;
import com.revature.sets.dao.ManagerDao;
import com.revature.sets.dao.ManagerDaoImpl;
import com.revature.sets.model.Employee;
import com.revature.sets.model.FileMeta;
import com.revature.sets.model.Request;
import com.revature.sets.model.Resolution;
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
	
	public String fetchRequestsAsEmployee(String idString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		List<Request> requests = ad.getRequestsByEmployeeId(Integer.parseInt(idString));
		
		if (requests != null) {
			if (requests.isEmpty()) {
				return new String("");
			}
			else {
				return UtilityManager.toJsonStringJackson(requests);
			}
		}
		else {
			return null;
		}
		
	}
	
	public String fetchPendingRequestsToBeResolved(int employeeId, int accessLevel) {
		
		AssociateDao ad = new AssociateDaoImpl();
		ManagerDao md = new ManagerDaoImpl();
		ExecutiveDao ed = new ExecutiveDaoImpl();
		
		List<Request> requests = null;
		
		if (accessLevel == 2) {
			requests = md.getPendingRequestsByManagerId(employeeId);
		}
		else if (accessLevel == 3) {
			requests = ed.getRequestsAsExecutive();
			requests.removeIf(r -> r.getResolution() != null);
			requests.removeIf(r -> r.getEmployeeId() == employeeId);
		}
		else {
			return null;
		}
		
		if (requests.isEmpty()) {
			return new String("");
		}
		else {
			for (Request r: requests) {
				r.setEmployeeName(ad.getFullNameByEmployeeId(r.getEmployeeId()));
			}
			return UtilityManager.toJsonStringJackson(requests);
		}
		
	}
	
	public String fetchFilesAttachedToRequest(int requestId) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		List<FileMeta> files = ad.getFilesByRequestId(requestId);
		
		if (files != null) {
			if (files.isEmpty()) {
				return new String("");
			}
			else {
				return UtilityManager.toJsonStringJackson(files);
			}
		}
		else {
			return null;
		}
		
	}
	
	public byte[] fetchFileData(int fileId) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		return ad.getFileById(fileId);
		
	}
	
	public String fetchResolvedRequests(int accessLevel) {
		
		AssociateDao ad = new AssociateDaoImpl();
		ManagerDao md = new ManagerDaoImpl();
		ExecutiveDao ed = new ExecutiveDaoImpl();
		
		List<Request> requests = null;
		
		if (accessLevel == 2) {
			requests = md.getResolvedRequestsAsManager();
		}
		else if (accessLevel == 3) {
			requests = ed.getRequestsAsExecutive();
			requests.removeIf(r -> r.getResolution() == null);
		}
		
		if (requests != null) {
			if (requests.isEmpty()) {
				return new String("");
			}
			else {
				for (Request r: requests) {
					r.setEmployeeName(ad.getFullNameByEmployeeId(r.getEmployeeId()));
					Resolution res = r.getResolution();
					res.setEmployeeName(ad.getFullNameByEmployeeId(res.getEmployeeId()));
					r.setResolution(res);
				}
				return UtilityManager.toJsonStringJackson(requests);
			}
		}
		else {
			return null;
		}
		
	}

}
