/*
 * cn.aprilsoft.jsapp.ui.MessageBox.js
 * jsapp, ui messagebox functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var FileUtil = Using("cn.aprilsoft.jsapp.common.FileUtil");
  var StringUtil = Using("cn.aprilsoft.jsapp.common.StringUtil");

  Class("cn.aprilsoft.jsapp.ui.MessageBox", Extend(), Implement(),
  {
    NOBTN  : Static(0x00000000),
    
    OK     : Static(0x00000001),
    
    YES    : Static(0x00000002),
    
    NO     : Static(0x00000004),
    
    ABORT  : Static(0x00000008),
    
    CLOSE  : Static(0x00000010),
    
    CANCEL : Static(0x00000020),
    
    _BTN_OK     : Static("OK"),
    
    _BTN_YES    : Static("YES"),
    
    _BTN_NO     : Static("NO"),
    
    _BTN_ABORT  : Static("ABORT"),
    
    _BTN_CLOSE  : Static("CLOSE"),
    
    _BTN_CANCEL : Static("CANCEL"),
    
    _backgroundColor: Static("silver"),
    
    _getonebutton: Static(function(btnname, value)
    {
      var retbtnhtmlstr = "";
      retbtnhtmlstr += "<button "
                    +  "style=\"width:80px;height:22px;background:"
                    +  ThisClass()._backgroundColor
                    +  ";\""
                    +  " onclick=\""
                    +  "window.returnValue = "
                    +  value
                    +  ";"
                    +  "if (typeof(_message_box_close) == 'function')"
                    +  "{"
                    +  "_message_box_close();"
                    +  "}"
                    +  "window.close();"
                    +  "\""
                    +  ">"
                    +  btnname
                    +  "</button>";
      return retbtnhtmlstr;
    }),
    
    _getbuttons: Static(function(buttontype)
    {
      var MessageBox = cn.aprilsoft.jsapp.ui.MessageBox;
      var btnhtmlstrlist = [];
      if (buttontype != MessageBox.NOBTN)
      {
        if (buttontype & MessageBox.OK)
        {
          btnhtmlstrlist.push(MessageBox._getonebutton(MessageBox._BTN_OK, MessageBox.OK));
        }
        if (buttontype & MessageBox.YES)
        {
          btnhtmlstrlist.push(MessageBox._getonebutton(MessageBox._BTN_YES, MessageBox.YES));
        }
        if (buttontype & MessageBox.NO)
        {
          btnhtmlstrlist.push(MessageBox._getonebutton(MessageBox._BTN_NO, MessageBox.NO));
        }
        if (buttontype & MessageBox.ABORT)
        {
          btnhtmlstrlist.push(MessageBox._getonebutton(MessageBox._BTN_ABORT, MessageBox.ABORT));
        }
        if (buttontype & MessageBox.CLOSE)
        {
          btnhtmlstrlist.push(MessageBox._getonebutton(MessageBox._BTN_CLOSE, MessageBox.CLOSE));
        }
        if (buttontype & MessageBox.CANCEL)
        {
          btnhtmlstrlist.push(MessageBox._getonebutton(MessageBox._BTN_CANCEL, MessageBox.CANCEL));
        }
      }
      return btnhtmlstrlist.join("&nbsp;\n");
    }),
    
    showHTML: Static(function(title, htmlcontext, button, width, height, left, top)
    {
      var MessageBox = cn.aprilsoft.jsapp.ui.MessageBox;
      var outhtmlstr = "";
      var outhtmfilename = "c:\\asjsmsgbox04021127.htm";
      
      outhtmlstr += "<html>\n"
                 +  "<head>\n"
                 +  "<meta http-equiv='Content-Type' content='text/html; charset=shift-jis'>\n"
                 +  "<title>\n"
                 +  title + "\n"
                 +  "</title>\n"
                 +  "<script type=\"text/javascript\">\n"
                 +  "//<![CDATA[\n"
                 +  "window.returnValue = 0;\n"
                 +  "document.onkeypress = function(evnt)"
                 +  "{"
                 +  "  var e = event? event: evnt;"
                 +  "  if (e.keyCode == 27)" // test if esc
                 +  "  {"
                 +  "    window.close();"
                 +  "  }"
                 +  "};"
                 +  "//]]>\n"
                 +  "</script>\n"
                 +  "</head>\n"
                 +  "<body style=\"background:"
                 +  ThisClass()._backgroundColor
                 +  ";\">\n"
                 +  "<table style=\"width:100%;height:100%;border:0px;\">\n"
                 +  "<tr>\n"
                 +  "<td valign=\"top\">\n"
                 +  htmlcontext + "\n"
                 +  "</td>\n"
                 +  "</tr>\n"
    if (button != MessageBox.NOBTN)
    {
      outhtmlstr += "<tr>\n"
                 +  "<td align='center' style=\"height:40px;\">\n"
                 +  MessageBox._getbuttons(button) + "\n"
                 +  "</td>\n"
                 +  "</tr>\n"
    }
    outhtmlstr   += "</table>\n"
                 +  "</body>\n"
                 +  "</html>\n";
      
      FileUtil.writeTxtFile(outhtmfilename, outhtmlstr);
      if ((typeof(width) == "undefined") || (width == null))
      {
        width = 400;
      }
      if ((typeof(height) == "undefined") || (height == null))
      {
        height = 160;
      }
      if ((typeof(left) == "undefined") || (left == null))
      {
        left = (screen.width - width) / 2;
      }
      if ((typeof(top) == "undefined") || (top == null))
      {
        top = (screen.height - height) / 2;
      }
      var showmsgboxtype = ""
                         + "resizable:" + "yes" + ";"
                         + "help:" + "no" + ";"
                         + "dialogWidth:" + width + "px;"
                         + "dialogHeight:" + height + "px;"
                         + "dialogLeft:" + left + "px;"
                         + "dialogTop:" + top + "px;"
                         + "";
      return window.showModalDialog(outhtmfilename, null, showmsgboxtype);
    }),
    
    input: Static(function(title, content, defaultcontext, width, height, left, top, mutillineflag)
    {
      var MessageBox = cn.aprilsoft.jsapp.ui.MessageBox;
      if (mutillineflag == null)
      {
        mutillineflag = false;
      }
      var context = "";
      title = StringUtil.replaceAllHtmlTag(title);
      if (defaultcontext == null)
      {
        defaultcontext = "@null";
      }
      defaultcontext = defaultcontext.replace(/\\/g, "\\\\");
      defaultcontext = defaultcontext.replace(/\r/g, "\\r");
      defaultcontext = defaultcontext.replace(/\n/g, "\\n");
      defaultcontext = defaultcontext.replace(/"/g, "\\\"");
      var imputhtmlstr;
      if (mutillineflag)
      {
        imputhtmlstr = "<textarea id='_input_box' value='"
                     +  "' style='width:100%;height:100%;'></textarea>";
      }
      else
      {
        imputhtmlstr = "<input type='text' id='_input_box' value='"
                     +  "' style='width:100%;'>";
      }
      context += ""
              +  "<table style='width:100%;height:100%;'>"
              +  "<tr>"
              +  "<td style='height:30px;'>"
              +  StringUtil.replaceAllHtmlTag(content)
              +  "</td>"
              +  "</tr>"
              +  "<tr>"
              +  "<td>"
              +  imputhtmlstr
              +  "</td>"
              +  "<tr>"
              +  "</table>"
              +  "";
      var jsgetvalue = "";
      jsgetvalue += ""
                 +  "<script type='text/javascript'>\n"
                 +  "//<![CDATA[\n"
                 +  "function _message_box_close()\n"
                 +  "{\n"
                 +  "  if (window.returnValue == " + MessageBox.OK + ")\n"
                 +  "  {\n"
                 +  "    window.returnValue = _input_box.value;\n"
                 +  "  }\n"
                 +  "  else\n"
                 +  "  {\n"
                 +  "    window.returnValue = null;\n"
                 +  "  }\n"
                 +  "}\n"
                 +  "\n"
                 +  "window.returnValue = null;\n"
                 +  "\n"
                 +  "window.onload = function(){\n"
                 +  "_input_box.value = \"" + defaultcontext + "\";\n"
                 +  "};\n"
                 +  "\n"
                 +  "//]]>\n"
                 +  "</script>\n"
                 +  "";
      return MessageBox.showHTML(title, context + jsgetvalue,
                                 MessageBox.OK | MessageBox.CANCEL,
                                 width, height, left, top);
    }),
    
    show: Static(function(title, context, button, width, height, left, top)
    {
      var MessageBox = cn.aprilsoft.jsapp.ui.MessageBox;
      title = StringUtil.replaceAllHtmlTag(title);
      context = StringUtil.replaceAllHtmlTag(context);
      return MessageBox.showHTML(title, context, button, width, height, left, top);
    }),
    
    setBackgroundColor: Static(function(backgroundColor)
    {
      ThisClass()._backgroundColor = backgroundColor;
    })
  });
})();

