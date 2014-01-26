/*
 * cn.aprilsoft.jsapp.common.ArrayUtil.js
 * jsapp, common array util. functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");
  
  var Map = Using("cn.aprilsoft.jsapp.collection.Map");

  Class("cn.aprilsoft.jsapp.common.ArrayUtil", Extend(), Implement(),
  {
    contains: Static(function(arr, obj)
    {
      for (var i = 0; i < arr.length; i++)
      {
        if (arr[i] == obj)
        {
          return true;
        }
      }
      
      return false;
    }),
    
    includes: Static(function(arr0, arr1)
    {
      for (var i = 0; i < arr1.length; i++)
      {
        if (!ThisClass().contains(arr0, arr1[i]))
        {
          return false;
        }
      }
      
      return true;
    }),
    
    includeAny: Static(function(arr0, arr1)
    {
      for (var i = 0; i < arr1.length; i++)
      {
        if (ThisClass().contains(arr0, arr1[i]))
        {
          return true;
        }
      }
      
      return false;
    }),
    
    walk: Static(function(arr, func)
    {
      for (var i = 0; i < arr.length; i++)
      {
        try
        {
          func(i, arr[i]);
        }
        catch(e)
        {
          throw new Exception(e, "Error occured when walk array at " + i + "!");
        }
      }
    }),
    
    match: Static(function(arr, func)
    {
      var result = [];
      ThisClass().walk(arr, function(i, o)
      {
        try
        {
          if (func(i, o))
          {
            result.push(o);
          }
        }
        catch(e)
        {
          throw new Exception(e, "Error occured when match array at " + i + "!");
        }
      });
      return result;
    }),
    
    pushArray: Static(function(arr0, arr1)
    {
      for (var i = 0; i < arr1.length; i++)
      {
        arr0.push(arr1[i]);
      }
    }),
    
    getCountEquals: Static(function(arr, obj)
    {
      var count = 0;
      
      ThisClass().walk(arr, function(i, obj0)
      {
        if (obj0 == obj)
        {
          count += 1;
        }
      });
      
      return count;
    }),
    
    getCountInArray: Static(function(arr0, arr1)
    {
      var count = 0;
      
      ThisClass().walk(arr, function(i, obj0)
      {
        if (ThisClass().contains(arr1, obj0))
        {
          count += 1;
        }
      });
      
      return count;
    }),
    
    reverse: Static(function(arr)
    {
      if (arr == null)
      {
        return null;
      }
      var newArr = [];
      for (var i = (arr.length - 1); i >= 0; i--)
      {
        newArr.push(arr[i]);
      }
      return newArr;
    }),
    
    max: Static(function(arr)
    {
      if (arr.length < 1)
      {
        return null;
      }
      var maxItem = arr[0];
      for (var i = 1; i < arr.length; i++)
      {
        if (maxItem < arr[i])
        {
          maxItem = arr[i];
        }
      }
      return maxItem;
    }),
    
    min: Static(function(arr)
    {
      if (arr.length < 1)
      {
        return null;
      }
      var minItem = arr[0];
      for (var i = 1; i < arr.length; i++)
      {
        if (minItem > arr[i])
        {
          minItem = arr[i];
        }
      }
      return minItem;
    }),
    
    distinct: Static(function(arr)
    {
      var result = [];
      var map = new Map();
      for (var i = 0; i < arr.length; i++)
      {
        var obj = arr[i];
        if (!map.contains(obj))
        {
          result.push(obj);
          map.put(obj, null);
        }
      }
      return result;
    }),
    
    first: Static(function(arr)
    {
      if ((arr != null) && (arr.length > 1))
      {
        return arr[0];
      }
      else
      {
        return null;
      }
    }),
    
    last: Static(function(arr)
    {
      if ((arr != null) && (arr.length > 1))
      {
        return arr[arr.length - 1];
      }
      else
      {
        return null;
      }
    })
  });
})();

