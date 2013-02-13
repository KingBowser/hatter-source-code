/*
 * cn.aprilsoft.jsapp.serialize.Serializer.js
 * jsapp, serialize functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.serialize
  Package("cn.aprilsoft.jsapp.serialize");

  Interface("cn.aprilsoft.jsapp.serialize.Serializer", Implement(),
  {
    serialize: Abstract(),
    
    deserialize: Abstract()
  });
})();

