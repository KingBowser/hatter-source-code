/*
 * cn.aprilsoft.jsapp.db.common.DBUtil.js
 * jsapp, db common functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.db.common
  Package("cn.aprilsoft.jsapp.db.common");

  Class("cn.aprilsoft.jsapp.db.common.DBUtil", Extend(), Implement(),
  {
    getFieldsFromTable: function(objConn, strTableName)
    {
      var lstFields = [];
      var rs = new ActiveXObject("ADODB.Recordset");
      try
      {
        rs.Open("select * from " + strTableName + "where 1=0", objConn, 1, 1);
        var enumer = new Enumerator(rs.Fields);
        for (; !enumer.atEnd(); enumer.moveNext())
        {
          var item = enumer.item();
          var objField = {};
          objField.definedSize = item.DefinedSize;
          objField.type = item.Type;
          objField.name = item.Name;
          objField.numericScale = item.NumericScale;
          objField.precision = item.Precision;
          lstFields.push(objField);
        }
      }
      catch(e)
      {
        throw new Exception("Get fields information from talbel + `" + strTableName + "' failed!");
      }
      finally
      {
        rs.Close();
      }
      if (lstFields.length > 0)
      {
        return lstFields;
      }
      return null;
    }
  });
})();

