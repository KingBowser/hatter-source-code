
function pgtab() {
this.border ="#99ff00 1px solid";
this.content = {
    clear: "both"
    ,display: "block"
    ,border: this.border

},
this.ulstyle={
    listStyleType: "none"
    ,paddingLeft: "0px"
    ,margin: "0px"
},
this.listyle={
   cssFloat: "left",
   borderRight: this.border,
   borderLeft: this.border
},

this.astyle ={
    position: "relative"
    ,textDecoration: "none"
    ,cursor: "pointer"
    ,display: "block"    
    ,font: "bold 14px arial, sans-serif"
    ,color: "#c00000"
},
this.Divs = []

};

pgtab.prototype = { 
init : function(settings) {

    for(var i in settings){
	    if (typeof(this[i])=="undefined") {
	        this[i] = settings[i];
	        
	    }
	    else
	        if (i=="border") this[i] = settings[i];
		    for(var j in settings[i]){
		        if (typeof(settings["border"])!="undefined") {
		            if (typeof(this[i]["border"])!="undefined") this[i]["border"] = settings["border"];
		            if (typeof(this[i]["borderRight"])!="undefined") this[i]["borderRight"] = settings["border"];
		            if (typeof(this[i]["borderLeft"])!="undefined") this[i]["borderLeft"] = settings["border"];
		        }
    		     //if (typeof(this[i][j])=="undefined")
    		            this[i][j]=settings[i][j];     		               		    
		     }
    }
    
    this.directNodes();
    this.showContent(this.activetab);   
    this._bindEvent = this.tabEvent.bind(this);	       
    this.bindEvent();
   
   
}

,

tabEvent: function(e) {

    e = (e) ? e : ((window.event) ? window.event : "");
    var elem = getTargetElement(e);
    var hvelem = (elem.tagName=='A') ? elem.parentNode : elem;
    var lielem =  (elem.tagName=='A') ? elem.parentNode.parentNode.getElementsByTagName('li') : elem.parentNode.getElementsByTagName('li');
    var actIndex = getIndex(lielem,hvelem);
    this.showContent(actIndex)
	   
},

bindEvent: function() {
	
	 		
	var ele = this.ul.getElementsByTagName('li');
    
	if(document.all) {
    if (this.fire)
        for(var i in this.fire){
	     addEvent ( ele[i], this.eventType, this.fire[i] );
	    }
    }
     
    for(var i=0; i<ele.length; i++){ 

    addEvent ( ele[i], this.eventType, this._bindEvent );

    }
    
    if(!document.all) {
    if (this.fire)
	   for(var i in this.fire){
	    addEvent ( ele[i], this.eventType, this.fire[i] );
	   }
    }
},

addstyle: function(node,styles) {

    for(var i in styles){
        if ((document.all)&&(i=="cssFloat")) node.style["styleFloat"]= styles[i];
        node.style[i]= styles[i];
    }

},

directNodes: function() {
var nodes = $(this.id).childNodes;


var ul;

for(var i=0; i<nodes.length; i++){

        if (nodes[i].nodeName=="DIV") {
            this.Divs.push(nodes[i]);        
        }
        else if (nodes[i].nodeName=="UL") 
            this.ul = nodes[i];
}

},

showContent: function(act) {
       
	this.addstyle(this.Divs[act],this.content);
	//this.addstyle($(this.id),this.container);
	$(this.id).style.width = this.content.width;
	var tagula = this.ul.getElementsByTagName('a');
	var tagulli = this.ul.getElementsByTagName('li');
    
	for(var i=0; i<tagula.length; i++){
	    //tagula[i].className="ula";
	    this.addstyle(tagula[i],this.astyle);
	}

	for(var i=0; i<tagulli.length; i++){	
		//tagulli[i].className="ullifloat";
		this.addstyle(tagulli[i],this.listyle);
	}
	
	//this.ul.className= "ulnofloat";
	this.addstyle(this.ul,this.ulstyle);
	for(var i=0; i<this.Divs.length; i++){	
		if (i != act) {
			this.Divs[i].style.display = "none";
		}
	}

	var actitem = tagulli[act];

	var actitema = actitem.getElementsByTagName('a')[0];
	
    if (this.position == "t") {

		actitema.style.top ="1px";

	    actitem.style.borderTop = this.border;
	 }

     else if (this.position == "b") {

	     actitema.style.top ="-1px";

	     actitem.style.borderBottom = this.border;

	}
	
	actitema.style.backgroundColor =this.content["backgroundColor"];	
    var marginRight_li = getstyle(actitem,"margin-right");		
	for(var i=0; i<tagulli.length; i++){	
	    if (marginRight_li=="0px") {	
			
			tagulli[i].style.borderLeft = (i==0) ? this.border : "#fff 0px";
	    }

	    if (i != act) {
	
	    var a = tagulli[i].getElementsByTagName('a')[0];
	    a.style.top ="0px";	
	    a.style.left ="0px";
	    a.style.backgroundColor =this.astyle["backgroundColor"];
	    
            if (this.position=="t") {
	        tagulli[i].style.borderTop = this.border;
        	
	        }
	        else
	        tagulli[i].style.borderBottom = this.border;    		        
	    }	
    }
  }
}


function vd() {
}

function getIndex(arry,elem) {
	for (var i=0; i < arry.length; i++) {
		if (arry[i] == elem) {
		return i;
		}
	}
}
 

function ie6() {
var appVer = navigator.appVersion.toLowerCase();
var iePos = appVer.indexOf('msie');
if (iePos !=-1) {
is_minor = parseFloat(appVer.substring(iePos+5,appVer.indexOf(';',iePos)))
is_major = parseInt(is_minor);
}

var is_ie = ((iePos!=-1));
var is_ie6 = (is_ie && is_major == 6);
return is_ie6;
}

function getstyle(elem, prop) {
		if(document.defaultView)
		{
			return document.defaultView.getComputedStyle(elem, null).getPropertyValue(prop);
		}
		else if(elem.currentStyle)
		{
			var prop = prop.replace(/-(\w)/gi, function($0,$1)
			{
				return $1.toUpperCase();
			});
			return elem.currentStyle[prop];
		}
		else return null;
}

function getTargetElement(evt) {
    return (evt.target) ? ((evt.target.nodeType == 3) ? evt.target.parentNode : evt.target) : evt.srcElement;
}

function addEvent ( obj, type, fn ) {
  if ( obj.attachEvent ) {
    obj["e"+type+fn] = fn;
    obj[type+fn] = function() { obj["e"+type+fn]( window.event ); }
    obj.attachEvent( "on"+type, obj[type+fn] );
  } else
    obj.addEventListener( type, fn, false );
}
	
function removeEvent ( obj, type, fn ) {
      if ( obj.detachEvent ) {
        obj.detachEvent( "on"+type, obj[type+fn] );
		    obj[type+fn] = null;
      } else
        obj.removeEventListener( type, fn, false );
}

Function.prototype.bind = function(obj) {
	
    var _method = this;
    return function() {
        return _method.apply(obj, arguments);
    };
    
} 
function $(id){ return(document.getElementById(id)); }

