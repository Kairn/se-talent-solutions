// Display Message
const displayMessage = function(success, message, reload) {
	var $target;
	if (success) {
		$target = $(".alert-success");
	}
	else {
		$target = $(".alert-danger");
	}
	$target.html(message);
	$target.slideDown(500).delay(1500).slideUp(500, function() {
		if (reload) {
			location.reload();
		}
	})
};

// Login with Session
const loginWithSession = function() {
	fetch("login", GET_HEADER_JSON)
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

// Login with Credentials
const loginWithCredentials = function() {
	var credentials = {};
	credentials["username"] = $("#username").val().trim();
	credentials["password"] = $("#password").val().trim();
	fetch("login", POST_HEADER_WRAPPER(credentials))
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

// Display Employee Information
const showEmployee = function(data) {
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
			$(".btn-group-man").removeClass("hide");
			$("#change-role").closest("div").removeClass("hide");
		}
		else if (accessLevel === 2) {
			$("#role").text("Manager");
			$(".btn-group-man").removeClass("hide");
		}
		else {
			$("#role").text("Associate");
			$(".btn-group-man").addClass("hide");
		}
		// Bind form data
		$("#newFirstName").val(employee["firstName"]);
		$("#newLastName").val(employee["lastName"]);
		$("#newEmail").val(employee["email"]);
	}
	else {
		$("#employee-section").addClass("hide");
		$("#login-section").removeClass("hide");
	}
};

// Update Employee Information
const updateEmployeeInformation = function() {
	var newInformation = {};
	newInformation["newFirstName"] = $("#newFirstName").val().trim();
	newInformation["newLastName"] = $("#newLastName").val().trim();
	newInformation["newEmail"] = $("#newEmail").val().trim();
	fetch("update", POST_HEADER_WRAPPER(newInformation))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
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
		.catch(function(error) {
			console.log(error);
		})
};

// Change Credentials
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
		fetch("security", POST_HEADER_WRAPPER(newCredentials))
			.then(function(response) {
				return response.json();
			})
			.then(function(data) {
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
			.catch(function(error) {
				console.log(error);
			})
	}
};

// Recover Credentials
const obtainNewCredentials = function() {
	var employeeInformation = {};
	employeeInformation["username"] = $("#rusername").val().trim();
	employeeInformation["email"] = $("#remail").val().trim();
	fetch("recover", POST_HEADER_WRAPPER(employeeInformation))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
			if (parseInt(data["status"]) === 200) {
				displayMessage(true, "Success: New Credentials Have Been Sent to Your Email", false);
			}
			else {
				displayMessage(false, "Error: Invalid Information", false);
			}
		})
		.catch(function(error) {
			console.log(error);
		})
};

// Get Employees under Management
const getJuniorEmployees = function() {
	fetch("manage", GET_HEADER_JSON)
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
			if (parseInt(data["status"]) === 200) {
				showJuniorEmployees(JSON.parse(data["content"]), false);
			}
			else if (parseInt(data["status"]) === 201) {
				showJuniorEmployees(JSON.parse(data["content"]), true);
			}
			else if (parseInt(data["status"]) === 404) {
				$("#employee-table").html("<h3>No One is under Your Management</h3>");
				$("#employee-table").find("h3").addClass("text-center");
				$("#manage-employee").slideDown(1000);
			}
			else if (parseInt(data["status"]) === 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else {
				displayMessage(false, "Error: Invalid Request", false);
			}
		})
		.catch(function(error) {
			console.log(error);
		})
};

