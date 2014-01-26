/*
 * cn.aprilsoft.jsapp.html.form.RequestHelper.js
 * jsapp, form request helper functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.html.form
  Package("cn.aprilsoft.jsapp.html.form");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var MapF = Using.F("cn.aprilsoft.jsapp.collection.Map");
  var EnumeratorF = Using.F("cn.aprilsoft.jsapp.collection.Enumerator");
  
  Static.Class("cn.aprilsoft.jsapp.html.form.RequestHelper", Extend(), Implement(),
  {
    Pair: Static(),
    
    Class: Static(function()
    {
      ThisClass().Pair = Class("cn.aprilsoft.jsapp.html.form.RequestHelper.Pair", Extend(), Implement(),
      {
        key: null,
        value: null,
        Constructor: function(key, value)
        {
          this.key = key;
          this.value = value;
        },
        asString: function()
        {
          var re = [];
          re.push("{");
          re.push("key = " + this.key)
          re.push(", ");
          re.push("value = " + this.value);
          re.push("}");
          return re.join("");
        }
      });
    }),
    
    makeQueryParam: Static(function(obj)
    {
      var Clazz = ThisClass();
      var paramArray = [];
      System.invokeOn(MapF(), function(map)
      {
        EnumeratorF().objectOf(map.keyList()).each(function(k)
        {
          paramArray.push(System.newObject(Clazz.Pair, [k, map.get(k)]));
        });
      }, null);
      
      return EnumeratorF().objectOf(paramArray).process(function(item)
      {
        return Clazz._makeQueryParamFromPair(item);
      }).join("&");
    }),
    
    _makeQueryParamFromPair: Static(function(pair)
    {
      return ThisClass()._escape(pair.key) + "=" + ThisClass()._escape(pair.value);
    }),
    
    _escape: Static(function(str)
    {
      return encodeURIComponent(str);
    })
  });
})();

