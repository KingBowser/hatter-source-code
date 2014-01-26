/*
 * cn.aprilsoft.jsapp.datetime.TimeCounter.js
 * jsapp, time counter functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.datetime
  Package("cn.aprilsoft.jsapp.datetime");

  Class("cn.aprilsoft.jsapp.datetime.TimeCounter", Extend(), Implement(),
  {
    _times: null,
    
    TIME_KEY_START: Static("cn.aprilsoft.jsapp.datetime.TimeCounter._CONSTANT_TIME_KEY_START"),
    
    TIME_KEY_END: Static("cn.aprilsoft.jsapp.datetime.TimeCounter._CONSTANT_TIME_KEY_END"),
    
    Constructor: function()
    {
      this._times = [];
    },
    
    setTime: function(key)
    {
        if (this._times[key] != null)
        {
          throw new Exception("Time key '" + key + "' already exists.");
        }
        
        this._times[key] = new Date();
    },
    
    getTime: function(key)
    {
      return this._times[k];
    },
    
    setStart: function()
    {
      this.setTime(ThisClass().TIME_KEY_START);
    },
    
    setEnd: function()
    {
      this.setTime(ThisClass().TIME_KEY_END);
    },
    
    getCostTime: function(key0, key1)
    {
      var time0 = this.getTime(key0);
      var time1 = this.getTime(key1);
      
      if (time0 == null)
      {
        throw new Exception("TIme key0 '" + key0 + "' not exists.");
      }
      if (time1 == null)
      {
        throw new Exception("TIme key1 '" + key1 + "' not exists.");
      }
      
      return (time1.getTime() - time0.getTime());
    },
    
    getOneTime: function()
    {
      return this.getCostTime(ThisClass().TIME_KEY_START, ThisClass().TIME_KEY_END);
    }
  });
})();

