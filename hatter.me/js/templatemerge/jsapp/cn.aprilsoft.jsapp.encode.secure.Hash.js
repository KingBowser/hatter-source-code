/*
 * cn.aprilsoft.jsapp.encode.secure.Hash.js
 * jsapp, hash encode functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.encode.secure
  Package("cn.aprilsoft.jsapp.encode.secure");

  Interface("cn.aprilsoft.jsapp.encode.secure.Hash", Implement(),
  {
    hash: Abstract(/* value */)
  });
})();

