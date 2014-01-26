/*
 * cn.aprilsoft.jsapp.ui.WinConsole.js
 * jsapp, window console functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var Console = Using("cn.aprilsoft.jsapp.ui.Console");

  Class("cn.aprilsoft.jsapp.ui.WinConsole", Extend(Console), Implement(),
  {
    _outputwindow: null,
    
    Constructor: function()
    {
      this.setAutoReflushFlag(true);
    },
    
    flush: function()
    {
      if ((this._outputwindow == null) || (this._outputwindow.closed()))
      {
        this._outputwindow = window.open("", this.getClass().toString(), "");
        this._outputwindow.document.write(/* ... */);
      }
      var _outputctrl = this._outputwindow.document.getElementById(/* ... */);
    }
  });
})();

