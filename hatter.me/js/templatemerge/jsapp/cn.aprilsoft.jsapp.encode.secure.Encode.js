/*
 * cn.aprilsoft.jsapp.encode.secure.Encode.js
 * jsapp, encode functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.encode.secure
  Package("cn.aprilsoft.jsapp.encode.secure");

  Interface("cn.aprilsoft.jsapp.encode.secure.Encode", Implement(),
  {
    encode: Abstract(/* value */),
    
    decode: Abstract(/* value */)
  });
})();

