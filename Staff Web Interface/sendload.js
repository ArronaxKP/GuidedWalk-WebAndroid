/**
 * This file deals with the loading of walks
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * Local variable storing a list of walks avialable to load.
 */
var listofwalks;

/**
 * This method gets the XML map file walk list from the server then calls the
 * loadWalkList which generates the pop up box allowing users to select a walk
 * to load
 */
function loadXMLMap() {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.open("GET", "getxmlmap.php", true);
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			overridebox();
			var myobject = eval('('+xmlhttp.responseText+')');
			loadWalkList(myobject);
		}
	};
	overridebox();
	xmlhttp.send();
}

/**
 * This function creates the HTML syntaxt for the walk list pop up window which
 * allows users to select which walk to load. It then displays the list of walks
 * pop up.
 * 
 * @param object
 *            the object containing the XML map file list of walks.
 */
function loadWalkList(object) {
	var numberofwalks = object.numberofwalks;
	if (0 != numberofwalks) {
		listofwalks = object.walklist;
		var string = '<div><form><select id="listofwalks" onchange="updateDisplay()">';
		for ( var i = 0; i < numberofwalks; i++) {
			string += '<option  value="' + i + '">' + listofwalks[i].id
					+ ' : Title: ' + listofwalks[i].title.substring(0,50); + '</option>';
			
		}
		string += '</select><div id="walk_desc_box" style="text-align:left;">'
				+ '<table border="0" align="center"><tr>'
				+ '<td><p"><b>Title: </b></p><p">'
				+ listofwalks[0].title.substring(0,200)
				+ '</p><p"><b>Description: </b></p><p">'
				+ listofwalks[0].desc.substring(0,500)
				+ '</p></td>'
				+ '<td><p"><b>Welsh Title: </b></p><p">'
				+ listofwalks[0].welshtitle.substring(0,200)
				+ '</p><p"><b>Welsh Description: </b></p><p">'
				+ listofwalks[0].welshdesc.substring(0,500) + '</p></td>' + '</tr></table>'
				+ '<p align="center"><b>Difficulty: </b>';

		if (listofwalks[0].difficulty == 0) {
			string += 'Easy';
		}
		if (listofwalks[0].difficulty == 1) {
			string += 'Normal';
		}
		if (listofwalks[0].difficulty == 2) {
			string += 'Hard';
		}

		string += '</p></div>'
				+ '<button id="loadselected" type="button" onClick="serializeLoadSend();">Load Walk</button>'
				+ '<button id="deleteselected" type="button" onClick="deleteSend();">Delete Walk</button></form></div>';
		alertbox(string);
	} else {
		var string = "No walks available";
		alertbox(string);
	}
}

/**
 * This function is called whenthe user selects another walk on the walk list
 * pop up box. This method redraws the display to show the selected walks title
 * and description
 */
function updateDisplay() {
	var index = document.getElementById("listofwalks").options[document
			.getElementById("listofwalks").selectedIndex].value;
	if (listofwalks[index].difficulty == 0) {
		difficulty = 'Easy';
	}
	if (listofwalks[index].difficulty == 1) {
		difficulty = 'Normal';
	}
	if (listofwalks[index].difficulty == 2) {
		difficulty = 'Hard';
	}
	document.getElementById("walk_desc_box").innerHTML = '<table border="0" align="center"><tr>'
			+ '<td><p "><b>Title: </b></p><p">'
			+ listofwalks[index].title.substring(0,200)
			+ '</p><p"><b>Description: </b></p><p">'
			+ listofwalks[index].desc.substring(0,500)
			+ '</p></td>'
			+ '<td><p"><b>Welsh Title: </b></p><p">'
			+ listofwalks[index].welshtitle.substring(0,200)
			+ '</p><p"><b>Welsh Description: </b></p><p">'
			+ listofwalks[index].welshdesc.substring(0,500)
			+ '</p></td>'
			+ '</tr></table>'
			+ '<p align="center"><b>Difficulty: </b>' + difficulty + '</p>';
}

/**
 * This function changes a string object into a JavaScript object. JSON is
 * JavaScript Object Notation thus de JSON is undoing it.
 * 
 * @param jsonobj
 *            is the JSON object returned from the Web Server when a walk is
 *            loaded.
 */
