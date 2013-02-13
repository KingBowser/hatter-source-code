/*
 * cn.aprilsoft.jsapp.system.System.js
 * jsapp, system process functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");
  
  var Destroyable = Using("cn.aprilsoft.jsapp.core.Destroyable");
  var Closeable = Using("cn.aprilsoft.jsapp.core.Closeable");

  Class("cn.aprilsoft.jsapp.system.System", Extend(), Implement(),
  {
    UNDEFINED:          Static(window.undefined),
    
    INTERNET_EXPLORER:  Static(0x1),
    FIREFOX:            Static(0x1 << 1),
    OPERA:              Static(0x1 << 2),
    CHROME:             Static(0x1 << 3),
    SAFARI:             Static(0x1 << 4),
    UNKNOWN:            Static(0x1 << 30),
    
    INTERNET_EXPLORER_NAME:  Static("InternetExplorer"),
    FIREFOX_NAME:            Static("Firefox"),
    OPERA_NAME:              Static("Opera"),
    CHROME_NAME:             Static("Chrome"),
    SAFARI_NAME:             Static("Safari"),
    UNKNOWN_NAME:            Static("Unknown"),
    
    BROWSERS: Static([]),
    _initBrowsers: Static(function()
    {
      ThisClass().BROWSERS.push(ThisClass().INTERNET_EXPLORER);
      ThisClass().BROWSERS.push(ThisClass().FIREFOX);
      ThisClass().BROWSERS.push(ThisClass().OPERA);
      ThisClass().BROWSERS.push(ThisClass().CHROME);
      ThisClass().BROWSERS.push(ThisClass().SAFARI);
      ThisClass().BROWSERS.push(ThisClass().UNKNOWN);
    }),
    
    BROWSERS_NAME_MAP: Static({}),
    _initBrowserNameMap: Static(function()
    {
      ThisClass().BROWSERS_NAME_MAP[ThisClass().INTERNET_EXPLORER] = ThisClass().INTERNET_EXPLORER_NAME;
      ThisClass().BROWSERS_NAME_MAP[ThisClass().FIREFOX] = ThisClass().FIREFOX_NAME;
      ThisClass().BROWSERS_NAME_MAP[ThisClass().OPERA] = ThisClass().OPERA_NAME;
      ThisClass().BROWSERS_NAME_MAP[ThisClass().CHROME] = ThisClass().CHROME_NAME;
      ThisClass().BROWSERS_NAME_MAP[ThisClass().SAFARI] = ThisClass().SAFARI_NAME;
      ThisClass().BROWSERS_NAME_MAP[ThisClass().UNKNOWN] = ThisClass().UNKNOWN_NAME;
    }),
    
    Class: Static(function()
    {
      ThisClass()._initBrowsers();
      ThisClass()._initBrowserNameMap();
    }),
    
    toBrowserName: Static(function(browser)
    {
      if (browser == null)
      {
        return null;
      }
      var browserName = ThisClass().BROWSERS_NAME_MAP[browser]
      if (browserName != null)
      {
        return browserName;
      }
      else
      {
        return ThisClass().UNKNOWN_NAME;
      }
    }),
    
    bind: Static(function(func, _this)
    {
      if (func != null)
      {
        var bingArguments = Array.prototype.slice.call(arguments, 2);
        return function()
        {
          var callArguments = Array.prototype.slice.call(arguments);
          var actualArguments = [];
          Array.prototype.push.apply(actualArguments, bingArguments);
          Array.prototype.push.apply(actualArguments, callArguments);
          return func.apply(_this, actualArguments);
        };
      }
      else
      {
        return null;
      }
    }),
    
    bits: Static(function(strBits)
    {
      try
      {
        return parseInt(strBits, 2);
      }
      catch(e)
      {
        throw new Exception(e);
      }
    }),
    
    hasAttribute: Static(function(property, attribute)
    {
      try
      {
        return ((property & attribute) > 0);
      }
      catch(e)
      {
        throw new Exception(e);
      }
    }),
    
    getCostMilliseconds: Static(function(dateFrom, dateTo)
    {
      try
      {
        return (dateTo.getTime() - dateFrom.getTime());
      }
      catch(e)
      {
        throw new Exception(e);
      }
    }),
    
    getCostSeconds: Static(function(dateFrom, dateTo)
    {
      return (Math.round(ThisClass().getCostMilliseconds(dateFrom, dateTo) / 1000));
    }),
    
    asFunction: Static(function(func)
    {
      if (ThisClass().isString(func))
      {
        return new Function(func);
      }
      return func;
    }),
    
    hasFunction: Static(function(object, funcname)
    {
      if  (
            (object != null)
          &&
            (ThisClass().isFunction(object[funcname]))
          )
      {
        return true;
      }
      return false;
    }),
    
    hasFunc: Static(function(object, funcname)
    {
      return ThisClass().hasFunction(object, funcname);
    }),
    
    isNull: Static(function(nullobj)
    {
      if (nullobj == null)
      {
        return true;
      }
      return false;
    }),
    
    isUndefined: Static(function(undefinedobj)
    {
      if (typeof(undefinedobj) == "undefined")
      {
        return true;
      }
      return false;
    }),
    
    isString: Static(function(strobj)
    {
      if ((typeof(strobj) == "string") || (strobj instanceof String))
      {
        return true;
      }
      return false;
    }),
    
    isStr: Static(function(strobj)
    {
      return ThisClass().isString(strobj);
    }),
    
    isChar: Static(function(charObj)
    {
      if (ThisClass().isString(charObj))
      {
        if ((charObj != null) && (charObj.length == 1))
        {
          return true;
        }
      }
      return false;
    }),
    
    isChr: Static(function(charObj)
    {
      return ThisClass().isChar(strobj);
    }),
    
    isNumber: Static(function(numobj)
    {
      if ((typeof(numobj) == "number") || (numobj instanceof Number))
      {
        if (isNaN(numobj))
        {
          return false;
        }
        return true;
      }
      return false;
    }),
    
    isNum: Static(function(numobj)
    {
      return ThisClass().isNumber(numobj);
    }),
    
    isInteger: Static(function(intobj)
    {
      if  (
            (ThisClass().isNumber(intobj))
          &&
            (Math.round(intobj) == intobj)
          )
      {
        return true;
      }
      return false;
    }),
    
    isInt: Static(function(intobj)
    {
      return ThisClass().isInteger(intobj);
    }),
    
    isBoolean: Static(function(boolobj)
    {
      if ((typeof(boolobj) == "boolean") || (boolobj instanceof Boolean))
      {
        return true;
      }
      return false;
    }),
    
    isBool: Static(function(boolobj)
    {
      return ThisClass().isBoolean(boolobj);
    }),
    
    isFunction: Static(function(funcobj)
    {
      if ((typeof(funcobj) == "function") || (funcobj instanceof Function))
      {
        return true;
      }
      return false;
    }),
    
    isFunc: Static(function(funcobj)
    {
      return ThisClass().isFunction(funcobj);
    }),
    
    isArray: Static(function(arrobj)
    {
      if (arrobj instanceof Array)
      {
        return true;
      }
      return false;
    }),
    
    isArr: Static(function(arrobj)
    {
      return ThisClass().isArray(arrobj);
    }),
    
    isRegExp: Static(function(regobj)
    {
      if (regobj instanceof RegExp)
      {
        return true;
      }
      return false;
    }),
    
    isRegex: Static(function(regobj)
    {
      return ThisClass().isRegExp(regobj);
    }),
    
    isDate: Static(function(dateobj)
    {
      if (dateobj instanceof Date)
      {
        return true;
      }
      return false;
    }),
    
    // NOTICE: not toString
    asString: Static(function()
    {
      if (ThisClass().isNull(obj))
      {
        return "";
      }
      return obj.toString();
    }),
    
    toStr: Static(function(obj)
    {
      return ThisClass().asString();
    }),
    
    toInteger: Static(function(obj)
    {
      if (ThisClass().isNull(obj))
      {
        return 0;
      }
      if (ThisClass().isInteger(obj))
      {
        return obj;
      }
      var intobj = parseInt(obj);
      if (isNaN(intobj))
      {
        return 0;
      }
      return intobj;
    }),
    
    toInt: Static(function(obj)
    {
      return ThisClass().toInteger(obj);
    }),
    
    toNumber: Static(function(obj)
    {
      if (ThisClass().isNull(obj))
      {
        return 0;
      }
      if (ThisClass().isNumber(obj))
      {
        return obj;
      }
      var numobj = parseFloat(ThisClass().toStr(obj));
      if (isNaN(numobj))
      {
        return 0;
      }
      return numobj;
    }),
    
    toNum: Static(function(obj)
    {
      return ThisClass().toNumber(obj);
    }),
    
    toBoolean: Static(function(obj)
    {
      if (ThisClass().isNull(obj))
      {
        return false;
      }
      return !!obj;
    }),
    
    toBool: Static(function(obj)
    {
      return ThisClass().toBoolean(obj);
    }),
    
    toArray: Static(function(args)
    {
      var array = [];
      for (var i = 0; i < args.length; i++)
      {
        array.push(args[i]);
      }
      return array;
    }),
    
    copyArray: Static(function(arr)
    {
      return ThisClass().toArray(arr);
    }),
    
    createElement: Static(function(type, name)
    {
      var element = null;
      if (name == null)
      {
        element = document.createElement(type);
      }
      else
      {
        try
        {
          element = document.createElement("<" + type + " name=\"" + name + "\" >");
        }
        catch(e)
        {
          element = document.createElement(type);
          element.name = name;
        }
      }
      return element;
    }),
    
    getElement: Static(function(obj)
    {
      var htmlobj = obj;
      if (ThisClass().isString(obj))
      {
        htmlobj = document.getElementById(obj);
        if (htmlobj == null)
        {
          var htmlobjs = document.getElementsByName(obj);
          if ((htmlobjs != null) && (htmlobjs.length > 0))
          {
            htmlobj = htmlobjs[0];
          }
          else
          {
            htmlobj = null;
          }
        }
      }
      return htmlobj;
    }),
    
    clearDOMElement: Static(function(domobject)
    {
      // run this before delete a DOM object to avoid memory leak in IE
      // from http://javascript.crockford.com/memory/leak.html
      var elements = domobject.attributes, i, l, n;
      if (elements)
      {
          l = elements.length;
          for (i = 0; i < l; i++)
          {
              n = elements[i].name;
              if (ThisClass().isFunction(domobject[n]))
              {
                  domobject[n] = null;
              }
          }
      }
      elements = domobject.childNodes;
      if (elements)
      {
          l = elements.length;
          for (i = 0; i < l; i++)
          {
              ThisClass().clearDOMElement(domobject.childNodes[i]);
          }
      }
    }),
    
    getUserAgent: Static(function()
    {
      return navigator.userAgent;
    }),
    
    _isInUserAgent: Static(function(arg0)
    {
      var useragent = ThisClass().getUserAgent().toLowerCase();
      return (useragent.indexOf(arg0) >= 0);
    }),
    
    isIE: Static(function()
    {
      return ThisClass()._isInUserAgent("msie");
    }),
    
    isFireFox: Static(function()
    {
      return ThisClass()._isInUserAgent("firefox");
    }),
    
    isOpera: Static(function()
    {
      return ThisClass()._isInUserAgent("opera");
    }),
    
    isChrome: Static(function()
    {
      return ThisClass()._isInUserAgent("chrome");
    }),
    
    isSafari: Static(function()
    {
      return ((!(ThisClass().isChrome())) && (ThisClass()._isInUserAgent("safari")));
    }),
    
    getBrowser: Static(function()
    {
      if (ThisClass().isIE())
      {
        return ThisClass().INTERNET_EXPLORER;
      }
      if (ThisClass().isFireFox())
      {
        return ThisClass().FIREFOX;
      }
      if (ThisClass().isOpera())
      {
        return ThisClass().OPERA;
      }
      if (ThisClass().isChrome())
      {
        return ThisClass().CHROME;
      }
      if (ThisClass().isSafari())
      {
        return ThisClass().SAFARI;
      }
      // unknown browser
      return ThisClass().UNKNOWN;
    }),
    
    getBrowserName: Static(function()
    {
      return ThisClass().toBrowserName(ThisClass().getBrowser());
    }),
    
    isWindows: Static(function()
    {
      return ThisClass()._isInUserAgent("windows");
    }),
    
    isWindows2000: Static(function()
    {
      return ThisClass()._isInUserAgent("windows nt 5.0");
    }),
    
    isWindowsXP: Static(function()
    {
      return ThisClass()._isInUserAgent("windows nt 5.1");
    }),
    
    isWindowsVista: Static(function()
    {
      return ThisClass()._isInUserAgent("windows nt 6.0");
    }),
    
    // not worked well in opera9
    doFunction: Static(function(runablefunc)
    {
      if (!(ThisClass().isFunction(runablefunc)))
      {
        throw new Exception("param runablefunc is not a function!");
      }
      var params = [];
      for (var i = 1; i < arguments.length; i++)
      {
        params.push(arguments[i]);
      }
      try
      {
        runablefunc.apply(null, params);
      }
      catch(e)
      {
        throw new Exception(e, "run function error occured!");
      }
    }),
    
    ie: Static(function(runablefunc)
    {
      if (ThisClass().isIE())
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    notIe: Static(function(runablefunc)
    {
      if (!(ThisClass().isIE()))
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    ff: Static(function(runablefunc)
    {
      if (ThisClass().isFireFox())
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    notFf: Static(function(runablefunc)
    {
      if (!(ThisClass().isFireFox()))
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    // not worked well in opera9
    opera: Static(function(runablefunc)
    {
      if (ThisClass().isOpera())
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    // not worked well in opera9
    notOpera: Static(function(runablefunc)
    {
      if (!(ThisClass().isOpera()))
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    chrome: Static(function(runablefunc)
    {
      if (ThisClass().isChrome())
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    notChrome: Static(function(runablefunc)
    {
      if (!(ThisClass().isChrome()))
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    safari: Static(function(runablefunc)
    {
      if (ThisClass().isSafari())
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    notSafari: Static(function(runablefunc)
    {
      if (!(ThisClass().isSafari()))
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    unknow: Static(function(runablefunc)
    {
      if  (
            (!(ThisClass().isIE()))
          &&
            (!(ThisClass().isFireFox()))
          &&
            (!(ThisClass().isOpera()))
          &&
            (!(ThisClass().isChrome()))
          &&
            (!(ThisClass().isSafari()))
          // or more ...
          )
      {
        ThisClass().doFunction.apply(null, arguments);
      }
    }),
    
    time: Static(function(funcFunc)
    {
      var time1 = (new Date()).getTime();
      try
      {
        funcFunc();
      }
      catch(e)
      {
        throw new Exception(e, "call custom function error!");
      }
      var time2 = (new Date()).getTime();
      return (time2 - time1);
    }),
    
    collect: Static(function()
    {
      ThisClass().ie(function()
      {
        setTimeout("CollectGarbage()", 1);
      });
    }),
    
    convertFileNameMatcherToRegexpMatcher: Static(function(strFileNameMatcher)
    {
      if (strFileNameMatcher == null)
      {
        return strFileNameMatcher;
      }
      var strRegexpMatcher = strFileNameMatcher;
      strRegexpMatcher = strRegexpMatcher.replace(/\./g, "\\.");
      strRegexpMatcher = strRegexpMatcher.replace(/\*/g, "\.*");
      strRegexpMatcher = strRegexpMatcher.replace(/\?/g, "\.?");
      return strRegexpMatcher;
    }),
    
    _checkParamType: Static(function(args, paramList, i)
    {
      if ((/^str.*$/.test(paramList[i])) || (/^string.*$/.test(paramList[i])))
      {
        if (!(ThisClass().isString(args[i])))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: string");
        }
      }
      else if ((/^chr.*$/.test(paramList[i])) || (/^char.*$/.test(paramList[i])))
      {
        if ((!(ThisClass().isString(args[i]))) || (args[i].length != 1))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: string");
        }
      }
      else if ((/^num.*$/.test(paramList[i]))
            || (/^number.*$/.test(paramList[i]))
            || (/^flt.*$/.test(paramList[i]))
            || (/^float.*$/.test(paramList[i]))
            || (/^dbl.*$/.test(paramList[i]))
            || (/^double.*$/.test(paramList[i])))
      {
        if (!(ThisClass().isNumber(args[i])))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: number");
        }
      }
      else if ((/^int.*$/.test(paramList[i])) || (/^integer.*$/.test(paramList[i])))
      {
        if (!(ThisClass().isInteger(args[i])))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: integer(number)");
        }
      }
      else if ((/^bool.*$/.test(paramList[i])) || (/^boolean.*$/.test(paramList[i])))
      {
        if (!(ThisClass().isBoolean(args[i])))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: boolean");
        }
      }
      else if ((/^fun.*$/.test(paramList[i]))
            || (/^func.*$/.test(paramList[i]))
            || (/^function.*$/.test(paramList[i])))
      {
        if (!(ThisClass().isFunction(args[i])))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: function");
        }
      }
      else if ((/^arr.*$/.test(paramList[i]))
            || (/^lst.*$/.test(paramList[i]))
            || (/^list.*$/.test(paramList[i]))
            || (/^array.*$/.test(paramList[i])))
      {
        if (!(ThisClass().isArray(args[i])))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: array");
        }
      }
      else if ((/^reg.*$/.test(paramList[i]))
            || (/^regex.*$/.test(paramList[i]))
            || (/^regExp.*$/.test(paramList[i])))
      {
        if (!(ThisClass().isRegExp(args[i])))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: regex");
        }
      }
      else if (/^date.*$/.test(paramList[i]))
      {
        if (!(ThisClass().isDate(args[i])))
        {
          throw new Exception("Param type error!",
                              "The " + i + "(th) param[" + paramList[i] + "] excepted: date");
        }
      }
    }),
    
    checkArguments: Static(function()
    {
      var caller = arguments.callee.caller;
      if (caller == null)
      {
        throw new Exception("No caller function!");
      }
      else
      {
        // get function variant arguments
        var functionValue = caller.toString();
        functionValue = functionValue.replace(/\s+/g, " ");
        var firstLB = functionValue.indexOf("(");
        var firstRB = functionValue.indexOf(")");
        var paramLine = functionValue.substring((firstLB + 1), firstRB);
        paramLine = paramLine.replace(/((^\s*)|(\s*$))/g, "");
        var paramList = paramLine.split(/\s*,\s*/);
        // get function value arguments
        var args = caller.arguments;
        
        if (args.length != paramList.length)
        {
          throw new Exception("Params count error!",
                              "Excepted:" + paramList.length + " params," +
                              " but call this function with:" + args.length + " params");
        }
        for (var i = 0; i < paramList.length; i++)
        {
          ThisClass()._checkParamType(args, paramList, i);
        }
      }
    }),
    
    _checkInvokeOnArguments: Static(function(expectedArgu, calledArgu)
    {
      var expedtedArgus = [];
      if (expectedArgu == null)
      {
        throw new Exception("Expected argument cannot be null.");
      }
      if (Extends(expectedArgu, cn.aprilsoft.jsapp.core.Object))
      {
        return InstanceOf(calledArgu, expectedArgu);
      }
      else if (Class.IS(expectedArgu))
      {
        return Extends(calledArgu, expectedArgu);
      }
      else if (Interface.IS(expectedArgu))
      {
        return Implements(calledArgu, expectedArgu);
      }
      else
      {
        try
        {
          expectedArgu = expectedArgu.replace(/\s+/, "");
          expedtedArgus = expectedArgu.toLowerCase().split(/\s*\|\s*/g);
        }
        catch(e)
        {
          throw new Exception(e, "Error occured when split `" + expectedArgu + "'");
        }
        for (var i = 0; i < expedtedArgus.length; i++)
        {
          if ((expedtedArgus[i] == "str") || (expedtedArgus[i] == "string"))
          {
            if (ThisClass().isString(calledArgu))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "chr") || (expedtedArgus[i] == "char"))
          {
            if (ThisClass().isChar(calledArgu))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "num") || (expedtedArgus[i] == "number"))
          {
            if (ThisClass().isNumber(calledArgu))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "int") || (expedtedArgus[i] == "integer"))
          {
            if (ThisClass().isInteger(calledArgu))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "float") || (expedtedArgus[i] == "double"))
          {
            if ((ThisClass().isNumber(calledArgu)) && (!(ThisClass().isInteger(calledArgu))))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "bool") || (expedtedArgus[i] == "boolean"))
          {
            if (ThisClass().isBoolean(calledArgu))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "arr") || (expedtedArgus[i] == "array"))
          {
            if (ThisClass().isArray(calledArgu))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "reg") || (expedtedArgus[i] == "regex")
                  || (expedtedArgus[i] == "regexp"))
          {
            if (ThisClass().isRegExp(calledArgu))
            {
              return true;
            }
          }
          else if (expedtedArgus[i] == "date")
          {
            if (ThisClass().isDate(calledArgu))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "fun") || (expedtedArgus[i] == "func")
                  || (expedtedArgus[i] == "function"))
          {
            if (ThisClass().isFunction(calledArgu))
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "und") || (expedtedArgus[i] == "undefined"))
          {
            if (ThisClass().isUndefined(calledArgu))
            {
              return true;
            }
          }
          else if (expedtedArgus[i] == "null")
          {
            if (calledArgu === null)
            {
              return true;
            }
          }
          else if ((expedtedArgus[i] == "obj") || (expedtedArgus[i] == "object"))
          {
            if (calledArgu != null)
            {
              return true;
            }
          }
        }
      }
      
      return false;
    }),
    
    invokeOn: Static(function()
    {
      var caller = arguments.callee.caller;
      if (caller == null)
      {
        throw new Exception("No caller function!");
      }
      var expectedArguments = arguments;
      var calledArguments = caller.arguments;
      
      if (expectedArguments.length < 2)
      {
        throw new Exception("Aspected arguments cannot less than 2!");
      }
      if (!(ThisClass().isFunction(expectedArguments[expectedArguments.length - 2])))
      {
        throw new Exception("Call back function error!");
      }
      if (ThisClass().isString(caller._funcName))
      {
        var cb = expectedArguments[expectedArguments.length - 2];
        if (ThisClass().isUndefined(cb._funcName))
        {
          cb._funcName = caller._funcName + "!anonymous()";
        }
      }
      
      // special process for ","
      if ((expectedArguments.length - 2) == 1)
      {
        if (ThisClass().isString(expectedArguments[0]) && (expectedArguments[0].indexOf(",") >= 0))
        {
          var oldExpectedArguments = expectedArguments;
          expectedArguments = expectedArguments[0].split(/\s*,\s*/g);
          expectedArguments.push(oldExpectedArguments[oldExpectedArguments.length - 2]);
          expectedArguments.push(oldExpectedArguments[oldExpectedArguments.length - 1]);
        }
      }
      
      if (calledArguments.length > (expectedArguments.length - 2))
      {
        return null;
      }
      for (var i = 0; i < (expectedArguments.length - 2); i++)
      {
        if (!(ThisClass()._checkInvokeOnArguments(expectedArguments[i], calledArguments[i])))
        {
          return null;
        }
      }
      try
      {
        var thisObject = expectedArguments[expectedArguments.length - 1];
        return expectedArguments[expectedArguments.length - 2].apply(thisObject, calledArguments);
      }
      catch(e)
      {
        throw new Exception(e, "Do call invokeOn function error occured.");
      }
    }),
    
    checkIsCallAsConstructor: Static(function(objThis)
    {
      if (objThis == null)
      {
        throw new Exception("Call this funciton need param!");
      }
      if (!(objThis instanceof arguments.callee.caller))
      {
        throw new Exception("This function should call as constructor!");
      }
    }),
    
    countArguments: Static(function(count)
    {
      var caller = arguments.callee.caller;
      if (caller == null)
      {
        throw new Exception("No caller function!");
      }
      return (caller.arguments.length == count);
    }),
    
    checkIsClassMemberCaller: Static(function()
    {
      var func = arguments.callee.caller;
      var funcCaller = null;
      if (func != null)
      {
        funcCaller = func.caller;
      }
      if (funcCaller != null)
      {
        if ((func._funcName != null) && (funcCaller._funcName != null))
        {
          var funcName = func._funcName;
          var funcCallerName = funcCaller._funcName;
          var funcClassName = funcName.replace(/(.*)#.*/, "$1");
          var funcCallerClassName = funcCallerName.replace(/(.*)#.*/, "$1");
          if (funcClassName == funcCallerClassName)
          {
            return;
          }
        }
      }
      throw new Exception("Should the same class's member call this method.");
    }),
    
    walkArray: Static(function(arrObj, funcWalkArr)
    {
      ThisClass().checkArguments();
      for (var i = 0; i < arrObj.length; i++)
      {
        try
        {
          var ret = funcWalkArr(i, arrObj[i]);
          if (ret === true)
          {
            break;
          }
        }
        catch(e)
        {
          throw new Exception(e, "Error occured when walk array!");
        }
      }
    }),
    
    walkObject: Static(function(objObj, funcWalkArr)
    {
      ThisClass().checkArguments();
      for (var k in objObj)
      {
        try
        {
          var ret = funcWalkArr(k, objObj[k]);
          if (ret === true)
          {
            break;
          }
        }
        catch(e)
        {
          throw new Exception(e, "Error occured when walk object!");
        }
      }
    }),
    
    times: Static(function(count, func)
    {
      for (var i = 0; i < count; i++)
      {
        func(i);
      }
    }),
    
    merge: Static(function(target, source)
    {
      for (var k in source)
      {
        target[k] = source[k];
      }
      return target;
    }),
    
    setStyle: Static(function(domObj, objStyle)
    {
      if (ThisClass().isString(objStyle))
      {
        var strStyle = objStyle.replace(/(^\s+)|(\s+$)/g, "");
        var lstStyle = strStyle.split(/\s*;\s*/);
        for (var i = 0; i < lstStyle.length; i++)
        {
          var lstOneStyle = lstStyle[i].split(/\s*:\s*/);
          if (lstOneStyle.length == 2)
          {
            domObj.style[ThisClass()._getTranslateStyleName(lstOneStyle[0])] = lstOneStyle[1];
          }
        }
      }
      else
      {
        ThisClass().walkObject(objStyle, function(objKey, objValue)
        {
          domObj.style[objKey] = objValue;
        });
      }
    }),
    
    _getTranslateStyleName: Static(function(name)
    {
      var nameList = name.split("-");
      
      var resultList = [];
      resultList.push(nameList[0]);
      for (var i = 1; i < nameList.length; i++)
      {
        resultList.push(nameList[i].substring(0, 1).toUpperCase() + nameList[i].substring(1));
      }
      
      return resultList.join("");
    }),
    
    isScriptIncluded: Static(function(strScriptUrl)
    {
      var scriptObjs = document.getElementsByTagName("script");
      var includedFlag = false;
      
      if (scriptObjs != null)
      {
        for (var i = 0; i < scriptObjs.length; i++)
        {
          if (scriptObjs[i].src == strScriptUrl)
          {
            includedFlag = true;
            break;
          }
        }
      }
      return includedFlag;
    }),
    
    doTry: Static(function()
    {
      var exceptionList = [];
      var funcList = [];
      if (arguments.length > 0)
      {
        for (var i = 0; i < arguments.length; i++)
        {
          if (ThisClass().isArray(arguments[i]))
          {
            Array.prototype.push.apply(funcList, arguments[i]);
          }
          else
          {
            funcList.push(arguments[i]);
          }
        }
      }
      for (var i = 0; i < funcList.length; i++)
      {
        try
        {
          funcList[i]();
        }
        catch(e)
        {
          // record exception
          e.excetionFunction = "" + funcList[i];
          exceptionList.push(e);
        }
      }
      return exceptionList;
    }),
    
    doSortArray: Static(function(arrObj, funcObj)
    {
      // normaly, please use Array.prototype.sort function
      if ((arrObj != null) && (arrObj.length > 1))
      {
        var min;
        for (var i = 0; i < arrObj.length; i++)
        {
          min = i;
          for (var j = (i + 1); j < arrObj.length; j++)
          {
            try
            {
              if (funcObj(arrObj[min], arrObj[j]) > 0)
              {
                min = j;
              }
            }
            catch(e)
            {
              throw new Exception("Error occured when compare: `"
                                  + arrObj[min] + "' and `"
                                  + arrObj[j] + "'");
            }
          }
          if (min != i)
          {
            var swapObj = arrObj[min];
            arrObj[min] = arrObj[i];
            arrObj[i] = swapObj;
          }
        }
      }
    }),
    
    newObject: Static(function(clazz, clazzArguments)
    {
      var callArgs = [];
      if (clazzArguments != null)
      {
        for (var i = 0; i < clazzArguments.length; i++)
        {
          callArgs.push("clazzArguments[" + i + "]");
        }
      }
      var object;
      var callCmd = "object = new clazz(" + callArgs.join(", ") + ")";
      eval(callCmd);
      return object;
    }),
    
    // codes from jquery (copyright jquery)
    domReady: Static(function(func)
    {
      var _domReady = null;
      if (ThisClass()._domReadyObj == null)
      {
        ThisClass()._domReadyObj = {};
        _domReady = ThisClass()._domReadyObj;
        _domReady.domReadyInited = false;
        _domReady.inited = false;
        _domReady.funcList = [];
      }
      _domReady = ThisClass()._domReadyObj;
      _domReady.funcList.push(func);
      
      if (_domReady.inited)
      {
        func();
      }
      else
      {
        if (!_domReady.domReadyInited)
        {
          _domReady.domReadyInited = true;
          
          var _doTry = ThisClass().doTry;
          var callInit = function()
          {
            if (!_domReady.inited)
            {
              _domReady.inited = true;
              // kill the timer
              if (_domReady.load_timer)
              {
                clearInterval(_domReady.load_timer);
              }
              _doTry(_domReady.funcList);
            }
          };
          
          // for Mozilla/Opera9
          if (document.addEventListener)
          {
            document.addEventListener("DOMContentLoaded", callInit, false);
          }
          
          // for Internet Explorer
          /**//*@cc_on @*/
          /**//*@if (@_win32)
            if (document && document.readyState && (document.readyState == "complete"))
            {
              callInit();
            }
            else
            {
              document.write("<script id=__ie_onload defer src=//0><\/scr"+"ipt>");
              var script = document.getElementById("__ie_onload");
              script.onreadystatechange = function()
              {
                if (this.readyState == "complete")
                {
                  callInit(); // call the onload handler
                }
              };
            }
          /*@end @*/
          
          // for Safari
          if (/WebKit/i.test(navigator.userAgent))
          { // sniff
            _domReady.load_timer = setInterval(function()
            {
              if (/loaded|complete/.test(document.readyState))
              {
                callInit(); // call the onload handler
              }
            }, 10);
          }
          
          // for other browsers set the window.onload, but also execute the old window.onload
          var old_onload = window.onload;
          window.onload = function()
          {
            try
            {
              if (old_onload)
              {
                old_onload();
              }
            }
            catch(e)
            {
            }
            callInit();
          };
        }
      }
    }),
    
    pause: Static(function()
    {
      debugger;
    }),
    
    sleep: Static(function(ms)
    {
      var sleepConn = new ActiveXObject("Msxml2.ServerXMLHTTP");
      sleepConn.setTimeouts(0, 500, 0, 0);
      
      var sleepReqUrl = "http://127.0.0.1:1111";
      
      var beg = new Date();
      
      var count = Math.floor(ms / 500);
      var lastms = ms % 500;
     
      for(var i = 0; i < count + 1; i++)
      {
        var now = new Date();
        if(ms < now - beg)
        {
          break;
        }
        else
        {
          if(i == count)
          {
            sleepConn.setTimeouts(0, lastms, 0, 0);
          }
         
          //Sleep
          try
          {
            sleepConn.open("GET", sleepReqUrl, false, null, null);
            sleepConn.send();
          }
          catch(e)
          {
            // do nothing
          }
        }
      }
    }),
    
    callEvent: Static(function(obj, evnt)
    {
      if (evnt.indexOf("on") != 0)
      {
        evnt = "on" + evnt;
      }
      ThisClass().ie(function()
      {
        obj.fireEvent(evnt);
      });
      ThisClass().notIe(function()
      {
        var evt = document.createEvent('HTMLEvents');  
        evt.initEvent(evnt.substring(2), true, true);  
        obj.dispatchEvent(evt);   
      });
    }),
    
    using: Static(function(object, func)
    {
      try
      {
        func(object);
      }
      finally
      {
        ThisClass().dismiss(object);
      }
    }),
    
    dismiss: Static(function(object)
    {
      if (object != null)
      {
        if (InstanceOf(object, Destroyable))
        {
          try
          {
            object.destroy();
          }
          catch(e)
          {
            throw NewException(e);
          }
        }
        else if (InstanceOf(object, Closeable))
        {
          try
          {
            object.close();
          }
          catch(e)
          {
            throw NewException(e);
          }
        }
        else
        {
          if (object.close != null)
          {
            try
            {
              object.close();
            }
            catch(e)
            {
              throw NewException(e);
            }
          }
          else if (object.destroy != null)
          {
            try
            {
              object.destroy();
            }
            catch(e)
            {
              throw NewException(e);
            }
          }
          else
          {
            throw new Exception("Object has no close or destroy function.");
          }
        }
      }
    })
  });
})();

