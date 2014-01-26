/*
 * cn.aprilsoft.jsapp.ui.SelectFolder.js
 * jsapp, ui select folder functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  Class("cn.aprilsoft.jsapp.ui.SelectFolder", Extend(), Implement(),
  {
    selectFolder: Static(function(title, location)
    {
      var shellApp = new ActiveXObject("Shell.Application");
      var bff = shellApp.BrowseForFolder(0, title, 0, location);
      
      if (bff != null)
      {
        return bff.Items().Item().Path;
      }
      else
      {
        return null;
      }
    })
  });
})();
