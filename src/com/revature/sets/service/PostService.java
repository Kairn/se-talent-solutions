package com.revature.sets.service;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.dao.ManagerDao;
import com.revature.sets.dao.ManagerDaoImpl;
import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;
import com.revature.sets.utility.UtilityManager;

public class PostService {

	public PostService() {
		super();
	}
	
	public String fetchEmployeeJsonWithCredentials(String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		try {
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
		catch (JSONException e) {
			return null;
		}
		
	}
	
	public boolean updateEmployeeInformation(int employeeId, String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		try {
			JSONObject jo = new JSONObject(jsonString);
			String newFirstName = jo.getString("newFirstName");
			newFirstName = newFirstName.substring(0, 1).toUpperCase().concat((newFirstName.substring(1)));
			String newLastName = jo.getString("newLastName");
			newLastName = newLastName.substring(0, 1).toUpperCase().concat((newLastName.substring(1)));
			String newEmail = jo.getString("newEmail").toLowerCase();
			
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
		catch (JSONException e) {
			return false;
		}
		
	}
	
	public boolean changeEmployeeCredentials(int employeeId, String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		try {
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
		catch (JSONException e) {
			return false;
		}
		
	}
	
	public boolean obtainNewEmployeeCredentials(String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		try {
			JSONObject jo = new JSONObject(jsonString);
			String username = jo.getString("username");
			String email = jo.getString("email").toLowerCase();
			
			if (username.isEmpty() || email.isEmpty()) {
				return false;
			}
			else {
				if (ad.obtainNewCredentials(username, email) != 0) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		catch (JSONException e) {
			return false;
		}
		
	}
	
	public boolean registerNewEmployee(String jsonString) {
		
		ManagerDao md = new ManagerDaoImpl();
		
		try {
			JSONObject jo = new JSONObject(jsonString);
			String firstName = jo.getString("firstName");
			firstName = firstName.substring(0, 1).toUpperCase().concat((firstName.substring(1)));
			String lastName = jo.getString("lastName");
			lastName = lastName.substring(0, 1).toUpperCase().concat((lastName.substring(1)));
			String email = jo.getString("email").toLowerCase();
			int upGroup = jo.getInt("upGroup");
			
			if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || upGroup < 100) {
				return false;
			}
			else {
				Employee employee = new Employee(firstName, lastName, email, upGroup);
				if (md.addEmployee(employee) == 0) {
					return false;
				}
				else {
					return true;
				}
			}
		}
		catch (JSONException e) {
			return false;
		}
		
	}
	
	public boolean submitNewRequest(int employeeId, String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		try {
			JSONObject jo = new JSONObject(jsonString);
			String reason = jo.getString("reason");
			reason = reason.substring(0, 1).toUpperCase().concat(reason.substring(1));
			String message = jo.getString("message");
			double amount = jo.getDouble("amount");
			
			if (reason.isEmpty() || message.isEmpty() || amount < 1) {
				return false;
			}
			else {
				Request request = new Request(employeeId, reason, message, amount);
				if (ad.submitRequest(request) == 0) {
					return false;
				}
				else {
					return true;
				}
			}
		}
		catch (JSONException e) {
			return false;
		}
		
	}
	
	public boolean uploadImageFileToRequest(int requestId, String imageType, InputStream image) {
		
		AssociateDao ad= new AssociateDaoImpl();
		
		if (ad.attachFileToRequest(requestId, imageType, image) == 0) {
			return false;
		}
		else {
			return true;
		}
		
	}

}
