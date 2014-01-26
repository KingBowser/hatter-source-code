/*
 * cn.aprilsoft.jsapp.gears.collection.GearsBigList.js
 * jsapp, gears big list functions
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

  Class("cn.aprilsoft.jsapp.collection.gears.GearsBigList", Extend(), Implement(Destroyable),
  {
    CREATE_TABLE_SQL: Static("create table if not exists $tablename (id int PRIMARY KEY, value text)"),
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
    
    size: function()
    {
      return this._getSqlValue("select count(*) from $tablename", null, 0);
    },
    
    add: function(/* arguments */)
    {
      for (var i = 0; i < arguments.length; i++)
      {
        this._addOne(arguments[i]);
      }
    },
    
    push: function(/* arguments */)
    {
      this.add.apply(this, arguments);
    },
    
    unshift: function(/* arguments */)
    {
      var unshiftCount = arguments.length;
      if (unshiftCount > 0)
      {
        this._database.transaction.begin();
        try
        {
          this._executeSql("update $tablename set id = id + ?", [unshiftCount]);
          for (var i = 0; i < arguments.length; i++)
          {
            this._executeSql("insert into $tablename (id, value) values (?, ?)"
                            , [i, this._serializer.serialize(arguments[i])]);
          }
          this._database.transaction.commit();
        }
        catch(e)
        {
          this._database.transaction.rollback();
          throw NewException(e);
        }
      }
    },
    
    remove: function(index)
    {
      this._database.transaction.begin();
      try
      {
        var object = this.get(index);
        this._executeSql("delete from $tablename where id = ?", [index]);
        this._executeSql("update $tablename set id = id - 1 where id > ?", [index]);
        this._database.transaction.commit();
        return object;
      }
      catch(e)
      {
        this._database.transaction.rollback();
        throw NewException(e);
      }
    },
    
    get: function(index)
    {
      return this._serializer.deserialize(this._getSqlValue("select value from $tablename where id = ?", [index], null));
    },
    
    shift: function()
    {
      return this.remove(0);
    },
    
    pop: function()
    {
      return this.remove(this.size() - 0);
    },
    
    toArray: function()
    {
      var array
      var rs = this._database.execute(this._replaceSql("select value from $tablename order by id asc"));
      System.using(rs, (function(rs)
      {
        array = this._getSqlValueList(rs);
      }).bind(this));
      return array;
    },
    
    getRange: function(from, to)
    {
      var array;
      var rs = this._database.execute(this._replaceSql(
                                      "select value from $tablename where id >= from and id <= to order by id asc"
                                      , [from, to]));
      System.using(rs, (function(rs)
      {
        array = this._getSqlValueList(rs);
      }).bind(this));
      return array;
    },
    
    join: function(str)
    {
      return this.toArray().join(str);
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
    
    _addOne: function(item)
    {
      this._executeSql("insert into $tablename (id, value) values ((select count(*) from $tablename), ?)"
                      , [this._serializer.serialize(item)]);
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
    },
    
    asString: function()
    {
      try
      {
        return ThisClass().getShortClassName() + " site=" + this.size();
      }
      catch(e)
      {
        return "Error in GearsBigList.asString()";
      }
    }
  });
})();
