## Links: ##
  * https://casecurity.org/
  * https://www.openssl.org/
  * http://gnutls.org/
  * http://www.trevp.net/tlslite/ [GitHub](https://github.com/trevp/tlslite)
  * http://www.bouncycastle.org/
  * https://istlsfastyet.com/
  * http://www.httpvshttps.com/
  * http://dev.chromium.org/Home/chromium-security/education/tls
  * http://chimera.labs.oreilly.com/books/1230000000545/ch04.html
  * https://sslcheck.casecurity.org/en_US
  * https://www.pcisecuritystandards.org/security_standards/
  * https://www.eff.org/https-everywhere/deploying-https
  * http://httpshaming.tumblr.com/
  * https://github.com/craigfrancis/dev-security
  * https://www.qualys.com
  * [CA/Browser Forum](https://cabforum.org/)



## SSL/TLS Test Tool: ##
  * https://www.ssllabs.com/ssltest/index.html
  * https://www.ssllabs.com/ssltest/viewMyClient.html
  * https://www.wormly.com/test_ssl
  * https://www.fairssl.dk/ssltest/
  * http://sourceforge.net/projects/sslscan/
  * https://cheapsslsecurity.com/ssltools/
  * http://sha1affected.com/
  * http://spdycheck.org/
  * [SSL Certificate Checker](https://www.digicert.com/help/)


<br>
With wget : OpenSSL: error:14077438:SSL routines:SSL23_GET_SERVER_HELLO:tlsv1 alert internal error<br>
<br>
With curl : curl: (35) error:14077438:SSL routines:SSL23_GET_SERVER_HELLO:tlsv1 alert internal error<br>
<br>
<br>
In wget, this can be fixed by specifying --secure-protocol=sslv3 option<br>
<br>
In curl, this can be fixed by specifying -sslv3 option<br>
<br>
<br>
<pre><code>wget --secure-protocol=sslv3 --no-check-certificate &lt;URL&gt;<br>
curl -sslv3 -k &lt;URL&gt;<br>
</code></pre>


<br>
Recommendations for Secure Use of TLS and DTLS - <a href='http://datatracker.ietf.org/doc/draft-ietf-uta-tls-bcp/'>http://datatracker.ietf.org/doc/draft-ietf-uta-tls-bcp/</a>


<br>
<a href='https://m.facebook.com/notes/protect-the-graph/windows-ssl-interception-gone-wild/1570074729899339'>https://m.facebook.com/notes/protect-the-graph/windows-ssl-interception-gone-wild/1570074729899339</a>


<hr />
SNI - <a href='http://wiki.apache.org/httpd/NameBasedSSLVHostsWithSNI'>http://wiki.apache.org/httpd/NameBasedSSLVHostsWithSNI</a>

<a href='http://blog.cloudflare.com/announcing-keyless-ssl-all-the-benefits-of-cloudflare-without-having-to-turn-over-your-private-ssl-keys/'>http://blog.cloudflare.com/announcing-keyless-ssl-all-the-benefits-of-cloudflare-without-having-to-turn-over-your-private-ssl-keys/</a>

<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/security/ssl/keyless-comic-v1.gif' />


<br>
<i>Related: <a href='Study_Security_EncryptionAlgorithm_SHA1.md'>Study_Security_EncryptionAlgorithm_SHA1</a> <a href='Study_HTTP_SPDY.md'>Study_HTTP_SPDY</a> <a href='Study_Security_HTTP_HSTS.md'>Study_Security_HTTP_HSTS</a></i>

<h3>参考资料</h3>
<code>[1].</code> <a href='https://wzyboy.im/post/799.html'>https://wzyboy.im/post/799.html</a><br>
<code>[2].</code> <a href='http://www.xinotes.org/notes/note/1094/'>http://www.xinotes.org/notes/note/1094/</a><br>
<code>[3].</code> <a href='https://help.ubuntu.com/10.04/serverguide/certificates-and-security.html'>https://help.ubuntu.com/10.04/serverguide/certificates-and-security.html</a><br>
<code>[4].</code> <a href='http://blog.roodo.com/rocksaying/archives/16158079.html'>http://blog.roodo.com/rocksaying/archives/16158079.html</a><br>
<code>[5].</code> <a href='http://www.madboa.com/geek/openssl/'>http://www.madboa.com/geek/openssl/</a><br>
<code>[6].</code> <a href='https://www.ssllabs.com/downloads/SSL_TLS_Deployment_Best_Practices_1.3.pdf'>https://www.ssllabs.com/downloads/SSL_TLS_Deployment_Best_Practices_1.3.pdf</a><br>