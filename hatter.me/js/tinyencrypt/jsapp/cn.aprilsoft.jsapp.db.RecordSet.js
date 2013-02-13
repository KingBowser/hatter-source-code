/*
 * cn.aprilsoft.jsapp.db.RecordSet.js
 * jsapp, database record set functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.db
  Package("cn.aprilsoft.jsapp.db");
  
  var Connection = Using("cn.aprilsoft.jsapp.db.Connection");

  Class("cn.aprilsoft.jsapp.db.RecordSet", Extend(), Implement(),
  {
    _recordSet: Private(null),
    
    _connection: Private(null),
    
    Constructor: function(connection)
    {
      if (connection == null)
      {
        throw new Exception("Connection cannot be null.");
      }
      if (!(connection instanceof Connection))
      {
        throw new Exception("A instance of Connection is needed.");
      }
      this._recordSet = new ActiveXObject("ADODB.Recordset");
      this._connection = connection;
    },
    
    open: function(sql, x, y)
    {
      if ((sql == null) || (sql == ""))
      {
        throw new Exception("Sql should not be empty.");
      }
      var _x = (x == null)? 1: x;
      var _y = (y == null)? 1: y;
      return this._recordSet.Open(sql, this._connection.getConnection(), _x, _y);
    },
    
    close: function()
    {
      return this._recordSet.Close();
    },
    
    getFields: function()
    {
      var fields = [];
      var enumer = new Enumerator(this._recordSet.Fields);
      for (; !enumer.atEnd(); enumer.moveNext())
      {
        fields.push(enumer.item());
      }
      return fields;
    }
  });
})();

