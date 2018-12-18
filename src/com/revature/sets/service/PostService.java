package com.revature.sets.service;

import org.json.JSONObject;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.model.Employee;
import com.revature.sets.utility.UtilityManager;

public class PostService {

	public PostService() {
		super();
	}
	
	public String fetchEmployeeJsonWithCredentials(String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		JSONObject jo = new JSONObject(jsonString);
		String username = jo.getString("username");
		String password = jo.getString("password");
		
		if (username.isEmpty() || password.isEmpty()) {
			return null;
		}
		
		Employee employee = ad.getEmployeeByCredentials(username, password);
		if (employee != null) {
			return UtilityManager.toJsonStringJackson(employee);
		}
		else {
			return null;
		}
		
	}
	
	public boolean updateEmployeeInformation(int employeeId, String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		JSONObject jo = new JSONObject(jsonString);
		String newFirstName = jo.getString("newFirstName");
		String newLastName = jo.getString("newLastName");
		String newEmail = jo.getString("newEmail");
		
		if (newFirstName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty()) {
			return false;
		}
		else {
			if (ad.updateInformation(employeeId, newFirstName, newLastName, newEmail) != 0) {
				return true;
			}
			else {
				return false;
			}
		}
		
	}
	
	public boolean changeEmployeeCredentials(int employeeId, String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		JSONObject jo = new JSONObject(jsonString);
		String oldPassword = jo.getString("oldPassword");
		String newUsername = jo.getString("newUsername");
		String newPassword1 = jo.getString("newPassword1");
		String newPassword2 = jo.getString("newPassword2");
		
		if (oldPassword.isEmpty() || newPassword1.isEmpty() || newPassword2.isEmpty()) {
			return false;
		}
		else if (!newPassword1.equals(newPassword2)) {
			return false;
		}
		else {
			if (ad.changeCredentials(employeeId, oldPassword, newUsername, newPassword1) != 0) {
				return true;
			}
			else {
				return false;
			}
		}
		
	}

}
