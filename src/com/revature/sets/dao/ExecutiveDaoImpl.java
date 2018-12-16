package com.revature.sets.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;
import com.revature.sets.model.Resolution;
import com.revature.sets.utility.UtilityManager;

public class ExecutiveDaoImpl implements ExecutiveDao {

	public ExecutiveDaoImpl() {
		super();
	}

	@Override
	public List<Employee> getEmployeesAsExecutive() {
		
		List<Employee> employees = new ArrayList<>();
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT * FROM EMPLOYEE\r\n" + 
				"WHERE ACCESS_LEVEL <> 3";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				employees.add(new Employee(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("EMAIL"), rs.getInt("UP_GROUP"), rs.getInt("DOWN_GROUP"), rs.getInt("ACCESS_LEVEL")));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
		
	}

	@Override
	public List<Request> getRequestsAsExecutive() {
		
		List<Request> requests = new ArrayList<>();
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "SELECT REQ.REQUEST_ID AS REQ_ID, REQ.EMPLOYEE_ID AS E_ID, REQ.REQUEST_DATE AS REQ_DATE, REQ.REASON, REQ.MESSAGE, REQ.AMOUNT, RES.RESOLUTION_ID AS RES_ID, RES.STATUS, RES.EMPLOYEE_ID AS M_ID, RES.RESOLUTION_DATE AS RES_DATE\r\n" + 
				"FROM REQUEST REQ\r\n" + 
				"LEFT JOIN RESOLUTION RES\r\n" + 
				"ON REQ.REQUEST_ID = RES.REQUEST_ID";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			
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
		
		return requests;
		
	}

	@Override
	public int changeEmployeeRole(int employeeId, int newUpGroup, int newDownGroup, int newAccessLevel) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "UPDATE EMPLOYEE\r\n" + 
				"SET UP_GROUP = ?, DOWN_GROUP = ?, ACCESS_LEVEL = ?\r\n" + 
				"WHERE EMPLOYEE_ID = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, newUpGroup);
			pstmt.setInt(2, newDownGroup);
			pstmt.setInt(3, newAccessLevel);
			pstmt.setInt(4, employeeId);
			return pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}

	@Override
	public int deleteEmployee(int employeeId) {
		
		Connection conn = UtilityManager.getConnection();
		String sqlStmt = "DELETE FROM EMPLOYEE\r\n" + 
				"WHERE EMPLOYEE_ID = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlStmt);
			pstmt.setInt(1, employeeId);
			return pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}

}
