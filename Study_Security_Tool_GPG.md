http://www.ietf.org/rfc/rfc4880.txt


| 加密文件 | `gpg -e -r <uid> <filename>` |
|:-------------|:-----------------------------|
| 解密文件 | `gpg <filename>` |
| 生成密钥 | `gpg --gen-key` |
| 列出公钥 | `gpg --list-keys / -k` |
| 列出私钥 | `gpg --list-secret-key / -K` |
| 导出公钥 | `gpg --export <uid> -a > <filename.asc>` |
| 导入公钥 | `gpg --import <filename.asc>` |
| 删除公钥 | `gpg --delete-keys <uid>` |
| 删除私钥 | `gpg --delete-secret-keys <uid>` |
| 信任公钥 | `gpg --sign-key <uid>` |

https://keybase.io/


<br>
Related: <a href='Study_Security_EncryptionAlgorithm.md'>Study_Security_EncryptionAlgorithm</a>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.openpgp.org/'>http://www.openpgp.org/</a><br>
<code>[2].</code> <a href='https://gpgtools.org/'>https://gpgtools.org/</a><br>
<code>[3].</code> <a href='http://www.pgpi.org/'>http://www.pgpi.org/</a><br>
<code>[4].</code> <a href='http://www.gnupg.org/'>http://www.gnupg.org/</a><br>
<code>[5].</code> <a href='http://openpgpjs.org/'>http://openpgpjs.org/</a><br>
<code>[6].</code> <a href='http://www.cryptography.org/getpgp.htm'>http://www.cryptography.org/getpgp.htm</a><br>