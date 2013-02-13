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

  Class("cn.aprilsoft.jsapp.datetime.DateFormat", Extend(), Implement(),
  {
    _translateMap: Static({
                    "y": [[2, 4], "_formatYear"],   // year,   foramt: yy, yyyy
                    "M": [[1, 2], "_formatMonth"],  // month,  format: M, MM
                    "d": [[1, 2], "_formatDay"],    // day,    format: d, dd
                    "h": [[1, 2], "_formatHour"],   // hour,   format: h, hh
                    "m": [[1, 2], "_formatMinute"], // minute, format: m, mm
                    "s": [[1, 2], "_formatSecond"], // second, format: s, ss
                    "w": [[1],    "_formatWeek"]    // week,   format: w
                  }),

    _weekMap: Static(["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"]),

    format: Static(function(date, format, param)
    {
      if ((format == null) || (format == ""))
      {
        return "";
      }
      var inBlock = false;
      var result = [];
      for (var i = 0; i < format.length; i++)
      {
        var c = format.charAt(i);
        if (c == "\\")
        {
          if (i < (format.length - 1))
          {
            result.push(format.charAt(i + 1));
            i += 1;
          }
          else
          {
            result.push(c);
          }
          continue;
        }
        if (inBlock)
        {
          if (c == "}")
          {
            inBlock = false;
          }
          else
          {
            result.push(c);
          }
          continue;
        }
        if (c == "{")
        {
          inBlock = true;
          continue;
        }

        var f = ThisClass()._translateMap[c];
        if (f == null)
        {
          result.push(c);
          continue;
        }
        var nc = ThisClass()._getNextSameCharsCount(format, i, c);
        var formatl = ThisClass()._match(f[0], nc);
        if (formatl == null)
        {
          result.push(c);
          continue;
        }
        try
        {
          result.push(ThisClass()[f[1]](date, formatl, param));
        }
        catch (e)
        {
          throw new Exception("Error occured in format:" + c, date, formatl, param);
        }
        i += formatl - 1;
      }
      return result.join("");
    }),

    _getNextSameCharsCount: Static(function(str, offset, c)
    {
        var count = 0;
        for (var i = offset; i < str.length; i++)
        {
          if (c == str.charAt(i))
          {
            count += 1;
          }
          else
          {
            break;
          }
        }
        return count;
    }),

    _match: Static(function(arr, dig)
    {
      if ((arr == null) || (arr.length == 0))
      {
        return null;
      }
      for (var i = (arr.length - 1); i >= 0; i--)
      {
        if (dig >= arr[i])
        {
          return arr[i];
        }
      }
      return null;
    }),

    _padding: Static(function(str, len, c)
    {
      var s = (str == null)? "": str.toString();
      if (len <= s.length)
      {
        return s;
      }
      var r = [];
      for (var i = 0; i < (len - s.length); i++)
      {
        r.push(c);
      }
      return (r.join("") + s);
    }),

    // -------------------------------------------- //

    _formatYear: Static(function(date, len, param)
    {
      if (len == 2)
      {
        return ThisClass()._padding((date.getFullYear() % 100), 2, "0");
      }
      else
      {
        return date.getFullYear();
      }
    }),

    _formatMonth: Static(function(date, len, param)
    {
      if (len == 1)
      {
        return (date.getMonth() + 1);
      }
      else
      {
        return ThisClass()._padding((date.getMonth() + 1), 2, "0");
      }
    }),

    _formatDay: Static(function(date, len, param)
    {
      if (len == 1)
      {
        return date.getDate();
      }
      else
      {
        return ThisClass()._padding(date.getDate(), 2, "0");
      }
    }),

    _formatHour: Static(function(date, len, param)
    {
      if (len == 1)
      {
        return date.getHours();
      }
      else
      {
        return ThisClass()._padding(date.getHours(), 2, "0");
      }
    }),

    _formatMinute: Static(function(date, len, param)
    {
      if (len == 1)
      {
        return date.getMinutes();
      }
      else
      {
        return ThisClass()._padding(date.getMinutes(), 2, "0");
      }
    }),

    _formatSecond: Static(function(date, len, param)
    {
      if (len == 1)
      {
        return date.getSeconds();
      }
      else
      {
        return ThisClass()._padding(date.getSeconds(), 2, "0");
      }
    }),

    _formatWeek: Static(function(date, len, param)
    {
      var map = ThisClass()._weekMap;
      if ((param != null) && (param["weekMap"] != null))
      {
        map = param["weekMap"];
      }
      return map[date.getDay()];
    })
  });
})();

