/**
 * This file handles saving
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * This function is called when the user clicks the Save As button. It reset the
 * unique ID to be 0 which is a special ID that tells the server to create a new
 * walk. This then calls the saveWalktoServer(form) that deals with the saving
 * of the walk
 * 
 * @param form
 *            the walk details form that contains the walk details
 */
function saveWalktoServerNew(form) {
	uniqueid = 0;
	this.saveWalktoServer(form);
}

/**
 * This function is called when the user clicks Save. or from the
 * saveWalktoServerNew(form). This function firstly saves the walk details
 * details in the form parameter. It then checks if the details are not empty
 * and that the walk contains atleast one waypoint. It then calls
 * sendSerializeObject().
 * 
 * @param form
 *            the form that contains the walk details.
 */
function saveWalktoServer(form) {
	saveFormDetails(form);
	if (0 == length) {
		walkDetailsBox("The walk is empty. Unable to save");
	} else if (walktitle.replace(/\s/g, "") == "") {
		walkDetailsBox("Please enter a walk title");
	} else if (walkdesc.replace(/\s/g, "") == "") {
		walkDetailsBox("Please enter a walk description");
	} else {
		overlay();
		overridebox();
		sendSerializeObject();
	}
}

/**
 * This creates a serializable object of the walk data to be send to the server.
 * Once
 */
function sendSerializeObject() {
	var listObj = [];
	for ( var index = 0; index < length; index++) {
		var point = waypoints[index];
		var latlng = point.marker.getPosition();
		var tm = new tokenWaypoint(index, point.title, point.desc,
				point.welshtitle, point.welshdesc, latlng.lat(), latlng.lng(),
				point.images, point.numberofimages);
		listObj[index] = tm;
	}
	version++;
	var walkDetailsObject = new walkDetails(uniqueid, walktitle, walkdesc,
			welshwalktitle, welshwalkdesc, walkdifficulty, version, listObj,
			length);
	serializeSaveSend(walkDetailsObject);
}

/**
 * This creates a token waypoint object to be serialized to be send to server.
 * Only cotains the core waypoint details. To create a new token waypoint call
 * "new tokenWaypoint(PARAMETERS)"
 * 
 * @param index
 *            waypoint index
 * @param title
 *            waypoint title
 * @param description
 *            waypoint description
 * @param latitude
 *            waypoint latitude
 * @param longitude
 *            waypoint longitude
 * @param images
 *            array of linked images
 * @param numberofimages
 *            number of images
 * @returns a tokenwaypoint object.
 */
function tokenWaypoint(index, title, description, welshtitle, welshdesc,
		latitude, longitude, images, numberofimages) {
	this.index = index;
	this.title = title;
	this.desc = description;
	this.welshtitle = welshtitle;
	this.welshdesc = welshdesc;
	this.lat = latitude;
	this.lng = longitude;
	this.images = images;
	this.numberofimages = numberofimages;
}

/**
 * Creates an object to store all the walk details and waypoint details. This is
 * to be serialzed to be send to server. To create a new object call "new
 * walkDetails(PARAMETER)"
 * 
 * @param uniqueid
 *            the walk uniquie ID
 * @param walktitle
 *            the walk title
 * @param walkdesc
 *            the walk description
 * @param route
 *            the list of token waypoints
 * @param walklength
 *            number of waypoints in the walk
 * @returns the walk details object containing everything in the walk
 */
function walkDetails(uniqueid, walktitle, walkdesc, welshwalktitle,
		welshwalkdesc, walkdifficulty, version, route, walklength) {
	this.id = uniqueid;
	this.walklength = walklength;
	this.walktitle = walktitle;
	this.walkdesc = walkdesc;
	this.welshwalktitle = welshwalktitle;
	this.welshwalkdesc = welshwalkdesc;
	this.walkdifficulty = walkdifficulty;
	this.version = version;
	this.route = route;
}

/**
 * Create an AJAX POST call that sends the walkdetails object to the server to
 * be saved. Also handles the server response.
 * 
 * @param walkDetailsObject
 *            the walkdetails object which is serialized into a string to send
 *            to the server with a AJAX POST.
 */
function serializeSaveSend(walkDetailsObject) {
	var serialized = JSON.stringify(walkDetailsObject);
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.open("POST", "sendsave.php", true);
	xmlhttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			overridebox();
			if (0 == xmlhttp.responseText) {
				walkDetailsBox('Walk file failed to be saved due to server error');
				overlay();
			} else {
				uniqueid = xmlhttp.responseText;
				walkDetailsBox('Walk file saved');
				overlay();
			}
		}
	};
	var str = "serialized=" + serialized;
	xmlhttp.send(str);
}
