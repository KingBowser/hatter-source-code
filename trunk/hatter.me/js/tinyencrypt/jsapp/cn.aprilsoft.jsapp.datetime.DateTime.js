/*
 * cn.aprilsoft.jsapp.datetime.DateTime.js
 * jsapp, ui date&time functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.datetime
  Package("cn.aprilsoft.jsapp.datetime");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  
  Class("cn.aprilsoft.jsapp.datetime.DateTime", Extend(), Implement(),
  {
    MILLS_IN_ONE_HOUR: (60 * 60 * 1000),
    
    WEEK_LIST: [],
    
    MONTH_LIST: [],
    
    _date: null,
    
    _timeZone: null,
    
    Constructor: function(/* ... variant parameter(s) */)
    {
      System.invokeOn(function()
      {
        this._date = new Date();
      }, this);
      System.invokeOn(ThisClass(), function(date)
      {
        this._date = date._date;
        this._timeZone = date._timeZone;
      }, this);
      System.invokeOn("date", function(date)
      {
        this._date = date;
      }, this);
      System.invokeOn("str", function(str)
      {
        this._date = new Date(str);
      }, this);
      System.invokeOn("int, int, int", function(year, month, day)
      {
        this._date = new Date(year, month, day);
      }, this);
      System.invokeOn("int, int, int, int", function(year, month, day, hour)
      {
        this._date = new Date(year, month, day, hour);
      }, this);
      System.invokeOn("int, int, int, int, int", function(year, month, day, hour, minute)
      {
        this._date = new Date(year, month, day, hour, minute);
      }, this);
      System.invokeOn("int, int, int, int, int, int", function(year, month, day, hour, minute, second)
      {
        this._date = new Date(year, month, day, hour, minute, second);
      }, this);
      
      if (this._date == null)
      {
        this._date = new Date();
      }
      if (this._timeZone == null)
      {
        this._timeZone = -(this._date.getTimezoneOffset() / 60);
      }
    },
    
    getUTCDateTime: function()
    {
      return this.getGMTDateTime(0);
    },
    
    getGMTDateTime: function(timeZone)
    {
      var gmtDate = ThisClass().newInstance(this);
      gmtDate._date = new Date(gmtDate._date.getTime() - (gmtDate._timeZone - timeZone) * this.MILLS_IN_ONE_HOUR);
      gmtDate._timeZone = timeZone;
      return gmtDate;
    },
    
    getYear: function()
    {
      return this._date.getFullYear();
    },
    
    getMonth: function()
    {
      return (this._date.getMonth() + 1);
    },
    
    getDate: function()
    {
      return this._date.getDate();
    },
    
    getDay: function()
    {
      return this._date.getDay();
    },
    
    getHours: function()
    {
      return this._date.getHours();
    },
    
    getMinutes: function()
    {
      return this._date.getMinutes();
    },
    
    getSeconds: function()
    {
      return this._date.getSeconds();
    },
    
    asString: function()
    {
      var result = [this.getYear(), this.getMonth(), this.getDate()].join("/") + " "
                 + [this.getHours(), this.getMinutes(), this.getSeconds()].join(":") + " "
                 + "GMT " + ((this._timeZone >= 0)? "+": "") + this._timeZone;
      return result;
    },
    
    isLeapYear: Static(function(year)
    {
      return (((0 == (year % 4)) && (0 != (year % 100))) || (0 == (year % 400)));
    }),
        
    getDaysOfYear: Static(function(year)
    {
      if (ThisClass().isLeapYear(year))
      {
        return 366;
      }
      else
      {
        return 365;
      }
    }),
    
    getDaysOfMonth: Static(function(year, month)
    {
      if ((month == null) || (month < 1) || (month > 12))
      {
        throw new Exception("Month:`" + month + "' range error!");
      }
      
      if (month == 2)
      {
        return (ThisClass().isLeapYear(year)) ? 29 : 28;
      }
      else
      {
        return [31, null, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month - 1];
      }
    })
  });
})();

