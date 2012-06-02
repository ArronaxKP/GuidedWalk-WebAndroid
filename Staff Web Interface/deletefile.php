<?php
/**
 * This PHP file recieves a POST containing a filename.
 * PHP then deletes the file in the upload folder that 
 * matches that file.
 * 
 * @author Karl Parry (kdp8)
 */
$filename = $_POST['filename'];
$results = '3';
if (file_exists("upload/".$filename))
{
	//file found and deleted
	if (!unlink("upload/".$filename))
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
