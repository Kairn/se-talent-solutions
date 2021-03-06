// App Name
const APP_NAME = "se-talent-solutions";

// API Endpoint
const URL_WRAPPER = function(endpoint) {
	return "/" + APP_NAME + "/" + endpoint;
};

// Fetch Headers
// Get General
const GET_HEADER = {
	method: "GET"
};

// Get JSON
const GET_HEADER_JSON = {
	method: "GET",
	headers: { Accept: "application/json" }
};

// Post JSON
const POST_HEADER_WRAPPER = function(data) {
	var header = {
		method: "POST",
		headers: {
			"Content-Type": "application/json; charset=utf-8"
		},
		body: JSON.stringify(data)
	};
	return header;
};

// Post Form Data
const POST_HEADER_FORM_WRAPPER = function(form) {
	var formData = new FormData(form);
	var header = {
		method: "POST",
		body: formData
	};
	return header;
};

// Put JSON
const PUT_HEADER_WRAPPER = function(data) {
	var header = {
		method: "PUT",
		headers: {
			"Content-Type": "application/json; charset=utf-8"
		},
		body: JSON.stringify(data)
	};
	return header;
};

// Delete JSON
const DELETE_HEADER_WRAPPER = function(data) {
	var header = {
		method: "DELETE",
		headers: {
			"Content-Type": "application/json; charset=utf-8"
		},
		body: JSON.stringify(data)
	};
	return header;
};

// File URL Maker
const FILE_URL_WRAPPER = function(type, id) {
	return "image/" + type + id;
}

// Convert UNIX Epoch to local date
const epochToDate = function(epoch) {
	let localDate = new Date(0);
	localDate.setUTCMilliseconds(parseInt(epoch));
	return localDate.toJSON().split("T")[0];
}
