package com.revature.sets.model;

public class Employee {
	
	private int employeeId;
	private String firstName;
	private String lastName;
	private String email;
	private int upGroup;
	private int downGroup;
	private int accessLevel;
	
	public Employee(int employeeId) {
		super();
		this.employeeId = employeeId;
	}

	public Employee(int employeeId, String firstName, String lastName) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Employee(int employeeId, String email) {
		super();
		this.employeeId = employeeId;
		this.email = email;
	}

	public Employee(int employeeId, String firstName, String lastName, String email) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public Employee(String firstName, String lastName, String email, int upGroup) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.upGroup = upGroup;
	}

	public Employee(String firstName, String lastName, String email, int upGroup, int downGroup, int accessLevel) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.upGroup = upGroup;
		this.downGroup = downGroup;
		this.accessLevel = accessLevel;
	}

	public Employee(int employeeId, String firstName, String lastName, String email, int upGroup, int downGroup,
			int accessLevel) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.upGroup = upGroup;
		this.downGroup = downGroup;
		this.accessLevel = accessLevel;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUpGroup() {
		return upGroup;
	}

	public void setUpGroup(int upGroup) {
		this.upGroup = upGroup;
	}

	public int getDownGroup() {
		return downGroup;
	}

	public void setDownGroup(int downGroup) {
		this.downGroup = downGroup;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", upGroup=" + upGroup + ", downGroup=" + downGroup + ", accessLevel=" + accessLevel + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (employeeId != other.employeeId)
			return false;
		return true;
	}

}
