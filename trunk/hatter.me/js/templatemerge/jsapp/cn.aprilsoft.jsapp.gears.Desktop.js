/*
 * cn.aprilsoft.jsapp.gears.Desktop.js
 * jsapp, gears desktop functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.gears
  Package("cn.aprilsoft.jsapp.gears");
  
  var GearsHelper = Using("cn.aprilsoft.jsapp.gears.common.GearsHelper");
  
  Class("cn.aprilsoft.jsapp.gears.Desktop", Extend(), Implement(),
  {
    _desktop: null,
    
    Constructor: function()
    {
      this._desktop = GearsHelper.create(GearsHelper.DESKTOP);
    },
    
    createShortcut: function(name, url, icons, description)
    {
      this._desktop.createShortcut(name, url, icons, description);
    },
    
    openFiles: function(callback, options)
    {
      this._desktop.openFiles(callback, options);
    }
  });
})();
