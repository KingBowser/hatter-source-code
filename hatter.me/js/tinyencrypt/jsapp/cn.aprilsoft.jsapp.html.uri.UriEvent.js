/*
 * cn.aprilsoft.jsapp.html.uri.UriEvent.js
 * jsapp, uri event functions
 * this class only catch parameters after #,
 * bescause parameters before # changes will cause server action event
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.html.uri
  Package("cn.aprilsoft.jsapp.html.uri");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Map = Using("cn.aprilsoft.jsapp.collection.Map");
  
  var GearsTimerF = Using.F("cn.aprilsoft.jsapp.gears.Timer");
  
  Class("cn.aprilsoft.jsapp.html.uri.UriEvent$Event", Extend(), Implement(),
  {
    id: null,
    key: null,
    func: null,
    
    Constructor: function(id, key, func)
    {
      this.id = id;
      this.key = key;
      this.func = func;
    },
    
    asString: function()
    {
      return ThisClass().getShortClassName() + " id: " + this.id + " event: " + this.key;
    }
  });
  
  var Event = Using("cn.aprilsoft.jsapp.html.uri.UriEvent$Event");
  
  Class("cn.aprilsoft.jsapp.html.uri.UriEvent", Extend(), Implement(),
  {
    _instance: Static(null),
    _currentCountId: null,
    _onAddFuncList: null,
    _onChangeFuncList: null,
    _onRemoveFuncList: null,
    _lastCheckedEventMap: null,
    _runningAutoCheckIntervalId: null,
    _doesAutoCheckUriParam: false,
    
    Constructor: Private(function()
    {
      System.checkIsClassMemberCaller();
      this._currentCountId = 0;
      this._onAddFuncList = [];
      this._onChangeFuncList = [];
      this._onRemoveFuncList = [];
      this._lastCheckedEventMap = this._splitParamToMap(this._getParamFromCurrentUri());
      
      this._initAutoCheckUriParam();
    }),
    
    instance: Static(function()
    {
      if (ThisClass()._instance == null)
      {
        ThisClass()._instance = ThisClass().newInstance();
      }
      return ThisClass()._instance;
    }),
    
    registerOnAdd: function(paramKey, func /* (key, oldValue, newVlaue) */)
    {
      this._onAddFuncList.push(new Event(this._currentCountId, paramKey, func));
      return this._currentCountId++;
    },
    
    registerOnChange: function(paramKey, func /* (key, oldValue, newVlaue) */)
    {
      this._onChangeFuncList.push(new Event(this._currentCountId, paramKey, func));
      return this._currentCountId++;
    },
    
    registerOnRemove: function(paramKey, func /* (key, oldValue, newVlaue) */)
    {
      this._onRemoveFuncList.push(new Event(this._currentCountId, paramKey, func));
      return this._currentCountId++;
    },
    
    unregister: function(id)
    {
      var removeResult;
      
      removeResult = this._removeIdFromFuncList(this._onAddFuncList, id);
      if (removeResult.hasId)
      {
        this._removeIdFromFuncList = removeResult.list;
        return true;
      }
      removeResult = this._removeIdFromFuncList(this._onChangeFuncList, id);
      if (removeResult.hasId)
      {
        this._onChangeFuncList = removeResult.list;
        return true;
      }
      removeResult = this._removeIdFromFuncList(this._onRemoveFuncList, id);
      if (removeResult.hasId)
      {
        this._onRemoveFuncList = removeResult.list;
        return true;
      }
      return false;
    },
    
    _removeIdFromFuncList: function(list, id)
    {
      var hasId = false;
      var result = [];
      for (var i = 0; i < list.length; i++)
      {
        var item = list[i];
        if (item.id == id)
        {
          hasId = true;
        }
        else
        {
          result.push(item);
        }
      }
      return {list: result, hasId: hasId};
    },
    
    _fireOnAdd: function(key, oldParamMap, newParamMap)
    {
      this._tryFireOnFuncList(this._onAddFuncList, key, oldParamMap, newParamMap);
    },
    
    _fireOnChange: function(key, oldParamMap, newParamMap)
    {
      this._tryFireOnFuncList(this._onChangeFuncList, key, oldParamMap, newParamMap);
    },
    
    _fireOnRemove: function(key, oldParamMap, newParamMap)
    {
      this._tryFireOnFuncList(this._onRemoveFuncList, key, oldParamMap, newParamMap);
    },
    
    _tryFireOnFuncList: function(onFuncList, key, oldParamMap, newParamMap)
    {
      var oldValue = oldParamMap.get(key);
      var newValue = newParamMap.get(key)
      
      for (var i = 0; i < onFuncList.length; i++)
      {
        var func = onFuncList[i];
        if ((func.key == null) || (func.key == key))
        {
          try
          {
            func.func(key, oldValue, newValue);
          }
          catch(e)
          {
            try
            {
              func.func.onerror(e);
            }
            catch(e2)
            {
              // ingore error
            }
          }
        }
      }
    },
    
    checkUriParam: function()
    {
      if (!this._doesAutoCheckUriParam)
      {
        // do not check
        return;
      }
      var newMap = this._splitParamToMap(this._getParamFromCurrentUri());
      var oldMap = new Map(this._lastCheckedEventMap); // this will clone a map
      
      var newMapKeyList = newMap.keyList();
      for (var i = 0; i < newMapKeyList.length; i++)
      {
        var key = newMapKeyList[i];
        if (!oldMap.contains(key))
        {
          // fire on add event
          this._fireOnAdd(key, this._lastCheckedEventMap, newMap);
          
          oldMap.remove(key);
        }
        else
        {
          if (oldMap.get(key) != newMap.get(key))
          {
            // fire on change event
            this._fireOnChange(key, this._lastCheckedEventMap, newMap);
          }
        }
      }
      var oldMapKeyList = oldMap.keyList();
      for (var i = 0; i < oldMapKeyList.length; i++)
      {
        var key = oldMapKeyList[i];
        // fire on remove event
        this._fireOnRemove(key, this._lastCheckedEventMap, newMap);
      }
      
      this._lastCheckedEventMap = newMap;
    },
    
    stopCheckUriParam: function()
    {
      this._destoryAutoCheckuriParam();
    },
    
    _initAutoCheckUriParam: function()
    {
      this._doesAutoCheckUriParam  = true;
      var id;
      if (GearsTimerF.isLoaded())
      {
        id = GearsTimerF().newInstance().setInterval(this.checkUriParam.bind(this), 100);
      }
      else
      {
        id = setInterval(this.checkUriParam.bind(this), 100);
      }
      this._runningAutoCheckIntervalId = id;
    },
    
    _destoryAutoCheckuriParam: function()
    {
      this._doesAutoCheckUriParam = false;
      if (GearsTimerF.isLoaded())
      {
        GearsTimerF().newInstance().clearInterval(this._runningAutoCheckIntervalId);
      }
      else
      {
        clearInterval(this._runningAutoCheckIntervalId);
      }
    },
    
    _getParamFromCurrentUri: function()
    {
      var mr = window.location.href.match(/.*#(.*)$/);
      if (mr == null)
      {
        return null;
      }
      else
      {
        return mr[1];
      }
    },
    
    _splitParamToMap: function(param)
    {
      var map = new Map();
      if (param != null)
      {
        var splitedByAndArray = param.split("&");
        for (var i = 0; i < splitedByAndArray.length; i++)
        {
          var keyValue = splitedByAndArray[i];
          var equalIndex = keyValue.indexOf("=");
          if (equalIndex < 0)
          {
            map.put(keyValue, "");
          }
          else
          {
            var key = keyValue.substring(0, equalIndex);
            var value = keyValue.substring(equalIndex + 1);
            map.put(key, value);
          }
        }
      }
      return map;
    }
  });
})();

