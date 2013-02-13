/*
 * cn.aprilsoft.jsapp.storage.Storage.js
 * JScript, storage functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.storage
  Package("cn.aprilsoft.jsapp.storage");

  Interface("cn.aprilsoft.jsapp.storage.Storage", Implement(),
  {
    ready: Abstract() /*: boolean */,
    
    write: Abstract(/*key, value*/) /*: void*/,
    
    read: Abstract(/*key*/) /*: value*/,
    
    open: Abstract() /*: void*/,
    
    close: Abstract() /*: void*/
  });
})();

