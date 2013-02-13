/*
 * cn.aprilsoft.jsapp.log.Log.js
 * jsapp, log functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.log
  Package("cn.aprilsoft.jsapp.log");
  
  /**
   * Log level: debug < info < warn < error < fatal
   */
  Interface("cn.aprilsoft.jsapp.log.Log", Implement(),
  {
    DEBUG: Static(0),
    INFO: Static(1),
    WARN: Static(2),
    ERROR: Static(3),
    FATAL: Static(4),
    
    LOG_NAME_MAP: Static(
    [
      "debug",
      "info",
      "warn",
      "error",
      "fatal"
    ]),
    
    DEFAULT_LEVEL: Static(2), // default log level: warn
    
    /**
     * log debug
     */
    debug: Abstract( /* message*/ ),
    
    /**
     * log info
     */
    info: Abstract( /* message*/ ),
    
    /**
     * log warn
     */
    warn: Abstract( /* message*/ ),
    
    /**
     * log error
     */
    error: Abstract( /* message*/ ),
    
    /**
     * log fatal
     */
    fatal: Abstract( /* message*/ )
  });
})();

