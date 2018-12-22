package com.revature.sets.dao;

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

public class ManagerDaoImpl implements ManagerDao {

	public ManagerDaoImpl() {
		super();
	}

	@Override
	public List<Employee> getEmployeesByManagerId(int managerId) {

		List<Employee> employees = new ArrayList<>();

		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT * FROM EMPLOYEE\r\n" + "WHERE UP_GROUP <> -1 AND UP_GROUP = (\r\n"
				+ "	SELECT DOWN_GROUP FROM EMPLOYEE\r\n" + "	WHERE EMPLOYEE_ID = ? AND DOWN_GROUP <> -1\r\n" + ")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, managerId);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				employees.add(new Employee(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRSTNAME"),
						rs.getString("LASTNAME"), rs.getString("EMAIL"), rs.getInt("UP_GROUP"), rs.getInt("DOWN_GROUP"),
						rs.getInt("ACCESS_LEVEL")));
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

		return employees;

	}

	@Override
	public int addEmployee(Employee newEmployee) {

		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "INSERT INTO EMPLOYEE (FIRSTNAME, LASTNAME, EMAIL, UP_GROUP, DOWN_GROUP, ACCESS_LEVEL)\r\n"
				+ "VALUES (?, ?, ?, ?, -1, 1)";
		PreparedStatement pstmt;
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, newEmployee.getFirstName());
			pstmt.setString(2, newEmployee.getLastName());
			pstmt.setString(3, newEmployee.getEmail());
			pstmt.setInt(4, newEmployee.getUpGroup());
			
			if (pstmt.executeUpdate() != 0) {
				if (addNewCredentials(conn, newEmployee.getEmail()) == 1) {
					conn.commit();
					return 1;
				}
				else {
					conn.rollback();
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

		return 0;

	}

	@Override
	public List<Request> getPendingRequestsByManagerId(int managerId) {

		List<Request> requests = new ArrayList<>();

		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT * FROM (\r\n" + "	SELECT * FROM REQUEST\r\n" + "	WHERE REQUEST_ID NOT IN (\r\n"
				+ "		SELECT REQUEST_ID FROM RESOLUTION\r\n" + "	)\r\n" + ") WHERE EMPLOYEE_ID IN (\r\n"
				+ "	SELECT EMPLOYEE_ID FROM EMPLOYEE\r\n" + "	WHERE UP_GROUP <> -1 AND UP_GROUP = (\r\n"
				+ "		SELECT DOWN_GROUP FROM EMPLOYEE\r\n" + "		WHERE EMPLOYEE_ID = ? AND DOWN_GROUP <> -1\r\n"
				+ "	)\r\n" + ")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, managerId);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				requests.add(new Request(rs.getInt("REQUEST_ID"), rs.getInt("EMPLOYEE_ID"), rs.getDate("REQUEST_DATE"),
						rs.getString("REASON"), rs.getString("MESSAGE"), rs.getDouble("AMOUNT")));
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
	public List<Request> getResolvedRequestsAsManager() {

		List<Request> requests = new ArrayList<>();

		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT REQ.REQUEST_ID AS REQ_ID, REQ.EMPLOYEE_ID AS E_ID, REQ.REQUEST_DATE AS REQ_DATE, REQ.REASON, REQ.MESSAGE, REQ.AMOUNT, RES.RESOLUTION_ID AS RES_ID, RES.STATUS, RES.EMPLOYEE_ID AS M_ID, RES.RESOLUTION_DATE AS RES_DATE\r\n"
				+ "FROM REQUEST REQ\r\n" + "INNER JOIN RESOLUTION RES\r\n" + "ON REQ.REQUEST_ID = RES.REQUEST_ID\r\n"
				+ "WHERE REQ.EMPLOYEE_ID NOT IN (\r\n" + "	SELECT EMPLOYEE_ID FROM EMPLOYEE\r\n"
				+ "	WHERE ACCESS_LEVEL = 3\r\n" + ")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Resolution resolution = new Resolution(rs.getInt("RES_ID"), rs.getInt("REQ_ID"), rs.getInt("STATUS"),
						rs.getInt("M_ID"), rs.getDate("RES_DATE"));
				requests.add(new Request(rs.getInt("REQ_ID"), rs.getInt("E_ID"), rs.getDate("REQ_DATE"),
						rs.getString("REASON"), rs.getString("MESSAGE"), rs.getDouble("AMOUNT"), resolution));
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
	public int approveRequest(int requestId, int managerId) {

		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "INSERT INTO RESOLUTION (REQUEST_ID, STATUS, EMPLOYEE_ID, RESOLUTION_DATE)\r\n"
				+ "VALUES (?, 1, ?, ?)";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, requestId);
			pstmt.setInt(2, managerId);
			pstmt.setDate(3, new Date(Calendar.getInstance().getTimeInMillis()));
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
	public int denyRequest(int requestId, int managerId) {

		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "INSERT INTO RESOLUTION (REQUEST_ID, STATUS, EMPLOYEE_ID, RESOLUTION_DATE)\r\n"
				+ "VALUES (?, -1, ?, ?)";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, requestId);
			pstmt.setInt(2, managerId);
			pstmt.setDate(3, new Date(Calendar.getInstance().getTimeInMillis()));
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
	public int addNewCredentials(Connection conn, String newEmail) {

		String sqlStmt1 = "SELECT EMPLOYEE_ID FROM EMPLOYEE\r\n" + "WHERE EMAIL = ?";
		PreparedStatement pstmt1;
		try {
			pstmt1 = conn.prepareStatement(sqlStmt1);
			pstmt1.setString(1, newEmail);

			ResultSet rs1 = pstmt1.executeQuery();
			int employeeId = 0;
			if (rs1.next()) {
				employeeId = rs1.getInt("EMPLOYEE_ID");
			}
			if (employeeId != 0) {
				String newUsername = RandomStringUtils.randomAlphanumeric(8);
				String newPassword = RandomStringUtils.randomAlphanumeric(12);
				String sqlStmt2 = "INSERT INTO CREDENTIALS (USERNAME, PASSWORD_HASH, EMPLOYEE_ID)\r\n"
						+ "VALUES (?, ?, ?)";
				PreparedStatement pstmt2 = conn.prepareStatement(sqlStmt2);
				pstmt2.setString(1, newUsername);
				pstmt2.setString(2, UtilityManager.digestSHA256(newPassword));
				pstmt2.setInt(3, employeeId);

				if (pstmt2.executeUpdate() > 0) {
					final String subject = "Welcome New Associate: YOUR NEW SETS CREDENTIALS!";
					final String content = String.format("YOUR NEW USERNAME: %s%nYOUR NEW PASSWORD: %s%n", newUsername,
							newPassword);
					UtilityManager.sendEmail(newEmail, subject, content);
					return 1;
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;

	}

	@Override
	public String getEmployeeEmailByRequestId(int requestId) {
		
		String email = null;
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT EMAIL FROM EMPLOYEE\r\n" + 
				"WHERE EMPLOYEE_ID = (\r\n" + 
				"	SELECT EMPLOYEE_ID FROM REQUEST\r\n" + 
				"	WHERE REQUEST_ID = ?\r\n" + 
				")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, requestId);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				email = rs.getString("EMAIL");
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
		
		return email;
		
	}

}
