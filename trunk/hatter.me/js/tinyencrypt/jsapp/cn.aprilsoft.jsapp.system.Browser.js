/*
 * cn.aprilsoft.jsapp.system.Browser.js
 * jsapp, system browser functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");
  
  Static.Class("cn.aprilsoft.jsapp.system.Browser", Extend(), Implement(),
  {
    getScrollTop: Static(function()
    {
      var scrollPos;
      if (typeof(window.pageYOffset) != 'undefined')
      {
        scrollPos = window.pageYOffset;
      }
      else if ((typeof(document.compatMode) != 'undefined') && (document.compatMode != 'BackCompat'))
      {
        scrollPos = document.documentElement.scrollTop;
      }
      else if (typeof(document.body) != 'undefined')
      {
      	scrollPos = document.body.scrollTop;
      }
      return scrollPos;
    }),
    
    setScrollTop: Static(function(scrollPos)
    {
      if (typeof(window.pageYOffset) != 'undefined')
      {
        window.pageYOffset = scrollPos;
      }
      else if ((typeof(document.compatMode) != 'undefined') && (document.compatMode != 'BackCompat'))
      {
        document.documentElement.scrollTop = scrollPos;
      }
      else if (typeof(document.body) != 'undefined')
      {
        document.body.scrollTop = scrollPos;
      }
    })
  });
})();

