/*
 * cn.aprilsoft.jsapp.text.Filter.js
 * jsapp, text filter functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.text
  Package("cn.aprilsoft.jsapp.text");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.text.Filter", Extend(), Implement(),
  {
    _whiteList: null,
    
    _blackList: null,
    
    _whiteFlag: true,
    
    Constructor: function(listOrStr, whiteFlag)
    {
      this._whiteList = [];
      this._blackList = [];
      
      var arr = listOrStr;
      
      if (System.isString(listOrStr))
      {
        arr.split("");
      }
      
      if (witeFlag === false)
      {
        this._whiteFlag = false;
        this._blackList = arr;
      }
      else
      {
        this._whiteFlag = true;
        this._whiteList = arr;
      }
    },
    
    doFilter: function(str)
    {
      if (str == null)
      {
        return null;
      }
      
      var r = [];
      
      for (var i = 0; i < str.length; i++)
      {
        var c = str.charAt(i);
        
        if (this._whiteFlag)
        {
          var hasFlag = false;
          
          System.walkArray(this._whiteList, function(idx, obj)
          {
            if ((!hasFlag) && (c == obj))
            {
                hasFlag = true;
            }
          });
          
          if (hasFlag)
          {
            r.push(c);
          }
        }
        else
        {
          var hasFlag = false;
          
          System.walkArray(this._blackList, function(idx, obj)
          {
            if ((!hasFlag) && (c == obj))
            {
                hasFlag = true;
            }
          });
          
          if (!hasFlag)
          {
            r.push(c);
          }
        }
      }
      
      return r.join("");
    }
  });
})();

