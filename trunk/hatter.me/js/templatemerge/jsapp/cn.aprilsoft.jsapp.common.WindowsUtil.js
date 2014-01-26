/*
 * cn.aprilsoft.jsapp.common.WindowsUtil.js
 * jsapp, Windows util functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  var MessageBox = Using("cn.aprilsoft.jsapp.ui.MessageBox");

  Class("cn.aprilsoft.jsapp.common.WindowsUtil", Extend(), Implement(),
  {
    askClose: Static(function(title, content)
    {
      if (title == null)
      {
        title = "Close window?";
      }
      if (content == null)
      {
        content = "\n       Are you really want to close this window?";
      }
      var buttons = MessageBox.YES | MessageBox.NO;
      var showmsgbox = MessageBox.show(title, content, buttons, null, 120);
      if (showmsgbox == MessageBox.YES)
      {
        cn.aprilsoft.jsapp.common.WindowsUtil.close();
      }
    }),
    
    close: Static(function()
    {
      window.close();
    }),
    
    setTitle: Static(function(titletext)
    {
      // TODO
    }),
    
    setStatus: Static(function(statustext)
    {
      // TODO
    }),
    
    _getbodyelement: Static(function()
    {
      var bodyelements = document.getElementsByTagName("body");
      if ((bodyelements == null) || (bodyelements.length < 1))
      {
        throw new Exception("Element body not found");
      }
      return bodyelements[0];
    }),
    
    setNoTopMargin: Static(function()
    {
      var WindowsUtil = cn.aprilsoft.jsapp.common.WindowsUtil;
      var bodyElement = WindowsUtil._getbodyelement();
      bodyElement.marginheight = "0";
      bodyElement.topmargin = "0";
    }),
    
    setNoLeftMargin: Static(function()
    {
      var WindowsUtil = cn.aprilsoft.jsapp.common.WindowsUtil;
      var bodyElement = WindowsUtil._getbodyelement();
      bodyElement.marginwidth = "0";
      bodyElement.leftmargin = "0";
    }),
    
    setNoBottomMargin: Static(function()
    {
      var WindowsUtil = cn.aprilsoft.jsapp.common.WindowsUtil;
      var bodyElement = WindowsUtil._getbodyelement();
      bodyElement.marginheight = "0";
      bodyElement.bottommargin = "0";
    }),
    
    setNoRightMargin: Static(function()
    {
      var WindowsUtil = cn.aprilsoft.jsapp.common.WindowsUtil;
      var bodyElement = WindowsUtil._getbodyelement();
      bodyElement.marginwidth = "0";
      bodyElement.rightmargin = "0";
    }),
    
    setNoMargin: Static(function()
    {
      var WindowsUtil = cn.aprilsoft.jsapp.common.WindowsUtil;
      WindowsUtil.setTopNoMargin();
      WindowsUtil.setLeftNoMargin();
      WindowsUtil.setBottomNoMargin();
      WindowsUtil.setRightNoMargin();
    })
  });
})();

