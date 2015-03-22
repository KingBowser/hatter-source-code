## Sample: Google Apps Setup ##

1. 创建`SPF`记录

[创建 SPF 记录](https://support.google.com/a/answer/178723?hl=zh-Hans&ref_topic=2759192)
http://www.openspf.org/SPF_Record_Syntax

创建如下：
```
openwebsecurity.org.	2285	IN	TXT	"v=spf1 include:_spf.google.com ~all"
```

查询并解释：https://dmarcian.com/spf-survey/

测试小工具：http://www.kitterman.com/spf/validate.html

超强测试工具：http://mxtoolbox.com/spf.aspx

Google IP 地址范围：https://support.google.com/a/answer/60764?hl=zh-Hans

2. 配置`DKIM`

[使用域密钥对电子邮件进行身份验证 - 启用电子邮件签名功能](https://support.google.com/a/answer/180504?hl=zh-Hans&ref_topic=2752442)

DKIM检查工具：http://protodave.com/tools/dkim-key-checker/


3. 创建`DMARC`记录

[创建 DMARC 记录](https://support.google.com/a/answer/2466563?hl=zh-Hans&ref_topic=2759254)

创建如下：
```
_dmarc.openwebsecurity.org. 3599 IN	TXT	"v=DMARC1\; p=none\; rua=mailto:hatter@openwebsecurity.org\; ruf=mailto:hatter@openwebsecurity.org"
```
查看并解释：https://dmarcian.com/dmarc-inspector/

生成`DMARC`小工具：http://kitterman.com/dmarc/assistant.html

[电子邮件身份验证](https://support.google.com/mail/answer/180707?hl=zh-Hans)
```
当您获取邮件标头之后，请查找“Authentication-Results”（身份验证结果）标头。
如果邮件成功通过 SPF 或 DKIM 身份验证，则会显示“spf=pass”或“dkim=pass”

例如：

Authentication-Results: mr.google.com; 
    spf=pass (google.com: domain of sender@gmail.com designates 10.90.20.10 as permitted sender) smtp.mail=sender@gmail.com; 
    dkim=pass header.i=sender@gmail.com
```

检查配置是否OK，发邮件到：
```
check-auth@verifier.port25.com
```



&lt;hr&gt;



  * http://www.bits.org/publications/security/BITSSecureEmailApr2007.pdf
  * http://www.bits.org/publications/security/BITSSecureBrowserRec091610.pdf
  * http://www.bits.org/publications/security/BITSSenderAuthDeployJun09.pdf
  * http://www.bits.org/publications/security/BITSEmailAuthenticationFeb2013.pdf
  * http://en.wikipedia.org/wiki/Author_Domain_Signing_Practices
  * https://leap.se/en/services/email

查看IP是否在黑名单：
  * http://www.blacklistalert.org/


邮件头分析：
  * http://mxtoolbox.com/Public/Tools/EmailHeaders.aspx
  * https://toolbox.googleapps.com/apps/messageheader/


相关RFC：
  * http://tools.ietf.org/html/rfc5321
  * http://tools.ietf.org/html/rfc6854
  * http://tools.ietf.org/html/rfc7001


ADSP:
```
$ dig +short txt _adsp._domainkey.paypal.com
"dkim=discardable"
```

<br>
Related: <a href='Study_Mail.md'>Study_Mail</a>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.trusteddomain.org/'>http://www.trusteddomain.org/</a><br>
<code>[2].</code> <a href='http://www.dkim.org/'>http://www.dkim.org/</a><br>
<code>[3].</code> <a href='http://www.opendkim.org/'>http://www.opendkim.org/</a><br>
<code>[4].</code> <a href='http://www.openspf.org/'>http://www.openspf.org/</a><br>
<code>[5].</code> <a href='http://www.dmarc.org/'>http://www.dmarc.org/</a><br>
<code>[6].</code> <a href='http://dmarc-qa.com/'>http://dmarc-qa.com/</a><br>
<code>[7].</code> <a href='http://www.techrepublic.com/blog/google-in-the-enterprise/send-better-email-configure-spf-and-dkim-for-google-apps/'>http://www.techrepublic.com/blog/google-in-the-enterprise/send-better-email-configure-spf-and-dkim-for-google-apps/</a><br>
<code>[8].</code> <a href='http://jsmtp.com/doc/index.html#!/how_to_sign_with_dkim'>http://jsmtp.com/doc/index.html#!/how_to_sign_with_dkim</a><br>
<code>[9].</code> <a href='http://emailstuff.org/authentication'>http://emailstuff.org/authentication</a><br>
<code>[10].</code> <a href='http://dkimcore.org/tools/'>http://dkimcore.org/tools/</a><br>
<code>[11].</code> <a href='http://yxcwf.wordpress.com/2011/05/30/dkim%E6%8A%80%E6%9C%AF%E8%AF%B4%E6%98%8E/'>http://yxcwf.wordpress.com/2011/05/30/dkim%E6%8A%80%E6%9C%AF%E8%AF%B4%E6%98%8E/</a><br>
<code>[12].</code> <a href='http://www.microsoft.com/senderid'>http://www.microsoft.com/senderid</a><br>
<code>[13].</code> <a href='http://internetmessagingtechnology.org/'>http://internetmessagingtechnology.org/</a><br>
<code>[14].</code> <a href='http://en.wikipedia.org/wiki/Author_Domain_Signing_Practices'>http://en.wikipedia.org/wiki/Author_Domain_Signing_Practices</a><br>
<code>[15].</code> <a href='http://en.wikipedia.org/wiki/List_of_DNS_record_types'>http://en.wikipedia.org/wiki/List_of_DNS_record_types</a><br>
<code>[16].</code> <a href='http://en.wikipedia.org/wiki/Message_transfer_agent'>http://en.wikipedia.org/wiki/Message_transfer_agent</a><br>
<code>[17].</code> <a href='http://en.wikipedia.org/wiki/Email_authentication'>http://en.wikipedia.org/wiki/Email_authentication</a><br>