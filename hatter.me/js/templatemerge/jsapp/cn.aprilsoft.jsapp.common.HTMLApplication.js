/*
 * cn.aprilsoft.jsapp.common.HTMLApplication.js
 * jsapp, class & function for html applications
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  Class("cn.aprilsoft.jsapp.common.HTMLApplication", Extend(), Implement(),
  {
    Constructor: function()
    {
    },
    
    setTitle: function(Title)
    {
      document.title = Title;
    },
    
    getTitle: function()
    {
      return document.title;
    },
    
    setApplicationLoad: function(func)
    {
    },
    
    setApplicationUnLoad: function(func)
    {
    },
    
    setApplicationStartUp: function(func)
    {
    }
  });
})();

