/**
 * This is the main JavaScript file and deals with the Map API and handling all
 * the local data
 * 
 * @author Karl Parry (kdp8)
 */

/**
 * Variable setup map = link to Google Maps map uniqueid = walk's unique ID
 * length = number of waypoints in walk route = a link to the Google Maps
 * Polyline creating a line between waypoints. waypoints = a link to an array of
 * waypoint objects walktitle = walk title walkdesc = walk description
 * 
 */
var map;
var uniqueid = 0;
var length = 0;
var route;
var waypoints = null;
var walktitle = "";
var walkdesc = "";
var walkdifficulty="1";

/**
 * This function creates a waypoint object. It creates a new object by calling
 * new waypointObject(marker,index)
 * 
 * @param marker
 *            a link to the Google Maps marker placed on the map
 * @param index
 *            the index in the list of waypoint objects
 * @returns a waypoint Object
 */
function waypointObject(marker, index) {
	this.marker = marker;
	this.infoWindow = null;
	this.index = index;
	this.title = '';
	this.desc = '';
	this.numberofimages = 0;
	this.images = [];
}

/**
 * Initializes the Google Maps API. Also creates the map listener that detects
 * clicks on the map and calls addMarker function
 */
function initialize() {

	var myLatlng = new google.maps.LatLng(52.083, -4.655);
	length = 0;
	waypoints = [];
	var myOptions = {
		zoom : 16,
		center : myLatlng,
		mapTypeId : google.maps.MapTypeId.HYBRID,
	};
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	var routeOptions = {
		strokeColor : '#000000',
		strokeOpacity : 1.0,
		strokeWeight : 3,
	};
	route = new google.maps.Polyline(routeOptions);
	route.setMap(map);
	google.maps.event.addListener(map, 'click', addMarker);
}

/**
 * This method adds a marker to the Google Maps Map. Also sets it's icon
 * dependant on its index. Also adds the left click, right click, Drag listeners
 * to the marker
 * 
 * @param event
 *            left click event on the map. Used to find coordinate values
 */
function addMarker(event) {
	var path = route.getPath();
	path.push(event.latLng);
	var image = null;
	var len = length;
	/**
	 * Sets the marker icon dependant on idex
	 */
	if (0 == length) {
		image = new google.maps.MarkerImage('images/green-dot.png',
				new google.maps.Size(32, 32), new google.maps.Point(0, 0));
	} else {
		image = new google.maps.MarkerImage('images/final-dot.png',
				new google.maps.Size(32, 32), new google.maps.Point(0, 0));
		if (1 < len) {
			var mktmp = waypoints[length - 1];
			var tmpimage;
			if (mktmp.title.replace(/\s/g, "") != "" || mktmp.desc.replace(/\s/g, "") != ""
				|| mktmp.numberofimages != 0) {
			tmpimage = new google.maps.MarkerImage('images/blue-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			}else{
			tmpimage = new google.maps.MarkerImage('images/red-dot.png',
					new google.maps.Size(32, 32), new google.maps.Point(0, 0));
			}
			mktmp.marker.setIcon(tmpimage);
		}
	}

	var markerOptions = {
		position : event.latLng,
		title : '' + length,
		map : map,
		draggable : true,
		icon : image,
	};

	var marker = new google.maps.Marker(markerOptions);
	var waypoint = new waypointObject(marker, length);
	waypoints[length] = waypoint;
	length++;
	/**
	 * Drag listener that calls the movePoint when the dragging of a marker
	 * ends. The API deals with the actual dragging
	 */
	google.maps.event.addListener(marker, 'dragend', function() {
		movePoint(waypoint);
	});
	/**
	 * Remove listener that calls the removePoint when a right click is
	 * detected.
	 */
	google.maps.event.addListener(marker, 'rightclick', function() {
		removePoint(waypoint);
	});
	/**
	 * Information listener that calls the openInfoWindow when a left click is
	 * detected.
	 */
	google.maps.event.addListener(marker, 'click', function() {
		openInfoWindow(waypoint);
	});
};

/**
 * Redraws the polyline now the marker has moved
 * 
 * @param waypoint
 *            the waypoint that was moved.It is used to get the new position to
 *            redraw the polyline to.
 */
function movePoint(waypoint) {
	var coords = waypoint.marker.getPosition();
	var i = waypoint.index;
	var path = route.getPath();
	path.setAt(i, coords);
}




