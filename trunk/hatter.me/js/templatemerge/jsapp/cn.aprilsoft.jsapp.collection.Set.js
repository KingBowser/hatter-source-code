/*
 * cn.aprilsoft.jsapp.collection.Set.js
 * jsapp, set functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.collection
  Package("cn.aprilsoft.jsapp.collection");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  
  var MapF = Using.F("cn.aprilsoft.jsapp.collection.Map");

  Class("cn.aprilsoft.jsapp.collection.Set", Extend(), Implement(),
  {
    _map: null,
    
    Constructor: function()
    {
      this._map = MapF().newInstance();
      
      this.addAll.apply(this, arguments);
    },
    
    add: function(/* arguments */)
    {
      for (var i = 0; i < arguments.length; i++)
      {
        this._map.put(arguments[i], 1);
      }
    },
    
    addAll: function(collection)
    {
      if (collection == null)
      {
        return;
      }
      var isInvokeCalled = false;
      System.invokeOn("arr", function(arr)
      {
        isInvokeCalled = true;
        this.add.apply(this, arr);
      }, this);
      System.invokeOn(ThisClass(), function(set)
      {
        isInvokeCalled = true;
        this.add.apply(this, set.toArray());
      }, this);
      
      if (!isInvokeCalled)
      {
        throw new Exception("Error in add all!");
      }
    },
    
    contains: function(value)
    {
      return this._map.contains(value);
    },
    
    remove: function(value)
    {
      return this._map.remove(value);
    },
    
    clear: function()
    {
      this._map = MapF().newInstance();
    },
    
    toArray: function()
    {
      return System.copyArray(this._map.keyList());
    },
    
    asString: function()
    {
      return "[" + this.toArray().join(", ") + "]";
    }
  });
})();
