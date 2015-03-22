## http://tsocks.sourceforge.net/ ##

```
brew tap adamv/alt
brew install tsocks
```

`/usr/local/etc/tsocks.conf` or `/etc/tsocks.conf`
```
local = 192.168.0.0/255.255.255.0
server = 127.0.0.1
server_type = 5
server_port = 8080
```

```
tsocks wget http://google.com/
```


### 参考资料 ###
`[1].` https://whatbox.ca/wiki/tsocks<br>