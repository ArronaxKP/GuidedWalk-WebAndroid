/**
 * This  Javascript file deals with the removing of waypoints 
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * This method creates a confirm box asking if you are sure about the removal of
 * a waypoint
 * 
 * @param waypoint
 *            the waypoint which was right clicked on. This is used to get index
 *            for the removal if confirm is selected.
 */
function removePoint(waypoint) {
	confirmbox(waypoint.index);
}

/**
 * Gets waypoint from the passed index and then calles the removePointForced.
 * This does not ask for a confirm.
 * 
 * @param index
 *            the index of the waypoint to be deleted
 */
function removePointForcedIndex(index) {
	var waypoint = waypoints[index]; 
	removePointForced(waypoint);
	overlay();
}

/**
 * This function deletes a waypoint. This also removes the marker from the map,
 * deletes the point form the polyline creating the route and also deletes any
 * images that are on the server linked to this waypoint. This is the upload
 * folder not the image saved in an XML file. Also redraws icons around this one
 * if any need to be changed
 * 
 * @param waypoint
 *            the waypoint to be deleted
 */
function removePointForced(waypoint) {
	if(waypoint.index == 1){
		var mktmp = waypoints[0];
		var tmpimage;
		if (mktmp.title.replace(/\s/g, "") != "" || mktmp.desc.replace(/\s/g, "") != ""
			|| mktmp.numberofimages != 0) {
			tmpimage = new google.maps.MarkerImage('images/green-blue-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
		}else{
			tmpimage = new google.maps.MarkerImage('images/green-dot.png',
				new google.maps.Size(32, 32), new google.maps.Point(0, 0));
		}
		mktmp.marker.setIcon(tmpimage);
	} else if (waypoint.index == 0) {
		if(length != 1){
			var mktmp = waypoints[1];
			var tmpimage;
			if (mktmp.title.replace(/\s/g, "") != "" || mktmp.desc.replace(/\s/g, "") != ""
				|| mktmp.numberofimages != 0) {
				tmpimage = new google.maps.MarkerImage('images/green-blue-dot.png',
						new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			}else{
				tmpimage = new google.maps.MarkerImage('images/green-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			}
			mktmp.marker.setIcon(tmpimage);
		}
	} else if (1 < length) {
		if (waypoint.index == length - 1) {
			var mktmp = waypoints[length - 2];
			var tmpimage;
			if (mktmp.title.replace(/\s/g, "") != "" || mktmp.desc.replace(/\s/g, "") != ""
				|| mktmp.numberofimages != 0) {
				tmpimage = new google.maps.MarkerImage('images/final-dot-blue.png',
						new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			}else{
				tmpimage = new google.maps.MarkerImage('images/final-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			}
			mktmp.marker.setIcon(tmpimage);
		}
	}
	this.removePathPoint(waypoint);
	this.removePoints(waypoint);
	this.clearLinkedImg(waypoint);
	waypoint.marker.setMap(null);
	length--;
}

/**
 * Iterates through all the linked images to the passed waypoint parameter and
 * Calls the deleteFile function on each.
 * 
 * @param waypoint
 *            the waypoint being removed
 */
function clearLinkedImg(waypoint) {
	for ( var i = 0; i < waypoint.numberofimages; i++) {
		var filename = waypoint.images[i];
		deleteFile(filename, waypoint.index, waypoint.numberofimages,null);
	}
	waypoint.numberofimages = 0;
}

/**
 * This function removes the point on the polyline line where the waypoint use
 * to be. Then redraws polyline linking up the waypoints.
 * 
 * @param waypoint
 *            the waypoint being removed
 */

function removePathPoint(waypoint) {
	var path = route.getPath();
	for ( var idex = waypoint.index + 1; idex < length; idex++) {
		var tempPathPoint = path.getAt(idex);
		path.setAt(idex - 1, tempPathPoint);
	}
	path.removeAt(length - 1);
}

/**
 * This removes the waypoint object. It removes it from the array and then
 * tidies the array filling the gap and fixing indexes.
 * 
 * @param waypoint
 *            the waypoint being removed
 */
function removePoints(waypoint) {
	for ( var idex = waypoint.index + 1; idex < length; idex++) {
		var tempPoint = waypoints[idex];
		var k = idex - 1;
		tempPoint.index = k;
		tempPoint.marker.setTitle('' + k);
		waypoints[idex - 1] = tempPoint;
		if (tempPoint.infoWindow != null) {
			var newContent = assembleInfoWindow(tempPoint);
			tempPoint.infoWindow.setContent(newContent);
		}
	}
}