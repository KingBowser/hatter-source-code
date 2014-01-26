/*
 * cn.aprilsoft.jsapp.system.JSEvent.js
 * jsapp, system event functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.system.JSEvent", Extend(), Implement(),
  {
    _event: null,
    
    Constructor: function()
    {
      var This = this;
      var processedFlag = false;
      System.ie(function()
      {
        processedFlag = true;
        This._event = window.event;
      });
      System.ff(function()
      {
        processedFlag = true;
        var caller = arguments.callee.caller;
        while (caller != null)
        {
          if (caller.arguments[0] instanceof Event)
          {
            This._event = caller.arguments[0];
            return;
          }
          caller = caller.caller;
        }
        throw new Exception("No event found!");
      });
      if (!processedFlag)
      {
        throw new Exception("Not supported browser!");
      }
      this._initEvent();
    },
    
    _initEvent: function()
    {
      var This = this;
      System.ie(function()
      {
        This.x = This._event.x;
        This.y = This._event.y;
        This.sender = This._event.srcElement;
        This.offsetX = This._event.offsetX;
        This.offsetY = This._event.offsetY;
        // TODO do more ...
      });
      System.ff(function()
      {
        This.x = This._event.pageX;
        This.y = This._event.pageY;
        This.sender = This._event.target;
        This.offsetX = This._event.layerX;
        This.offsetY = This._event.layerY;
        // TODO do more ...
      });
    },
    
    getEvent: function()
    {
      return this._event;
    },
    
    stop: function()
    {
      var This = this;
      var returnValue;
      System.ie(function()
      {
        This._event.cancelBubble = true;
        returnValue = This._event.cancelBubble;
      });
      System.ff(function()
      {
        returnValue = This._event.stopPropagation();
      });
      return returnValue;
    },
    
    abort: function()
    {
      var This = this;
      var returnValue;
      System.ie(function()
      {
        This._event.returnValue = false;
        returnValue = This._event.returnValue;
      });
      System.ff(function()
      {
        returnValue = This._event.preventDefault();
      });
      return returnValue;
    }
  });
})();

