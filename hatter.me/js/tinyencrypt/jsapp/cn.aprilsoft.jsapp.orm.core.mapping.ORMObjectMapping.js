/*
 * cn.aprilsoft.jsapp.orm.core.mapping.ORMObjectMapping.js
 * jsapp, orm orm-object-mapping functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.orm.core.mapping
  Package("cn.aprilsoft.jsapp.orm.core.mapping");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Map = Using("cn.aprilsoft.jsapp.collection.Map");
  var ORMObject = Using("cn.aprilsoft.jsapp.orm.ORMObject");
  var ORMParser = Using("cn.aprilsoft.jsapp.orm.core.parser.ORMParser");
  
  Class("cn.aprilsoft.jsapp.orm.core.mapping.ORMObjectMapping", Extend(), Implement(),
  {
    _classObject: null,
    _classObjectInstance: null,
    _dbFieldMap: null,
    
    Constructor: function(classObject)
    {
      if (!Implements(classObject, ORMObject))
      {
        throw new Exception("Class object must implemnts ORMObject.");
      }
      this._classObject = classObject;
      this._classObjectInstance = classObject;
      this._initDbFieldMap();
    },
    
    fromObjectList: function(objectList)
    {
      var mappedObjectList = [];
      for (var i = 0; i < objectList.length; i++)
      {
        mappedObjectList.push(this.fromObject(objectList[i]));
      }
      return mappedObjectList;
    },
    
    fromObject: function(object)
    {
      var mappedObject = this._classObject.newInstance();
      var fieldNames = this._getObjectFields(object);
      for (var i = 0; i < fieldNames.length; i++)
      {
        var field = this._dbFieldMap.get(fieldNames[i].toLowerCase());
        if (field != null)
        {
          mappedObject[field.jsField] = this._getObjectFieldValue(object, fieldNames[i]);
        }
      }
      return mappedObject;
    },
    
    _initDbFieldMap: function()
    {
      var dbFieldMap = new Map();
      var parser = new ORMParser(this._classObjectInstance);
      var fields = parser.getFields();
      for (var i = 0; i < fields.length; i++)
      {
        dbFieldMap.put(fields[i].dbField.toLowerCase(), fields[i]);
      }
      this._dbFieldMap = dbFieldMap;
    },
    
    _getObjectFieldValue: function(object, field)
    {
      return object.fieldByName(field);
    },
    
    _getObjectFields: function(object)
    {
      var fieldCount = object.fieldCount();
      var fieldNames = [];
      for (var i = 0; i < fieldCount; i++)
      {
        fieldNames.push(object.fieldName(i));
      }
      return fieldNames;
    }
  });
})();

