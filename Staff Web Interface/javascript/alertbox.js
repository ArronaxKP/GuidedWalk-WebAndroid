/**
 * This  Javascript file deals with the modal dialogs
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * This create a modal dialog which presents the user with the walk details and
 * the Save and Save As button. This is influenced from the following link
 * http://raventools.com/blog/create-a-modal-dialog-using-css-and-javascript/
 * 
 * @param text
 *            can contain HTML syntax or just plain text. This param is placed
 *            inside
 *            <p>
 *            </p>
 *            tags at the top of the dialog box. This param is normally update
 *            messages such as Walk saved. If it is ommited the standard
 *            maessage "Type Walk Details below" appears.
 */
function walkDetailsBox(text) {
	var string;
	if (!text) {
		string = '<p>Type Walk Details below</p>'
				+ '<form id="walk_info" name="walk_info">'
				+ '<p>Walk Title: <br /><textarea rows="1" cols="34" name="title" id="title">'
				+ walktitle
				+ '</textarea></p>'
				+ '<p>Walk Description: <br /><textarea rows="3" cols="34" name="desc" id="desc">'
				+ walkdesc
				+ '</textarea></p> '
				+ '<p>'
				+ '<button id="send" type="button" onClick="saveWalktoServer(this.form);">Save</button>'
				+ '<button id="send" type="button" onClick="saveWalktoServerNew(this.form);">Save As</button>'
				+ '<button type="button" onClick="closeWalkDetailsBox(this.form);">Close</button></p>'
				+ '</form>';
		document.getElementById('overlaybox').innerHTML = string;
		overlay();
	} else {
		string = '<p style="color:red;"><b>'
				+ text
				+ '</b></p>'
				+ '<form id="map_info" name="map_info">'
				+ '<p>Walk Title: <br /><textarea rows="1" cols="34" name="title" id="title">'
				+ walktitle
				+ '</textarea></p>'
				+ '<p>Walk Description: <br /><textarea rows="3" cols="34" name="desc" id="desc">'
				+ walkdesc
				+ '</textarea></p> '
				+ '<p>'
				+ '<button id="send" type="button" onClick="saveWalktoServer(this.form);">Save</button>'
				+ '<button id="send" type="button" onClick="saveWalktoServerNew(this.form);">Save As</button>'
				+ '<button type="button" onClick="closeWalkDetailsBox(this.form);">Close</button></p>'
				+ '</form>';
		document.getElementById('overlaybox').innerHTML = string;
	}
}

/**
 * This is the function that is called when the mapdetails dialog is closed and
 * saves the walk description and walk title.
 * 
 * @param form
 *            is the linked form which is passed when the function is called. It
 *            is passed so the function can get the title and description out of
 *            it
 */
function closeWalkDetailsBox(form) {
	walktitle = form.title.value;
	walkdesc = form.desc.value;
	overlay();
}

/**
 * This create an alert modal dialog which presents the user with an error
 * message pased on the passed parameter.
 * 
 * @param text
 *            can contain HTML syntax or just plain text. This param is placed
 *            inside
 *            <p>
 *            </p>
 *            tags
 */
function alertbox(text) {
	var string;
	string = '<form id="alert_info" name="alert_info">'
			+ '<p>'
			+ text
			+ '</p>'
			+ '<p><button type="button" onClick="overlay();">Close</button></p>'
			+ '</form>';
	document.getElementById('overlaybox').innerHTML = string;
	overlay();
}

/**
 * This create an alert modal dialog which presents the user with an error
 * message pased on the passed parameter.
 * 
 * @param text
 *            can contain HTML syntax or just plain text. This param is placed
 *            inside
 *            <p>
 *            </p>
 */
function confirmbox(index) {
	var string;
	string = '<form id="confrim_info" name="confrim_info">'
			+ '<p>If you delete this, all associated information and pictures '
			+ 'will also be removed from the server.</p>'
			+ '<p><button type="button" onClick="removePointForcedIndex('
			+ index
			+ ');">Yes</button><button type="button" onClick="overlay();">No</button></p>'
			+ '</form>';
	document.getElementById('overlaybox').innerHTML = string;
	overlay();
}

/**
 * This create an overlay box that prevents people from doing anything until the
 * action is completed.
 */
function overridebox() {
	var string;
	string = '<p>Please wait while server works</p>'
		+ '<img align="center" src="images/ajax-loader.gif" alt="Loading Icon" />';
			
	document.getElementById('overlaybox').innerHTML = string;
	overlay();
}
/**
 * This function flips the overlay. So therefore if it is visible and you call
 * this method it goes invisible and vice versa.
 */
function overlay() {
	el = document.getElementById("overlay");
	el.style.visibility = (el.style.visibility == "visible") ? "hidden"
			: "visible";
}
