# [Network](http://docs.oracle.com/javase/6/docs/technotes/guides/net/index.html) #

参数配置[Properties](http://docs.oracle.com/javase/6/docs/technotes/guides/net/properties.html)：

`sun.net.NetworkClient`
```
                        vals[0] = Integer.getInteger("sun.net.client.defaultReadTimeout", 0).intValue();
                        vals[1] = Integer.getInteger("sun.net.client.defaultConnectTimeout", 0).intValue();
                        encs[0] = System.getProperty("file.encoding", "ISO8859_1");
```

_SEE ALSO: [Study\_Network](Study_Network.md) [Study\_OS\_Linux\_Network](Study_OS_Linux_Network.md)_