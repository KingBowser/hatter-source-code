/*
 * cn.aprilsoft.jsapp.io.Output.js
 * jsapp, io output functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.io
  Package("cn.aprilsoft.jsapp.io");

  Interface("cn.aprilsoft.jsapp.io.Output", Implement(),
  {
    write: Abstract(),
    
    writeln: Abstract()
  });
})();
