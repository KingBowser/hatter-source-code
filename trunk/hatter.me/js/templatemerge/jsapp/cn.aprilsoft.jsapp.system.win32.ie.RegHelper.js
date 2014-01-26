/*
 * cn.aprilsoft.jsapp.system.win32.ie.RegHelper.js
 * jsapp, win ie reg-helper functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system.win32.ie
  Package("cn.aprilsoft.jsapp.system.win32.ie");
  
  var WinReg = Using("cn.aprilsoft.jsapp.system.win32.WinReg");

  Static.Class("cn.aprilsoft.jsapp.system.win32.ie.RegHelper", Extend(), Implement(),
  {
    CONTEXTS_DEFAULT:   Static(0x1),
    CONTEXTS_IMAGES:    Static(0x2),
    CONTEXTS_CONTROLS:  Static(0x4),
    CONTEXTS_TABLES:    Static(0x10),
    CONTEXTS_ANCHOR:    Static(0x20),
    
    REG_SPLIT_STR: Static("\\"),
    IE_MENU_EXT_REG_CONTEXTS: Static("contexts"),
    IE_MENU_EXT_REG_PATH: Static("HKCU\\Software\\Microsoft\\Internet Explorer\\MenuExt\\"),
    
    addIEMenuExtItem: Static(function(name, command, type)
    {
      var regPath = ThisClass()._getMenuExtRegPath(name);
      WinReg.writeReg(regPath, command, WinReg.REG_SZ);
      if (type != null)
      {
        WinReg.writeReg(regPath + ThisClass().IE_MENU_EXT_REG_CONTEXTS, type, WinReg.REG_DWORD);
      }
    }),
    
    deleteIEMenuExtItem: Static(function(name)
    {
      WinReg.deleteReg(ThisClass()._getMenuExtRegPath(name));
    }),
    
    _getMenuExtRegPath: Static(function(name)
    {
      return ThisClass().IE_MENU_EXT_REG_PATH + name + ThisClass().REG_SPLIT_STR;
    })
  });
})();