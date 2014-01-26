/*
 * cn.aprilsoft.jsapp.inet.HTTPConnection.js
 * jsapp, inet http connection functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.inet
  Package("cn.aprilsoft.jsapp.inet");

  Class("cn.aprilsoft.jsapp.inet.HTTPConnection", Extend(), Implement(),
  {
    READSTATE_UNINITIALIZED: 0,
    
    READSTATE_OPEN: 1,
    
    READSTATE_SENT: 2,
    
    READSTATE_RECEIVING: 3,
    
    READSTATE_LOADED: 4,
    
    METHOD_GET: "GET",
    
    METHOD_POST: "POST",
    
    METHOD_HEAD: "HEAD",
    
    METHOD_PUT: "PUT",
    
    METHOD_DELETE: "DELETE",
    
    METHOD_PROPFIND: "PROPFIND",
    
    METHOD_PROPPATCH: "PROPPATCH",
    
    METHOD_MKCOL: "MKCOL",
    
    METHOD_COPY: "COPY",
    
    METHOD_MOVE: "MOVE",
    
    METHOD_LOCK: "LOCK",
    
    METHOD_UNLOCK: "UNLOCK",
    
    METHOD_VERSION_CONTROL: "VERSION-CONTROL",
    
    METHOD_REPORT: "REPORT",
    
    METHOD_CHECKOUT: "CHECKOUT",
    
    METHOD_CHECKIN: "CHECKIN",
    
    METHOD_UNCHECKOUT: "UNCHECKOUT",
    
    METHOD_MKWORKSPACE: "MKWORKSPACE",
    
    METHOD_UPDATE: "UPDATE",
    
    METHOD_LABEL: "LABEL",
    
    METHOD_MERGE: "MERGE",
    
    METHOD_BASELINE_CONTROL: "BASELINE-CONTROL",
    
    METHOD_MKACTIVITY: "MKACTIVITY",
    
    METHOD_ORDERPATCH: "ORDERPATCH",
    
    METHOD_ACL : "ACL ",
    
    _xmlhttpconnection: null,
    
    _msxmlconnectionlist: ["Msxml2.XMLHTTP.4.0",
                           "Msxml2.XMLHTTP.3.0",
                           "Msxml2.XMLHTTP",
                           "Microsoft.XMLHTTP"],
    
    status: 0,
    
    readyState: 0,
    
    responseXML: null,
    
    responseText: null,
    
    responseStream: null,
    
    onreadystatechange: null,
    
    onsuccess: null,
    
    onerror: null,
    
    Constructor: function()
    {
      var This = this;
      var _statechangecallback = function()
      {
        This._updateStatus();
        try
        {
          if (This.onreadystatechange != null)
          {
            This.onreadystatechange();
          }
          else
          {
            This._defaultonreadystatechange();
          }
        }
        catch(e)
        {
          throw new Exception(e, "user onreadystatechange exception!");
        }
      };
      
      if (window.XMLHttpRequest)
      {
        this._xmlhttpconnection = new window.XMLHttpRequest();
        this._xmlhttpconnection.onreadystatechange = _statechangecallback;
      }
      else
      {
        var xmlhttpconnection = null;
        for (var i = 0; i < this._msxmlconnectionlist.length; i++)
        {
          try
          {
            xmlhttpconnection = new ActiveXObject(this._msxmlconnectionlist[i]);
            xmlhttpconnection.onreadystatechange = _statechangecallback;
            this._xmlhttpconnection = xmlhttpconnection;
            return;
          }
          catch(e)
          {
            /* SIGN:ECB [do nothing] */
          }
        }
        throw new Exception("create HTTPConnection failed!");
      }
    },
    
    _defaultonreadystatechange: function()
    {
      if(this.readyState == 4)
      {
        if (this.status == 200)
        {
          if (this.onsuccess != null)
          {
            this.onsuccess();
          }
        }
        else
        {
          if (this.onerror != null)
          {
            this.onerror();
          }
        }
      }
    },
    
    _updateStatus: function()
    {
      this.readyState = this._xmlhttpconnection.readyState;
      if (this.readyState == 4)
      {
        this.status = this._xmlhttpconnection.status;
        this.responseXML = this._xmlhttpconnection.responseXML;
        this.responseText = this._xmlhttpconnection.responseText;
        this.responseStream = this._xmlhttpconnection.responseStream;
      }
    },
    
    open: function()
    {
      var args = arguments;
      this._xmlhttpconnection.open(args[0], args[1], args[2], args[3], args[4]);
    },
    
    send: function()
    {
      if (arguments.length == 0)
      {
        this._xmlhttpconnection.send();
      }
      else if (arguments.length == 1)
      {
        this._xmlhttpconnection.send(arguments[0]);
      }
      else
      {
        throw new Exception("arguments count error!");
      }
      // change httpconnection status
      this._updateStatus();
    },
    
    abort: function()
    {
      this._xmlhttpconnection.abort();
    },
    
    setRequestHeader: function(arg0, arg1)
    {
      this._xmlhttpconnection.setRequestHeader(arg0, arg1);
    },
    
    getAllResponseHeaders: function()
    {
      return this._xmlhttpconnection.getAllResponseHeaders();
    },
    
    getResponseHeader: function(arg0)
    {
      return this._xmlhttpconnection.getResponseHeader(arg0);
    },
    
    getStatus: function()
    {
      return this._xmlhttpconnection.status;
    },
    
    getReadyState: function()
    {
      return this._xmlhttpconnection.readyState;
    },
    
    getResponseXML: function()
    {
      return this._xmlhttpconnection.responseXML;
    },
    
    getResponseText: function()
    {
      return this._xmlhttpconnection.responseText;
    },
    
    getResponseStream: function()
    {
      return this._xmlhttpconnection.responseStream;
    }
  });
})();

