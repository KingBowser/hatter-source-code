/*
 * cn.aprilsoft.jsapp.db.Connection.js
 * jsapp, database connction functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.db
  Package("cn.aprilsoft.jsapp.db");

  Class("cn.aprilsoft.jsapp.db.Connection", Extend(), Implement(),
  {
    _connection: Private(null),
    
    _connDsn: Private(null),
    
    Constructor: function(connDsn)
    {
      if (connDsn == null)
      {
        throw new Exception("Connection DSN cannot be null.");
      }
      this._connection = new ActiveXObject("ADODB.Connection");
      this._connDsn = connDsn;
    },
    
    open: function()
    {
      return this._connection.Open(this._connDsn.getDsn());
    },
    
    close: function()
    {
      if (this._connection != null)
      {
        return this._connection.Close();
      }
    },
    
    getConnection: function()
    {
      return this._connection;
    },
    
    execute: function(sql)
    {
      if ((sql == null) || (sql == ""))
      {
        throw new Exception("Sql cannot be empty.");
      }
      return this._connection.Execute(sql);
    }
  });
})();

