/*
 * cn.aprilsoft.jsapp.orm.core.parser.ORMParser.js
 * jsapp, orm core orm-parser functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.orm.core.parser
  Package("cn.aprilsoft.jsapp.orm.core.parser");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var ORMObject = Using("cn.aprilsoft.jsapp.orm.ORMObject");
  
  Class("cn.aprilsoft.jsapp.orm.core.parser.ORMParser$Item", Extend(), Implement(),
  {
    DESCRIPTION_PATTERN: Static(/^(\w+)(=(\w+))?@(\w+)(:(\w+))?$/),
    jsField: null,
    dbField: null,
    dbType: null,
    dbConstraint: null,
    Constructor: function()
    {
      var isInvokeCalled = false;
      System.invokeOn("str", function(str)
      {
        isInvokeCalled = true;
        var matchObject = this._matchDescription(str);
        if (matchObject != null)
        {
          this.jsField = matchObject.jsField;
          this.dbField = matchObject.dbField;
          this.dbType = matchObject.dbType;
          this.dbConstraint = matchObject.dbConstraint;
        }
      }, this);
      System.invokeOn("str, str, str, str", function(jsField, dbField, dbType, dbConstraint)
      {
        isInvokeCalled = true;
        this.jsField = jsField;
        this.dbField = dbField;
        this.dbType = dbType;
        this.dbConstraint = dbConstraint;
      }, this);
      
      if (!isInvokeCalled)
      {
        throw new Exception("Call constructor in a wrong way.");
      }
      this._checkFields();
    },
    
    _checkFields: function()
    {
      if (this.jsField == null)
      {
        throw new Exception("Js field cannot be null.");
      }
      if (this.dbType == null)
      {
        throw new Exception("Db type cannot be null.");
      }
    },
    
    _matchDescription: function(description)
    {
      if (description == null)
      {
        return null;
      }
      var matchResult = description.match(ThisClass().DESCRIPTION_PATTERN);
      if (matchResult == null)
      {
        throw new Exception("Descript matcher error: " + description);
      }
      var jsField = matchResult[1];
      var dbField = matchResult[3];
      var dbType = matchResult[4];
      var dbConstraint = matchResult[6];
      dbField = (dbField == null)? jsField: dbField;
      return {
        jsField: jsField,
        dbField: dbField,
        dbType: dbType,
        dbConstraint: dbConstraint
      };
    },
    
    _clearSpace: function(str)
    {
      return ((str == null)? str: str.replace(/\s/g, ""));
    },
    
    asString: function()
    {
      var buffer = [];
      buffer.push(this.jsField);
      if (this.dbField != null)
      {
        buffer.push("=" + this.dbField);
      }
      buffer.push("@" + this.dbType);
      if (this.dbConstraint != null)
      {
        buffer.push(":" + this.dbConstraint);
      }
      return buffer.join("");
    }
  });
  
  var ORMParserItem = Using("cn.aprilsoft.jsapp.orm.core.parser.ORMParser$Item");
  
  Class("cn.aprilsoft.jsapp.orm.core.parser.ORMParser", Extend(), Implement(),
  {
    _table: null,
    _instance: null,
    _description: null,
    _fields: null,
    
    Constructor: function()
    {
      var isInvokeCalled = false;
      System.invokeOn(ORMObject, function(ormObject)
      {
        isInvokeCalled = true;
        this._instance = ormObject.newInstance();
        this._table = this._instance.orm_table();
        this._description = this._instance.orm_description();
        this._fields = this._parseDescrptionToFields(this._description);
      }, this);
      
      if (!isInvokeCalled)
      {
        throw new Exception("Call constructor in a wrong way.");
      }
    },
    
    getTable: function()
    {
      return this._table;
    },
    
    getDescription: function()
    {
      return this._description;
    },
    
    getFields: function()
    {
      return this._fields;
    },
    
    _parseDescrptionToFields: function(description)
    {
      var result = [];
      if (description != null)
      {
        var splitedDescriptions = this._getNotBlankArray(description.split(","));
        for (var i = 0; i < splitedDescriptions.length; i++)
        {
          result.push(new ORMParserItem(splitedDescriptions[i]));
        }
      }
      return result;
    },
    
    _getNotBlankArray: function(arr)
    {
      var notBlankArr = [];
      for (var i = 0; i < arr.length; i++)
      {
        var trimedItem = arr[i].replace(/^\s+/, "").replace(/\s$/, "");
        if (trimedItem != "")
        {
          notBlankArr.push(trimedItem);
        }
      }
      return notBlankArr;
    },
    
    asString: function()
    {
      var fieldsDescription = (this._fields == null)? "": this._fields.join(", ");
      return ThisClass().getShortClassName() + " [" + fieldsDescription + "]";
    }
  });
})();

