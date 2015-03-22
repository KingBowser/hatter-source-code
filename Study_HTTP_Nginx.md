Nginx的`proxy_pass`不识别`/etc/hosts`

替代方案：
You can get around this by installing dnsmasq and setting your resolver to 127.0.0.1. Basically this uses your local DNS as a resolver, but it only resolves what it knows about (among those things is your /etc/hosts) and forwards the rest to your default DNS.

<br>
Install make:<br>
<pre><code>yum -y install gcc automake autoconf libtool make<br>
</code></pre>

Install g++:<br>
<pre><code>yum install gcc gcc-c++<br>
</code></pre>

Install PCRE:<br>
<pre><code>cd /usr/local/src<br>
wget ftp://ftp.csx.cam.ac.uk/pub/software/programming/pcre/pcre-8.34.tar.gz <br>
tar -zxvf pcre-8.34.tar.gz<br>
cd pcre-8.34<br>
./configure<br>
make<br>
make install<br>
</code></pre>

Install zlib:<br>
<pre><code>cd /usr/local/src<br>
wget http://zlib.net/zlib-1.2.8.tar.gz<br>
tar -zxvf zlib-1.2.8.tar.gz<br>
cd zlib-1.2.8<br>
./configure<br>
make<br>
make install<br>
</code></pre>

Install OpenSSL(没有时):<br>
<pre><code>cd /usr/local/src<br>
wget https://www.openssl.org/source/openssl-1.0.1j.tar.gz<br>
tar -zxvf openssl-1.0.1j.tar.gz<br>
</code></pre>

Install nginx:<br>
<pre><code>cd /usr/local/src<br>
wget http://nginx.org/download/nginx-1.4.2.tar.gz<br>
tar -zxvf nginx-1.4.2.tar.gz<br>
cd nginx-1.4.2<br>
<br>
./configure --sbin-path=/usr/local/nginx/nginx \<br>
--conf-path=/usr/local/nginx/nginx.conf \<br>
--pid-path=/usr/local/nginx/nginx.pid \<br>
--with-http_ssl_module \<br>
--with-pcre=/usr/local/src/pcre-8.34 \<br>
--with-zlib=/usr/local/src/zlib-1.2.8 \<br>
--with-openssl=/usr/local/src/openssl-1.0.1j \<br>
--with-http_spdy_module<br>
<br>
make<br>
make install<br>
</code></pre>


<br>
<i>Related: <a href='Study_Security_TLS_OpenSSL.md'>Study_Security_TLS_OpenSSL</a> <a href='Study_Security_TLS_StartSSL.md'>Study_Security_TLS_StartSSL</a></i>


<h3>参考资料</h3>
<code>[1].</code> <a href='http://stackoverflow.com/questions/8305015/when-using-proxy-pass-can-etc-hosts-be-used-to-resolve-domain-names-instead-of/8559797'>http://stackoverflow.com/questions/8305015/when-using-proxy-pass-can-etc-hosts-be-used-to-resolve-domain-names-instead-of/8559797</a><br>
<code>[2].</code> <a href='http://www.nginx.cn/install'>http://www.nginx.cn/install</a><br>
<code>[3].</code> <a href='http://wiki.nginx.org/InstallOptions'>http://wiki.nginx.org/InstallOptions</a> <a href='http://wiki.nginx.org/ChsInstallOptions'>zh-CN</a><br>