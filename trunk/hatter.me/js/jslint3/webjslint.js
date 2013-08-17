// (C)2011 Douglas Crockford
// www.JSLint.com

var JSON;if(!JSON){JSON={};}
(function(){"use strict";function f(n){return n<10?'0'+n:n;}
if(typeof Date.prototype.toJSON!=='function'){Date.prototype.toJSON=function(key){return isFinite(this.valueOf())?this.getUTCFullYear()+'-'+
f(this.getUTCMonth()+1)+'-'+
f(this.getUTCDate())+'T'+
f(this.getUTCHours())+':'+
f(this.getUTCMinutes())+':'+
f(this.getUTCSeconds())+'Z':null;};String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(key){return this.valueOf();};}
var cx=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,escapable=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,gap,indent,meta={'\b':'\\b','\t':'\\t','\n':'\\n','\f':'\\f','\r':'\\r','"':'\\"','\\':'\\\\'},rep;function quote(string){escapable.lastIndex=0;return escapable.test(string)?'"'+string.replace(escapable,function(a){var c=meta[a];return typeof c==='string'?c:'\\u'+('0000'+a.charCodeAt(0).toString(16)).slice(-4);})+'"':'"'+string+'"';}
function str(key,holder){var i,k,v,length,mind=gap,partial,value=holder[key];if(value&&typeof value==='object'&&typeof value.toJSON==='function'){value=value.toJSON(key);}
if(typeof rep==='function'){value=rep.call(holder,key,value);}
switch(typeof value){case'string':return quote(value);case'number':return isFinite(value)?String(value):'null';case'boolean':case'null':return String(value);case'object':if(!value){return'null';}
gap+=indent;partial=[];if(Object.prototype.toString.apply(value)==='[object Array]'){length=value.length;for(i=0;i<length;i+=1){partial[i]=str(i,value)||'null';}
v=partial.length===0?'[]':gap?'[\n'+gap+partial.join(',\n'+gap)+'\n'+mind+']':'['+partial.join(',')+']';gap=mind;return v;}
if(rep&&typeof rep==='object'){length=rep.length;for(i=0;i<length;i+=1){if(typeof rep[i]==='string'){k=rep[i];v=str(k,value);if(v){partial.push(quote(k)+(gap?': ':':')+v);}}}}else{for(k in value){if(Object.prototype.hasOwnProperty.call(value,k)){v=str(k,value);if(v){partial.push(quote(k)+(gap?': ':':')+v);}}}}
v=partial.length===0?'{}':gap?'{\n'+gap+partial.join(',\n'+gap)+'\n'+mind+'}':'{'+partial.join(',')+'}';gap=mind;return v;}}
if(typeof JSON.stringify!=='function'){JSON.stringify=function(value,replacer,space){var i;gap='';indent='';if(typeof space==='number'){for(i=0;i<space;i+=1){indent+=' ';}}else if(typeof space==='string'){indent=space;}
rep=replacer;if(replacer&&typeof replacer!=='function'&&(typeof replacer!=='object'||typeof replacer.length!=='number')){throw new Error('JSON.stringify');}
return str('',{'':value});};}
if(typeof JSON.parse!=='function'){JSON.parse=function(text,reviver){var j;function walk(holder,key){var k,v,value=holder[key];if(value&&typeof value==='object'){for(k in value){if(Object.prototype.hasOwnProperty.call(value,k)){v=walk(value,k);if(v!==undefined){value[k]=v;}else{delete value[k];}}}}
return reviver.call(holder,key,value);}
text=String(text);cx.lastIndex=0;if(cx.test(text)){text=text.replace(cx,function(a){return'\\u'+
('0000'+a.charCodeAt(0).toString(16)).slice(-4);});}
if(/^[\],:{}\s]*$/.test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,'@').replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,']').replace(/(?:^|:|,)(?:\s*\[)+/g,''))){j=eval('('+text+')');return typeof reviver==='function'?walk({'':j},''):j;}
throw new SyntaxError('JSON.parse');};}}());var JSLINT=(function(){"use strict";var adsafe_id,adsafe_may,adsafe_top,adsafe_went,anonname,approved,bang={'<':true,'<=':true,'==':true,'===':true,'!==':true,'!=':true,'>':true,'>=':true,'+':true,'-':true,'*':true,'/':true,'%':true},banned={'arguments':true,callee:true,caller:true,constructor:true,'eval':true,prototype:true,stack:true,unwatch:true,valueOf:true,watch:true},browser={clearInterval:false,clearTimeout:false,document:false,event:false,frames:false,history:false,Image:false,location:false,name:false,navigator:false,Option:false,parent:false,screen:false,setInterval:false,setTimeout:false,XMLHttpRequest:false},bundle={a_function:"'{a}' is a function.",a_label:"'{a}' is a statement label.",a_not_allowed:"'{a}' is not allowed.",a_not_defined:"'{a}' is not defined.",a_scope:"'{a}' used out of scope.",adsafe:"ADsafe violation.",adsafe_a:"ADsafe violation: '{a}'.",adsafe_autocomplete:"ADsafe autocomplete violation.",adsafe_bad_id:"ADSAFE violation: bad id.",adsafe_div:"ADsafe violation: Wrap the widget in a div.",adsafe_fragment:"ADSAFE: Use the fragment option.",adsafe_go:"ADsafe violation: Missing ADSAFE.go.",adsafe_html:"Currently, ADsafe does not operate on whole HTML documents. It operates on <div> fragments and .js files.",adsafe_id:"ADsafe violation: id does not match.",adsafe_id_go:"ADsafe violation: Missing ADSAFE.id or ADSAFE.go.",adsafe_lib:"ADsafe lib violation.",adsafe_lib_second:"ADsafe: The second argument to lib must be a function.",adsafe_missing_id:"ADSAFE violation: missing ID_.",adsafe_name_a:"ADsafe name violation: '{a}'.",adsafe_placement:"ADsafe script placement violation.",adsafe_prefix_a:"ADsafe violation: An id must have a '{a}' prefix",adsafe_script:"ADsafe script violation.",adsafe_source:"ADsafe unapproved script source.",adsafe_subscript_a:"ADsafe subscript '{a}'.",adsafe_tag:"ADsafe violation: Disallowed tag '{a}'.",already_defined:"'{a}' is already defined.",and:"The '&&' subexpression should be wrapped in parens.",assign_exception:"Do not assign to the exception parameter.",assignment_function_expression:"Expected an assignment or function call and instead saw an expression.",attribute_case_a:"Attribute '{a}' not all lower case.",avoid_a:"Avoid '{a}'.",bad_assignment:"Bad assignment.",bad_color_a:"Bad hex color '{a}'.",bad_constructor:"Bad constructor.",bad_entity:"Bad entity.",bad_html:"Bad HTML string",bad_id_a:"Bad id: '{a}'.",bad_in_a:"Bad for in variable '{a}'.",bad_invocation:"Bad invocation.",bad_name_a:"Bad name: '{a}'.",bad_new:"Do not use 'new' for side effects.",bad_number:"Bad number '{a}'.",bad_operand:"Bad operand.",bad_type:"Bad type.",bad_url:"Bad url string.",bad_wrap:"Do not wrap function literals in parens unless they are to be immediately invoked.",combine_var:"Combine this with the previous 'var' statement.",conditional_assignment:"Expected a conditional expression and instead saw an assignment.",confusing_a:"Confusing use of '{a}'.",confusing_regexp:"Confusing regular expression.",constructor_name_a:"A constructor name '{a}' should start with an uppercase letter.",control_a:"Unexpected control character '{a}'.",css:"A css file should begin with @charset 'UTF-8';",dangling_a:"Unexpected dangling '_' in '{a}'.",dangerous_comment:"Dangerous comment.",deleted:"Only properties should be deleted.",duplicate_a:"Duplicate '{a}'.",empty_block:"Empty block.",empty_case:"Empty case.",empty_class:"Empty class.",evil:"eval is evil.",expected_a:"Expected '{a}'.",expected_a_b:"Expected '{a}' and instead saw '{b}'.",expected_a_b_from_c_d:"Expected '{a}' to match '{b}' from line {c} and instead saw '{d}'.",expected_at_a:"Expected an at-rule, and instead saw @{a}.",expected_a_at_b_c:"Expected '{a}' at column {b}, not column {c}.",expected_attribute_a:"Expected an attribute, and instead saw [{a}].",expected_attribute_value_a:"Expected an attribute value and instead saw '{a}'.",expected_class_a:"Expected a class, and instead saw .{a}.",expected_fraction_a:"Expected a number between 0 and 1 and instead saw '{a}'",expected_id_a:"Expected an id, and instead saw #{a}.",expected_identifier_a:"Expected an identifier and instead saw '{a}'.",expected_identifier_a_reserved:"Expected an identifier and instead saw '{a}' (a reserved word).",expected_linear_a:"Expected a linear unit and instead saw '{a}'.",expected_lang_a:"Expected a lang code, and instead saw :{a}.",expected_media_a:"Expected a CSS media type, and instead saw '{a}'.",expected_name_a:"Expected a name and instead saw '{a}'.",expected_nonstandard_style_attribute:"Expected a non-standard style attribute and instead saw '{a}'.",expected_number_a:"Expected a number and instead saw '{a}'.",expected_operator_a:"Expected an operator and instead saw '{a}'.",expected_percent_a:"Expected a percentage and instead saw '{a}'",expected_positive_a:"Expected a positive number and instead saw '{a}'",expected_pseudo_a:"Expected a pseudo, and instead saw :{a}.",expected_selector_a:"Expected a CSS selector, and instead saw {a}.",expected_small_a:"Expected a small number and instead saw '{a}'",expected_space_a_b:"Expected exactly one space between '{a}' and '{b}'.",expected_string_a:"Expected a string and instead saw {a}.",expected_style_attribute:"Excepted a style attribute, and instead saw '{a}'.",expected_style_pattern:"Expected a style pattern, and instead saw '{a}'.",expected_tagname_a:"Expected a tagName, and instead saw {a}.",for_if:"The body of a for in should be wrapped in an if statement to filter unwanted properties from the prototype.",function_block:"Function statements should not be placed in blocks. "+"Use a function expression or move the statement to the top of "+"the outer function.",function_eval:"The Function constructor is eval.",function_loop:"Don't make functions within a loop.",function_statement:"Function statements are not invocable. "+"Wrap the whole function invocation in parens.",function_strict:"Use the function form of \"use strict\".",get_set:"get/set are ES5 features.",html_confusion_a:"HTML confusion in regular expression '<{a}'.",html_handlers:"Avoid HTML event handlers.",identifier_function:"Expected an identifier in an assignment and instead saw a function invocation.",implied_evil:"Implied eval is evil. Pass a function instead of a string.",infix_in:"Unexpected 'in'. Compare with undefined, or use the hasOwnProperty method instead.",insecure_a:"Insecure '{a}'.",isNaN:"Use the isNaN function to compare with NaN.",label_a_b:"Label '{a}' on '{b}' statement.",lang:"lang is deprecated.",leading_decimal_a:"A leading decimal point can be confused with a dot: '.{a}'.",missing_a:"Missing '{a}'.",missing_a_after_b:"Missing '{a}' after '{b}'.",missing_option:"Missing option value.",missing_property:"Missing property name.",missing_space_a_b:"Missing space between '{a}' and '{b}'.",missing_url:"Missing url.",missing_use_strict:"Missing \"use strict\" statement.",mixed:"Mixed spaces and tabs.",move_invocation:"Move the invocation into the parens that contain the function.",move_var:"Move 'var' declarations to the top of the function.",name_function:"Missing name in function statement.",nested_comment:"Nested comment.",not:"Nested not.",not_a_constructor:"Do not use {a} as a constructor.",not_a_defined:"'{a}' has not been fully defined yet.",not_a_function:"'{a}' is not a function.",not_a_label:"'{a}' is not a label.",not_a_scope:"'{a}' is out of scope.",not_greater:"'{a}' should not be greater than '{b}'.",parameter_a_get_b:"Unexpected parameter '{a}' in get {b} function.",parameter_set_a:"Expected parameter (value) in set {a} function.",radix:"Missing radix parameter.",read_only:"Read only.",redefinition_a:"Redefinition of '{a}'.",reserved_a:"Reserved name '{a}'.",scanned_a_b:"{a} ({b}% scanned).",slash_equal:"A regular expression literal can be confused with '/='.",statement_block:"Expected to see a statement and instead saw a block.",stopping:"Stopping. ",strange_loop:"Strange loop.",strict:"Strict violation.",subscript:"['{a}'] is better written in dot notation.",tag_a_in_b:"A '<{a}>' must be within '<{b}>'.",too_long:"Line too long.",too_many:"Too many errors.",trailing_decimal_a:"A trailing decimal point can be confused with a dot: '.{a}'.",type:"type is unnecessary.",unclosed:"Unclosed string.",unclosed_comment:"Unclosed comment.",unclosed_regexp:"Unclosed regular expression.",unescaped_a:"Unescaped '{a}'.",unexpected_a:"Unexpected '{a}'.",unexpected_char_a_b:"Unexpected character '{a}' in {b}.",unexpected_comment:"Unexpected comment.",unexpected_member_a:"Unexpected property '{a}'.",unexpected_space_a_b:"Unexpected space between '{a}' and '{b}'.",unnecessary_initialize:"It is not necessary to initialize '{a}' to 'undefined'.",unnecessary_use:"Unnecessary \"use strict\".",unreachable_a_b:"Unreachable '{a}' after '{b}'.",unrecognized_style_attribute_a:"Unrecognized style attribute '{a}'.",unrecognized_tag_a:"Unrecognized tag '<{a}>'.",unsafe:"Unsafe character.",url:"JavaScript URL.",use_array:"Use the array literal notation [].",use_braces:"Spaces are hard to count. Use {{a}}.",use_object:"Use the object literal notation {}.",used_before_a:"'{a}' was used before it was defined.",var_a_not:"Variable {a} was not declared correctly.",weird_assignment:"Weird assignment.",weird_condition:"Weird condition.",weird_new:"Weird construction. Delete 'new'.",weird_program:"Weird program.",weird_relation:"Weird relation.",weird_ternary:"Weird ternary.",wrap_immediate:"Wrap an immediate function invocation in parentheses "+"to assist the reader in understanding that the expression "+"is the result of a function, and not the function itself.",wrap_regexp:"Wrap the /regexp/ literal in parens to disambiguate the slash operator.",write_is_wrong:"document.write can be a form of eval."},comments_off,css_attribute_data,css_any,css_colorData={"aliceblue":true,"antiquewhite":true,"aqua":true,"aquamarine":true,"azure":true,"beige":true,"bisque":true,"black":true,"blanchedalmond":true,"blue":true,"blueviolet":true,"brown":true,"burlywood":true,"cadetblue":true,"chartreuse":true,"chocolate":true,"coral":true,"cornflowerblue":true,"cornsilk":true,"crimson":true,"cyan":true,"darkblue":true,"darkcyan":true,"darkgoldenrod":true,"darkgray":true,"darkgreen":true,"darkkhaki":true,"darkmagenta":true,"darkolivegreen":true,"darkorange":true,"darkorchid":true,"darkred":true,"darksalmon":true,"darkseagreen":true,"darkslateblue":true,"darkslategray":true,"darkturquoise":true,"darkviolet":true,"deeppink":true,"deepskyblue":true,"dimgray":true,"dodgerblue":true,"firebrick":true,"floralwhite":true,"forestgreen":true,"fuchsia":true,"gainsboro":true,"ghostwhite":true,"gold":true,"goldenrod":true,"gray":true,"green":true,"greenyellow":true,"honeydew":true,"hotpink":true,"indianred":true,"indigo":true,"ivory":true,"khaki":true,"lavender":true,"lavenderblush":true,"lawngreen":true,"lemonchiffon":true,"lightblue":true,"lightcoral":true,"lightcyan":true,"lightgoldenrodyellow":true,"lightgreen":true,"lightpink":true,"lightsalmon":true,"lightseagreen":true,"lightskyblue":true,"lightslategray":true,"lightsteelblue":true,"lightyellow":true,"lime":true,"limegreen":true,"linen":true,"magenta":true,"maroon":true,"mediumaquamarine":true,"mediumblue":true,"mediumorchid":true,"mediumpurple":true,"mediumseagreen":true,"mediumslateblue":true,"mediumspringgreen":true,"mediumturquoise":true,"mediumvioletred":true,"midnightblue":true,"mintcream":true,"mistyrose":true,"moccasin":true,"navajowhite":true,"navy":true,"oldlace":true,"olive":true,"olivedrab":true,"orange":true,"orangered":true,"orchid":true,"palegoldenrod":true,"palegreen":true,"paleturquoise":true,"palevioletred":true,"papayawhip":true,"peachpuff":true,"peru":true,"pink":true,"plum":true,"powderblue":true,"purple":true,"red":true,"rosybrown":true,"royalblue":true,"saddlebrown":true,"salmon":true,"sandybrown":true,"seagreen":true,"seashell":true,"sienna":true,"silver":true,"skyblue":true,"slateblue":true,"slategray":true,"snow":true,"springgreen":true,"steelblue":true,"tan":true,"teal":true,"thistle":true,"tomato":true,"turquoise":true,"violet":true,"wheat":true,"white":true,"whitesmoke":true,"yellow":true,"yellowgreen":true,"activeborder":true,"activecaption":true,"appworkspace":true,"background":true,"buttonface":true,"buttonhighlight":true,"buttonshadow":true,"buttontext":true,"captiontext":true,"graytext":true,"highlight":true,"highlighttext":true,"inactiveborder":true,"inactivecaption":true,"inactivecaptiontext":true,"infobackground":true,"infotext":true,"menu":true,"menutext":true,"scrollbar":true,"threeddarkshadow":true,"threedface":true,"threedhighlight":true,"threedlightshadow":true,"threedshadow":true,"window":true,"windowframe":true,"windowtext":true},css_border_style,css_break,css_lengthData={'%':true,'cm':true,'em':true,'ex':true,'in':true,'mm':true,'pc':true,'pt':true,'px':true},css_media,css_overflow,devel={alert:false,confirm:false,console:false,Debug:false,opera:false,prompt:false},escapes={'\b':'\\b','\t':'\\t','\n':'\\n','\f':'\\f','\r':'\\r','"':'\\"','/':'\\/','\\':'\\\\'},funct,functionicity=['closure','exception','global','label','outer','unused','var'],functions,global,html_tag={a:{},abbr:{},acronym:{},address:{},applet:{},area:{empty:true,parent:' map '},article:{},aside:{},audio:{},b:{},base:{empty:true,parent:' head '},bdo:{},big:{},blockquote:{},body:{parent:' html noframes '},br:{empty:true},button:{},canvas:{parent:' body p div th td '},caption:{parent:' table '},center:{},cite:{},code:{},col:{empty:true,parent:' table colgroup '},colgroup:{parent:' table '},command:{parent:' menu '},datalist:{},dd:{parent:' dl '},del:{},details:{},dialog:{},dfn:{},dir:{},div:{},dl:{},dt:{parent:' dl '},em:{},embed:{},fieldset:{},figure:{},font:{},footer:{},form:{},frame:{empty:true,parent:' frameset '},frameset:{parent:' html frameset '},h1:{},h2:{},h3:{},h4:{},h5:{},h6:{},head:{parent:' html '},header:{},hgroup:{},hr:{empty:true},'hta:application':{empty:true,parent:' head '},html:{parent:'*'},i:{},iframe:{},img:{empty:true},input:{empty:true},ins:{},kbd:{},keygen:{},label:{},legend:{parent:' details fieldset figure '},li:{parent:' dir menu ol ul '},link:{empty:true,parent:' head '},map:{},mark:{},menu:{},meta:{empty:true,parent:' head noframes noscript '},meter:{},nav:{},noframes:{parent:' html body '},noscript:{parent:' body head noframes '},object:{},ol:{},optgroup:{parent:' select '},option:{parent:' optgroup select '},output:{},p:{},param:{empty:true,parent:' applet object '},pre:{},progress:{},q:{},rp:{},rt:{},ruby:{},samp:{},script:{empty:true,parent:' body div frame head iframe p pre span '},section:{},select:{},small:{},span:{},source:{},strong:{},style:{parent:' head ',empty:true},sub:{},sup:{},table:{},tbody:{parent:' table '},td:{parent:' tr '},textarea:{},tfoot:{parent:' table '},th:{parent:' tr '},thead:{parent:' table '},time:{},title:{parent:' head '},tr:{parent:' table tbody thead tfoot '},tt:{},u:{},ul:{},'var':{},video:{}},ids,implied,in_block,indent,json_mode,lines,lookahead,member,properties,nexttoken,option,predefined,prereg,prevtoken,regexp_flag={g:true,i:true,m:true},rhino={defineClass:false,deserialize:false,gc:false,help:false,load:false,loadClass:false,print:false,quit:false,readFile:false,readUrl:false,runCommand:false,seal:false,serialize:false,spawn:false,sync:false,toint32:false,version:false},scope,semicolon_coda={';':true,'"':true,'\'':true,')':true},src,stack,standard={Array:false,Boolean:false,Date:false,decodeURI:false,decodeURIComponent:false,encodeURI:false,encodeURIComponent:false,Error:false,'eval':false,EvalError:false,Function:false,hasOwnProperty:false,isFinite:false,isNaN:false,JSON:false,Math:false,Number:false,Object:false,parseInt:false,parseFloat:false,RangeError:false,ReferenceError:false,RegExp:false,String:false,SyntaxError:false,TypeError:false,URIError:false},standard_member={E:true,LN2:true,LN10:true,LOG2E:true,LOG10E:true,MAX_VALUE:true,MIN_VALUE:true,NEGATIVE_INFINITY:true,PI:true,POSITIVE_INFINITY:true,SQRT1_2:true,SQRT2:true},strict_mode,syntax={},tab,token,urls,var_mode,warnings,widget={alert:true,animator:true,appleScript:true,beep:true,bytesToUIString:true,Canvas:true,chooseColor:true,chooseFile:true,chooseFolder:true,closeWidget:true,COM:true,convertPathToHFS:true,convertPathToPlatform:true,CustomAnimation:true,escape:true,FadeAnimation:true,filesystem:true,Flash:true,focusWidget:true,form:true,FormField:true,Frame:true,HotKey:true,Image:true,include:true,isApplicationRunning:true,iTunes:true,konfabulatorVersion:true,log:true,md5:true,MenuItem:true,MoveAnimation:true,openURL:true,play:true,Point:true,popupMenu:true,preferenceGroups:true,preferences:true,print:true,prompt:true,random:true,Rectangle:true,reloadWidget:true,ResizeAnimation:true,resolvePath:true,resumeUpdates:true,RotateAnimation:true,runCommand:true,runCommandInBg:true,saveAs:true,savePreferences:true,screen:true,ScrollBar:true,showWidgetPreferences:true,sleep:true,speak:true,Style:true,suppressUpdates:true,system:true,tellWidget:true,Text:true,TextArea:true,Timer:true,unescape:true,updateNow:true,URL:true,Web:true,widget:true,Window:true,XMLDOM:true,XMLHttpRequest:true,yahooCheckLogin:true,yahooLogin:true,yahooLogout:true},windows={ActiveXObject:false,CScript:false,Debug:false,Enumerator:false,System:false,VBArray:false,WScript:false},xmode,xquote,ax=/@cc|<\/?|script|\]\s*\]|<\s*!|&lt/i,cx=/[\u0000-\u001f\u007f-\u009f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/,tx=/^\s*([(){}\[.,:;'"~\?\]#@]|==?=?|\/(\*(jslint|properties|members|global)?|=|\/)?|\*[\/=]?|\+(?:=|\++)?|-(?:=|-+)?|%=?|&[&=]?|\|[|=]?|>>?>?=?|<([\/=!]|\!(\[|--)?|<=?)?|\^=?|\!=?=?|[a-zA-Z_$][a-zA-Z0-9_$]*|[0-9]+([xX][0-9a-fA-F]+|\.[0-9]*)?([eE][+\-]?[0-9]+)?)/,hx=/^\s*(['"=>\/&#]|<(?:\/|\!(?:--)?)?|[a-zA-Z][a-zA-Z0-9_\-:]*|[0-9]+|--)/,nx=/[\u0000-\u001f&<"\/\\\u007f-\u009f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/,nxg=/[\u0000-\u001f&<"\/\\\u007f-\u009f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,ox=/[>&]|<[\/!]?|--/,lx=/\*\/|\/\*/,ix=/^([a-zA-Z_$][a-zA-Z0-9_$]*)$/,jx=/^(?:javascript|jscript|ecmascript|vbscript|mocha|livescript)\s*:/i,ux=/&|\+|\u00AD|\.\.|\/\*|%[^;]|base64|url|expression|data|mailto/i,sx=/^\s*([{:#%.=,>+\[\]@()"';]|\*=?|\$=|\|=|\^=|~=|[a-zA-Z_][a-zA-Z0-9_\-]*|[0-9]+|<\/|\/\*)/,ssx=/^\s*([@#!"'};:\-%.=,+\[\]()*_]|[a-zA-Z][a-zA-Z0-9._\-]*|\/\*?|\d+(?:\.\d+)?|<\/)/,qx=/[^a-zA-Z0-9+\-_\/ ]/,dx=/[\[\]\/\\"'*<>.&:(){}+=#]/,rx={outer:hx,html:hx,style:sx,styleproperty:ssx};function return_this(){return this;}
function F(){}
if(typeof Array.prototype.filter!=='function'){Array.prototype.filter=function(f){var i,length=this.length,result=[];for(i=0;i<length;i+=1){try{result.push(f(this[i]));}catch(ignore){}}
return result;};}
if(typeof Array.isArray!=='function'){Array.isArray=function(o){return Object.prototype.toString.apply(o)==='[object Array]';};}
if(typeof Object.create!=='function'){Object.create=function(o){F.prototype=o;return new F();};}
if(typeof Object.keys!=='function'){Object.keys=function(o){var a=[],k;for(k in o){if(Object.prototype.hasOwnProperty.call(o,k)){a.push(k);}}
return a;};}
if(typeof String.prototype.entityify!=='function'){String.prototype.entityify=function(){return this.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');};}
if(typeof String.prototype.isAlpha!=='function'){String.prototype.isAlpha=function(){return(this>='a'&&this<='z\uffff')||(this>='A'&&this<='Z\uffff');};}
if(typeof String.prototype.isDigit!=='function'){String.prototype.isDigit=function(){return(this>='0'&&this<='9');};}
if(typeof String.prototype.supplant!=='function'){String.prototype.supplant=function(o){return this.replace(/\{([^{}]*)\}/g,function(a,b){var r=o[b];return typeof r==='string'||typeof r==='number'?r:a;});};}
if(typeof String.prototype.name!=='function'){String.prototype.name=function(){if(ix.test(this)){return this;}
if(nx.test(this)){return'"'+this.replace(nxg,function(a){var c=escapes[a];if(c){return c;}
return'\\u'+('0000'+a.charCodeAt().toString(16)).slice(-4);})+'"';}
return'"'+this+'"';};}
function combine(t,o){var n;for(n in o){if(Object.prototype.hasOwnProperty.call(o,n)){t[n]=o[n];}}}
function assume(){if(!option.safe){if(option.rhino){combine(predefined,rhino);}
if(option.devel){combine(predefined,devel);}
if(option.browser){combine(predefined,browser);}
if(option.windows){combine(predefined,windows);}
if(option.widget){combine(predefined,widget);}}}
function quit(message,line,character){throw{name:'JSLintError',line:line,character:character,message:bundle.scanned_a_b.supplant({a:message,b:Math.floor((line/lines.length)*100)})};}
function warn(message,offender,a,b,c,d){var character,line,warning;offender=offender||nexttoken;line=offender.line||0;character=offender.from||0;warning={id:'(error)',raw:message,evidence:lines[line-1]||'',line:line,character:character,a:a||offender.value,b:b,c:c,d:d};warning.reason=message.supplant(warning);JSLINT.errors.push(warning);if(option.passfail){quit(bundle.stopping,line,character);}
warnings+=1;if(warnings>=option.maxerr){quit(bundle.too_many,line,character);}
return warning;}
function warn_at(message,line,character,a,b,c,d){return warn(message,{line:line,from:character},a,b,c,d);}
function fail(message,offender,a,b,c,d){var warning=warn(message,offender,a,b,c,d);quit(bundle.stopping,warning.line,warning.character);}
function fail_at(message,line,character,a,b,c,d){return fail(message,{line:line,from:character},a,b,c,d);}
function expected_at(at){if(option.white&&nexttoken.from!==at){warn(bundle.expected_a_at_b_c,nexttoken,nexttoken.value,at,nexttoken.from);}}
function aint(it,name,expected){if(it[name]!==expected){warn(bundle.expected_a_b,it,expected,it[name]);return true;}else{return false;}}
var lex=(function lex(){var character,comments,from,line,source_row,older_token={};function collect_comment(comment,quote,at){if(older_token.line!==line){if(comments){comments.push(comment);}else{comments=[comment];}}else{if(older_token.postcomments){older_token.postcomments.push(comment);}else{older_token.postcomments=[comment];}}}
function next_line(){var at;if(line>=lines.length){return false;}
character=1;source_row=lines[line];line+=1;at=source_row.search(/ \t/);if(at>=0){warn_at(bundle.mixed,line,at+1);}
source_row=source_row.replace(/\t/g,tab);at=source_row.search(cx);if(at>=0){warn_at(bundle.unsafe,line,at);}
if(option.maxlen&&option.maxlen<source_row.length){warn_at(bundle.too_long,line,source_row.length);}
return true;}
function it(type,value,quote){var id,the_token;if(type==='(string)'||type==='(range)'){if(jx.test(value)){warn_at(bundle.url,line,from);}}
the_token=Object.create(syntax[(type==='(punctuator)'||(type==='(identifier)'&&Object.prototype.hasOwnProperty.call(syntax,value))?value:type)]||syntax['(error)']);if(type==='(identifier)'){the_token.identifier=true;if(value==='__iterator__'||value==='__proto__'){fail_at(bundle.reserved_a,line,from,value);}else if(option.nomen&&(value.charAt(0)==='_'||value.charAt(value.length-1)==='_')){warn_at(bundle.dangling_a,line,from,value);}}
if(value!==undefined){the_token.value=value;}
if(quote){the_token.quote=quote;}
if(comments){the_token.comments=comments;comments=null;}
the_token.line=line;the_token.from=from;the_token.thru=character;the_token.prev=older_token;id=the_token.id;prereg=id&&(('(,=:[!&|?{};'.indexOf(id.charAt(id.length-1))>=0)||id==='return');older_token.next=the_token;older_token=the_token;return the_token;}
return{init:function(source){if(typeof source==='string'){lines=source.replace(/\r\n/g,'\n').replace(/\r/g,'\n').split('\n');}else{lines=source;}
line=0;next_line();from=1;},range:function(begin,end){var c,value='';from=character;if(source_row.charAt(0)!==begin){fail_at(bundle.expected_a_b,line,character,begin,source_row.charAt(0));}
for(;;){source_row=source_row.slice(1);character+=1;c=source_row.charAt(0);switch(c){case'':fail_at(bundle.missing_a,line,character,c);break;case end:source_row=source_row.slice(1);character+=1;return it('(range)',value);case xquote:case'\\':warn_at(bundle.unexpected_a,line,character,c);break;}
value+=c;}},token:function(){var b,c,captures,digit,depth,flag,high,i,j,length,low,quote,t;function match(x){var exec=x.exec(source_row),first;if(exec){length=exec[0].length;first=exec[1];c=first.charAt(0);source_row=source_row.substr(length);from=character+length-first.length;character+=length;return first;}}
function string(x){var c,j,r='';function hex(n){var i=parseInt(source_row.substr(j+1,n),16);j+=n;if(i>=32&&i<=126&&i!==34&&i!==92&&i!==39){warn_at(bundle.unexpected_a,line,character,'\\');}
character+=n;c=String.fromCharCode(i);}
if(json_mode&&x!=='"'){warn_at(bundle.expected_a,line,character,'"');}
if(xquote===x||(xmode==='scriptstring'&&!xquote)){return it('(punctuator)',x);}
j=0;for(;;){while(j>=source_row.length){j=0;if(xmode!=='html'||!next_line()){fail_at(bundle.unclosed,line,from);}}
c=source_row.charAt(j);if(c===x){character+=1;source_row=source_row.substr(j+1);return it('(string)',r,x);}
if(c<' '){if(c==='\n'||c==='\r'){break;}
warn_at(bundle.control_a,line,character+j,source_row.slice(0,j));}else if(c===xquote){warn_at(bundle.bad_html,line,character+j);}else if(c==='<'){if(option.safe&&xmode==='html'){warn_at(bundle.adsafe_a,line,character+j,c);}else if(source_row.charAt(j+1)==='/'&&(xmode||option.safe)){warn_at(bundle.expected_a_b,line,character,'<\\/','</');}else if(source_row.charAt(j+1)==='!'&&(xmode||option.safe)){warn_at(bundle.unexpected_a,line,character,'<!');}}else if(c==='\\'){if(xmode==='html'){if(option.safe){warn_at(bundle.adsafe_a,line,character+j,c);}}else if(xmode==='styleproperty'){j+=1;character+=1;c=source_row.charAt(j);if(c!==x){warn_at(bundle.unexpected_a,line,character,'\\');}}else{j+=1;character+=1;c=source_row.charAt(j);switch(c){case xquote:warn_at(bundle.bad_html,line,character+j);break;case'\\':case'"':case'/':break;case'\'':if(json_mode){warn_at(bundle.unexpected_a,line,character,'\\\'');}
break;case'b':c='\b';break;case'f':c='\f';break;case'n':c='\n';break;case'r':c='\r';break;case't':c='\t';break;case'u':hex(4);break;case'v':if(json_mode){warn_at(bundle.unexpected_a,line,character,'\\v');}
c='\v';break;case'x':if(json_mode){warn_at(bundle.unexpected_a,line,character,'\\x');}
hex(2);break;default:warn_at(bundle.unexpected_a,line,character,'\\');}}}
r+=c;character+=1;j+=1;}}
for(;;){while(!source_row){if(!next_line()){return it('(end)');}}
while(xmode==='outer'){i=source_row.search(ox);if(i===0){break;}else if(i>0){character+=1;source_row=source_row.slice(i);break;}else{if(!next_line()){return it('(end)','');}}}
t=match(rx[xmode]||tx);if(!t){t='';c='';while(source_row&&source_row<'!'){source_row=source_row.substr(1);}
if(source_row){if(xmode==='html'){return it('(error)',source_row.charAt(0));}else{fail_at(bundle.unexpected_a,line,character,source_row.substr(0,1));}}}else{if(c.isAlpha()||c==='_'||c==='$'){return it('(identifier)',t);}
if(c.isDigit()){if(xmode!=='style'&&xmode!=='styleproperty'&&source_row.substr(0,1).isAlpha()){warn_at(bundle.expected_space_a_b,line,character,c,source_row.charAt(0));}
if(c==='0'){digit=t.substr(1,1);if(digit.isDigit()){if(token.id!=='.'&&xmode!=='styleproperty'){warn_at(bundle.unexpected_a,line,character,t);}}else if(json_mode&&(digit==='x'||digit==='X')){warn_at(bundle.unexpected_a,line,character,'0x');}}
if(t.substr(t.length-1)==='.'){warn_at(bundle.trailing_decimal_a,line,character,t);}
if(xmode!=='style'){digit=+t;if(!isFinite(digit)){warn_at(bundle.bad_number,line,character,t);}
t=digit;}
return it('(number)',t);}
switch(t){case'"':case"'":return string(t);case'//':if(comments_off||src||(xmode&&xmode!=='script')){warn_at(bundle.unexpected_comment,line,character);}else if(xmode==='script'&&/<\source_row*\//i.test(source_row)){warn_at(bundle.unexpected_a,line,character,'<\/');}else if((option.safe||xmode==='script')&&ax.test(source_row)){warn_at(bundle.dangerous_comment,line,character);}
collect_comment(source_row);source_row='';break;case'/*':if(comments_off||src||(xmode&&xmode!=='script'&&xmode!=='style'&&xmode!=='styleproperty')){warn_at(bundle.unexpected_comment,line,character);}
if(option.safe&&ax.test(source_row)){warn_at(bundle.dangerous_comment,line,character);}
for(;;){i=source_row.search(lx);if(i>=0){break;}
collect_comment(source_row);if(!next_line()){fail_at(bundle.unclosed_comment,line,character);}else{if(option.safe&&ax.test(source_row)){warn_at(bundle.dangerous_comment,line,character);}}}
character+=i+2;if(source_row.substr(i,1)==='/'){fail_at(bundle.nested_comment,line,character);}
collect_comment(source_row.substr(0,i));source_row=source_row.substr(i+2);break;case'':break;case'/':if(token.id==='/='){fail_at(bundle.slash_equal,line,from);}
if(prereg){depth=0;captures=0;length=0;for(;;){b=true;c=source_row.charAt(length);length+=1;switch(c){case'':fail_at(bundle.unclosed_regexp,line,from);return;case'/':if(depth>0){warn_at(bundle.unescaped_a,line,from+length,'/');}
c=source_row.substr(0,length-1);flag=Object.create(regexp_flag);while(flag[source_row.charAt(length)]===true){flag[source_row.charAt(length)]=false;length+=1;}
if(source_row.charAt(length).isAlpha()){fail_at(bundle.unexpected_a,line,from,source_row.charAt(length));}
character+=length;source_row=source_row.substr(length);quote=source_row.charAt(0);if(quote==='/'||quote==='*'){fail_at(bundle.confusing_regexp,line,from);}
return it('(regexp)',c);case'\\':c=source_row.charAt(length);if(c<' '){warn_at(bundle.control_a,line,from+length,String(c));}else if(c==='<'){warn_at(bundle.unexpected_a,line,from+length,'\\');}
length+=1;break;case'(':depth+=1;b=false;if(source_row.charAt(length)==='?'){length+=1;switch(source_row.charAt(length)){case':':case'=':case'!':length+=1;break;default:warn_at(bundle.expected_a_b,line,from+length,':',source_row.charAt(length));}}else{captures+=1;}
break;case'|':b=false;break;case')':if(depth===0){warn_at(bundle.unescaped_a,line,from+length,')');}else{depth-=1;}
break;case' ':j=1;while(source_row.charAt(length)===' '){length+=1;j+=1;}
if(j>1){warn_at(bundle.use_braces,line,from+length,j);}
break;case'[':c=source_row.charAt(length);if(c==='^'){length+=1;if(option.regexp){warn_at(bundle.insecure_a,line,from+length,c);}else if(source_row.charAt(length)===']'){fail_at(bundle.unescaped_a,line,from+length,'^');}}
quote=false;if(c===']'){warn_at(bundle.empty_class,line,from+length-1);quote=true;}
klass:do{c=source_row.charAt(length);length+=1;switch(c){case'[':case'^':warn_at(bundle.unescaped_a,line,from+length,c);quote=true;break;case'-':if(quote){quote=false;}else{warn_at(bundle.unescaped_a,line,from+length,'-');quote=true;}
break;case']':if(!quote){warn_at(bundle.unescaped_a,line,from+length-1,'-');}
break klass;case'\\':c=source_row.charAt(length);if(c<' '){warn_at(bundle.control_a,line,from+length,String(c));}else if(c==='<'){warn_at(bundle.unexpected_a,line,from+length,'\\');}
length+=1;quote=true;break;case'/':warn_at(bundle.unescaped_a,line,from+length-1,'/');quote=true;break;case'<':if(xmode==='script'){c=source_row.charAt(length);if(c==='!'||c==='/'){warn_at(bundle.html_confusion_a,line,from+length,c);}}
quote=true;break;default:quote=true;}}while(c);break;case'.':if(option.regexp){warn_at(bundle.insecure_a,line,from+length,c);}
break;case']':case'?':case'{':case'}':case'+':case'*':warn_at(bundle.unescaped_a,line,from+length,c);break;case'<':if(xmode==='script'){c=source_row.charAt(length);if(c==='!'||c==='/'){warn_at(bundle.html_confusion_a,line,from+length,c);}}
break;}
if(b){switch(source_row.charAt(length)){case'?':case'+':case'*':length+=1;if(source_row.charAt(length)==='?'){length+=1;}
break;case'{':length+=1;c=source_row.charAt(length);if(c<'0'||c>'9'){warn_at(bundle.expected_number_a,line,from+length,c);}
length+=1;low=+c;for(;;){c=source_row.charAt(length);if(c<'0'||c>'9'){break;}
length+=1;low=+c+(low*10);}
high=low;if(c===','){length+=1;high=Infinity;c=source_row.charAt(length);if(c>='0'&&c<='9'){length+=1;high=+c;for(;;){c=source_row.charAt(length);if(c<'0'||c>'9'){break;}
length+=1;high=+c+(high*10);}}}
if(source_row.charAt(length)!=='}'){warn_at(bundle.expected_a_b,line,from+length,'}',c);}else{length+=1;}
if(source_row.charAt(length)==='?'){length+=1;}
if(low>high){warn_at(bundle.not_greater,line,from+length,low,high);}
break;}}}
c=source_row.substr(0,length-1);character+=length;source_row=source_row.substr(length);return it('(regexp)',c);}
return it('(punctuator)',t);case'<!--':length=line;c=character;for(;;){i=source_row.indexOf('--');if(i>=0){break;}
i=source_row.indexOf('<!');if(i>=0){fail_at(bundle.nested_comment,line,character+i);}
if(!next_line()){fail_at(bundle.unclosed_comment,length,c);}}
length=source_row.indexOf('<!');if(length>=0&&length<i){fail_at(bundle.nested_comment,line,character+length);}
character+=i;if(source_row.charAt(i+2)!=='>'){fail_at(bundle.expected_a,line,character,'-->');}
character+=3;source_row=source_row.slice(i+3);break;case'#':if(xmode==='html'||xmode==='styleproperty'){for(;;){c=source_row.charAt(0);if((c<'0'||c>'9')&&(c<'a'||c>'f')&&(c<'A'||c>'F')){break;}
character+=1;source_row=source_row.substr(1);t+=c;}
if(t.length!==4&&t.length!==7){warn_at(bundle.bad_color_a,line,from+length,t);}
return it('(color)',t);}
return it('(punctuator)',t);default:if(xmode==='outer'&&c==='&'){character+=1;source_row=source_row.substr(1);for(;;){c=source_row.charAt(0);character+=1;source_row=source_row.substr(1);if(c===';'){break;}
if(!((c>='0'&&c<='9')||(c>='a'&&c<='z')||c==='#')){fail_at(bundle.bad_entity,line,from+length,character);}}
break;}
return it('(punctuator)',t);}}}}};}());function add_label(t,type){if(option.safe&&funct['(global)']&&typeof predefined[t]!=='boolean'){warn(bundle.adsafe_a,token,t);}else if(t==='hasOwnProperty'){warn(bundle.bad_name_a,token,t);}
if(Object.prototype.hasOwnProperty.call(funct,t)&&!funct['(global)']){warn(funct[t]===true?bundle.used_before_a:bundle.already_defined,nexttoken,t);}
funct[t]=type;if(funct['(global)']){if(global[t]===false){warn(bundle.read_only);}
global[t]=true;if(Object.prototype.hasOwnProperty.call(implied,t)){warn(bundle.used_before_a,nexttoken,t);delete implied[t];}}else{scope[t]=funct;}}
function peek(distance){var found,slot=0;distance=distance||0;while(slot<=distance){found=lookahead[slot];if(!found){found=lookahead[slot]=lex.token();}
slot+=1;}
return found;}
function discard(it){var next,prev;it=it||token;if(it.postcomments){next=it.next||peek();next.comments=next.comments?next.comments.concat(it.postcomments):it.postcomments;}
if(it.comments){prev=it.prev;while(prev.postcomments===null){prev=prev.prev;}
if(prev.postcomments){prev.postcomments=prev.postcomments.concat(it.comments);}else{prev.postcomments=it.comments;}}
it.comments=null;it.postcomments=null;}
function advance(id,match){if(indent){if(var_mode&&nexttoken.line!==token.line){if((var_mode!==indent||!nexttoken.edge)&&nexttoken.from===indent.at-
(nexttoken.edge?option.indent:0)){var dent=indent;for(;;){dent.at-=option.indent;if(dent===var_mode){break;}
dent=dent.was;}
dent.open=false;}
var_mode=false;}
if(indent.open){if(nexttoken.edge){if(nexttoken.edge==='label'){expected_at(1);}else if(nexttoken.edge==='case'){expected_at(indent.at-option.indent);}else if(indent.mode!=='array'||nexttoken.line!==token.line){expected_at(indent.at);}}else if(nexttoken.line!==token.line&&nexttoken.from<indent.at+(indent.mode==='expression'?0:option.indent)){expected_at(indent.at+option.indent);}}else if(nexttoken.line!==token.line){if(nexttoken.edge){expected_at(indent.at);}else{indent.wrap=true;if(indent.mode==='statement'||indent.mode==='var'){expected_at(indent.at+option.indent);}else if(nexttoken.from<indent.at+(indent.mode==='expression'?0:option.indent)){expected_at(indent.at+option.indent);}}}}
switch(token.id){case'(number)':if(nexttoken.id==='.'){warn(bundle.trailing_decimal_a);}
break;case'-':if(nexttoken.id==='-'||nexttoken.id==='--'){warn(bundle.confusing_a);}
break;case'+':if(nexttoken.id==='+'||nexttoken.id==='++'){warn(bundle.confusing_a);}
break;}
if(token.arity==='string'||token.identifier){anonname=token.value;}
if(id&&nexttoken.id!==id){if(match){warn(bundle.expected_a_b_from_c_d,nexttoken,id,match.id,match.line,nexttoken.value);}else if(!nexttoken.identifier||nexttoken.value!==id){warn(bundle.expected_a_b,nexttoken,id,nexttoken.value);}}
prevtoken=token;token=nexttoken;nexttoken=lookahead.shift()||lex.token();if(token.id==='(end)'){discard();}}
function directive(){var command=this.id,name,old_comments_off=comments_off,old_option_white=option.white,value;comments_off=true;option.white=false;if(lookahead.length>0||this.postcomments||nexttoken.comments){warn(bundle.unexpected_a,this);}
switch(command){case'/*properties':case'/*members':command='/*properties';if(!properties){properties={};}
break;case'/*jslint':if(option.safe){warn(bundle.adsafe_a,this);}
break;case'/*global':if(option.safe){warn(bundle.adsafe_a,this);}
break;default:fail("What?");}
loop:for(;;){for(;;){if(nexttoken.id==='*/'){break loop;}
if(nexttoken.id!==','){break;}
advance();}
if(nexttoken.arity!=='string'&&!nexttoken.identifier){fail(bundle.unexpected_a,nexttoken);}
name=nexttoken.value;advance();switch(command){case'/*global':if(nexttoken.id===':'){advance(':');switch(nexttoken.id){case'true':if(typeof scope[name]==='object'||global[name]===false){fail(bundle.unexpected_a);}
global[name]=true;advance('true');break;case'false':if(typeof scope[name]==='object'){fail(bundle.unexpected_a);}
global[name]=false;advance('false');break;default:fail(bundle.unexpected_a);}}else{if(typeof scope[name]==='object'){fail(bundle.unexpected_a);}
global[name]=false;}
break;case'/*jslint':if(nexttoken.id!==':'){fail(bundle.expected_a_b,nexttoken,':',nexttoken.value);}
advance(':');switch(name){case'indent':value=+nexttoken.value;if(typeof value!=='number'||!isFinite(value)||value<0||Math.floor(value)!==value){fail(bundle.expected_small_a);}
if(value>0){old_option_white=true;}
option.indent=value;break;case'maxerr':value=+nexttoken.value;if(typeof value!=='number'||!isFinite(value)||value<=0||Math.floor(value)!==value){fail(bundle.expected_small_a,nexttoken);}
option.maxerr=value;break;case'maxlen':value=+nexttoken.value;if(typeof value!=='number'||!isFinite(value)||value<0||Math.floor(value)!==value){fail(bundle.expected_small_a);}
option.maxlen=value;break;case'white':if(nexttoken.id==='true'){old_option_white=true;}else if(nexttoken.id==='false'){old_option_white=false;}else{fail(bundle.unexpected_a);}
break;default:if(nexttoken.id==='true'){option[name]=true;}else if(nexttoken.id==='false'){option[name]=false;}else{fail(bundle.unexpected_a);}}
advance();break;case'/*properties':properties[name]=true;break;default:fail(bundle.unexpected_a);}}
if(command==='/*jslint'){assume();}
comments_off=old_comments_off;advance('*/');option.white=old_option_white;}
function edge(mode){nexttoken.edge=!indent||(indent.open&&(mode||true));}
function step_in(mode){var open,was;if(typeof mode==='number'){indent={at:mode,open:true,was:was};}else if(!indent){indent={at:1,mode:'statement',open:true};}else{was=indent;open=mode==='var'||(nexttoken.line!==token.line&&mode!=='statement');indent={at:(open||mode==='control'?was.at+option.indent:was.at)+
(was.wrap?option.indent:0),mode:mode,open:open,was:was};if(mode==='var'&&open){var_mode=indent;}}}
function step_out(id,t){if(id){if(indent&&indent.open){indent.at-=option.indent;edge();}
advance(id,t);}
if(indent){indent=indent.was;}}
function one_space(left,right){left=left||token;right=right||nexttoken;if(right.id!=='(end)'&&option.white&&(token.line!==right.line||token.thru+1!==right.from)){warn(bundle.expected_space_a_b,right,token.value,right.value);}}
function one_space_only(left,right){left=left||token;right=right||nexttoken;if(right.id!=='(end)'&&(left.line!==right.line||(option.white&&left.thru+1!==right.from))){warn(bundle.expected_space_a_b,right,left.value,right.value);}}
function no_space(left,right){left=left||token;right=right||nexttoken;if((option.white||xmode==='styleproperty'||xmode==='style')&&left.thru!==right.from&&left.line===right.line){warn(bundle.unexpected_space_a_b,right,left.value,right.value);}}
function no_space_only(left,right){left=left||token;right=right||nexttoken;if(right.id!=='(end)'&&(left.line!==right.line||(option.white&&left.thru!==right.from))){warn(bundle.unexpected_space_a_b,right,left.value,right.value);}}
function spaces(left,right){if(option.white){left=left||token;right=right||nexttoken;if(left.thru===right.from&&left.line===right.line){warn(bundle.missing_space_a_b,right,left.value,right.value);}}}
function comma(){if(nexttoken.id!==','){warn_at(bundle.expected_a_b,token.line,token.thru,',',nexttoken.value);}else{if(option.white){no_space_only();}
advance(',');discard();spaces();}}
function semicolon(){if(nexttoken.id!==';'){warn_at(bundle.expected_a_b,token.line,token.thru,';',nexttoken.value);}else{if(option.white){no_space_only();}
advance(';');discard();if(semicolon_coda[nexttoken.id]!==true){spaces();}}}
function use_strict(){if(nexttoken.value==='use strict'){if(strict_mode){warn(bundle.unnecessary_use);}
edge();advance();semicolon();strict_mode=true;option.newcap=true;option.undef=true;return true;}else{return false;}}
function are_similar(a,b){if(a===b){return true;}
if(Array.isArray(a)){if(Array.isArray(b)&&a.length===b.length){var i;for(i=0;i<a.length;i+=1){if(!are_similar(a[i],b[i])){return false;}}
return true;}
return false;}
if(Array.isArray(b)){return false;}
if(a.arity===b.arity&&a.value===b.value){switch(a.arity){case'prefix':case'suffix':case undefined:return are_similar(a.first,b.first);case'infix':return are_similar(a.first,b.first)&&are_similar(a.second,b.second);case'ternary':return are_similar(a.first,b.first)&&are_similar(a.second,b.second)&&are_similar(a.third,b.third);case'function':case'regexp':return false;default:return true;}}else{if(a.id==='.'&&b.id==='['&&b.arity==='infix'){return a.second.value===b.second.value&&b.second.arity==='string';}else if(a.id==='['&&a.arity==='infix'&&b.id==='.'){return a.second.value===b.second.value&&a.second.arity==='string';}}
return false;}
function expression(rbp,initial){var left;if(nexttoken.id==='(end)'){fail(bundle.unexpected_a,token,nexttoken.id);}
advance();if(option.safe&&typeof predefined[token.value]==='boolean'&&(nexttoken.id!=='('&&nexttoken.id!=='.')){warn(bundle.adsafe,token);}
if(initial){anonname='anonymous';funct['(verb)']=token.value;}
if(initial===true&&token.fud){left=token.fud();}else{if(token.nud){left=token.nud();}else{if(nexttoken.arity==='number'&&token.id==='.'){warn(bundle.leading_decimal_a,token,nexttoken.value);advance();return token;}else{fail(bundle.expected_identifier_a,token,token.id);}}
while(rbp<nexttoken.lbp){advance();if(token.led){left=token.led(left);}else{fail(bundle.expected_operator_a,token,token.id);}}}
return left;}
function symbol(s,p){var x=syntax[s];if(!x||typeof x!=='object'){syntax[s]=x={id:s,lbp:p,value:s};}
return x;}
function delim(s){return symbol(s,0);}
function postscript(x){x.postscript=true;return x;}
function ultimate(s){var x=symbol(s,0);x.from=1;x.thru=1;x.line=0;x.edge=true;s.value=s;return postscript(x);}
function stmt(s,f){var x=delim(s);x.identifier=x.reserved=true;x.fud=f;return x;}
function labeled_stmt(s,f){var x=stmt(s,f);x.labeled=true;}
function disrupt_stmt(s,f){var x=stmt(s,f);x.disrupt=true;}
function reserve_name(x){var c=x.id.charAt(0);if((c>='a'&&c<='z')||(c>='A'&&c<='Z')){x.identifier=x.reserved=true;}
return x;}
function prefix(s,f){var x=symbol(s,150);reserve_name(x);x.nud=(typeof f==='function')?f:function(){if(s==='typeof'){one_space();}else{no_space_only();}
this.first=expression(150);this.arity='prefix';if(this.id==='++'||this.id==='--'){if(option.plusplus){warn(bundle.unexpected_a,this);}else if((!this.first.identifier||this.first.reserved)&&this.first.id!=='.'&&this.first.id!=='['){warn(bundle.bad_operand,this);}}
return this;};return x;}
function type(s,arity,nud){var x=delim(s);x.arity=arity;if(nud){x.nud=nud;}
return x;}
function reserve(s,f){var x=delim(s);x.identifier=x.reserved=true;if(typeof f==='function'){x.nud=f;}
return x;}
function reservevar(s,v){return reserve(s,function(){if(typeof v==='function'){v(this);}
return this;});}
function infix(s,p,f,w){var x=symbol(s,p);reserve_name(x);x.led=function(left){this.arity='infix';if(!w){spaces(prevtoken,token);spaces();}
if(typeof f==='function'){return f(left,this);}else{this.first=left;this.second=expression(p);return this;}};return x;}
function expected_relation(node,message){if(node.assign){warn(message||bundle.conditional_assignment,node);}
return node;}
function expected_condition(node,message){switch(node.id){case'[':case'-':if(node.arity!=='infix'){warn(message||bundle.weird_condition,node);}
break;case'false':case'function':case'Infinity':case'NaN':case'null':case'true':case'undefined':case'void':case'(number)':case'(regexp)':case'(string)':case'{':warn(message||bundle.weird_condition,node);break;}
return node;}
function check_relation(node){switch(node.arity){case'prefix':switch(node.id){case'{':case'[':warn(bundle.unexpected_a,node);break;case'!':warn(bundle.confusing_a,node);break;}
break;case'function':case'regexp':warn(bundle.unexpected_a,node);break;default:if(node.id==='NaN'){warn(bundle.isNaN,node);}}
return node;}
function relation(s,eqeq){var x=infix(s,100,function(left,that){check_relation(left);if(eqeq){warn(bundle.expected_a_b,that,eqeq,that.id);}
var right=expression(100);if(are_similar(left,right)||((left.arity==='string'||left.arity==='number')&&(right.arity==='string'||right.arity==='number'))){warn(bundle.weird_relation,that);}
that.first=left;that.second=check_relation(right);return that;});return x;}
function assignop(s,bit){var x=infix(s,20,function(left,that){var l;if(option.bitwise&&bit){warn(bundle.unexpected_a,that);}
that.first=left;if(funct[left.value]===false){warn(bundle.read_only,left);}else if(left['function']){warn(bundle.a_function,left);}
if(option.safe){l=left;do{if(typeof predefined[l.value]==='boolean'){warn(bundle.adsafe,l);}
l=l.first;}while(l);}
if(left){if(left===syntax['function']){warn(bundle.identifier_function,token);}
if(left.id==='.'||left.id==='['){if(!left.first||left.first.value==='arguments'){warn(bundle.bad_assignment,that);}
that.second=expression(19);if(that.id==='='&&are_similar(that.first,that.second)){warn(bundle.weird_assignment,that);}
return that;}else if(left.identifier&&!left.reserved){if(funct[left.value]==='exception'){warn(bundle.assign_exception,left);}
that.second=expression(19);if(that.id==='='&&are_similar(that.first,that.second)){warn(bundle.weird_assignment,that);}
return that;}}
fail(bundle.bad_assignment,that);});x.assign=true;return x;}
function bitwise(s,p){return infix(s,p,function(left,that){if(option.bitwise){warn(bundle.unexpected_a,that);}
that.first=left;that.second=expression(p);return that;});}
function suffix(s,f){var x=symbol(s,150);x.led=function(left){no_space_only(prevtoken,token);if(option.plusplus){warn(bundle.unexpected_a,this);}else if((!left.identifier||left.reserved)&&left.id!=='.'&&left.id!=='['){warn(bundle.bad_operand,this);}
this.first=left;this.arity='suffix';return this;};return x;}
function optional_identifier(){if(nexttoken.identifier){advance();if(option.safe&&banned[token.value]){warn(bundle.adsafe_a,token);}else if(token.reserved&&!option.es5){warn(bundle.expected_identifier_a_reserved,token);}
return token.value;}}
function identifier(){var i=optional_identifier();if(i){return i;}
if(token.id==='function'&&nexttoken.id==='('){warn(bundle.name_function);}else{fail(bundle.expected_identifier_a);}}
function statement(no_indent){var label,old_scope=scope,the_statement;if(nexttoken.id===';'){warn(bundle.unexpected_a);semicolon();return;}
if(nexttoken.identifier&&!nexttoken.reserved&&peek().id===':'){edge('label');label=nexttoken;advance();discard();advance(':');discard();scope=Object.create(old_scope);add_label(label.value,'label');if(nexttoken.labeled!==true){warn(bundle.label_a_b,nexttoken,label.value,nexttoken.value);}
if(jx.test(label.value+':')){warn(bundle.url,label);}
nexttoken.label=label;}
edge();step_in('statement');the_statement=expression(0,true);if(the_statement){if(the_statement.arity==='statement'){if(the_statement.id==='switch'||(the_statement.block&&the_statement.id!=='do')){spaces();}else{semicolon();}}else{if(the_statement.id==='('){if(the_statement.first.id==='new'){warn(bundle.bad_new);}}else if(!the_statement.assign&&the_statement.id!=='delete'&&the_statement.id!=='++'&&the_statement.id!=='--'){warn(bundle.assignment_function_expression,token);}
semicolon();}}
step_out();scope=old_scope;return the_statement;}
function statements(){var array=[],disruptor,the_statement;while(nexttoken.postscript!==true){if(nexttoken.id===';'){warn(bundle.unexpected_a,nexttoken);semicolon();}else{if(disruptor){warn(bundle.unreachable_a_b,nexttoken,nexttoken.value,disruptor.value);disruptor=null;}
the_statement=statement();if(the_statement){array.push(the_statement);if(the_statement.disrupt){disruptor=the_statement;array.disrupt=true;}}}}
return array;}
function block(ordinary){var array,curly=nexttoken,old_inblock=in_block,old_scope=scope,old_strict_mode=strict_mode;in_block=ordinary;scope=Object.create(scope);spaces();if(nexttoken.id==='{'){advance('{');step_in();if(!ordinary&&!use_strict()&&!old_strict_mode&&option.strict&&funct['(context)']['(global)']){warn(bundle.missing_use_strict);}
array=statements();strict_mode=old_strict_mode;step_out('}',curly);discard();}else if(!ordinary){fail(bundle.expected_a_b,nexttoken,'{',nexttoken.value);}else{warn(bundle.expected_a_b,nexttoken,'{',nexttoken.value);array=[statement()];array.disrupt=array[0].disrupt;}
funct['(verb)']=null;scope=old_scope;in_block=old_inblock;if(ordinary&&array.length===0){warn(bundle.empty_block);}
return array;}
function tally_member(name){if(properties&&typeof properties[name]!=='boolean'){warn(bundle.unexpected_member_a,token,name);}
if(typeof member[name]==='number'){member[name]+=1;}else{member[name]=1;}}
function note_implied(token){var name=token.value,line=token.line,a=implied[name];if(typeof a==='function'){a=false;}
if(!a){a=[line];implied[name]=a;}else if(a[a.length-1]!==line){a.push(line);}}
syntax['(identifier)']={type:'(identifier)',lbp:0,identifier:true,nud:function(){var variable=this.value,site=scope[variable];if(typeof site==='function'){site=undefined;}
if(funct===site){switch(funct[variable]){case'error':warn(bundle.unexpected_a,token);funct[variable]='var';break;case'unused':funct[variable]='var';break;case'unction':funct[variable]='function';this['function']=true;break;case'function':this['function']=true;break;case'label':warn(bundle.a_label,token,variable);break;}}else if(funct['(global)']){if(typeof global[variable]==='boolean'){funct[variable]=global[variable];}else{if(option.undef){warn(bundle.not_a_defined,token,variable);}else{note_implied(token);}}}else{switch(funct[variable]){case'closure':case'function':case'var':case'unused':warn(bundle.a_scope,token,variable);break;case'label':warn(bundle.a_label,token,variable);break;case'outer':case true:case false:break;default:if(typeof site==='boolean'){funct[variable]=site;functions[0][variable]=true;}else if(site===null){warn(bundle.a_not_allowed,token,variable);note_implied(token);}else if(typeof site!=='object'){if(option.undef){warn(bundle.a_not_defined,token,variable);}else{funct[variable]=true;}
note_implied(token);}else{switch(site[variable]){case'function':case'unction':this['function']=true;site[variable]='closure';funct[variable]=site['(global)']?false:'outer';break;case'var':case'unused':site[variable]='closure';funct[variable]=site['(global)']?true:'outer';break;case'closure':case'parameter':funct[variable]=site['(global)']?true:'outer';break;case'error':warn(bundle.not_a_defined,token);break;case'label':warn(bundle.a_label,token,variable);break;}}}}
return this;},led:function(){fail(bundle.expected_operator_a);}};type('(number)','number',return_this);type('(string)','string',return_this);type('(regexp)','regexp',return_this);type('(color)','color');type('(range)','range');ultimate('(begin)');ultimate('(end)');ultimate('(error)');postscript(delim('</'));delim('<!');delim('<!--');delim('-->');postscript(delim('}'));delim(')');delim(']');postscript(delim('"'));postscript(delim('\''));delim(';');delim(':');delim(',');delim('#');delim('@');delim('*/');reserve('else');postscript(reserve('case'));reserve('catch');postscript(reserve('default'));reserve('finally');reservevar('arguments',function(x){if(strict_mode&&funct['(global)']){warn(bundle.strict,x);}else if(option.safe){warn(bundle.adsafe,x);}});reservevar('eval',function(x){if(option.safe){warn(bundle.adsafe,x);}});reservevar('false');reservevar('Infinity');reservevar('NaN');reservevar('null');reservevar('this',function(x){if(strict_mode&&((funct['(statement)']&&funct['(name)'].charAt(0)>'Z')||funct['(global)'])){warn(bundle.strict,x);}else if(option.safe){warn(bundle.adsafe,x);}});reservevar('true');reservevar('undefined');assignop('=');assignop('+=');assignop('-=');assignop('*=');assignop('/=').nud=function(){fail(bundle.slash_equal);};assignop('%=');assignop('&=',true);assignop('|=',true);assignop('^=',true);assignop('<<=',true);assignop('>>=',true);assignop('>>>=',true);infix('?',30,function(left,that){that.first=expected_condition(expected_relation(left));that.second=expression(0);spaces();advance(':');discard();spaces();that.third=expression(10);that.arity='ternary';if(are_similar(that.second,that.third)){warn(bundle.weird_ternary,that);}
return that;});infix('||',40,function(left,that){function paren_check(that){if(that.id==='&&'&&!that.paren){warn(bundle.and,that);}
return that;}
that.first=paren_check(expected_condition(expected_relation(left)));that.second=paren_check(expected_relation(expression(40)));if(are_similar(that.first,that.second)){warn(bundle.weird_condition,that);}
return that;});infix('&&',50,function(left,that){that.first=expected_condition(expected_relation(left));that.second=expected_relation(expression(50));if(are_similar(that.first,that.second)){warn(bundle.weird_condition,that);}
return that;});prefix('void',function(){this.first=expression(0);if(this.first.arity!=='number'||this.first.value){warn(bundle.unexpected_a,this);return this;}
return this;});bitwise('|',70);bitwise('^',80);bitwise('&',90);relation('==','===');relation('===');relation('!=','!==');relation('!==');relation('<');relation('>');relation('<=');relation('>=');bitwise('<<',120);bitwise('>>',120);bitwise('>>>',120);infix('in',120,function(left,that){warn(bundle.infix_in,that);that.left=left;that.right=expression(130);return that;});infix('instanceof',120);infix('+',130,function(left,that){if(!left.value){if(left.arity==='number'){warn(bundle.unexpected_a,left);}else if(left.arity==='string'){warn(bundle.expected_a_b,left,'String','\'\'');}}
var right=expression(130);if(!right.value){if(right.arity==='number'){warn(bundle.unexpected_a,right);}else if(right.arity==='string'){warn(bundle.expected_a_b,right,'String','\'\'');}}
if(left.arity===right.arity&&(left.arity==='string'||left.arity==='number')){left.value+=right.value;left.thru=right.thru;if(left.arity==='string'&&jx.test(left.value)){warn(bundle.url,left);}
discard(right);discard(that);return left;}
that.first=left;that.second=right;return that;});prefix('+','num');prefix('+++',function(){warn(bundle.confusing_a,token);this.first=expression(150);this.arity='prefix';return this;});infix('+++',130,function(left){warn(bundle.confusing_a,token);this.first=left;this.second=expression(130);return this;});infix('-',130,function(left,that){if((left.arity==='number'&&left.value===0)||left.arity==='string'){warn(bundle.unexpected_a,left);}
var right=expression(130);if((right.arity==='number'&&right.value===0)||right.arity==='string'){warn(bundle.unexpected_a,left);}
if(left.arity===right.arity&&left.arity==='number'){left.value-=right.value;left.thru=right.thru;discard(right);discard(that);return left;}
that.first=left;that.second=right;return that;});prefix('-');prefix('---',function(){warn(bundle.confusing_a,token);this.first=expression(150);this.arity='prefix';return this;});infix('---',130,function(left){warn(bundle.confusing_a,token);this.first=left;this.second=expression(130);return this;});infix('*',140,function(left,that){if((left.arity==='number'&&(left.value===0||left.value===1))||left.arity==='string'){warn(bundle.unexpected_a,left);}
var right=expression(140);if((right.arity==='number'&&(right.value===0||right.value===1))||right.arity==='string'){warn(bundle.unexpected_a,right);}
if(left.arity===right.arity&&left.arity==='number'){left.value*=right.value;left.thru=right.thru;discard(right);discard(that);return left;}
that.first=left;that.second=right;return that;});infix('/',140,function(left,that){if((left.arity==='number'&&left.value===0)||left.arity==='string'){warn(bundle.unexpected_a,left);}
var right=expression(140);if((right.arity==='number'&&(right.value===0||right.value===1))||right.arity==='string'){warn(bundle.unexpected_a,right);}
if(left.arity===right.arity&&left.arity==='number'){left.value/=right.value;left.thru=right.thru;discard(right);discard(that);return left;}
that.first=left;that.second=right;return that;});infix('%',140,function(left,that){if((left.arity==='number'&&(left.value===0||left.value===1))||left.arity==='string'){warn(bundle.unexpected_a,left);}
var right=expression(140);if((right.arity==='number'&&(right.value===0||right.value===1))||right.arity==='string'){warn(bundle.unexpected_a,right);}
if(left.arity===right.arity&&left.arity==='number'){left.value%=right.value;left.thru=right.thru;discard(right);discard(that);return left;}
that.first=left;that.second=right;return that;});suffix('++');prefix('++');suffix('--');prefix('--');prefix('delete',function(){one_space();var p=expression(0);if(!p||(p.id!=='.'&&p.id!=='[')){warn(bundle.deleted);}
this.first=p;return this;});prefix('~',function(){no_space_only();if(option.bitwise){warn(bundle.unexpected_a,this);}
expression(150);return this;});prefix('!',function(){no_space_only();this.first=expression(150);this.arity='prefix';if(bang[this.first.id]===true){warn(bundle.confusing_a,this);}
return this;});prefix('typeof');prefix('new',function(){one_space();var c=expression(160),i,p;this.first=c;if(c.id!=='function'){if(c.identifier){switch(c.value){case'Object':warn(bundle.use_object,token);break;case'Array':if(nexttoken.id==='('){p=nexttoken;p.first=this;advance('(');if(nexttoken.id!==')'){p.second=expression(0);if(p.second.arity!=='number'||!p.second.value){expected_condition(p.second,bundle.use_array);i=false;}else{i=true;}
while(nexttoken.id!==')'&&nexttoken.id!=='(end)'){if(i){warn(bundle.use_array,p);i=false;}
advance();}}else{warn(bundle.use_array,token);}
advance(')',p);discard();return p;}
warn(bundle.use_array,token);break;case'Number':case'String':case'Boolean':case'Math':case'JSON':warn(bundle.not_a_constructor,c);break;case'Function':if(!option.evil){warn(bundle.function_eval);}
break;case'Date':case'RegExp':break;default:if(c.id!=='function'){i=c.value.substr(0,1);if(option.newcap&&(i<'A'||i>'Z')){warn(bundle.constructor_name_a,token);}}}}else{if(c.id!=='.'&&c.id!=='['&&c.id!=='('){warn(bundle.bad_constructor,token);}}}else{warn(bundle.weird_new,this);}
if(nexttoken.id!=='('){warn(bundle.missing_a,nexttoken,'()');}
return this;});infix('(',160,function(left,that){if(indent&&indent.mode==='expression'){no_space(prevtoken,token);}else{no_space_only(prevtoken,token);}
if(!left.immed&&left.id==='function'){warn(bundle.wrap_immediate);}
var p=[];if(left){if(left.identifier){if(left.value.match(/^[A-Z]([A-Z0-9_$]*[a-z][A-Za-z0-9_$]*)?$/)){if(left.value!=='Number'&&left.value!=='String'&&left.value!=='Boolean'&&left.value!=='Date'){if(left.value==='Math'||left.value==='JSON'){warn(bundle.not_a_function,left);}else if(left.value==='Object'){warn(bundle.use_object,token);}else if(left.value==='Array'||option.newcap){warn(bundle.missing_a,left,'new');}}}}else if(left.id==='.'){if(option.safe&&left.first.value==='Math'&&left.second==='random'){warn(bundle.adsafe,left);}}}
step_in();if(nexttoken.id!==')'){no_space();for(;;){edge();p.push(expression(10));if(nexttoken.id!==','){break;}
comma();}}
no_space();step_out(')',that);if(typeof left==='object'){if(left.value==='parseInt'&&p.length===1){warn(bundle.radix,left);}
if(!option.evil){if(left.value==='eval'||left.value==='Function'||left.value==='execScript'){warn(bundle.evil,left);}else if(p[0]&&p[0].arity==='string'&&(left.value==='setTimeout'||left.value==='setInterval')){warn(bundle.implied_evil,left);}}
if(!left.identifier&&left.id!=='.'&&left.id!=='['&&left.id!=='('&&left.id!=='&&'&&left.id!=='||'&&left.id!=='?'){warn(bundle.bad_invocation,left);}}
that.first=left;that.second=p;return that;},true);prefix('(',function(){step_in('expression');discard();no_space();edge();if(nexttoken.id==='function'){nexttoken.immed=true;}
var value=expression(0);value.paren=true;no_space();step_out(')',this);discard();if(value.id==='function'){if(nexttoken.id==='('){warn(bundle.move_invocation);}else{warn(bundle.bad_wrap,this);}}
return value;});infix('.',170,function(left,that){no_space(prevtoken,token);no_space();var m=identifier();if(typeof m==='string'){tally_member(m);}
that.first=left;that.second=token;if(left&&left.value==='arguments'&&(m==='callee'||m==='caller')){warn(bundle.avoid_a,left,'arguments.'+m);}else if(!option.evil&&left&&left.value==='document'&&(m==='write'||m==='writeln')){warn(bundle.write_is_wrong,left);}else if(option.adsafe){if(!adsafe_top&&left.value==='ADSAFE'){if(m==='id'||m==='lib'){warn(bundle.adsafe,that);}else if(m==='go'){if(xmode!=='script'){warn(bundle.adsafe,that);}else if(adsafe_went||nexttoken.id!=='('||peek(0).arity!=='string'||peek(0).value!==adsafe_id||peek(1).id!==','){fail(bundle.adsafe_a,that,'go');}
adsafe_went=true;adsafe_may=false;}}
adsafe_top=false;}
if(!option.evil&&(m==='eval'||m==='execScript')){warn(bundle.evil);}else if(option.safe){for(;;){if(banned[m]===true){warn(bundle.adsafe_a,token,m);}
if(typeof predefined[left.value]!=='boolean'||nexttoken.id==='('){break;}
if(standard_member[m]===true){if(nexttoken.id==='.'){warn(bundle.adsafe,that);}
break;}
if(nexttoken.id!=='.'){warn(bundle.adsafe,that);break;}
advance('.');token.first=that;token.second=m;that=token;m=identifier();if(typeof m==='string'){tally_member(m);}}}
return that;},true);infix('[',170,function(left,that){no_space_only(prevtoken,token);no_space();step_in();edge();var e=expression(0),s;if(e.arity==='string'){if(option.safe&&banned[e.value]===true){warn(bundle.adsafe_a,e);}else if(!option.evil&&(e.value==='eval'||e.value==='execScript')){warn(bundle.evil,e);}else if(option.safe&&(e.value.charAt(0)==='_'||e.value.charAt(0)==='-')){warn(bundle.adsafe_subscript_a,e);}
tally_member(e.value);if(!option.sub&&ix.test(e.value)){s=syntax[e.value];if(!s||!s.reserved){warn(bundle.subscript,e);}}}else if(e.arity!=='number'||e.value<0){if(option.safe){warn(bundle.adsafe_subscript_a,e);}}
step_out(']',that);discard();no_space(prevtoken,token);that.first=left;that.second=e;return that;},true);prefix('[',function(){this.arity='prefix';this.first=[];step_in('array');while(nexttoken.id!=='(end)'){while(nexttoken.id===','){warn(bundle.unexpected_a,nexttoken);advance(',');discard();}
if(nexttoken.id===']'){break;}
edge();this.first.push(expression(10));if(nexttoken.id===','){comma();if(nexttoken.id===']'&&!option.es5){warn(bundle.unexpected_a,token);break;}}else{break;}}
step_out(']',this);discard();return this;},170);function property_name(){var id=optional_identifier(true);if(!id){if(nexttoken.arity==='string'){id=nexttoken.value;if(option.safe){if(banned[id]){warn(bundle.adsafe_a);}else if(id.charAt(0)==='_'||id.charAt(id.length-1)==='_'){warn(bundle.dangling_a);}}
advance();}else if(nexttoken.arity==='number'){id=nexttoken.value.toString();advance();}}
return id;}
function function_params(){var id,paren=nexttoken,params=[];advance('(');step_in();discard();no_space();if(nexttoken.id===')'){no_space();step_out(')',paren);discard();return;}
for(;;){edge();id=identifier();params.push(token);add_label(id,'parameter');if(nexttoken.id===','){comma();}else{no_space();step_out(')',paren);discard();return params;}}}
function do_function(func,name){var old_properties=properties,old_option=option,old_global=global,old_scope=scope;funct={'(name)':name||'"'+anonname+'"','(line)':nexttoken.line,'(context)':funct,'(breakage)':0,'(loopage)':0,'(scope)':scope,'(token)':func};properties=old_properties&&Object.create(old_properties);option=Object.create(old_option);global=Object.create(old_global);scope=Object.create(old_scope);token.funct=funct;functions.push(funct);if(name){add_label(name,'function');}
func.name=name||'';func.first=funct['(params)']=function_params();one_space();func.block=block(false);funct=funct['(context)'];properties=old_properties;option=old_option;global=old_global;scope=old_scope;}
prefix('{',function(){var get,i,j,name,p,set,seen={},t;this.arity='prefix';this.first=[];step_in();while(nexttoken.id!=='}'){edge();if(nexttoken.value==='get'&&peek().id!==':'){if(!option.es5){warn(bundle.get_set);}
get=nexttoken;one_space_only();advance('get');name=nexttoken;i=property_name();if(!i){fail(bundle.missing_property);}
do_function(get,'');if(funct['(loopage)']){warn(bundle.function_loop,t);}
p=get.first;if(p){warn(bundle.parameter_a_get_b,t,p[0],i);}
comma();set=nexttoken;spaces();edge();advance('set');one_space_only();j=property_name();if(i!==j){fail(bundle.expected_a_b,token,i,j);}
do_function(set,'');p=set.first;if(!p||p.length!==1||p[0]!=='value'){warn(bundle.parameter_set_a,t,i);}
name.first=[get,set];}else{name=nexttoken;i=property_name();if(typeof i!=='string'){fail(bundle.missing_property);}
advance(':');discard();spaces();name.first=expression(10);}
this.first.push(name);if(seen[i]===true){warn(bundle.duplicate_a,nexttoken,i);}
seen[i]=true;tally_member(i);if(nexttoken.id!==','){break;}
for(;;){comma();if(nexttoken.id!==','){break;}
warn(bundle.unexpected_a,nexttoken);}
if(nexttoken.id==='}'&&!option.es5){warn(bundle.unexpected_a,token);}}
step_out('}',this);discard();return this;});stmt('{',function(){discard();warn(bundle.statement_block);this.arity='statement';this.block=statements();this.disrupt=this.block.disrupt;advance('}',this);discard();return this;});stmt('/*properties',directive);stmt('/*members',directive);stmt('/*jslint',directive);stmt('/*global',directive);stmt('var',function(){var assign,id,name;if(funct['(onevar)']&&option.onevar){warn(bundle.combine_var);}else if(!funct['(global)']){funct['(onevar)']=true;}
this.arity='statement';this.first=[];step_in('var');for(;;){name=nexttoken;id=identifier();if(funct['(global)']&&predefined[id]===false){warn(bundle.redefinition_a,token,id);}
add_label(id,'error');if(nexttoken.id==='='){assign=nexttoken;assign.first=name;spaces();advance('=');spaces();if(nexttoken.id==='undefined'){warn(bundle.unnecessary_initialize,token,id);}
if(peek(0).id==='='&&nexttoken.identifier){fail(bundle.var_a_not);}
assign.second=expression(0);assign.arity='infix';this.first.push(assign);}else{this.first.push(name);}
funct[id]='unused';if(nexttoken.id!==','){break;}
comma();if(var_mode&&nexttoken.line===token.line&&this.first.length===1){var_mode=false;indent.open=false;indent.at-=option.indent;}
spaces();edge();}
var_mode=false;step_out();return this;});stmt('function',function(){one_space();if(in_block){warn(bundle.function_block,token);}
var i=identifier();if(i){add_label(i,'unction');no_space();}
do_function(this,i,true);if(nexttoken.id==='('&&nexttoken.line===token.line){fail(bundle.function_statement);}
this.arity='statement';return this;});prefix('function',function(){one_space();var i=optional_identifier();if(i){no_space();}
do_function(this,i);if(funct['(loopage)']){warn(bundle.function_loop);}
this.arity='function';return this;});stmt('if',function(){var t=nexttoken;one_space();advance('(');step_in('control');discard();no_space();edge();this.arity='statement';this.first=expected_condition(expected_relation(expression(0)));no_space();step_out(')',t);discard();one_space();this.block=block(true);if(nexttoken.id==='else'){one_space();advance('else');discard();one_space();this['else']=nexttoken.id==='if'||nexttoken.id==='switch'?statement(true):block(true);if(this['else'].disrupt&&this.block.disrupt){this.disrupt=true;}}
return this;});stmt('try',function(){var b,e,s,t;if(option.adsafe){warn(bundle.adsafe_a,this);}
one_space();this.arity='statement';this.block=block(false);if(nexttoken.id==='catch'){one_space();advance('catch');discard();one_space();t=nexttoken;advance('(');step_in('control');discard();no_space();edge();s=scope;scope=Object.create(s);e=nexttoken.value;this.first=e;if(!nexttoken.identifier){warn(bundle.expected_identifier_a,nexttoken);}else{add_label(e,'exception');}
advance();no_space();step_out(')',t);discard();one_space();this.second=block(false);b=true;scope=s;}
if(nexttoken.id==='finally'){discard();one_space();t=nexttoken;advance('finally');discard();one_space();this.third=block(false);}else if(!b){fail(bundle.expected_a_b,nexttoken,'catch',nexttoken.value);}
return this;});labeled_stmt('while',function(){one_space();var t=nexttoken;funct['(breakage)']+=1;funct['(loopage)']+=1;advance('(');step_in('control');discard();no_space();edge();this.arity='statement';this.first=expected_relation(expression(0));if(this.first.id!=='true'){expected_condition(this.first,bundle.unexpected_a);}
no_space();step_out(')',t);discard();one_space();this.block=block(true);if(this.block.disrupt){warn(bundle.strange_loop,prevtoken);}
funct['(breakage)']-=1;funct['(loopage)']-=1;return this;});reserve('with');labeled_stmt('switch',function(){var particular,the_case=nexttoken,unbroken=true;funct['(breakage)']+=1;one_space();advance('(');discard();no_space();step_in();this.arity='statement';this.first=expected_condition(expected_relation(expression(0)));no_space();step_out(')',the_case);discard();one_space();advance('{');step_in();this.second=[];while(nexttoken.id==='case'){the_case=nexttoken;the_case.first=[];spaces();edge('case');advance('case');for(;;){one_space();particular=expression(0);the_case.first.push(particular);if(particular.id==='NaN'){warn(bundle.unexpected_a,particular);}
no_space_only();advance(':');discard();if(nexttoken.id!=='case'){break;}
spaces();edge('case');advance('case');discard();}
spaces();the_case.second=statements();if(the_case.second&&the_case.second.length>0){particular=the_case.second[the_case.second.length-1];if(particular.disrupt){if(particular.id==='break'){unbroken=false;}}else{warn(bundle.missing_a_after_b,nexttoken,'break','case');}}else{warn(bundle.empty_case);}
this.second.push(the_case);}
if(this.second.length===0){warn(bundle.missing_a,nexttoken,'case');}
if(nexttoken.id==='default'){spaces();the_case=nexttoken;edge('case');advance('default');discard();no_space_only();advance(':');discard();spaces();the_case.second=statements();if(the_case.second&&the_case.second.length>0){particular=the_case.second[the_case.second.length-1];if(unbroken&&particular.disrupt&&particular.id!=='break'){this.disrupt=true;}}
this.second.push(the_case);}
funct['(breakage)']-=1;spaces();step_out('}',this);return this;});stmt('debugger',function(){if(!option.debug){warn(bundle.unexpected_a,this);}
this.arity='statement';return this;});labeled_stmt('do',function(){funct['(breakage)']+=1;funct['(loopage)']+=1;one_space();this.arity='statement';this.block=block(true);if(this.block.disrupt){warn(bundle.strange_loop,prevtoken);}
one_space();advance('while');discard();var t=nexttoken;one_space();advance('(');step_in();discard();no_space();edge();this.first=expected_condition(expected_relation(expression(0)),bundle.unexpected_a);no_space();step_out(')',t);discard();funct['(breakage)']-=1;funct['(loopage)']-=1;return this;});labeled_stmt('for',function(){var blok,filter,ok=false,paren=nexttoken,the_in,value;this.arity='statement';funct['(breakage)']+=1;funct['(loopage)']+=1;advance('(');step_in('control');discard();spaces(this,paren);no_space();if(nexttoken.id==='var'){fail(bundle.move_var);}
edge();if(peek(0).id==='in'){value=nexttoken;switch(funct[value.value]){case'unused':funct[value.value]='var';break;case'var':break;default:warn(bundle.bad_in_a,value);}
advance();the_in=nexttoken;advance('in');the_in.first=value;the_in.second=expression(20);step_out(')',paren);discard();this.first=the_in;blok=block(true);if(!option.forin){if(blok.length===1&&typeof blok[0]==='object'&&blok[0].value==='if'&&!blok[0]['else']){filter=blok[0].first;while(filter.id==='&&'){filter=filter.first;}
switch(filter.id){case'===':case'!==':ok=filter.first.id==='['?(filter.first.first.value===the_in.second.value&&filter.first.second.value===the_in.first.value):(filter.first.id==='typeof'&&filter.first.first.id==='['&&filter.first.first.first.value===the_in.second.value&&filter.first.first.second.value===the_in.first.value);break;case'(':ok=filter.first.id==='.'&&((filter.first.first.value===the_in.second.value&&filter.first.second.value==='hasOwnProperty'&&filter.second[0].value===the_in.first.value)||(filter.first.first.value==='ADSAFE'&&filter.first.second.value==='has'&&filter.second[0].value===the_in.second.value&&filter.second[1].value===the_in.first.value)||(filter.first.first.id==='.'&&filter.first.first.first.id==='.'&&filter.first.first.first.first.value==='Object'&&filter.first.first.first.second.value==='prototype'&&filter.first.first.second.value==='hasOwnProperty'&&filter.first.second.value==='call'&&filter.second[0].value===the_in.second.value&&filter.second[1].value===the_in.first.value));break;}}
if(!ok){warn(bundle.for_if,this);}}}else{if(nexttoken.id!==';'){edge();this.first=[];for(;;){this.first.push(expression(0,'for'));if(nexttoken.id!==','){break;}
comma();}}
semicolon();if(nexttoken.id!==';'){edge();this.second=expected_relation(expression(0));if(this.second.id!=='true'){expected_condition(this.second,bundle.unexpected_a);}}
semicolon(token);if(nexttoken.id===';'){fail(bundle.expected_a_b,nexttoken,')',';');}
if(nexttoken.id!==')'){this.third=[];edge();for(;;){this.third.push(expression(0,'for'));if(nexttoken.id!==','){break;}
comma();}}
no_space();step_out(')',paren);discard();one_space();blok=block(true);}
if(blok.disrupt){warn(bundle.strange_loop,prevtoken);}
this.block=blok;funct['(breakage)']-=1;funct['(loopage)']-=1;return this;});disrupt_stmt('break',function(){var v=nexttoken.value;this.arity='statement';if(funct['(breakage)']===0){warn(bundle.unexpected_a,this);}
if(nexttoken.identifier&&token.line===nexttoken.line){one_space_only();if(funct[v]!=='label'){warn(bundle.not_a_label,nexttoken);}else if(scope[v]!==funct){warn(bundle.not_a_scope,nexttoken);}
this.first=nexttoken;advance();}
return this;});disrupt_stmt('continue',function(){if(!option['continue']){warn(bundle.unexpected_a,this);}
var v=nexttoken.value;this.arity='statement';if(funct['(breakage)']===0){warn(bundle.unexpected_a,this);}
if(nexttoken.identifier&&token.line===nexttoken.line){one_space_only();if(funct[v]!=='label'){warn(bundle.not_a_label,nexttoken);}else if(scope[v]!==funct){warn(bundle.not_a_scope,nexttoken);}
this.first=nexttoken;advance();}
return this;});disrupt_stmt('return',function(){this.arity='statement';if(nexttoken.id!==';'&&nexttoken.line===token.line){one_space_only();if(nexttoken.id==='/'||nexttoken.id==='(regexp)'){warn(bundle.wrap_regexp);}
this.first=expression(20);}
return this;});disrupt_stmt('throw',function(){this.arity='statement';one_space_only();this.first=expression(20);return this;});reserve('class');reserve('const');reserve('enum');reserve('export');reserve('extends');reserve('import');reserve('super');reserve('let');reserve('yield');reserve('implements');reserve('interface');reserve('package');reserve('private');reserve('protected');reserve('public');reserve('static');function json_value(){function json_object(){var o={},t=nexttoken;advance('{');if(nexttoken.id!=='}'){while(nexttoken.id!=='(end)'){while(nexttoken.id===','){warn(bundle.unexpected_a,nexttoken);comma();}
if(nexttoken.arity!=='string'){warn(bundle.expected_string_a);}
if(o[nexttoken.value]===true){warn(bundle.duplicate_a);}else if(nexttoken.value==='__proto__'){warn(bundle.dangling_a);}else{o[nexttoken.value]=true;}
advance();advance(':');json_value();if(nexttoken.id!==','){break;}
comma();if(nexttoken.id==='}'){warn(bundle.unexpected_a,token);break;}}}
advance('}',t);}
function json_array(){var t=nexttoken;advance('[');if(nexttoken.id!==']'){while(nexttoken.id!=='(end)'){while(nexttoken.id===','){warn(bundle.unexpected_a,nexttoken);comma();}
json_value();if(nexttoken.id!==','){break;}
comma();if(nexttoken.id===']'){warn(bundle.unexpected_a,token);break;}}}
advance(']',t);}
switch(nexttoken.id){case'{':json_object();break;case'[':json_array();break;case'true':case'false':case'null':case'(number)':case'(string)':advance();break;case'-':advance('-');no_space_only();advance('(number)');break;default:fail(bundle.unexpected_a);}}
function css_name(){if(nexttoken.identifier){advance();return true;}}
function css_number(){if(nexttoken.id==='-'){advance('-');no_space_only();}
if(nexttoken.arity==='number'){advance('(number)');return true;}}
function css_string(){if(nexttoken.arity==='string'){advance();return true;}}
function css_color(){var i,number,t,value;if(nexttoken.identifier){value=nexttoken.value;if(value==='rgb'||value==='rgba'){advance();t=nexttoken;advance('(');for(i=0;i<3;i+=1){if(i){comma();}
number=nexttoken.value;if(nexttoken.arity!=='number'||number<0){warn(bundle.expected_positive_a,nexttoken);advance();}else{advance();if(nexttoken.id==='%'){advance('%');if(number>100){warn(bundle.expected_percent_a,token,number);}}else{if(number>255){warn(bundle.expected_small_a,token,number);}}}}
if(value==='rgba'){comma();number=+nexttoken.value;if(nexttoken.arity!=='number'||number<0||number>1){warn(bundle.expected_fraction_a,nexttoken);}
advance();if(nexttoken.id==='%'){warn(bundle.unexpected_a);advance('%');}}
advance(')',t);return true;}else if(css_colorData[nexttoken.value]===true){advance();return true;}}else if(nexttoken.id==='(color)'){advance();return true;}
return false;}
function css_length(){if(nexttoken.id==='-'){advance('-');no_space_only();}
if(nexttoken.arity==='number'){advance();if(nexttoken.arity!=='string'&&css_lengthData[nexttoken.value]===true){no_space_only();advance();}else if(+token.value!==0){warn(bundle.expected_linear_a);}
return true;}
return false;}
function css_line_height(){if(nexttoken.id==='-'){advance('-');no_space_only();}
if(nexttoken.arity==='number'){advance();if(nexttoken.arity!=='string'&&css_lengthData[nexttoken.value]===true){no_space_only();advance();}
return true;}
return false;}
function css_width(){if(nexttoken.identifier){switch(nexttoken.value){case'thin':case'medium':case'thick':advance();return true;}}else{return css_length();}}
function css_margin(){if(nexttoken.identifier){if(nexttoken.value==='auto'){advance();return true;}}else{return css_length();}}
function css_attr(){if(nexttoken.identifier&&nexttoken.value==='attr'){advance();advance('(');if(!nexttoken.identifier){warn(bundle.expected_name_a);}
advance();advance(')');return true;}
return false;}
function css_comma_list(){while(nexttoken.id!==';'){if(!css_name()&&!css_string()){warn(bundle.expected_name_a);}
if(nexttoken.id!==','){return true;}
comma();}}
function css_counter(){if(nexttoken.identifier&&nexttoken.value==='counter'){advance();advance('(');advance();if(nexttoken.id===','){comma();if(nexttoken.arity!=='string'){warn(bundle.expected_string_a);}
advance();}
advance(')');return true;}
if(nexttoken.identifier&&nexttoken.value==='counters'){advance();advance('(');if(!nexttoken.identifier){warn(bundle.expected_name_a);}
advance();if(nexttoken.id===','){comma();if(nexttoken.arity!=='string'){warn(bundle.expected_string_a);}
advance();}
if(nexttoken.id===','){comma();if(nexttoken.arity!=='string'){warn(bundle.expected_string_a);}
advance();}
advance(')');return true;}
return false;}
function css_shape(){var i;if(nexttoken.identifier&&nexttoken.value==='rect'){advance();advance('(');for(i=0;i<4;i+=1){if(!css_length()){warn(bundle.expected_number_a);break;}}
advance(')');return true;}
return false;}
function css_url(){var c,url;if(nexttoken.identifier&&nexttoken.value==='url'){nexttoken=lex.range('(',')');url=nexttoken.value;c=url.charAt(0);if(c==='"'||c==='\''){if(url.slice(-1)!==c){warn(bundle.bad_url);}else{url=url.slice(1,-1);if(url.indexOf(c)>=0){warn(bundle.bad_url);}}}
if(!url){warn(bundle.missing_url);}
if(option.safe&&ux.test(url)){fail(bundle.adsafe_a,nexttoken,url);}
urls.push(url);advance();return true;}
return false;}
css_any=[css_url,function(){for(;;){if(nexttoken.identifier){switch(nexttoken.value.toLowerCase()){case'url':css_url();break;case'expression':warn(bundle.unexpected_a);advance();break;default:advance();}}else{if(nexttoken.id===';'||nexttoken.id==='!'||nexttoken.id==='(end)'||nexttoken.id==='}'){return true;}
advance();}}}];css_border_style=['none','dashed','dotted','double','groove','hidden','inset','outset','ridge','solid'];css_break=['auto','always','avoid','left','right'];css_media={'all':true,'braille':true,'embossed':true,'handheld':true,'print':true,'projection':true,'screen':true,'speech':true,'tty':true,'tv':true};css_overflow=['auto','hidden','scroll','visible'];css_attribute_data={background:[true,'background-attachment','background-color','background-image','background-position','background-repeat'],'background-attachment':['scroll','fixed'],'background-color':['transparent',css_color],'background-image':['none',css_url],'background-position':[2,[css_length,'top','bottom','left','right','center']],'background-repeat':['repeat','repeat-x','repeat-y','no-repeat'],'border':[true,'border-color','border-style','border-width'],'border-bottom':[true,'border-bottom-color','border-bottom-style','border-bottom-width'],'border-bottom-color':css_color,'border-bottom-style':css_border_style,'border-bottom-width':css_width,'border-collapse':['collapse','separate'],'border-color':['transparent',4,css_color],'border-left':[true,'border-left-color','border-left-style','border-left-width'],'border-left-color':css_color,'border-left-style':css_border_style,'border-left-width':css_width,'border-right':[true,'border-right-color','border-right-style','border-right-width'],'border-right-color':css_color,'border-right-style':css_border_style,'border-right-width':css_width,'border-spacing':[2,css_length],'border-style':[4,css_border_style],'border-top':[true,'border-top-color','border-top-style','border-top-width'],'border-top-color':css_color,'border-top-style':css_border_style,'border-top-width':css_width,'border-width':[4,css_width],bottom:[css_length,'auto'],'caption-side':['bottom','left','right','top'],clear:['both','left','none','right'],clip:[css_shape,'auto'],color:css_color,content:['open-quote','close-quote','no-open-quote','no-close-quote',css_string,css_url,css_counter,css_attr],'counter-increment':[css_name,'none'],'counter-reset':[css_name,'none'],cursor:[css_url,'auto','crosshair','default','e-resize','help','move','n-resize','ne-resize','nw-resize','pointer','s-resize','se-resize','sw-resize','w-resize','text','wait'],direction:['ltr','rtl'],display:['block','compact','inline','inline-block','inline-table','list-item','marker','none','run-in','table','table-caption','table-cell','table-column','table-column-group','table-footer-group','table-header-group','table-row','table-row-group'],'empty-cells':['show','hide'],'float':['left','none','right'],font:['caption','icon','menu','message-box','small-caption','status-bar',true,'font-size','font-style','font-weight','font-family'],'font-family':css_comma_list,'font-size':['xx-small','x-small','small','medium','large','x-large','xx-large','larger','smaller',css_length],'font-size-adjust':['none',css_number],'font-stretch':['normal','wider','narrower','ultra-condensed','extra-condensed','condensed','semi-condensed','semi-expanded','expanded','extra-expanded'],'font-style':['normal','italic','oblique'],'font-variant':['normal','small-caps'],'font-weight':['normal','bold','bolder','lighter',css_number],height:[css_length,'auto'],left:[css_length,'auto'],'letter-spacing':['normal',css_length],'line-height':['normal',css_line_height],'list-style':[true,'list-style-image','list-style-position','list-style-type'],'list-style-image':['none',css_url],'list-style-position':['inside','outside'],'list-style-type':['circle','disc','square','decimal','decimal-leading-zero','lower-roman','upper-roman','lower-greek','lower-alpha','lower-latin','upper-alpha','upper-latin','hebrew','katakana','hiragana-iroha','katakana-oroha','none'],margin:[4,css_margin],'margin-bottom':css_margin,'margin-left':css_margin,'margin-right':css_margin,'margin-top':css_margin,'marker-offset':[css_length,'auto'],'max-height':[css_length,'none'],'max-width':[css_length,'none'],'min-height':css_length,'min-width':css_length,opacity:css_number,outline:[true,'outline-color','outline-style','outline-width'],'outline-color':['invert',css_color],'outline-style':['dashed','dotted','double','groove','inset','none','outset','ridge','solid'],'outline-width':css_width,overflow:css_overflow,'overflow-x':css_overflow,'overflow-y':css_overflow,padding:[4,css_length],'padding-bottom':css_length,'padding-left':css_length,'padding-right':css_length,'padding-top':css_length,'page-break-after':css_break,'page-break-before':css_break,position:['absolute','fixed','relative','static'],quotes:[8,css_string],right:[css_length,'auto'],'table-layout':['auto','fixed'],'text-align':['center','justify','left','right'],'text-decoration':['none','underline','overline','line-through','blink'],'text-indent':css_length,'text-shadow':['none',4,[css_color,css_length]],'text-transform':['capitalize','uppercase','lowercase','none'],top:[css_length,'auto'],'unicode-bidi':['normal','embed','bidi-override'],'vertical-align':['baseline','bottom','sub','super','top','text-top','middle','text-bottom',css_length],visibility:['visible','hidden','collapse'],'white-space':['normal','nowrap','pre','pre-line','pre-wrap','inherit'],width:[css_length,'auto'],'word-spacing':['normal',css_length],'word-wrap':['break-word','normal'],'z-index':['auto',css_number]};function style_attribute(){var v;while(nexttoken.id==='*'||nexttoken.id==='#'||nexttoken.value==='_'){if(!option.css){warn(bundle.unexpected_a);}
advance();}
if(nexttoken.id==='-'){if(!option.css){warn(bundle.unexpected_a);}
advance('-');if(!nexttoken.identifier){warn(bundle.expected_nonstandard_style_attribute);}
advance();return css_any;}else{if(!nexttoken.identifier){warn(bundle.expected_style_attribute);}else{if(Object.prototype.hasOwnProperty.call(css_attribute_data,nexttoken.value)){v=css_attribute_data[nexttoken.value];}else{v=css_any;if(!option.css){warn(bundle.unrecognized_style_attribute_a);}}}
advance();return v;}}
function style_value(v){var i=0,n,once,match,round,start=0,vi;switch(typeof v){case'function':return v();case'string':if(nexttoken.identifier&&nexttoken.value===v){advance();return true;}
return false;}
for(;;){if(i>=v.length){return false;}
vi=v[i];i+=1;if(vi===true){break;}else if(typeof vi==='number'){n=vi;vi=v[i];i+=1;}else{n=1;}
match=false;while(n>0){if(style_value(vi)){match=true;n-=1;}else{break;}}
if(match){return true;}}
start=i;once=[];for(;;){round=false;for(i=start;i<v.length;i+=1){if(!once[i]){if(style_value(css_attribute_data[v[i]])){match=true;round=true;once[i]=true;break;}}}
if(!round){return match;}}}
function style_child(){if(nexttoken.arity==='number'){advance();if(nexttoken.value==='n'&&nexttoken.identifier){no_space_only();advance();if(nexttoken.id==='+'){no_space_only();advance('+');no_space_only();advance('(number)');}}
return;}else{if(nexttoken.identifier&&(nexttoken.value==='odd'||nexttoken.value==='even')){advance();return;}}
warn(bundle.unexpected_a);}
function substyle(){var v;for(;;){if(nexttoken.id==='}'||nexttoken.id==='(end)'||(xquote&&nexttoken.id===xquote)){return;}
while(nexttoken.id===';'){warn(bundle.unexpected_a);semicolon();}
v=style_attribute();advance(':');if(nexttoken.identifier&&nexttoken.value==='inherit'){advance();}else{if(!style_value(v)){warn(bundle.unexpected_a);advance();}}
if(nexttoken.id==='!'){advance('!');no_space_only();if(nexttoken.identifier&&nexttoken.value==='important'){advance();}else{warn(bundle.expected_a_b,nexttoken,'important',nexttoken.value);}}
if(nexttoken.id==='}'||nexttoken.id===xquote){warn(bundle.expected_a_b,nexttoken,';',nexttoken.value);}else{semicolon();}}}
function style_selector(){if(nexttoken.identifier){if(!Object.prototype.hasOwnProperty.call(html_tag,option.cap?nexttoken.value.toLowerCase():nexttoken.value)){warn(bundle.expected_tagname_a);}
advance();}else{switch(nexttoken.id){case'>':case'+':advance();style_selector();break;case':':advance(':');switch(nexttoken.value){case'active':case'after':case'before':case'checked':case'disabled':case'empty':case'enabled':case'first-child':case'first-letter':case'first-line':case'first-of-type':case'focus':case'hover':case'last-child':case'last-of-type':case'link':case'only-of-type':case'root':case'target':case'visited':advance();break;case'lang':advance();advance('(');if(!nexttoken.identifier){warn(bundle.expected_lang_a);}
advance(')');break;case'nth-child':case'nth-last-child':case'nth-last-of-type':case'nth-of-type':advance();advance('(');style_child();advance(')');break;case'not':advance();advance('(');if(nexttoken.id===':'&&peek(0).value==='not'){warn(bundle.not);}
style_selector();advance(')');break;default:warn(bundle.expected_pseudo_a);}
break;case'#':advance('#');if(!nexttoken.identifier){warn(bundle.expected_id_a);}
advance();break;case'*':advance('*');break;case'.':advance('.');if(!nexttoken.identifier){warn(bundle.expected_class_a);}
advance();break;case'[':advance('[');if(!nexttoken.identifier){warn(bundle.expected_attribute_a);}
advance();if(nexttoken.id==='='||nexttoken.value==='~='||nexttoken.value==='$='||nexttoken.value==='|='||nexttoken.id==='*='||nexttoken.id==='^='){advance();if(nexttoken.arity!=='string'){warn(bundle.expected_string_a);}
advance();}
advance(']');break;default:fail(bundle.expected_selector_a);}}}
function style_pattern(){if(nexttoken.id==='{'){warn(bundle.expected_style_pattern);}
for(;;){style_selector();if(nexttoken.id==='</'||nexttoken.id==='{'||nexttoken.id==='(end)'){return'';}
if(nexttoken.id===','){comma();}}}
function style_list(){while(nexttoken.id!=='</'&&nexttoken.id!=='(end)'){style_pattern();xmode='styleproperty';if(nexttoken.id===';'){semicolon();}else{advance('{');substyle();xmode='style';advance('}');}}}
function styles(){var i;while(nexttoken.id==='@'){i=peek();advance('@');if(nexttoken.identifier){switch(nexttoken.value){case'import':advance();if(!css_url()){warn(bundle.expected_a_b,nexttoken,'url',nexttoken.value);advance();}
semicolon();break;case'media':advance();for(;;){if(!nexttoken.identifier||css_media[nexttoken.value]===true){fail(bundle.expected_media_a);}
advance();if(nexttoken.id!==','){break;}
comma();}
advance('{');style_list();advance('}');break;default:warn(bundle.expected_at_a);}}else{warn(bundle.expected_at_a);}}
style_list();}
function do_begin(n){if(n!=='html'&&!option.fragment){if(n==='div'&&option.adsafe){fail(bundle.adsafe_fragment);}else{fail(bundle.expected_a_b,token,'html',n);}}
if(option.adsafe){if(n==='html'){fail(bundle.adsafe_html,token);}
if(option.fragment){if(n!=='div'){fail(bundle.adsafe_div,token);}}else{fail(bundle.adsafe_fragment,token);}}
option.browser=true;assume();}
function do_attribute(n,a,v){var u,x;if(a==='id'){u=typeof v==='string'?v.toUpperCase():'';if(ids[u]===true){warn(bundle.duplicate_a,nexttoken,v);}
if(!/^[A-Za-z][A-Za-z0-9._:\-]*$/.test(v)){warn(bundle.bad_id_a,nexttoken,v);}else if(option.adsafe){if(adsafe_id){if(v.slice(0,adsafe_id.length)!==adsafe_id){warn(bundle.adsafe_prefix_a,nexttoken,adsafe_id);}else if(!/^[A-Z]+_[A-Z]+$/.test(v)){warn(bundle.adsafe_bad_id);}}else{adsafe_id=v;if(!/^[A-Z]+_$/.test(v)){warn(bundle.adsafe_bad_id);}}}
x=v.search(dx);if(x>=0){warn(bundle.unexpected_char_a_b,token,v.charAt(x),a);}
ids[u]=true;}else if(a==='class'||a==='type'||a==='name'){x=v.search(qx);if(x>=0){warn(bundle.unexpected_char_a_b,token,v.charAt(x),a);}
ids[u]=true;}else if(a==='href'||a==='background'||a==='content'||a==='data'||a.indexOf('src')>=0||a.indexOf('url')>=0){if(option.safe&&ux.test(v)){fail(bundle.bad_url,nexttoken,v);}
urls.push(v);}else if(a==='for'){if(option.adsafe){if(adsafe_id){if(v.slice(0,adsafe_id.length)!==adsafe_id){warn(bundle.adsafe_prefix_a,nexttoken,adsafe_id);}else if(!/^[A-Z]+_[A-Z]+$/.test(v)){warn(bundle.adsafe_bad_id);}}else{warn(bundle.adsafe_bad_id);}}}else if(a==='name'){if(option.adsafe&&v.indexOf('_')>=0){warn(bundle.adsafe_name_a,nexttoken,v);}}}
function do_tag(n,a){var i,t=html_tag[n],script,x;src=false;if(!t){fail(bundle.unrecognized_tag_a,nexttoken,n===n.toLowerCase()?n:n+' (capitalization error)');}
if(stack.length>0){if(n==='html'){fail(bundle.unexpected_a,token,n);}
x=t.parent;if(x){if(x.indexOf(' '+stack[stack.length-1].name+' ')<0){fail(bundle.tag_a_in_b,token,n,x);}}else if(!option.adsafe&&!option.fragment){i=stack.length;do{if(i<=0){fail(bundle.tag_a_in_b,token,n,'body');}
i-=1;}while(stack[i].name!=='body');}}
switch(n){case'div':if(option.adsafe&&stack.length===1&&!adsafe_id){warn(bundle.adsafe_missing_id);}
break;case'script':xmode='script';advance('>');if(a.lang){warn(bundle.lang,token);}
if(option.adsafe&&stack.length!==1){warn(bundle.adsafe_placement,token);}
if(a.src){if(option.adsafe&&(!adsafe_may||!approved[a.src])){warn(bundle.adsafe_source,token);}
if(a.type){warn(bundle.type,token);}}else{step_in(nexttoken.from);edge();use_strict();adsafe_top=true;script=statements();if(option.adsafe){if(adsafe_went){fail(bundle.adsafe_script,token);}
if(script.length!==1||aint(script[0],'id','(')||aint(script[0].first,'id','.')||aint(script[0].first.first,'value','ADSAFE')||aint(script[0].second[0],'value',adsafe_id)){fail(bundle.adsafe_id_go);}
switch(script[0].first.second.value){case'id':if(adsafe_may||script[0].second.length!==1){fail(bundle.adsafe_id,nexttoken);}
adsafe_may=true;break;case'go':if(!adsafe_may){fail(bundle.adsafe_id);}
if(script[0].second.length!==2||aint(script[0].second[1],'id','function')||script[0].second[1].first.length!==2||aint(script[0].second[1].first[0],'value','dom')||aint(script[0].second[1].first[1],'value','lib')){fail(bundle.adsafe_go,nexttoken);}
adsafe_went=true;break;default:fail(bundle.adsafe_id_go);}}
indent=null;}
xmode='html';advance('</');if(!nexttoken.identifier&&nexttoken.value!=='script'){warn(bundle.expected_a_b,nexttoken,'script',nexttoken.value);}
advance();xmode='outer';break;case'style':xmode='style';advance('>');styles();xmode='html';advance('</');if(!nexttoken.identifier&&nexttoken.value!=='style'){warn(bundle.expected_a_b,nexttoken,'style',nexttoken.value);}
advance();xmode='outer';break;case'input':switch(a.type){case'radio':case'checkbox':case'button':case'reset':case'submit':break;case'text':case'file':case'password':case'file':case'hidden':case'image':if(option.adsafe&&a.autocomplete!=='off'){warn(bundle.adsafe_autocomplete);}
break;default:warn(bundle.bad_type);}
break;case'applet':case'body':case'embed':case'frame':case'frameset':case'head':case'iframe':case'noembed':case'noframes':case'object':case'param':if(option.adsafe){warn(bundle.adsafe_tag,nexttoken,n);}
break;}}
function closetag(n){return'</'+n+'>';}
function html(){var a,attributes,e,n,q,t,v,w=option.white,wmode;xmode='html';xquote='';stack=null;for(;;){switch(nexttoken.value){case'<':xmode='html';advance('<');attributes={};t=nexttoken;if(!t.identifier){warn(bundle.bad_name_a,t);}
n=t.value;if(option.cap){n=n.toLowerCase();}
t.name=n;advance();if(!stack){stack=[];do_begin(n);}
v=html_tag[n];if(typeof v!=='object'){fail(bundle.unrecognized_tag_a,t,n);}
e=v.empty;t.type=n;for(;;){if(nexttoken.id==='/'){advance('/');if(nexttoken.id!=='>'){warn(bundle.expected_a_b,nexttoken,'>',nexttoken.value);}
break;}
if(nexttoken.id&&nexttoken.id.substr(0,1)==='>'){break;}
if(!nexttoken.identifier){if(nexttoken.id==='(end)'||nexttoken.id==='(error)'){warn(bundle.expected_a_b,nexttoken,'>',nexttoken.value);}
warn(bundle.bad_name_a);}
option.white=true;spaces();a=nexttoken.value;option.white=w;advance();if(!option.cap&&a!==a.toLowerCase()){warn(bundle.attribute_case_a,token);}
a=a.toLowerCase();xquote='';if(Object.prototype.hasOwnProperty.call(attributes,a)){warn(bundle.duplicate_a,token,a);}
if(a.slice(0,2)==='on'){if(!option.on){warn(bundle.html_handlers);}
xmode='scriptstring';advance('=');q=nexttoken.id;if(q!=='"'&&q!=='\''){fail(bundle.expected_a_b,nexttoken,'"',nexttoken.value);}
xquote=q;wmode=option.white;option.white=false;advance(q);use_strict();statements();option.white=wmode;if(nexttoken.id!==q){fail(bundle.expected_a_b,nexttoken,q,nexttoken.value);}
xmode='html';xquote='';advance(q);v=false;}else if(a==='style'){xmode='scriptstring';advance('=');q=nexttoken.id;if(q!=='"'&&q!=='\''){fail(bundle.expected_a_b,nexttoken,'"',nexttoken.value);}
xmode='styleproperty';xquote=q;advance(q);substyle();xmode='html';xquote='';advance(q);v=false;}else{if(nexttoken.id==='='){advance('=');v=nexttoken.value;if(!nexttoken.identifier&&nexttoken.id!=='"'&&nexttoken.id!=='\''&&nexttoken.arity!=='string'&&nexttoken.arity!=='number'&&nexttoken.id!=='(color)'){warn(bundle.expected_attribute_value_a,token,a);}
advance();}else{v=true;}}
attributes[a]=v;do_attribute(n,a,v);}
do_tag(n,attributes);if(!e){stack.push(t);}
xmode='outer';advance('>');break;case'</':xmode='html';advance('</');if(!nexttoken.identifier){warn(bundle.bad_name_a);}
n=nexttoken.value;if(option.cap){n=n.toLowerCase();}
advance();if(!stack){fail(bundle.unexpected_a,nexttoken,closetag(n));}
t=stack.pop();if(!t){fail(bundle.unexpected_a,nexttoken,closetag(n));}
if(t.name!==n){fail(bundle.expected_a_b,nexttoken,closetag(t.name),closetag(n));}
if(nexttoken.id!=='>'){fail(bundle.expected_a_b,nexttoken,'>',nexttoken.value);}
xmode='outer';advance('>');break;case'<!':if(option.safe){warn(bundle.adsafe_a);}
xmode='html';for(;;){advance();if(nexttoken.id==='>'||nexttoken.id==='(end)'){break;}
if(nexttoken.value.indexOf('--')>=0){fail(bundle.unexpected_a,nexttoken,'--');}
if(nexttoken.value.indexOf('<')>=0){fail(bundle.unexpected_a,nexttoken,'<');}
if(nexttoken.value.indexOf('>')>=0){fail(bundle.unexpected_a,nexttoken,'>');}}
xmode='outer';advance('>');break;case'(end)':return;default:if(nexttoken.id==='(end)'){fail(bundle.missing_a,nexttoken,'</'+stack[stack.length-1].value+'>');}else{advance();}}
if(stack&&stack.length===0&&(option.adsafe||!option.fragment||nexttoken.id==='(end)')){break;}}
if(nexttoken.id!=='(end)'){fail(bundle.unexpected_a);}}
var itself=function(the_source,the_option){var i,keys,predef;JSLINT.errors=[];JSLINT.tree='';predefined=Object.create(standard);if(the_option){option=Object.create(the_option);predef=option.predef;if(predef){if(Array.isArray(predef)){for(i=0;i<predef.length;i+=1){predefined[predef[i]]=true;}}else if(typeof predef==='object'){keys=Object.keys(predef);for(i=0;i<keys.length;i+=1){predefined[keys[i]]=!!predef[keys];}}}
if(option.adsafe){option.safe=true;}
if(option.safe){option.browser=option['continue']=option.css=option.debug=option.devel=option.evil=option.forin=option.on=option.rhino=option.sub=option.widget=option.windows=false;option.nomen=option.strict=option.undef=true;predefined.Date=predefined['eval']=predefined.Function=predefined.Object=null;predefined.ADSAFE=predefined.lib=false;}}else{option={};}
option.indent=+option.indent||0;option.maxerr=option.maxerr||50;adsafe_id='';adsafe_may=adsafe_top=adsafe_went=false;approved={};if(option.approved){for(i=0;i<option.approved.length;i+=1){approved[option.approved[i]]=option.approved[i];}}else{approved.test='test';}
tab='';for(i=0;i<option.indent;i+=1){tab+=' ';}
global=Object.create(predefined);scope=global;funct={'(global)':true,'(name)':'(global)','(scope)':scope,'(breakage)':0,'(loopage)':0};functions=[funct];comments_off=false;ids={};implied={};in_block=false;indent=false;json_mode=false;lookahead=[];member={};properties=null;prereg=true;src=false;stack=null;strict_mode=false;urls=[];var_mode=false;warnings=0;xmode=false;lex.init(the_source);prevtoken=token=nexttoken=syntax['(begin)'];assume();try{advance();if(nexttoken.arity==='number'){fail(bundle.unexpected_a);}else if(nexttoken.value.charAt(0)==='<'){html();if(option.adsafe&&!adsafe_went){warn(bundle.adsafe_go,this);}}else{switch(nexttoken.id){case'{':case'[':json_mode=true;json_value();break;case'@':case'*':case'#':case'.':case':':xmode='style';advance();if(token.id!=='@'||!nexttoken.identifier||nexttoken.value!=='charset'||token.line!==1||token.from!==1){fail(bundle.css);}
advance();if(nexttoken.arity!=='string'&&nexttoken.value!=='UTF-8'){fail(bundle.css);}
advance();semicolon();styles();break;default:if(option.adsafe&&option.fragment){fail(bundle.expected_a_b,nexttoken,'<div>',nexttoken.value);}
step_in(1);if(nexttoken.id===';'){semicolon();}
if(nexttoken.value==='use strict'){warn(bundle.function_strict);use_strict();}
adsafe_top=true;JSLINT.tree=statements();if(option.adsafe&&(JSLINT.tree.length!==1||aint(JSLINT.tree[0],'id','(')||aint(JSLINT.tree[0].first,'id','.')||aint(JSLINT.tree[0].first.first,'value','ADSAFE')||aint(JSLINT.tree[0].first.second,'value','lib')||JSLINT.tree[0].second.length!==2||JSLINT.tree[0].second[0].arity!=='string'||aint(JSLINT.tree[0].second[1],'id','function'))){fail(bundle.adsafe_lib);}
if(JSLINT.tree.disrupt){warn(bundle.weird_program,prevtoken);}}}
indent=null;advance('(end)');}catch(e){if(e){JSLINT.errors.push({reason:e.message,line:e.line||nexttoken.line,character:e.character||nexttoken.from},null);}}
return JSLINT.errors.length===0;};itself.data=function(){var data={functions:[]},function_data,globals,i,implieds=[],j,kind,members=[],name,the_function,unused=[];if(itself.errors.length){data.errors=itself.errors;}
if(json_mode){data.json=true;}
for(name in implied){if(Object.prototype.hasOwnProperty.call(implied,name)){implieds.push({name:name,line:implied[name]});}}
if(implieds.length>0){data.implieds=implieds;}
if(urls.length>0){data.urls=urls;}
globals=Object.keys(functions[0]).filter(function(value){return value.charAt(0)!=='('?value:undefined;});if(globals.length>0){data.globals=globals;}
for(i=1;i<functions.length;i+=1){the_function=functions[i];function_data={};for(j=0;j<functionicity.length;j+=1){function_data[functionicity[j]]=[];}
for(name in the_function){if(Object.prototype.hasOwnProperty.call(the_function,name)){if(name.charAt(0)!=='('){kind=the_function[name];if(kind==='unction'){kind='unused';}else if(typeof kind==='boolean'){kind='global';}
if(Array.isArray(function_data[kind])){function_data[kind].push(name);if(kind==='unused'){unused.push({name:name,line:the_function['(line)'],'function':the_function['(name)']});}}}}}
for(j=0;j<functionicity.length;j+=1){if(function_data[functionicity[j]].length===0){delete function_data[functionicity[j]];}}
function_data.name=the_function['(name)'];function_data.param=the_function['(params)'];function_data.line=the_function['(line)'];data.functions.push(function_data);}
if(unused.length>0){data.unused=unused;}
members=[];for(name in member){if(typeof member[name]==='number'){data.member=member;break;}}
return data;};itself.report=function(errors_only){var data=itself.data();var err,evidence,i,j,key,keys,length,mem='',name,names,output=[],snippets,the_function,warning;function detail(h,array){var comma_needed,i,singularity;if(array){output.push('<div><i>'+h+'</i> ');array=array.sort();for(i=0;i<array.length;i+=1){if(array[i]!==singularity){singularity=array[i];output.push((comma_needed?', ':'')+singularity);comma_needed=true;}}
output.push('</div>');}}
if(data.errors||data.implieds||data.unused){err=true;output.push('<div id=errors><i>Error:</i>');if(data.errors){for(i=0;i<data.errors.length;i+=1){warning=data.errors[i];if(warning){evidence=warning.evidence||'';output.push('<p>Problem'+(isFinite(warning.line)?' at line '+
warning.line+' character '+warning.character:'')+': '+warning.reason.entityify()+'</p><p class=evidence>'+
(evidence&&(evidence.length>80?evidence.slice(0,77)+'...':evidence).entityify())+'</p>');}}}
if(data.implieds){snippets=[];for(i=0;i<data.implieds.length;i+=1){snippets[i]='<code>'+data.implieds[i].name+'</code>&nbsp;<i>'+
data.implieds[i].line+'</i>';}
output.push('<p><i>Implied global:</i> '+snippets.join(', ')+'</p>');}
if(data.unused){snippets=[];for(i=0;i<data.unused.length;i+=1){snippets[i]='<code><u>'+data.unused[i].name+'</u></code>&nbsp;<i>'+
data.unused[i].line+' </i> <small>'+
data.unused[i]['function']+'</small>';}
output.push('<p><i>Unused variable:</i> '+snippets.join(', ')+'</p>');}
if(data.json){output.push('<p>JSON: bad.</p>');}
output.push('</div>');}
if(!errors_only){output.push('<br><div id=functions>');if(data.urls){detail("URLs<br>",data.urls,'<br>');}
if(xmode==='style'){output.push('<p>CSS.</p>');}else if(data.json&&!err){output.push('<p>JSON: good.</p>');}else if(data.globals){output.push('<div><i>Global</i> '+
data.globals.sort().join(', ')+'</div>');}else{output.push('<div><i>No new global variables introduced.</i></div>');}
for(i=0;i<data.functions.length;i+=1){the_function=data.functions[i];names=[];if(the_function.param){for(j=0;j<the_function.param.length;j+=1){names[j]=the_function.param[j].value;}}
output.push('<br><div class=function><i>'+the_function.line+'</i> '+
(the_function.name||'')+'('+names.join(', ')+')</div>');detail('<big><b>Unused</b></big>',the_function.unused);detail('Closure',the_function.closure);detail('Variable',the_function['var']);detail('Exception',the_function.exception);detail('Outer',the_function.outer);detail('Global',the_function.global);detail('Label',the_function.label);}
if(data.member){keys=Object.keys(data.member);if(keys.length){keys=keys.sort();mem='<br><pre id=properties>/*properties ';length=13;for(i=0;i<keys.length;i+=1){key=keys[i];name=key.name();if(length+name.length>72){output.push(mem+'<br>');mem='    ';length=1;}
length+=name.length+2;if(data.member[key]===1){name='<i>'+name+'</i>';}
if(i<keys.length-1){name+=', ';}
mem+=name;}
output.push(mem+'<br>*/</pre>');}
output.push('</div>');}}
return output.join('');};itself.jslint=itself;itself.edition='2011-03-07';return itself;}());var ADSAFE=(function(){"use strict";var adsafe_id,adsafe_lib,banned={'arguments':true,callee:true,caller:true,constructor:true,'eval':true,prototype:true,stack:true,unwatch:true,valueOf:true,watch:true},cache_style_object,cache_style_node,defaultView=document.defaultView,ephemeral,flipflop,has_focus,hunter,interceptors=[],makeableTagName={a:true,abbr:true,acronym:true,address:true,area:true,b:true,bdo:true,big:true,blockquote:true,br:true,button:true,canvas:true,caption:true,center:true,cite:true,code:true,col:true,colgroup:true,dd:true,del:true,dfn:true,dir:true,div:true,dl:true,dt:true,em:true,fieldset:true,font:true,form:true,h1:true,h2:true,h3:true,h4:true,h5:true,h6:true,hr:true,i:true,img:true,input:true,ins:true,kbd:true,label:true,legend:true,li:true,map:true,menu:true,object:true,ol:true,optgroup:true,option:true,p:true,pre:true,q:true,samp:true,select:true,small:true,span:true,strong:true,sub:true,sup:true,table:true,tbody:true,td:true,textarea:true,tfoot:true,th:true,thead:true,tr:true,tt:true,u:true,ul:true,'var':true},name,pecker,result,star,the_range,value;function error(message){ADSAFE.log("ADsafe error: "+(message||"ADsafe violation."));throw{name:"ADsafe",message:message||"ADsafe violation."};}
function string_check(string){if(typeof string!=='string'){error("ADsafe string violation.");}
return string;}
function owns(object,string){return object&&typeof object==='object'&&Object.prototype.hasOwnProperty.call(object,string_check(string));}
(function mozilla(name){var method=Array.prototype[name];Array.prototype[name]=function(){return!this||this.window?error():method.apply(this,arguments);};return mozilla;}
('concat')
('every')
('filter')
('forEach')
('map')
('reduce')
('reduceRight')
('reverse')
('slice')
('some')
('sort'));function reject_name(name){return banned[name]||((typeof name!=='number'||name<0)&&(typeof name!=='string'||name.charAt(0)==='_'||name.slice(-1)==='_'||name.charAt(0)==='-'));}
function reject_property(object,name){return typeof object!=='object'||reject_name(name);}
function reject_global(that){if(that.window){error();}}
function getStyleObject(node){if(node===cache_style_node){return cache_style_object;}
cache_style_node=node;cache_style_object=node.currentStyle||defaultView.getComputedStyle(node,'');return cache_style_object;}
function walkTheDOM(node,func,skip){if(!skip){func(node);}
node=node.firstChild;while(node){walkTheDOM(node,func);node=node.nextSibling;}}
function purge_event_handlers(node){walkTheDOM(node,function(node){if(node.tagName){node['___ on ___']=node.change=null;}});}
function parse_query(text,id){var match,query=[],selector,qx=id?/^\s*(?:([\*\/])|\[\s*([a-z][0-9a-z_\-]*)\s*(?:([!*~|$\^]?\=)\s*([0-9A-Za-z_\-*%&;.\/:!]+)\s*)?\]|#\s*([A-Z]+_[A-Z0-9]+)|:\s*([a-z]+)|([.&_>\+]?)\s*([a-z][0-9a-z\-]*))\s*/:/^\s*(?:([\*\/])|\[\s*([a-z][0-9a-z_\-]*)\s*(?:([!*~|$\^]?\=)\s*([0-9A-Za-z_\-*%&;.\/:!]+)\s*)?\]|#\s*([\-A-Za-z0-9_]+)|:\s*([a-z]+)|([.&_>\+]?)\s*([a-z][0-9a-z\-]*))\s*/;do{match=qx.exec(string_check(text));if(!match){error("ADsafe: Bad query:"+text);}
if(match[1]){selector={op:match[1]};}else if(match[2]){selector=match[3]?{op:'['+match[3],name:match[2],value:match[4]}:{op:'[',name:match[2]};}else if(match[5]){if(query.length>0||match[5].length<=id.length||match[5].slice(0,id.length)!==id){error("ADsafe: Bad query: "+text);}
selector={op:'#',name:match[5]};}else if(match[6]){selector={op:':'+match[6]};}else{selector={op:match[7],name:match[8]};}
query.push(selector);text=text.slice(match[0].length);}while(text);return query;}
hunter={'':function(node){var e=node.getElementsByTagName(name),i;for(i=0;i<1000;i+=1){if(e[i]){result.push(e[i]);}else{break;}}},'+':function(node){node=node.nextSibling;name=name.toUpperCase();while(node&&!node.tagName){node=node.nextSibling;}
if(node&&node.tagName===name){result.push(node);}},'>':function(node){node=node.firstChild;name=name.toUpperCase();while(node){if(node.tagName===name){result.push(node);}
node=node.nextSibling;}},'#':function(node){var n=document.getElementById(name);if(n.tagName){result.push(n);}},'/':function(node){var e=node.childNodes,i;for(i=0;i<e.length;i+=1){result.push(e[i]);}},'*':function(node){star=true;walkTheDOM(node,function(node){result.push(node);},true);}};pecker={'.':function(node){return(' '+node.className+' ').indexOf(' '+name+' ')>=0;},'&':function(node){return node.name===name;},'_':function(node){return node.type===name;},'[':function(node){return typeof node[name]==='string';},'[=':function(node){var member=node[name];return typeof member==='string'&&member===value;},'[!=':function(node){var member=node[name];return typeof member==='string'&&member!==value;},'[^=':function(node){var member=node[name];return typeof member==='string'&&member.slice(0,member.length)===value;},'[$=':function(node){var member=node[name];return typeof member==='string'&&member.slice(-member.length)===value;},'[*=':function(node){var member=node[name];return typeof member==='string'&&member.indexOf(value)>=0;},'[~=':function(node){var member=node[name];return typeof member==='string'&&(' '+member+' ').indexOf(' '+value+' ')>=0;},'[|=':function(node){var member=node[name];return typeof member==='string'&&('-'+member+'-').indexOf('-'+value+'-')>=0;},':blur':function(node){return node!==has_focus;},':checked':function(node){return node.checked;},':disabled':function(node){return node.tagName&&node.disabled;},':enabled':function(node){return node.tagName&&!node.disabled;},':even':function(node){var f;if(node.tagName){f=flipflop;flipflop=!flipflop;return f;}else{return false;}},':focus':function(node){return node===has_focus;},':hidden':function(node){return node.tagName&&getStyleObject(node).visibility!=='visible';},':odd':function(node){if(node.tagName){flipflop=!flipflop;return flipflop;}else{return false;}},':tag':function(node){return node.tagName;},':text':function(node){return node.nodeName==='#text';},':trim':function(node){return node.nodeName!=='#text'||/\W/.test(node.nodeValue);},':unchecked':function(node){return node.tagName&&!node.checked;},':visible':function(node){return node.tagName&&getStyleObject(node).visibility==='visible';}};function quest(query,nodes){var selector,func,i,j;for(i=0;i<query.length;i+=1){selector=query[i];name=selector.name;func=hunter[selector.op];if(typeof func==='function'){if(star){error("ADsafe: Query violation: *"+selector.op+
(selector.name||''));}
result=[];for(j=0;j<nodes.length;j+=1){func(nodes[j]);}}else{value=selector.value;flipflop=false;func=pecker[selector.op];if(typeof func!=='function'){switch(selector.op){case':first':result=nodes.slice(0,1);break;case':rest':result=nodes.slice(1);break;default:error('ADsafe: Query violation: :'+selector.op);}}else{result=[];for(j=0;j<nodes.length;j+=1){if(func(nodes[j])){result.push(nodes[j]);}}}}
nodes=result;}
return result;}
function make_root(root,id){if(id){if(root.tagName!=='DIV'){error('ADsafe: Bad node.');}}else{if(root.tagName!=='BODY'){error('ADsafe: Bad node.');}}
function Bunch(nodes){this.___nodes___=nodes;this.___star___=star&&nodes.length>1;star=false;}
var allow_focus=true,dom,dom_event=function(e){var key,target,that,the_event,the_target,the_actual_event=e||event,type=the_actual_event.type;the_target=the_actual_event.target||the_actual_event.srcElement;target=new Bunch([the_target]);that=target;switch(type){case'mousedown':allow_focus=true;if(document.selection){the_range=document.selection.createRange();}
break;case'focus':case'focusin':allow_focus=true;has_focus=the_target;the_actual_event.cancelBubble=false;type='focus';break;case'blur':case'focusout':allow_focus=false;has_focus=null;type='blur';break;case'keypress':allow_focus=true;has_focus=the_target;key=String.fromCharCode(the_actual_event.charCode||the_actual_event.keyCode);switch(key){case'\u000d':case'\u000a':type='enterkey';break;case'\u001b':type='escapekey';break;}
break;case'click':allow_focus=true;break;}
if(the_actual_event.cancelBubble&&the_actual_event.stopPropagation){the_actual_event.stopPropagation();}
the_event={altKey:the_actual_event.altKey,ctrlKey:the_actual_event.ctrlKey,bubble:function(){try{var parent=that.getParent(),b=parent.___nodes___[0];that=parent;the_event.that=that;if(b['___ on ___']&&b['___ on ___'][type]){that.fire(the_event);}else{the_event.bubble();}}catch(e){error(e);}},key:key,preventDefault:function(){if(the_actual_event.preventDefault){the_actual_event.preventDefault();}
the_actual_event.returnValue=false;},shiftKey:the_actual_event.shiftKey,target:target,that:that,type:type,x:the_actual_event.clientX,y:the_actual_event.clientY};if(the_target['___ on ___']&&the_target['___ on ___'][the_event.type]){target.fire(the_event);}else{for(;;){the_target=the_target.parentNode;if(!the_target){break;}
if(the_target['___ on ___']&&the_target['___ on ___'][the_event.type]){that=new Bunch([the_target]);the_event.that=that;that.fire(the_event);break;}
if(the_target['___adsafe root___']){break;}}}
if(the_event.type==='escapekey'){if(ephemeral){ephemeral.remove();}
ephemeral=null;}
that=the_target=the_event=the_actual_event=null;return;};root['___adsafe root___']='___adsafe root___';Bunch.prototype={append:function(appendage){reject_global(this);var b=this.___nodes___,flag=false,i,j,node,rep;if(b.length===0||!appendage){return this;}
if(appendage instanceof Array){if(appendage.length!==b.length){error('ADsafe: Array length: '+b.length+'-'+
value.length);}
for(i=0;i<b.length;i+=1){rep=appendage[i].___nodes___;for(j=0;j<rep.length;j+=1){b[i].appendChild(rep[j]);}}}else{rep=appendage.___nodes___;for(i=0;i<b.length;i+=1){node=b[i];for(j=0;j<rep.length;j+=1){node.appendChild(flag?rep[j].cloneNode(true):rep[j]);}
flag=true;}}
return this;},blur:function(){reject_global(this);var b=this.___nodes___,i,node;has_focus=null;for(i=0;i<b.length;i+=1){node=b[i];if(node.blur){node.blur();}}
return this;},check:function(value){reject_global(this);var b=this.___nodes___,i,node;if(value instanceof Array){if(value.length!==b.length){error('ADsafe: Array length: '+b.length+'-'+
value.length);}
for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node.checked=!!value[i];}}}else{for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node.checked=!!value;}}}
return this;},'class':function(value){reject_global(this);var b=this.___nodes___,i,node;if(value instanceof Array){if(value.length!==b.length){error('ADsafe: Array length: '+b.length+'-'+
value.length);}
for(i=0;i<b.length;i+=1){if(/url/i.test(string_check(value[i]))){error('ADsafe error.');}
node=b[i];if(node.tagName){node.className=value[i];}}}else{if(/url/i.test(string_check(value))){error('ADsafe error.');}
for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node.className=value;}}}
return this;},count:function(){reject_global(this);return this.___nodes___.length;},each:function(func){reject_global(this);var b=this.___nodes___,i;if(typeof func==='function'){for(i=0;i<b.length;i+=1){func(new Bunch([b[i]]));}
return this;}
error();},empty:function(){reject_global(this);var b=this.___nodes___,i,node;if(value instanceof Array){if(value.length!==b.length){error('ADsafe: Array length: '+b.length+'-'+
value.length);}
for(i=0;i<b.length;i+=1){node=b[i];while(node.firstChild){purge_event_handlers(node);node.removeChild(node.firstChild);}}}else{for(i=0;i<b.length;i+=1){node=b[i];while(node.firstChild){purge_event_handlers(node);node.removeChild(node.firstChild);}}}
return this;},enable:function(enable){reject_global(this);var b=this.___nodes___,i,node;if(enable instanceof Array){if(enable.length!==b.length){error('ADsafe: Array length: '+b.length+'-'+
enable.length);}
for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node.disabled=!enable[i];}}}else{for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node.disabled=!enable;}}}
return this;},ephemeral:function(){reject_global(this);if(ephemeral){ephemeral.remove();}
ephemeral=this;return this;},explode:function(){reject_global(this);var a=[],b=this.___nodes___,i;for(i=0;i<b.length;i+=1){a[i]=new Bunch([b[i]]);}
return a;},fire:function(event){reject_global(this);var array,b,i,j,n,node,on,type;if(typeof event==='string'){type=event;event={type:type};}else if(typeof event==='object'){type=event.type;}else{error();}
b=this.___nodes___;n=b.length;for(i=0;i<n;i+=1){node=b[i];on=node['___ on ___'];if(owns(on,type)){array=on[type];for(j=0;j<array.length;j+=1){array[j].call(this,event);}}}
return this;},focus:function(){reject_global(this);var b=this.___nodes___;if(b.length===1&&allow_focus){has_focus=b[0].focus();return this;}
error();},fragment:function(){reject_global(this);return new Bunch([document.createDocumentFragment()]);},getCheck:function(){return this.getChecks()[0];},getChecks:function(){reject_global(this);var a=[],b=this.___nodes___,i;for(i=0;i<b.length;i+=1){a[i]=b[i].checked;}
return a;},getClass:function(){return this.getClasses()[0];},getClasses:function(){reject_global(this);var a=[],b=this.___nodes___,i;for(i=0;i<b.length;i+=1){a[i]=b[i].className;}
return a;},getMark:function(){return this.getMarks()[0];},getMarks:function(){reject_global(this);var a=[],b=this.___nodes___,i;for(i=0;i<b.length;i+=1){a[i]=b[i]['_adsafe mark_'];}
return a;},getName:function(){return this.getNames()[0];},getNames:function(){reject_global(this);var a=[],b=this.___nodes___,i;for(i=0;i<b.length;i+=1){a[i]=b[i].name;}
return a;},getOffsetHeight:function(){return this.getOffsetHeights()[0];},getOffsetHeights:function(){reject_global(this);var a=[],b=this.___nodes___,i;for(i=0;i<b.length;i+=1){a[i]=b[i].offsetHeight;}
return a;},getOffsetWidth:function(){return this.getOffsetWidths()[0];},getOffsetWidths:function(){reject_global(this);var a=[],b=this.___nodes___,i;for(i=0;i<b.length;i+=1){a[i]=b[i].offsetWidth;}
return a;},getParent:function(){reject_global(this);var a=[],b=this.___nodes___,i,n;for(i=0;i<b.length;i+=1){n=b[i].parentNode;if(n['___adsafe root___']){error('ADsafe parent violation.');}
a[i]=n;}
return new Bunch(a);},getSelection:function(){reject_global(this);var b=this.___nodes___,end,node,start,range;if(b.length===1&&allow_focus){node=b[0];if(typeof node.selectionStart==='number'){start=node.selectionStart;end=node.selectionEnd;return node.value.slice(start,end);}else{range=node.createTextRange();range.expand('textedit');if(range.inRange(the_range)){return the_range.text;}}}
return null;},getStyle:function(name){return this.getStyles(name)[0];},getStyles:function(name){reject_global(this);if(reject_name(name)){error("ADsafe style violation.");}
var a=[],b=this.___nodes___,i,node,s;for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){s=name!=='float'?getStyleObject(node)[name]:getStyleObject(node).cssFloat||getStyleObject(node).styleFloat;if(typeof s==='string'){a[i]=s;}}}
return a;},getTagName:function(){return this.getTagNames()[0];},getTagNames:function(){reject_global(this);var a=[],b=this.___nodes___,i,name;for(i=0;i<b.length;i+=1){name=b[i].tagName;a[i]=typeof name==='string'?name.toLowerCase():name;}
return a;},getTitle:function(){return this.getTitles()[0];},getTitles:function(){reject_global(this);var a=[],b=this.___nodes___,i;for(i=0;i<b.length;i+=1){a[i]=b[i].title;}
return a;},getValue:function(){return this.getValues()[0];},getValues:function(){reject_global(this);var a=[],b=this.___nodes___,i,node;for(i=0;i<b.length;i+=1){node=b[i];if(node.nodeName==='#text'){a[i]=node.nodeValue;}else if(node.tagName&&node.type!=='password'){a[i]=node.value;if(a[i]===undefined&&node.firstChild&&node.firstChild.nodeName==='#text'){a[i]=node.firstChild.nodeValue;}}}
return a;},klass:function(value){return this['class'](value);},mark:function(value){reject_global(this);var b=this.___nodes___,i,node;if(value instanceof Array){if(value.length!==b.length){error('ADsafe: Array length: '+b.length+'-'+
value.length);}
for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node['_adsafe mark_']=String(value[i]);}}}else{for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node['_adsafe mark_']=String(value);}}}
return this;},off:function(type){reject_global(this);var b=this.___nodes___,i,node;for(i=0;i<b.length;i+=1){node=b[i];if(typeof type==='string'){if(typeof node['___ on ___']){node['___ on ___'][type]=null;}}else{node['___ on ___']=null;}}
return this;},on:function(type,func){reject_global(this);if(typeof type!=='string'||typeof func!=='function'){error();}
var b=this.___nodes___,i,node,on,ontype;for(i=0;i<b.length;i+=1){node=b[i];if(type==='change'){ontype='on'+type;if(node[ontype]!==dom_event){node[ontype]=dom_event;}}
on=node['___ on ___'];if(!on){on={};node['___ on ___']=on;}
if(owns(on,type)){on[type].push(func);}else{on[type]=[func];}}
return this;},protect:function(){reject_global(this);var b=this.___nodes___,i;for(i=0;i<b.length;i+=1){b[i]['___adsafe root___']='___adsafe root___';}
return this;},q:function(text){reject_global(this);star=this.___star___;return new Bunch(quest(parse_query(string_check(text),id),this.___nodes___));},remove:function(){reject_global(this);this.replace();},replace:function(replacement){reject_global(this);var b=this.___nodes___,flag=false,i,j,newnode,node,parent,rep;if(b.length===0){return;}
for(i=0;i<b.length;i+=1){purge_event_handlers(b[i]);}
if(!replacement||replacement.length===0||(replacement.___nodes___&&replacement.___nodes___.length===0)){for(i=0;i<b.length;i+=1){node=b[i];purge_event_handlers(node);if(node.parentNode){node.parentNode.removeChild(node);}}}else if(replacement instanceof Array){if(replacement.length!==b.length){error('ADsafe: Array length: '+
b.length+'-'+value.length);}
for(i=0;i<b.length;i+=1){node=b[i];parent=node.parentNode;purge_event_handlers(node);if(parent){rep=replacement[i].___nodes___;if(rep.length>0){newnode=rep[0];parent.replaceChild(newnode,node);for(j=1;j<rep.length;j+=1){node=newnode;newnode=rep[j];parent.insertBefore(newnode,node.nextSibling);}}else{parent.removeChild(node);}}}}else{rep=replacement.___nodes___;for(i=0;i<b.length;i+=1){node=b[i];purge_event_handlers(node);parent=node.parentNode;if(parent){newnode=flag?rep[0].cloneNode(true):rep[0];parent.replaceChild(newnode,node);for(j=1;j<rep.length;j+=1){node=newnode;newnode=flag?rep[j].clone(true):rep[j];parent.insertBefore(newnode,node.nextSibling);}
flag=true;}}}
return this;},select:function(){reject_global(this);var b=this.___nodes___;if(b.length!==1||!allow_focus){error();}
b[0].focus();b[0].select();return this;},selection:function(string){reject_global(this);string_check(string);var b=this.___nodes___,end,node,old,start,range;if(b.length===1&&allow_focus){node=b[0];if(typeof node.selectionStart==='number'){start=node.selectionStart;end=node.selectionEnd;old=node.value;node.value=old.slice(0,start)+string+old.slice(end);node.selectionStart=node.selectionEnd=start+
string.length;node.focus();}else{range=node.createTextRange();range.expand('textedit');if(range.inRange(the_range)){the_range.select();the_range.text=string;the_range.select();}}}
return this;},style:function(name,value){reject_global(this);if(reject_name(name)){error("ADsafe style violation.");}
if(value===undefined||/url/i.test(string_check(value))){error();}
var b=this.___nodes___,i,node,v;if(value instanceof Array){if(value.length!==b.length){error('ADsafe: Array length: '+
b.length+'-'+value.length);}
for(i=0;i<b.length;i+=1){node=b[i];v=string_check(value[i]);if(/url/i.test(v)){error();}
if(node.tagName){if(name!=='float'){node.style[name]=v;}else{node.style.cssFloat=node.style.styleFloat=v;}}}}else{v=string_check(value);if(/url/i.test(v)){error();}
for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){if(name!=='float'){node.style[name]=v;}else{node.style.cssFloat=node.style.styleFloat=v;}}}}
return this;},tag:function(tag,type,name){reject_global(this);var node;if(typeof tag!=='string'){error();}
if(makeableTagName[tag]!==true){error('ADsafe: Bad tag: '+tag);}
node=document.createElement(tag);if(name){node.autocomplete='off';node.name=string_check(name);}
if(type){node.type=string_check(type);}
return new Bunch([node]);},text:function(text){reject_global(this);var a,i;if(text instanceof Array){a=[];for(i=0;i<text.length;i+=1){a[i]=document.createTextNode(string_check(text[i]));}
return new Bunch(a);}
return new Bunch([document.createTextNode(string_check(text))]);},title:function(value){reject_global(this);var b=this.___nodes___,i,node;if(value instanceof Array){if(value.length!==b.length){error('ADsafe: Array length: '+b.length+'-'+value.length);}
for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node.title=string_check(value[i]);}}}else{string_check(value);for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){node.title=value;}}}
return this;},value:function(value){reject_global(this);if(value===undefined){error();}
var b=this.___nodes___,i,node;if(value instanceof Array&&b.length===value.length){for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){if(node.type!=='password'){if(typeof node.value==='string'){node.value=value[i];}else{while(node.firstChild){purge_event_handlers(node);node.removeChild(node.firstChild);}
node.appendChild(document.createTextNode(String(value[i])));}}}else if(node.nodeName==='#text'){node.nodeValue=String(value[i]);}}}else{value=String(value);for(i=0;i<b.length;i+=1){node=b[i];if(node.tagName){if(typeof node.value==='string'){node.value=value;}else{while(node.firstChild){purge_event_handlers(node);node.removeChild(node.firstChild);}
node.appendChild(document.createTextNode(value));}}else if(node.nodeName==='#text'){node.nodeValue=value;}}}
return this;}};dom={append:function(bunch){var b=bunch.___nodes___,i,n;for(i=0;i<b.length;i+=1){n=b[i];if(typeof n==='string'||typeof n==='number'){n=document.createTextNode(String(n));}
root.appendChild(n);}
return dom;},combine:function(array){if(!array||!array.length){error('ADsafe: Bad combination.');}
var b=array[0].___nodes___,i;for(i=0;i<array.length;i+=1){b=b.concat(array[i].___nodes___);}
return new Bunch(b);},count:function(){return 1;},ephemeral:function(bunch){if(ephemeral){ephemeral.remove();}
ephemeral=bunch;return dom;},fragment:function(){return new Bunch([document.createDocumentFragment()]);},prepend:function(bunch){var b=bunch.___nodes___,i;for(i=0;i<b.length;i+=1){root.insertBefore(b[i],root.firstChild);}
return dom;},q:function(text){star=false;var query=parse_query(text,id);if(typeof hunter[query[0].op]!=='function'){error('ADsafe: Bad query: '+query[0]);}
return new Bunch(quest(query,[root]));},remove:function(){purge_event_handlers(root);root.parent.removeElement(root);root=null;},row:function(values){var tr=document.createElement('tr'),td,i;for(i=0;i<values.length;i+=1){td=document.createElement('td');td.appendChild(document.createTextNode(String(values[i])));tr.appendChild(td);}
return new Bunch([tr]);},tag:function(tag,type,name){var node;if(typeof tag!=='string'){error();}
if(makeableTagName[tag]!==true){error('ADsafe: Bad tag: '+tag);}
node=document.createElement(tag);if(name){node.autocomplete='off';node.name=name;}
if(type){node.type=type;}
return new Bunch([node]);},text:function(text){var a,i;if(text instanceof Array){a=[];for(i=0;i<text.length;i+=1){a[i]=document.createTextNode(string_check(text[i]));}
return new Bunch(a);}
return new Bunch([document.createTextNode(string_check(text))]);}};if(typeof root.addEventListener==='function'){root.addEventListener('focus',dom_event,true);root.addEventListener('blur',dom_event,true);root.addEventListener('mouseover',dom_event,true);root.addEventListener('mouseout',dom_event,true);root.addEventListener('mouseup',dom_event,true);root.addEventListener('mousedown',dom_event,true);root.addEventListener('mousemove',dom_event,true);root.addEventListener('click',dom_event,true);root.addEventListener('dblclick',dom_event,true);root.addEventListener('keypress',dom_event,true);}else{root.onfocusin=root.onfocusout=root.onmouseout=root.onmousedown=root.onmousemove=root.onmouseup=root.onmouseover=root.onclick=root.ondblclick=root.onkeypress=dom_event;}
return[dom,Bunch.prototype];}
function F(){}
return{create:typeof Object.create==='function'?Object.create:function(o){F.prototype=typeof o==='object'&&o?o:Object.prototype;return new F();},get:function(object,name){reject_global(object);if(arguments.length===2&&!reject_property(object,name)){return object[name];}
error();},go:function(id,f){var dom,fun,root,i,scripts;if(adsafe_id&&adsafe_id!==id){error();}
root=document.getElementById(id);if(root.tagName!=='DIV'){error();}
adsafe_id=null;scripts=root.getElementsByTagName('script');i=scripts.length-1;if(i<0){error();}
do{root.removeChild(scripts[i]);i-=1;}while(i>=0);root=make_root(root,id);dom=root[0];for(i=0;i<interceptors.length;i+=1){fun=interceptors[i];if(typeof fun==='function'){try{fun(id,dom,adsafe_lib,root[1]);}catch(e1){ADSAFE.log(e1);}}}
try{f(dom,adsafe_lib);}catch(e2){ADSAFE.log(e2);}
root=null;adsafe_lib=null;},has:function(object,name){return owns(object,name);},id:function(id){if(adsafe_id){error();}
adsafe_id=id;adsafe_lib={};},isArray:Array.isArray||function(value){return Object.prototype.toString.apply(value)==='[object Array]';},later:function(func,timeout){if(typeof func==='function'){setTimeout(func,timeout||0);}else{error();}},lib:function(name,f){if(!adsafe_id||reject_name(name)){error("ADsafe lib violation.");}
adsafe_lib[name]=f(adsafe_lib);},log:function log(s){if(window.console){console.log(s);}else if(typeof Debug==='object'){Debug.writeln(s);}else if(typeof opera==='opera'){opera.postError(s);}},remove:function(object,name){if(arguments.length===2&&!reject_property(object,name)){delete object[name];return;}
error();},set:function(object,name,value){reject_global(object);if(arguments.length===3&&!reject_property(object,name)){object[name]=value;return;}
error();},_intercept:function(f){interceptors.push(f);}};}());ADSAFE._intercept(function(id,dom,lib,bunch){"use strict";lib.cookie={get:function(){var c=' '+document.cookie+';',s=c.indexOf((' '+id+'=')),v;try{if(s>=0){s+=id.length+2;v=JSON.parse(c.slice(s,c.indexOf(';',s)));}}catch(ignore){}
return v;},set:function(value){var d,j=JSON.stringify(value).replace(/[=]/g,'\\u003d').replace(/[;]/g,'\\u003b');if(j.length<2000){d=new Date();d.setTime(d.getTime()+1e9);document.cookie=id+"="+j+';expires='+d.toGMTString();}}};});ADSAFE._intercept(function(id,dom,lib,bunch){"use strict";if(id==='JSLINT_'){lib.jslint=function(source,options,output){JSLINT(source,options);output.___nodes___[0].innerHTML=JSLINT.report();};lib.edition=function(){return JSLINT.edition;};lib.tree=function(){return JSLINT.tree;};}});