# https://www.startssl.com/ #

```
openssl rsa -in ssl.key -out ssl.key
```


  * _http://www.startssl.com/certs/_

```
wget https://www.startssl.com/certs/ca.pem
wget https://www.startssl.com/certs/sub.class1.server.ca.pem

cat your_cert.pem sub.class1.server.ca.pem ca.pem > new_cert.pem

```


  * nginx config sample

```
    server {
        listen       443 ssl;
        server_name  example.org www.example.org;

        ssl_certificate      /root/ssl/cert.pem;
        ssl_certificate_key  /root/ssl/key.pem;

        ssl_session_cache    shared:SSL:1m;
        ssl_session_timeout  5m;

        ssl_ciphers  HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers  on;

        location / {
            proxy_pass http://test.example.com/;
        }
    }
```


  * nginx SNI

```
$ nginx -V
...
TLS SNI support enabled
...
```

Related: [Study\_HTTP\_Nginx](Study_HTTP_Nginx.md) [Study\_Security\_TLS\_OpenSSL\_Cipher](Study_Security_TLS_OpenSSL_Cipher.md)


### 参考资料 ###
`[1].` http://www.freehao123.com/startssl-ssl/<br>
<code>[2].</code> <a href='http://nginx.org/cn/docs/http/configuring_https_servers.html'>http://nginx.org/cn/docs/http/configuring_https_servers.html</a><br>