/*
 * cn.aprilsoft.jsapp.richclient.flash.Flash.js
 * jsapp, rich-client flash functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.richclient.flash
  Package("cn.aprilsoft.jsapp.richclient.flash");

  Class("cn.aprilsoft.jsapp.richclient.flash.Flash", Extend(), Implement(),
  {
    detectFlashVersion: Static(function()
    {
      var version = null;
      var highVersion = 16;
      var ua = navigator.userAgent.toLowerCase();
      if (navigator.plugins && navigator.plugins.length) 
      {
        var flashPlugin = navigator.plugins['Shockwave Flash'];
        if (typeof(flashPlugin) == 'object') 
        {
          for (var i = highVersion; i>=3; i--)
          {
            if (flashPlugin.description && flashPlugin.description.indexOf(' ' + i + '.') != -1)
            {
              version = i;
              break;
            }
          }
        }
        else if (navigator.mimeTypes && navigator.mimeTypes.length)
        {
          var flashPlugin = navigator.mimeTypes['application/x-shockwave-flash'];
          if (flashPlugin.enabledPlugin)
          {
            flashPlugin = flashPlugin.enabledPlugin;
            for (var i = highVersion; i>=3; i--)
            {
              if (flashPlugin.description && flashPlugin.description.indexOf(' ' + i + '.') != -1)
              {
                version = i;
                break;
              }
            }
          }
        }
        else if (typeof(navigator.plugins['Shockwave Flash 2.0']) == 'object')
        {
          version = 2;
        }
      }
      else if (ua.indexOf("msie") != -1 && window.execScript) 
      {
        var vbs = "ON ERROR RESUME NEXT\r\n"
                + "'DIM I\r\n"
                + "FOR I = highVersion TO 3 STEP -1 \r\n"
                + "  IF (IsObject(CreateObject(\"ShockwaveFlash.ShockwaveFlash.\" & I))) THEN\r\n"
                + "    version = I \r\n"
                + "  EXIT FOR\r\n"
                + "  END IF\r\n"
                + "NEXT\r\n"
                + "";
        window.execScript(vbs, "VBScript");
      }
      else if (ua.indexOf("webtv/2.5") != -1)
      {
        version = 3;
      }
      else if (ua.indexOf("webtv") != -1)
      {
        version = 2;
      }
      
      return version;
    })
  });
})();

