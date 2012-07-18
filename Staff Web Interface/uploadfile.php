<?php
/**
 * This file deals with the uplaoding of images. 
 * It recieves a file as a POST. PHP then checks 
 * to see it is an image an is less than 16MB in size.
 * It then moves the file to the uploads folder. Then 
 * uses the resize class to resize the image to a mobile
 * phone friendly size. 
 * Once this is done it returns the new file name and 
 * the text part of the POST.
 * 
 * @author Karl Parry (kdp8)
 */
	include 'resizeclass.php';
	if($_POST!=null){
		$_FILES['file']['name'];
		$textstuff = $_POST['text'];
		$results = '';
		if ((($_FILES["file"]["type"] == "image/gif")
		|| ($_FILES["file"]["type"] == "image/jpeg")
		|| ($_FILES["file"]["type"] == "image/pjpeg")
		|| ($_FILES["file"]["type"] == "image/jpg")
		|| ($_FILES["file"]["type"] == "image/png"))
		//Files no larger than 16MB can be uploaded.
		&& ($_FILES["file"]["size"] < 16777216))
		{
			if ($_FILES["file"]["error"] > 0)
			{
				$results  = "'0','0','0'"; //file error
			}
			else
			{
				$destination_path = "upload/".$_FILES["file"]["name"];
				move_uploaded_file($_FILES["file"]["tmp_name"],$destination_path);
				
				$instance = new resize($destination_path);
				$instance->resizeImage(320,480,"auto");
				unlink($destination_path);
				$instance->saveImage($destination_path , "100");
				
				$newfilename = $instance->newFilename();
				$dirnew = "upload/".$newfilename;
				rename($destination_path,$dirnew);
				$results  = "'1','".$newfilename."','".$textstuff."'";
			}
		}
		else
		{
		  $results  = "'0','2','Please select an image file (jpg, jpeg, pjpeg, gif, png only)'"; //Invalid file"
		}
		sleep(1);
	}else{
		sleep(1);
		echo "<p>No Post</p>";
	}
	
?>
    <script language="javascript" type="text/javascript">
    window.top.window.overlay();
    window.top.window.uploadStatus(<?php echo $results; ?>);
    </script>
	