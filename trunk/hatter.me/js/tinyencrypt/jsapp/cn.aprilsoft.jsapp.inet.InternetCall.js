/*
 * cn.aprilsoft.jsapp.inet.InternetCall.js
 * jsapp, inet remote call functions
 * Param type should be:
 * {
 *  "function": "name",
 *  "argument": [1, 2, 3, ...]
 * }
 *
 * Result type should be:
 * {
 *  "exception" : null,
 *  "result": result
 * }
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.inet
  Package("cn.aprilsoft.jsapp.inet");

  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Serializer = Using("cn.aprilsoft.jsapp.serialize.Serializer");
  var JSONSerializer = Using("cn.aprilsoft.jsapp.serialize.JSONSerializer");
  var HTTPConnection = Using("cn.aprilsoft.jsapp.inet.HTTPConnection");

  Class("cn.aprilsoft.jsapp.inet.InternetCall", Extend(), Implement(),
  {
    WAIT_INVOKE: Static("_waitInvoke"),
    
    NOWAIT_INVOKE: Static("_noWaitInvoke"),
    
    _default_serialized_provider: Static(new JSONSerializer()),
    
    _inet_call_server: null,
    
    _inet_default_invoke_type: null,
    
    _serialize_provider: null,

    Constructor: function(serverAddr, serializeProvider)
    {
      // default waitInvoke
      this._inet_default_invoke_type = ThisClass().WAIT_INVOKE;
      this._inet_call_server = serverAddr;
      this._serialize_provider = (serializeProvider == null)
                                 ? ThisClass()._default_serialized_provider
                                 : serializeProvider;
      if (!Implements(this._serialize_provider, Serializer))
      {
        throw new Exception("SerializeProvider must implements Serializer.");
      }
    },
    
    setDefaultInvokeType: function(invokeType)
    {
      this._inet_default_invoke_type = invokeType;
    },
    
    invoke: function()
    {
      if (this._inet_default_invoke_type == ThisClass().NOWAIT_INVOKE)
      {
        return ThisClass().prototype.noWaitInvoke.apply(this, arguments);
      }
      else
      {
        return ThisClass().prototype.waitInvoke.apply(this, arguments);
      }
    },
    
    waitInvoke: function(funcName)
    {
      var params = this._getParams(arguments, true);
      var invokeParam = this._createInvokeParam(funcName, params);
      var serializedInvokeParam = this._serialize_provider.serialize(invokeParam);
      
      var connection = new HTTPConnection();
      connection.open("POST", this._inet_call_server, false);
      connection.send(serializedInvokeParam);
      
      var responseText = connection.responseText;
      
      var result = this._serialize_provider.deserialize(responseText);
      if (result["exception"] == null)
      {
        return result["result"];
      }
      else
      {
        throw new Exception("Error occured in invoke:" + funcName, result["exception"]);
      }
    },
    
    // callback should be:
    // function(result, exception)
    // or
    // {
    //  onsuccess: function(result),
    //  onerror: function(exception)
    // }
    noWaitInvoke: function(funcName)
    {
      var This = this;
      var params = this._getParams(arguments, false);
      var invokeParam = this._createInvokeParam(funcName, params);
      var serializedInvokeParam = this._serialize_provider.serialize(invokeParam);
      
      var connection = new HTTPConnection();
      var callback = arguments[arguments.length - 1];
      connection.onsuccess = function()
      {
        var responseText = connection.responseText;
        var result = This._serialize_provider.deserialize(responseText);
        This._nowaitinvokecallback(callback, result);
      };
      connection.onerror = function()
      {
        var result = {"result": null, "exception": "return status: " + connection.status};
        This._nowaitinvokecallback(callback, result);
      };
      
      connection.open("POST", this._inet_call_server, true);
      connection.send(serializedInvokeParam);
    },
    
    _nowaitinvokecallback: function(callback, result)
    {
      if (result != null)
      {
        if (System.isFunction(callback))
        {
          callback(result["result"], result["exception"]);
        }
        else
        {
          if (result["exception"] == null)
          {
            callback.onsuccess(result["result"]);
          }
          else
          {
            callback.onerror(result["exception"]);
          }
        }
      }
    },
    
    _createInvokeParam: function(funcName, params)
    {
      var invokeParam = {};
      invokeParam["function"] = funcName;
      invokeParam["argument"] = params;
      
      return invokeParam;
    },
    
    _getParams: function(args, isIncludeLast)
    {
      var params = [];
      var minusCount = isIncludeLast? 0: 1;
      
      for (var i = 1; i < (args.length - minusCount); i++)
      {
        params.push(args[i]);
      }
      
      return params;
    }
  });
})();

