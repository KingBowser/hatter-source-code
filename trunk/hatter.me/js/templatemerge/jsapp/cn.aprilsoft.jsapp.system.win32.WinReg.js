/*
 * cn.aprilsoft.jsapp.system.win32.WinReg.js
 * jsapp, win reg functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system.win32
  Package("cn.aprilsoft.jsapp.system.win32");

  Static.Class("cn.aprilsoft.jsapp.system.win32.WinReg", Extend(), Implement(),
  {
    HKCU:                 Static("HKCU"),             // HKEY_CURRENT_USER
    HKLM:                 Static("HKLM"),             // HKEY_LOCAL_MACHINE
    HKCR:                 Static("HKCR"),             // HKEY_CLASSES_ROOT
    HKEY_USERS:           Static("HKEY_USERS"),        // HKEY_USERS
    HKEY_CURRENT_CONFIG:  Static("HKEY_CURRENT_CONFIG"), // HKEY_CURRENT_CONFIG
    
    REG_SZ:         Static("REG_SZ"),       // string
    REG_DWORD:      Static("REG_DWORD"),    // number
    REG_BINARY:     Static("REG_BINARY"),   // binary
    REG_EXPAND_SZ:  Static("REG_EXPAND_SZ"), // expandable string (i.e. %windir%\\calc.exe)
    
    _getShell: Static(function()
    {
      return new ActiveXObject("WScript.Shell");
    }),
    
    writeReg: Static(function(key, value, type)
    {
      return ThisClass()._getShell().RegWrite(key, value, type);
    }),
    
    readReg: Static(function(key)
    {
      return ThisClass()._getShell().RegRead(key);
    }),
    
    deleteReg: Static(function(key)
    {
      return ThisClass()._getShell().RegDelete(key);
    })
  });
})();