/*
 * cn.aprilsoft.jsapp.log.ConsoleLog.js
 * jsapp, log functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.log
  Package("cn.aprilsoft.jsapp.log");
  
  var Log = Using("cn.aprilsoft.jsapp.log.Log");
  var AbstractLog = Using("cn.aprilsoft.jsapp.log.AbstractLog");
  
  Class("cn.aprilsoft.jsapp.log.ConsoleLog", Extend(AbstractLog), Implement(),
  {
    Constructor: function(clazz)
    {
      this._clazz = clazz;
    },
    
    _doWrite: function(level, message)
    {
      if (level == Log.INFO)
      {
        console.info(this._getLevelName(level) + ": " + message);
      }
      else if (level == Log.DEBUG)
      {
        console.debug(this._getLevelName(level) + ": " + message);
      }
      else if (level == Log.WARN)
      {
        console.warn(this._getLevelName(level) + ": " + message);
      }
      else
      {
        console.error(this._getLevelName(level) + ": " + message);
      }
    }
  });
})();

