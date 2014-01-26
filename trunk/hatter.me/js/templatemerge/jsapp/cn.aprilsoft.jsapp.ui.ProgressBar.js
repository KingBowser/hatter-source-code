/*
 * cn.aprilsoft.jsapp.ui.ProgressBar.js
 * jsapp, process bar functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.ui.ProgressBar", Extend(), Implement(),
  {
    PROCESS_BAR_PERCENT : Static("_process_bar_percent"),
    
    PROCESS_BAR_PIXEL   : Static("_process_bar_pixel"),
    
    _process_bar_status : null,
    
    _width: 100,
    
    _total: 100,
    
    _process: 0,
    
    _processobject: null,
    
    _processleftcolor: "blue",
    
    _processrightcolor: "silver",
    
    _flushProcessBar: function()
    {
      if (this._processobject == null)
      {
        throw new Exception("Process bar not binded!");
      }
      var strhtml = "";
      if (this._process_bar_status == cn.aprilsoft.jsapp.ui.ProgressBar.PROCESS_BAR_PERCENT)
      {
        var ctrlwidth = this._processobject.clientWidth;
        var widthleft = parseInt((this._process / this._total) * ctrlwidth);
        var widthright = ctrlwidth - widthleft;
        strhtml += "<table style='width:100%;height:100%;border:0px;' border='0' cellspacing='0' cellpadding='0'>"
                +  "<tr>"
                +  "<td style='background:" + this._processleftcolor + ";' border='0' width='" + widthleft + "px'>"
                +  ""
                +  "</td>"
                +  "<td style='background:" + this._processrightcolor + ";' border='0' width='" + widthright + "px'>"
                +  ""
                +  "</td>"
                +  "</tr>"
                +  "</table>";
      }
      else
      {
        var widthleft = (this._process / this._total) * this._width;
        var widthright = this._width - widthleft;
        strhtml += "<table style='width:" + this._width + "px;height:100%;border:0px;' border='0' cellspacing='0' cellpadding='0'>"
                +  "<tr>"
                +  "<td style='background:" + this._processleftcolor + ";' border='0' width='" + widthleft + "px'>"
                +  ""
                +  "</td>"
                +  "<td style='background:" + this._processrightcolor + ";' border='0' width='" + widthright + "px'>"
                +  ""
                +  "</td>"
                +  "</tr>"
                +  "</table>";
      }
      this._processobject.innerHTML = strhtml;
    },
    
    hide: function()
    {
      this._processobject.innerHTML = "&nbsp;";
    },
    
    Constructor: function(processobj)
    {
      this._process_bar_status = cn.aprilsoft.jsapp.ui.ProgressBar.PROCESS_BAR_PERCENT;
      if (processobj != null)
      {
        this.bind(processobj);
        this._flushProcessBar();
      }
    },
    
    bind: function(processobj)
    {
      if (System.isString(processobj))
      {
        this._processobject = System.getElement(processobj);
      }
      else
      {
        this._processobject = processobj;
      }
    },
    
    setProcessBarStatus: function(flgstatus)
    {
      this._process_bar_status = flgstatus;
    },
    
    setWidth: function(width)
    {
      this._width = width;
    },
    
    setTotal: function(total)
    {
      this._total = total;
    },
    
    setPostion: function(process)
    {
      if (process > this._total)
      {
        process = this._total;
      }
      if (this._process != process)
      {
        this._process = process;
        this._flushProcessBar();
      }
    }
  });
})();

