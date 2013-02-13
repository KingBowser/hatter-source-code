/*
 * cn.aprilsoft.jsapp.math.Math.js
 * jsapp, math functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.math
  Package("cn.aprilsoft.jsapp.math");
  
  // the system math
  var systemMath = Math;
  var systemNaN = NaN;
  var systemIsNaN = isNaN;
  var systemParseInt = parseInt;
  var systemParseFloat = parseFloat;

  Static.Class("cn.aprilsoft.jsapp.math.Math", Extend(), Implement(),
  {
    E:        Static(systemMath.E),
    LN2:      Static(systemMath.LN2),
    LN10:     Static(systemMath.LN10),
    LOG2E:    Static(systemMath.LOG2E),
    LOG10E:   Static(systemMath.LOG10E),
    PI:       Static(systemMath.PI),
    SQRT1_2:  Static(systemMath.SQRT1_2),
    SQRT2:    Static(systemMath.SQRT2),
    NaN:      Static(systemNaN),
    
    _isNumber: Static(function(object)
    {
      return ((typeof(object) == "number") || (object instanceof Number));
    }),
    
    _isValidNumber: Static(function(object)
    {
      return (ThisClass()._isNumber(object) && (!systemIsNaN(object)));
    }),
    
    getSystemMath: Static(function()
    {
      return systemMath;
    }),
    
    min: Static(function(/* var... number */)
    {
      return systemMath.min.apply(null, arguments);
    }),
    
    max: Static(function(/* var... number */)
    {
      return systemMath.max.apply(null, arguments);
    }),
    
    abs: Static(function(x)
    {
      return systemMath.abs(x);
    }),
    
    acos: Static(function(x)
    {
      return systemMath.acos(x);
    }),
    
    asin: Static(function(x)
    {
      return systemMath.asin(x);
    }),
    
    atan: Static(function(x)
    {
      return systemMath.atan(x);
    }),
    
    atan2: Static(function(y, x)
    {
      return systemMath.atan2(y, x);
    }),
    
    ceil: Static(function(x)
    {
      return systemMath.ceil(x);
    }),
    
    cos: Static(function(x)
    {
      return systemMath.cos(x);
    }),
    
    exp: Static(function(x)
    {
      return systemMath.exp(x);
    }),
    
    floor: Static(function(x)
    {
      return systemMath.floor(x);
    }),
    
    log: Static(function(x)
    {
      return systemMath.log(x);
    }),
    
    pow: Static(function(x, y)
    {
      return systemMath.pow(x, y);
    }),
    
    random: Static(function()
    {
      return systemMath.random();
    }),
    
    round: Static(function(x)
    {
      return systemMath.round(x);
    }),
    
    sin: Static(function(x)
    {
      return systemMath.sin(x);
    }),
    
    sqrt: Static(function(x)
    {
      return systemMath.sqrt(x);
    }),
    
    tan: Static(function(x)
    {
      return systemMath.tan(x);
    }),
    
    isNaN: Static(function(obj)
    {
      return systemIsNaN(obj);
    }),
    
    parseInt: Static(function(obj)
    {
      return systemParseInt(obj);
    }),
    
    parseFloat: Static(function(obj)
    {
      return systemParseFloat(obj);
    })
  });
})();

