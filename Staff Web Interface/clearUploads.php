<?php
/**
 * This deals with the recurive delete function.
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * 
 * This function recurisvely deletes a file in the folder
 * @param String $str
 */
function recursiveDelete($str){
	if(is_file($str))
	{
		return @unlink($str);
	}
	elseif(is_dir($str))
	{
		$scan = glob(rtrim($str,'/').'/*');
		foreach($scan as $index=>$path){
			recursiveDelete($path);
		}
	}
}

recursiveDelete(getcwd()."/upload");
	
?>