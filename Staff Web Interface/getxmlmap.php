<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
/**
 * This file deals with the loading of the XML Map file. 
 * It then echos the contents back to what ever called it.
 *
 * @author Karl Parry (kdp8)
 */

include 'loadandsavefunctions.php';

$instance = new loadandsavefunctions();
$jsonobj = $instance->getXMLMap();
sleep(1);
echo $jsonobj;

?>
