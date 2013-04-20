/**
 * This file deals with the clearing/emptying of the map
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * This function clears the map.
 */
function clearMap(){
	loadingbox("local");
	for ( var index = length - 1; index >= 0; index--) {
		var waypoint = waypoints[index];
		removePointForced2(waypoint);
		loadingbox("local","Loop: "+index);
	}
	uniqueid = 0;
	walktitle = "";
	walkdesc = "";
	welshwalktitle = "";
	welshwalkdesc = "";
	walkdifficulty = "1";
	version = "0";
	alertbox("Clear map completed");
}

/**
 * This remove the points on the map but doesn't do the icon changes like the
 * other removePointForced function
 * 
 * @param waypoint the waypoint that will be deleted.
 */
function removePointForced2(waypoint) {
	waypoint.marker.setMap(null);
	this.removePathPoint(waypoint);
	this.removePoints(waypoint);
	this.clearLinkedImg2(waypoint);
	length--;
	updateSideBar();
}

/**
 * This function deletes the linked images to the waypoint passed as a parameter.
 * 
 * @param waypoint the waypoint that the linked images are going to be deleted for,
 */
function clearLinkedImg2(waypoint) {

	for ( var i = 0; i < waypoint.numberofimages; i++) {
		var filename = waypoint.images[i];
		deleteFile2(filename, waypoint.index, waypoint.numberofimages);
	}
	waypoint.numberofimages = 0;
}

function deleteFile2(filename, index, imgNo) {
	if (((filename.indexOf("/")) == -1) && ((filename.indexOf("\\")) == -1)) {
		var parms = "filename=" + filename;
		var xmlhttp;
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
		} else {
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.open("POST", "deletefile.php", true);
		xmlhttp.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		xmlhttp.send(parms);
	}
}