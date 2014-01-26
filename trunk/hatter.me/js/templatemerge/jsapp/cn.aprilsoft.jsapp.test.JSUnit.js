/*
 * cn.aprilsoft.jsapp.test.JSUnit.js
 * jsapp, jsapp unit(JSUnit) functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.test
  Package("cn.aprilsoft.jsapp.test");

  Class("cn.aprilsoft.jsapp.test.JSUnit", Extend(), Implement(),
  {
    Constructor: function()
    {
    },
    
    _isunitmethod: function(methodname)
    {
      if (/^test.w*/.test(methodname))
      {
        return true;
      }
      return false;
    },
    
    doJSUnit: function()
    {
      var methodsList = [];
      for (var k in this)
      {
        if (this._isunitmethod(k))
        {
          methodsList.push(k);
        }
      }
      methodsList.sort();
      // do list
    },
    
    assert: function(condition, message)
    {
      this.assertTrue(condition, message);
    },
    
    assertTrue: function()
    {
      this.assertEqual(condition, true, message);
    },
    
    assertFalse: function(condition, message)
    {
      this.assertTrue((!(condition)), message);
    },
    
    assertEqual: function()
    {
    },
    
    assertNotEqual: function()
    {
      //call ! assertEqual
    },
    
    assertUndefined: function(condition, message)
    {
      this.assertEqual(typeof condition, "undefined", message);
    },
    
    assertNotUndefined: function (condition, message)
    {
      this.assertNotEqual(typeof condition, "undefined", message);
    },
    
    assertNull: function(condition, message)
    {
      this.assertEqual(condition, null, message);
    },
    
    assertNotNull: function(condition, message)
    {
      this.assertNotEqual(condition, null, message);
    },
    
    assertEmptyString: function(condition, message)
    {
      this.assertEqual(condition, "", message);
    },
    
    assertZeroNumber: function(condition, message)
    {
      this.assertEqual(condition, 0, message);
    },
    
    assertIsBoolean: function(condition, message)
    {
      // todo adn isNumber, isInteger, isString, isChar, isFunction,  ...
    },
  });
})();

