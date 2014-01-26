/*
 * cn.aprilsoft.jsapp.encode.Base64Encoder.js
 * jsapp, base64 encode functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.encode
  Package("cn.aprilsoft.jsapp.encode");

  var Unicode2Ansi = Using("cn.aprilsoft.jsapp.encode.Unicode2Ansi");

  Class("cn.aprilsoft.jsapp.encode.Base64Encoder", Extend(), Implement(),
  {
    KEY_STR: Static("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="),
    
    encode64: Static(function(input)
    {
      if (input == null)
      {
        throw new Exception("base 64 encode input cannot be @null.");
      }
      var Base64Encoder = cn.aprilsoft.jsapp.encode.Base64Encoder;
      var output = "";
      var chr1, chr2, chr3 = "";
      var enc1, enc2, enc3, enc4 = "";
      var i = 0;
      
      do
      {
        chr1 = input.charCodeAt(i++);
        chr2 = input.charCodeAt(i++);
        chr3 = input.charCodeAt(i++);
        
        enc1 = chr1 >> 2;
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
        enc4 = chr3 & 63;
        
        if (isNaN(chr2))
        {
          enc3 = enc4 = 64;
        }
        else if (isNaN(chr3))
        {
          enc4 = 64;
        }
        
        output = output
               + Base64Encoder.KEY_STR.charAt(enc1)
               + Base64Encoder.KEY_STR.charAt(enc2)
               + Base64Encoder.KEY_STR.charAt(enc3)
               + Base64Encoder.KEY_STR.charAt(enc4);
        chr1 = chr2 = chr3 = "";
        enc1 = enc2 = enc3 = enc4 = "";
      } while (i < input.length);
      
      return output;
    }),
     
    decode64: Static(function(input)
    {
      if (input == null)
      {
        throw new Exception("base 64 encode input cannot be @null.");
      }
      var Base64Encoder = cn.aprilsoft.jsapp.encode.Base64Encoder;
      var output = "";
      var chr1, chr2, chr3 = "";
      var enc1, enc2, enc3, enc4 = "";
      var i = 0;
          
      if ((input.length % 4) != 0)
      {
        return "";
      }
      var base64test = /[^A-Za-z0-9\+\/\=]/g;
      if (base64test.exec(input))
      {
        return "";
      }
      
      do
      {
        enc1 = Base64Encoder.KEY_STR.indexOf(input.charAt(i++));
        enc2 = Base64Encoder.KEY_STR.indexOf(input.charAt(i++));
        enc3 = Base64Encoder.KEY_STR.indexOf(input.charAt(i++));
        enc4 = Base64Encoder.KEY_STR.indexOf(input.charAt(i++));
        
        chr1 = (enc1 << 2) | (enc2 >> 4);
        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
        chr3 = ((enc3 & 3) << 6) | enc4;
        
        output = output + String.fromCharCode(chr1);
        
         if (enc3 != 64)
         {
            output += String.fromCharCode(chr2);
         }
         if (enc4 != 64)
         {
            output += String.fromCharCode(chr3);
         }
         
         chr1 = chr2 = chr3 = "";
         enc1 = enc2 = enc3 = enc4 = "";
         
      } while (i < input.length);
      
      return output;
    }),
    
    encode64Unicode: Static(function(input)
    {
      var Base64Encoder = cn.aprilsoft.jsapp.encode.Base64Encoder;
      return Base64Encoder.encode64(Unicode2Ansi.strUnicode2Ansi(input));
    }),
    
    decode64Unicode: Static(function(input)
    {
      var Base64Encoder = cn.aprilsoft.jsapp.encode.Base64Encoder;
      return Unicode2Ansi.strAnsi2Unicode(Base64Encoder.decode64(input));
    })
  });
})();

