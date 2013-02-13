/*
 * cn.aprilsoft.jsapp.orm.core.ORMAccessor.js
 * jsapp, orm orm-accessor functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.orm.core
  Package("cn.aprilsoft.jsapp.orm.core");
  
  var Destroyable = Using("cn.aprilsoft.jsapp.core.Destroyable");
  var Database = Using("cn.aprilsoft.jsapp.database.Database");
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var ORMParser = Using("cn.aprilsoft.jsapp.orm.core.parser.ORMParser");
  var ORMSqlGenerator = Using("cn.aprilsoft.jsapp.orm.core.sql.ORMSqlGenerator");
  var ORMObjectMapping = Using("cn.aprilsoft.jsapp.orm.core.mapping.ORMObjectMapping");
  
  Class("cn.aprilsoft.jsapp.orm.core.ORMAccessor", Extend(), Implement(Destroyable),
  {
    _database: null,
    _sqlGenerator: null,
    
    Constructor: function(database, sqlGenerator)
    {
      if (!InstanceOf(database, Database))
      {
        throw new Exception("Database should implements Database interface.");
      }
      if (!Implements(sqlGenerator, ORMSqlGenerator))
      {
        throw new Exception("Sql generator should implements ORMSqlGenerator interface.");
      }
      this._database = database;
      this._sqlGenerator = sqlGenerator;
      this._database.open();
    },
    
    createTableIfNotExists: function(clazzOrmObject)
    {
      var query = this._sqlGenerator.newInstance(clazzOrmObject).createQuery();
      this._database.execute(query.query);
    },
    
    insertObject: function(ormObject)
    {
    },
    
    updateObject: function(ormObject)
    {
    },
    
    findObject: function(ormObject)
    {
    },
    
    deleteObject: function(ormObject)
    {
    },
    
    selectObject: function(clazzOrmObject, strKeys, arrValues)
    {
      return this._getSingleResult(this.listObject(clazzOrmObject, strKeys, arrValues));
    },
    
    selectObjectByQuery: function(clazzOrmObject, strKeys, arrValues)
    {
      return this._getSingleResult(this.listObjectByQuery(clazzOrmObject, strKeys, arrValues));
    },
    
    listObject: function(clazzOrmObject, strKeys, arrValues)
    {
      if (strKeys == null)
      {
        if (arrValues != null)
        {
          throw new Exception("Keys is null but values is not null.");
        }
        return this.listObjectByQuery(clazzOrmObject);
      }
      var arrKeys = strKeys.split(/\s*\,\s*/);
      
      if (arrKeys.length != arrValues.length)
      {
        throw new Exception("Keys and values count does not match.");
      }
      var queryBuffer = [];
      for (var i = 0; i < arrKeys.length; i++)
      {
        queryBuffer.push(arrKeys[i] + " = ?");
      }
      var query = queryBuffer.join(", ");
      return this.listObjectByQuery(clazzOrmObject, query, arrValues);
    },
    
    listObjectByQuery: function(clazzOrmObject, strQuery, arrValues)
    {
      var ormObjectInstance = clazzOrmObject.newInstance();
      var table = ormObjectInstance.orm_table();
      var description = ormObjectInstance.orm_description();
      
      var sql = "select * from " + table + " where " + ((strQuery == null)? "1 = 1": strQuery);
      var mapping = new ORMObjectMapping(clazzOrmObject);
      var rows = [];
      var rs = this._database.execute(sql, arrValues);
      System.using(rs, function(rs)
      {
        while (rs.isValidRow())
        {
          rows.push(mapping.fromObject(rs));
          rs.next();
        }
      });
      return rows;
    },
    
    destroy: function()
    {
      this._database.close();
    },
    
    _getSingleResult: function(resultList)
    {
      if (resultList == null)
      {
        return null;
      }
      if (resultList.length > 1)
      {
        throw new Exception("Accept one result, but returns more than 1 results.");
      }
      if (resultList.length == 0)
      {
        return null;
      }
      return resultList[0];
    }
  });
})();

