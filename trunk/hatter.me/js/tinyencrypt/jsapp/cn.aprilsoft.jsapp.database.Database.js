/*
 * cn.aprilsoft.jsapp.database.Database.js
 * jsapp, database functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.database
  Package("cn.aprilsoft.jsapp.database");
  
  var Closeable = Using("cn.aprilsoft.jsapp.core.Closeable");

  Interface("cn.aprilsoft.jsapp.database.Database", Implement(Closeable),
  {
    /**
     * open dtabase
     */
    open: Abstract(),
    
    /**
     * close dtabase
     */
    close: Abstract(),
    
    /**
     * execute query
     */
    execute: Abstract(/* sql, params */) /* : resultset */
  });
})();

