/*
 * cn.aprilsoft.jsapp.timer.Until.js
 * jsapp, timer until functions
 * 
 * Usage:
 * Until.condition(function()
 * {
 *   return true; // condition
 * })
 * .error(function(e)
 * {
 *   // handle error
 * })
 * .done(function()
 * {
 *   // handle done
 * })
 * .check();
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.timer
  Package("cn.aprilsoft.jsapp.timer");
  
  var Timer = Using("cn.aprilsoft.jsapp.timer.Timer");
  
  Class("cn.aprilsoft.jsapp.timer.Until", Extend(), Implement(),
  {
    _timer: Static(new Timer()),
    
    _isDone: false,
    _isCancel: false,
    _conditionFunc: null,
    _doneFunc: null,
    _errorFunc: function(e)
    {
      alert(NewException(e));
    },
    
    Constructor: function(conditionFunc, doneFunc)
    {
      this._conditionFunc = conditionFunc;
      this._doneFunc = doneFunc;
    },
    
    condition: Static(function(conditionFunc)
    {
      return ThisClass().newInstance().on(conditionFunc);
    }),
    
    on: function(conditionFunc)
    {
      this._conditionFunc = conditionFunc;
      return this;
    },
    
    done: function(doneFunc)
    {
      this._doneFunc = doneFunc;
      return this;
    },
    
    error: function(errorFunc)
    {
      this._errorFunc = errorFunc;
      return this;
    },
    
    check: function()
    {
      this._checkConditionFunc();
    },
    
    isDone: function()
    {
      return this._isDone;
    },
    
    cancel: function()
    {
      this._isCancel = true;
    },
    
    _checkConditionFunc: function()
    {
      try
      {
        if (this._conditionFunc())
        {
          this._isDone = true;
          this._doneFunc();
        }
        else
        {
          if (!(this._isCancel))
          {
            ThisClass()._timer.setTimeout(this._checkConditionFunc.bind(this), 100);
          }
        }
      }
      catch(e)
      {
        this._errorFunc(e);
      }
    }
  });
})();

