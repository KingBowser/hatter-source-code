/*
 * cn.aprilsoft.jsapp.io.Input.js
 * jsapp, io input functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.io
  Package("cn.aprilsoft.jsapp.io");

  Interface("cn.aprilsoft.jsapp.io.Input", Implement(),
  {
    read: Abstract(),
    
    readln: Abstract(),
    
    readToEnd: Abstract()
  });
})();
