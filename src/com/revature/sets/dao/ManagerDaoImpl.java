package com.revature.sets.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
		String sqlStmt = "SELECT * FROM EMPLOYEE\r\n" + 
				"WHERE UP_GROUP <> -1 AND UP_GROUP = (\r\n" + 
				"	SELECT DOWN_GROUP FROM EMPLOYEE\r\n" + 
				"	WHERE EMPLOYEE_ID = ? AND DOWN_GROUP <> -1\r\n" + 
				")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, managerId);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				employees.add(new Employee(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("EMAIL"), rs.getInt("UP_GROUP"), rs.getInt("DOWN_GROUP"), rs.getInt("ACCESS_LEVEL")));
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
		String sqlStmt = "INSERT INTO EMPLOYEE (FIRSTNAME, LASTNAME, EMAIL, UP_GROUP, DOWN_GROUP, ACCESS_LEVEL)\r\n" + 
				"VALUES (?, ?, ?, ?, -1, 1)";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setString(1, newEmployee.getFirstName());
			pstmt.setString(2, newEmployee.getLastName());
			pstmt.setString(3, newEmployee.getEmail());
			pstmt.setInt(4, newEmployee.getUpGroup());
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
	public List<Request> getPendingRequestsByManagerId(int managerId) {
		
		List<Request> requests = new ArrayList<>();
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT * FROM (\r\n" + 
				"	SELECT * FROM REQUEST\r\n" + 
				"	WHERE REQUEST_ID NOT IN (\r\n" + 
				"		SELECT REQUEST_ID FROM RESOLUTION\r\n" + 
				"	)\r\n" + 
				") WHERE EMPLOYEE_ID IN (\r\n" + 
				"	SELECT EMPLOYEE_ID FROM EMPLOYEE\r\n" + 
				"	WHERE UP_GROUP <> -1 AND UP_GROUP = (\r\n" + 
				"		SELECT DOWN_GROUP FROM EMPLOYEE\r\n" + 
				"		WHERE EMPLOYEE_ID = ? AND DOWN_GROUP <> -1\r\n" + 
				"	)\r\n" + 
				")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, managerId);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				requests.add(new Request(rs.getInt("REQUEST_ID"), rs.getInt("EMPLOYEE_ID"), rs.getDate("REQUEST_DATE"), rs.getString("REASON"), rs.getString("MESSAGE"), rs.getDouble("AMOUNT")));
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
		String sqlStmt = "SELECT REQ.REQUEST_ID AS REQ_ID, REQ.EMPLOYEE_ID AS E_ID, REQ.REQUEST_DATE AS REQ_DATE, REQ.REASON, REQ.MESSAGE, REQ.AMOUNT, RES.RESOLUTION_ID AS RES_ID, RES.STATUS, RES.EMPLOYEE_ID AS M_ID, RES.RESOLUTION_DATE AS RES_DATE\r\n" + 
				"FROM REQUEST REQ\r\n" + 
				"INNER JOIN RESOLUTION RES\r\n" + 
				"ON REQ.REQUEST_ID = RES.REQUEST_ID\r\n" + 
				"WHERE REQ.EMPLOYEE_ID NOT IN (\r\n" + 
				"	SELECT EMPLOYEE_ID FROM EMPLOYEE\r\n" + 
				"	WHERE ACCESS_LEVEL = 3\r\n" + 
				")";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Resolution resolution = new Resolution(rs.getInt("RES_ID"), rs.getInt("REQ_ID"), rs.getInt("STATUS"), rs.getInt("M_ID"), rs.getDate("RES_DATE"));
				requests.add(new Request(rs.getInt("REQ_ID"), rs.getInt("E_ID"), rs.getDate("REQ_DATE"), rs.getString("REASON"), rs.getString("MESSAGE"), rs.getDouble("AMOUNT"), resolution));
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
		String sqlStmt = "INSERT INTO RESOLUTION (REQUEST_ID, STATUS, EMPLOYEE_ID, RESOLUTION_DATE)\r\n" + 
				"VALUES (?, 1, ?, ?)";
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
		String sqlStmt = "INSERT INTO RESOLUTION (REQUEST_ID, STATUS, EMPLOYEE_ID, RESOLUTION_DATE)\r\n" + 
				"VALUES (?, -1, ?, ?)";
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

}
