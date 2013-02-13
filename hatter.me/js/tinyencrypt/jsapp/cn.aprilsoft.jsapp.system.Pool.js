/*
 * cn.aprilsoft.jsapp.system.Pool.js
 * jsapp, pool functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var LifeCycle = Using("cn.aprilsoft.jsapp.system.LifeCycle");

  Class("cn.aprilsoft.jsapp.system.Pool", Extend(), Implement(),
  {
    _pool: null,
    
    _minSize: null,
    
    _maxSize: null,
    
    _lifeCycle: null,
    
    Constructor: function(minSize, maxSize, lifeCycle)
    {
      this._pool = [];
      this._minSize = minSize;
      this._maxSize = maxSize;
      if (!Implements(lifeCycle, LifeCycle))
      {
        throw new Exception("Parameter lifeCycle must implemnts LifeCycle.");
      }
      this._lifeCycle = lifeCycle;
      System.times(minSize, (function()
      {
        this._pool.push(this._makeObject());
      }).bind(this));
    },
    
    getObject: function()
    {
      var obj = this._pool.pop();
      if (obj == null)
      {
        return this._makeObject();
      }
      else
      {
        return obj;
      }
    },
    
    putObject: function(obj)
    {
      if (this._pool.length >= this._maxSize)
      {
        this._destoryObject(obj);
      }
      else
      {
        this._pool.push(obj);
      }
    },
    
    _makeObject: function()
    {
      return this._lifeCycle.create();
    },
    
    _destoryObject: function(obj)
    {
      return this._lifeCycle.destory(obj);
    }
  });
})();

