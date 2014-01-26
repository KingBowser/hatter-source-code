/*
 * cn.aprilsoft.jsapp.system.Mutex.js
 * jsapp, Mutil thread mutex functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  Class("cn.aprilsoft.jsapp.system.Mutex", Extend(), Implement(),
  {
    _maxlocktimes: 1,
    
    _currentlocktimes: 0,
    
    _lockerlist: [],
    
    _findlockerformlist: function(lockerobj)
    {
      for (var i = 0; i < this._lockerlist.length; i++)
      {
        if (lockerobj == this._lockerlist[i])
        {
          return i;
        }
      }
      return -1;
    },
    
    Constructor: function(maxlocktimes)
    {
      this._lockerlist = [];
      
      this.setMaxLockTimes(maxlocktimes);
    },
    
    setMaxLockTimes: function(maxlocktimes)
    {
      if (maxlocktimes != null)
      {
        this._maxlocktimes = maxlocktimes;
      }
    },
    
    lock: function(lockerobj)
    {
      if (this._currentlocktimes < this._maxlocktimes)
      {
        this._currentlocktimes++;
        this._lockerlist.push(lockerobj);
        return true;
      }
      return false;
    },
    
    isLocked: function()
    {
      return this._currentlocktimes >= this._maxlocktimes;
    },
    
    isNotLocked: function()
    {
      return (!this.isLocked());
    },
    
    isThisLocker: function(lockerobj)
    {
      return (this._findlockerformlist(lockerobj) >= 0);
    },
    
    isNotThisLocker: function(lockerobj)
    {
      return (!this.isThisLocker(lockerobj));
    },
    
    canThisLock: function(lockerobj)
    {
      return this.isNotLocked();
    },
    
    canThisOneLock: function(lockerobj)
    {
      return (this.isNotLocked() && this.isNotThisLocker(lockerobj));
    },
    
    release: function(lockerobj)
    {
      if (this._currentlocktimes > 0)
      {
        this._currentlocktimes--;
        var lockerobjindex = this._findlockerformlist(lockerobj);
        this._lockerlist.splice(lockerobjindex, 1);
        return true;
      }
      this._lockerlist = [];
      return false;
    },
    
    doThisLockerAction: function(lockerobj, thislockeraction)
    {
      if (this.canThisOneLock(lockerobj))
      {
        this.lock(lockerobj);
      }
      if (this.isThisLocker(lockerobj))
      {
        thislockeraction();
      }
    }
  });
})();

