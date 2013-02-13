/*
 * cn.aprilsoft.jsapp.system.RefValue.js
 * jsapp, Ref. value functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");
  
  Class("cn.aprilsoft.jsapp.system.RefValue", Extend(), Implement(),
  {
    _value: Private(null),
    
    _isSetted: Private(false),
    
    Constructor: function(value)
    {
      if (arguments.length > 0)
      {
        // when has argument
        this.set(value);
      }
    },
    
    set: function(value)
    {
      this._isSetted = true;
      this._value = value;
    },
    
    get: function()
    {
      if (!this._isSetted)
      {
        throw new Exception("Value has not be inited.");
      }
      return this._value;
    }
  });
})();

