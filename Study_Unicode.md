http://www.unicode.org/

[Unicode Technical Reports](http://www.unicode.org/reports/)
  * UNICODE LINE BREAKING ALGORITHM - http://unicode.org/reports/tr14/


http://www.charbase.com/

Unicode的 `U+D800` `U+DFFF` 不占用字符

<br>
<code>U+010000</code> ~ <code>U+10FFFF</code> - ES6:<br>
<br>
<pre><code>\uD83D \uDCA9<br>
<br>
  ==<br>
<br>
\u{1F4A9}<br>
<br>
H = Math.floor((C - 0x10000) / 0x400) + 0xD800<br>
L = (C - 0x10000) % 0x400 + 0xDC00<br>
<br>
C = (H - 0xD800) * 0x400 + L - 0xDC00 + 0x10000<br>
</code></pre>

<ul><li><a href='https://mothereff.in/js-escapes#1%F0%9D%8C%86'>https://mothereff.in/js-escapes#1%F0%9D%8C%86</a>
</li><li><a href='https://github.com/mathiasbynens/jsesc'>https://github.com/mathiasbynens/jsesc</a></li></ul>


<br>
JavaScript’s internal character encoding: UCS-2 or UTF-16? - <a href='https://mathiasbynens.be/notes/javascript-encoding'>https://mathiasbynens.be/notes/javascript-encoding</a>