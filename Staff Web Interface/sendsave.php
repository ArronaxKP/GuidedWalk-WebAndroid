<?php
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
		$walkdifficulty = $myobject['walkdifficulty'];
		$route = $myobject['route'];
		$instance = new LoadAndSaveFunctions();
		$uniqueid = $instance->save($uniqueid,$walklength,$walktitle,$walkdesc,$walkdifficulty,$route);
		echo "".$uniqueid;//passed
	}else{
		echo "";//failed
	}
?>