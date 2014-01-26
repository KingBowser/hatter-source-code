/*
 * cn.aprilsoft.jsapp.html.dom.HtmlDom.js
 * jsapp, html dom functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.html.dom
  Package("cn.aprilsoft.jsapp.html.dom");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var ElementSelectorF = Using.F("cn.aprilsoft.jsapp.html.dom.ElementSelector");

  Class("cn.aprilsoft.jsapp.html.dom.HtmlDom", Extend(), Implement(),
  {
    _getFuncListName: Static(function(funcName)
    {
      return "_" + funcName + "_function_list";
    }),
    
    _getFuncPos: Static(function(funcList, func)
    {
      for (var i = 0; i < funcList.length; i++)
      {
        if (funcList[i] == func)
        {
          return i;
        }
      }
      
      return -1;
    }),
    
    _doFuncList: Static(function(funcList, dom, args)
    {
      var r;
      for (var i = 0; i < funcList.length; i++)
      {
        try
        {
          r = funcList[i].apply(dom, args);
        }
        catch(e)
        {
          // when error
          var ne = NewException(e);
          var l = [];
          for(var k in ne)
          {
            l.push(k + ":" + ne[k]);
          }
          alert(l.join("\n"));
        }
      }
      
      return r;
    }),
    
    clickElement: Static(function(element)
    {
      element = System.getElement(element);
      if (System.isIE())
      {
        element.click();
      }
      else
      {
        var evt = document.createEvent("MouseEvents");
        evt.initEvent("click", true, true);
        element.dispatchEvent(evt);
      }
    }),
    
    addEvent: Static(function(dom, funcName, func)
    {
      var eventName = funcName;
      if (/^on.*/.test(funcName))
      {
        eventName = funcName.substring(2);
      }
      if (dom.addEventListener)
      {
        dom.addEventListener(eventName, func, false);
      }
      else if (element.attachEvent)
      {
        dom.attachEvent("on" + eventName, func);
      }
      else
      {
        ThisClass().attachFunction(dom, eventName, func);
      }
    }),
    
    removeEvent: Static(function(dom, funcName, func)
    {
      var eventName = funcName;
      if (/^on.*/.test(funcName))
      {
        eventName = funcName.substring(2);
      }
      if (dom.removeEventListener)
      {
        dom.removeEventListener(eventName, func, false);
      }
      else if (element.detachEvent)
      {
        dom.detachEvent("on" + eventName, func);
      }
      else
      {
        ThisClass().removeFunction(dom, eventName, func);
      }
    }),
    
    attachFunction: Static(function(dom, funcName, func)
    {
      if (!(/^on.*/.test(funcName)))
      {
        funcName = "on" + funcName;
      }
      var funcList = dom[ThisClass()._getFuncListName(funcName)];
      
      if (funcList == null)
      {
        funcList = [];
        
        dom[ThisClass()._getFuncListName(funcName)] = funcList;
        
        if (dom[funcName] != null)
        {
          funcList.push(dom[funcName]);
        }
      }
      
      var r = false;
      
      if (ThisClass()._getFuncPos(funcList, func) < 0)
      {
        funcList.push(func);
        
        r = true;
      }
      
      var thisClass = ThisClass();
      
      dom[funcName] = function()
      {
        return thisClass._doFuncList(funcList, dom, arguments);
      };
      
      return r;
    }),
    
    removeFunction: Static(function(dom, funcName, func)
    {
      if (!(/^on.*/.test(funcName)))
      {
        funcName = "on" + funcName;
      }
      var funcList = dom[ThisClass()._getFuncListName(funcName)];
      
      if (funcList != null)
      {
        var idx = ThisClass()._getFuncPos(funcList, func);
        
        if (idx >= 0)
        {
          funcList.splice(idx, 1);
          return true;
        }
      }
      return false;
    }),
    
    hasClass: Static(function(node, clazz)
    {
      if ((node != null) && (node.className != null))
      {
        return (node.className.indexOf(clazz) >= 0);
      }
      else
      {
        return false;
      }
    }),
    
    addClass: Static(function(node, clazz)
    {
      if ((node != null) && (!(ThisClass().hasClass(node, clazz))))
      {
        node.className = node.className + " " + clazz;
      }
    }),
    
    removeClass: Static(function(node, clazz)
    {
      if (ThisClass().hasClass(node, clazz))
      {
        var classes = node.className.split(/\s+/);
        var newClasses = [];
        System.walkArray(classes, function(i, cls)
        {
          if (cls != clazz)
          {
            newClasses.push(cls);
          }
        });
        node.className = newClasses.join(" ");
      }
    }),
    
    removeAllClass: Static(function(node)
    {
      if (node != null)
      {
        node.className = "";
      }
    }),
    
    getAbsolutePostion: Static(function(element)
    {
      if (element == null)
      {
        return null;
      }
      try
      {
        var absX = 0;
        var absY = 0;
        while( element != null ) {
          absY += element.offsetTop;
          absX += element.offsetLeft;
          element = element.offsetParent;
        }
        return {
          x: absX,
          y: absY
        };
      }
      catch(e)
      {
        return null;
      }
    }),
    
    select: Static(function(selector)
    {
      return ElementSelectorF().getElementsBySelector(selector);
    }),
	
    getSelection: Static(function()
    {
      var txt;
      if (window.getSelection)
      {
        txt = window.getSelection();
      }
      else if (document.getSelection)
      {
        txt = document.getSelection();
      }
      else if (document.selection)
      {
        txt = document.selection.createRange().text;
      }
      else
      {
        throw new Exception("Get selection failed.");
      }
      return txt;
    })
  });
})();

