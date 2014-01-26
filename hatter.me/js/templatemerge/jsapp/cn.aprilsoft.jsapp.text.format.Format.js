/*
 * cn.aprilsoft.jsapp.text.format.Format.js
 * jsapp, String text format functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.text.format
  Package("cn.aprilsoft.jsapp.text.format");

  Abstract.Class("cn.aprilsoft.jsapp.text.format.Format", Extend(), Implement(),
  {
    Constructor: function()
    {
      throw new Exception("Cannot create instance of this class.");
    },
    
    doFormat: Abstract(),
    
    format: function(text)
    {
      return this.doFormat(text);
    }
  });
})();

