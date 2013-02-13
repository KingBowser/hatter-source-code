/*
 * cn.aprilsoft.jsapp.system.Random.js
 * jsapp, system random functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  // SIGNATURE:OTR [Hatter Jiang]@[2006/09/29]#[random seed does work now]

  Class("cn.aprilsoft.jsapp.system.Random", Extend(), Implement(),
  {
    _randomseed: 0,
    
    _defaultrandommin: 0,
    
    _defaultrandommax: 1,
    
    Constructor: function(seed)
    {
      this.setSeed(seed);
    },
    
    setSeed: function(seed)
    {
      if (seed == null)
      {
        seed = new Date().getMilliseconds();
      }
      this._randomseed = seed;
    },
    
    setDefaultRandomMin: function(randommin)
    {
      this._defaultrandommin = randommin;
    },
    
    setDefaultRandomMax: function(randommax)
    {
      this._defaultrandommax = randommax;
    },
    
    getNext: function(randommin, randommax)
    {
      if (randommin == null)
      {
        randommin = this._defaultrandommin;
      }
      if (randommax == null)
      {
        randommax = this._defaultrandommax;
      }
      if (randommin > randommax)
      {
        throw new Exception("randommin value is bigger than randommax value");
      }
      if (randommin == randommax)
      {
        throw new Exception("randommin value is equals randommax value");
      }
      var randomwidth = randommax - randommin;
      var randomvalue = Math.random();
      var randomoutput = Math.round(randomvalue * randomwidth);
      return (randomoutput + randommin);
    }
  });
})();

