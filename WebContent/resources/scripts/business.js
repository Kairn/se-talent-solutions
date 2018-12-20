// Display transaction message
const displayMessage = function (success, message, reload) {
	var $target;
	if (success) {
		$target = $("#success-message");
	}
	else {
		$target = $("#error-message");
	}
	$target.html(message);
	$target.slideDown(1000).delay(3000).slideUp(1000, function () {
		if (reload) {
			location.reload();
		}
	})
};

// Login with an active session
const loginWithSession = function () {
	fetch("login", GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			showEmployee(data);
		})
		.catch(function (error) {
			console.log(error);
		});
};

// Submit login form to login with credentials
const loginWithCredentials = function () {
	var credentials = {};
	credentials["username"] = $("#username").val().trim();
	credentials["password"] = $("#password").val().trim();
	fetch("login", POST_HEADER_WRAPPER(credentials))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			showEmployee(data);
		})
		.catch(function (error) {
			console.log(error);
		});
};

// Display employee information based on fetch response
const showEmployee = function (data) {
	if (parseInt(data["status"]) == 404) {
		$("#employee-section").addClass("hide");
		$("#login-section").removeClass("hide");
	}
	else if (parseInt(data["status"]) == 400 || parseInt(data["status"]) == 401) {
		$("#employee-section").addClass("hide");
		$("#login-section").removeClass("hide");
		displayMessage(false, "Error: Invalid Credentials", false);
	}
	else if (parseInt(data["status"]) == 200) {
		var employee = JSON.parse(data["content"]);
		$("#employee-section").removeClass("hide");
		$("#login-section").addClass("hide");
		$("#firstName").text(employee["firstName"]);
		$("#lastName").text(employee["lastName"]);
		$("#email").text(employee["email"]);
		$("#accessLevel").text(employee["accessLevel"]);
		// Update the update form
		$("#newFirstName").val(employee["firstName"]);
		$("#newLastName").val(employee["lastName"]);
		$("#newEmail").val(employee["email"]);
		if (parseInt(employee["accessLevel"]) > 1) {
			$("#manage").removeClass("hide");
			$("#resolve").removeClass("hide");
		}
		else {
			$("#manage").addClass("hide");
			$("#resolve").addClass("hide");
		}
	}
};

// Send an request to update the employee's information
const updateEmployeeInformation = function () {
	var newInformation = {};
	newInformation["newFirstName"] = $("#newFirstName").val().trim();
	newInformation["newLastName"] = $("#newLastName").val().trim();
	newInformation["newEmail"] = $("#newEmail").val().trim();
	fetch("update", POST_HEADER_WRAPPER(newInformation))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) == 200) {
				displayMessage(true, "Success: Information Updated", true);
			}
			else if (parseInt(data["status"]) == 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else {
				displayMessage(false, "Error: Invalid Information", false);
			}
		})
		.catch(function (error) {
			console.log(error);
		})
};

// Send an request to change the employee's credentials
const changeEmployeeCredentials = function () {
	var newCredentials = {};
	newCredentials["oldPassword"] = $("#oldPassword").val().trim();
	newCredentials["newUsername"] = $("#newUsername").val().trim();
	newCredentials["newPassword1"] = $("#newPassword1").val().trim();
	newCredentials["newPassword2"] = $("#newPassword2").val().trim();
	if (newCredentials["newPassword1"] !== newCredentials["newPassword2"]) {
		$("#password-mismatch").removeClass("hide");
	}
	else {
		$("#password-mismatch").addClass("hide");
		fetch("security", POST_HEADER_WRAPPER(newCredentials))
			.then(function (response) {
				return response.json();
			})
			.then(function (data) {
				if (parseInt(data["status"]) == 200) {
					displayMessage(true, "Success: Your Credentials Have Been Changed", true);
				}
				else if (parseInt(data["status"]) == 440) {
					displayMessage(false, "Error: Invalid Session or Session Expired", true);
				}
				else {
					displayMessage(false, "Error: Invalid Credentials or Information", false);
				}
			})
			.catch(function (error) {
				console.log(error);
			})
	}
};

// Send a request to receive new credentials if the employee forgets their password
const obtainNewCredentials = function () {
	var employeeInformation = {};
	employeeInformation["username"] = $("#rusername").val().trim();
	employeeInformation["email"] = $("#remail").val().trim();
	fetch("recover", POST_HEADER_WRAPPER(employeeInformation))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) == 200) {
				displayMessage(true, "Success: New Credentials Have Been Sent to Your Email", false);
			}
			else {
				displayMessage(false, "Error: Invalid Information", false);
			}
		})
		.catch(function (error) {
			console.log(error);
		})
};

// Send a request to fetch all employees that a high level person manages
const getJuniorEmployees = function () {
	fetch("manage", GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) == 200) {
				showJuniorEmployees(JSON.parse(data["content"]), false);
			}
			else if (parseInt(data["status"]) == 201) {
				showJuniorEmployees(JSON.parse(data["content"]), true);
			}
			else if (parseInt(data["status"]) == 404) {
				$("#employee-table").html("<h3>You are not managing anyone</h3>");
				$("#lower-employee-section").slideDown(1000);
			}
			else if (parseInt(data["status"]) == 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else {
				displayMessage(false, "Error: Invalid Request", false);
			}
		})
		.catch(function (error) {
			console.log(error);
		})
};

