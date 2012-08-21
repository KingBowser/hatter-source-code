function initr() {
    
    var elem = document.getElementsByTagName("div");
    for (i = 0; i < elem.length; i++) {
        if (elem[i].className.indexOf("dcd") != -1) {
            elem[i].onclick = ldcss;
            elem[i].onmouseout = ldcss;
            elem[i].onmouseover = ldcss;
        }
    }
}

String.prototype.quote=function() {
return this.replace(/([!$^*()|\\[\]<>?:=+])/g,"\\$1");

}
function check(id) { 
  
    var cr; 
    var srcstring = document.getElementById("scrstr").value; 
    var result = '';
	var output; 
	var n;
	switch ( id ) {        
    case 'st0': 
			n=0;          
            break;       
    case 'st1':
            n=1;
            break;
    case 'st2':
            n=2;
            break;
    case 'st3':
            n=3;
            break;	
    }
    
    
    output = document.getElementById("scrstr").value;
 
    output = output.replace(/[\n\t\r]+/g,""); 
    output = output.replace(/(\*\/)/g,function($0,$1){
       return $0.replace($1,$1+"<br>")
    });   
     output = output.replace(/(\\\*)/g,function($0,$1){
       return $0.replace($1,$1+"<br>")
    });    
    cr = output.match(/(?!})[\w ,#.:]+(?={)/g);
 
    var vvb = new Array;
    var vva = new Array;
    
    if ( cr != null && typeof(cr) == 'object' && cr.length != null )  { 	   
		  
		for ( i = 0; i < cr.length; i++ ) {  
		    var crr = cr[i];		        
		     var ree = new RegExp(cr[i], "");
		     var vmm = ree.exec(output);
             var lstind = (vmm!=null) ? output.indexOf(vmm[0])+vmm[0].length : 0;

		     vvb[i] = output.substring(0,lstind);
		     vva[i] = vvb[i].replace(ree,'<span class ="cssb'+n+'">'+cr[i]+' </span>');
		     output = output.substring(lstind,output.length);
		        
		 }  		     
		     output = vva.join("")+output;
		    
    }
 
 
 	     cr = document.getElementById("scrstr").value.match(/[\w-]+(?=:)(?!:hover)/g);
 	      if ( cr != null && typeof(cr) == 'object' && cr.length != null )  {  
		     for ( i = 0; i < cr.length; i++ )   {  
		     output = output.replace(/[\w-]+(?=:)(?!:hover)/,'<br />'+ '<span class ="cssc'+n+'">'+cr[i]+'</span>' );
		     
		     }
		  }   
		     cr = document.getElementById("scrstr").value.match(/[# ,\.%\w-]+\s*(?=[;])|[# ,\.%\w-]+\s*(?=[}])/g);
		  if ( cr != null && typeof(cr) == 'object' && cr.length != null )  {    
		     for ( i = 0; i < cr.length; i++ )   { 
		     output = output.replace(/[# ,\.%\w-]+\s*(?=[;])|[# ,\.%\w-]+\s*(?=[}])/,'<span class ="cssv'+n+'">'+ ' '+ cr[i]+'</span>' );
		     }
		     
		     formated =true;
		  }  
		    
		     output = output.replace(/[}]/g,'<br />'+"}"+'<br />');
		     
		     document.getElementById("hlrest").innerHTML = output; 
		      document.getElementById("htm").value = output;
		      
		      
		      
		     tab1.showContent(1);
		     doit();
		 
}	

var formated =false;	

function ldcss(evt) {
    evt = (evt) ? evt: ((window.event) ? window.event: "");
    if (evt) {
        var elem = getTargetElement(evt);
        if (evt.type == "mouseover") {
            elem.className = "dcdt";
        }
        if (evt.type == "mouseout") {
            elem.className = "dcd";
        }
        if (evt.type == "click") {
        var id = elem.id;
          check(id);
        }
    }
}





function doit() {
if (formated ==true)
 tab1.Divs[1].style.height = "auto";
}

window.onload = function(){

initr();

}