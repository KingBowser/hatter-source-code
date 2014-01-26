/*
 * cn.aprilsoft.jsapp.text.StringMaker.js
 * jsapp, String maker functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.text
  Package("cn.aprilsoft.jsapp.text");

  Class("cn.aprilsoft.jsapp.text.StringMaker", Extend(), Implement(),
  {
    _stringbuffer: [],
    
    Constructor: function(str)
    {
      this._stringbuffer = [];
      if (typeof(str) != "undefined")
      {
        this._stringbuffer.push(str);
      }
    },
    
    append: function()
    {
      for (var i = 0; i < arguments.length; i++)
      {
        var tmpstr = "";
        if (arguments[i] != null)
        {
          tmpstr = arguments[i].toString();
        }
        this._stringbuffer.push(tmpstr);
      }
      return this;
    },
    
    getString: function()
    {
      return this._stringbuffer.join("");
    }
  });
})();

