/*
 * cn.aprilsoft.jsapp.storage.SessionStorage.js
 * JScript, session storage functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.storage
  Package("cn.aprilsoft.jsapp.storage");
  
  var Storage = Using("cn.aprilsoft.jsapp.storage.Storage");
  
  var systemSessionStorage = sessionStorage;

  Class("cn.aprilsoft.jsapp.storage.SessionStorage", Extend(), Implement(Storage),
  {
    ready: function()
    {
      return true;
    },
    
    write: function(key, value)
    {
      systemSessionStorage[key] = value;
    },
    
    read: function(key)
    {
      return systemSessionStorage[key];
    },
    
    open: function()
    {
    },
    
    close: function()
    {
    }
  });
})();

