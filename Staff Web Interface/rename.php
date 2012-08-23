<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
/**
 * This file deals with renaming. It generates a
 * random number between 1, 32767 and then checks
 * if a file already matches if it does it
 * generates a new number by  calling itself
 * again.
 *
 * @author Karl Parry (kdp8)
 */
Class rename
{
	function __construct()
	{
	}

	/**
	 * Rename function for images. Looks in 
	 * upload folder to find a unique filename.
	 */
	public function newFilename()
	{
		$newfilename = rand().".png";
		$dirnew = getcwd()."/upload/".$newfilename;
		if (file_exists($dirnew)){
			$newfilename = newFilename();
		}
		return $newfilename;
	}

	/**
	* Rename function for walks. Looks in
	* walks folder to find a unique filename.
	*/
	public function newXMLFilename()
	{
		$newfilename = rand(1,32767);
		$dirnew = getcwd()."/walks/".$newfilename.".xml";
		if (file_exists($dirnew)){
			$newfilename = newFilename();
		}
		return $newfilename;
	}

}

?>