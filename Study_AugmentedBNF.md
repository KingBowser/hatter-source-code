# Augmented Backus-Naur Form (BNF) #

  * `name = definition`
  * `"literal"`
> 用双引号包含越来的字符串。除非特别说明，默认情况下不区分大小写
  * `rule1 | rule2`
> 以竖线“`|`”分割的二选一（或多选一）元素。例如：“`yes | no`”则表示“`yes`”和“`no`”都可被接受
  * `(rule1 rule2)`
  * `*rule`
  * `[rule]`
> 方括号括越来表示可选，例如“`[foo bar]`”与“`*1(foo bar)`”等价
  * `N rule`
> 特定的重复次数，例如“`<n>(element)`”与“`<n>*<n>(element)`”等价
  * `#rule`
  * `; comment`
> 注释
  * `implied *LWS`

```
       OCTET          = <any 8-bit sequence of data>
       CHAR           = <any US-ASCII character (octets 0 - 127)>
       UPALPHA        = <any US-ASCII uppercase letter "A".."Z">
       LOALPHA        = <any US-ASCII lowercase letter "a".."z">
       ALPHA          = UPALPHA | LOALPHA
       DIGIT          = <any US-ASCII digit "0".."9">
       CTL            = <any US-ASCII control character
                        (octets 0 - 31) and DEL (127)>
       CR             = <US-ASCII CR, carriage return (13)>
       LF             = <US-ASCII LF, linefeed (10)>
       SP             = <US-ASCII SP, space (32)>
       HT             = <US-ASCII HT, horizontal-tab (9)>
       <">            = <US-ASCII double-quote mark (34)>
```

```
       CRLF           = CR LF

       LWS            = [CRLF] 1*( SP | HT )

       TEXT           = <any OCTET except CTLs,
                           but including LWS>

       HEX            = "A" | "B" | "C" | "D" | "E" | "F"
                      | "a" | "b" | "c" | "d" | "e" | "f" | DIGIT

       token          = 1*<any CHAR except CTLs or separators>
       separators     = "(" | ")" | "<" | ">" | "@"
                      | "," | ";" | ":" | "\" | <">
                      | "/" | "[" | "]" | "?" | "="
                      | "{" | "}" | SP | HT

       comment        = "(" *( ctext | quoted-pair | comment ) ")"
       ctext          = <any TEXT excluding "(" and ")">

       quoted-string  = ( <"> *(qdtext | quoted-pair ) <"> )
       qdtext         = <any TEXT except <">>

       quoted-pair    = "\" CHAR
```

### See also ###
  * `STANDARD FOR THE FORMAT OF ARPA INTERNET TEXT MESSAGES`
> http://www.ietf.org/rfc/rfc822.txt


### 参考资料 ###
`[1].` http://www.ietf.org/rfc/rfc2616.txt<br>
<code>[2].</code> <a href='http://blog.chinaunix.net/uid-20350981-id-1701057.html'>http://blog.chinaunix.net/uid-20350981-id-1701057.html</a><br>