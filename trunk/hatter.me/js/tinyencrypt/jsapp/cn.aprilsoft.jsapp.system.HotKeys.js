/*
 * cn.aprilsoft.jsapp.system.HotKeys.js
 * jsapp, system keys manager functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  Class("cn.aprilsoft.jsapp.system.HotKeys", Extend(), Implement(),
  {
    Constructor: function()
    {
    },
    
    registerKeyTo: function(domobject, keycode, keyctrl, keyalt, keyshift, keywin)
    {
      if (domobject == null)
      {
        throw new Exception("domobject must be assigned.");
      }
      if (keycode == null)
      {
        throw new Exception("keycode must be assigned.");
      }
      keyctrl  = !!keyctrl;
      keyalt   = !!keyalt;
      keyshift = !!keyshift;
      keywin   = !!keywin;
      // TODO...
    },
    
    registerSystemKey: function(keycode, keyctrl, keyalt, keyshift, keywin)
    {
      var domobject = window.body;
      this.registerKeyTo(domobject, keycode, keyctrl, keyalt, keyshift, keywin);
    }
  });
})();

