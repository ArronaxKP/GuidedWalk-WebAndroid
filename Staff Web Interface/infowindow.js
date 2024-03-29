/**
 * This Javascript file deals with the information windows linked to the
 * waypoints
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * This function deals with creating an information window and linking it to the
 * passed waypoint parameter. It also changes the waypoint icon to show the
 * linked information window
 * 
 * @param waypoint
 *            the waypoint the information window will link to.
 */
function createInfoWindow(waypoint) {
	var infoWindow;
	var infoWindowContent = assembleInfoWindow(waypoint);
	var div = document.createElement('div');
	div.setAttribute("id", "infoForm" + waypoint.index);
	div.setAttribute("style", "width:465px; display: block;text-align:center;");
	div.innerHTML = infoWindowContent;
	infoWindow = new google.maps.InfoWindow({
		maxWidth : 500,
		content : div
	});
	google.maps.event.addListener(infoWindow, 'closeclick', function() {
		closeSave(waypoint.index);
	});
	waypoints[waypoint.index].infoWindow = infoWindow;
	if (waypoint.index == 0) {
		var image = new google.maps.MarkerImage('images/green-blue-dot.png',
				new google.maps.Size(32, 32), new google.maps.Point(0, 0));
		waypoints[waypoint.index].marker.setIcon(image);
	} else if (waypoint.index == length - 1) {
		var image = new google.maps.MarkerImage('images/final-dot-blue.png',
				new google.maps.Size(32, 32), new google.maps.Point(0, 0));
		waypoints[waypoint.index].marker.setIcon(image);
	} else {
		var image = new google.maps.MarkerImage('images/blue-dot.png',
				new google.maps.Size(32, 32), new google.maps.Point(0, 0));
		waypoints[waypoint.index].marker.setIcon(image);
	}
}

/**
 * This handles the deleting of an information window from a waypoint along with
 * the waypoint title and description
 * 
 * @param event
 *            the mouse click event is passed to stop Propagation and creating a
 *            waypoint on the map.
 * @param form
 *            the linked from is passed to get the waypoint index and to delete
 *            its contents and linked information
 */
