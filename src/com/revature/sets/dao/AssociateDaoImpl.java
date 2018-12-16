package com.revature.sets.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;
import com.revature.sets.utility.UtilityManager;

public class AssociateDaoImpl implements AssociateDao {

	public AssociateDaoImpl() {
		super();
	}

	@Override
	public Employee getEmployeeByCredentials(String username, String password) {
		
		Employee employee = null;
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT * FROM EMPLOYEE\r\n" + 
				"WHERE EMPLOYEE_ID = (\r\n" + 
				"	SELECT EMPLOYEE_ID FROM CREDENTIALS\r\n" + 
				"	WHERE USERNAME = ? AND PASSWORD_HASH = ?\r\n" + 
				")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, username);
			pstmt.setString(2, UtilityManager.digestSHA256(password));
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				employee = new Employee(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("EMAIL"), rs.getInt("UP_GROUP"), rs.getInt("DOWN_GROUP"), rs.getInt("ACCESS_LEVEL"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employee;
		
	}

	@Override
	public Employee getEmployeeById(int employeeId) {

		Employee employee = null;
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT * FROM EMPLOYEE\r\n" + 
				"WHERE EMPLOYEE_ID = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, employeeId);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				employee = new Employee(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("EMAIL"), rs.getInt("UP_GROUP"), rs.getInt("DOWN_GROUP"), rs.getInt("ACCESS_LEVEL"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employee;
		
	}

	@Override
	public Employee getEmployeeByEmail(String email) {

		Employee employee = null;
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT * FROM EMPLOYEE\r\n" + 
				"WHERE EMAIL = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, email);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				employee = new Employee(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("EMAIL"), rs.getInt("UP_GROUP"), rs.getInt("DOWN_GROUP"), rs.getInt("ACCESS_LEVEL"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employee;
		
	}

	@Override
	public List<Request> getRequestsByEmployeeId(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int submitRequest(Request request) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int recallRequest(int requestId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateInformation(int employeeId, String firstName, String lastName, String email) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int changeCredentials(int employeeId, String oldPassword, String newUsername, String newPassword) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int attachFileToRequest(int requestId, String fileType, InputStream fileData) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getFileById(int fileId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int obtainNewCredentials(String username, String email) {
		// TODO Auto-generated method stub
		return 0;
	}

}
