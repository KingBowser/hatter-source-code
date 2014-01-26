/*
 * cn.aprilsoft.jsapp.template.Template.js
 * jsapp, template functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.template
  Package("cn.aprilsoft.jsapp.template");

  Interface("cn.aprilsoft.jsapp.template.Template", Implement(),
  {
    /**
     * render template
     */
    render: Abstract(/* context , optFlags */) /* : rendered text */
  });
})();

