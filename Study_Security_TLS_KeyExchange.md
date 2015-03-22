| **密钥协商术语** |  **中文** | **缩写** | **定义和用法** |
|:-----------------------|:------------|:-----------|:--------------------|
| Key exchange | 密钥交换 |  | 生成非对称加密算法的密钥的过程。两种主要方法是 RSA 协议和 Diffie-Hellman 协议。  |
| Diffie-Hellman protocol | Diffie-Hellman 协议 | DH | 一种涉及密钥生成和密钥验证的密钥交换协议。通常称为经过验证的密钥交换。 |
| RSA protocol | RSA 协议  | RSA | 一种涉及密钥生成和密钥传输的密钥交换协议。此协议以其三个创建者 Rivest、Shamir 和 Adleman 命名。 |
| Perfect forward secrecy | 完全正向保密 | PFS | 仅适用于经过验证的密钥交换。PFS 确保密钥的长期保密材料不会影响来自以前通信的已交换密钥的保密性。在 PFS 中，不能使用保护数据传输的密钥派生其他密钥。此外，也不能使用保护数据传输的密钥的源派生其他密钥。  |
| Oakley method | Oakley 方法  |  | 一种以安全方式为阶段 2 建立密钥的方法。此协议类似于密钥交换的 Diffie-Hellman 方法。与 Diffie-Hellman 类似，Oakley 组密钥交换涉及密钥生成和密钥验证。Oakley 方法用于协商 PFS。  |


### 参考资料 ###
`[1].` http://docs.oracle.com/cd/E19253-01/819-7058/ike-3/<br>
<code>[2].</code> <a href='https://community.qualys.com/blogs/securitylabs/2013/06/25/ssl-labs-deploying-forward-secrecy'>https://community.qualys.com/blogs/securitylabs/2013/06/25/ssl-labs-deploying-forward-secrecy</a><br>