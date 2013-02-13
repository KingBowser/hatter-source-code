/*
 * cn.aprilsoft.jsapp.inet.remoting.RemotingDelegate.js
 * jsapp, remoting delegate functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.inet.remoting
  Package("cn.aprilsoft.jsapp.inet.remoting");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  
  Class("cn.aprilsoft.jsapp.inet.remoting.RemotingDelegate", Extend(), Implement(),
  {
    SYNCHRONIZED: Static(0),
    
    ASYNCHRONOUS: Static(1),
    
    ARGUMENT_CHECK_MAP: {
                          "string":   System.isString,
                          "char":     System.isChar,
                          "number":   System.isNumber,
                          "integer":  System.isInteger,
                          "double":   System.isNumber,
                          "boolean":  System.isBoolean,
                          "array":    System.isArray,
                          "regex":    System.isRegExp,
                          "date":     System.isDate,
                          "object":   System.isObject
                        },
    
    _provider: null,
    
    // format
    // {
    //  serviceName: serverSideService,
    //  className: cn.aprilsoft.remotint.ServerSideClass,
    //  functions: [
    //              {
    //                name: getUserName,
    //                arguments: [
    //                            {name: "id", type: "int", option: false},
    //                            {name: "area", type: "string", option: false},
    //                            {name: "opt", type: "boolean", option: true}
    //                           ]
    //              }
    //             ]
    // }
    _metaData: null,
    
    // SYNCHRONIZED or ASYNCHRONOUS
    _callType: null,
    
    // only for asynchronous call
    // function(funcName, argumentsList, ex)
    _errorHandle: null,
    
    Constructor: function(provider, metaData, callType, errorHandle)
    {
      this._provider = provider;
      this._metaData = metaData;
      this._callType = callType;
      this._errorHandle = errorHandle;
      
      this._internalImplementRemoteObject(this._metaData);
    },
    
    isSynchronized: function()
    {
      return (this._callType == ThisClass().SYNCHRONIZED);
    },
    
    _internalSynchronizedCall: function(funcName, argumentList)
    {
      try
      {
        return this._provider.synchronizedCall(this._metaData, funcName, argumentList);
      }
      catch(e)
      {
        throw new Exception(e);
      }
    },
    
    _internalAsynchronousCall: function(funcName, argumentList)
    {
      try
      {
        var callback = argumentList[argumentList.length - 1];
        this._provider.asynchronousCall(this._metaData, funcName,
                                        argumentList.slice(0, (argumentList.length - 1)), 
          {
            onsuccess: this._internalAsynchronousCallSuccess.bind(this, funcName, argumentList, callback),
            onerror: this._internalAsynchronousCallError.bind(this, funcName, argumentList, callback)
          });
      }
      catch(e)
      {
        if (!(this._errorHandle(funcName, argumentList, e)))
        {
          throw new Exception(e);
        }
      }
    },
    
    _internalAsynchronousCallSuccess: function(funcName, argumentList, callback, result)
    {
      callback(result);
    },
    
    _internalAsynchronousCallError: function(funcName, argumentList, callback, e)
    {
      var result;
      if (!callback(result, e))
      {
        if (!(this._errorHandle(funcName, argumentList, e)))
        {
          throw new Exception(e);
        }
      }
    },
    
    _internalCall: function(functionMetaData, argumentList)
    {
      var funcName = functionMetaData["name"];
      this._internalCheckArguments(functionMetaData, argumentList);
      
      if (this.isSynchronized())
      {
        return this._internalSynchronizedCall(funcName, argumentList);
      }
      else
      {
        return this._internalAsynchronousCall(funcName, argumentList);
      }
    },
    
    _internalImplementRemoteObjectFunction: function(functionMetaData)
    {
      var funcName = functionMetaData["name"];
      var argumentNames = this._internalGetArgumentNames(functionMetaData);
      var func = ["function "];
      func.push("(", argumentNames.join(", "), ")", " {", "\n");
      func.push("  var argumentList = System.toArray(arguments);", "\n");
      func.push("  return this._internalCall(functionMetaData, argumentList);", "\n");
      func.push("}");
      
      eval("this[funcName] = " + func.join(""));
      this[funcName]._clientFuncName = ThisClass()._className + "#" + funcName;
      this[funcName]._serverFuncName = this._metaData["className"] + "#" + functionMetaData["name"];
      this[funcName]._funcName = this[funcName]._clientFuncName + "(" + this[funcName]._serverFuncName + ")";
    },
    
    _internalImplementRemoteObject: function(metaData)
    {
      var functionList = metaData["functions"];
      System.walkArray(functionList, (function(i, m)
      {
        this._internalImplementRemoteObjectFunction(m);
      }).bind(this));
    },
    
    _internalGetArgumentNames: function(functionMetaData)
    {
      var argumentNameList = [];
      System.walkArray(functionMetaData["arguments"], function(i, m)
      {
        argumentNameList.push(m["name"]);
      });
      return argumentNameList;
    },
    
    _internalCheckArguments: function(functionMetaData, argumentList)
    {
      var checkArgumentList = argumentList;
      if (!(this.isSynchronized()))
      {
        if ((argumentList.length == 0) || (!System.isFunction(argumentList[argumentList.length - 1])))
        {
          throw new Exception("Asynchronous call should have callback.");
        }
        checkArgumentList = argumentList.slice(0, (argumentList.length - 1));
      }
      if (functionMetaData["arguments"].length < checkArgumentList.length)
      {
        throw new Exception("Too many arguments call this function.");
      }
      System.walkArray(functionMetaData["arguments"], (function(i, m)
      {
        var an = m["name"];
        var type = m["type"];
        var opt = m["option"];
        
        var checkFunc = this.ARGUMENT_CHECK_MAP[type];
        if (checkFunc == null)
        {
          throw new Exception("Unknow remot type: " + type + ".");
        }
        if (checkArgumentList.length <= i)
        {
          if (!opt)
          {
            throw new Exception("Not option arguement not assigned: " + an + ".");
          }
        }
        var value = checkArgumentList[i];
        if (value != null)
        {
          if (!checkFunc.bind(System)(value))
          {
            throw new Exception("Call remote function argument type error: " + an + ", type should be: " + type + ".");
          }
        }
      }).bind(this));
    }
  });
})();

