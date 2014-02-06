
```
http://www.pps.univ-paris-diderot.fr/~jch/software/polipo/
```

```
make
```

```
-rwxr-xr-x polipo
```

c.conf:
```
socksParentProxy = "localhost:7777"
socksProxyType = socks5
proxyPort = 7778
```

```
polipo -c c.conf > log 2>&1 &
```

```
[groups]
groupgooglecode = *.googlecode.com

[groupgooglecode]
http-proxy-host = localhost
http-proxy-port = 7778
http-proxy-compression = no
```

