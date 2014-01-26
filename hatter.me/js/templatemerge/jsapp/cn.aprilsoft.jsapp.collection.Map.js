/*
 * cn.aprilsoft.jsapp.collection.Map.js
 * jsapp, map functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.collection
  Package("cn.aprilsoft.jsapp.collection");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.collection.Map", Extend(), Implement(),
  {
    _HASH_MAX_KEY_COUNT: 10,
    
    _map: null,
    
    Constructor: function()
    {
      this._map = {};
      System.invokeOn(ThisClass(), function(map)
      {
        var mapKeyList = map.keyList();
        for (var i = 0; i < mapKeyList.length; i++)
        {
          var key = mapKeyList[i];
          this.put(key, map.get(key));
        }
      }, this);
    },
    
    put: function(key, value)
    {
      var hash = this._hash(key);
      var list = this._map[hash];
      if (list == null)
      {
        list = [];
        list._type = "Map";
        list.push({"key": key, "value": value});
        this._map[hash] = list;
      }
      else
      {
        for (var i = 0; i < list.length; i++)
        {
          if (list[i].key === key)
          {
            list[i].value = value;
            return;
          }
        }
        list.push({"key": key, "value": value});
      }
    },
    
    get: function(key)
    {
      var hash = this._hash(key);
      var list = this._map[hash];
      if (list == null)
      {
        return null;
      }
      else
      {
        for (var i = 0; i < list.length; i++)
        {
          if (list[i].key === key)
          {
            return list[i].value;
          }
        }
        return null;
      }
    },
    
    contains: function(key)
    {
      var hash = this._hash(key);
      var list = this._map[hash];
      if (list != null)
      {
        for (var i = 0; i < list.length; i++)
        {
          if (list[i].key === key)
          {
            return true;
          }
        }
      }
      return false;
    },
    
    remove: function(key)
    {
      var hash = this._hash(key);
      var list = this._map[hash];
      if (list != null)
      {
        for (var i = 0; i < list.length; i++)
        {
          if (list[i].key === key)
          {
            list.splice(i, 1)
            return true;
          }
        }
      }
      return false;
    },
    
    clear: function()
    {
      this._map = {};
    },
    
    keyList: function()
    {
      var keyList = [];
      for (var k in this._map)
      {
        var list = this._map[k];
        if ((list instanceof Array) && (list._type == "Map"))
        {
          for (var i = 0; i < list.length; i++)
          {
            keyList.push(list[i].key);
          }
        }
      }
      return keyList;
    },
    
    asString: function()
    {
      var buffer = [];
      buffer.push("{");
      var keyList = this.keyList();
      for (var i = 0; i < keyList.length; i++)
      {
        if (i > 0)
        {
          buffer.push(", ");
        }
        buffer.push(keyList[i] + ": " + this.get(keyList[i]));
      }
      buffer.push("}");
      return buffer.join("");
    },
    
    _hash: function(object)
    {
      if (typeof(object) == "object")
      {
        try
        {
          var hash = [];
          for (var k in object)
          {
            hash.push(k + object[k]);
            if (hash.length > this._HASH_MAX_KEY_COUNT)
            {
              break;
            }
          }
          return hash.join(",");
        }
        catch(e)
        {
          return object;
        }
      }
      else
      {
        return object;
      }
    }
  });
})();
