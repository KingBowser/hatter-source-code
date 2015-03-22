# 在Mac OSX下Subversion使用socks5代理 #

首先我只有通过ssh的socks5代理，没有HTTP代理，但在Subversion代理中只能配置HTTP代理。所以我们通过socks5转HTTP代理，再设置Subversion的HTTP代理。

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/svn_socks5_proxy.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/svn_socks5_proxy.png)

## 将socks5转HTTP代理 ##
下载Polipo最新版：
```
http://www.pps.univ-paris-diderot.fr/~jch/software/polipo/
```
编译：
```
make
```
看到生成文件即可：
```
-rwxr-xr-x polipo
```
编写配置文件 `c.conf` ，内容如下：
```
socksParentProxy = "localhost:7777"
socksProxyType = socks5
proxyPort = 7778
```
再通过命令后台启动Polipo：
```
polipo -c c.conf > log 2>&1 &
```

## 设置Subversion的HTTP代理 ##
在Subversion的配置文件 `~/.subversion/servers` 中增加：
```
[groups]
groupgooglecode = *.googlecode.com

[groupgooglecode]
http-proxy-host = localhost
http-proxy-port = 7778
http-proxy-compression = no
```

配置完就OK了，Enjoy it。