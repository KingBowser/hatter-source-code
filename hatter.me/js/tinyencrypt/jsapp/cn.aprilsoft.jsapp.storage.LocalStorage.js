/*
 * cn.aprilsoft.jsapp.storage.LocalStorage.js
 * JScript, local storage functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.storage
  Package("cn.aprilsoft.jsapp.storage");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Storage = Using("cn.aprilsoft.jsapp.storage.Storage");
  var Serializer = Using("cn.aprilsoft.jsapp.serialize.Serializer");
  
  var GearsHelperF = Using.F("cn.aprilsoft.jsapp.gears.common.GearsHelper");
  
  var systemLocalStorage = window.localStorage;
  var systemGlobalStorage = window.globalStorage;
  var systemLocation = window.location;

  Class("cn.aprilsoft.jsapp.storage.LocalStorage", Extend(), Implement(Storage),
  {
    _IE_BEHAVIOR_ID: Static("localStorage"),
    
    _storage: Static(null),
    _behaviorObject: Static(null),
    _gearsDatabase: Static(null),
    
    _serializer: null,
    _accessor: null,
    
    Constructor: function(serializer)
    {
      if (serializer == null)
      {
        throw new Exception("Serializer cannot be null.");
      }
      if (!(serializer.InstanceOf(Serializer)))
      {
        throw new Exception("Serializer must implements Serializer interface.");
      }
      this._serializer = serializer;
      
      var storageReady = false;
      if (ThisClass()._storage == null)
      {
        ThisClass()._storage = this._getLocalStorageObject();
      }
      
      if (ThisClass()._storage != null)
      {
        storageReady = true;
        this._accessor =
        {
          read: this._readLocalStorage.bind(this),
          write: this._writeLocalStorage.bind(this)
        };
      }
      
      if (!storageReady)
      {
        if (GearsHelperF.isLoaded() && GearsHelperF().isGearsAvailable())
        {
          ThisClass()._gearsDatabase = GearsHelperF().create(GearsHelperF().DATABASE);
        }
        
        if (ThisClass()._gearsDatabase != null)
        {
          storageReady = true;
          ThisClass()._gearsDatabase.open(this._getDatabaseName());
          ThisClass()._gearsDatabase.execute("create table if not exists LocalStorage (key text PRIMARY KEY, value text)");
          this._accessor =
          {
            read: this._readGears.bind(this),
            write: this._writeGears.bind(this)
          };
        }
      }
      
      if (!storageReady)
      {
        if (ThisClass()._behaviorObject == null)
        {
          ThisClass()._behaviorObject = this._getIeBehaviorObject();
        }
        
        if (ThisClass()._behaviorObject != null)
        {
          storageReady = true;
          this._accessor =
          {
            read: this._readIeBehavior.bind(this),
            write: this._writeIeBehavior.bind(this)
          };
        }
      }
    },
    
    ready: function()
    {
      return (this._accessor != null);
    },
    
    write: function(key, value)
    {
      if (value != null)
      {
        value = this._serializer.serialize(value);
      }
      if (this._accessor != null)
      {
        return this._accessor.write(key, value);
      }
      throw new Exception("Browser does not support local storage.");
    },
    
    read: function(key)
    {
      if (this._accessor != null)
      {
        return this._serializer.deserialize(this._accessor.read(key));
      }
      throw new Exception("Browser does not support local storage.");
    },
    
    open: function()
    {
      // do nothing
    },
    
    close: function()
    {
      if (ThisClass()._gearsDatabase != null)
      {
        ThisClass()._gearsDatabase.close();
      }
    },
    
    _writeLocalStorage: function(key, value)
    {
      ThisClass()._storage[key] = value;
    },
    
    _readLocalStorage: function(key)
    {
      return ThisClass()._storage[key];
    },
    
    _writeIeBehavior: function(key, value)
    {
      if (value == null)
      {
        ThisClass()._behaviorObject.removeAttribute(this._getIeBehaviorKey(key));
      }
      else
      {
        ThisClass()._behaviorObject.setAttribute(this._getIeBehaviorKey(key), value);
      }
      ThisClass()._behaviorObject.save(ThisClass()._IE_BEHAVIOR_ID);
    },
    
    _readIeBehavior: function(key)
    {
      return ThisClass()._behaviorObject.getAttribute(this._getIeBehaviorKey(key));
    },
    
    _writeGears: function(key, value)
    {
      if (value == null)
      {
        ThisClass()._gearsDatabase.execute("delete from LocalStorage where key = ?", [key]);
      }
      else
      {
        var rs = ThisClass()._gearsDatabase.execute("select 1 from LocalStorage where key = ?", [key]);
        var hasKeyExists = rs.isValidRow();
        rs.close();
        if (hasKeyExists)
        {
          ThisClass()._gearsDatabase.execute("update LocalStorage set value = ? where key = ?", [value, key]);
        }
        else
        {
          ThisClass()._gearsDatabase.execute("insert into LocalStorage (key, value) values(?, ?)", [key, value]);
        }
      }
    },
    
    _readGears: function(key)
    {
      var result = null;
      var rs = ThisClass()._gearsDatabase.execute("select value from LocalStorage where key = ?", [key]);
      if (rs.isValidRow())
      {
        result = rs.fieldByName("value");
      }
      rs.close();
      return result;
    },
    
    _getIeBehaviorKey: function(key)
    {
      return "__key_" + key;
    },
    
    _getDatabaseName: function()
    {
      return ThisClass().getClassName() + "#localStoreage";
    },
    
    _getLocalStorageObject: function()
    {
      if (this._isSupportLocalStorage())
      {
        return systemLocalStorage;
      }
      if (this._isSuppoortGlobalStorage())
      {
        return systemGlobalStorage[this._getHostName()];
      }
      
      return null;
    },
    
    _getIeBehaviorObject: function()
    {
      var Clazz = ThisClass();
      var behaviorObject = null;
      System.ie(function()
      {
        var script = System.createElement("script");
        script.addBehavior("#default#userData");
        document.getElementsByTagName("head")[0].appendChild(script);
        script.load(Clazz._IE_BEHAVIOR_ID);
        behaviorObject = script;
      });
      
      return behaviorObject;
    },
    
    _isSupportLocalStorage: function()
    {
      return (!System.isUndefined(systemLocalStorage));
    },
    
    _isSuppoortGlobalStorage: function()
    {
      return (!System.isUndefined(systemGlobalStorage));
    },
    
    _getHostName: function()
    {
      return systemLocation.hostname;
    }
  });
})();

