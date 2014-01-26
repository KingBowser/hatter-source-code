/*
 * cn.aprilsoft.jsapp.collection.Range.js
 * jsapp, range functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.collection
  Package("cn.aprilsoft.jsapp.collection");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Enumerator = Using("cn.aprilsoft.jsapp.collection.Enumerator");

  Class("cn.aprilsoft.jsapp.collection.Range", Extend(Enumerator), Implement(),
  {
    _from: 0,
    
    _to: 0,
    
    _step: 1,
    
    _current: 0,
    
    Constructor: function()
    {
      var isInvokeCalled = false;
      System.invokeOn(ThisClass(), function(range)
      {
        isInvokeCalled = true;
        this._from = range._from;
        this._to = range._to;
        this._step = range._step;
      }, this);
      System.invokeOn("int, int", function(from, to)
      {
        isInvokeCalled = true;
        this._from = from;
        this._to = to;
        this._step = 1;
      }, this);
      System.invokeOn("int, int, int", function(from, to, step)
      {
        isInvokeCalled = true;
        this._from = from;
        this._to = to;
        this._step = step;
      }, this);
      
      this._current = this._from;
      
      if (this._step == 0)
      {
        throw new Exception("Step cannot be 0");
      }
      if (this._step > 0)
      {
        if (this._from > this._to)
        {
          throw new Exception("From cannot bigger than to.");
        }
      }
      else
      {
        if (this._from < this._to)
        {
          throw new Exception("From cannot less thann to.");
        }
      }
      if (!isInvokeCalled)
      {
        throw new Exception("Constructor parameters error!");
      }
    },
    
    range: Static(function(/* arguments */)
    {
      return System.newObject(ThisClass(), arguments);
    }),
    
    objectOf: Static(function(/* arguments */)
    {
      return System.newObject(ThisClass(), arguments);
    }),
    
    hasNext: function()
    {
      if (this._step > 0)
      {
        return (this._current <= this._to);
      }
      else
      {
        return (this._current >= this._to);
      }
    },
    
    next: function()
    {
      if (this.hasNext())
      {
        var curr = this._current;
        this._current += this._step;
        return curr;
      }
      else
      {
        throw new Exception("No next element.");
      }
    },
    
    reset: function()
    {
      this._current = this._from;
    },
    
    first: function()
    {
      this.reset();
      return this.next();
    },
    
    last: function()
    {
      throw new Exception("Not implemented function.");
    },
    
    asString: function()
    {
      var buffer = ["Enumerator from = " + this._from];
      buffer.push(", to = " + this._to);
      buffer.push(", pointer = " + this._current);
      return buffer.join("");
    }
  });
})();
