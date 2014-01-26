/*
 * cn.aprilsoft.jsapp.common.PasswordCheck.js
 * jsapp, password check class
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  Class("cn.aprilsoft.jsapp.common.PasswordCheck", Extend(), Implement(),
  {
    Constructor: function(initpassword)
    {
      this.password = initpassword;
    },
    
    check: function()
    {
      if ((/[a-z]+/.test(this.password))
          && (/[A-Z]+/.test(this.password))
          && (/\d+/.test(this.password))
          && (/[^a-zA-Z0-9]+/.test(this.password)))
      {
        return ThisClass().LEVEL_HIGH;
      }
      if ((/[a-z]+/.test(this.password))
          && (/[A-Z]+/.test(this.password))
          && (/\d+/.test(this.password)))
      {
        return ThisClass().LEVEL_MIDDLE;
      }
      
      var filterPassword = this.password;
      filterPassword = filterPassword.replace(/[a-zA-Z\d]/g, "");
      if (filterPassword.length > 0)
      {
        return ThisClass().LEVEL_HIGH;
      }
      else
      {
        return ThisClass().LEVEL_LOW;
      }
    },
    
    LEVEL_HIGH: Static("_hight"),
    
    LEVEL_MIDDLE: Static("_middle"),
    
    LEVEL_LOW: Static("_low")
  });
})();

