/*
 * cn.aprilsoft.jsapp.encode.secure.UTF8.js
 * jsapp, utf-8 functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
//+ Jonas Raoni Soares Silva
//@ http://jsfromhell.com/geral/utf-8 [rev. #1]

var UTF8 = {
    encode: function(s){
        for(var c, i = -1, l = (s = s.split("")).length, o = String.fromCharCode; ++i < l;
            s[i] = (c = s[i].charCodeAt(0)) >= 127 ? o(0xc0 | (c >>> 6)) + o(0x80 | (c & 0x3f)) : s[i]
        );
        return s.join("");
    },
    decode: function(s){
        for(var a, b, i = -1, l = (s = s.split("")).length, o = String.fromCharCode, c = "charCodeAt"; ++i < l;
            ((a = s[i][c](0)) & 0x80) &&
            (s[i] = (a & 0xfc) == 0xc0 && ((b = s[i + 1][c](0)) & 0xc0) == 0x80 ?
            o(((a & 0x03) << 6) + (b & 0x3f)) : o(128), s[++i] = "")
        );
        return s.join("");
    }
};

  // New package: cn.aprilsoft.jsapp.encode.secure
  Package("cn.aprilsoft.jsapp.encode.secure");

  Class("cn.aprilsoft.jsapp.encode.secure.UTF8", Extend(),Implement(),
  {
    encode: function(value)
    {
      return UTF8.encode(value);
    },
    
    decode: function(value)
    {
      return UTF8.decode(value);
    }
  });
})();

