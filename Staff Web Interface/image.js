/**
 * This file handles images, adding and removing.
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * This function is called before the selected image is uploaded to teh server
 * and saves the waypoint details and checks that the selected file is of an
 * image type.
 * 
 * @param form
 *            the form linked to the information window currently selected to
 *            upload an image to.
 * @returns {Boolean} true if the image is a picture or false otherwise
 */
function checkForm(form) {
	var index = parseInt(form.id);
	var waypoint = waypoints[index];
	waypoint.title = form.title.value;
	waypoint.desc = form.desc.value;
	waypoint.welshtitle = form.welshtitle.value;
	waypoint.welshdesc = form.welshdesc.value;
	var filename = form.file.value;
	var extension = filename.split('.').pop();
	if ('JPG' === extension.toUpperCase() || 'GIF' === extension.toUpperCase()
			|| 'PNG' === extension.toUpperCase()
			|| 'JPEG' === extension.toUpperCase()) {
		overridebox();
		return true;
	} else {
		alertbox('Please choose a image file with extension .jpg, .jpeg, .gif or .png');
		return false;
	}
	updateSideBar();
}

/**
 * This method is called once the Web Server has deleted an image from the
 * uploads foler. Removes the image witht he imgindex from the waypoint with the
 * index parameter.
 * 
 * @param index
 *            is the index of the waypoint which is linked to the deleted image.
 * @param imgIndex
 *            is the index of the image being deleted.
 */
function removeImg(index, imgIndex) {
	var imageNo = parseInt(imgIndex);
	var waypoint = waypoints[index];
	waypoint.images.splice(imageNo, 1);
	waypoint.numberofimages--;
	var infoWindowContent = assembleInfoWindow(waypoint);
	var div = document.createElement('div');
	div.setAttribute("id", "infoForm" + waypoint.index);
	div.setAttribute("style", "width:465px; display: block;text-align:center;");
	div.innerHTML = infoWindowContent;
	waypoint.infoWindow.setContent(div);
}

/**
 * Adds the image to the waypoint object
 * 
 * @param location
 *            the location of the uploaded image on the Web Server
 * @param index
 *            the index of the waypoint the image is linked to.
 */
function addImg(location, index) {
	var waypoint = waypoints[index];
	waypoint.images[waypoint.numberofimages] = location;
	waypoint.numberofimages++;
	
	var infoWindowContent = assembleInfoWindow(waypoint);
	var div = document.createElement('div');
	div.setAttribute("id", "infoForm" + waypoint.index);
	div.setAttribute("style", "width:465px; display: block;text-align:center;");
	div.innerHTML = infoWindowContent;
	waypoint.infoWindow.setContent(div);
	updateSideBar();
}

/**
 * This funtion is called when an Upload has finished. It calls alert box
 * dependant on success or failure
 * 
 * @param status
 *            the staus of the upload either 1 or 0 meaning success or failue
 *            respectively.
 * @param location
 *            the location of the uploaded image on the server
 * @param message
 *            the error message returned from the server or the index of the
 *            waypoint if it was a success
 */
function uploadStatus(status, location, message) {
	if (status == '0') {
		if (location == '0') {
			alertbox('An error occured during upload please try again.');
		}
		if (location == '1') {
			alertbox('' + message);
		}
		if (location == '2') {
			alertbox('' + message);
		}
	} else if (status == '1') {
		alertbox("Uplaod Successful");
		addImg(location, message);
	}
}

/**
 * This functions handles the AJAX call to delete the image files in the Upload
 * folder on the Web Serve
 * 
 * @param filename
 *            the filename of image to be deleted
 * @param index
 *            the index of the waypoint linked to the image
 * @param imageIndex
 *            the index of the image
 * @param form
 *            the form in which the user selected the file in. This is used to
 *            save waypoint title and description before the information window
 *            is redrawn
 */
function deleteFile(filename, index, imageIndex, form) {
	var waypoint = waypoints[index];
	if(null!=form){
		overridebox();
		waypoint.title = form.title.value;
		waypoint.desc = form.desc.value;
		waypoint.welshtitle = form.welshtitle.value;
		waypoint.welshdesc = form.welshdesc.value;
	}
	if (((filename.indexOf("/")) == -1) && ((filename.indexOf("\\")) == -1)) {
		var parms = "filename=" + filename;
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera,
			// Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}

		xmlhttp.open("POST", "deletefile.php", true);
		xmlhttp.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				{
					if(null!=form){
						var results = xmlhttp.responseText;
						overlay();
						if (parseInt(results) == 0) {
							alertbox("File not found on server. Link removed");
							removeImg(index, imageIndex);
						} else if (parseInt(results) == 1) {
							alertbox("File deleted on server. Link removed");
							removeImg(index, imageIndex);
						} else if (parseInt(results) == 2) {
							alertbox("File error on server. Please try again");
						}
					}
				}
			}
		};
		xmlhttp.send(parms);
	}
	updateSideBar();
}

/**
 * This clears the upload folder on the Web Server. This is to remove an
 * orphaned images on the Server. As the Web Server is stateless and doesn't
 * remember why or who the images are from it must be done manually.
 */
function clearUploadedFiles() {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.open("POST", "clearUploads.php", true);
	xmlhttp.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
	xmlhttp.send();
}