// Display all junior employee information
const showJuniorEmployees = function (data, exe) {
	$empInfo = $("#employee-information");
	data.sort((a, b) => parseInt(a["accessLevel"]) - parseInt(b["accessLevel"]));
	for (let i in data) {
		$newEmpRow = $("<tr></tr>");
		$employeeId = $("<td></td>");
		$firstName = $("<td></td>");
		$lastName = $("<td></td>");
		$upGroup = $("<td></td>");
		$downGroup = $("<td></td>");
		$action = $("<td></td>");
		$button = $("<button>Fire</button>");
		$button.addClass("btn").addClass("btn-warning").addClass("fire");
		$action.append($button);
		$newEmpRow.append($employeeId).append($firstName).append($lastName).append($upGroup).append($downGroup).append($action);
		// Load information
		$employeeId.html(data[i]["employeeId"]);
		$firstName.html(data[i]["firstName"]);
		$lastName.html(data[i]["lastName"]);
		if (parseInt(data[i]["upGroup"]) == -1) {
			$upGroup.html("None");
		}
		else {
			$upGroup.html(data[i]["upGroup"]);
		}
		if (parseInt(data[i]["downGroup"]) == -1) {
			$downGroup.html("None");
		}
		else {
			$downGroup.html(data[i]["downGroup"]);
		}
		$button.attr("data-id", data[i]["employeeId"]);
		if (!exe) {
			$button.addClass("disabled");
		}
		$empInfo.prepend($newEmpRow);
	}
	$("#lower-employee-section").slideDown(3000);
	// When an executive fires an employee
	$(".fire").on("click", function () {
		fireEmployee(parseInt($(this).attr("data-id")));
	})
};

// Send an request to register a new employee
const registerEmployee = function () {
	var regEmployee = {};
	regEmployee["firstName"] = $("#regFirstName").val().trim();
	regEmployee["lastName"] = $("#regLastName").val().trim();
	regEmployee["email"] = $("#regEmail").val().trim();
	regEmployee["upGroup"] = $("#regUpGroup").val().trim();
	fetch("manage", POST_HEADER_WRAPPER(regEmployee))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				displayMessage(true, "Success: New Employee Registered", true);
			}
			else if (data["status"] == 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else {
				displayMessage(false, "Error: Invalid Request", false);
			}
		})
		.catch(function (error) {
			console.log(error);
		})
};

// Send a put request to change the role of an employee
const changeEmployeeRole = function () {
	var newRole = {};
	newRole["employeeId"] = $("#changeEmployeeId").val();
	newRole["upGroup"] = $("#changeUpGroup").val();
	newRole["downGroup"] = $("#changeDownGroup").val();
	newRole["accessLevel"] = $("#changeAccessLevel").val();
	fetch("manage", PUT_HEADER_WRAPPER(newRole))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				displayMessage(true, "Success: Employee Role Changed", true);
			}
			else if (data["status"] == 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else {
				displayMessage(false, "Error: Invalid Request", false);
			}
		})
		.catch(function (error) {
			console.log(error);
		})
};

// Send a delete request to fire an employee
const fireEmployee = function (id) {
	var firing = {};
	firing["employeeId"] = id;
	fetch("manage", DELETE_HEADER_WRAPPER(firing))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				displayMessage(true, "Success: Employee Fired", true);
			}
			else if (data["status"] == 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else {
				displayMessage(false, "Error: Invalid Request", false);
			}
		})
		.catch(function (error) {
			console.log(error);
		})
};

$(function () {
	// Try to login with a valid session
	loginWithSession();
	// Login with credentials
	$("#login-form").on("submit", function (e) {
		e.preventDefault();
		loginWithCredentials();
	})
	// When employee wants to update their information
	$("#update").on("click", function () {
		$("#update-form").removeClass("hide");
	})
	// When employee confirms an update
	$("#update-form").on("submit", function (e) {
		e.preventDefault();
		updateEmployeeInformation();
	})
	// When employee wants to change their credentials
	$("#security").on("click", function () {
		$("#security-form").slideDown();
	})
	// When employee confirms password change
	$("#security-form").on("submit", function (e) {
		e.preventDefault();
		changeEmployeeCredentials();
	})
	// When employee tries to recover their credentials
	$("#recover-form").on("submit", function (e) {
		e.preventDefault();
		obtainNewCredentials();
	})
	// When a manager level or up tries to get employee information
	$("#manage").on("click", function () {
		getJuniorEmployees();
	})
	// When a manager toggle registration form
	$("#register").on("click", function () {
		$("#register-form").slideDown(1000);
	})
	// When a manager level or up tries to register a new junior employee
	$("#register-form").on("submit", function (e) {
		e.preventDefault();
		registerEmployee();
	})
	// When an executive wants to change the role of an employee
	$("#change-role").on("click", function () {
		$("#change-role-form").slideDown(1000);
	})
	// When an executive confirms a role change
	$("#change-role-form").on("submit", function (e) {
		e.preventDefault();
		changeEmployeeRole();
	})
});
