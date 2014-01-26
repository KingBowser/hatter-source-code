/*
 * cn.aprilsoft.jsapp.timer.TimeLine.js
 * jsapp, timer time line functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.timer
  Package("cn.aprilsoft.jsapp.timer");
  
  var Timer = Using("cn.aprilsoft.jsapp.timer.Timer");
  var Until = Using("cn.aprilsoft.jsapp.timer.Until")
  
  Interface("cn.aprilsoft.jsapp.timer.TimeLine$Event", Implement(),
  {
    doEvent: Abstract(),
    isDone: Abstract() /* : boolean */
  });
  
  var TimeLineEvent = Using("cn.aprilsoft.jsapp.timer.TimeLine$Event");
  
  Class("cn.aprilsoft.jsapp.timer.TimeLine", Extend(), Implement(),
  {
    _events: null,
    _index: null,
    
    Constructor: function()
    {
      this._events = [];
    },
    
    add: function(event)
    {
      if (!InstanceOf(event, TimeLineEvent))
      {
        this._events.push(event);
      }
    },
    
    start: function()
    {
      this._index = 0;
      this._doEvent();
    },
    
    _doEvent: function()
    {
      if (this._index < this._events.length)
      {
        var event = this._events[this._index++];
        event.doEvent();
        
        Until.condition(function()
        {
          return event.isDone();
        })
        .done(this._doEvent.bind(this))
        .check();
      }
    }
  });
})();

