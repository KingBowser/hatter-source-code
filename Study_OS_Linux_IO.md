Linux/Unix的五种I/O模型：
  * 阻塞I/O
  * 非阻塞I/O
  * I/O复用(select和poll)
  * 信号驱动I/O（SIGIO)
  * 异步I/O

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/bio.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/bio.jpg)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/nbio.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/nbio.jpg)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/rio.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/rio.jpg)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/sio.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/sio.jpg)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/aio.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/aio.jpg)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/io_cmp.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/io_cmp.jpg)


## Linux I/O Stack ##

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/linux-io-stack-diagram_v1.0.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/linux-io-stack-diagram_v1.0.png)

I/O related books: http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/io/


<br>
Related: <a href='Study_Network_IOMultiplexing.md'>Study_Network_IOMultiplexing</a>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://jasonwu.me/2012/12/04/the_unp_reading_notes_io_model.html'>http://jasonwu.me/2012/12/04/the_unp_reading_notes_io_model.html</a><br>