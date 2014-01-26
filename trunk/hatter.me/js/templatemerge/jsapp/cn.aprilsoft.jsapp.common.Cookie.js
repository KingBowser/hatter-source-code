/*
 * cn.aprilsoft.jsapp.common.Cookie.js
 * jsapp, cookie manage functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  Class("cn.aprilsoft.jsapp.common.Cookie", Extend(), Implement(),
  {
    MILLS_IN_ONE_DAY: Static(24 * 60 * 60 * 1000),
    
    // default tenyears
    _defaultExpiresDays: Static(365 * 10),
    
    setExpiresDays: Static(function(expiresDays)
    {
      ThisClass()._defaultExpiresDays = expiresDays;
    }),
    
    getCookie: Static(function(key)
    {
      var cookie = document.cookie;
      if (cookie == null)
      {
        return null;
      }
      var values = cookie.split(";");
      if (values == null)
      {
        return null;
      }
      for (var i = 0; i < values.length; i++)
      {
        if (values[i].indexOf("=") < 0)
        {
          continue;
        }
        var twoValues = values[i].split("=");
        if (unescape(twoValues[0].replace(/\s/, "")) == key)
        {
          return unescape(twoValues[1]); // get value
        }
      }
      return null;
    }),
    
    setCookie: Static(function(key, value, days)
    {
      days = (days == null)? ThisClass()._defaultExpiresDays: days;
      document.cookie = escape(key) + "=" + escape(value) + "; expires="
                      + new Date((new Date()).getTime() + (days * ThisClass().MILLS_IN_ONE_DAY));
    })
  });
})();

