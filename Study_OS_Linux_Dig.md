安装`dig`：
```
yum install bind-utils
```

支持`edns-client-subnet`：
http://wilmer.gaa.st/edns-client-subnet/

```
$ bin/dig/dig @ns1.google.com www.google.com +client=130.89.89.130

; <<>> DiG 9.7.1-P2 <<>> @ns1.google.com www.google.com +client=130.89.89.130
;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 512
; CLIENT-SUBNET: 130.89.89.130/32/21
;; QUESTION SECTION:
;www.google.com.                        IN      A

;; ANSWER SECTION:
www.google.com.         604800  IN      CNAME   www.l.google.com.
www.l.google.com.       300     IN      A       74.125.79.104
www.l.google.com.       300     IN      A       74.125.79.99
www.l.google.com.       300     IN      A       74.125.79.147
```

安装支持`edns-client-subnet`的`dig`(`bind`)：
```
yum install patch
yum install gcc


wget ftp://ftp.isc.org/isc/bind9/9.7.3/bind-9.7.3.tar.gz
tar zxf bind-9.7.3.tar.gz
cd bind-9.7.3/

wget http://wilmer.gaa.st/edns-client-subnet/bind-9.7.3-dig-edns-client-subnet.diff
cp bin/dig/dig.c{,.20130110}
sed -n '26,322p' bind-9.7.3-dig-edns-client-subnet.diff > dig.patch
patch -p0 < dig.patch

./configure --disable-openssl-version-check
make && make install
make 
```


### 参考资料 ###
`[1].` http://blog.chinaunix.net/uid-8926237-id-3470426.html<br>
<code>[2].</code> <a href='http://wilmer.gaa.st/edns-client-subnet/'>http://wilmer.gaa.st/edns-client-subnet/</a><br>