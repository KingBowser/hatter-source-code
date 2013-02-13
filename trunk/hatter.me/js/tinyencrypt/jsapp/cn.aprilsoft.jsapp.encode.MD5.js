/**********************************************************
* md5.js
*
* A JavaScript implementation of the RSA Data Security, Inc. MD5
* Message-Digest Algorithm.
*
* Copyright (C) Paul Johnston 1999. Distributed under the LGPL.
***********************************************************/
/*
 * cn.aprilsoft.jsapp.encode.MD5.js
 * jsapp, md5 encode functions
 * modified by Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.encode
  Package("cn.aprilsoft.jsapp.encode");

  Class("cn.aprilsoft.jsapp.encode.MD5", Extend(), Implement(),
  {
    /* to convert strings to a list of ascii values */
    sAscii: Static(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"),

    /* convert integer to hex string */
    sHex: Static("0123456789ABCDEF"),

    hex: Static(function(i)
    {
      var MD5 = cn.aprilsoft.jsapp.encode.MD5;
      var h = "";
      for(var j = 0; j <= 3; j++)
      {
        h += MD5.sHex.charAt((i >> (j * 8 + 4)) & 0x0F) +
        MD5.sHex.charAt((i >> (j * 8)) & 0x0F);
      }
      return h;
    }),

    /* add, handling overflows correctly */
    add: Static(function(x, y)
    {
      return ((x & 0x7FFFFFFF) + (y & 0x7FFFFFFF)) ^ (x & 0x80000000) ^ (y & 0x80000000);
    }),

    /* MD5 rounds functions */
    R1: Static(function(A, B, C, D, X, S, T)
    {
      var MD5 = cn.aprilsoft.jsapp.encode.MD5;
      var q = MD5.add(MD5.add(A, (B & C) | (~B & D)), MD5.add(X, T));
      return MD5.add((q << S) | ((q >> (32 - S)) & ( Math.pow(2, S) - 1)), B);
    }),

    R2: Static(function(A, B, C, D, X, S, T)
    {
      var MD5 = cn.aprilsoft.jsapp.encode.MD5;
      var q = MD5.add(MD5.add(A, (B & D) | (C & ~D)), MD5.add(X, T));
      return MD5.add((q << S) | ((q >> (32 - S)) & ( Math.pow(2, S) - 1)), B);
    }),

    R3: Static(function(A, B, C, D, X, S, T)
    {
      var MD5 = cn.aprilsoft.jsapp.encode.MD5;
      var q = MD5.add(MD5.add(A, B ^ C ^ D), MD5.add(X, T));
      return MD5.add((q << S) | ((q >> (32 - S)) & (Math.pow (2, S) - 1)), B);
    }),

    R4: Static(function(A, B, C, D, X, S, T)
    {
      var MD5 = cn.aprilsoft.jsapp.encode.MD5;
      var q = MD5.add(MD5.add(A, C ^ (B | ~D)), MD5.add(X, T));
      return MD5.add((q << S) | ((q >> (32 - S)) & (Math.pow (2, S) - 1)), B);
    }),

    /* main entry point */
    calcMD5: Static(function(sInp) {

      var MD5 = cn.aprilsoft.jsapp.encode.MD5;
      
      /* Calculate length in machine words, including padding */
      var wLen = (((sInp.length + 8) >> 6) + 1) << 4;
      var X = new Array(wLen);
      
      /* Convert string to array of words */
      var j = 4;
      for (var i = 0; (i * 4) < sInp.length ; i++)
      {
        X[i] = 0;
        for (j = 0; (j < 4) && ((j + i * 4) < sInp.length); j++)
        {
          X[i] += (MD5.sAscii.indexOf(sInp.charAt((i * 4) + j)) + 32) << (j * 8);
        }
      }
      
      /* Append padding bits and length */
      if (j == 4)
      {
        X[i++] = 0x80;
      }
      else
      {
        X[i - 1] += 0x80 << (j * 8);
      }
      for(; i < wLen; i++)
      {
        X[i] = 0;
      }
      X[wLen - 2] = sInp.length * 8;
      
      /* hard-coded initial values */
      var a = 0x67452301;
      var b = 0xefcdab89;
      var c = 0x98badcfe;
      var d = 0x10325476;
      
      /* Process each 16-word block in turn */
      for (var i = 0; i < wLen; i += 16)
      {
        var aO = a;
        var bO = b;
        var cO = c;
        var dO = d;
        
        a = MD5.R1(a, b, c, d, X[i+ 0], 7 , 0xd76aa478);
        d = MD5.R1(d, a, b, c, X[i+ 1], 12, 0xe8c7b756);
        c = MD5.R1(c, d, a, b, X[i+ 2], 17, 0x242070db);
        b = MD5.R1(b, c, d, a, X[i+ 3], 22, 0xc1bdceee);
        a = MD5.R1(a, b, c, d, X[i+ 4], 7 , 0xf57c0faf);
        d = MD5.R1(d, a, b, c, X[i+ 5], 12, 0x4787c62a);
        c = MD5.R1(c, d, a, b, X[i+ 6], 17, 0xa8304613);
        b = MD5.R1(b, c, d, a, X[i+ 7], 22, 0xfd469501);
        a = MD5.R1(a, b, c, d, X[i+ 8], 7 , 0x698098d8);
        d = MD5.R1(d, a, b, c, X[i+ 9], 12, 0x8b44f7af);
        c = MD5.R1(c, d, a, b, X[i+10], 17, 0xffff5bb1);
        b = MD5.R1(b, c, d, a, X[i+11], 22, 0x895cd7be);
        a = MD5.R1(a, b, c, d, X[i+12], 7 , 0x6b901122);
        d = MD5.R1(d, a, b, c, X[i+13], 12, 0xfd987193);
        c = MD5.R1(c, d, a, b, X[i+14], 17, 0xa679438e);
        b = MD5.R1(b, c, d, a, X[i+15], 22, 0x49b40821);
        
        a = MD5.R2(a, b, c, d, X[i+ 1], 5 , 0xf61e2562);
        d = MD5.R2(d, a, b, c, X[i+ 6], 9 , 0xc040b340);
        c = MD5.R2(c, d, a, b, X[i+11], 14, 0x265e5a51);
        b = MD5.R2(b, c, d, a, X[i+ 0], 20, 0xe9b6c7aa);
        a = MD5.R2(a, b, c, d, X[i+ 5], 5 , 0xd62f105d);
        d = MD5.R2(d, a, b, c, X[i+10], 9 , 0x2441453);
        c = MD5.R2(c, d, a, b, X[i+15], 14, 0xd8a1e681);
        b = MD5.R2(b, c, d, a, X[i+ 4], 20, 0xe7d3fbc8);
        a = MD5.R2(a, b, c, d, X[i+ 9], 5 , 0x21e1cde6);
        d = MD5.R2(d, a, b, c, X[i+14], 9 , 0xc33707d6);
        c = MD5.R2(c, d, a, b, X[i+ 3], 14, 0xf4d50d87);
        b = MD5.R2(b, c, d, a, X[i+ 8], 20, 0x455a14ed);
        a = MD5.R2(a, b, c, d, X[i+13], 5 , 0xa9e3e905);
        d = MD5.R2(d, a, b, c, X[i+ 2], 9 , 0xfcefa3f8);
        c = MD5.R2(c, d, a, b, X[i+ 7], 14, 0x676f02d9);
        b = MD5.R2(b, c, d, a, X[i+12], 20, 0x8d2a4c8a);
        
        a = MD5.R3(a, b, c, d, X[i+ 5], 4 , 0xfffa3942);
        d = MD5.R3(d, a, b, c, X[i+ 8], 11, 0x8771f681);
        c = MD5.R3(c, d, a, b, X[i+11], 16, 0x6d9d6122);
        b = MD5.R3(b, c, d, a, X[i+14], 23, 0xfde5380c);
        a = MD5.R3(a, b, c, d, X[i+ 1], 4 , 0xa4beea44);
        d = MD5.R3(d, a, b, c, X[i+ 4], 11, 0x4bdecfa9);
        c = MD5.R3(c, d, a, b, X[i+ 7], 16, 0xf6bb4b60);
        b = MD5.R3(b, c, d, a, X[i+10], 23, 0xbebfbc70);
        a = MD5.R3(a, b, c, d, X[i+13], 4 , 0x289b7ec6);
        d = MD5.R3(d, a, b, c, X[i+ 0], 11, 0xeaa127fa);
        c = MD5.R3(c, d, a, b, X[i+ 3], 16, 0xd4ef3085);
        b = MD5.R3(b, c, d, a, X[i+ 6], 23, 0x4881d05);
        a = MD5.R3(a, b, c, d, X[i+ 9], 4 , 0xd9d4d039);
        d = MD5.R3(d, a, b, c, X[i+12], 11, 0xe6db99e5);
        c = MD5.R3(c, d, a, b, X[i+15], 16, 0x1fa27cf8);
        b = MD5.R3(b, c, d, a, X[i+ 2], 23, 0xc4ac5665);

        a = MD5.R4(a, b, c, d, X[i+ 0], 6 , 0xf4292244);
        d = MD5.R4(d, a, b, c, X[i+ 7], 10, 0x432aff97);
        c = MD5.R4(c, d, a, b, X[i+14], 15, 0xab9423a7);
        b = MD5.R4(b, c, d, a, X[i+ 5], 21, 0xfc93a039);
        a = MD5.R4(a, b, c, d, X[i+12], 6 , 0x655b59c3);
        d = MD5.R4(d, a, b, c, X[i+ 3], 10, 0x8f0ccc92);
        c = MD5.R4(c, d, a, b, X[i+10], 15, 0xffeff47d);
        b = MD5.R4(b, c, d, a, X[i+ 1], 21, 0x85845dd1);
        a = MD5.R4(a, b, c, d, X[i+ 8], 6 , 0x6fa87e4f);
        d = MD5.R4(d, a, b, c, X[i+15], 10, 0xfe2ce6e0);
        c = MD5.R4(c, d, a, b, X[i+ 6], 15, 0xa3014314);
        b = MD5.R4(b, c, d, a, X[i+13], 21, 0x4e0811a1);
        a = MD5.R4(a, b, c, d, X[i+ 4], 6 , 0xf7537e82);
        d = MD5.R4(d, a, b, c, X[i+11], 10, 0xbd3af235);
        c = MD5.R4(c, d, a, b, X[i+ 2], 15, 0x2ad7d2bb);
        b = MD5.R4(b, c, d, a, X[i+ 9], 21, 0xeb86d391);
        
        a = MD5.add(a, aO);
        b = MD5.add(b, bO);
        c = MD5.add(c, cO);
        d = MD5.add(d, dO);
      }
      return MD5.hex(a) + MD5.hex(b) + MD5.hex(c) + MD5.hex(d);
    })
  });
})();

