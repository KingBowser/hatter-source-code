# How to install apache benchmark on CentOS #
You can discover which package contains the program you want using yum provides:

```
yum provides /usr/bin/ab
```

Then you will see that ab is in the httpd-tools package.

And now you can install it:

```
yum install httpd-tools
```


### 参考资料 ###
`[1].` http://serverfault.com/questions/514401/how-to-install-apache-benchmark-on-centos<br>