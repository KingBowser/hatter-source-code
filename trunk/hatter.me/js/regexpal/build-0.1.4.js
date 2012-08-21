(function(){if(window.XRegExp){return }var B={RegExp:RegExp,exec:RegExp.prototype.exec,match:String.prototype.match,replace:String.prototype.replace};var A={extended:/(?:[^[#\s\\]+|\\(?:[\S\s]|$)|\[\^?]?(?:[^\\\]]+|\\(?:[\S\s]|$))*]?)+|(\s*#[^\n\r\u2028\u2029]*\s*|\s+)([?*+]|{[0-9]+(?:,[0-9]*)?})?/g,singleLine:/(?:[^[\\.]+|\\(?:[\S\s]|$)|\[\^?]?(?:[^\\\]]+|\\(?:[\S\s]|$))*]?)+|\./g,characterClass:/(?:[^\\[]+|\\(?:[\S\s]|$))+|\[\^?(]?)(?:[^\\\]]+|\\(?:[\S\s]|$))*]?/g,capturingGroup:/(?:[^[(\\]+|\\(?:[\S\s]|$)|\[\^?]?(?:[^\\\]]+|\\(?:[\S\s]|$))*]?|\((?=\?))+|(\()(?:<([$\w]+)>)?/g,namedBackreference:/(?:[^\\[]+|\\(?:[^k]|$)|\[\^?]?(?:[^\\\]]+|\\(?:[\S\s]|$))*]?|\\k(?!<[$\w]+>))+|\\k<([$\w]+)>([0-9]?)/g,replacementVariable:/(?:[^$]+|\$(?![1-9$&`']|{[$\w]+}))+|\$(?:([1-9]\d*|[$&`'])|{([$\w]+)})/g};XRegExp=function(G,E){E=E||"";if(E.indexOf("x")>-1){G=B.replace.call(G,A.extended,function(I,H,J){return H?(J||"(?:)"):I})}var D=false;if(E.indexOf("k")>-1){var C=[];G=B.replace.call(G,A.capturingGroup,function(I,H,J){if(H){if(J){D=true}C.push(J||null);return"("}else{return I}});if(D){G=B.replace.call(G,A.namedBackreference,function(I,H,K){var J=H?C.indexOf(H):-1;return J>-1?"\\"+(J+1)+(K?"(?:)"+K:""):I})}}G=B.replace.call(G,A.characterClass,function(I,H){return H?B.replace.call(I,"]","\\]"):I});if(E.indexOf("s")>-1){G=B.replace.call(G,A.singleLine,function(H){return H==="."?"[\\S\\s]":H})}var F=B.RegExp(G,B.replace.call(E,/[sxk]+/g,""));if(D){F._captureNames=C}return F};RegExp.prototype.addFlags=function(C){C=(C||"")+(this.global?"g":"")+(this.ignoreCase?"i":"")+(this.multiline?"m":"");var D=new XRegExp(this.source,C);if(!D._captureNames&&this._captureNames){D._captureNames=this._captureNames.slice(0)}return D};RegExp.prototype.exec=function(F){var C=B.exec.call(this,F);if(!(this._captureNames&&C&&C.length>1)){return C}for(var E=1;E<C.length;E++){var D=this._captureNames[E-1];if(D){C[D]=C[E]}}return C};String.prototype.match=function(C){if(!C._captureNames||C.global){return B.match.call(this,C)}return C.exec(this)};String.prototype.replace=function(C,D){if(!(C instanceof B.RegExp&&C._captureNames)){return B.replace.apply(this,arguments)}if(typeof D==="function"){return B.replace.call(this,C,function(){arguments[0]=new String(arguments[0]);for(var E=0;E<C._captureNames.length;E++){if(C._captureNames[E]){arguments[0][C._captureNames[E]]=arguments[E+1]}}return D.apply(window,arguments)})}else{return B.replace.call(this,C,function(){var E=arguments;return B.replace.call(D,A.replacementVariable,function(G,F,J){if(F){switch(F){case"$":return"$";case"&":return E[0];case"`":return E[E.length-1].slice(0,E[E.length-2]);case"'":return E[E.length-1].slice(E[E.length-2]+E[0].length);default:var H="";F=+F;while(F>C._captureNames.length){H=F.split("").pop()+H;F=Math.floor(F/10)}return(F?E[F]:"$")+H}}else{if(J){var I=C._captureNames.indexOf(J);return I>-1?E[I+1]:G}else{return G}}})})}}})();XRegExp.cache=function(C,A){var B="/"+C+"/"+(A||"");return XRegExp.cache[B]||(XRegExp.cache[B]=new XRegExp(C,A))};XRegExp.overrideNative=function(){RegExp=XRegExp};if(!Array.prototype.indexOf){Array.prototype.indexOf=function(C,D){var A=this.length;for(var B=(D<0)?Math.max(0,A+D):D||0;B<A;B++){if(this[B]===C){return B}}return -1}}function $(A){if(A.nodeName){return A}if(typeof A==="string"){return document.getElementById(A)}return false}var trim=function(){var B=/^\s\s*/,A=/\s\s*$/;return function(C){return C.replace(B,"").replace(A,"")}}();function replaceHtml(el,html){var oldEl=$(el);/*@cc_on oldEl.innerHTML = html;return oldEl; @*/var newEl=oldEl.cloneNode(false);newEl.innerHTML=html;oldEl.parentNode.replaceChild(newEl,oldEl);return newEl}function replaceOuterHtml(C,A){C=replaceHtml(C,"");if(C.outerHTML){var E=C.id,B=C.className,D=C.nodeName;C.outerHTML="<"+D+' id="'+E+'" class="'+B+'">'+A+"</"+D+">";C=$(E)}else{C.innerHTML=A}return C}function getElementsByClassName(F,E,A){var D=($(A)||document).getElementsByTagName(E||"*"),C=[];for(var B=0;B<D.length;B++){if(hasClass(F,D[B])){C.push(D[B])}}return C}function hasClass(B,A){return XRegExp.cache("(?:^|\\s)"+B+"(?:\\s|$)").test($(A).className)}function addClass(B,A){A=$(A);if(!hasClass(B,A)){A.className=trim(A.className+" "+B)}}function removeClass(B,A){A=$(A);A.className=trim(A.className.replace(XRegExp.cache("(?:^|\\s)"+B+"(?:\\s|$)","g")," "))}function toggleClass(B,A){if(hasClass(B,A)){removeClass(B,A)}else{addClass(B,A)}}function swapClass(A,C,B){removeClass(A,B);addClass(C,B)}function replaceSelection(C,E){if(C.setSelectionRange){var F=C.selectionStart,A=C.selectionEnd,D=(F+E.length);C.value=(C.value.substring(0,F)+E+C.value.substring(A));C.setSelectionRange(D,D)}else{if(document.selection){var B=document.selection.createRange();B.text=E;B.select()}}}function extend(C,B){for(var A in B){C[A]=B[A]}return C}function purge(D){var B=D.attributes,C,A,E;if(B){A=B.length;for(C=0;C<A;C+=1){E=B[C].name;if(typeof D[E]==="function"){D[E]=null}}}B=D.childNodes;if(B){A=B.length;for(C=0;C<A;C+=1){purge(D.childNodes[C])}}}var isWebKit=navigator.userAgent.indexOf("WebKit")>-1,isIE/*@cc_on = true @*/,isIE6/*@cc_on = @_jscript_version < 5.7 @*/;var RegexPal={fields:{search:new SmartField("search"),input:new SmartField("input"),options:{flags:{g:$("flagG"),i:$("flagI"),m:$("flagM"),s:$("flagS")},highlightSyntax:$("highlightSyntax"),highlightMatches:$("highlightMatches"),invertMatches:$("invertMatches")}}};extend(RegexPal,function(){var A=RegexPal.fields,B=A.options;return{highlightMatches:function(){var C={matchPair:/`~\{((?:[^}]+|\}(?!~`))*)\}~`((?:[^`]+|`(?!~\{(?:[^}]+|\}(?!~`))*\}~`))*)(?:`~\{((?:[^}]+|\}(?!~`))*)\}~`)?/g,sansTrailingAlternator:/^(?:[^\\|]+|\\[\S\s]?|\|(?=[\S\s]))*/};return function(){var F=String(A.search.textbox.value),E=String(A.input.textbox.value);if(XRegExp.cache('<[bB] class="?err"?>').test(A.search.bg.innerHTML)||(!F.length&&!B.invertMatches.checked)||!B.highlightMatches.checked){A.input.clearBg();return }try{var H=new XRegExp(C.sansTrailingAlternator.exec(F)[0],(B.flags.g.checked?"g":"")+(B.flags.i.checked?"i":"")+(B.flags.m.checked?"m":"")+(B.flags.s.checked?"s":""))}catch(G){A.input.clearBg();return }if(B.invertMatches.checked){var D=("`~{"+E.replace(H,"}~`$&`~{")+"}~`").replace(XRegExp.cache("`~\\{\\}~`|\\}~``~\\{","g"),"")}else{var D=E.replace(H,"`~{$&}~`")}D=D.replace(XRegExp.cache("[<&>]","g"),"_").replace(C.matchPair,"<b>$1</b>$2<i>$3</i>");A.input.setBgHtml(D)}}(),highlightSearchSyntax:function(){if(B.highlightSyntax.checked){A.search.setBgHtml(parseRegex(A.search.textbox.value))}else{A.search.clearBg()}},permalink:function(){var E=(B.flags.i.checked?"i":"")+(B.flags.m.checked?"m":"")+(B.flags.s.checked?"s":""),D=encodeURIComponent(A.search.textbox.value),C=encodeURIComponent(A.input.textbox.value);location="./?flags="+E+"&regex="+D+"&input="+C}}}());var parseRegex=function(){var C={regexToken:/\[\^?]?(?:[^\\\]]+|\\[\S\s]?)*]?|\\(?:0(?:[0-3][0-7]{0,2}|[4-7][0-7]?)?|[1-9][0-9]*|x[0-9A-Fa-f]{2}|u[0-9A-Fa-f]{4}|c[A-Za-z]|[\S\s]?)|\((?:\?[:=!]?)?|(?:[?*+]|\{[0-9]+(?:,[0-9]*)?\})\??|[^.?*+^${[()|\\]+|./g,characterClassParts:/^(<opening>\[\^?)(<contents>]?(?:[^\\\]]+|\\[\S\s]?)*)(<closing>]?)$/.addFlags("k"),characterClassToken:/[^\\-]+|-|\\(?:[0-3][0-7]{0,2}|[4-7][0-7]?|x[0-9A-Fa-f]{2}|u[0-9A-Fa-f]{4}|c[A-Za-z]|[\S\s]?)/g,quantifier:/^(?:[?*+]|\{[0-9]+(?:,[0-9]*)?\})\??$/},B={NONE:0,RANGE_HYPHEN:1,METACLASS:2,ALTERNATOR:3};function D(F){return'<b class="err">'+F+"</b>"}function A(G){if(G.length>1&&G.charAt(0)==="\\"){var F=G.slice(1);if(XRegExp.cache("^c[A-Za-z]$").test(F)){return"ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(F.charAt(1).toUpperCase())+1}else{if(XRegExp.cache("^(?:x[0-9A-Fa-f]{2}|u[0-9A-Fa-f]{4})$").test(F)){return parseInt(F.slice(1),16)}else{if(XRegExp.cache("^(?:[0-3][0-7]{0,2}|[4-7][0-7]?)$").test(F)){return parseInt(F,8)}else{if(F.length===1&&"cuxDdSsWw".indexOf(F)>-1){return false}else{if(F.length===1){switch(F){case"b":return 8;case"f":return 12;case"n":return 10;case"r":return 13;case"t":return 9;case"v":return 11;default:return F.charCodeAt(0)}}}}}}}else{if(G!=="\\"){return G.charCodeAt(0)}}return false}function E(N){var H="",K=C.characterClassParts.exec(N),F=C.characterClassToken,I={rangeable:false,type:B.NONE},L,J;H+=K.closing?K.opening:D(K.opening);while(L=F.exec(K.contents)){J=L[0];if(J.charAt(0)==="\\"){if(XRegExp.cache("^\\\\[cux]$").test(J)){H+=D(J);I={rangeable:I.type!==B.RANGE_HYPHEN}}else{if(XRegExp.cache("^\\\\[dsw]$","i").test(J)){H+="<b>"+J+"</b>";I={rangeable:I.type!==B.RANGE_HYPHEN,type:B.METACLASS}}else{if(J==="\\"){H+=D(J)}else{H+="<b>"+J.replace(XRegExp.cache("[<&>]"),"_")+"</b>";I={rangeable:I.type!==B.RANGE_HYPHEN,charCode:A(J)}}}}}else{if(J==="-"){if(I.rangeable){var M=F.lastIndex,G=F.exec(K.contents);if(G){var O=A(G[0]);if((O!==false&&I.charCode>O)||I.type===B.METACLASS||XRegExp.cache("^\\\\[dsw]$","i").test(G[0])){H+=D("-")}else{H+="<u>-</u>"}I={rangeable:false,type:B.RANGE_HYPHEN}}else{if(K.closing){H+="-"}else{H+="<u>-</u>";break}}F.lastIndex=M}else{H+="-";I={rangeable:I.type!==B.RANGE_HYPHEN}}}else{H+=J.replace(XRegExp.cache("[<&>]","g"),"_");I={rangeable:(J.length>1||I.type!==B.RANGE_HYPHEN),charCode:J.charCodeAt(J.length-1)}}}}return H+K.closing}return function(U){var I="",R=0,S=0,G=[],J={quantifiable:false,type:B.NONE},P,K;function F(V){return'<b class="g'+S+'">'+V+"</b>"}while(P=C.regexToken.exec(U)){K=P[0];switch(K.charAt(0)){case"[":I+="<i>"+E(K)+"</i>";J={quantifiable:true};break;case"(":if(K.length===2){I+=D(K)}else{if(K.length===1){R++}S=S===5?1:S+1;G.push({index:I.length+14,opening:K});I+=F(K)}J={quantifiable:false};break;case")":if(!G.length){I+=D(")");J={quantifiable:false}}else{I+=F(")");J={quantifiable:!XRegExp.cache("^[=!]").test(G[G.length-1].opening.charAt(2)),style:"g"+S};S=S===1?5:S-1;G.pop()}break;case"\\":if(XRegExp.cache("^[1-9]").test(K.charAt(1))){var Q="",O=+K.slice(1);while(O>R){Q=XRegExp.cache("[0-9]$").exec(O)[0]+Q;O=Math.floor(O/10)}if(O>0){I+="<b>\\"+O+"</b>"+Q}else{var L=XRegExp.cache("^\\\\([0-3][0-7]{0,2}|[4-7][0-7]?|[89])([0-9]*)").exec(K);I+="<b>\\"+L[1]+"</b>"+L[2]}}else{if(XRegExp.cache("^[0bBcdDfnrsStuvwWx]").test(K.charAt(1))){if(XRegExp.cache("^\\\\[cux]$").test(K)){I+=D(K);J={quantifiable:false};break}I+="<b>"+K+"</b>";if("bB".indexOf(K.charAt(1))>-1){J={quantifiable:false};break}}else{if(K==="\\"){I+=D(K)}else{I+=K.replace(XRegExp.cache("[<&>]"),"_")}}}J={quantifiable:true};break;default:if(C.quantifier.test(K)){if(J.quantifiable){var H=XRegExp.cache("^\\{([0-9]+)(?:,([0-9]*))?").exec(K);if(H&&((H[1]>65535)||(H[2]&&((H[2]>65535)||(+H[1]>+H[2]))))){I+=D(K)}else{I+=(J.style?'<b class="'+J.style+'">':"<b>")+K+"</b>"}}else{I+=D(K)}J={quantifiable:false}}else{if(K==="|"){if(J.type===B.NONE||(J.type===B.ALTERNATOR&&!G.length)){I+=D(K)}else{I+=G.length?F("|"):"<b>|</b>"}J={quantifiable:false,type:B.ALTERNATOR}}else{if("^$".indexOf(K)>-1){I+="<b>"+K+"</b>";J={quantifiable:false}}else{if(K==="."){I+="<b>.</b>";J={quantifiable:true}}else{I+=K.replace(XRegExp.cache("[<&>]","g"),"_");J={quantifiable:true}}}}}}}var N=0;for(var M=0;M<G.length;M++){var T=G[M].index+N;I=(I.slice(0,T)+D(G[M].opening)+I.slice(T+G[M].opening.length));N+=D("").length}return I}}();function SmartField(C){C=$(C);var A=C.getElementsByTagName("textarea")[0],B=document.createElement("pre");A.id=C.id+"Text";B.id=C.id+"Bg";C.insertBefore(B,A);A.onkeydown=function(D){SmartField.prototype._onKeyDown(D)};A.onkeyup=function(D){SmartField.prototype._onKeyUp(D)};if(isIE){C.style.overflowX="hidden"}if(A.spellcheck){A.spellcheck=false}if(isWebKit){A.style.marginLeft="-3px"}this.field=C;this.textbox=A;this.bg=B}extend(SmartField.prototype,{setBgHtml:function(A){if(isIE){A=A.replace(XRegExp.cache("^\\r\\n"),"\r\n\r\n")}this.bg=replaceOuterHtml(this.bg,A+"<br>&nbsp;");this.setDimensions()},clearBg:function(){this.setBgHtml(this.textbox.value.replace(XRegExp.cache("[<&>]","g"),"_"))},setDimensions:function(){this.textbox.style.width="";var A=this.textbox.scrollWidth,B=this.textbox.offsetWidth;this.textbox.style.width=(A===B?B-1:A+8)+"px";this.textbox.style.height=Math.max(this.bg.offsetHeight,this.field.offsetHeight-2)+"px"},_onKeyDown:function(B){B=B||event;if(!this._filterKeys(B)){return false}var A=B.srcElement||B.target;switch(A){case RegexPal.fields.search.textbox:setTimeout(function(){RegexPal.highlightSearchSyntax.call(RegexPal)},0);break}if(isWebKit&&A.selectionEnd===A.value.length){A.parentNode.scrollTop=A.scrollHeight}this._testKeyHold(B)},_onKeyUp:function(B){B=B||event;var A=B.srcElement||B.target;this._keydownCount=0;if(this._matchOnKeyUp){this._matchOnKeyUp=false;switch(A){case RegexPal.fields.search.textbox:case RegexPal.fields.input.textbox:RegexPal.highlightMatches();break}}},_testKeyHold:function(B){var A=B.srcElement||B.target;this._keydownCount++;if(this._keydownCount>2){RegexPal.fields.input.clearBg();this._matchOnKeyUp=true}else{switch(A){case RegexPal.fields.search.textbox:case RegexPal.fields.input.textbox:setTimeout(function(){RegexPal.highlightMatches.call(RegexPal)},0);break}}},_filterKeys:function(C){var A=C.srcElement||C.target,B=RegexPal.fields;if(this._deadKeys.indexOf(C.keyCode)>-1){return false}if((C.keyCode===9)&&(A===B.input.textbox||(A===B.search.textbox&&!C.shiftKey))){if(A===B.input.textbox){if(C.shiftKey){B.search.textbox.focus()}else{replaceSelection(A,"\t");if(window.opera){setTimeout(function(){A.focus()},0)}}}else{B.input.textbox.focus()}if(C.preventDefault){C.preventDefault()}else{C.returnValue=false}}return true},_matchOnKeyUp:false,_keydownCount:0,_deadKeys:[16,17,18,19,20,27,33,34,35,36,37,38,39,40,44,45,112,113,114,115,116,117,118,119,120,121,122,123,144,145]});(function(){var E=RegexPal.fields,B=E.options;onresize=function(J){E.input.field.style.height=Math.max((window.innerHeight||document.documentElement.clientHeight)-210,60)+"px";E.search.setDimensions();E.input.setDimensions()};onresize();RegexPal.highlightSearchSyntax();RegexPal.highlightMatches();for(var G in B.flags){B.flags[G].onclick=RegexPal.highlightMatches}B.highlightSyntax.onclick=RegexPal.highlightSearchSyntax;B.highlightMatches.onclick=RegexPal.highlightMatches;B.invertMatches.onclick=RegexPal.highlightMatches;function C(J){return function(){J.clearBg();J.textbox.value="";J.textbox.onfocus=null}}if(E.search.textbox.value==="Enter regex here."){E.search.textbox.onfocus=C(E.search)}if(E.input.textbox.value==="Enter test data here."){E.input.textbox.onfocus=C(E.input)}var F=$("quickReferenceDropdown"),A=$("quickReference"),I=getElementsByClassName("pin","img",A)[0],H=getElementsByClassName("close","img",A)[0];F.onmouseover=function(J){removeClass("hidden",A);addClass("hover",this)};F.onmouseout=function(J){if(!hasClass("pinned",A)){addClass("hidden",A);removeClass("hover",this)}};A.onmouseover=function(J){F.onmouseover()};A.onmouseout=function(J){F.onmouseout()};I.onclick=function(J){this.src="./assets/"+(hasClass("pinned",A)?"pin":"pinned")+".gif";toggleClass("pinned",A)};H.onclick=function(J){swapClass("pinned","hidden",A);I.src="./assets/pin.gif"};if(isIE6){var D=$("optionsDropdown");D.onmouseenter=function(){addClass("hover",this)};D.onmouseleave=function(){removeClass("hover",this)};onunload=function(J){E.search.clearBg();E.input.clearBg();purge(document.body)}}})();