// Display Employees under Management
const showJuniorEmployees = function(data, exe) {
	var $empInfo = $("#employee-information");
	$empInfo.empty();
	data.sort((a, b) => parseInt(a["accessLevel"]) - parseInt(b["accessLevel"]));
	for (let i in data) {
		let $newEmpRow = $("<tr></tr>");
		let $employeeId = $("<th></th>");
		let $firstName = $("<td></td>");
		let $lastName = $("<td></td>");
		let $upGroup = $("<td></td>");
		let $downGroup = $("<td></td>");
		let $action = $("<td></td>");
		let $button = $("<button>Fire</button>");
		$button.addClass("btn").addClass("btn-mat-red").addClass("fire").attr("data-toggle", "modal").attr("data-target", "#fire-modal");
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
			$("#fire-modal").remove();
		}
		$empInfo.prepend($newEmpRow);
	}
	if (exe) {
		$("#change-role").closest("div").removeClass("hide");
	}
	$empInfo.find("th").addClass("align-middle");
	$empInfo.find("td").addClass("align-middle");
	$("#manage-employee").slideDown(2000);
	// Fire Employee Trigger
	$(".fire").on("click", function() {
		if ($(this).hasClass("disabled")) {
			return;
		}
		$("#confirm-fire").attr("data-id", parseInt($(this).attr("data-id")));
	})
	// Confirm Fire Trigger
	$("#confirm-fire").on("click", function() {
		fireEmployee(parseInt($(this).attr("data-id")));
	})
	$('#employee-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
};

// Register Employee
const registerEmployee = function() {
	var regEmployee = {};
	regEmployee["firstName"] = $("#regFirstName").val().trim();
	regEmployee["lastName"] = $("#regLastName").val().trim();
	regEmployee["email"] = $("#regEmail").val().trim();
	regEmployee["upGroup"] = $("#regUpGroup").val().trim();
	fetch("manage", POST_HEADER_WRAPPER(regEmployee))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
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
		.catch(function(error) {
			console.log(error);
		})
};

// Change Employee Role
const changeEmployeeRole = function() {
	var newRole = {};
	newRole["employeeId"] = $("#changeEmployeeId").val();
	newRole["upGroup"] = $("#changeUpGroup").val();
	newRole["downGroup"] = $("#changeDownGroup").val();
	newRole["accessLevel"] = $("#changeAccessLevel").val();
	fetch("manage", PUT_HEADER_WRAPPER(newRole))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
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
		.catch(function(error) {
			console.log(error);
		})
};

// Fire Employee
const fireEmployee = function(id) {
	var firing = {};
	firing["employeeId"] = id;
	fetch("manage", DELETE_HEADER_WRAPPER(firing))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
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
		.catch(function(error) {
			console.log(error);
		})
};

// Get Employee's Requests
const getOwnRequests = function() {
	fetch("reimbursement", GET_HEADER_JSON)
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
			if (parseInt(data["status"]) === 200) {
				showOwnRequests(JSON.parse(data["content"]));
			}
			else if (parseInt(data["status"]) === 440) {
				displayMessage(false, "Error: Invalid Session or Session Expired", true);
			}
			else if (parseInt(data["status"]) === 404) {
				$("#reimbursement-table").html("<h3>No Request Submitted</h3>");
				$("#reimbursement-table").find("h3").addClass("text-center");
				$("#reimbursement-request").slideDown(1000);
			}
			else {
				displayMessage(false, "Error: Invalid Request", false);
			}
		})
		.catch(function(error) {
			console.log(error);
		})
};

