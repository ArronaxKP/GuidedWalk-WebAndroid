<?php
include 'rename.php';

/**
 * This is the main PHP file. It handles all the
 * saving and loading from XML files. It uses
 * the XML DOM parser
 *
 * @author Karl Parry (kdp8)
 *
 */
Class LoadAndSaveFunctions
{
	function __construct()
	{
	}

	/**
	 * This function loads a walk that matches the $selectedwalk parameter.
	 * Enter description here ...
	 * @param $selectedwalk the selected walk's unique ID
	 * @return JSON object containing the walk data. The JSON object is 
	 * 			an array set out like below: -
	 * 			"array('id' => $id, 'walk_length' => $walklength ,
	 *  		'walk_title' => $walktitle , 'walk_desc' => $walkdesc,
	 *  		'route' => $route)"
	 */
	public function load($selectedwalk)
	{
		$myobjecj;
		$walklength;
		$walktitle;
		$walkdesc;
		$walkdifficulty;
		$version;
		$route;
		$id;
		$index=0;
		$numberofimages=0;

		$instance = new rename();
		$xmlDoc = new DOMDocument();
		$xmlDoc->load("walks/".$selectedwalk);
		$x = $xmlDoc->documentElement;
		foreach ($x->childNodes AS $item)
		{
			switch($item->nodeName){
				case "id";
				$id = $item->nodeValue;
				break;
				case "walk_length";
				$walklength = $item->nodeValue;
				break;
				case "walk_title";
				$walktitle = $item->nodeValue;
				break;
				case "walk_desc";
				$walkdesc = $item->nodeValue;
				break;
				case "walk_difficulty";
				$walkdifficulty = $item->nodeValue;
				break;
				case "version";
				$version = $item->nodeValue;
				break;
				case "route";
				foreach ($item->childNodes AS $waypoints)
				{
					switch($waypoints->nodeName){
						case "waypoint";
						foreach ($waypoints->childNodes AS $waypoint)
						{
							switch($waypoint->nodeName){
								case "waypoint_title";
								$route[$index][$waypoint->nodeName] = $waypoint->nodeValue;
								break;
								case "waypoint_desc";
								$route[$index][$waypoint->nodeName] = $waypoint->nodeValue;
								break;

								case "lat";
								$route[$index][$waypoint->nodeName] = $waypoint->nodeValue;
								break;

								case "lng";
								$route[$index][$waypoint->nodeName] = $waypoint->nodeValue;
								break;

								case "number_of_images";
								$route[$index][$waypoint->nodeName] = $waypoint->nodeValue;
								$numberofiImages=$waypoint->nodeValue;
								break;

								case "images";
								$image_index = 0;
								foreach ($waypoint->childNodes AS $images)
								{
									switch($images->nodeName){
										case "image";
										foreach ($images->childNodes AS $image)
										{
											switch($image->nodeName){
												case "image_base64";
												$img = base64_decode($image->nodeValue);
												$filename = $instance->newFilename();
												$dir = "upload/".$filename;
												$ourFileHandle = fopen($dir, 'w');
												fwrite($ourFileHandle,$img);
												fclose($ourFileHandle);
												$route[$index][$waypoint->nodeName][$image_index][$image->nodeName] = $filename;
												break;
											}
										}$image_index++;
									}
								}
							}
						}$index++;
					}
				}
				break;
			}
		}
		$object = array('id' => $id, 'walk_length' => $walklength , 'walk_title' => $walktitle , 'walk_desc' => $walkdesc, 'walk_difficulty' => $walkdifficulty, 'version' => $version, 'route' => $route);
		$jsonobj = json_encode($object);
		return $jsonobj;
	}

	/**
	 * This function saves a walk. If the $uniqueid already exists
	 * on the server it rewrites the file. Otherwise it creates a new file.
	 *
	 * @param $selectedwalk the selected walk's unique ID
	 * @return JSON object containing the walk data.
	 */
	/**
	 * This function saves a walk. If the $uniqueid already exists
	 * on the server it rewrites the file. Otherwise it creates a new file.
	 *
	 * @param $uniqueid the walk's unique ID. Is 0 if not set.
	 * @param $walklength the number of waypoints in the walk
	 * @param $walktitle the walk title
	 * @param $walkdesc the walk title
	 * @param $route an array of waypoints with waypoint details
	 * @return $uniqueid returns the walks unique ID.
	 */
	public function save($uniqueid, $walklength, $walktitle, $walkdesc, $walkdifficulty, $version, $route)
	{
		if ($this->fileexists($uniqueid)){
			$this->updateMap($uniqueid, $walktitle, $walkdesc, $walkdifficulty, $version);
			$this->saveWalk($uniqueid, $walklength, $walktitle, $walkdesc, $walkdifficulty, $version, $route);
		} else {
			$instance = new rename();
			$uniqueid = $instance->newXMLFilename();
			$this->saveWalk($uniqueid, $walklength, $walktitle, $walkdesc, $walkdifficulty, $version, $route);
			$this->addWalk($uniqueid, $walktitle, $walkdesc, $walkdifficulty, $version);
		}
		return $uniqueid;
	}

	/**
	 * This function checks if a file already exists
	 * that matches the $uniqueid parameter. If file exists return
	 * true.
	 *
	 * @param $uniqueid the walks uniqueid you are checking if it exists.
	 * @return true if file exist else it returns false
	 */
	public function fileexists($uniqueid)
	{
		$dirnew = "walks/".$uniqueid.".xml";
		if (file_exists($dirnew)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * This function delets the xml file that matches the $uniqueid.
	 *
	 * @param $uniqueid the uniqueid of the walk being deleted
	 */
	public function deleteWalk($uniqueid){
		unlink('walks/'.$uniqueid.'.xml');
		$doc = new DOMDocument();
		$doc->formatOutput = true;
		$doc->load("map.xml");
		$x = $doc->documentElement;
		$numberofwalks = $x->getElementsByTagName('numberofwalks');
		foreach ($numberofwalks AS $noms)
		{
			$count = intval($noms->nodeValue);
			$count--;
			$noms->replaceChild($doc->createTextNode($count),$noms->firstChild);
		}
		foreach ($x->childNodes AS $elements)
		{
			switch($elements->nodeName){
				case "walklist";
				foreach ($elements->childNodes AS $walk)
				{
					switch($walk->nodeName){
						case "walk";
						$id = $walk->getAttributeNode('id');
						if($uniqueid==$id->nodeValue){
							$elements->removeChild($walk);
						}
					}
				}
			}
		}
		$xml_string = $doc->saveXML();
		$fp = @fopen('map.xml','w');
		if(!$fp) {
			die('Error cannot create XML file');
		}
		fwrite($fp,$xml_string);
		fclose($fp);
	}

	/**
	 * This updats the XML Map file, updating the
	 * entry for the walk and replaces the old title
	 * and description with the new ones.
	 *
	 * @param $uniqueid the walk's unique ID
	 * @param $walktitle the walk's new title
	 * @param $walkdesc the walk's new description
	 */
	public function updateMap($uniqueid, $walktitle, $walkdesc, $walkdifficulty, $version){
		unlink('walks/'.$uniqueid.'.xml');
		$doc = new DOMDocument();
		$doc->formatOutput = true;
		$doc->load("map.xml");
		$x = $doc->documentElement;
		foreach ($x->childNodes AS $elements)
		{
			switch($elements->nodeName){
				case "walklist";
				foreach ($elements->childNodes AS $walk)
				{
					switch($walk->nodeName){
						case "walk";
						$id = $walk->getAttributeNode('id');
						if($uniqueid==$id->nodeValue){
							foreach ($walk->childNodes AS $item)
							{
								switch($item->nodeName){
									case "title";
									$item->replaceChild($doc->createTextNode($walktitle),$item->firstChild);
									break;
									case "desc";
									$item->replaceChild($doc->createTextNode($walkdesc),$item->firstChild);
									break;
									case "difficulty";
									$item->replaceChild($doc->createTextNode($walkdifficulty),$item->firstChild);
									break;
									case "version";
									$item->replaceChild($doc->createTextNode($version),$item->firstChild);
									break;
								}
							}
						}
					}
				}
			}
		}

		$xml_string = $doc->saveXML();
		$fp = @fopen('map.xml','w');
		if(!$fp) {
			die('Error cannot create XML file');
		}
		fwrite($fp,$xml_string);
		fclose($fp);
	}

	/**
	 * This function adds the new walk to the XML map file.
	 *
	 * @param $uniqueid the new walk's unique ID
	 * @param $walktitle the new walk's title
	 * @param $walkdesc the new walk's description
	 */
	public function addWalk($uniqueid, $walktitle, $walkdesc, $walkdifficulty, $version){
		$doc = new DOMDocument();
		$doc->formatOutput = true;
		$doc->load("map.xml");
		$x = $doc->documentElement;
		$numberofwalks = $x->getElementsByTagName('numberofwalks');
		foreach ($numberofwalks AS $noms)
		{
			$count = intval($noms->nodeValue);
			$count++;
			$noms->replaceChild($doc->createTextNode($count),$noms->firstChild);
		}
		$walklist = $x->getElementsByTagName('walklist');
		$walk = $doc->createElement('walk');
		foreach ($walklist AS $mapl)
		{
			$mapl->appendChild($walk);
			$walk->setAttribute('id', $uniqueid);
			$walk->setIdAttribute('id',true);
			$id = $doc->createElement('id');
			$walk->appendChild($id);
			$id->appendChild($doc->createTextNode($uniqueid));
			$title = $doc->createElement('title');
			$walk->appendChild($title);
			$title->appendChild($doc->createTextNode($walktitle));
			$desc = $doc->createElement('desc');
			$walk->appendChild($desc);
			$desc->appendChild($doc->createTextNode($walkdesc));
			$difficulty = $doc->createElement('difficulty');
			$walk->appendChild($difficulty);
			$difficulty->appendChild($doc->createTextNode($walkdifficulty));
			$ver = $doc->createElement('version');
			$walk->appendChild($ver);
			$ver->appendChild($doc->createTextNode($version));
			
			//fixes formatting in XML file
			$xml_string = $doc->saveXML();
			$doc = new DOMDocument();
			$doc->preserveWhiteSpace = false;
			$doc->formatOutput = true;
			$doc->loadXML($xml_string);
			$xml_string = $doc->saveXML();
				

			$fp = @fopen('map.xml','w');
			if(!$fp) {
				die('Error cannot create XML file');
			}
			fwrite($fp,$xml_string);
			fclose($fp);
		}
	}

	/**
	 * This function gets the XML Map file contents
	 * and creates a JSON object of the full walk list.
	 * This object is then returned back to the caller.
	 *
	 * @return jsonobj the json object holds the walk list.
	 *			 It is in the following format: -
	 * 			"array('numberofwalks' => $numberofwalks , 'walklist' => $walk_list);"
	 */
	public function getXMLMap(){
		$index = 0;
		$numberofwalks = 0;
		$walk_list = null;

		$xmlDoc = new DOMDocument();
		$xmlDoc->load("map.xml");
		$x = $xmlDoc->documentElement;
		foreach ($x->childNodes AS $elements)
		{
			switch($elements->nodeName){
				case "numberofwalks";
				$numberofwalks = $elements->nodeValue;
				break;
				case "walklist";
				foreach ($elements->childNodes AS $walk)
				{
					switch($walk->nodeName){
						case "walk";
						foreach ($walk->childNodes AS $item)
						{
							switch($item->nodeName){
								case "id";
								$walk_list[$index][$item->nodeName] = $item->nodeValue;
								break;
								case "title";
								$walk_list[$index][$item->nodeName] = $item->nodeValue;
								break;
								case "desc";
								$walk_list[$index][$item->nodeName] = $item->nodeValue;
								break;
								case "difficulty";
								$walk_list[$index][$item->nodeName] = $item->nodeValue;
								break;
								case "version";
								$walk_list[$index][$item->nodeName] = $item->nodeValue;
								break;
							}
						}
						$index++;
						break;
					}
				}
				break;
			}
		}

		$object = array('numberofwalks' => $numberofwalks , 'walklist' => $walk_list);

		$jsonobj = json_encode($object);
		return $jsonobj;
	}

	/**
	 * This function creates a new XML document and 
	 * converts the parameters into an XML walk file.
	 * 
	 * @param $uniqueid the walk's unique ID
	 * @param $walklength the number of waypoints in the walk
	 * @param $walktitle the walk's title
	 * @param $walkdesc the walk's description
	 * @param $route the waypoint array containing all the information about each waypoint
	 */
	public function saveWalk($uniqueid, $walklength, $walktitle, $walkdesc, $walkdifficulty, $version, $route){
		$doc = new DOMDocument();
		$doc->formatOutput = true;

		$walk = $doc->createElement('walk');
		$doc->appendChild($walk);

		$walk->setAttribute('id', $uniqueid);
		$walk->setIdAttribute('id',true);

		$unique_id = $doc->createElement('id');
		$walk->appendChild($unique_id);
		$unique_id->appendChild($doc->createTextNode($uniqueid));

		$walk_length = $doc->createElement('walk_length');
		$walk->appendChild($walk_length);
		$walk_length->appendChild($doc->createTextNode($walklength));

		$walk_title = $doc->createElement('walk_title');
		$walk->appendChild($walk_title);
		$walk_title->appendChild($doc->createTextNode($walktitle));

		$walk_desc = $doc->createElement('walk_desc');
		$walk->appendChild($walk_desc);
		$walk_desc->appendChild($doc->createTextNode($walkdesc));

		$walk_difficulty = $doc->createElement('walk_difficulty');
		$walk->appendChild($walk_difficulty);
		$walk_difficulty->appendChild($doc->createTextNode($walkdifficulty));

		$ver = $doc->createElement('version');
		$walk->appendChild($ver);
		$ver->appendChild($doc->createTextNode($version));
		
		//Start Route loop
		$route_xml = $doc->createElement('route');
		$walk->appendChild($route_xml);

		$index = 0;
		foreach ($route as $waypoint) {
			$waypointtitle = $waypoint['title'];
			$waypointdesc = $waypoint['desc'];
			$lat = $waypoint['lat'];
			$lng = $waypoint['lng'];
			$numberOfImages = $waypoint['numberofimages'];
			$images = $waypoint['images'];
			//create waypoint
			$waypoint = $doc->createElement('waypoint');
			$route_xml->appendChild($waypoint);

			//add waypoint details
			$waypoint_index = $doc->createElement('waypoint_index');
			$waypoint->appendChild($waypoint_index);
			$waypoint_index->appendChild($doc->createTextNode($index));

			$waypoint_title = $doc->createElement('waypoint_title');
			$waypoint->appendChild($waypoint_title);
			$waypoint_title->appendChild($doc->createTextNode($waypointtitle));

			$waypoint_desc = $doc->createElement('waypoint_desc');
			$waypoint->appendChild($waypoint_desc);
			$waypoint_desc->appendChild($doc->createTextNode($waypointdesc));

			$lat_xml = $doc->createElement('lat');
			$waypoint->appendChild($lat_xml);
			$lat_xml->appendChild($doc->createTextNode($lat));

			$lng_xml = $doc->createElement('lng');
			$waypoint->appendChild($lng_xml);
			$lng_xml->appendChild($doc->createTextNode($lng));

			$number_of_images = $doc->createElement('number_of_images');
			$waypoint->appendChild($number_of_images);
			$number_of_images->appendChild($doc->createTextNode($numberOfImages));

			if(0 != $numberOfImages){
				$images_xml = $doc->createElement('images');
				$waypoint->appendChild($images_xml);
				foreach ($images as $img) {
					if ($img) {
						$image_xml = $doc->createElement('image');
						$images_xml->appendChild($image_xml);
						$imgbinary = fread(fopen('upload/'.$img, "r"), filesize('upload/'.$img));
						$img_base64 = base64_encode($imgbinary);
						$image_base64 = $doc->createElement('image_base64');
						$image_xml->appendChild($image_base64);
						$image_base64->appendChild($doc->createTextNode($img_base64));
					}
				}
			}
			$index++;
		}
		//fixes XML formatting in file
		$xml_string = $doc->saveXML();
		$doc = new DOMDocument();
		$doc->preserveWhiteSpace = false;
		$doc->formatOutput = true;
		$doc->loadXML($xml_string);
		$xml_string = $doc->saveXML();
		
		$fp = @fopen('walks/'.$uniqueid.'.xml','w');
		if(!$fp) {
			die('Error cannot create XML file');
		}
		fwrite($fp,$xml_string);
		fclose($fp);
	}

}

?>