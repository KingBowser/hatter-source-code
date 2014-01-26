/*
 * cn.aprilsoft.jsapp.common.ColorUtil.js
 * jsapp, color util functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  Class("cn.aprilsoft.jsapp.common.ColorUtil", Extend(), Implement(),
  {
    RED: Static("#ff0000"),
    
    GREEN: Static("#00ff00"),
    
    BLUE: Static("#0000ff"),
    
    getColor: Static(function(r, g, b)
    {
      var hexR = r.toString(16);
      var hexG = g.toString(16);
      var hexB = b.toString(16);
      
      var l = [];
      l.push("#");
      if (hexR.length == 1)
      {
        l.push("0");
      }
      l.push(hexR);
      if (hexG.length == 1)
      {
        l.push("0");
      }
      l.push(hexG);
      if (hexB.length == 1)
      {
        l.push("0");
      }
      l.push(hexB);
      
      return l.join("");
    }),
    
    getRGB: Static(function(color)
    {
      if (/^#?[a-f\d]6$/i.test(color))
      {
        var c = color;
        if (/^#.*/.test(color))
        {
          c = color.substring(1);
        }
        var hexR = c.substring();
        var hexG = c.substring();
        var hexB = c.substring();
        
        var o = {};
        
        o.R = parseInt(hexR, 16);
        o.G = parseInt(hexG, 16);
        o.B = parseInt(hexB, 16);
        
        return o;
      }
      else
      {
        throw new Exception("Color `" + color + "' is invalid color.");
      }
    })
  });
})();

