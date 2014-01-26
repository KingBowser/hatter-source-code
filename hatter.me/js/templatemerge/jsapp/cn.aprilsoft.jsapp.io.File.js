/*
 * cn.aprilsoft.jsapp.io.File.js
 * jsapp, io file functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.io
  Package("cn.aprilsoft.jsapp.io");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.io.File", Extend(), Implement(),
  {
    Constructor: function()
    {
      System.invokeOn("str", function(file)
      {
      });
      System.invokeOn(ThisClass(), function(file)
      {
      });
    },
    
    exists: function()
    {
    },
    
    isDirectory: function()
    {
    },
    
    listFiles: function()
    {
    },
    
    listFolders: function()
    {
    },
    
    remove: function()
    {
    }
  });
})();
