/*
 * cn.aprilsoft.jsapp.common.StringUtil.js
 * jsapp, String util functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  Class("cn.aprilsoft.jsapp.common.StringUtil", Extend(), Implement(),
  {
    notNull: Static(function(str)
    {
      return (str == null)? "": str;
    }),
    
    trimLeft: Static(function(str)
    {
      return (str == null)? "": str.toString().replace(/^\s*/, "");
    }),
    
    trimRight: Static(function(str)
    {
      return (str == null)? "": str.toString().replace(/\s*$/, "");
    }),
    
    trim: Static(function(str)
    {
      return cn.aprilsoft.jsapp.common.StringUtil.trimLeft(cn.aprilsoft.jsapp.common.StringUtil.trimRight(str));
    }),
    
    replaceAllHtmlTag: Static(function(str)
    {
      if (str == null)
      {
        return "";
      }
      str = str.replace(/</g, "&lt;");
      str = str.replace(/>/g, "&gt;");
      str = str.replace(/ /g, "&nbsp;");
      str = str.replace(/\n/g, "<br/>");
      str = str.replace(/\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;");
      return str;
    }),
    
    startWith: Static(function(str, substr)
    {
      if (str != null)
      {
        if ((substr != null) && (substr.length != 0) && (substr.length <= str.length))
        {
          if (str.substring(0, substr.length) == substr)
          {
            return true;
          }
          return false;
        }
        else
        {
          if (substr.length > str.length)
          {
            return false;
          }
          else
          {
            return true;
          }
        }
      }
      else
      {
        if (substr == null)
        {
          return true;
        }
        else
        {
          return false;
        }
      }
    }),
    
    startsWith: Static(function()
    {
      return cn.aprilsoft.jsapp.common.StringUtil.startWith.apply(null, arguments);
    }),
    
    endWith: Static(function(str, substr)
    {
      if (str != null)
      {
        if ((substr != null) && (substr.length != 0) && (substr.length <= str.length))
        {
          if (str.substring(str.length - substr.length) == substr)
          {
            return true;
          }
          return false;
        }
        else
        {
          if (substr.length > str.length)
          {
            return false;
          }
          else
          {
            return true;
          }
        }
      }
      else
      {
        if (substr == null)
        {
          return true;
        }
        else
        {
          return false;
        }
      }
    }),
    
    endsWith: Static(function()
    {
      return cn.aprilsoft.jsapp.common.StringUtil.endWith.apply(null, arguments);
    }),
    
    betweenWith: Static(function(str, substrS, substrE)
    {
      return  (
                (cn.aprilsoft.jsapp.common.StringUtil.startWith(str, substrS))
              &&
                (cn.aprilsoft.jsapp.common.StringUtil.endWith(substrE))
              )? true: false;
    }),
    
    contains: Static(function(str, substr)
    {
      if (((str == null) && (substr == null)) || (substr == null))
      {
        return true;
      }
      if (str == null)
      {
        return false;
      }
      if (str.indexOf(substr) >= 0)
      {
        return true;
      }
      return false;
    }),
    
    fillString: Static(function(str, filllength, fillchar, beforeafterflag)
    {
      if (str == null)
      {
        str = "";
      }
      if (filllength == null)
      {
        filllength = 0;
      }
      if (fillchar == null)
      {
        fillchar = " ";
      }
      if (fillchar.length != 1)
      {
        throw new Exception("param `fillchar' should be char not string");
      }
      if (beforeafterflag == null)
      {
        beforeafterflag = true;
      }
      if (filllength <= 0)
      {
        return str;
      }
      var fillstring = "";
      var fillstringlength = filllength - str.length;
      for (var i = 0; i < fillstringlength; i++)
      {
        fillstring += fillchar;
      }
      if (beforeafterflag)
      {
        str = fillstring + str;
      }
      else
      {
        str += fillstring;
      }
      return str;
    }),
    
    isEmpty: Static(function(str)
    {
      return ((str == null) || (str == ""));
    }),
    
    isNotEmpty: Static(function(str)
    {
      return ((str != null) && (str != ""));
    }),
    
    notNull: Static(function(str)
    {
      if (str == null)
      {
        return "";
      }
      return str;
    }),
    
    _formatIntFillZero: Static(function(numbervalue, fillzerocount, beforeafterflag)
    {
      if ((numbervalue == null) || (isNaN(numbervalue)))
      {
        numbervalue = 0;
      }
      var minusflag = false;
      if ((beforeafterflag) && (numbervalue < 0))
      {
        minusflag = true;
      }
      if (beforeafterflag == null)
      {
        beforeafterflag = true;
      }
      numbervalue = numbervalue.toString();
      if (minusflag)
      {
        numbervalue = numbervalue.substring(1);
      }
      var needzerocount = fillzerocount - numbervalue.length;
      if (!(beforeafterflag))
      {
        needzerocount += 2;
      }
      if (minusflag)
      {
        needzerocount--;
      }
      var zeros = "";
      for (var i = 0; i < needzerocount; i++)
      {
        zeros += "0";
      }
      if (beforeafterflag)
      {
        numbervalue = zeros + numbervalue;
      }
      else
      {
        if (zeros.length > 0)
        {
          numbervalue = numbervalue.substring(2);
          numbervalue += zeros;
        }
        else
        {
          numbervalue = numbervalue.substring(2, fillzerocount + 2);
        }
      }
      if (minusflag)
      {
        numbervalue = "-" + numbervalue;
      }
      return numbervalue;
    }),
    
    _getNumberFormat: Static(function(patternbd, numbervalue)
    {
      var StringUtil = cn.aprilsoft.jsapp.common.StringUtil;
      Debug.message(numbervalue);
      if  (
            (typeof(numbervalue) != "number")
          &&
            (!(numbervalue instanceof Number))
          )
      {
        if  (
              (typeof(numbervalue) != "string")
            &&
              (!(numbervalue instanceof String))
            )
        {
          throw new Exception("invalid object pattern:" + "`" + numbervalue + "'");
        }
        else
        {
          if (!(/^(\d+(\.d*)?)|(\.\d*)?$/.test(numbervalue)))
          {
            throw new Exception("invalid string pattern:" + "`" + numbervalue + "'");
          }
        }
      }
      var beforedot = 0;
      var afterdot = 0;
      if (numbervalue == null)
      {
        numbervalue = 0;
      }
      if (StringUtil.contains(patternbd, "."))
      {
        var patternstrlist = patternbd.split(".");
        beforedot = parseInt(patternstrlist[0]);
        afterdot = parseInt(patternstrlist[1]);
      }
      else
      {
        beforedot = parseInt(patternbd);
      }
      if (isNaN(beforedot))
      {
        beforedot = 0;
      }
      if (isNaN(afterdot))
      {
        afterdot = 0;
      }
      var valuebeforedot = 0;
      var valueafterdot = 0;
      numbervalue = numbervalue.toString();
      if (StringUtil.contains(numbervalue, "."))
      {
        var numbervaluestrlist = numbervalue.split(".");
        valuebeforedot = parseInt(numbervaluestrlist[0]);
        valueafterdot = parseFloat("0." + numbervaluestrlist[1]);
      }
      else
      {
        valuebeforedot = parseInt(numbervalue);
      }
      if (isNaN(valuebeforedot))
      {
        valuebeforedot = 0;
      }
      if (isNaN(valueafterdot))
      {
        valueafterdot = 0;
      }
      var retvalue = "";
      retvalue += StringUtil._formatIntFillZero(valuebeforedot, beforedot, true);
      if (afterdot != 0)
      {
        retvalue += ".";
        retvalue += StringUtil._formatIntFillZero(valueafterdot, afterdot, false);
      }
      return retvalue;
    }),
    
    _format: Static(function(str, params)
    {
      var StringUtil = cn.aprilsoft.jsapp.common.StringUtil;
      var outstrlist = [];
      var currentparamindex = 0;
      var splitstrlist = str.split("%%");
      for (var i = 0; i < splitstrlist.length; i++)
      {
        var tpstr = splitstrlist[i];
        if (StringUtil.contains(tpstr, "%"))
        {
          var tpsplitstrlist = tpstr.split("%");
          tpstr = tpsplitstrlist[0];
          for (var j = 1; j < tpsplitstrlist.length; j++)
          {
            var tpsplitstr = tpsplitstrlist[j];
            if (/^s(.*)/.test(tpsplitstr))
            {
              var tpsplitstrmatch = tpsplitstr.match(/^s(.*)/);
              tpstr += params[currentparamindex];
              tpstr += tpsplitstrmatch[1];
            }
            else if (/^((?:\.\d*)?|(?:\d+(?:\.\d*)?))d(.*)/.test(tpsplitstr))
            {
              var tpsplitstrmatch = tpsplitstr.match(/^((?:\.\d*)?|(?:\d+(?:\.\d*)?))d(.*)/);
              tpstr += StringUtil._getNumberFormat(tpsplitstrmatch[1], params[currentparamindex]);
              tpstr += tpsplitstrmatch[2];
            }
            else
            {
              throw new Exception("unknow pattern :`" + tpsplitstr + "'");
            }
            currentparamindex++;
          }
        }
        outstrlist.push(tpstr);
      }
      return outstrlist.join("%");
    }),
    
    format: Static(function(str)
    {
      var StringUtil = cn.aprilsoft.jsapp.common.StringUtil;
      if (arguments.length == 0)
      {
        throw new Exception("format need params");
      }
      if (str == null)
      {
        str = "";
      }
      var tmpstr = str.replace(/%%/g, "");
      var lentmpstr1 = tmpstr.length;
      tmpstr = tmpstr.replace(/%/g, "");
      var lentmpstr2 = tmpstr.length;
      if ((lentmpstr1 - lentmpstr2) != (arguments.length - 1))
      {
        throw new Exception("not matched `%' with params");
      }
      if (arguments.length == 1)
      {
        return str.replace(/%%/g, "%");
      }
      var params = [];
      for (var i = 1; i < arguments.length; i++)
      {
        params.push(arguments[i]);
      }
      return StringUtil._format(str, params);
    }),
    
    reverse: Static(function(strStr)
    {
      var lstStr = strStr.split(/.*?/);
      return lstStr.reverse().join("");
    }),
    
    repeat: Static(function(strStr, intNum)
    {
      var lstStrout = [];
      for (var i = 0; i < intNum; i++)
      {
        lstStrout.push(strStr);
      }
      return lstStrout.join("");
    }),
    
    isUpperCase: Static(function(chrChr)
    {
      return (/^[A-Z]$/.test(chrChr));
    }),
    
    isLowerCase: Static(function(chrChr)
    {
      return (/^[a-z]$/.test(chrChr));
    }),
    
    swapCharCase: Static(function(strStr)
    {
      var StringUtil = cn.aprilsoft.jsapp.common.StringUtil;
      var lstChars = [];
      for (var i = 0; i < strStr.length; i++)
      {
        var chr = strStr.charAt(i);
        if (StringUtil.isUpperCase(chr))
        {
          lstChars.push(chr.toLowerCase());
        }
        else if (StringUtil.isLowerCase(chr))
        {
          lstChars.push(chr.toUpperCase());
        }
        else
        {
          lstChars.push(chr);
        }
      }
      return lstChars.join("");
    }),
    
    breakBeforeUpperCaseChar: Static(function(strStr)
    {
      var StringUtil = cn.aprilsoft.jsapp.common.StringUtil;
      var lstStrs = [];
      
      var subStr = "";
      for (var i = 0; i < strStr.length; i++)
      {
        if (StringUtil.isUpperCase(strStr.charAt(i)))
        {
          if (subStr != "")
          {
            lstStrs.push(subStr);
            subStr = strStr.charAt(i);
          }
        }
        else
        {
          subStr += strStr.charAt(i);
        }
      }
      if (subStr != "")
      {
        lstStrs.push(subStr);
      }
      return lstStrs;
    }),
    
    getStringByCharCodeRange: Static(function(iFr, iTo)
    {
      if (iFr > iTo)
      {
        throw new Exception("iFr cannot bigger than iTo.");
      }
      
      var r = [];
      
      for (var i = iFr; i<= iTo; i++)
      {
        r.push(String.fromCharCode(i));
      }
      
      return r.join("");
    }),
    
    isString0To9: Static(function(str)
    {
      return (/^[0-9]*$/.test(str));
    }),
    
    isStringaToz: Static(function(str)
    {
      return (/^[a-z]$/.test(str));
    }),
    
    isStringAToZ: Static(function(str)
    {
      return (/^[A-Z]$/.test(str));
    }),
    
    isStringaTozOAToZ: Static(function(str)
    {
      return (/^[a-z]*$/i.test(str));
    }),
    
    lowerInitial: Static(function(str)
    {
      if (str == null)
      {
        return null;
      }
      return str.substring(0, 1).toLowerCase() + str.substring(1);
    }),
    
    upperInitial: Static(function(str)
    {
      if (str == null)
      {
        return null;
      }
      return str.substring(0, 1).toUpperCase() + str.substring(1);
    }),
    
    beforeFirst: Static(function(str, split, withSplit)
    {
      var i = str.indexOf(split);
      if (i < 0)
      {
        return null;
      }
      else
      {
        return str.substring(0, (i + (withSplit? split.length: 0)));
      }
    }),
    
    afterFirst: Static(function(str, split, withSplit)
    {
      var i = str.indexOf(split);
      if (i < 0)
      {
        return null;
      }
      else
      {
        return str.substring(i + (withSplit? 0: split.length));
      }
    }),
    
    beforeLast: Static(function(str, split, withSplit)
    {
      var i = str.lastIndexOf(split);
      if (i < 0)
      {
        return null;
      }
      else
      {
        return str.substring(0, (i + (withSplit? split.length: 0)));
      }
    }),
    
    afterLast: Static(function(str, split, withSplit)
    {
      var i = str.lastIndexOf(split);
      if (i < 0)
      {
        return null;
      }
      else
      {
        return str.substring(i + (withSplit? 0: split.length));
      }
    })
  });
})();

