/*
 * cn.aprilsoft.jsapp.log.AbstractLog.js
 * jsapp, log functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.log
  Package("cn.aprilsoft.jsapp.log");
  
  var Log = Using("cn.aprilsoft.jsapp.log.Log");
  
  Abstract.Class("cn.aprilsoft.jsapp.log.AbstractLog", Extend(), Implement(Log),
  {
    _logLevel: Static(null),
    _clazz: null,
    
    Class: Static(function()
    {
      ThisClass()._logLevel = Log.DEFAULT_LEVEL;
    }),
    
    Constructor: function(clazz)
    {
      this._clazz = clazz;
    },
    
    setLogLevel: Static(function(logLevel)
    {
      ThisClass()._logLevel = logLevel;
    }),
    
    getLogLevel: Static(function(logLevel)
    {
      return ThisClass()._logLevel;
    }),
    
    debug: function(message)
    {
      this._write(Log.DEBUG, message);
    },
    
    info: function(message)
    {
      this._write(Log.INFO, message);
    },
    
    warn: function(message)
    {
      this._write(Log.WARN, message);
    },
    
    error: function(message)
    {
      this._write(Log.ERROR, message);
    },
    
    fatal: function(message)
    {
      this._write(Log.FATAL, message);
    },
    
    _write: function(level, message)
    {
      if (level >= ThisClass()._logLevel)
      {
        this._doWrite(level, message);
      }
    },
    
    _getLevelName: function(level)
    {
      return Log.LOG_NAME_MAP[level];
    },
    
    _doWrite: Abstract(/* function(level, message) */)
  });
})();

