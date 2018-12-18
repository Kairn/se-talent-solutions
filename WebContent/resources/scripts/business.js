// Login with an active session
const loginWithSession = function() {
	fetch(URL_WRAPPER("login"), GET_HEADER_JSON)
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		showEmployee(data);
	})
	.catch(function(error) {
		console.log(error);
	});
};

// Submit login form to login with credentials
const loginWithCredentials = function() {
	var credentials = {};
	credentials["username"] = $("#username").val().trim();
	credentials["password"] = $("#password").val().trim();
	fetch(URL_WRAPPER("login"), POST_HEADER_WRAPPER(credentials))
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		showEmployee(data);
	})
	.catch(function(error) {
		console.log(error);
	});
};

// Display employee information based on fetch response
const showEmployee = function(data) {
	if (parseInt(data["status"]) == 404) {
		$("#employee-section").addClass("hide");
		$("#login-section").removeClass("hide");
	}
	else if (parseInt(data["status"]) == 405) {
		$("#employee-section").addClass("hide");
		$("#login-section").removeClass("hide");
		$("#invalid-credentials").slideDown(1000).delay(1000).slideUp(1000);
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
const updateEmployeeInformation = function() {
	var newInformation = {};
	newInformation["newFirstName"] = $("#newFirstName").val().trim();
	newInformation["newLastName"] = $("#newLastName").val().trim();
	newInformation["newEmail"] = $("#newEmail").val().trim();
	fetch(URL_WRAPPER("update"), POST_HEADER_WRAPPER(newInformation))
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		if (parseInt(data["status"]) == 200) {
			$("#update-success").slideDown(2000).delay(2000, function() {
				location.reload();
			});
		}
		else {
			$("#update-failure").slideDown(2000).delay(2000).slideUp(1000);
		}
	})
	.catch(function(error) {
		console.log(error);
	})
};

// Send an request to change the employee's credentials
const changeEmployeeCredentials = function() {
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
		fetch(URL_WRAPPER("security"), POST_HEADER_WRAPPER(newCredentials))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
			if (parseInt(data["status"]) == 200) {
				$("#password-changed").slideDown(2000).delay(2000, function() {
					location.reload();
				})
			}
			else {
				$("#security-error").slideDown(2000).delay(1000).slideUp(1000);
			}
		})
		.catch(function(error) {
			console.log(error);
		})
	}
}

// Send a request to receive new credentials if the employee forgets their password
const obtainNewCredentials = function() {
	var employeeInformation = {};
	employeeInformation["username"] = $("#rusername").val().trim();
	employeeInformation["email"] = $("#remail").val().trim();
	fetch(URL_WRAPPER("recover"), POST_HEADER_WRAPPER(employeeInformation))
	.then(function(response) {
		return response.json();
	})
	.then(function(data) {
		if(parseInt(data["status"]) == 200) {
			$("#recover-success").slideDown(2000).delay(2000).slideUp(1000);
		}
		else {
			$("#invalid-information").slideDown(2000).delay(2000).slideUp(1000);
		}
	})
	.catch(function(error) {
		console.log(error);
	})
}

$(function() {
	// Try to login with a valid session
	loginWithSession();
	// Login with credentials
	$("#login-form").on("submit", function(e) {
		e.preventDefault();
		loginWithCredentials();
	})
	// When employee wants to update their information
	$("#update").on("click", function() {
		$("#update-form").removeClass("hide");
	})
	// When employee confirms an update
	$("#update-form").on("submit", function(e) {
		e.preventDefault();
		updateEmployeeInformation();
	})
	// When employee wants to change their credentials
	$("#security").on("click", function() {
		$("#security-form").slideDown();
	})
	// When employee confirms password change
	$("#security-form").on("submit", function(e) {
		e.preventDefault();
		changeEmployeeCredentials();
	})
	// When employee tries to recover their credentials
	$("#recover-form").on("submit", function(e) {
		e.preventDefault();
		obtainNewCredentials();
	})
});
