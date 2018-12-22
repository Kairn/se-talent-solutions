package com.revature.sets.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;
import com.revature.sets.model.Resolution;
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
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
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
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
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
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return employee;
		
	}

	@Override
	public String getFullNameByEmployeeId(int employeeId) {
		
		String employeeName = null;
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT FIRSTNAME, LASTNAME FROM EMPLOYEE\r\n" + 
				"WHERE EMPLOYEE_ID = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, employeeId);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				employeeName = (new String(rs.getString("FIRSTNAME"))).concat(" ").concat(rs.getString("LASTNAME"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return employeeName;
		
	}

	@Override
	public List<Request> getRequestsByEmployeeId(int employeeId) {
		
		List<Request> requests = new ArrayList<>();
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT REQ.REQUEST_ID AS REQ_ID, REQ.EMPLOYEE_ID AS E_ID, REQ.REQUEST_DATE AS REQ_DATE, REQ.REASON, REQ.MESSAGE, REQ.AMOUNT, RES.RESOLUTION_ID AS RES_ID, RES.STATUS, RES.EMPLOYEE_ID AS M_ID, RES.RESOLUTION_DATE AS RES_DATE\r\n" + 
				"FROM REQUEST REQ\r\n" + 
				"LEFT JOIN RESOLUTION RES\r\n" + 
				"ON REQ.REQUEST_ID = RES.REQUEST_ID\r\n" + 
				"WHERE REQ.EMPLOYEE_ID = (\r\n" + 
				"	SELECT EMPLOYEE_ID FROM EMPLOYEE\r\n" + 
				"	WHERE EMPLOYEE_ID = ?\r\n" + 
				")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, employeeId);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("RES_ID") == 0) {
					requests.add(new Request(rs.getInt("REQ_ID"), rs.getInt("E_ID"), rs.getDate("REQ_DATE"), rs.getString("REASON"), rs.getString("MESSAGE"), rs.getDouble("AMOUNT")));
				}
				else {
					Resolution resolution = new Resolution(rs.getInt("RES_ID"), rs.getInt("REQ_ID"), rs.getInt("STATUS"), rs.getInt("M_ID"), rs.getDate("RES_DATE"));
					requests.add(new Request(rs.getInt("REQ_ID"), rs.getInt("E_ID"), rs.getDate("REQ_DATE"), rs.getString("REASON"), rs.getString("MESSAGE"), rs.getDouble("AMOUNT"), resolution));
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return requests;
		
	}

	@Override
	public Request getPendingRequestByEmployeeAndRequestId(int employeeId, int requestId) {
		
		Request request = null;
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT * FROM REQUEST\r\n" + 
				"WHERE EMPLOYEE_ID = ? AND REQUEST_ID = ? AND REQUEST_ID NOT IN (\r\n" + 
				"	SELECT REQUEST_ID FROM RESOLUTION\r\n" + 
				")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, employeeId);
			pstmt.setInt(2, requestId);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				request = new Request(requestId, employeeId, rs.getDate("REQUEST_DATE"), rs.getString("REASON"), rs.getString("MESSAGE"), rs.getDouble("AMOUNT"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return request;
		
	}

	@Override
	public int submitRequest(Request request) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "INSERT INTO REQUEST (EMPLOYEE_ID, REQUEST_DATE, REASON, MESSAGE, AMOUNT)\r\n" + 
				"VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, request.getEmployeeId());
			pstmt.setDate(2, new Date(Calendar.getInstance().getTimeInMillis()));
			pstmt.setString(3, request.getReason());
			pstmt.setString(4, request.getMessage());
			pstmt.setDouble(5, request.getAmount());
			return pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
		
	}

	@Override
	public int recallRequest(int requestId) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "DELETE FROM REQUEST\r\n" + 
				"WHERE REQUEST_ID = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, requestId);
			return pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
		
	}

	@Override
	public int updateInformation(int employeeId, String firstName, String lastName, String email) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "UPDATE EMPLOYEE\r\n" + 
				"SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?\r\n" + 
				"WHERE EMPLOYEE_ID = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, email);
			pstmt.setInt(4, employeeId);
			return pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
		
	}

	@Override
	public int changeCredentials(int employeeId, String oldPassword, String newUsername, String newPassword) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "UPDATE CREDENTIALS\r\n" + 
				"SET USERNAME = ?, PASSWORD_HASH = ?\r\n" + 
				"WHERE EMPLOYEE_ID = ? AND PASSWORD_HASH = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, newUsername);
			pstmt.setString(2, UtilityManager.digestSHA256(newPassword));
			pstmt.setInt(3, employeeId);
			pstmt.setString(4, UtilityManager.digestSHA256(oldPassword));
			return pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
		
	}

	@Override
	public int attachFileToRequest(int requestId, String fileType, InputStream fileData) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "INSERT INTO IMAGEFILE (FILE_DATA, FILE_TYPE, REQUEST_ID)\r\n" + 
				"VALUES (?, ?, ?)";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setBlob(1, fileData);
			pstmt.setString(2, fileType);
			pstmt.setInt(3, requestId);
			return pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
		
	}

	@Override
	public byte[] getFileById(int fileId) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT FILE_DATA FROM IMAGEFILE\r\n" + 
				"WHERE FILE_ID = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, fileId);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getBytes("FILE_DATA");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return null;
		
	}

	@Override
	public int obtainNewCredentials(String username, String email) {
		
		final String newUsername = RandomStringUtils.randomAlphanumeric(8);
		final String newPassword = RandomStringUtils.randomAlphanumeric(12);
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "UPDATE CREDENTIALS\r\n" + 
				"SET USERNAME = ?, PASSWORD_HASH = ?\r\n" + 
				"WHERE USERNAME = ? AND EMPLOYEE_ID = (\r\n" + 
				"	SELECT EMPLOYEE_ID FROM EMPLOYEE\r\n" + 
				"	WHERE EMAIL = ?\r\n" + 
				")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, newUsername);
			pstmt.setString(2, UtilityManager.digestSHA256(newPassword));
			pstmt.setString(3, username);
			pstmt.setString(4, email);
			
			if (pstmt.executeUpdate() > 0) {
				final String subject = "IMPORTANT: YOUR NEW SETS CREDENTIALS!";
				final String content = String.format("YOUR NEW USERNAME: %s%nYOUR NEW PASSWORD: %s%n", newUsername, newPassword);
				UtilityManager.sendEmail(email, subject, content);
				return 1;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
		
	}

	@Override
	public int getRequestIdByFileId(int fileId) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT REQUEST_ID FROM IMAGEFILE\r\n" + 
				"WHERE FILE_ID = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, fileId);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("REQUEST_ID");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
		
	}

}
