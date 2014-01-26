/*
 * cn.aprilsoft.jsapp.ui.StatusBar.js
 * jsapp, status bar functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var System = Using("cn.aprilsoft.jsapp.system.System");
  var StringUtil = Using("cn.aprilsoft.jsapp.common.StringUtil");

  Class("cn.aprilsoft.jsapp.ui.StatusBar", Extend(), Implement(),
  {
    _backgroundColor: "silver",
    
    _statusobj: null,
    
    Constructor: function(statusobj)
    {
      if (statusobj == null)
      {
        throw new Exception("Status bar is not binded!");
      }
      if (System.isString(statusobj))
      {
        var findstatusobj = System.getElement(statusobj);
        if (findstatusobj == null)
        {
          throw new Exception("Find element `" + statusobj + "' failed.");
        }
        statusobj = findstatusobj;
      }
      statusobj.style.overflow = "hidden";
      statusobj.style.textOverflow = "ellipsis";
      this._statusobj = statusobj;
    },
    
    _show_html_status: function(statusmsg, transflag)
    {
      if (transflag == null)
      {
        transflag = true;
      }
      if (transflag)
      {
        statusmsg = StringUtil.replaceAllHtmlTag(statusmsg);
      }
      statusmsg = statusmsg.replace(/<\s*br\s*\/?\s*>/gi, "\\n");
      var htmlcode = "";
      htmlcode += ""
               +  "<table border='1px' style='width:100%;height:100%;font-size:12px;background:"
               + this._backgroundColor
               + ";'>"
               +  "<tr>"
               +  "<td style='width:50px;'>"
               +  "<b>STATUS</b>"
               +  "</td>"
               +  "<td>"
               +  statusmsg
               +  "</td>"
               +  "</tr>"
               +  "<table>"
               +  "";
      this._statusobj.innerHTML = htmlcode;
    },
    
    setBackgroundColor: function(backgroundColor)
    {
      this._backgroundColor = backgroundColor;
    },
    
    showStatus: function(statusmsg)
    {
      if (statusmsg == null)
      {
        statusmsg = "@null";
      }
      if (statusmsg == "")
      {
        statusmsg = "&nbsp;";
      }
      if (!(System.isString(statusmsg)))
      {
        statusmsg = statusmsg.toString();
      }
      this._show_html_status(statusmsg, true);
    }
  });
})();

