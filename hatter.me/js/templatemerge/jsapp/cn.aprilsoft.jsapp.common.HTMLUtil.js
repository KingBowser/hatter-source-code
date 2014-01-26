/*
 * cn.aprilsoft.jsapp.common.HTMLUtil.js
 * jsapp, HTML util functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  var System = Using("cn.aprilsoft.jsapp.system.System");
  var StringUtil = Using("cn.aprilsoft.jsapp.common.StringUtil");

  Class("cn.aprilsoft.jsapp.common.HTMLUtil", Extend(), Implement(),
  {
    hightLight: Static(function(domHTMLContainer, lstKeyWords)
    {
      var obj = System.getElement(domHTMLContainer);
      try
      {
        var html = obj.innerHTML;
        // TODO
        obj.innerHTML = html;
      }
      catch(e)
      {
        throw NewException(e);
      }
    }),
    
    scrollToTop: Static(function(domElement)
    {
      var obj = System.getElement(domHTMLContainer);
      try
      {
        obj.scrollTop = 0;
      }
      catch(e)
      {
        throw NewException(e);
      }
    }),
    
    scrollToBottom: Static(function(domElement)
    {
      var obj = System.getElement(domHTMLContainer);
      try
      {
        obj.scrollTop = 0;
        obj.scrollTop = obj.scrollHeight - obj.clientHeight;
      }
      catch(e)
      {
        throw NewException(e);
      }
    })
  });
})();

