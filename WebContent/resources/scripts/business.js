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
			$("#inspect").removeClass("hide");
		}
		else {
			$("#manage").addClass("hide");
			$("#resolve").addClass("hide");
			$("#inspect").addClass("hide");
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
	var $empInfo = $("#employee-information");
	$empInfo.empty();
	data.sort((a, b) => parseInt(a["accessLevel"]) - parseInt(b["accessLevel"]));
	for (let i in data) {
		let $newEmpRow = $("<tr></tr>");
		let $employeeId = $("<td></td>");
		let $firstName = $("<td></td>");
		let $lastName = $("<td></td>");
		let $upGroup = $("<td></td>");
		let $downGroup = $("<td></td>");
		let $action = $("<td></td>");
		let $button = $("<button>Fire</button>");
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
	if (exe) {
		$("#change-role").closest("div").removeClass("hide");
	}
	$("#lower-employee-section").slideDown(3000);
	// When an executive fires an employee
	$(".fire").on("click", function () {
		if ($(this).hasClass("disabled")) {
			return;
		}
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

// Fetch all submitted reimbursement requests of an employee
const getOwnRequests = function () {
	fetch("reimbursement", GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				showOwnRequests(JSON.parse(data["content"]));
			}
			else if (data["status"] == 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else if (data["status"] == 404) {
				$("#own-request-body").closest("table").html("<h3>You have no requests</h3>").closest("section").slideDown(1000);
			}
			else {
				displayMessage(false, "Error: Invalid Request", false);
			}
		})
		.catch(function (error) {
			console.log(error);
		})
};

// Populate the submitted request table 
const showOwnRequests = function (data) {
	var $reqInfo = $("#own-request-body");
	$reqInfo.empty();
	data.sort((a, b) => parseInt(a["requestId"]) - parseInt(b["requestId"]));
	for (let i in data) {
		let $newReqRow = $("<tr></tr>");
		let $reqId = $("<td></td>");
		let $date = $("<td></td>");
		let $reason = $("<td></td>");
		let $amount = $("<td></td>");
		let $status = $("<td></td>");
		let $detail = $("<td></td>");
		let $action = $("<td></td>");
		let $view = $("<button>View</button>");
		let $recall = $("<button>Recall</button>");
		// Load information
		let requestId = parseInt(data[i]["requestId"]);
		$reqId.html(requestId);
		$view.attr("data-id", requestId).addClass("btn").addClass("btn-primary").addClass("view");
		$recall.attr("data-id", requestId).addClass("btn").addClass("btn-danger").addClass("recall");
		$date.html(data[i]["requestDate"]);
		$reason.html(data[i]["reason"]);
		$amount.html("$" + parseFloat(data[i]["amount"]).toFixed(2));
		if (data[i]["resolution"] == null) {
			$status.html("Pending");
		}
		else if (parseInt(data[i]["resolution"]["status"]) == -1) {
			$status.html("Denied").addClass("text-danger");
			$recall.addClass("disabled");
		}
		else {
			$status.html("Approved").addClass("text-success");
			$recall.addClass("disabled");
		}
		$view.attr("data-toggle", "popover").attr("title", "Explanation").attr("data-content", data[i]["message"]);
		// Attach elements
		$detail.append($view);
		$action.append($recall);
		$newReqRow.append($reqId).append($date).append($reason).append($amount).append($status).append($detail).append($action);
		$reqInfo.append($newReqRow);
	}
	$(".view").popover({
		container: "body"
	});
	$reqInfo.closest("section").slideDown(2000);
	// Add event listeners to buttons
	$(".recall").on("click", function () {
		if ($(this).hasClass("disabled")) {
			return;
		}
		recallReimbursementRequest(parseInt($(this).attr("data-id")));
	})
};

// Submit a new reimbursement request
const submitNewRequest = function () {
	var reqContent = {};
	reqContent["reason"] = $("#reason").val().trim();
	reqContent["message"] = $("#message").val().trim();
	reqContent["amount"] = parseFloat($("#amount").val().trim());
	fetch("reimbursement", POST_HEADER_WRAPPER(reqContent))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				displayMessage(true, "Success: New Request Submitted", true);
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

// Send a delete request to recall a reimbursement request
const recallReimbursementRequest = function (id) {
	var recalling = {};
	recalling["requestId"] = parseInt(id);
	fetch("reimbursement", DELETE_HEADER_WRAPPER(recalling))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				displayMessage(true, "Success: Reimbursement Request Recalled", true);
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

// Fetch all pending requests submitted under a manager
const fetchJuniorPendingRequests = function () {
	fetch("resolve", GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				showPendingRequests(JSON.parse(data["content"]));
			}
			else if (data["status"] == 404) {
				$("#pending-table").closest("table").html("<h3>You have no pending request to resolve</h3>");
				$("#resolve-section").slideDown(1000);
			}
			else if (data["status"] == 401) {
				displayMessage(false, "Error: Unauthorized Access", false);
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

// Display all pending requests to be resolved
const showPendingRequests = function (data) {
	$pendInfo = $("#pending-table");
	$pendInfo.empty();
	data.sort((a, b) => parseInt(a["requestId"]) - parseInt(b["requestId"]));
	for (let i in data) {
		let $newReqRow = $("<tr></tr>");
		let $reqId = $("<td></td>");
		let $empId = $("<td></td>");
		let $empName = $("<td></td>");
		let $date = $("<td></td>");
		let $reason = $("<td></td>");
		let $amount = $("<td></td>");
		let $detail = $("<td></td>");
		let $action = $("<td></td>");
		let $view = $("<button>View</button>");
		let $approve = $("<button>Approve</button>");
		let $deny = $("<button>Deny</button>");
		// Load information
		let requestId = parseInt(data[i]["requestId"]);
		$reqId.html(requestId);
		$view.attr("data-id", requestId).addClass("btn").addClass("btn-primary").addClass("view");
		$approve.attr("data-id", requestId).addClass("btn").addClass("btn-success").addClass("resolve");
		$deny.attr("data-id", requestId).addClass("btn").addClass("btn-danger").addClass("resolve");
		$empId.html(data[i]["employeeId"]);
		$empName.html(data[i]["employeeName"]);
		$date.html(data[i]["requestDate"]);
		$reason.html(data[i]["reason"]);
		$amount.html("$" + parseFloat(data[i]["amount"]).toFixed(2));
		$view.attr("data-toggle", "popover").attr("title", "Explanation").attr("data-content", data[i]["message"]);
		// Attach elements
		$detail.append($view);
		$action.append($approve).append($deny);
		$newReqRow.append($reqId).append($empId).append($empName).append($date).append($reason).append($amount).append($detail).append($action);
		$pendInfo.append($newReqRow);
		$(".view").popover({
			container: "body"
		});
		$("#resolve-section").slideDown(2000);
		// Add event listeners
		$(".resolve").on("click", function () {
			if ($(this).hasClass("btn-success")) {
				resolveReimbursementRequest($(this).attr("data-id"), "approve");
			}
			else {
				resolveReimbursementRequest($(this).attr("data-id"), "deny");
			}
		})
	}
};

// Send a put request to resolve an reimbursement
const resolveReimbursementRequest = function (requestId, action) {
	var acting = {};
	acting["requestId"] = parseInt(requestId);
	acting["action"] = action;
	fetch("resolve", PUT_HEADER_WRAPPER(acting))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				displayMessage(true, "Success: Reimbursement Request Resolved", true);
			}
			else if (data["status"] == 401) {
				displayMessage(false, "Error: Unauthorized Access", false);
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

// Send the file form data to the servlet
const uploadImageFile = function () {
	fetch("file", POST_HEADER_FORM_WRAPPER(document.querySelector("#upload-form")))
		.then(function (response) {
			console.log(response);
			return response.json();
		})
		.then(function (data) {
			if (data["status"] == 200) {
				displayMessage(true, "Success: File Uploaded", true);
			}
			else if (data["status"] == 401) {
				displayMessage(false, "Error: Unauthorized Access", false);
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
	// When an employee wants to view his reimbursement requests
	$("#request").on("click", function () {
		getOwnRequests();
	})
	// When an employee wants to submit a new request
	$("#new-request").on("click", function () {
		$("#submit-request-form").slideDown(1000);
	})
	// When an employee wants to upload a file to a request
	$("#new-file").on("click", function () {
		$("#upload-form").slideDown(1000);
	})
	// When an employee confirms a request submission
	$("#submit-request-form").on("submit", function (e) {
		e.preventDefault();
		submitNewRequest();
	})
	// When a manager level or up wants to resolve requests
	$("#resolve").on("click", function () {
		fetchJuniorPendingRequests();
	})
	// When an employee uploads a file
	$("#upload-form").on("submit", function (e) {
		e.preventDefault();
		uploadImageFile();
	})
});
