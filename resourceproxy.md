# Introduction #

ResourceProxy is a tool for online JavaScript, CSS debug.

# Details #

> ### 1. What is ResourceProxy? ###
ResourceProxy is a tool for online JavaScript, CSS debug. It is a proxy tool. ResourceProxy give you the way to format, modify the online JavaScript, CSS resource(s).

> ### 2. How does ResourceProxy work? ###
ResourceProxy work like the graph below:
![http://hatter-source-code.googlecode.com/svn/trunk/attachments/resourceproxy/resourceproxy.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/resourceproxy/resourceproxy.png)

The proxy use the 'Host' field(RFC 2616 Section 14.23) in http request header to know the resource uri.<br>
Example: Access <a href='http://www.example.com/hello_world.txt'>http://www.example.com/hello_world.txt</a> from the browser.<br>
Add this to the /etc/hosts:<br>
<pre><code>10.20.157.110	www.example.com<br>
</code></pre>
Client request (send to the proxy):<br>
<pre><code>    GET /hello_world.txt HTTP/1.0<br>
    Host: www.example.com<br>
</code></pre>
Proxy received the request:<br>
Find from database<br>
<pre><code>select * from http_object where ip=&lt;client's ip&gt; and url=&lt;request's host+path&gt;<br>
</code></pre>
Not found: Proxy resend the request(send to the real server):<br>
<pre><code>    GET /hello_world.txt HTTP/1.0<br>
    Host: www.example.com<br>
</code></pre>
The server received the request and return:<br>
<pre><code>    HTTP/1.0 200 OK<br>
    Date: Mon, 05 Mar 2012 06:28:45 GMT<br>
    Server: Apache<br>
    Content-Language: en-US<br>
    Connection: Close<br>
    Transfer-Encoding: chunked<br>
    Content-Type: text/plain;charset=UTF-8<br>
    <br>
    Returned contents...<br>
</code></pre>
Then the proxy received the response, record to the database and return the real response to the client.<br>
<br>
<blockquote><h3>3. How to install ResourceProxy?</h3>
Check out:<br>
<pre><code>svn co https://hatter-source-code.googlecode.com/svn/trunk/resourceproxy/ resourceproxy<br>
</code></pre></blockquote>

Build it by Ant(My ant version is: 1.7.0)<br>
<pre><code>Create sqlite data file: data.db<br>
    CREATE TABLE http_object ( id INTEGER PRIMARY KEY, method TEXT, url TEXT, access_address TEXT, <br>
status INTEGER, status_message TEXT, content_type TEXT, charset TEXT, encoding TEXT, header TEXT, <br>
bytes TEXT, string TEXT, is_updated TEXT );<br>
    CREATE UNIQUE INDEX idx_http_object_url_accaddr on http_object (url, access_address);<br>
    CREATE TABLE host_config ( id INTEGER PRIMARY KEY, domain TEXT , access_address TEXT, target_ip TEXT);<br>
    CREATE UNIQUE INDEX idx_host_config_domain_accaddr on host_config (domain, access_address);<br>
    CREATE TABLE user_config ( access_address TEXT PRIMARY KEY, user_agent TEXT );<br>
</code></pre>

Then run: startup<br>
<br>
<blockquote><h3>4. How to use it?</h3>
For example we debug www.baidu.com</blockquote>

Step 1 Add host to your /etc/hosts (Unix/Linux, Mac) file:<br>
<pre><code>    10.20.157.110      www.baidu.com<br>
</code></pre>

Step 2 Access: www.baidu.com<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/resourceproxy/baidu1.png' />

Step 3 Access: <a href='http://c2.hatter.zj.cn/'>http://c2.hatter.zj.cn/</a>, you will see:<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/resourceproxy/resourceproxy1.png' />

Step 4 Modify <a href='http://www.baidu.com/'>http://www.baidu.com/</a>, and save it: <br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/resourceproxy/resourceproxy2.png' />

Step 5 Refresh www.baidu.com, and you see:<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/resourceproxy/baidu2.png' />

If you modify JavaScript file, you can format it.<br>
<br>
<blockquote><h3>5. Startup properties</h3>
<pre><code>-Ddebug&lt;no value&gt; set thread pool to single thread<br>
-Dthishost=example.com set current host domain, access local jssp file(s)<br>
-Dcheckjsspupdate=n set n second(s) to check jssp file if updated<br>
</code></pre>