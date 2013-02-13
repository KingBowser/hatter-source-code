/*
 * cn.aprilsoft.jsapp.serialize.Serializer.js
 * jsapp, serialize functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.serialize
  Package("cn.aprilsoft.jsapp.serialize");
  
  var JSON = Using("cn.aprilsoft.jsapp.encode.JSON");
  var Serializer = Using("cn.aprilsoft.jsapp.serialize.Serializer");

  Class("cn.aprilsoft.jsapp.serialize.JSONSerializer", Extend(), Implement(Serializer),
  {
    serialize: function(message)
    {
      if (message == null)
      {
        return null;
      }
      return JSON.stringify(message);
    },
    
    deserialize: function(string)
    {
      if (string == null)
      {
        return null;
      }
      return JSON.parse(string);
    }
  });
})();

