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
});
