# Secure SHell #

SSH 打通：
```
ssh-keygen -t rsa

cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```

SSH代理：
```
ssh -f -N <user name>@<ip or domain> -D <port>
```

`ssh -i`
```
ssh -i id_rsa  // private key, the pub key >> authorized_keys [id_rsa的机器需要和目标机打通]
```

`mac 下自动打通工具：`
```
https://github.com/beautifulcode/ssh-copy-id-for-OSX
```

`mac下设置keepalive：`
```
~/.ssh/config

ServerAliveInterval 300
ServerAliveCountMax 36
```

`ssh-otp`:
https://github.com/ziyan/ssh-otp

**端口修改:**

文件`/etc/ssh/sshd_config`，找到`#Port 22`修改为需要的端口。再生启服务:
```
service sshd restart
```

<br>
Related: <a href='Study_Security_Password_PasswordLess.md'>Study_Security_Password_PasswordLess</a>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://en.wikipedia.org/wiki/Secure_Shell'>http://en.wikipedia.org/wiki/Secure_Shell</a><br>
<code>[2].</code> <a href='http://jsczxy2.iteye.com/blog/1441712'>http://jsczxy2.iteye.com/blog/1441712</a><br>
<code>[3].</code> <a href='http://www.unixlore.net/articles/five-minutes-to-more-secure-ssh.html'>http://www.unixlore.net/articles/five-minutes-to-more-secure-ssh.html</a><br>