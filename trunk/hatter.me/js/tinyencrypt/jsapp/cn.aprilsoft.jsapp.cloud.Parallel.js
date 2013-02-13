/*
 * cn.aprilsoft.jsapp.cloud.Parallel.js
 * jsapp, cloud parallel functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.cloud
  Package("cn.aprilsoft.jsapp.cloud");
  
  Interface("cn.aprilsoft.jsapp.cloud.Parallel", Implement(),
  {
    context: Abstract() /* : context */,
    
    forItem: Abstract( /* from, to, calcFunc(item) */ ),
    
    eachItem: Abstract( /* array, calcFunc(item) */ )
  });
})();

