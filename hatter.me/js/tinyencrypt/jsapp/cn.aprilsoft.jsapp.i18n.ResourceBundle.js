/*
 * cn.aprilsoft.jsapp.i18n.ResourceBundle.js
 * jsapp, resource bundle functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.i18n
  Package("cn.aprilsoft.jsapp.i18n");

  Interface("cn.aprilsoft.jsapp.i18n.ResourceBundle", Extend(), Implement(),
  {
    getLocal: Abstract(),
    
    getMessage: Abstract()
  });
})();

