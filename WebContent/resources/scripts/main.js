// Display Message
const displayMessage = function (success, message, reload) {
	var $target;
	if (success) {
		$target = $(".alert-success");
	}
	else {
		$target = $(".alert-danger");
	}
	$target.html(message);
	$target.slideDown(500).delay(1500).slideUp(500, function () {
		if (reload) {
			location.reload();
		}
	})
};

// Login with Session
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

// Login with Credentials
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

// Display Employee Information
const showEmployee = function (data) {
	if (parseInt(data["status"]) === 400 || parseInt(data["status"]) === 401) {
		$("#employee-section").addClass("hide");
		$("#login-section").removeClass("hide");
		displayMessage(false, "Error: Invalid Credentials", false);
	}
	else if (parseInt(data["status"]) === 200) {
		var employee = JSON.parse(data["content"]);
		$("#employee-section").removeClass("hide");
		$("#login-section").addClass("hide");
		// Bind Data
		$("#firstName").text(employee["firstName"]);
		$("#lastName").text(employee["lastName"]);
		$("#employeeId").text(employee["employeeId"]);
		$("#email").text(employee["email"]);
		$("#upGroup").text(parseInt(employee["upGroup"]) === -1 ? "None" : employee["upGroup"]);
		$("#downGroup").text(parseInt(employee["downGroup"]) === -1 ? "None" : employee["downGroup"]);
		let accessLevel = parseInt(employee["accessLevel"]);
		$("#accessLevel").text(accessLevel);
		if (accessLevel === 3) {
			$("#role").text("Executive");
		}
		else if (accessLevel === 2) {
			$("#role".text("Manager"));
		}
		else {
			$("#role".text("Associate"));
		}
		// Bind form data
		$("#newFirstName").val(employee["firstName"]);
		$("#newLastName").val(employee["lastName"]);
		$("#newEmail").val(employee["email"]);
		if (parseInt(employee["accessLevel"]) > 1) {
			$(".btn-group-man").removeClass("hide");
		}
		else {
			$(".btn-group-man").addClass("hide");
		}
	}
	else {
		$("#employee-section").addClass("hide");
		$("#login-section").removeClass("hide");
	}
};

