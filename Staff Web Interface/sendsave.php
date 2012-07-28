<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
/**
* This file deals with the saving of a walk.
* It recieves a POST with the walk details, 
* unique ID, title, description, and route 
* etc. It then calls the save function passing 
* the walk details. Echos Unique ID when save completes.
*
* @author Karl Parry (kdp8)
*/

include 'loadandsavefunctions.php';
	if($_POST!=null){
		$myobject = json_decode( $_POST['serialized'], true );
		$uniqueid = $myobject['id'];
		$walklength = $myobject['walklength'];
		$walktitle = $myobject['walktitle'];
		$walkdesc = $myobject['walkdesc'];
		$welshwalktitle = $myobject['welshwalktitle'];
		$welshwalkdesc = $myobject['welshwalkdesc'];
		$walkdifficulty = $myobject['walkdifficulty'];
		$version = $myobject['version'];
		$route = $myobject['route'];
		$instance = new LoadAndSaveFunctions();
		$uniqueid = $instance->save($uniqueid,$walklength,$walktitle,$walkdesc,$welshwalktitle,$welshwalkdesc,$walkdifficulty,$version,$route);
		sleep(1);
		echo "".$uniqueid;//passed
	}else{
		sleep(1);
		echo "";//failed
	}
?>