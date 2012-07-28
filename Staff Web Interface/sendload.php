<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
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
	sleep(1);
	echo $jsonobj;
?>