/*
 * cn.aprilsoft.jsapp.gears.Timer.js
 * jsapp, gears timer functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.gears
  Package("cn.aprilsoft.jsapp.gears");
  
  var GearsHelper = Using("cn.aprilsoft.jsapp.gears.common.GearsHelper");

  Class("cn.aprilsoft.jsapp.gears.Timer$Record", Extend(), Implement(),
  {
    id: null,
    func: null,
    
    Constructor: function(id, func)
    {
      this.id = id;
      this.func = func;
    },
    
    asString: function()
    {
      return ThisClass().getShortClassName() + ": id=" +  this.id;
    }
  });
  
  var Record = Using("cn.aprilsoft.jsapp.gears.Timer$Record");
  
  Class("cn.aprilsoft.jsapp.gears.Timer", Extend(), Implement(),
  {
    _timer: null,
    _timerTimeoutList: null,
    _timerIntervalList: null,
    
    Constructor: function()
    {
      this._timerTimeoutList = [];
      this._timerIntervalList = [];
      this._timer = GearsHelper.create(GearsHelper.TIMER);
    },
    
    setTimeout: function(func, msecDelay)
    {
      var id;
      var proxyFunc = function()
      {
        try
        {
          func();
        }
        finally
        {
          this._timerTimeoutList = this._removeIdFromList(this._timerTimeoutList, id);
        }
      };
      id = this._timer.setTimeout(proxyFunc.bind(this), msecDelay);
      this._timerTimeoutList.push(new Record(id, func));
      return id;
    },
    
    setInterval: function(func, msecDelay)
    {
      var id = this._timer.setInterval(func, msecDelay);
      this._timerIntervalList.push(new Record(id, func));
      return id;
    },
    
    clearTimeout: function(timerId)
    {
      this._timerTimeoutList = this._removeIdFromList(this._timerTimeoutList, timerId);
      this._timer.clearTimeout(timerId);
    },
    
    clearInterval: function(timerId)
    {
      this._timerIntervalList = this._removeIdFromList(this._timerIntervalList, timerId);
      this._timer.clearInterval(timerId);
    },
    
    isTimeout: function(timerId)
    {
      return this._isIdInList(this._timerTimeoutList, timerId);
    },
    
    isInterval: function(timerId)
    {
      return this._isIdInList(this._timerIntervalList, timerId);
    },
    
    _isIdInList: function(list, id)
    {
      for (var i = 0; i < list; i++)
      {
        if (list[i].id == id)
        {
          return true;
        }
      }
      return false;
    },
    
    _removeIdFromList: function(list, id)
    {
      var exceptIdList = [];
      for (var i = 0; i < list.length; i++)
      {
        if (list[i].id != id)
        {
          exceptIdList.push(list[i]);
        }
      }
      return exceptIdList;
    },
    
    asString: function()
    {
      return ThisClass().getShortClassName()
             + " timeoutList: [" + this._timerTimeoutList + "]"
             + " intervalList: [" + this._timerIntervalList + "]";
    }
  });
})();
