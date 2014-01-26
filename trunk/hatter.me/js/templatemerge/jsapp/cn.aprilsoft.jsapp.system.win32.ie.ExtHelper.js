/*
 * cn.aprilsoft.jsapp.system.win32.ie.ExtHelper.js
 * jsapp, win ie ext-helper functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system.win32.ie
  Package("cn.aprilsoft.jsapp.system.win32.ie");

  Static.Class("cn.aprilsoft.jsapp.system.win32.ie.ExtHelper", Extend(), Implement(),
  {
    getSrcLink: Static(function()
    {
      var srcEvent = external.menuArguments.event;
      var eventElement;
      if(typeof(srcEvent.clientX) == "undefined")
      {
        eventElement = external.menuArguments.document.elementFromPoint(srcEvent.pointerX, srcEvent.pointerY);
      }
      else
      {
        eventElement = external.menuArguments.document.elementFromPoint(srcEvent.clientX, srcEvent.clientY);
      }
      
      var srcAnchor;
      if (srcEvent.type == "MenuExtAnchor")
      {
        srcAnchor = eventElement;
        do
        {
          srcAnchor = srcAnchor.parentElement;
        } while (typeof(srcAnchor) != "HTMLAnchorElement");
        return srcAnchor;
      }
      else if (srcEvent.type == "MenuExtImage")
      {
        if (typeof(eventElement) == "HTMLAreaElement")
        {
          return eventElement;
        }
        else
        {
          var srcImage = eventElement;
          var srcAnchor = srcImage.parentElement;
          do
          {
            srcAnchor=srcAnchor.parentElement;
            if (typeof(srcAnchor) == "undefined")
            {
              return srcImage;
            }
          } while(typeof(srcAnchor) != "HTMLAnchorElement");
          return srcAnchor;
        }
      }
      else if (srcEvent.type == "MenuExtUnknown")
      {
        srcAnchor = eventElement;
        if(srcAnchor != null && srcAnchor.tagName != null && srcAnchor.tagName.toLowerCase() == "a")
        {
          return srcAnchor;
        }
        else
        {
          while(srcAnchor != null && srcAnchor.tagName != null && srcAnchor.tagName.toLowerCase() != "a")
          {
            srcAnchor = srcAnchor.parentElement;
            if(srcAnchor != null && srcAnchor.tagName != null && srcAnchor.tagName.toLowerCase() == "a")
            {
              return srcAnchor;
            }
          }
          
          if(eventElement != null && eventElement.tagName != null)
          {
            return eventElement;
          }
          else
          {
            throw new Exception("Unknown link!");
          }
        }
      }
    })
  });
})();