function deleteInfoWindow(event, form) {
	// http://www.mappingsupport.com/forum_post/context_menu.html#
	if (event.stopPropagation) {
		event.stopPropagation();
	} else {
		event.cancelBubble = true;
	}
	// Next line is for IE
	event.returnValue = false;
	var index = parseInt(form.id);
	var waypoint = waypoints[index];
	infoWindow = waypoint.infoWindow;
	infoWindow.close();
	waypoint.infoWindow = null;
	waypoint.title = '';
	waypoint.desc = '';
	waypoint.welshtitle = '';
	waypoint.welshdesc = '';
	if (waypoint.numberofimages == 0) {
		if (waypoint.index == 0) {
			var image = new google.maps.MarkerImage('images/green-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		} else if (waypoint.index == length - 1) {
			var image = new google.maps.MarkerImage('images/final-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		} else {
			var image = new google.maps.MarkerImage('images/red-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		}
	}
	updateSideBar();
}

/**
 * This deals with the openeing of an inforamtion window. This is called when a
 * waypoint is left clicked on. If a waypoint doesn't have an inforation window
 * linked it creates a new inforamtion window and links it to the waypoint. If
 * there already has a link inforamtion window it opens opens the linked
 * information window.
 * 
 * @param waypoint
 *            the waypoint that was left click on.
 */
function openInfoWindow(waypoint) {
	if (waypoint.infoWindow == null) {
		this.createInfoWindow(waypoint);
	}
	waypoint.infoOpen = true;
	waypoint.infoWindow.open(map, waypoint.marker);

}

function closeSave(index) {
	var div = document.getElementById("infoForm" + index);
	var form = div.firstChild;
	saveDetails(form);
	waypoints[index].infoOpen = false;
}

function changeView(index, view, event, form){
	if(event != null){
		// http://www.mappingsupport.com/forum_post/context_menu.html#
		if (event.stopPropagation) {
			event.stopPropagation();
		} else {
			event.cancelBubble = true;
		}	
		// Next line is for IE
		event.returnValue = false;
	}
	if(view == 'english'){
		var el = document.getElementById("waypointEnglish"+index);
		el.style.display = "block";
		var e2 = document.getElementById("waypointWelsh"+index);
		e2.style.display = "none";
		var e3 = document.getElementById("waypointImages"+index);
		e3.style.display = "none";
	} else if(view == 'welsh'){
		var el = document.getElementById("waypointEnglish"+index);
		el.style.display = "none";
		var e2 = document.getElementById("waypointWelsh"+index);
		e2.style.display = "block";
		var e3 = document.getElementById("waypointImages"+index);
		e3.style.display = "none";
	} else if(view == 'images'){
		var el = document.getElementById("waypointEnglish"+index);
		el.style.display = "none";
		var e2 = document.getElementById("waypointWelsh"+index);
		e2.style.display = "none";
		var e3 = document.getElementById("waypointImages"+index);
		e3.style.display = "block";
	}
	redrawInfoWindow(index,form);
}

function redrawInfoWindow(index,form){
	var waypoint = waypoints[index];
	if(form!=null){
		var title = form.elements["title"].value;
		var desc = form.elements["desc"].value;
		var welshtitle = form.elements["welshtitle"].value;
		var welshdesc = form.elements["welshdesc"].value;
		var waypoint = waypoints[index];
		waypoint.title = title;
		waypoint.desc = desc;
		waypoint.welshtitle = welshtitle;
		waypoint.welshdesc = welshdesc;
	} else {
		waypoint.infoOpen = false;
		waypoint.infoWindow.close();
	}
	updateSideBar();
	waypoint.infoOpen = false;
	openInfoWindowIndex(index);
}


/**
 * This create the the HTML syntax that is put into the information window's
 * contents. This is then rendered what an inforamtion window opens.
 * 
 * @param waypoint
 *            the waypoint object linked to the information window
 * @returns {String} the HTML syntax to set as the information window's contents
 */
function assembleInfoWindow(waypoint) {
	var imgLinks = assembleImgLinks(waypoint);
	var newContent = '<form id="'
			+ waypoint.index
			+ '" name="'
			+ waypoint.index
			+ '" method="post" enctype="multipart/form-data" action="uploadfile.php" target = '
			+ '"upload_target" onSubmit="return checkForm(this)">'
		
		
			+ '<div id="waypointEnglish'+ waypoint.index +'" style="display:block">'
			+ '<input type="button" value="English" onClick="changeView('+ waypoint.index +', \'english\',event,this.form)">'
			+ '<input type="button" value="Welsh" onClick="changeView('+ waypoint.index +', \'welsh\',event,this.form)">'	
			+ '<input type="button" value="Images" onClick="changeView('+ waypoint.index +', \'images\',event,this.form)">'		
			+ '<br />'
			+ '<table border="0"><tr>'
			+ '<td>English Title: <br /><textarea rows="4" cols="53" name="title" id="title">'
			+ waypoint.title
			+ '</textarea></td>'
			+ '</tr><tr>'
			+ '<td>English Description: <br /><textarea rows="7" cols="53" name="desc" id="desc">'
			+ waypoint.desc
			+ '</textarea></td>'
			+ '</tr></table>'
			+ '</div>'

			
			+ '<div id="waypointWelsh'+ waypoint.index +'" style="display:none">'
			+ '<input type="button" value="English" onClick="changeView('+ waypoint.index +', \'english\',event,this.form)">'
			+ '<input type="button" value="Welsh" onClick="changeView('+ waypoint.index +', \'welsh\',event,this.form)">'	
			+ '<input type="button" value="Images" onClick="changeView('+ waypoint.index +', \'images\',event,this.form)">'		
			+ '<br />'
			+ '<table border="0"><tr>'
			+ '<td>Welsh Title: <br /><textarea rows="4" cols="53" name="welshtitle" id="welshtitle">'
			+ waypoint.welshtitle
			+ '</textarea></td>'
			+ '</tr><tr>'
			+ '<td>Welsh Description: <br /><textarea rows="7" cols="53" name="welshdesc" id="welshdesc">'
			+ waypoint.welshdesc
			+ '</textarea></td>'
			+ '</tr></table>'
			+ '</div>'

			+ '<div id="waypointImages'+ waypoint.index +'" style="display:none">'
			+ '<input type="button" value="English" onClick="changeView('+ waypoint.index +', \'english\',event,this.form)">'
			+ '<input type="button" value="Welsh" onClick="changeView('+ waypoint.index +', \'welsh\',event,this.form)">'	
			+ '<input type="button" value="Images" onClick="changeView('+ waypoint.index +', \'images\',event,this.form)">'		
			+ '<br />'			
			+ '<input name="file" id="file" size="10" type="file" />'
			+ '<input type="submit" name="action" value="Upload Image" /> <br />'
			+ imgLinks 
			+ '</div>'
			
			
			+ '<input type="button" value="Submit" onClick="getInfoDetails(event,this.form)">'
			+ '</input><input type="button" value="Delete" onClick="deleteInfoWindow(event,this.form)">'
			+ '</input>'
			+ '<input type=hidden name="text" id="text" value="'+waypoint.index+'" style="visibility:hidden" />'
			+ '</form></div>';

	return newContent;
}

/**
 * This sets the information windows contents to be the new contents created
 * using the assembleInfoWindow function.
 * 
 * @param index
 *            the waypoint index currently selected
 * @param title
 *            the new waypoint title
 * @param desc
 *            the new waypoint description
 */

function setInfoWindow(index, title, desc, welshtitle, welshdesc) {
	var waypoint = waypoints[index];
	waypoint.title = title;
	waypoint.desc = desc;
	waypoint.welshtitle = welshtitle;
	waypoint.welshdesc = welshdesc;

	var infoWindowContent = assembleInfoWindow(waypoint);
	var div = document.createElement('div');
	div.setAttribute("id", "infoForm" + waypoint.index);
	div.setAttribute("style", "width:465px; display: block; text-align:center;");
	div.innerHTML = infoWindowContent;

	waypoint.infoWindow.setContent(div);
	waypoint.infoOpen = false;
	waypoint.infoWindow.close();
	updateSideBar();
}

function saveDetails(form) {
	var index = parseInt(form.id);
	var title = form.elements["title"].value;
	var desc = form.elements["desc"].value;
	var welshtitle = form.elements["welshtitle"].value;
	var welshdesc = form.elements["welshdesc"].value;
	var waypoint = waypoints[index];
	if (title.replace(/\s/g, "") == "" && desc.replace(/\s/g, "") == ""
			&& welshtitle.replace(/\s/g, "") == ""
			&& welshdesc.replace(/\s/g, "") == ""
			&& waypoint.numberofimages == 0) {
		infoWindow = waypoint.infoWindow;
		waypoint.infoOpen = false;
		infoWindow.close();
		waypoint.infoWindow = null;
		waypoint.title = '';
		waypoint.desc = '';
		waypoint.welshtitle = '';
		waypoint.welshdesc = '';
		if (waypoint.index == 0) {
			var image = new google.maps.MarkerImage('images/green-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		} else if (waypoint.index == length - 1) {
			var image = new google.maps.MarkerImage('images/final-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		} else {
			var image = new google.maps.MarkerImage('images/red-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		}
	} else {
		setInfoWindow(index, title, desc, welshtitle, welshdesc);
	}
	updateSideBar();
}

/**
 * This gets the Inforamtion window details when the submit button is pressed.
 * It gets the details and saves them in the waypoint object.
 * 
 * @param event
 *            the left more click event which is passed to stop propagation and
 *            creating a waypoint on the map.
 * @param form
 *            the form which contains the new waypoint details
 */
function getInfoDetails(event, form) {
	// http://www.mappingsupport.com/forum_post/context_menu.html#
	if (event.stopPropagation) {
		event.stopPropagation();
	} else {
		event.cancelBubble = true;
	}
	// Next line is for IE
	event.returnValue = false;

	var index = parseInt(form.id);

	var title = form.title.value;
	var desc = form.desc.value;
	var welshtitle = form.welshtitle.value;
	var welshdesc = form.welshdesc.value;
	var waypoint = waypoints[index];
	if (title.replace(/\s/g, "") == "" && desc.replace(/\s/g, "") == ""
			&& welshtitle.replace(/\s/g, "") == ""
			&& welshdesc.replace(/\s/g, "") == ""
			&& waypoint.numberofimages == 0) {
		infoWindow = waypoints[index].infoWindow;
		waypoint.infoOpen = false;
		infoWindow.close();
		waypoint.infoWindow = null;
		waypoint.title = '';
		waypoint.desc = '';
		waypoint.welshtitle = '';
		waypoint.welshdesc = '';
		if (waypoint.index == 0) {
			var image = new google.maps.MarkerImage('images/green-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		} else if (waypoint.index == length - 1) {
			var image = new google.maps.MarkerImage('images/final-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		} else {
			var image = new google.maps.MarkerImage('images/red-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			waypoints[waypoint.index].marker.setIcon(image);
		}
	} else {
		setInfoWindow(index, title, desc, welshtitle, welshdesc);
	}
	updateSideBar();
}

/**
 * Creates the HTML syntax for the information window that links the images to
 * the waypon. This shows a small icon of the image on the information window
 * 
 * @param waypoint
 * @returns
 */
function assembleImgLinks(waypoint) {
	var results;
	if (waypoint.numberofimages != 0) {
		results = '<table border="0" align="center">';
		for ( var i = 0; i < waypoint.numberofimages; i++) {
			var filename = waypoint.images[i];
			if (i % 3 == 0) {
				results += '<tr>';
			}
			results += '<td><a href=\"upload/'
					+ filename
					+ '\" target=\"_blank\"><img height=\"75px\" alt=\"'
					+ i
					+ '\"src=\"upload/'
					+ filename
					+ '\"></a></img><br /><button onClick=\"deleteFile(\''
					+ filename
					+ '\',\''
					+ waypoint.index
					+ '\',\''
					+ i
					+ '\',this.form)\" type=\"button\" >Delete Image</button> </td>';
			if (i % 3 == 2) {
				results += '</tr>';
			}
		}
		results += '</table>';
	} else {
		results = '';
	}
	return results;
}

function openInfoWindowIndex(index){
	var marker = waypoints[index];
	if(marker.infoOpen){
		marker.infoOpen = false;
		closeSave(index);
	} else {
		marker.infoOpen = true;
		openInfoWindow(marker);
	}
}