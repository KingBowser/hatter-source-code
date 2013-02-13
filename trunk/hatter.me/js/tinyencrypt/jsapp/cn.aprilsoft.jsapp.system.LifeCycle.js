/*
 * cn.aprilsoft.jsapp.system.LifeCycle.js
 * jsapp, life cycle functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  Interface("cn.aprilsoft.jsapp.system.LifeCycle", Implement(),
  {
    create: Abstract(),
    
    destory: Abstract()
  });
})();

