/*
 * cn.aprilsoft.jsapp.ui.HighLight.js
 * jsapp, high light functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var DOMUtil = Using("cn.aprilsoft.jsapp.common.DOMUtil");

  Class("cn.aprilsoft.jsapp.ui.HighLight", Extend(), Implement(),
  {
    highlight: Static(function(node, te, cls, incls)
    {
      node = (node == null)? document.getElementsByTagName("body")[0]: node;
      if (document.body.createTextRange)
      {
        ThisClass()._ieHighlight(node, te, cls, incls);
      }
      else
      {
        ThisClass()._nonIeHighlight(node, te, cls, incls);
      }
    }),
    
    _ieHighlight: Static(function(node, te, cls, incls)
    {
      if ((te == null) || (te == ""))
      {
        return;
      }
      var r = document.body.createTextRange();
      r.moveToElementText(node);
      for (var i = 0; r.findText(te); i++)
      {
        var replace = false;
        if (incls != null)
        {
          for (var o = r.parentElement(); o != null ; o = o.parentElement)
          {
            if (DOMUtil.hasClass(o, incls))
            {
              replace = true;
              break;
            }
          }
        }
        else
        {
          replace = true;
        }
        if (replace)
        {
          r.pasteHTML('<span class="' + cls + '">' +  r.text + '<\/span>');
        }
        r.collapse(false);
      }
    }),
    
    _nonIeHighlight: Static(function(node, te, cls, incls)
    {
      if ((te == null) || (te == ""))
      {
        return;
      }
      var pos, skip, spannode, middlebit, endbit, middleclone;
      skip = 0;
      te = (te == null) ? null: te.toUpperCase();
      if (node.nodeType == 3)
      {
        var replace = false;
        if (incls != null)
        {
          for (var o = node.parentNode; o != null ; o = o.parentNode)
          {
            if (DOMUtil.hasClass(o, incls))
            {
              replace = true;
              break;
            }
          }
        }
        else
        {
          replace = true;
        }
        if (replace)
        {
          pos = node.data.toUpperCase().indexOf(te);
          if (pos >= 0)
          {
            spannode = document.createElement('span');
            spannode.className = cls;
            middlebit = node.splitText(pos);
            endbit = middlebit.splitText(te.length);
            middleclone = middlebit.cloneNode(true);
            spannode.appendChild(middleclone);
            middlebit.parentNode.replaceChild(spannode, middlebit);
            skip = 1;
          }
        }
      }
      else if ((node.nodeType == 1) && (node.childNodes) && (!(/(script|style)/i.test(node.tagName))))
      {
        for (var i = 0; i < node.childNodes.length; i++)
        {
          i += ThisClass()._nonIeHighlight(node.childNodes[i], te, cls);
        }
      }
      return skip;
    }),
    
    clearHighlight: Static(function(clazz)
    {
      var spans = DOMUtil.select("#span." + clazz);
      System.walkArray(spans, function(i, span)
      {
        with (span.parentNode)
        {
          replaceChild(span.firstChild, span);
          normalize();
        }
      });
    })
  });
})();

