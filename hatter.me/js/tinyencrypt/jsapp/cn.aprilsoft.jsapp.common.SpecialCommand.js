/*
 * cn.aprilsoft.jsapp.common.SpecialCommand.js
 * jsapp, common special commands functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  Static.Class("cn.aprilsoft.jsapp.common.SpecialCommand", Extend(), Implement(),
  {
  });

  And = function()
  {
    if (arguments.length == 0)
    {
      return false;
    }
    
    for (var i = 0; i < arguments.length; i++)
    {
      if (!arguments[i])
      {
        return false;
      }
    }
    
    return true;
  }

  Or = function()
  {
    if (arguments.length == 0)
    {
      return false;
    }
    
    for (var i = 0; i < arguments.length; i++)
    {
      if (arguments[i])
      {
        return true;
      }
    }
    
    return false;
  }

  Not = function(value)
  {
    return (!value);
  }

  Equals = function()
  {
    if (arguments.length == 0)
    {
      return false;
    }
    
    var firstValue = arguments[0];
    
    for (var i = 1; i < arguments.length; i++)
    {
      if (firstValue != arguments[i])
      {
        return false;
      }
    }
    
    return true;
  }

  BitAnd = function(v1, v2)
  {
    return (v1 & v2);
  }

  BitOr = function(v1, v2)
  {
    return (v1 | v2);
  }

  XOr = function(v1, v2)
  {
    return (v1 ^ v2);
  }

  Add = function()
  {
    var value = 0;
    
    for (var i = 0; i < arguments.length; i++)
    {
      value += arguments[i];
    }
    
    return value;
  }

  Subtract = function(v1, v2)
  {
    return (v1 - v2);
  }

  Multiply = function()
  {
    var value = 1;
    
    for (var i = 0; i < arguments.length; i++)
    {
      value *= arguments[i];
    }
    
    return value;
  }

  Divide = function(v1, v2)
  {
    return (v1 / v2);
  }

  Power = function(v1, v2)
  {
    return Math.pow(v1, v2);
  }

  Max = function()
  {
    var value = arguments[0];
    
    for (var i = 1; i < arguments.length; i++)
    {
      if (value < arguments[i])
      {
        value = arguments[i];
      }
    }
    
    return value;
  }

  Min = function()
  {
    var value = arguments[0];
    
    for (var i = 1; i < arguments.length; i++)
    {
      if (value > arguments[i])
      {
        value = arguments[i];
      }
    }
    
    return value;
  }
})();

