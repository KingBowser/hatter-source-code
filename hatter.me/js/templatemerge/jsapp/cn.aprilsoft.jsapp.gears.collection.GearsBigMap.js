/*
 * cn.aprilsoft.jsapp.gears.collection.GearsBigMap.js
 * jsapp, gears big map functions
 * 
 * Copyright(C) Hatter Jiang
 */
 
(function()
{
  // New package: cn.aprilsoft.jsapp.gears.collection
  Package("cn.aprilsoft.jsapp.gears.collection");
  
  var Destroyable = Using("cn.aprilsoft.jsapp.core.Destroyable");
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Serializer = Using("cn.aprilsoft.jsapp.serialize.Serializer");
  var GearsHelper = Using("cn.aprilsoft.jsapp.gears.common.GearsHelper");
  var GearsDatabase = Using("cn.aprilsoft.jsapp.gears.Database");

  Class("cn.aprilsoft.jsapp.collection.gears.GearsBigMap", Extend(), Implement(Destroyable),
  {
    CREATE_TABLE_SQL: Static("create table if not exists $tablename (key text PRIMARY KEY, value text)"),
    _globalTableIndex: Static(0),
    _tableName: null,
    _database: null,
    _serializer: null,
    
    Constructor: function(serializer)
    {
      if (serializer == null)
      {
        throw new Exception("Serializer cannot be null.");
      }
      if (!(serializer.InstanceOf(Serializer)))
      {
        throw new Exception("Serializer must implements Serializer interface.");
      }
      this._serializer = serializer;
      
      this._tableName = this._getTableName();
      this._database = new GearsDatabase(ThisClass().getClassName());
      this._database.open();
      this._database.execute(this._replaceSql(ThisClass().CREATE_TABLE_SQL));
      this.clear();
      
      System.invokeOn("obj, arr", function(obj, arr)
      {
        this.add.apply(this, arr);
      }, this);
    },
    
    put: function(key, value)
    {
      if (this.contains(key))
      {
        this._executeSql("update $tablename set value = ? where key = ?", [value, key]);
      }
      else
      {
        this._executeSql("insert into $tablename (key, value) values (?, ?)"
                        , [key, this._serializer.serialize(value)]);
      }
    },
    
    get: function(key)
    {
      return this._serializer.deserialize(this._getSqlValue("select value from $tablename where key = ?", [key], null));
    },
    
    contains: function(key)
    {
      return (this._getSqlValue("select count(*) from $tablename where key = ?", [key], 0) > 0);
    },
    
    remove: function(key)
    {
      this._executeSql("delete from $tablename where key = ?", [key]);
    },
    
    keyList: function()
    {
      return this._getSqlValueList("select key from $tablename");
    },
    
    clear: function()
    {
      this._executeSql("delete from $tablename");
    },
    
    destory: function()
    {
      this.clear();
      this._database.close();
    },
    
    _executeSql: function(sql, params)
    {
      this._database.execute(this._replaceSql(sql), params);
    },
    
    _getSqlValueList: function(rs)
    {
      var result = [];
      while (rs.isValidRow())
      {
        result.push(this._serializer.deserialize(rs.field(0)));
        rs.next();
      }
      return result;
    },
    
    _getSqlValue: function(sql, params, defaultValue)
    {
      var result;
      var rs = this._database.execute(this._replaceSql(sql), params);
      System.using(rs, (function(rs)
      {
        if (rs.isValidRow())
        {
          result = rs.field(0);
        }
        else
        {
          result = defaultValue;
        }
      }).bind(this));
      return result;
    },
    
    _replaceSql: function(sql)
    {
      return sql.replace(/\$tablename/g, this._tableName);
    },
    
    _getTableName: function()
    {
      return ThisClass().getShortClassName() + "_" + (ThisClass()._globalTableIndex++);
    }
  });
})();