function dejson(jsonobj) {

	var myObject = eval('(' + jsonobj + ')');
	clearMap();
	uniqueid = myObject.id;
	walktitle = myObject.walk_title;
	walkdesc = myObject.walk_desc;
	welshwalktitle = myObject.welsh_walk_title;
	welshwalkdesc = myObject.welsh_walk_desc;

	walkdifficulty = myObject.walk_difficulty;
	version = myObject.version;
	var listofwaypoints = myObject.route;
	var len = listofwaypoints.length;
	var waypoint;

	for ( var i = 0; i < len; i++) {

		var waypointDetails = listofwaypoints[i];
		waypoint = new createWaypoint(waypointDetails.lat, waypointDetails.lng);
		waypoint.index = i;
		waypoint.title = waypointDetails.waypoint_title;
		waypoint.desc = waypointDetails.waypoint_desc;

		waypoint.welshtitle = waypointDetails.welsh_waypoint_title;
		waypoint.welshdesc = waypointDetails.welsh_waypoint_desc;

		if ("" != waypoint.title || "" != waypoint.desc
				|| "" != waypoint.welshtitle || "" != waypoint.welshdesc
				|| 0 != waypointDetails.number_of_images) {

			if (waypoint.index == 0) {
				var image = new google.maps.MarkerImage(
						'images/green-blue-dot.png', new google.maps.Size(32,
								32), new google.maps.Point(0, 0));
				waypoint.marker.setIcon(image);
			} else if (waypoint.index == len - 1) {
				var image = new google.maps.MarkerImage(
						'images/final-dot-blue.png', new google.maps.Size(32,
								32), new google.maps.Point(0, 0));
				waypoint.marker.setIcon(image);
			} else {
				var image = new google.maps.MarkerImage('images/blue-dot.png',
						new google.maps.Size(32, 32), new google.maps.Point(0,
								0));
				waypoint.marker.setIcon(image);
			}
			if (0 != waypointDetails.number_of_images) {
				var imglen = waypointDetails.number_of_images;
				for ( var j = 0; j < imglen; j++) {
					var filename = waypointDetails.images[j].image_base64;
					waypoint.images[j] = filename;
					waypoint.numberofimages++;
				}
			}
		} else {
			if (waypoint.index == 0) {
				var image = new google.maps.MarkerImage('images/green-dot.png',
						new google.maps.Size(32, 32), new google.maps.Point(0,
								0));
				waypoint.marker.setIcon(image);
			} else if (waypoint.index == len - 1) {
				var image = new google.maps.MarkerImage('images/final-dot.png',
						new google.maps.Size(32, 32), new google.maps.Point(0,
								0));
				waypoint.marker.setIcon(image);
			} else {
				var image = new google.maps.MarkerImage('images/red-dot.png',
						new google.maps.Size(32, 32), new google.maps.Point(0,
								0));
				waypoint.marker.setIcon(image);
			}
		}
	}
	map.setCenter(waypoints[0].marker.getPosition());
}

/**
 * This fucntion creats a waypoint object and adds the listeners to it. To
 * create a new object call "new createWaypoint(latitude,longitude)"
 * 
 * @param latitude
 *            the latitude of the waypoint to be created
 * @param longitude
 *            the latitude of the waypoint to be created
 * @returns the waypoint object with the latitude and longitude of the
 *          parameters passed in.
 */
function createWaypoint(latitude, longitude) {
	var latLng = new google.maps.LatLng(latitude, longitude);
	var path = route.getPath();
	path.push(latLng);
	var image = new google.maps.MarkerImage('images/red-dot.png',
			new google.maps.Size(32, 32), new google.maps.Point(0, 0));
	var markerOptions = {
		position : latLng,
		title : '' + length,
		map : map,
		draggable : true,
		icon : image,
	};

	var marker = new google.maps.Marker(markerOptions);
	var waypoint = new waypointObject(marker, length);
	waypoints[length] = waypoint;
	length++;
	// Adding Listener
	google.maps.event.addListener(marker, 'dragend', function() {
		movePoint(waypoint);
	});
	// Removal Listener
	google.maps.event.addListener(marker, 'rightclick', function() {
		removePoint(waypoint);
	});
	// Info box listener
	google.maps.event.addListener(marker, 'click', function() {
		openInfoWindow(waypoint);
	});
	return waypoint;
};

/**
 * This function gets the selected walk from the list of walks and sends the
 * request to the server to load that walk. The system then calls the dejson
 * method with the returned server results.
 */
function serializeLoadSend() {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.open("POST", "sendload.php", true);
	xmlhttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			dejson(xmlhttp.responseText);
			overlay();
			updateSideBar();
			walkDetailsBox("Walk loaded successfully");
		}
	};
	var index = document.getElementById("listofwalks").options[document
			.getElementById("listofwalks").selectedIndex].value;
	overlay();
	overridebox();
	xmlhttp.send('selectedwalk=' + listofwalks[index].id);
}

/**
 * This function deletes the selected walk from the list of walks and sends the
 * request to the server to delete that walk. The system then calls the alertbox
 * saying which walk was deleted
 */
function deleteSend() {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.open("POST", "sendelete.php", true);
	xmlhttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			overridebox();
			alertbox('Deleted walk with id: ' + xmlhttp.responseText);
		}
	};
	var index = document.getElementById("listofwalks").options[document
			.getElementById("listofwalks").selectedIndex].value;
	overlay();
	overridebox();
	xmlhttp.send('selectedwalk=' + listofwalks[index].id);
}
