|  **Header**  |  **Example Value**   |  **Notes**   |
|:-------------|:---------------------|:-------------|
| `Access-Control-Allow-Credentials` |    TRUE    |      |
| `Access-Control-Allow-Headers` |    X-PINGOTHER |      |
| `Access-Control-Allow-Methods` |    PUT, DELETE, XMODIFY    |      |
| `Access-Control-Allow-Origin` |    http://example.org  |      |
| `Access-Control-Expose-Headers` |    X-My-Custom-Header, X-Another-Custom-Header |      |
| `Access-Control-Max-Age` |    2520    |      |
| `Accept-Ranges` |    bytes   |      |
| `Age` |    12  |      |
| `Allow` |    GET, HEAD, POST, OPTIONS    |  Commonly includes other things, like PROPFIND etc…  |
| `Alternate-Protocol` |    443:npn-spdy/2,443:npn-spdy/2   |      |
| `Cache-Control` |    private, no-cache, must-revalidate  |      |
| `Client-Date` |    Tue, 27 Jan 2009 18:17:30 GMT   |      |
| `Client-Peer` |    123.123.123.123:80  |      |
| `Client-Response-Num` |    1   |      |
| `Connection` |    Keep-Alive  |      |
| `Content-Disposition` |    attachment; filename=”example.exe”  |      |
| `Content-Encoding` |    gzip    |      |
| `Content-Language` |    en  |      |
| `Content-Length` |    1329    |      |
| `Content-Location` |    /index.htm  |      |
| `Content-MD5` |    Q2hlY2sgSW50ZWdyaXR5IQ==    |      |
| `Content-Range` |    bytes 21010-47021/47022 |      |
| `Content-Security-Policy, X-Content-Security-Policy, X-WebKit-CSP` |    default-src ‘self’  |  Different header needed to control different browsers   |
| `Content-Security-Policy-Report-Only` |    default-src ‘self'; …; report-uri /csp\_report\_parser;   |      |
| `Content-Type` |    text/html   |  Can also include charset information (E.g.: text/html;charset=ISO-8859-1)   |
| `Date` |    Fri, 22 Jan 2010 04:00:00 GMT   |      |
| `ETag` |    “737060cd8c284d8af7ad3082f209582d”  |      |
| `Expires` |    Mon, 26 Jul 1997 05:00:00 GMT   |      |
| `HTTP` |    /1.1 401 Unauthorized   |  Special header, no colon space delimiter    |
| `Keep-Alive` |    timeout=3, max=87   |      |
| `Last-Modified` |    Tue, 15 Nov 1994 12:45:26 +0000 |      |
| `Link` |    <http://www.example.com/>; rel=”cononical”  |  rel=”alternate” |
| `Location` |    http://www.example.com/ |      |
| `P3P` |    policyref=”http://www.example.com/w3c/p3p.xml”, CP=”NOI DSP COR ADMa OUR NOR STA”   |      |
| `Pragma` |    no-cache    |      |
| `Proxy-Authenticate` |    Basic   |      |
| `Proxy-Connection` |    Keep-Alive  |      |
| `Refresh` |    5; url=http://www.example.com/  |      |
| `Retry-After` |    120 |      |
| `Server` |    Apache  |      |
| `Set-Cookie` |    test=1; domain=example.com; path=/; expires=Tue, 01-Oct-2013 19:16:48 GMT   |  Can also include the secure and HTTPOnly flag   |
| `Status` |    200 OK  |      |
| `Strict-Transport-Security` |    max-age=16070400; includeSubDomains |      |
| `Timing-Allow-Origin` |    www.example.com |      |
| `Trailer` |    Max-Forwards    |      |
| `Transfer-Encoding` |    chunked |  compress, deflate, gzip, identity   |
| `Upgrade` |    HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11   |      |
| `Vary` |    |      |
| `Via` |    1.0 fred, 1.1 example.com (Apache/1.1)  |      |
| `Warning` |    Warning: 199 Miscellaneous warning  |      |
| `WWW-Authenticate` |    Basic   |      |
| `X-Aspnet-Version` |    2.0.50727   |      |
| `X-Content-Type-Options` |    nosniff |      |
| `X-Frame-Options` |    deny    |      |
| `X-Permitted-Cross-Domain-Policies` |    master-only |  Used by Adobe Flash |
| `X-Pingback` |    http://www.example.com/pingback/xmlrpc  |      |
| `X-Powered-By` |    PHP/5.4.0   |      |
| `X-Robots-Tag` |    noindex,nofollow    |      |
| `X-UA-Compatible` |    Chome=1 |      |
| `X-XSS-Protection` |    1; mode=block   |      |

<br>
Another file: <a href='https://github.com/dret/sedola/blob/master/MD/headers.md'>https://github.com/dret/sedola/blob/master/MD/headers.md</a>


<h3>参考资料</h3>
<code>[1].</code> <a href='https://blog.whitehatsec.com/list-of-http-response-headers/'>https://blog.whitehatsec.com/list-of-http-response-headers/</a><br>