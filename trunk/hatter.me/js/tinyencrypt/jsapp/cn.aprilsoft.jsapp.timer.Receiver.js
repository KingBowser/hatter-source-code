/*
 * cn.aprilsoft.jsapp.timer.Receiver.js
 * jsapp, timer receiver functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.timer
  Package("cn.aprilsoft.jsapp.timer");
  
  var Timer = Using("cn.aprilsoft.jsapp.timer.Timer");
  var Destroyable = Using("cn.aprilsoft.jsapp.core.Destroyable");

  Class("cn.aprilsoft.jsapp.timer.Receiver", Extend(), Implement(Destroyable),
  {
    _timer: Static(new Timer()),
    
    _messageList: null,
    _intervalId: null,
    _messageFunc: null,
    _errorFunc: function(e)
    {
      alert(NewException(e));
    },
    
    Constructor: function()
    {
      this._messageList = [];
    },
    
    sendMessage: function(message)
    {
      this._messageList.push(message);
    },
    
    message: function(messageFunc)
    {
      this._messageFunc = messageFunc;
      return this;
    },
    
    error: function(errorFunc)
    {
      this._errorFunc = errorFunc;
      return this;
    },
    
    run: function()
    {
      this._intervalId = _timer.setInterval(this._intervalCheck.bind(this), 100);
    },
    
    destroy: function()
    {
      _timer.clearInterval(this._intervalId);
    },
    
    _intervalCheck: function()
    {
      if (this._messageList.length > 0)
      {
        var messageList = this._messageList;
        this._messageList = [];
        for (var i = 0; i < messageList.length; i++)
        {
          this._doMessage(messageList[i]);
        }
      }
    },
    
    _doMessage: function(message)
    {
      try
      {
        this._messageFunc(message);
      }
      catch(e)
      {
        this._errorFunc(NewException(e));
      }
    }
  });
})();

