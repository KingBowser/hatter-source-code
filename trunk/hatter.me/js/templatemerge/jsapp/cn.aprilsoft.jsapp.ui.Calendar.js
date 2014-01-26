/*
 * cn.aprilsoft.jsapp.ui.Calendar.js
 * jsapp, ui calendar functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var DateFormat = Using("cn.aprilsoft.jsapp.datetime.DateFormat");
  var Datetime = Using("cn.aprilsoft.jsapp.datetime.DateTime");

  Class("cn.aprilsoft.jsapp.ui.Calendar", Extend(), Implement(),
  {
    _sel_date: null,
    
    _cur_date: null,
    
    //           sun,  mon,   tue,  wed,   thu,  fri, sat
    _week_list: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
    
    _first_day: 0,// sunday
    
    _calendarPre: null,
    
    Constructor: function()
    {
      this._sel_date = {};
      
      this._cur_date = {};
      
      this._calendarPre = "_caldenar_" + (new Date()).getTime();
    },
    
    getHTML: function()
    {
      // output html
    },
    
    setSelectedYear: function(year)
    {
      this._sel_date.year = year;
    },
    
    setSelectedYear: function(month)
    {
      this._sel_date.month = month;
    },
    
    setSelectedDay: function(day)
    {
      this._sel_date.day = day;
    },
    
    setSelectedYMDDate: function(year, month, day)
    {
      this.setYear(year);
      this.setMonth(month);
      this.setDay(day);
    },
    
    setShowYMDate: function(year, month)
    {
    },
    
    reshowWeeksTitle: function()
    {
      for (var i = 0; i < 7; i++)
      {
        var weeekIndex = i + this._first_day;
        if (weeekIndex > 6)
        {
          weeekIndex -= 7;
        }
        
        try
        {
          document.getElementById(this._calendarPre + "week_" + i).innerHTML = this._week_list[i];
        }
        catch(e)
        {
          throw new Exception("Error occured in set calendar week title.");
        }
      }
    },
    
    reshowCalendarGrid: function()
    {
      var year = this._cur_date.year;
      var month = this._cur_date.month;
      
      var ym1 = new Date(year, month, 1);
      
      var initWeek = ym1.getDay();
      
      var allThisMonthDays = [];
      
      // pre month
      var preMonth = DateTime.getPreMonth(this._cur_date);
      var preDays = DateTime.getDays(preMonth.year, preMonth.month);
      var initWeekDay = initWeek - this._first_day;
      if (initWeekDay < 0)
      {
        initWeekDay += 7;
      }
      for (var i = initWeekDay; i > 0; i--)
      {
        //                     is this week, pre month year, pre month month, day
        allThisMonthDays.push([false, preMonth.year, preMonth.month, preDays - i + 1]);
      }
      
      // this month
      var thisDays = DateTime.getDays(year, month);
      for (var i = 1; i <= thisDays; i++)
      {
        allThisMonthDays.push([true, year, month, i]);
      }
      
      // next month
      var nextMonth = DateTime.getNextMonth(this._cur_date);
      var nextDays = DateTime.getDays(nextMonth.year, nextMonth.month);
      for (var i = 1; i < 28; i++)
      {
        allThisMonthDays.push([false, nextMonth.year, nextMonth.month, i]);
      }
      
      // show in grid
      var showDaysIndex = 0;
      for (var row = 0; row < 6; row++)
      {
        for (var week = 0; week < 7; week++)
        {
          // TODO go show use allThisMonthDays[showDaysIndex]
          
          showDaysIndex += 1;
        }
      }
    }
  });
})();

