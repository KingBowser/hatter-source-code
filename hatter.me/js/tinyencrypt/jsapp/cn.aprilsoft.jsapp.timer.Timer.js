/*
 * cn.aprilsoft.jsapp.timer.Timer.js
 * jsapp, timer functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.timer
  Package("cn.aprilsoft.jsapp.timer");
  
  var GearsTimerF = Using.F("cn.aprilsoft.jsapp.gears.Timer");
  
  var systemSetTimeout = setTimeout;
  var systemClearTimeout = clearTimeout;
  var systemSetInterval = setInterval;
  var systemClearInterval = clearInterval;
  
  Class("cn.aprilsoft.jsapp.timer.Timer", Extend(), Implement(),
  {
    _isGearsTimer: null,
    _setTimeout: null,
    _clearTimeout: null,
    _setInterval: null,
    _clearInterval: null,
    
    Constructor: function()
    {
      if (GearsTimerF.isLoaded())
      {
        this._isGearsTimer = true;
        var gearsTimer = GearsTimerF().newInstance();
        this._setTimeout = gearsTimer.setTimeout.bind(gearsTimer);
        this._clearTimeout = gearsTimer.clearTimeout.bind(gearsTimer);
        this._setInterval = gearsTimer.setInterval.bind(gearsTimer);
        this._clearInterval = gearsTimer.clearInterval.bind(gearsTimer);
      }
      else
      {
        this._isGearsTimer = false;
        this._setTimeout = systemSetTimeout;
        this._clearTimeout = systemClearTimeout;
        this._setInterval = systemSetInterval;
        this._clearInterval = systemClearInterval;
      }
    },
    
    setTimeout: function(func, ms)
    {
      return this._setTimeout(func, ms);
    },
    
    clearTimeout: function(id)
    {
      return this._clearTimeout(id);
    },
    
    setInterval: function()
    {
      return this._setInterval(func, ms);
    },
    
    clearInterval: function()
    {
      return this._clearInterval(id);
    }
  });
})();

