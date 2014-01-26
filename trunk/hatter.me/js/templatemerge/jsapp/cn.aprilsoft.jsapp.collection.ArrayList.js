/*
 * cn.aprilsoft.jsapp.collection.ArrayList.js
 * jsapp, array list functions
 * 
 * Copyright(C) Hatter Jiang
 */
 
(function()
{
  // New package: cn.aprilsoft.jsapp.collection
  Package("cn.aprilsoft.jsapp.collection");
  
  var Cloneable = Using("cn.aprilsoft.jsapp.core.Cloneable");
  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.collection.ArrayList", Extend(), Implement(Cloneable),
  {
    _array: null,
    
    Constructor: function()
    {
      this._array = [];
      
      System.invokeOn("arr", function(arr)
      {
        this.add.apply(this, arr);
      }, this);
      System.invokeOn(ThisClass(), function(linkedList)
      {
        this.add.apply(this, linkedList.toArray());
      }, this);
    },
    
    size: function()
    {
      return this._array.length;
    },
    
    add: function(/* arguments */)
    {
      for (var i = 0; i < arguments.length; i++)
      {
        this._array.push(arguments[i]);
      }
    },
    
    push: function(/* arguments */)
    {
      this.add.apply(this, arguments);
    },
    
    unshift: function(/* arguments */)
    {
      for (var i = 0; i < arguments.length; i++)
      {
        this._array.unshift(arguments[i]);
      }
    },
    
    remove: function(index)
    {
      if (index < 0)
      {
        throw new Exception("Index cannot be less than 0.");
      }
      if (index >= this._array.length)
      {
        throw new Exception("Index cannot be more than " + (this.size() - 1) + ".");
      }
      if (index == 0)
      {
        return this.shift();
      }
      if (index == (this._array.length - 1))
      {
        return this.pop();
      }
      return this._array.splice(index, 1);
    },
    
    get: function(index)
    {
      if (index < 0)
      {
        throw new Exception("Index cannot be less than 0.");
      }
      if (index >= this._array.length)
      {
        throw new Exception("Index cannot be more than " + (this.size() - 1) + ".");
      }
      return this._array[index];
    },
    
    shift: function()
    {
      return this._array.shift();
    },
    
    pop: function()
    {
      return this._array.pop();
    },
    
    toArray: function()
    {
      return System.copyArray(this._array);
    },
    
    join: function(str)
    {
      return this._array.join(str);
    },
    
    clear: function()
    {
      this._array = [];
    },
    
    clone: function()
    {
      return ThisClass().newInstance(this);
    },
    
    asString: function()
    {
      return "[" + this._array.join(", ") + "]";
    }
  });
})();
