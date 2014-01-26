/*
 * cn.aprilsoft.jsapp.ui.Console.js
 * jsapp, ui console functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  Class("cn.aprilsoft.jsapp.ui.Console", Extend(), Implement(),
  {
    _OutCtrl: null,
    
    _MessageBuffer: "",
    
    _AutoReflushFlag: true,
    
    Constructor: function(outctrl, flag)
    {
      if ((typeof(outctrl) == "undefined") || (outctrl == null))
      {
        throw new Exception("A output ctrl is needed.");
      }
      if (typeof(outctrl) == "string")
      {
        outctrl = document.getElementById(outctrl);
      }
      if (typeof(flag) == "boolean")
      {
        this._AutoReflushFlag = flag;
      }
      this._OutCtrl = outctrl;
    },
    
    write: function(msg)
    {
      if (msg == null)
      {
        msg = "@null";
      }
      this._MessageBuffer += msg.toString();
      if (this._AutoReflushFlag)
      {
        this.flush();
      }
    },
    
    print: function(msg)
    {
      this.write(msg);
    },
    
    writeln: function(msg)
    {
      this.write(msg + "\n");
    },
    
    println: function(msg)
    {
      this.writeln(msg);
    },
    
    setAutoReflushFlag: function(flag)
    {
      this._AutoReflushFlag = !!flag;
    },
    
    flush: function()
    {
      this._OutCtrl.value += this._MessageBuffer;
      this._MessageBuffer = "";
    }
  });
})();

