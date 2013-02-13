//  Copyright (c) 2002 M.Inamori,All rights reserved.
//  Coded 2/26/02
//  usage:
//     format(strFormat[, arg1[, arg2...]])
//  i.e.:
//    sprintf("%d", 12345));        12345
//    sprintf("%d", 123.6));        123
//    sprintf("%6d", 123));            123
//    sprintf("%06d", 123));        000123
//    sprintf("%06d", -123));       -00123
//    sprintf("%6.4d", 123));         0123
//    sprintf("%6.8d", 123));       00000123
//    sprintf("%f", 123.45));       123.450000
//    sprintf("%11f", 123.45));      123.450000
//    sprintf("%.3f", 123.45));     123.450
//    sprintf("%7.1f", 123.25));      123.2
//    sprintf("%7.1f", 123.251));     123.3
//    sprintf("%7.0f", 123.45));        123
//    sprintf("%08.2f", 123.45));   00123.45
//    sprintf("%08.2f", -123.45));  -0123.45
//    sprintf("%E", 123.45));       1.234500E+02
//    sprintf("%13E", 123.45));      1.234500E+02
//    sprintf("%.4E", 123.45));     1.2345E+02
//    sprintf("%13.5e", 123.45));     1.23450e+02
//    sprintf("%s", "abc"));        abc
//    sprintf("%4s", "abc"));        abc
//    sprintf("%.2s", "abc"));      ab
//    sprintf("%4.s", "abc"));
//    sprintf("%d", 123.45));       123
//    sprintf("%d", "123"));        123
//    sprintf("%d", "abc"));        0
//    sprintf("%7s", 123.45));       123.45
//    sprintf("%-7d", 123.45));     123
//    sprintf("% f", 123.45));       123.450000
//    sprintf("%+7.3f", 123.45));   +123.450
//    sprintf("%+- 9.2fa", 123.45)); +123.45 a
//  sprintf()
/*
 * cn.aprilsoft.jsapp.text.StringFormat.js
 * jsapp, string format functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.text
  Package("cn.aprilsoft.jsapp.text");

  Class("cn.aprilsoft.jsapp.text.StringFormat", Extend(), Implement(),
  {
    format: Static(function()
    {
      var argv = arguments;
      var argc = argv.length;
      if(argc == 0)
      {
        return "";
      }
      var result = "";
      var format = argv[0];
      var format_length = format.length;
      
      var flag, width, precision;
      flag = 0;
      
      var index = 1;
      var mode = 0;
      var tmpresult;
      var buff;
      for(var i = 0; i < format_length; i++)
      {
        var c = format.charAt(i);
        switch(mode)
        {
          case 0:    //normal
            if(c == '%')
            {
              tmpresult = c;
              mode = 1;
              buff = "";
            }
            else
              result += c;
            break;
          case 1:    //after '%'
            if(c == '%')
            {
              result += c;
              mode = 0;
              break;
            }
            if(index >= argc)
            {
              argv[argc++] = "";
            }
            width = 0;
            precision = -1;
            switch(c)
            {
              case '-':
                flag |= 1;
                mode = 1;
                break;
              case '+':
                flag |= 2;
                mode = 1;
                break;
              case '0':
                flag |= 4;
                mode = 2;
                break;
              case ' ':
                flag |= 8;
                mode = 1;
                break;
              case '#':
                flag |= 16;
                mode = 1;
                break;
              case '1': case '2': case '3': case '4': case '5':
              case '6': case '7': case '8': case '9':
                width = parseInt(c);
                mode = 2;
                break;
              case '-':
                flag = 1;
                mode = 2;
                break;
              case '.':
                width = "";
                precision = 0;
                mode = 3;
                break;
              case 'd':
                result += ThisClass().toInteger(argv[index], flag, width, precision);
                index++;
                mode = 0;
                break;
              case 'f':
                result += ThisClass().toFloatingPoint(argv[index], flag, width, 6);
                index++;
                mode = 0;
                break;
              case 'e':
                result += ThisClass().toExponential(argv[index], flag, width, 6, 'e');
                index++;
                mode = 0;
                break;
              case 'E':
                result += ThisClass().toExponential(argv[index], flag, width, 6, 'E');
                index++;
                mode = 0;
                break;
              case 's':
                result += argv[index];
                index++;
                mode = 0;
                break;
              default:
                result += buff + c;
                mode = 0;
                break;
            }
            break;
          case 2:    //while defining width
            switch(c)
            {
              case '.':
                precision = 0;
                mode = 3;
                break;
              case '0': case '1': case '2': case '3': case '4':
              case '5': case '6': case '7': case '8': case '9':
                width = width * 10 + parseInt(c);
                mode = 2;
                break;
              case 'd':
                result += ThisClass().toInteger(argv[index], flag, width, precision);
                index++;
                mode = 0;
                break;
              case 'f':
                result += ThisClass().toFloatingPoint(argv[index], flag, width, 6);
                index++;
                mode = 0;
                break;
              case 'e':
                result += ThisClass().toExponential(argv[index], flag, width, 6, 'e');
                index++;
                mode = 0;
                break;
              case 'E':
                result += ThisClass().toExponential(argv[index], flag, width, 6, 'E');
                index++;
                mode = 0;
                break;
              case 's':
                result += ThisClass().toFormatString(argv[index], width, precision);
                index++;
                mode = 0;
                break;
              default:
                result += buff + c;
                mode = 0;
                break;
            }
            break;
          case 3:    //while defining precision
            switch(c)
            {
              case '0': case '1': case '2': case '3': case '4':
              case '5': case '6': case '7': case '8': case '9':
                precision = precision * 10 + parseInt(c);
                break;
              case 'd':
                result += ThisClass().toInteger(argv[index], flag, width, precision);
                index++;
                mode = 0;
                break;
              case 'f':
                result += ThisClass().toFloatingPoint(argv[index], flag, width, precision);
                index++;
                mode = 0;
                break;
              case 'e':
                result += ThisClass().toExponential(argv[index], flag, width, precision, 'e');
                index++;
                mode = 0;
                break;
              case 'E':
                result += ThisClass().toExponential(argv[index], flag, width, precision, 'E');
                index++;
                mode = 0;
                break;
              case 's':
                result += ThisClass().toFormatString(argv[index], width, precision);
                index++;
                mode = 0;
                break;
              default:
                result += buff + c;
                mode = 0;
                break;
            }
            break;
          default:
            return "error";
        }
        
        if(mode)
        {
          buff += c;
        }
      }
      
      return result;
    }),

    toInteger: Private(Static(function (n, f, w, p)
    {
      if(typeof n != "number")
      {
        if(typeof n == "string")
        {
          n = parseFloat(n);
          if(isNaN(n))
          {
            n = 0;
          }
        }
        else
        {
          n = 0;
        }
      }
      
      var str = n.toString();
      
      //to integer if decimal
      if(-1 < n && n < 1)
      {
        str = "0";
      }
      else
      {
        if(n < 0)
        {
          str = str.substring(1);
        }
        var pos_e = str.indexOf('e');
        if(pos_e != -1)
        {    //
          var exp = parseInt(str.substring(pos_e + 2));
          var pos_dot = str.indexOf('.');
          if(pos_dot == -1)
          {
            str = str.substring(0, pos_e) + "000000000000000000000";
            exp -= 21;
          }
          else
          {
            str = str.substring(0, pos_dot)
                  + str.substring(pos_dot + 1, pos_e) + "00000";
            exp -= str.length - pos_dot;
          }
          for( ; exp; exp--)
          {
            str += "0";
          }
        }
        else
        {
          var pos_dot = str.indexOf('.');
          if(pos_dot != -1)
          {
            str = str.substring(0, pos_dot);
          }
        }
      }
      
      var len = str.length;
      if(len < p)
      {
        var c = "0";
        for(var i = p - len; i; i--)
        {
          str = c + str;
        }
        len = p;
      }
      
      return ThisClass().procFlag(str, f, w - len, n >= 0);
    })),

    toFloatingPoint: Private(Static(function (n, f, w, p)
    {
      if(typeof n != "number")
      {
        if(typeof n == "string")
        {
          n = parseFloat(n);
          if(isNaN(n))
          {
            n = 0;
          }
        }
        else
        {
          n = 0;
        }
      }
      
      var bpositive = (n >= 0);
      if(!bpositive)
      {
        n = -n;
      }
      
      str = ThisClass().toFloatingPoint2(n, f, p);
      
      return ThisClass().procFlag(str, f, w - str.length, bpositive);
    })),

    toFloatingPoint2: Private(Static(function (n, f, p)
    {
      var str = n.toString();
      
      //to decimal if exponential
      var pos_e = str.indexOf('e');
      if(pos_e != -1)
      {
        var exp = parseInt(str.substring(pos_e + 1));
        if(exp > 0)
        {      //
          var pos_dot = str.indexOf('.');
          if(pos_dot == -1)
          {
            str = str.substring(0, pos_e) + "000000000000000000000";
            exp -= 21;
          }
          else
          {
            str = str.charAt(0) + str.substring(2, pos_e) + "00000";
            exp -= str.length - 1;
          }
          for( ; exp; exp--)
            str += "0";
        }
        else
        {          //
          var equive_p = exp + p;
          if(equive_p < -1)  //
          {
            str = "0";
          }
          else if(equive_p >= 0)
          {  //
            str = str.substring(0, pos_e);
            var pos_dot = str.indexOf(".");
            if(pos_dot != -1)
            {
              str = str.charAt(0) + str.substring(2, pos_e);
            }
            str = "000000" + str;
            for(exp += 7; exp; exp++)
            {
              str = "0" + str;
            }
            str = "0." + str;
          }
          else
          {        //
            var tmp = parseFloat(str.substring(0, pos_e));
            if(tmp > 5)
            {  //
              str = "0.00000";
              for(var i = exp + 7; i; i++)
              {
                str += "0";
              }
              str += "1";
            }
            else      //
            {
              str = "0";
            }
          }
        }
      }
      
      //
      var len = str.length;
      var pos_dot = str.indexOf(".");
      if(pos_dot != -1)
      {
        var dec = len - pos_dot - 1;
        if(dec > p)
        {    //
          var tmp = parseFloat(str.charAt(pos_dot + p + 1)
                      + "." + str.substring(pos_dot + p + 2));
          if(tmp > 5)
          {  //
            var i;
            if(n < 1)
            {
              i = 2;
              while(str.charAt(i) == "0")
              {
                i++;
              }
              tmp = (parseInt(str.substring(i, p + 2)) + 1).toString();
              if(tmp.length > p + 2 - i)
              {    //
                if(i == 2)
                {
                  str = "1." + tmp.substring(1);
                }
                else
                {
                  str = str.substring(0, i - 1) + tmp;
                }
              }
              else
              {
                str = str.substring(0, i) + tmp;
              }
            }
            else
            {
              tmp = (parseInt(str.substring(0, pos_dot) + str.substring(
                    pos_dot + 1, pos_dot + p + 1)) + 1).toString();
              if(tmp.length > pos_dot + p)        //
              {
                str = tmp.substring(0, pos_dot + 1)
                      + "." +  tmp.substring(pos_dot + 1);
              }
              else
              {
                str = tmp.substring(0, pos_dot)
                      + "." + tmp.substring(pos_dot);
              }
            }
          }
          else
          {    //
            str = str.substring(0, p ? pos_dot + p + 1 : pos_dot);
          }
        }
        else if(dec < p)
        {  //"0"
          for(var i = p - dec; i; i--)
          {
            str += "0";
          }
        }
      }
      else
      {
        if(p)
        {
          str += ".0";
          for(var i = p - 1; i; i--)
          {
            str += "0";
          }
        }
      }
      
      return str;
    })),

    toExponential: Private(Static(function (n, f, w, p, e)
    {
      if(typeof n != "number")
      {
        if(typeof n == "string")
        {
          n = parseFloat(n);
          if(isNaN(n))
          {
            n = 0;
          }
        }
        else
        {
          n = 0;
        }
      }
      
      var bpositive = n >= 0;
      if(!bpositive)
      {
        n = -n;
      }
      
      var str = n.toString();
      var pos_dot = str.indexOf(".");
      var pos_e = str.indexOf("e");
      var type = ((pos_e != -1) << 1) + (pos_dot != -1);
      var exp;
      
      if(type == 0)
      {      //
        if(exp = str.length - 1)
        {
          str = str.charAt(0) + "." + str.substring(pos_dot = 1);
        }
      }
      else if(type == 1)
      {  //
        if(n > 10)
        {
          exp = pos_dot - 1;
          str = str.substring(0, 1) + "."
              + str.substring(1, pos_dot) + str.substring(pos_dot + 1);
          pos_dot = 1;
        }
        else if(n > 1)
        {
          exp = 0;
        }
        else
        {
          for(var i = 2; ; i++)
          {
            if(str.charAt(i) != "0")
            {
              exp = 1 - i;
              str = str.charAt(i) + "." + str.substring(i + 1);
              break;
            }
          }
          pos_dot = 1;
        }
      }
      else
      {  //
        exp = parseInt(str.substring(pos_e + 1));
        str = str.substring(0, pos_e);
      }
      
      str = ThisClass().toFloatingPoint2(parseFloat(str), f, p);
      
      if(exp >= 0)
      {
        str += e + (exp < 10 ? "+0" : "+") + exp;
      }
      else
      {
        str += e + (exp > -10 ? "-0" + (-exp) : exp);
      }
      
      str = ThisClass().procFlag(str, f, w - str.length, bpositive);
      
      return str;
    })),

    toFormatString: Private(Static(function (s, w, p)
    {
      if(typeof s != "string")
      {
        s = s.toString();
      }
      
      var len = s.length;
      if(p >= 0)
      {
        if(p < len)
        {
          s = s.substring(0, p);
          len = p;
        }
      }
      if(len < w)
      {
        var c = " ";
        for(var i = w - len; i; i--)
        {
          s = c + s;
        }
      }
      
      return s;
    })),

    procFlag: Private(Static(function (str, f, extra, b)
    {
      var minus = f & 1;
      var plus = f & 2;
      var space = f & 8;
      if(space)      //with ' '
      {
        extra--;
      }
      extra -= !b + plus > 0;
      if((f & 4) > 0 && !minus)
      {  //with 0 and not -
        if(extra > 0)
        {
          var c = "0";
          for(var i = extra; i; i--)
          {
            str = c + str;
          }
        }
        if(!b)
        {
          str = "-" + str;
        }
        else if(plus)
        {
          str = "+" + str;
        }
      }
      else
      {          //without 0 or with -
        if(!b)
        {
          str = "-" + str;
        }
        else if(plus)
        {
          str = "+" + str;
        }
        var c = " ";
        if(extra > 0)
        {
          var c = " ";
          if(minus)
          {
            for(var i = extra; i; i--)
            {
              str += c;
            }
          }
          else
          {
            for(var i = extra; i; i--)
            {
              str = c + str;
            }
          }
        }
      }
      if(space)
      {
        str = " " + str;
      }
      
      return str;
    }))
  });
})();

