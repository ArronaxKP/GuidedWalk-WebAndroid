var visible = false;

function hideShowSideBar() {
	sidebar = document.getElementById('side_bar');
	mainlayer = document.getElementById('mainlayer');
	if (visible) {
		document.getElementById("side_bar").style.display="none";
		sidebar.style.width = "0px";
		mainlayer.style.right = "0px";
		visible = false;		
	} else {
		updateSideBar();
		document.getElementById("side_bar").style.display="block";
		sidebar.style.width = "200px";
		mainlayer.style.right = "200px";
		visible = true;
	}
}

function updateSideBar() {
	sidebar = document.getElementById('side_bar');
	var tempHTML = "<table border ='1px' align='center' style='text-align: center; table-layout: fixed;' >";
	tempHTML += "<tr><td width='16%'><span>No</span></td><td width='16%'><span>ET</span></td><td width='16%'><span>ED</span></td><td width='16%'><span>WT</span></td><td width='16%'><span>WD</span></td><td width='16%'><span>I</span></td></tr>";
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
