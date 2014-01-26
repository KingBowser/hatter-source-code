/*
 * cn.aprilsoft.jsapp.inet.remoting.RemotingProvider.js
 * jsapp, remoting provider functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.inet.remoting
  Package("cn.aprilsoft.jsapp.inet.remoting");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Serializer = Using("cn.aprilsoft.jsapp.serialize.Serializer");
  var JSONSerializer = Using("cn.aprilsoft.jsapp.serialize.JSONSerializer");
  var HTTPConnection = Using("cn.aprilsoft.jsapp.inet.HTTPConnection");
  var InternetCall = Using("cn.aprilsoft.jsapp.inet.InternetCall");
  
  // remoting argument(s) type allowed only below:
  // int, long, float, double, string, array, map, object
  // internet call protocol:
  // send:
  // {
  //  function: "initService",
  //  options:
  //  {
  //    passport: "hatter",
  //    password: "123456"
  //  }
  // }
  // send:
  // {
  //  function: "callService",
  //  options:
  //  {
  //    sessionid: 1234567,
  //    className: cn.aprilsoft.remoting.HelloWorld,
  //    function: doHello,
  //    arguemnts: ["hatter jiang"]
  //  }
  // }
  Class("cn.aprilsoft.jsapp.inet.remoting.RemotingProvider", Extend(), Implement(),
  {
    _SERIALIZER: Static(new JSONSerializer()),
    
    _serverUrl: null,
    
    _sessionId: null,
    
    // a list of metaData (@see RemotingDelegate)
    _metaDataList: null,
    
    _inited: null,
    
    _initException: null,
    
    _passport: null,
    
    _password: null,
    
    Constructor: function(address, port, serviceName, readyHandle)
    {
      var strPort = (port == 80)? "": (":" + port);
      this._serverUrl = "http://" + address + ":" + port + "/" + serviceName;
      this._inited = false;
      
      this._initServiceAndGetMetaData(readyHandle);
    },
    
    isInited: function()
    {
      return this._inited;
    },
    
    hasException: function()
    {
      return (this._initException != null);
    },
    
    getException: function()
    {
      return this._initException;
    },
    
    getProtocolName: function()
    {
      return "jsrmt";
    },
    
    getMetaData: function(serviceName)
    {
      if (this._initException != null)
      {
        throw new Exception(this._initException);
      }
      if (!this.isInited())
      {
        throw new Exception("Remoting provider has not been inited.");
      }
      var targetMetaData = null;
      System.walkArray(this._metaDataList, function(i, m)
      {
        if (m["serviceName"] == serviceName)
        {
          targetMetaData = m;
          return true;
        }
        return false;
      });
      if (targetMetaData == null)
      {
        throw new Exception("Assigned service name cannot found: " + serviceName + ".");
      }
      return targetMetaData;
    },
    
    synchronizedCall: function(metaData, funcName, argumentList, callback)
    {
      try
      {
        var internetCall = this._makeInternetCall();
        var callServiceObject = this._makeCallServiceCallObject(funcName, metaData["className"], argumentList);
        return internetCall.waitInvoke("callService", callServiceObject);
      }
      catch(e)
      {
        throw new Exception(e);
      }
    },
    
    asynchronousCall: function(metaData, funcName, argumentList, callback)
    {
      try
      {
        var internetCall = this._makeInternetCall();
        var callServiceObject = this._makeCallServiceCallObject(funcName, metaData["className"], argumentList);
        internetCall.noWaitInvoke("callService", callServiceObject,
          {
            "onsuccess": function(result)
            {
              callback.onsuccess(result);
            },
            "onerror": function(exception)
            {
              callback.onerror(exception);
            }
          });
      }
      catch(e)
      {
        throw new Exception(e);
      }
    },
    
    _initServiceAndGetMetaData: function(readyHandle)
    {
      var internetCall = this._makeInternetCall();
      var initServiceObject = this._makeInitServiceCallObject(this._passport, this._password);
      internetCall.noWaitInvoke("initService", initServiceObject,
        {
          "onsuccess": (function(result)
          {
            this._inited = true;
            this._sessionId = result["sessionid"];
            this._metaDataList = result["metaDataList"];
            if ((this._sessionId == null) || (this._metaDataList == null))
            {
              this._inited = false;
              this._initException = new Exception("Init failed, sessionId or metaDataList is null.");
            }
            readyHandle();
          }).bind(this),
          "onerror": (function(exception)
          {
            this._initException = exception;
          }).bind(this)
        });
    },
    
    _makeInternetCall: function()
    {
      return new InternetCall(this._serverUrl, ThisClass()._SERIALIZER);
    },
    
    _makeInitServiceCallObject: function(passport, password)
    {
      var callObject =  {
                          "function": "initService",
                          "options":
                          {
                            "passport": passport,
                            "password": password
                          }
                        };
      return callObject;
    },
    
    _makeCallServiceCallObject: function(funcName, className, argumentList)
    {
      var callObject =  {
                          "function": "callService",
                          options:
                          {
                            "sessionid": this._sessionId,
                            "className": className,
                            "function": funcName,
                            "arguments": argumentList
                          }
                        };
      return callObject;
    }
  });
})();

