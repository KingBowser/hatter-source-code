makeauthority.sh
```
# Run this once
openssl genrsa -des3 -out ca.key 4096
openssl req -new -x509 -days 365 -key ca.key -out ca.crt
```

makecert.sh
```
# Run this for each email account.  The system must install the CA cert and the resulting p12 file in order to be happy.
 
# Borrowed from http://serverfault.com/questions/103263/can-i-create-my-own-s-mime-certificate-for-email-encryption
 
openssl genrsa -des3 -out smime.key 4096
openssl req -new -key smime.key -out smime.csr
openssl x509 -req -days 365 -in smime.csr -CA ca.crt -CAkey ca.key -set_serial 1 -out smime.crt -setalias "Self Signed SMIME" -addtrust emailProtection -addreject clientAuth -addreject serverAuth -trustout
openssl pkcs12 -export -in smime.crt -inkey smime.key -out smime.p12
```

"Personal Information Exchange (.p12)".



### 参考资料 ###
`[1].` https://gist.github.com/richieforeman/3166387<br>
<code>[2].</code> <a href='https://kb.iu.edu/data/bcsn.html'>https://kb.iu.edu/data/bcsn.html</a><br>
