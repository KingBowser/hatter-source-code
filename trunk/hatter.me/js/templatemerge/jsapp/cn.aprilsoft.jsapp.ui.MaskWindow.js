/*
 * cn.aprilsoft.jsapp.ui.MaskWindow.js
 * jsapp, mask window functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  Class("cn.aprilsoft.jsapp.ui.MaskWindow", Extend(), Implement(),
  {
    _maskId: null,
    
    Constructor: function()
    {
      // create mask div node
    },
    
    // opacity is 0 ~ 100
    _cssMaskStyle: function(opacity)
    {
      var dotDig = "" + (opacity / 100);
      var css = "filter:alpha(opacity=" + opacity + ");" // IE
              + "-moz-opacity:" + dotDig + ";"          // Moz + FF
              + "opacity: " + dotDig + ";";            // CSS3 supported browser
      return css;
    }
    
    showMask: function()
    {
      // show mask div node
    },
    
    hideMask: function()
    {
      // hide mask div node
    },
    
    removeMask: function()
    {
      // remove mask node
    }
  });
})();

