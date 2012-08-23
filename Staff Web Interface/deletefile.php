<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
/**
 * This PHP file recieves a POST containing a filename.
 * PHP then deletes the file in the upload folder that 
 * matches that file.
 * 
 * @author Karl Parry (kdp8)
 */
$filename = $_POST['filename'];
$results = '3';
if (file_exists(getcwd()."/upload/".$filename))
{
	//file found and deleted
	if (!unlink(getcwd()."/upload/".$filename))
	{
		//Error deleting;
		$results = '2';
	}
	else
	{
		//Deleted file
		$results = '1';
	}
}
else
{	//file not found
$results  = '0';
}
sleep(1);
echo $results;
?>
