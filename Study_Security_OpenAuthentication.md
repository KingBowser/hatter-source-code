## 一、OTP ##
> OTP即 One-Time Password。是目前广泛使用的2阶段密码验证技术，如 [2-step verification](http://support.google.com/accounts/bin/topic.py?hl=en&topic=28786&parent=2373945&ctx=topic) <br>
</li></ul><blockquote>使用如下所示：<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/otp/2-step-with-red-box.png' /> <br>
输入如下显示的6位数字： <br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/otp/google_2step_iphone.png' /></blockquote>


<h2>二、规范</h2>
<a href='http://www.openauthentication.org/specification'>http://www.openauthentication.org/specification</a>

<ul><li>HOTP 【 An HMAC-Based OTP Algorithm (RFC 4226) 】<br>
</li></ul><blockquote><a href='http://www.ietf.org/rfc/rfc4226.txt'>TXT</a> <a href='http://tools.ietf.org/html/rfc4226'>HTML</a> <a href='http://tools.ietf.org/pdf/rfc4226.pdf'>PDF</a>
</blockquote><ul><li>TOTP【 Time-based One-time Password Algorithm (RFC 6238) 】<br>
</li></ul><blockquote><a href='http://www.ietf.org/rfc/rfc6238.txt'>TXT</a> <a href='http://tools.ietf.org/html/rfc6238'>HTML</a> <a href='http://tools.ietf.org/pdf/rfc6238.pdf'>PDF</a>
</blockquote><ul><li>OCRA 【 OATH Challenge/Response Algorithms Specification (RFC 6287) 】<br>
</li></ul><blockquote><a href='http://www.ietf.org/rfc/rfc6287.txt'>TXT</a> <a href='http://tools.ietf.org/html/rfc6287'>HTML</a> <a href='http://tools.ietf.org/pdf/rfc6287.pdf'>PDF</a></blockquote>

<h2>三、实现</h2>
<ul><li>Google Authenticator<br>
</li></ul><blockquote><a href='http://code.google.com/p/google-authenticator/'>http://code.google.com/p/google-authenticator/</a>
</blockquote><ul><li>Hatter's Go Lang Implement<br>
</li></ul><blockquote><a href='https://code.google.com/p/hatter-source-code/source/browse/trunk/golang/googleauth/googleauth.go'>https://code.google.com/p/hatter-source-code/source/browse/trunk/golang/googleauth/googleauth.go</a></blockquote>

<h2>四、其它OTP</h2>
<ul><li>Celf <a href='https://getclef.com/'>https://getclef.com/</a> -  Two-factor authentication from the future.<br>
</li><li>Waltz <a href='http://getwaltz.com/'>http://getwaltz.com/</a> - A modern account manager for the web</li></ul>


<hr />

<ul><li><a href='https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2'>https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2</a>
</li><li><a href='https://support.google.com/accounts/answer/1066447?hl=en&ref_topic=2784804'>https://support.google.com/accounts/answer/1066447?hl=en&amp;ref_topic=2784804</a>
</li><li><a href='https://www.saaspass.com/Authenticator.html'>https://www.saaspass.com/Authenticator.html</a>
</li><li><a href='http://www.multiotp.net/'>http://www.multiotp.net/</a>