var visible = false;

function hideShowSideBar() {
	sidebar = document.getElementById('side_bar');
	topbar = document.getElementById('map_canvas');
	if (visible) {
		sidebar.style.width = "0%";
		topbar.style.width = "100%";
		visible = false;		
	} else {
		sidebar.style.width = "20%";
		topbar.style.width = "80%";
		visible = true;
	}
}

function updateSideBar() {
	sidebar = document.getElementById('side_bar');
	var tempHTML = "<table border ='1px' align='center' style='text-align: center;'>";
	tempHTML += "<tr><td><span>No</span></td><td><span>ET</span></td><td><span>ED</span></td><td><span>WT</span></td><td><span>WD</span></td><td><span>I</span></td></tr>";
	for (var i = 0; i<length ; i++){
		var marker = waypoints[i];
		var a = "",b = "",c = "",d = "",e = "";
		if(marker.title.replace(/\s/g, "") != "" ){
			a="X";
		}
		if(marker.desc.replace(/\s/g, "") != ""){
			b="X";
		}
		if(marker.welshtitle.replace(/\s/g, "") != "" ){
			c="X";
		}
		if(marker.welshdesc.replace(/\s/g, "") != ""){
			d="X";
		}
		if (marker.numberofimages != 0){
			e="X";
		}
		tempHTML += "<tr><td><button type='button' onClick=\'openInfoWindowIndex("+marker.index+");\'>"+i+"</button></td><td><span>"+a+"</span></td><td><span>"+b+"</span></td><td><span>"+c+"</span></td><td><span>"+d+"</span></td><td><span>"+e+"</span></td></tr>";
	}
	tempHTML += "</table>";
	sidebar.innerHTML = tempHTML;
}

function openInfoWindowIndex(index){
	var marker = waypoints[index];
	openInfoWindow(marker);
}
