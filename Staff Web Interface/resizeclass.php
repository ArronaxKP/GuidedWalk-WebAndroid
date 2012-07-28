<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
/**
 * This class deals with image resizing. It is heavily influenced from
 * the White Hat Web Design blog on resizing with PHP.
 * http://www.white-hat-web-design.co.uk/blog/resizing-images-with-php/
 *
 * @author White Hat Web Design modified by Karl Parry (kdp8)
 *
 */
Class resize
{
	// *** Class variables
	private $image;
	private $width;
	private $height;
	private $imageResized;

	function __construct($fileName)
	{
		// *** Open up the file
		$this->image = $this->openImage($fileName);

		// *** Get width and height
		$this->width  = imagesx($this->image);
		$this->height = imagesy($this->image);
	}

	private function openImage($file)
	{
		// *** Get extension
		$extension = strtolower(strrchr($file, '.'));

		switch($extension)
		{
			case '.jpg':
			case '.jpeg':
				$img = @imagecreatefromjpeg($file);
				break;
			case '.gif':
				$img = @imagecreatefromgif($file);
				break;
			case '.png':
				$img = @imagecreatefrompng($file);
				break;
			default:
				$img = false;
			break;
		}
		return $img;
	}
	public function resizeImage($newWidth, $newHeight, $option="auto")
	{

		// *** Get optimal width and height - based on $option
		$optionArray = $this->getDimensions($newWidth, $newHeight, strtolower($option));

		$optimalWidth  = $optionArray['optimalWidth'];
		$optimalHeight = $optionArray['optimalHeight'];

		// *** Resample - create image canvas of x, y size
		$this->imageResized = imagecreatetruecolor($optimalWidth, $optimalHeight);
		imagecopyresampled($this->imageResized, $this->image, 0, 0, 0, 0, $optimalWidth, $optimalHeight, $this->width, $this->height);

	}
	public function getDimensions($newWidth, $newHeight, $option)
	{
		if($this->width<$newWidth){
			if($this->height<$newHeight){
				$newHeight = $this->height;
			}
			$newWidth = $this->width;
		} else if($this->height<$newHeight){
			if($this->width<$newWidth){
				$newWidth = $this->width;
			}
			$newHeight = $this->height;
		}
		$optionArray = $this->getSizeByAuto($newWidth, $newHeight);
		$optimalWidth = $optionArray['optimalWidth'];
		$optimalHeight = $optionArray['optimalHeight'];
		return array('optimalWidth' => $optimalWidth, 'optimalHeight' => $optimalHeight);
	}
	private function getSizeByFixedHeight($newHeight)
	{
		$ratio = $this->width / $this->height;
		$newWidth = $newHeight * $ratio;
		return $newWidth;
	}

	private function getSizeByFixedWidth($newWidth)
	{
		$ratio = $this->height / $this->width;
		$newHeight = $newWidth * $ratio;
		return $newHeight;
	}

	private function getSizeByAuto($newWidth, $newHeight)
	{
		if ($this->height < $this->width)
		// *** Image to be resized is wider (landscape)
		{
			$optimalWidth = $newWidth;
			$optimalHeight= $this->getSizeByFixedWidth($newWidth);
		}
		elseif ($this->height > $this->width)
		// *** Image to be resized is taller (portrait)
		{
			$optimalWidth = $this->getSizeByFixedHeight($newHeight);
			$optimalHeight= $newHeight;
		}
		else
		// *** Image to be resizerd is a square
		{
			if ($newHeight < $newWidth) {
				$optimalWidth = $newWidth;
				$optimalHeight= $this->getSizeByFixedWidth($newWidth);
			} else if ($newHeight > $newWidth) {
				$optimalWidth = $this->getSizeByFixedHeight($newHeight);
				$optimalHeight= $newHeight;
			} else {
				// *** Sqaure being resized to a square
				$optimalWidth = $newWidth;
				$optimalHeight= $newHeight;
			}
		}

		return array('optimalWidth' => $optimalWidth, 'optimalHeight' => $optimalHeight);
	}

	public function saveImage($savePath, $imageQuality="100")
	{
		// *** Scale quality from 0-100 to 0-9
		$scaleQuality = round(($imageQuality/100) * 9);

		// *** Invert quality setting as 0 is best, not 9
		$invertScaleQuality = 9 - $scaleQuality;

		if (imagetypes() & IMG_PNG) {
			imagepng($this->imageResized, $savePath, $invertScaleQuality);
		}
		imagedestroy($this->imageResized);
	}

	public function newFilename()
	{
		$newfilename = rand().".png";
		$dirnew = getcwd()."/upload/".$newfilename;
		if (file_exists($dirnew)){
			$newfilename = newFilename();
		}
		return $newfilename;
	}
}
?>