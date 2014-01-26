/*
 * cn.aprilsoft.jsapp.inet.remoting.Remoting.js
 * jsapp, remoting functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.inet.remoting
  Package("cn.aprilsoft.jsapp.inet.remoting");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var RemotingProvider = Using("cn.aprilsoft.jsapp.inet.remoting.RemotingProvider");
  var RemotingDelegate = Using("cn.aprilsoft.jsapp.inet.remoting.RemotingDelegate");
  
  Class("cn.aprilsoft.jsapp.inet.remoting.Remoting", Extend(), Implement(),
  {
    REMOTING_PROTOCOLS: Static(
    {
      jsrmt:  {
                name: "jsrmt",
                provider: RemotingProvider,
                port: 1127
              }
    }),
    
    _URI_PATTERN: /^(\w+):\/\/([0-9a-zA-Z_\-\.]+)(:(\d+))?(\/([0-9a-zA-Z_\-\+:\.\/]+))?$/,
    
    _DEFAULT_ERROR_HANDLE: function(funcName, argumentList, e)
    {
      alert(["funcName:", funcName, "\n", "arguments:", argumentList.join(", "), "\n", e].join(""));
    },
    
    _remoting_uri: null,
    
    _remoting_protocol: null,
    
    _remoting_address: null,
    
    _remoting_port: null,
    
    _remoting_serviceName: null,
    
    _remoting_provider: null,
    
    _error_handle: null,
    
    Constructor: function(uri, errorHandle, readyHandle)
    {
      this._remoting_uri = uri;
      this._error_handle = (errorHandle == null)? this._DEFAULT_ERROR_HANDLE: errorHandle;
      
      var _readyHandle = readyHandle? readyHandle: function(){};
      this._parseUri(uri, _readyHandle);
    },
    
    getRemoteObject: function(serviceName, isSynchronized)
    {
      if (isSynchronized)
      {
        return this.getSynchronizedRemoteObject(serviceName);
      }
      else
      {
        return this.getAsynchronousRemoteObject(serviceName);
      }
    },
    
    getSynchronizedRemoteObject: function(serviceName)
    {
      var metaData = this._remoting_provider.getMetaData(serviceName);
      return new RemotingDelegate(this._remoting_provider, metaData, RemotingDelegate.SYNCHRONIZED);
    },
    
    getAsynchronousRemoteObject: function(serviceName)
    {
      var metaData = this._remoting_provider.getMetaData(serviceName);
      return new RemotingDelegate(this._remoting_provider, metaData, RemotingDelegate.ASYNCHRONOUS,
                                  this._error_handle);
    },
    
    _parseUri: function(uri, readyHandle)
    {
      if (uri == null)
      {
        throw new Exception("Uri cannot be null.");
      }
      var matchUri = uri.match(this._URI_PATTERN);
      if (matchUri == null)
      {
        throw new Exception("Uri no correct exception:" + uri + ".");
      }
      this._remoting_protocol = matchUri[1];
      var remotingProtocol = ThisClass().REMOTING_PROTOCOLS[this._remoting_protocol];
      if (remotingProtocol == null)
      {
        throw new Exception("Not supported protocol: " + this._remoting_protocol + ".");
      }
      
      this._remoting_address = matchUri[2];
      this._remoting_port = (matchUri[4] == null)? remotingProtocol.port: parseInt(matchUri[4]);
      this._remoting_serviceName = matchUri[6];
      
      this._remoting_provider = new remotingProtocol.provider(this._remoting_address,
                                                              this._remoting_port,
                                                              this._remoting_serviceName,
                                                              readyHandle.bind(null, this));
    }
  });
})();

