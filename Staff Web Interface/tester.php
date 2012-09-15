 <?php 
 error_reporting(E_ALL);
ini_set('display_errors', '1');
 
 $File = "YourFile2.txt"; 
 echo getcwd() . "\n";
 $Handle = fopen(getcwd()."/".$File, 'w');
 if(!$Handle){
	 $Data = "Did it work?\n"; 
	 fwrite($Handle, $Data); 
	 $Data = "Bilbo Jones\n"; 
	 fwrite($Handle, $Data); 
	 echo "Data Written\n"; 
	 fclose($Handle); 
 }

 ?>