/*
 * cn.aprilsoft.jsapp.db.DatabaseType.js
 * jsapp, oracle type functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.db
  Package("cn.aprilsoft.jsapp.db");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.db.DatabaseType", Extend(), Implement(),
  {
    _typesList: Static(
    [
      ["ARRAY", 0x2000],
      ["BIGINT", 20],
      ["BINARY", 128],
      ["BOOLEAN", 11],
      ["BSTR", 8],
      ["CHAPTER", 136],
      ["CHAR", 129],
      ["CURRENCY", 6],
      ["DATE", 7],
      ["DBDATE", 133],
      ["DBTIME", 134],
      ["DBTIMESTAMP", 135],
      ["DECIMAL", 14],
      ["DOUBLE", 5],
      ["EMPTY", 0],
      ["EORROR", 10],
      ["FILETIME", 64],
      ["GUID", 72],
      ["IDISPATCH", 9],
      ["INTEGER", 3],
      ["IUNKNOW", 13],
      ["LONGVARBINARY", 205],
      ["LONGVARCHAR", 201],
      ["LONGVARWCHAR", 203],
      ["NUMBERIC", 131],
      ["PROPVARIANT", 138],
      ["SINGLE", 4],
      ["SAMLLINT", 2],
      ["TINYINT", 16],
      ["UNSIGNEDBIGINT", 21],
      ["UNSIGNEDINT", 19],
      ["UNSIGNEDSMALLINT", 18],
      ["UNSIGNEDTINYINT", 17],
      ["USERDEFINED", 132],
      ["VARBINARY", 204],
      ["VARCHAR", 200],
      ["VARIANT", 12],
      ["VARNUMBERIC", 139],
      ["VARWCHAR", 202],
      ["WCHAR", 130]
    ]),
    
    getStringType: Static(function(intType)
    {
      var strType = null;
      var OracleType = cn.aprilsoft.jsapp.db.DatabaseType;
      System.walkArray(OracleType._typesList, function(idx, value)
      {
        if (value[1] == intType)
        {
          strType = value[0];
        }
      });
      return strType;
    }),
    
    getIntegerType: Static(function(strType)
    {
      var intType = null;
      var OracleType = cn.aprilsoft.jsapp.db.DatabaseType;
      System.walkArray(OracleType._typesList, function(idx, value)
      {
        if (value[0] == strType)
        {
          intType = value[1];
        }
      });
      return intType;
    })
  });
})();

