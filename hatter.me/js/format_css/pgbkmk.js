function getTargetElement(evt) {
    var elem;
	if (evt.target) {
		elem = (evt.target.nodeType == 3) ? evt.target.parentNode : evt.target;
	 } 
	 else {
		elem = evt.srcElement;
	    }
	 return elem;
}

function getImgName(url) {	
	//var arr = url.match(/\/([\w\-]+)\.\w+\"/g);
	//return  RegExp.$1;
	var l = url.length;
	var x = url.lastIndexOf("/",l);
	var y = url.lastIndexOf(".",l);
	var str = url.substring(x+1, y);
	return str;

}

function selbkmks(nm) {
var urlstr = location.href;
var tt=document.title;

	if (nm=="delicious") {
	    location.href = 'http://del.icio.us/post?url=' + urlstr+ '&title=' + tt;
	}
	else if (nm=="digg") {
		location.href = 'http://digg.com/submit?phase=2&url=' + urlstr+ '&title=' + tt; 
    }
    else if (nm=="reddit") {
		location.href = 'http://reddit.com/submit?url=' + urlstr+ '&title=' + tt; 		
    }
    else if (nm=="furl") {
		location.href = 'http://www.furl.net/storeIt.jsp?t=' + tt + '&u=' + urlstr; 		   
    }
    else if (nm=="StumbleUpon") {
		location.href = 'http://www.stumbleupon.com/submit?url=' + urlstr+ '&title=' + tt; 
    }
    else if (nm=="yahoo-myweb") {
		location.href = 'http://myweb2.search.yahoo.com/myresults/bookmarklet?t=' + tt + '&u=' + urlstr; 
    }
    else if (nm=="google") {
		location.href = 'http://www.google.com/bookmarks/mark?op=edit&bkmk=' + urlstr + '&title=' + tt; 
    }

}

function loadcss(filename){
	
	var fileref=document.createElement("link");
	fileref.setAttribute("rel", "stylesheet");
	fileref.setAttribute("type", "text/css");
	fileref.setAttribute("href", filename);

	if (typeof fileref!="undefined") {
		document.getElementsByTagName("head")[0].appendChild(fileref);
		
	} 
}

//var pg ='www.pgecolumn.com/';
  var pg="";
  loadcss(pg +"../style/pgbk.css");   
  var pgsite = pg +'../images/';
         
 var pgco = '<div id="pgco" class="bkcc">';
    pgco += '<div class="bkmc" style="background-image:url(' + pgsite + 'delicious.png)"></div>';
    pgco +='<div class="bkmc" style="background-image:url(' + pgsite + 'digg.gif)"></div>';
    pgco +='<div class="bkmc" style="background-image:url(' + pgsite + 'reddit.gif)"></div>';
    pgco +='<div class="bkmc" style="background-image:url(' + pgsite + 'furl.gif)"></div>';
    pgco +='<div class="bkmc" style="background-image:url(' + pgsite + 'StumbleUpon.png)"></div>';
    pgco +='<div class="bkmc" style="background-image:url(' + pgsite + 'yahoo-myweb.png)"></div>';
    pgco +='<div class="bkmc" style="background-image:url(' + pgsite + 'google.gif)"></div></div><div class="bkmgin"></div>';

document.write(pgco);
init();    
  
function appDiv() {

if (!document.getElementById("toolTip")) {
var oDv=document.createElement("div");
oDv.id ="toolTip";
document.body.appendChild(oDv);
    oDv.style.position="absolute";
	oDv.style.border='1px solid #ccffff';
	oDv.style.backgroundColor='#ccffcc';
	oDv.style.margin='0px';
	oDv.style.padding="2px";
	oDv.style.fontFamily='arial';
	oDv.style.fontSize='12px';
	oDv.style.filter='alpha(opacity=85)'; // IE
	oDv.style.opacity='0.85'; // FF
	}
	
}

function bkmkEvent(evt) {
    evt = (evt) ? evt : ((window.event) ? window.event : "");
	if (evt) {
	    var elem = getTargetElement(evt);
		if (evt.type == "click") {		    
	        var im =getBkImage(elem);
			var imnm = getImgName(im);
			selbkmks(imnm);		    
		}
		
		if (evt.type == "mouseover") {		
			var im =getBkImage(elem);				
			var imnm = getImgName(im);
			
			var xPos = evt.clientX;
			var yPos = evt.clientY;
			var xP = document.documentElement.scrollLeft;
			var yP = document.documentElement.scrollTop; 
			
			appDiv();
			var tlTip = document.getElementById("toolTip");
			tlTip.innerHTML = imnm;
			
			tlTip.style.top =parseInt(yPos)+10+ yP+ "px";
			tlTip.style.left =parseInt(xPos)-10+ xP+ "px";
			tlTip.style.visibility = "visible";
			
	
		}
		if (evt.type == "mouseout") {
		hideToolTip();
		}
		
	}
}

function hideToolTip(){
   var toolTip = document.getElementById("toolTip");
   toolTip.style.visibility = "hidden";
}


function getBkImage(obj) {
var imgstr =(obj.currentStyle) ?  obj.currentStyle.backgroundImage : getComputedStyle(obj,'').getPropertyValue('background-image');
return imgstr;
}

function init() {
			
	var ele = document.getElementsByTagName("div");
	var j=0;
	for(var i=0; i<ele.length; i++){
		if (ele[i].className.indexOf("bkmc") != -1) {
			ele[i].onclick =bkmkEvent;
			ele[i].onmouseover = bkmkEvent;
			ele[i].onmouseout = bkmkEvent;
			ele[i].onmousemove = bkmkEvent;	
		 }
			    
	 }
 }
