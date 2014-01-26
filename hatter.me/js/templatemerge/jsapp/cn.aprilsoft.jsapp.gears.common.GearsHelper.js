/*
 * cn.aprilsoft.jsapp.gears.common.GearsHelper.js
 * jsapp, gears helper functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.gears.common
  Package("cn.aprilsoft.jsapp.gears.common");

  Static.Class("cn.aprilsoft.jsapp.gears.common.GearsHelper", Extend(), Implement(),
  {
    DATABASE:     Static("beta.database"),
    DESKTOP:      Static("beta.desktop"),
    GEOLOCATION:  Static("beta.geolocation"),
    HTTPREQUEST:  Static("beta.httprequest"),
    LOCALSERVER:  Static("beta.localserver"),
    TIMER:        Static("beta.timer"),
    WORKPOOL:     Static("beta.workerpool"),
    
    requireGears: Static(function(askMessage, param)
    {
      if (!window.google || !google.gears)
      {
        if (confirm(askMessage))
        {
          location.href = "http://gears.google.com/?action=install&message=" + param.message
                        + "&return=" + param.returnUrl;
        }
        return false;
      }
      return true;
    }),
    
    isGearsAvailable: Static(function()
    {
      if (!window.google || !google.gears)
      {
        return false;
      }
      return true;
    }),
    
    create: Static(function(name, classVersion)
    {
      ThisClass()._checkGearsAvailabe();
      return google.gears.factory.create(name, classVersion);
    }),
    
    getBuildInfo: Static(function()
    {
      ThisClass()._checkGearsAvailabe();
      return google.gears.factory.getBuildInfo();
    }),
    
    getPermission: Static(function(siteName, imageUrl, extraMessage)
    {
      ThisClass()._checkGearsAvailabe();
      return google.gears.factory.getPermission(siteName, imageUrl, extraMessage);
    }),
    
    hasPermission: Static(function()
    {
      ThisClass()._checkGearsAvailabe();
      return google.gears.factory.hasPermission;
    }),
    
    getVersion: Static(function()
    {
      ThisClass()._checkGearsAvailabe();
      return google.gears.factory.version;
    }),
    
    _checkGearsAvailabe: Static(function()
    {
      if (!ThisClass().isGearsAvailable())
      {
        throw new Exception("Google gears is not available.");
      }
    })
  });
})();
