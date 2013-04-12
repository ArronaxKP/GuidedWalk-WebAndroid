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
	var string = '';
	if (text) {
		string += '<p style="color:red;"><b>' + text + '</b></p>';
	}
	var difficultystring = walkDifficultyString();

	string += '<p>Type Walk Details below</p>'
			+ '<form id="walk_info" name="walk_info">'
			+ '<table border="0" align="center"><tr>'
			+ '<td>Walk Title: <textarea rows="4" cols="33" name="title" id="title">'
			+ walktitle
			+ '</textarea></td>'
			
			+ '<td>Welsh Walk Title: <textarea rows="4" cols="33" name="welshtitle" id="welshtitle">'
			+ welshwalktitle
			+ '</textarea></td>'
			
			+ '</tr><tr>'
			
			+ '<td>Walk Description: <textarea rows="8" cols="33" name="desc" id="desc">'
			+ walkdesc
			+ '</textarea></td>'
			
			+ '<td>Welsh Walk Description: <textarea rows="8" cols="33" name="welshdesc" id="welshdesc">'
			+ welshwalkdesc
			+ '</textarea></td>'
			+ '</tr></table>'
			+ difficultystring
			+ '<p>Version: '+ version +' || Published Version: '+ publishversion +'</p>'
			+ '<p>'
			+ '<button id="send" type="button" onClick="saveWalktoServer(this.form);">Save</button>'
			+ '<button id="send" type="button" onClick="saveWalktoServerNew(this.form);">Save As</button>'
			+ '<button id="send" type="button" onClick="publishWalktoServer(this.form);">Publish</button>'
			+ '<button type="button" onClick="closeWalkDetailsBox(this.form);">Close</button></p>'
			+ '</form>';
	document.getElementById('overlaybox').innerHTML = string;

	if (!text) {
		overlay();
	}
}

/**
 * This function creates radio buttons as a string and returns the string with
 * the correct difficulty selected. This string is then normally inserted in the
 * alertbox.
 */
function walkDifficultyString() {
	var tempstring = '';
	if (walkdifficulty == 0) {
		tempstring = '<input type="radio" name="difficulty" value="Easy" checked="checked"/> Easy'
				+ '<input type="radio" name="difficulty" value="Normal" /> Normal'
				+ '<input type="radio" name="difficulty" value="Hard" /> Hard <br />';
	}
	if (walkdifficulty == 1) {
		tempstring = '<input type="radio" name="difficulty" value="Easy" /> Easy'
				+ '<input type="radio" name="difficulty" value="Normal" checked="checked"/> Normal'
				+ '<input type="radio" name="difficulty" value="Hard" /> Hard <br />';
	}
	if (walkdifficulty == 2) {
		tempstring = '<input type="radio" name="difficulty" value="Easy"/> Easy'
				+ '<input type="radio" name="difficulty" value="Normal" /> Normal'
				+ '<input type="radio" name="difficulty" value="Hard" checked="checked"/> Hard <br />';
	}
	return tempstring;
}

/**
 * This is the function that is called when the map details dialog is closed and
 * saves the walk description and walk title.
 * 
 * @param form
 *            is the linked form which is passed when the function is called. It
 *            is passed so the function can get the title and description out of
 *            it
 */
function closeWalkDetailsBox(form) {
	saveFormDetails(form);
	overlay();
}

/**
 * This is the function that saves the walk description and walk title and
 * difficulty.
 * 
 * @param form
 *            is the linked form which is passed when the function is called. It
 *            is passed so the function can get the title and description out of
 *            it
 */
function saveFormDetails(form) {
	walktitle = form.title.value;
	walkdesc = form.desc.value;
	welshwalktitle = form.welshtitle.value;
	welshwalkdesc = form.welshdesc.value;
	walkdifficulty = getDifficultyValue(form);
}

/**
 * This function finds out which difficulty was chose by iterating through each
 * radio button and checking if it is checked.
 * 
 * @param form
 *            is the linked form which is passed when the function is called. It
 *            is passed so the function can get the radio buttons out of it
 */
function getDifficultyValue(form) {
	var difficulty = '';
	for ( var i = 0; i < form.difficulty.length; i++) {
		if (form.difficulty[i].checked) {
			difficulty = '' + i;
		}
	}
	return difficulty;
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
			+ '<button type="button" onClick="overlay();">Close</button>'
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