// Update Employee Information
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
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: Information Updated", true);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Change Credentials
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
				if (parseInt(data["status"]) === 200) {
					displayMessage(true, "Success: Your Credentials Have Been Changed", true);
				}
				else if (parseInt(data["status"]) === 440) {
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

// Recover Credentials
const obtainNewCredentials = function () {
	var employeeInformation = {};
	employeeInformation["username"] = $("#rusername").val().trim();
	employeeInformation["email"] = $("#remail").val().trim();
	fetch("recover", POST_HEADER_WRAPPER(employeeInformation))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
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

// Get Employees under Management
const getJuniorEmployees = function () {
	fetch("manage", GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				showJuniorEmployees(JSON.parse(data["content"]), false);
			}
			else if (parseInt(data["status"]) === 201) {
				showJuniorEmployees(JSON.parse(data["content"]), true);
			}
			else if (parseInt(data["status"]) === 404) {
				$("#employee-table").html("<h3>You are not managing anyone</h3>");
				$("#lower-employee-section").slideDown(1000);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Display Employees under Management
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
		// Bind Data
		$employeeId.html(data[i]["employeeId"]);
		$firstName.html(data[i]["firstName"]);
		$lastName.html(data[i]["lastName"]);
		if (parseInt(data[i]["upGroup"]) === -1) {
			$upGroup.html("None");
		}
		else {
			$upGroup.html(data[i]["upGroup"]);
		}
		if (parseInt(data[i]["downGroup"]) === -1) {
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
	// Fire Employee Trigger
	$(".fire").on("click", function () {
		if ($(this).hasClass("disabled")) {
			return;
		}
		fireEmployee(parseInt($(this).attr("data-id")));
	})
};

// Register Employee
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
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: New Employee Registered", true);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Change Employee Role
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
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: Employee Role Changed", true);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Fire Employee
const fireEmployee = function (id) {
	var firing = {};
	firing["employeeId"] = id;
	fetch("manage", DELETE_HEADER_WRAPPER(firing))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: Employee Fired", true);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Get Employee's Requests
const getOwnRequests = function () {
	fetch("reimbursement", GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				showOwnRequests(JSON.parse(data["content"]));
			}
			else if (parseInt(data["status"]) === 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else if (parseInt(data["status"]) === 404) {
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

// Show Request Information
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
		// Bind Data
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
		$view.attr("data-trigger", "hover").attr("data-id", requestId);
		$detail.append($view);
		$action.append($recall);
		$newReqRow.append($reqId).append($date).append($reason).append($amount).append($status).append($detail).append($action);
		$reqInfo.append($newReqRow);
	}
	$(".view").popover({
		container: "body"
	});
	$reqInfo.closest("section").slideDown(2000);
	// Image List Trigger
	$(".view").on("click", function () {
		getAttachedFiles(parseInt($(this).attr("data-id")));
	});
	// Recall Request Trigger
	$(".recall").on("click", function () {
		if ($(this).hasClass("disabled")) {
			return;
		}
		recallReimbursementRequest(parseInt($(this).attr("data-id")));
	})
};

// Submit Request
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
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: New Request Submitted", true);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Recall Request
const recallReimbursementRequest = function (id) {
	var recalling = {};
	recalling["requestId"] = parseInt(id);
	fetch("reimbursement", DELETE_HEADER_WRAPPER(recalling))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: Reimbursement Request Recalled", true);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Get Managed Requests
const fetchJuniorPendingRequests = function () {
	fetch("resolve", GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				showPendingRequests(JSON.parse(data["content"]));
			}
			else if (parseInt(data["status"]) === 404) {
				$("#pending-table").closest("table").html("<h3>You have no pending request to resolve</h3>");
				$("#resolve-section").slideDown(1000);
			}
			else if (parseInt(data["status"]) === 401) {
				displayMessage(false, "Error: Unauthorized Access", false);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Display Pending Request Information
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
		// Bind data
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
		$view.attr("data-trigger", "hover").attr("data-id", requestId);
		$detail.append($view);
		$action.append($approve).append($deny);
		$newReqRow.append($reqId).append($empId).append($empName).append($date).append($reason).append($amount).append($detail).append($action);
		$pendInfo.append($newReqRow);
		$(".view").popover({
			container: "body"
		});
		$("#resolve-section").slideDown(2000);
		// Image List Trigger
		$(".view").on("click", function () {
			getAttachedFiles(parseInt($(this).attr("data-id")));
		});
		// Resolve Request Trigger
		$(".resolve").on("click", function () {
			if ($(this).hasClass("btn-success")) {
				resolveReimbursementRequest($(this).attr("data-id"), "approve");
			}
			else {
				resolveReimbursementRequest($(this).attr("data-id"), "deny");
			}
		});
	}
};

// Resolve Request
const resolveReimbursementRequest = function (requestId, action) {
	var acting = {};
	acting["requestId"] = parseInt(requestId);
	acting["action"] = action;
	fetch("resolve", PUT_HEADER_WRAPPER(acting))
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: Reimbursement Request Resolved", true);
			}
			else if (parseInt(data["status"]) === 401) {
				displayMessage(false, "Error: Unauthorized Access", false);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Send Image Form
const uploadImageFile = function () {
	fetch("file", POST_HEADER_FORM_WRAPPER(document.querySelector("#upload-form")))
		.then(function (response) {
			console.log(response);
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: File Uploaded", true);
			}
			else if (parseInt(data["status"]) === 401) {
				displayMessage(false, "Error: Unauthorized Access", false);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Get File List
const getAttachedFiles = function (id) {
	fetch(FILE_URL_WRAPPER("r", id), GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				showFilesList(JSON.parse(data["content"]));
			}
		})
		.catch(function (error) {
			console.log(error);
		})
};

// Display File Panel
const showFilesList = function (data) {
	$fileUl = $("#file-panel").find("ul");
	$fileUl.empty();
	for (let i in data) {
		fileId = parseInt(data[i]["fileId"]);
		$newLi = $("<li></li>");
		$newBtn = $("<button></button>");
		$newBtn.html("File# " + fileId).addClass("btn").addClass("btn-info").addClass("image");
		$newBtn.attr("data-id", fileId).attr("data-type", data[i]["fileType"]);
		$newBtn.attr("type", "button").attr("data-toggle", "modal").attr("data-target", "#image-view");
		$newLi.append($newBtn);
		$fileUl.append($newLi);
	}
	$fileUl.closest("aside").slideDown(1000);
	// Show Image Trigger
	$(".image").on("click", function () {
		fetchImageFile($(this).attr("data-type"), $(this).attr("data-id"));
	})
};

// Get Image by ID
const fetchImageFile = function (type, id) {
	fetch(FILE_URL_WRAPPER("i_" + type + "_", id), GET_HEADER)
		.then(function (response) {
			return response.blob();
		})
		.then(function (blob) {
			$("#image-spot").attr("src", URL.createObjectURL(blob));
		})
		.catch(function (error) {
			console.log(error);
		})
};

// Get Resolved Requests
const getResolvedRequests = function () {
	fetch("inspect", GET_HEADER_JSON)
		.then(function (response) {
			return response.json();
		})
		.then(function (data) {
			if (parseInt(data["status"]) === 200) {
				showRequestsWithResolution(JSON.parse(data["content"]));
			}
			else if ((parseInt(data["status"])) === 404) {
				$("#inspect-view").closest("table").html("<h3>No Requests to Show</h3>");
				$("#inspect-section").slideDown(1000);
			}
			else if (parseInt(data["status"]) === 401) {
				displayMessage(false, "Error: Unauthorized Access", false);
			}
			else if (parseInt(data["status"]) === 440) {
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

// Display Resolution Information
const showRequestsWithResolution = function (data) {
	$inspectTableBody = $("#inspect-view");
	$inspectTableBody.empty();
	for (let i in data) {
		$newInsRow = $("<tr></tr>");
		$empId = $("<td></td>");
		$empName = $("<td></td>");
		$reason = $("<td></td>");
		$sDate = $("<td></td>");
		$amount = $("<td></td>");
		$status = $("<td></td>");
		$manName = $("<td></td>");
		$rDate = $("<td></td>");
		// Bind data
		$empId.html(data[i]["employeeId"]);
		$empName.html(data[i]["employeeName"]);
		$reason.html(data[i]["reason"]);
		$sDate.html(data[i]["requestDate"]);
		$amount.html("$" + parseFloat(data[i]["amount"]).toFixed(2));
		if (parseInt(data[i]["resolution"]["status"]) === -1) {
			$status.html("Denied").addClass("text-danger");
		}
		else {
			$status.html("Approved").addClass("text-success");
		}
		$manName.html(data[i]["resolution"]["employeeName"]);
		$rDate.html(data[i]["resolution"]["resolutionDate"]);
		$newInsRow.append($empId).append($empName).append($reason).append($sDate).append($amount).append($status).append($manName).append($rDate);
		$inspectTableBody.append($newInsRow);
	}
	$("#inspect-section").slideDown(2500);
};

$(function () {
	// Unhide Everything
	// $(".hide").removeClass("hide");
	// Login on Startup
	loginWithSession();
	// Login with credentials
	$("#login-form").on("submit", function (e) {
		e.preventDefault();
		loginWithCredentials();
	});
	// Submit Login Form
	$("#update").on("click", function () {
		$("#update-form").slideToggle(1000);
	});
	// Submit Update Form
	$("#update-form").on("submit", function (e) {
		e.preventDefault();
		updateEmployeeInformation();
	});
	// Forgot Credentials
	$("#security").on("click", function () {
		$("#security-form").slideToggle(1000);
	});
	// Reset Credentials
	$("#security-form").on("submit", function (e) {
		e.preventDefault();
		changeEmployeeCredentials();
	});
	// Submit Recover Form
	$("#recover-form").on("submit", function (e) {
		e.preventDefault();
		obtainNewCredentials();
	});
	// Manage Employees
	$("#manage").on("click", function () {
		getJuniorEmployees();
	});
	// Toggle Registration Form
	$("#register").on("click", function () {
		$("#register-form").slideDown(1000);
	});
	// Submit Registration Form
	$("#register-form").on("submit", function (e) {
		e.preventDefault();
		registerEmployee();
	});
	// Toggle Change Role Form
	$("#change-role").on("click", function () {
		$("#change-role-form").slideDown(1000);
	});
	// Submit Change Role Form
	$("#change-role-form").on("submit", function (e) {
		e.preventDefault();
		changeEmployeeRole();
	});
	// View Requests
	$("#request").on("click", function () {
		getOwnRequests();
	});
	// Toggle Submit Request Form
	$("#new-request").on("click", function () {
		$("#submit-request-form").slideDown(1000);
	});
	// Toggle File Upload Form
	$("#new-file").on("click", function () {
		$("#upload-form").slideDown(1000);
	});
	// Submit New Request Form
	$("#submit-request-form").on("submit", function (e) {
		e.preventDefault();
		submitNewRequest();
	});
	// Resolve Requests
	$("#resolve").on("click", function () {
		fetchJuniorPendingRequests();
	});
	// Submit File Upload Form
	$("#upload-form").on("submit", function (e) {
		e.preventDefault();
		uploadImageFile();
	});
	// View Resolved Requests
	$("#inspect").on("click", function () {
		getResolvedRequests();
	})
});
