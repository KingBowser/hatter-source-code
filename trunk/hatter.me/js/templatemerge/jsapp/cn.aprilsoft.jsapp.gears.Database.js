/*
 * cn.aprilsoft.jsapp.gears.Database.js
 * jsapp, gears database functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.gears
  Package("cn.aprilsoft.jsapp.gears");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Database = Using("cn.aprilsoft.jsapp.database.Database");
  var GearsHelper = Using("cn.aprilsoft.jsapp.gears.common.GearsHelper");
  
  Class("cn.aprilsoft.jsapp.gears.Database", Extend(), Implement(Database),
  {
    _database: null,
    _databaseName: null,
    
    transaction: null,
    
    Constructor: function(strDatabaseName)
    {
      System.checkArguments();
      this._databaseName = strDatabaseName;
      this._database = GearsHelper.create(GearsHelper.DATABASE);
      
      this.transaction = 
      {
        begin: this.beginTransaction.bind(this),
        commit: this.commitTransaction.bind(this),
        rollback: this.rollbackTransaction.bind(this)
      };
    },
    
    open: function()
    {
      this._database.open(this._databaseName);
    },
    
    close: function()
    {
      this._database.close();
    },
    
    remove: function()
    {
      this._database.remove();
    },
    
    execute: function(sql, arrArg)
    {
      return this._database.execute(sql, arrArg);
    },
    
    beginTransaction: function()
    {
      this.execute("BEGIN");
    },
    
    commitTransaction: function()
    {
      this.execute("COMMIT");
    },
    
    rollbackTransaction: function()
    {
      this.execute("ROLLBACK");
    },
    
    getLastInsertRowId: function()
    {
      return this._database.lastInsertRowId;
    },
    
    getRowsAffected: function()
    {
      return this._database.rowsAffected;
    },
    
    asString: function()
    {
      return ThisClass().getShortClassName() + " databaseName: " + this._databaseName;
    }
  });
})();
