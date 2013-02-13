/*
 * cn.aprilsoft.jsapp.encode.secure.Encrypt.js
 * jsapp, secure encrypt functions
 * 
 * modified by Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.encode.secure
  Package("cn.aprilsoft.jsapp.encode.secure");

  Interface("cn.aprilsoft.jsapp.encode.secure.Encrypt", Implement(),
  {
    encrypt: Abstract(/* key, value */),
    
    decrypt: Abstract(/* key, value */)
  });
})();
