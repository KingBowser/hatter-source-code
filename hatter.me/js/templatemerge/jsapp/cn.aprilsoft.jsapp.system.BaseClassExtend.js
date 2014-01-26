/*
 * cn.aprilsoft.jsapp.system.BaseClassExtend.js
 * jsapp, base class extend functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  var StringUtil = Using("cn.aprilsoft.jsapp.common.StringUtil");
  var System = Using("cn.aprilsoft.jsapp.system.System");

  Static.Class("cn.aprilsoft.jsapp.system.BaseClassExtend", Extend(), Implement(),
  {
  });

  /******************************** String **************************************/

  if (!(String.prototype.getEncode))
  {
    String.prototype.getEncode = function()
    {
      var encodedString = "";
      encodeString = encodeURIComponent(this);
      return encodeString;
    };
    String.prototype.getEncode._funcName = "String#getEncode";
  }

  if (!(String.prototype.getDecode))
  {
    String.prototype.getDecode = function()
    {
      var decodedString = "";
      decodeString = decodeURIComponent(this);
      return decodeString;
    };
    String.prototype.getDecode._funcName = "String#getDecode";
  }

  if (!(String.prototype.lTrim))
  {
    String.prototype.lTrim = function()
    {
      return StringUtil.trimLeft(this);
    };
    String.prototype.lTrim._funcName = "String#lTrim";
  }

  if (!(String.prototype.rTrim))
  {
    String.prototype.rTrim = function()
    {
      return StringUtil.trimRight(this);
    };
    String.prototype.rTrim._funcName = "String#rTrim";
  }

  if (!(String.prototype.trim))
  {
    String.prototype.trim = function()
    {
      return this.rTrim().lTrim();
    };
    String.prototype.trim._funcName = "String#trim";
  }

  if (!(String.prototype.startsWith))
  {
    String.prototype.startsWith = function(objStr)
    {
      return StringUtil.startWith(this, objstr);
    };
    String.prototype.startsWith._funcName = "String#startsWith";
  }

  if (!(String.prototype.endsWith))
  {
    String.prototype.endsWith = function(objStr)
    {
      return StringUtil.endWith(this, objstr);
    };
    String.prototype.endsWith._funcName = "String#endsWith";
  }

  if (!(String.prototype.betweenWith))
  {
    String.prototype.betweenWith = function(objStrStart, objStrEnd)
    {
      return ((this.startsWith(objStrStart)) && (this.endsWith(objStrEnd)));
    };
    String.prototype.betweenWith._funcName = "String#betweenWith";
  }

  if (!(String.prototype.contains))
  {
    String.prototype.contains = function(objStr)
    {
      return StringUtil.contains(this, objStr);
    };
    String.prototype.contains._funcName = "String#contains";
  }

  /********************************* Array **************************************/
  if (!(Array.getFromArguments))
  {
    Array.getFromArguments = function(arguArrayObj)
    {
      var arrArguments = [];
      for (var i = 0; i < arguArrayObj.length; i++)
      {
        arrArguments.push(arguArrayObj[i]);
      }
      return arrArguments;
    };
    Array.getFromArguments._funcName = "Array#Static(getFromArguments)";
  }

  if (!(Array.prototype.findIndex))
  {
    Array.prototype.findIndex = function(obj)
    {
      for (var i = 0; i < this.length; i++)
      {
        if (this[i] == obj)
        {
          return i;
        }
      }
      return -1;
    };
    Array.prototype.findIndex._funcName = "Array#findIndex";
  }

  if (!(Array.prototype.contains))
  {
    Array.prototype.contains = function(obj)
    {
      if (this.findIndex(obj) >= 0)
      {
        return true;
      }
      return false;
    };
    Array.prototype.contains._funcName = "Array#contains";
  }

  if (!(Array.prototype.addUnique))
  {
    Array.prototype.addUnique = function(obj)
    {
      if (!(this.contains(obj)))
      {
        this.push(obj);
        return (this.length - 1);
      }
      return -1;
    };
    Array.prototype.addUnique._funcName = "Array#addUnique";
  }

  if (!(Array.prototype.remove))
  {
    Array.prototype.remove = function(obj)
    {
      var objindex = this.findIndex(obj);
      if (objindex >= 0)
      {
        this.removeIndex(objindex);
      }
      return objindex;
    };
    Array.prototype.remove._funcName = "Array#remove";
  }

  if (!(Array.prototype.removeAll))
  {
    Array.prototype.removeAll = function(obj)
    {
      var objindex = this.findIndex(obj);
      while (objindex >= 0)
      {
        this.removeIndex(objindex);
        objindex = this.findIndex(obj)
      }
    };
    Array.prototype.removeAll._funcName = "Array#removeAll";
  }

  if (!(Array.prototype.clear))
  {
    Array.prototype.clear = function()
    {
      var arraylength = this.length;
      if (arraylength > 0)
      {
        this.splice(0, arraylength);
      }
      return arraylength;
    };
    Array.prototype.clear._funcName = "Array#clear";
  }

  if (!(Array.prototype.removeIndex))
  {
    Array.prototype.removeIndex = function(objindex)
    {
      if ((objindex < 0) || (objindex >= this.length))
      {
        throw new Exception("Index: " + objindex + " is out of bound!");
      }
      this.splice(objindex, 1);
      return objindex;
    };
    Array.prototype.removeIndex._funcName = "Array#removeIndex";
  }

  if (!(Array.prototype.walk))
  {
    Array.prototype.walk = function(funcWalkArr)
    {
      System.walkObject(this, funcWalkArr);
    };
    Array.prototype.walk._funcName = "Array#walk";
  }

  if (!(Array.prototype.addFirst))
  {
    Array.prototype.addFirst = function(objObj)
    {
      return this.unshift(objObj);
    };
    Array.prototype.addFirst._funcName = "Array#addFirst";
  }

  if (!(Array.prototype.getFirst))
  {
    Array.prototype.getFirst = function()
    {
      if (this.length > 0)
      {
        return this[0];
      }
      else
      {
        return undefined;
      }
    };
    Array.prototype.getFirst._funcName = "Array#getFirst";
  }

  if (!(Array.prototype.popFirst))
  {
    Array.prototype.popFirst = function()
    {
      return this.shift();
    };
    Array.prototype.popFirst._funcName = "Array#popFirst";
  }

  if (!(Array.prototype.addLast))
  {
    Array.prototype.addLast = function(objObj)
    {
      return this.push(objObj);
    };
    Array.prototype.addLast._funcName = "Array#addLast";
  }

  if (!(Array.prototype.getLast))
  {
    Array.prototype.getLast = function()
    {
      if (this.length > 0)
      {
        return this[this.length - 1];
      }
      else
      {
        return undefined;
      }
    };
    Array.prototype.getLast._funcName = "Array#getLast";
  }

  if (!(Array.prototype.popLast))
  {
    Array.prototype.popLast = function()
    {
      return this.pop();
    };
    Array.prototype.popLast._funcName = "Array#popLast";
  }
  
  /********************************* Number **************************************/
  if (!(Number.prototype.times))
  {
    Number.prototype.times = function(obj)
    {
      if ((typeof(obj) == "function") || (obj instanceof Function))
      {
        return Number.prototype._functionTimes.apply(this, arguments);
      }
      else
      {
        return Number.prototype._objectTimes.apply(this, arguments);
      }
    };
    Number.prototype.times._funcName = "Number#times";
  }
  
  if (!(Number.prototype.repeat))
  {
    Number.prototype.repeat = function(value)
    {
      var result = [];
      for (var i = 0; i < this; i++)
      {
        result.push(value);
      }
      return result.join("");
    };
    Number.prototype.repeat._funcName = "Number#repeat";
  }
  
  if (!(Number.prototype._functionTimes))
  {
    Number.prototype._functionTimes = function(func)
    {
      var args = [];
      args.push(0);
      for (var i = 1; i < arguments.length; i++)
      {
        args.push(arguments[i]);
      }
      for (var i = 0; i < this; i++)
      {
        args[0] = i;
        func.apply(null, args);
      }
    };
    Number.prototype._functionTimes._funcName = "Number#_functionTimes";
  }
  
  if (!(Number.prototype._objectTimes))
  {
    Number.prototype._objectTimes = function(obj)
    {
      var result = [];
      for (var i = 0; i < this; i++)
      {
        result.push(obj);
      }
      return result;
    };
    Number.prototype._objectTimes._funcName = "Number#_objectTimes";
  }
  
  /********************************* Function **************************************/
  /* current empty */
})();

