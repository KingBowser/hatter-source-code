/*
 * cn.aprilsoft.jsapp.ui.Alarm.js
 * jsapp, ui flat button functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");
  
  var Debug = Using("cn.aprilsoft.jsapp.common.Debug");
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Thread = Using("cn.aprilsoft.jsapp.system.Thread");

  Class("cn.aprilsoft.jsapp.ui.Alarm", Extend(), Implement(),
  {
    ALARM_TYPE_MESSAGE: Static(parseInt("1", 2)),
    
    ALARM_TYPE_BLINKCOLOR: Static(parseInt("10", 2)),
    
    ALARM_BLINK_ON: Static("ON"),
    
    ALARM_BLINK_OFF: Static("OFF"),
    
    _alarmType: null,
    
    _showingList: null,
    
    _defaultShowTimes: 3, //(time(s))
    
    _defaultShowTimeTime: (1 * 1000), // (millisecond(s))
    
    _defaultShowTime: (10 * 1000), //(millisecond(s))
    
    _cancelLastMessage: false,
    
    _showMessage: false,
    
    _thread: null,
    
    _object: null,
    
    _messageAdapter: function(msg)
    {
      throw new Exception("_messageAdapter not setted.");
    },
    
    _blinkcolorAdapter: function(msg)
    {
      throw new Exception("_blinkcolorAdapter not setted.");
    },
    
    _showThreadSlice: function()
    {
      if (this._showingList.length < 1)
      {
        return;
      }
      
      var Alarm = cn.aprilsoft.jsapp.ui.Alarm;
      
      var messageObject = this._showingList[0];
      
      if (messageObject._messageAdapter == null)
      {
        messageObject._messageAdapter = this._messageAdapter;
      }
      if (messageObject._blinkcolorAdapter == null)
      {
        messageObject._blinkcolorAdapter = this._blinkcolorAdapter;
      }
      
      var msgObjExp = false;
      
      try
      {
        if (System.hasAttribute(messageObject.alarmType, Alarm.ALARM_TYPE_MESSAGE))
        {
          if (messageObject.startTime == null)
          {
            messageObject.startTime = new Date();
            messageObject._messageAdapter(messageObject.message);
            this._showMessage = true;
          }
          else
          {
            var now = new Date();
            
            if (System.getCostMilliseconds(messageObject.startTime, now)
              >= this._defaultShowTime)
            {
              msgObjExp = true;
            }
          }
        }
        
        if (System.hasAttribute(messageObject.alarmType, Alarm.ALARM_TYPE_BLINKCOLOR))
        {
          if (messageObject.leftTimes > 0)
          {
            msgObjExp = false;
            
            if (messageObject.colorOnOff == Alarm.ALARM_BLINK_ON)
            {
              var now = new Date();
              
              if (System.getCostMilliseconds(messageObject.timeStartTime, now)
                >= this._defaultShowTimeTime)
              {
                messageObject._blinkcolorAdapter(Alarm.ALARM_BLINK_OFF);
                messageObject.timeStartTime = now;
                messageObject.colorOnOff = Alarm.ALARM_BLINK_OFF;
                messageObject.leftTimes -= 1;
              }
            }
            else
            {
              if (messageObject.timeStartTime == null)
              {
                messageObject._blinkcolorAdapter(Alarm.ALARM_BLINK_ON);
                messageObject.timeStartTime = new Date();
                messageObject.colorOnOff = Alarm.ALARM_BLINK_ON;
              }
              else
              {
                var now = new Date();
                
                if (System.getCostMilliseconds(messageObject.timeStartTime, now)
                  >= this._defaultShowTimeTime)
                {
                  messageObject._blinkcolorAdapter(Alarm.ALARM_BLINK_ON);
                  messageObject.timeStartTime = now;
                  messageObject.colorOnOff = Alarm.ALARM_BLINK_ON;
                }
              }
            }
          }
          else
          {
            // TODO
          }
        }
      }
      catch(e)
      {
        msgObjExp = true;
        throw NewException(e);
      }
      finally
      {
        if (msgObjExp)
        {
          this._showingList.shift();
          if (this._showingList.length < 1)
          {
            if (System.hasAttribute(messageObject.alarmType, Alarm.ALARM_TYPE_MESSAGE))
            {
              messageObject._messageAdapter(null);
            }
            if (System.hasAttribute(messageObject.alarmType, Alarm.ALARM_TYPE_BLINKCOLOR))
            {
              messageObject._blinkcolorAdapter(null);
            }
            this._showMessage = false;
          }
        }
      }
    },
    
    Constructor: function(alarmType)
    {
      this._object = {};
      this._showingList = [];
      this._thread = new Thread();
      var This = this;
      this._thread.setTimeSlice(function()
      {
        This._showThreadSlice();
      });
      this._thread.setError(function(e)
      {
        Debug.alertError(e);
      });
      
      this._alarmType = alarmType;
      
      this._thread.start();
    },
    
    getObject: function()
    {
      return this._object;
    },
    
    setMessageAdapter: function(func)
    {
      this._messageAdapter = func;
    },
    
    setBlinkAdapter: function(func)
    {
      this._blinkcolorAdapter = func;
    },
    
    setShowTimes: function(times)
    {
      this._defaultShowTimes = times;
    },
    
    setShowTime: function(time)
    {
      this._defaultShowTime = time;
    },
    
    setCancelLastMessage: function(calcelLastMessage)
    {
      this._cancelLastMessage = calcelLastMessage;
    },
    
    getShowMessage: function()
    {
      return this._showMessage;
    },
    
    show: function(message, alarmType)
    {
      var Alarm = cn.aprilsoft.jsapp.ui.Alarm;
      
      var showMessageObject = {};
      showMessageObject.message = message;
      showMessageObject.leftTimes = this._defaultShowTimes;
      showMessageObject.timeStartTime = null;
      showMessageObject.startTime = null;
      showMessageObject.colorOnOff = Alarm.ALARM_BLINK_OFF;
      
      if (alarmType == null)
      {
        showMessageObject.alarmType = this._alarmType;
      }
      else
      {
        showMessageObject.alarmType = alarmType;
      }
      
      this._showingList.push(showMessageObject);
      
      if (this._cancelLastMessage)
      {
        if (this._showingList.length > 1)
        {
          var messageObject = this._showingList[0];
          messageObject.startTime = new Date("1900/1/1");
          messageObject.leftTimes = -1;
        }
      }
    }
  });
})();

