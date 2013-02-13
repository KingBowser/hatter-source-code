/*
 * cn.aprilsoft.jsapp.system.win32.Win32.js
 * jsapp, win32 functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system.win32
  Package("cn.aprilsoft.jsapp.system.win32");

  Class("cn.aprilsoft.jsapp.system.win32.Win32", Extend(), Implement(),
  {
    getCodePage: Static(function()
    {
      var tempfile = "c:\\cn_aprilsoft_jsapp_system_win32_getcodepage.tmp";
      
      var shell = new ActiveXObject("WScript.Shell");
      var fso = new ActiveXObject("Scripting.FileSystemObject");
      
      shell.Run("cmd /c chcp>\"" + tempfile + "\"", 0, true);
      var r = fso.OpenTextFile(tempfile).ReadLine().match(/[0-9]+/);
      
      while (fso.FileExists(tempfile))
      {
        try
        {
          fso.DeleteFile(tempfile);
        }catch(e){}
      }
      
      return r
    }),
    
    toUnicode: Static(function(str, codepage)
    {
      return new ActiveXObject("OlePrn.OleCvt").toUnicode(str, codepage);
    }),
    
    getGuid: Static(function()
    {
      var oTypeLib = new ActiveXObject("Scriptlet.TypeLib");
      return oTypeLib.Guid;
    })
  });
})();