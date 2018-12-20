// App Name
const APP_NAME = "se-talent-solutions";

// API Endpoint
const URL_WRAPPER = function (endpoint) {
	return "/" + APP_NAME + "/" + endpoint;
};

// Fetch Header
// Get General (Blob Accepted)
const GET_HEADER = {
	method: "GET"
};
// Get JSON
const GET_HEADER_JSON = {
	method: "GET",
	headers: { Accept: "application/json" }
};
// Post JSON
const POST_HEADER_WRAPPER = function (data) {
	var header = {
		method: "POST",
		headers: {
			"Content-Type": "application/json; charset=utf-8"
		},
		body: JSON.stringify(data)
	};
	return header;
};
// Post Form Data with File
const POST_HEADER_FORM_WRAPPER = function (formData) {
	var header = {
		method: "POST",
		body: formData
	}
};
// Put JSON
const PUT_HEADER_WRAPPER = function (data) {
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
const DELETE_HEADER_WRAPPER = function (data) {
	var header = {
		method: "DELETE",
		headers: {
			"Content-Type": "application/json; charset=utf-8"
		},
		body: JSON.stringify(data)
	};
	return header;
};
