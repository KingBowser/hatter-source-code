/*
 * cn.aprilsoft.jsapp.system.ClipBoard.js
 * jsapp, windows clip board functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  Class("cn.aprilsoft.jsapp.system.ClipBoard", Extend(), Implement(),
  {
    getTextData: Static(function()
    {
      return window.clipboardData.getData ('text');
    }),
    
    setTextData: Static(function(strText)
    {
      window.clipboardData.setData ('text', strText);
    })
  });
})();

