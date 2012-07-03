<?php
/************************************************************************/
/* PHP Simple PasswordProtect v1.0                                      */
/* ===========================                                          */
/*                                                                      */
/*   Written by Steve Dawson - http://www.stevedawson.com               */
/*   Freelance Web Developer - PHP, MySQL, HTML programming             */
/*                                                                      */
/* This program is free software. You can redistribute it and/or modify */
/* but please leave this header intact, thanks                          */
/************************************************************************/
##########################################################################
$password = "cardigan";  // Modify Password to suit for access, Max 10 Char.
##########################################################################
?>
<!DOCTYPE html>
<html>
<head>
<title>Guided Walk Staff Web Interface</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<link rel="stylesheet" type="text/css" href="mapapi.css" />
<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?key=AIzaSyCmV8V2zidGBB8h35IIkzgxkj3MD75ai9g&sensor=false"></script>
<script type="text/javascript" src="javascript/alertbox.js"></script>
<script type="text/javascript" src="javascript/apiscript.js"></script>
<script type="text/javascript" src="javascript/emptymap.js"></script>
<script type="text/javascript" src="javascript/image.js"></script>
<script type="text/javascript" src="javascript/infowindow.js"></script>
<script type="text/javascript" src="javascript/remove.js"></script>
<script type="text/javascript" src="javascript/sendload.js"></script>
<script type="text/javascript" src="javascript/sendsave.js"></script>
</head>
<body onload="initialize()">


<?php
session_start();
if (isset($_POST["password"]) && ($_POST["password"]=="$password")) {
	?>
	<!-- START OF HIDDEN HTML - PLACE YOUR CONTENT HERE -->

	<div id="top_bar">
		<button id="walkdetailsbutton" type="button"
			onClick="walkDetailsBox();">Walk details</button>
		<button id="loadXMLbutton" type="button" onClick="loadXMLMap();">Load
			a Walk</button>
		<button id="loadXMLbutton" type="button" onClick="clearMap();">Clear
			Map</button>
		<button id="clearuploadbutton" type="button"
			onClick="clearUploadedFiles();">Clear Uploads Folder</button>
	</div>
	<div id="map_canvas"></div>
	<div id="hidden_frame">
		<iframe id="upload_target" name="upload_target" src=""></iframe>
	</div>
	<div id="overlay">
		<div id="overlaybox"></div>
	</div>

	<!-- END OF HIDDEN HTML -->
	
	
<?php 
}
else
{
// Wrong password or no password entered display this message
if (isset($_POST['password']) || $password == "") {
  print "<p align=\"center\"><font color=\"red\"><b>Incorrect Password</b><br>Please enter the correct password</font></p>";}
  print "<form method=\"post\"><p align=\"center\">Please enter your password for access<br>";
  print "<input name=\"password\" type=\"password\" size=\"25\" maxlength=\"10\"><input value=\"Login\" type=\"submit\"></p></form>";
}
?>
<BR>
</body>
</html>
