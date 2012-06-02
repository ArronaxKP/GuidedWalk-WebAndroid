<?php
/**
* This file deals with the loading of a walk.
* It recieves a POST with the walk unique ID and
* then calls the load function passing the
* unique ID. It then returns the walk as a JSON string.
* 
* @author Karl Parry (kdp8)
*/
include 'loadandsavefunctions.php';

	$selectedwalk = $_POST['selectedwalk'].'.xml';
	$instance = new LoadAndSaveFunctions();
	$jsonobj = $instance->load($selectedwalk);
	echo $jsonobj;
?>