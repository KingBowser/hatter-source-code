/*
 * cn.aprilsoft.jsapp.collection.Enumerator.js
 * jsapp, enumerator functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.collection
  Package("cn.aprilsoft.jsapp.collection");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");

  var systemParseInt = parseInt;

  Class("cn.aprilsoft.jsapp.collection.Enumerator", Extend(), Implement(),
  {
    collectionArray: null,
    
    pointer: 0,
    
    Constructor: function(/* variant */)
    {
      this.collectionArray = [];
      this.pointer = 0;
      
      var isInvokeCalled = false;
      System.invokeOn("arr", function(arr) {
        isInvokeCalled = true;
        for (var i = 0; i < arr.length; i++)
        {
          this.collectionArray.push(arr[i]);
        }
      }, this);
      System.invokeOn(ThisClass(), function(collection) {
        isInvokeCalled = true;
        for (var i = 0; i < collection.collectionArray.length; i++)
        {
          this.collectionArray.push(collection.collectionArray[i]);
        }
      }, this);
      
      if (!isInvokeCalled)
      {
        for (var i = 0; i < arguments.length; i++)
        {
          this.collectionArray.push(arguments[i]);
        }
      }
    },
    
    objectOf: Static(function(/* arguments */)
    {
      return System.newObject(ThisClass(), arguments);
    }),
    
    range: Static(function(/* from, to , step?*/)
    {
      var result = null;
      System.invokeOn("int, int", function(from, to)
      {
        if (from > to)
        {
          throw new Exception("From cannot bigger than to!", from, to);
        }
        var arr = [];
        for (var i = from; i <= to; i++)
        {
          arr.push(i);
        }
        result = new (ThisClass())(arr);
      }, null);
      System.invokeOn("int, int, int", function(from, to, step)
      {
        if (step == 0)
        {
          throw new Exception("Step cannot be minus!", step);
        }
        if (((step > 0) && (from > to)) || ((step < 0) && (from < to)))
        {
          throw new Exception("From to and step error!", from, to , step);
        }
        var arr = [];
        if (step > 0)
        {
          for (var i = from; i <= to; i += step)
          {
            arr.push(i);
          }
        }
        else
        {
          for (var i = from; i >= to; i += step)
          {
            arr.push(i);
          }
        }
        result = new (ThisClass())(arr);
      }, null);
      
      if (result == null)
      {
        throw new Exception("Create range faled!");
      }
      return result;
    }),
    
    split: Static(function(strString, regRegex)
    {
      System.checkArguments();
      return new Enumerator(strString.split(regRegex));
    }),
    
    next: function()
    {
      if (this.hasNext())
      {
        return this.collectionArray[this.pointer++];
      }
      else
      {
        throw new Exception("No next element.");
      }
    },
    
    hasNext: function()
    {
      if (this.collectionArray != null)
      {
        return (this.collectionArray.length > this.pointer);
      }
      else
      {
        return false;
      }
    },
    
    reset: function()
    {
      this.pointer = 0;
    },
    
    first: function()
    {
      this.reset();
      return this.next();
    },
    
    last: function()
    {
      this.pointer = this.collectionArray.length - 1;
      return this.next();
    },
    
    each: function(callback/* (item) */)
    {
      while (this.hasNext())
      {
        callback(this.next());
      }
    },
    
    array: function()
    {
      var result = [];
      while (this.hasNext())
      {
        result.push(this.next());
      }
      return result;
    },
    
    filter: function(callback/* (item) */)
    {
      var result = [];
      while (this.hasNext())
      {
        var item = this.next();
        if (callback(item))
        {
          result.push(item);
        }
      }
      return result;
    },
    
    some: function(callback/* (item) */)
    {
      while (this.hasNext())
      {
        if (callback(this.next()))
        {
          return true;
        }
      }
      return false;
    },
    
    every: function(callback/* (item) */)
    {
      while (this.hasNext())
      {
        if (!callback(this.next()))
        {
          return false;
        }
      }
      return true;
    },
    
    sum: function()
    {
      var sum = 0;
      while (this.hasNext())
      {
        var item = systemParseInt(this.next());
        if (System.isNumber(item))
        {
          sum += item;
        }
      }
      return sum;
    },
    
    process: function(callback/* (item) */)
    {
      var result = [];
      while (this.hasNext())
      {
        result.push(callback(this.next()));
      }
      return result;
    },
    
    replace: function(regex, value)
    {
      return this.process(function(item)
      {
        if (System.isString(item))
        {
          return item.replace(regex, value);
        }
        else
        {
          return item;
        }
      });
    },
    
    contains: function(value, comparator)
    {
      if (System.countArguments(1))
      {
        return this.some(function(item)
        {
          return (item == value);
        });
      }
      else if (System.countArguments(2))
      {
        return this.some(function(item)
        {
          return comparator(item, value);
        });
      }
      else
      {
        throw new Exception("arguments error!");
      }
    },
    
    join: function(str)
    {
      return this.array().join(str);
    },
    
    asString: function()
    {
      var buffer = ["Enumerator [", this.collectionArray.join(", "), "]"];
      buffer.push(" pointer = " + this.pointer);
      return buffer.join("");
    }
  });
})();

