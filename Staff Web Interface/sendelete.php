<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
/**
 * This file deals with the deleting of a walk. 
 * It recieves a POST with the walk unique ID and 
 * then calls the DeleteWalk function passing the 
 * unique ID. It then returns the Unique ID.
 * 
 * @author Karl Parry (kdp8)
 */
include 'loadandsavefunctions.php';
	$selectedwalk = $_POST['selectedwalk'];
	$instance = new LoadAndSaveFunctions();
	$instance->deleteWalk($selectedwalk);
	sleep(1);
	echo $selectedwalk;
?>