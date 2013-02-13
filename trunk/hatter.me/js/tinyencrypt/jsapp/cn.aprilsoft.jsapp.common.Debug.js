/*
 * cn.aprilsoft.jsapp.common.Debug.js
 * jsapp, debug
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  Class("cn.aprilsoft.jsapp.common.Debug", Extend(), Implement(),
  {
    _outconsole: null,
    
    _debug_console_box: Static("_debug_console_box"),
    
    countId: Static(0),
    
    setConsole: Static(function(console)
    {
      cn.aprilsoft.jsapp.common.Debug._outconsole = console;
    }),
    
    setConsoleBoxId: Static(function(id)
    {
      this._debug_console_box = id;
    }),
    
    message: Static(function()
    {
      var Debug = cn.aprilsoft.jsapp.common.Debug;
      var _debug = document.getElementById(this._debug_console_box);
      var msg = "";
      for (var i = 0; i < arguments.length; i++)
      {
        if (arguments[i] == null)
        {
          arguments[i] = "@null";
        }
        msg += (i == 0)? arguments[i].toString(): "," + arguments[i].toString();
      }
      var strId = Debug.countId.toString();
      var app0 = "";
      for (var i = 0; i < ( 4 - strId.length); i++)
      {
        app0 += "0";
      }
      strId = app0 + strId;
      if (Debug._outconsole == null)
      {
        if (_debug == null)
        {
          alert("Out console box '_debug_console_box' not found!");
          throw new Exception("Out console box '_debug_console_box' not found!");
        }
        _debug.value += "[DEBUG " + strId + ":" + msg + "]\n";
      }
      else
      {
        Debug._outconsole.write("[DEBUG " + strId + ":" + msg + "]\n");
      }
      Debug.countId++;
    }),
    
    exception: Static(function(e)
    {
      var Debug = cn.aprilsoft.jsapp.common.Debug;
      var exa = new Array();
      var trac = new Array();
      var call = arguments.callee.caller;
      var depth = 0;
      while (call != null)
      {
        trac.push(call.toString().split("\n")[0]);
        call = call.caller;
        depth++;
        
        if (depth >= 20)
        {
          trac.push("(more than 20 ...)");
          break;
        }
      }
      
      exa.push("EXCEPTION:   \n......." + trac.join("\n......."));
      exa.push(".......caused by:");
      for (var k in e)
      {
        if (k != "toString")
        {
          if (k == "stack")
          {
            var tmpStackList = e[k].toString().split("\n");
            if (tmpStackList[tmpStackList.length - 1] == "")
            {
              tmpStackList.pop();
            }
            exa.push("       " + k + ": " + tmpStackList.join("\n............. "));
          }
          else
          {
            exa.push("       " + k + ": " + e[k]);
          }
        }
      }
      Debug.message(exa.join("\n"));
    }),
    
    see: Static(function(obj, msg, filter)
    {
      var Debug = cn.aprilsoft.jsapp.common.Debug;
      Debug.message(">> >> >>  @SEEING OBJECT  >> >> >>");
      if (msg != null)
      {
        Debug.message("MESSAGE: " + msg);
      }
      Debug.message("TYPE: @" + typeof(obj));
      if (obj === null)
      {
        Debug.message("@null");
      }
      if (obj === undefined)
      {
        Debug.message("@undefined");
      }
      else if ((typeof(obj) == "object") || (typeof(obj) == "function"))
      {
        Debug.object(obj, msg, filter);
      }
      else
      {
        Debug.message(obj);
      }
      Debug.message("<< << <<  @SEEING OBJECT  << << <<");
    }),
    
    object: Static(function(obj, msg, filter)
    {
      var Debug = cn.aprilsoft.jsapp.common.Debug;
      var objattrlist = [];
      if (msg == null)
      {
        msg = " @NO NAME";
      }
      if (filter != null)
      {
        msg += " with filter: `" + filter + "`";
      }
      objattrlist.push("Object abbributes: " + msg);
      
      var regFilter = null;
      if (filter != null)
      {
        if (/^[a-z\?\*]+$/i.test(filter))
        {
          regFilter = eval("/^" + filter.replace(/\?/g, "\.?").replace(/\*/g, "\.*") + "$/i");
          Debug.message(regFilter);
        }
        else
        {
          Debug.message("Filter includes inleager charactors.");
        }
      }
      
      Debug.message("Object as string:" + obj);
      
      for (var k in obj)
      {
        if ((regFilter != null) && (k.match(regFilter) == null))
        {
          continue;
        }
        objattrlist.push(k + ": " + ((obj[k] == null)? "@null": 
                         ((obj[k] instanceof Function)? "@function {}": obj[k])));
      }
      Debug.message(objattrlist.join("\n            "));
    }),
    
    assert: Static(function(condition, errmsg, okmsg, errthrowexception)
    {
      var Debug = cn.aprilsoft.jsapp.common.Debug;
      if (errmsg == null)
      {
        errmsg = "@default failure message";
      }
      if (okmsg == null)
      {
        okmsg = "@default success message";
      }
      if (errthrowexception == null)
      {
        errthrowexception = true;
      }
      if (!condition)
      {
        Debug.message("Debug.assert(boolean, string) failed", errmsg);
        if (errthrowexception)
        {
          throw new Exception("Debug.assert(boolean, string) failed", errmsg);
        }
      }
      else
      {
        Debug.message("Debug.assert(boolean, string) success", okmsg);
      }
    }),
    
    _setDebugItFunction: Static(function(clsObject, k)
    {
      var Debug = cn.aprilsoft.jsapp.common.Debug;
      var oldFunc = clsObject[k];
      if ((typeof(oldFunc) == "function") || (oldFunc instanceof Function))
      {
        clsObject[k] = function()
        {
          var arguList = [];
          for (var i = 0; i < arguments.length; i++)
          {
            if (arguments[i] == null)
            {
              arguList.push("@null");
            }
            else
            {
              arguList.push(arguments[i].toString());
            }
          }
          
          Debug.message("call function:" + oldFunc._funcName
                      + "\n     ...... call with arguments:" + arguList.join(", "));
          var result = null;
          try
          {
            result = oldFunc.apply(this, arguments);
          }
          catch(e)
          {
            if (isNotException(e))
            {
              e = new Exception(e);
            }
            Debug.exception(e);
            throw e;
          }
          var showResult = "";
          if (typeof(result) == "undefined")
          {
            showResult = "@undefined";
          }
          else if (result === null)
          {
            showResult = "@null";
          }
          else
          {
            showResult = result.toString();
          }
          Debug.message("end function:" + oldFunc._funcName
                      + "\n     ...... return with result:" + showResult);
          return result;
        };
        clsObject[k]._funcName = oldFunc._funcName + "(DEBUG)";
      }
    }),
    
    _setDebugItFunctionStringOrArray: Static(function(clsObject, argK)
    {
      if (argK instanceof Array)
      {
        for (var i = 0; i < argK.length; i++)
        {
          ThisClass()._setDebugItFunction(clsObject, argK[i]);
        }
      }
      else
      {
        ThisClass()._setDebugItFunction(clsObject, argK);
      }
    }),
    
    _isRegExpOrNull: Static(function(arg)
    {
      return ((arg == null) || (arg instanceof RegExp));
    }),
    
    debugIt: Static(function(clsObject, argK)
    {
      var isRegExpOrNull = ThisClass()._isRegExpOrNull(argK);
      if (!isRegExpOrNull)
      {
        ThisClass()._setDebugItFunctionStringOrArray(clsObject, argK);
      }
      else
      {
        try
        {
          for (var k in clsObject)
          {
            if (argK != null)
            {
              if (k.match(argK) == null)
              {
                continue;
              }
            }
            ThisClass()._setDebugItFunction(clsObject, k);
          }
        }
        catch(e)
        {
          throw new Exception(e, "Error occured in enum clsObject");
        }
      }
      try
      {
        if (clsObject.prototype)
        {
          if (!isRegExpOrNull)
          {
            ThisClass()._setDebugItFunctionStringOrArray(clsObject.prototype, argK);
          }
          else
          {
            for (var k in clsObject.prototype)
            {
              if (argK != null)
              {
                if (k.match(argK) == null)
                {
                  continue;
                }
              }
              ThisClass()._setDebugItFunction(clsObject.prototype, k);
            }
          }
        }
      }
      catch(e)
      {
        throw new Exception(e, "Error occured in enum clsObject");
      }
    }),
    
    alertError: Static(function(e)
    {
      try
      {
        var l = [];
        for (var k in e)
        {
          l.push(k + ":" + e[k]);
        }
        alert(l.join("\n"));
      }
      catch(e2)
      {
        alert("Do enum error object error!" + e2);
      }
    }),
    
    debugWindow: Static(function(w, callback)
    {
      w.onerror = function() {
        if (arguments.length == 0)
        {
          Debug.message("Unknow error occured.");
        }
        else
        {
          Debug.message("Uncatched error occured(see object below).");
          for (var i = 0; i < arguments.length; i++)
          {
            Debug.message("argument[" + i + "]:" + arguments[i]);
          }
        }
        if (callback == null)
        {
          return false;
        }
        else
        {
          return callback.apply(arguments);
        }
      };
    }),
    
    showLoadedClasses: Static(function(root)
    {
      ThisClass().message("Loaded classes:\n" + ThisClass().loadedClasses(root).join("\n") + "\n");
    }),
    
    alertLoadedClasses: Static(function(root)
    {
      alert(ThisClass().loadedClasses(root).join("\n"));
    }),
    
    loadedClasses: Static(function(root)
    {
      var result = [];
      var roots = (root == null)? Package._rootPackageList: [root];
      for (var i = 0; i < roots.length; i ++)
      {
        result.push(roots[i])
        ThisClass()._loadedClasses(roots[i], result);
      }
      return result;
    }),
    
    _loadedClasses: Static(function(path, result)
    {
      var p = Using(path);
      for (var k in p)
      {
        if (k != "_package_name_id_field")
        {
          var len = path.length;
          len = (len == 2)? 3: len;
          result.push(ThisClass()._repeat(" ", (len - 1)) + "-" + k);
          if ((typeof(p[k]) == "object") && (typeof(p[k]["_package_name_id_field"]) != "undefined"))
          {
            ThisClass()._loadedClasses((path + "." + k), result);
          }
        }
      }
    }),
    
    _repeat: Static(function(c, n)
    {
      var r = [];
      for (var i = 0; i < n; i++)
      {
        r.push(c);
      }
      return r.join("");
    })
  });
})();

