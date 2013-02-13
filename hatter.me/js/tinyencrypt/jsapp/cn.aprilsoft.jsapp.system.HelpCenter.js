/*
 * cn.aprilsoft.jsapp.system.HelpCenter.js
 * jsapp, system process functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  Class("cn.aprilsoft.jsapp.system.HelpCenter", Extend(), Implement(),
  {
    _helpcenterobject: Static(null),
    
    _helpactionlist: [],
    
    Constructor: function()
    {
      this._helpactionlist = [];
    },
    
    clearHelpAction: function()
    {
      this._helpactionlist = [];
    },
    
    addHelpAction: function(helpaction)
    {
      this._helpactionlist.push(helpaction);
    },
    
    removeHelpAction: function()
    {
      // TODO ...
    },
    
    setHelpAction: function(helpaction)
    {
      this.clearHelpAction();
      this.addHelpAction(helpaction);
    },
    
    _doHelpActions: Static(function()
    {
      var HelpCenter = cn.aprilsoft.jsapp.system.HelpCenter;
      if (HelpCenter._helpcenterobject != null)
      {
        var helpactionlist = HelpCenter._helpcenterobject._helpactionlist;
        for (var i = 0; i < helpactionlist.length; i++)
        {
          try
          {
            helpactionlist[i]();
          }
          catch(e)
          {
            throw new Exception(e, "do help action event error occured.");
          }
        }
      }
    }),
    
    registerHelpAction: function()
    {
      var HelpCenter = cn.aprilsoft.jsapp.system.HelpCenter;
      HelpCenter._helpcenterobject = this;
      window.onhelp = HelpCenter._doHelpActions;
    },
    
    unregisterHelpAction: function()
    {
      var HelpCenter = cn.aprilsoft.jsapp.system.HelpCenter;
      HelpCenter._helpcenterobject = null;
      window.onhelp = null;
    }
  });
})();

