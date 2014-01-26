/*
 * cn.aprilsoft.jsapp.common.QuickFunction.js
 * jsapp, common quick functions functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  Class("cn.aprilsoft.jsapp.common.QuickFunction", Extend(), Implement(),
  {
    Constructor: function()
    {
      throw new Exception("This class cannot create instance.");
    }
  });

  TO = "_cn.aprilsoft.jsapp.common.QuickFunction.TO"

  // usage: NewArray(1, 3, 5, 7, 9)
  //        NewArray(3, TO, 9)
  //        NewArray(1, 3, TO, 10, 11)
  NewArray = function()
  {
    var args = arguments;
    var newArray = [];
    
    // do check all numbers ex TO
    
    for (var i = 0; i < args.length; i++)
    {
      if (args[i] == TO)
      {
        throw new Exception("TO grammer error.");
      }
      
      if ((i < (args.length - 1)) && (args[i + 1] == TO))
      {
        if (i < (args.length - 2))
        {
          var min = args[i];
          var max = args[i + 2];
          
          if (min > max)
          {
            min = args[i + 2];
            max = args[i];
          }
          
          for (var j = min; j <= max; j++)
          {
            newArray.push(j);
          }
          
          i += 2;
        }
        else
        {
          throw new Exception("TO grammer error.");
        }
      }
      else
      {
        newArray.push(args[i]);
      }
    }
    
    return newArray;
  }
})();