// Show Request Information
const showOwnRequests = function(data) {
	var $reqInfo = $("#request-body-self");
	$reqInfo.empty();
	data.sort((a, b) => parseInt(a["requestId"]) - parseInt(b["requestId"]));
	for (let i in data) {
		let $newReqRow = $("<tr></tr>");
		let $reqId = $("<th></th>");
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
	$reqInfo.find("th").addClass("align-middle");
	$reqInfo.find("td").addClass("align-middle");
	$("#reimbursement-request").slideDown(2000);
	// Image List Trigger
	$(".view").on("click", function() {
		getAttachedFiles(parseInt($(this).attr("data-id")));
	});
	// Recall Request Trigger
	$(".recall").on("click", function() {
		if ($(this).hasClass("disabled")) {
			return;
		}
		recallReimbursementRequest(parseInt($(this).attr("data-id")));
	})
	$('#reimbursement-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
};

// Submit Request
const submitNewRequest = function() {
	var reqContent = {};
	reqContent["reason"] = $("#reason").val().trim();
	reqContent["message"] = $("#message").val().trim();
	reqContent["amount"] = parseFloat($("#amount").val().trim());
	fetch("reimbursement", POST_HEADER_WRAPPER(reqContent))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
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
		.catch(function(error) {
			console.log(error);
		})
};

// Recall Request
const recallReimbursementRequest = function(id) {
	var recalling = {};
	recalling["requestId"] = parseInt(id);
	fetch("reimbursement", DELETE_HEADER_WRAPPER(recalling))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
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
		.catch(function(error) {
			console.log(error);
		})
};

// Get Managed Requests
const fetchJuniorPendingRequests = function() {
	fetch("resolve", GET_HEADER_JSON)
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
			if (parseInt(data["status"]) === 200) {
				showPendingRequests(JSON.parse(data["content"]));
			}
			else if (parseInt(data["status"]) === 404) {
				$("#pending-table").html("<h3>No Pending Request</h3>");
				$("#pending-table").find("h3").addClass("text-center");
				$("#resolve-request").slideDown(1000);
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
		.catch(function(error) {
			console.log(error);
		})
};

// Display Pending Request Information
const showPendingRequests = function(data) {
	var $pendInfo = $("#pending-body");
	$pendInfo.empty();
	data.sort((a, b) => parseInt(a["requestId"]) - parseInt(b["requestId"]));
	for (let i in data) {
		let $newReqRow = $("<tr></tr>");
		let $reqId = $("<th></th>");
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
	}
	$(".view").popover({
		container: "body"
	});
	$pendInfo.find("th").addClass("align-middle");
	$pendInfo.find("td").addClass("align-middle");
	$("#resolve-request").slideDown(2000);
	// Image List Trigger
	$(".view").on("click", function() {
		getAttachedFiles(parseInt($(this).attr("data-id")));
	});
	// Resolve Request Trigger
	$(".resolve").on("click", function() {
		if ($(this).hasClass("btn-success")) {
			resolveReimbursementRequest($(this).attr("data-id"), "approve");
		}
		else {
			resolveReimbursementRequest($(this).attr("data-id"), "deny");
		}
	});
	$('#pending-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
};

// Resolve Request
const resolveReimbursementRequest = function(requestId, action) {
	var acting = {};
	acting["requestId"] = parseInt(requestId);
	acting["action"] = action;
	fetch("resolve", PUT_HEADER_WRAPPER(acting))
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
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
		.catch(function(error) {
			console.log(error);
		})
};

// Send Image Form
const uploadImageFile = function() {
	fetch("file", POST_HEADER_FORM_WRAPPER(document.querySelector("#upload-form")))
		.then(function(response) {
			console.log(response);
			return response.json();
		})
		.then(function(data) {
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
		.catch(function(error) {
			console.log(error);
		})
};

// Get File List
const getAttachedFiles = function(id) {
	$("#file-panel").fadeOut(500);
	fetch(FILE_URL_WRAPPER("r", id), GET_HEADER_JSON)
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
			if (parseInt(data["status"]) === 200) {
				showFilesList(JSON.parse(data["content"]));
			}
			else {
				displayMessage(false, "Oops: No File Found", false);
			}
		})
		.catch(function(error) {
			console.log(error);
		})
};

// Display File Panel
const showFilesList = function(data) {
	var $fileUl = $("#file-list");
	$fileUl.empty();
	for (let i in data) {
		let fileId = parseInt(data[i]["fileId"]);
		let $newBtn = $("<h2></h2>");
		$newBtn.html("<i class=\"fas fa-images\"></i>").addClass("image").addClass("text-center");
		$newBtn.attr("data-id", fileId).attr("data-type", data[i]["fileType"]);
		$newBtn.attr("data-toggle", "modal").attr("data-target", "#image-view");
		$fileUl.append($newBtn);
	}
	$("#file-panel").fadeIn(500);
	// Show Image Trigger
	$(".image").on("click", function() {
		$("#image-spot").attr("src", "https://loading.io/spinners/microsoft/lg.rotating-balls-spinner.gif");
		fetchImageFile($(this).attr("data-type"), $(this).attr("data-id"));
	})
};

// Get Image by ID
const fetchImageFile = function(type, id) {
	fetch(FILE_URL_WRAPPER("i_" + type + "_", id), GET_HEADER)
		.then(function(response) {
			return response.blob();
		})
		.then(function(blob) {
			$("#image-spot").attr("src", URL.createObjectURL(blob));
		})
		.catch(function(error) {
			console.log(error);
		})
};

// Get Resolved Requests
const getResolvedRequests = function() {
	fetch("inspect", GET_HEADER_JSON)
		.then(function(response) {
			return response.json();
		})
		.then(function(data) {
			if (parseInt(data["status"]) === 200) {
				showRequestsWithResolution(JSON.parse(data["content"]));
			}
			else if ((parseInt(data["status"])) === 404) {
				$("#inspect-table").html("<h3>No History Available</h3>");
				$("#inspect-table").addClass("text-center");
				$("#inspect-request").slideDown(1000);
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
		.catch(function(error) {
			console.log(error);
		})
};

// Display Resolution Information
const showRequestsWithResolution = function(data) {
	var $inspectTableBody = $("#inspect-body");
	$inspectTableBody.empty();
	for (let i in data) {
		let $newInsRow = $("<tr></tr>");
		let $empId = $("<th></th>");
		let $empName = $("<td></td>");
		let $reason = $("<td></td>");
		let $sDate = $("<td></td>");
		let $amount = $("<td></td>");
		let $status = $("<td></td>");
		let $manName = $("<td></td>");
		let $rDate = $("<td></td>");
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
	$inspectTableBody.find("th").addClass("align-middle");
	$inspectTableBody.find("td").addClass("align-middle");
	$("#inspect-request").slideDown(2000);
	$('#inspect-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
};

$(function() {
	// Login on Startup
	loginWithSession();
	// Login with credentials
	$("#login-form").on("submit", function(e) {
		e.preventDefault();
		loginWithCredentials();
	});
	$("#logout-form").on("submit", function(e) {
		e.preventDefault();
		fetch("logout", POST_HEADER_WRAPPER(null))
			.then(() => {
				displayMessage(true, "Successfully logged out", true);
			})
	})
	// Submit Login Form
	$("#update").on("click", function() {
		$("#update-form").slideToggle(1000);
	});
	// Submit Update Form
	$("#update-form").on("submit", function(e) {
		e.preventDefault();
		updateEmployeeInformation();
	});
	// Forgot Credentials
	$("#security").on("click", function() {
		$("#security-form").slideToggle(1000);
	});
	// Reset Credentials
	$("#security-form").on("submit", function(e) {
		e.preventDefault();
		changeEmployeeCredentials();
	});
	// Submit Recover Form
	$("#recover-form").on("submit", function(e) {
		e.preventDefault();
		obtainNewCredentials();
	});
	// Manage Employees
	$("#manage").on("click", function() {
		getJuniorEmployees();
	});
	// Toggle Registration Form
	$("#register").on("click", function() {
		$("#register-form").slideToggle(1000);
	});
	// Submit Registration Form
	$("#register-form").on("submit", function(e) {
		e.preventDefault();
		registerEmployee();
	});
	// Toggle Change Role Form
	$("#change-role").on("click", function() {
		$("#change-role-form").slideToggle(1000);
	});
	// Submit Change Role Form
	$("#change-role-form").on("submit", function(e) {
		e.preventDefault();
		changeEmployeeRole();
	});
	// View Requests
	$("#request").on("click", function() {
		getOwnRequests();
	});
	// Toggle Submit Request Form
	$("#new-request").on("click", function() {
		$("#submit-request-form").slideDown(1000);
	});
	// Toggle File Upload Form
	$("#new-file").on("click", function() {
		$("#upload-form").slideDown(1000);
	});
	// Submit New Request Form
	$("#submit-request-form").on("submit", function(e) {
		e.preventDefault();
		submitNewRequest();
	});
	// Resolve Requests
	$("#resolve").on("click", function() {
		fetchJuniorPendingRequests();
	});
	// Submit File Upload Form
	$("#upload-form").on("submit", function(e) {
		e.preventDefault();
		uploadImageFile();
	});
	// View Resolved Requests
	$("#inspect").on("click", function() {
		getResolvedRequests();
	})
	// Close File Panel
	$("#panel-close").on("click", function() {
		$("#file-panel").fadeOut();
	})
	// Resize File Modal
	$('#image-view').on('show.bs.modal', function() {
		$(this).find('.modal-body').css({
			width: "auto",
			height: "auto",
			'max-height': '100%'
		});
	});
});
