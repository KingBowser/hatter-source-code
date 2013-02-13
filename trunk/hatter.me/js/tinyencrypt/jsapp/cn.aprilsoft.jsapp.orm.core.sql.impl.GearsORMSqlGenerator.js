/*
 * cn.aprilsoft.jsapp.orm.core.sql.impl.GearsORMSqlGenerator.js
 * jsapp, orm orm-sql-generator functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.orm.core.sql.impl
  Package("cn.aprilsoft.jsapp.orm.core.sql.impl");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var ORMObject = Using("cn.aprilsoft.jsapp.orm.ORMObject");
  var ORMParser = Using("cn.aprilsoft.jsapp.orm.core.parser.ORMParser");
  var ORMSqlGenerator = Using("cn.aprilsoft.jsapp.orm.core.sql.ORMSqlGenerator");
  var ORMSqlGeneratorQuery = Using("cn.aprilsoft.jsapp.orm.core.sql.ORMSqlGenerator$Query");
  
  Class("cn.aprilsoft.jsapp.orm.core.sql.impl.GearsORMSqlGenerator", Extend(), Implement(ORMSqlGenerator),
  {
    _classObject: null,
    _ormParser: null,
    _classObjectInstance: null,
    
    Constructor: function(classObject)
    {
      if (!Implements(classObject, ORMObject))
      {
        throw new Exception("Class object must implemnts ORMObject.");
      }
      this._classObject = classObject;
      this._ormParser = new ORMParser(classObject);
      this._classObjectInstance = classObject.newInstance();
    },
    
    createQuery: function()
    {
      var query = [];
      var keys = [];
      query.push("create table if not exists " + this._ormParser.getTable() + " ");
      var queryFields = [];
      var fields = this._ormParser.getFields();
      for (var i = 0; i < fields.length; i++)
      {
        var field = fields[i];
        queryFields.push(field.dbField + " " + this._translateDbType(field.dbType)
                       + this._getDbConstraint(field.dbConstraint));
      }
      query.push("(" + queryFields.join(", ") + ")");
      return new ORMSqlGeneratorQuery(query.join(""), keys);
    },
    
    insertQuery: function()
    {
      var query = [];
      var keys = [];
      var questionMarks = [];
      query.push("insert into " + this._ormParser.getTable() + " ");
      var fields = this._ormParser.getFields();
      for (var i = 0; i < fields.length; i++)
      {
        keys.push(fields[i].dbField);
        questionMarks.push("?");
      }
      query.push("(" + keys.join(", ") + ")");
      query.push(" values ");
      query.push("(" + questionMarks.join(", ") + ")");
      return new ORMSqlGeneratorQuery(query.join(""), keys);
    },
    
    updateQuery: function()
    {
      var query = [];
      var keys = [];
      var whereKeys = [];
      var whereKeySetValueQuestionMarks = [];
      var setValueKeys = [];
      var setValueKeySetValueQuestionMarks = [];
      query.push("update " + this._ormParser.getTable() + " set ");
      var fields = this._ormParser.getFields();
      for (var i = 0; i < fields.length; i++)
      {
        var field = fields[i];
        if (this._translateDbConstraint(field.dbConstraint) == "primary key")
        {
          whereKeys.push(field.dbField);
          whereKeySetValueQuestionMarks.push(field.dbField + " = ?");
        }
        else
        {
          setValueKeys.push(field.dbField);
          setValueKeySetValueQuestionMarks.push(field.dbField + " = ?");
        }
      }
      query.push(setValueKeySetValueQuestionMarks.join(", "));
      query.push(" where ");
      query.push(whereKeySetValueQuestionMarks.join(" and "));
      keys = keys.concat(setValueKeys, whereKeys);
      return new ORMSqlGeneratorQuery(query.join(""), keys);
    },
    
    deleteQuery: function()
    {
      var query = [];
      var keys = [];
      var whereKeySetValueQuestionMarks = [];
      query.push("delete from " + this._ormParser.getTable() + " where ");
      var fields = this._ormParser.getFields();
      for (var i = 0; i < fields.length; i++)
      {
        var field = fields[i];
        if (this._translateDbConstraint(field.dbConstraint) == "primary key")
        {
          keys.push(field.dbField);
          whereKeySetValueQuestionMarks.push(field.dbField + " = ?");
        }
      }
      query.push(whereKeySetValueQuestionMarks.join(" and "));
      return new ORMSqlGeneratorQuery(query.join(""), keys);
    },
    
    selectQuery: function()
    {
      var query = [];
      var keys = [];
      var whereKeySetValueQuestionMarks = [];
      var queryKeys = [];
      query.push("delete from " + this._ormParser.getTable() + " ");
      var fields = this._ormParser.getFields();
      for (var i = 0; i < fields.length; i++)
      {
        var field = fields[i];
        queryKeys.push(field.dbField);
        if (this._translateDbConstraint(field.dbConstraint) == "primary key")
        {
          keys.push(field.dbField);
          whereKeySetValueQuestionMarks.push(field.dbField + " = ?");
        }
      }
      query.push(queryKeys.join(", "));
      query.push(" where ");
      query.push(whereKeySetValueQuestionMarks.join(" and "));
      return new ORMSqlGeneratorQuery(query.join(""), keys);
    },
    
    _getDbConstraint: function(dbConstraint)
    {
      if ((dbConstraint == null) || (dbConstraint == ""))
      {
        return "";
      }
      return " " + this._translateDbConstraint(dbConstraint);
    },
    
    _translateDbType: function(dbType)
    {
      if (["text", "string", "str"].indexOf(dbType.toLowerCase()) >= 0)
      {
        return "text";
      }
      if (["integer", "int"].indexOf(dbType.toLowerCase()) >= 0)
      {
        return "integer";
      }
      return dbType;
    },
    
    _translateDbConstraint: function(dbConstraint)
    {
      if (dbConstraint == null)
      {
        return "";
      }
      if (["key", "id"].indexOf(dbConstraint.toLowerCase()) >= 0)
      {
        return "primary key";
      }
      return dbConstraint;
    }
  });
})();

