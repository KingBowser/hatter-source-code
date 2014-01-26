/*
 * cn.aprilsoft.jsapp.ui.HintInput.js
 * jsapp, hint input functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var HtmlDom = Using("cn.aprilsoft.jsapp.html.dom.HtmlDom");

  Class("cn.aprilsoft.jsapp.ui.HintInput", Extend(), Implement(),
  {
    BLUR: Static("BLUR"),
    FOCUS: Static("FOCUS"),
    NORMAL: Static("NORMAL"),
    
    _hightLightBackgroundColor: "#FFFF99",
    _blurBackgroundColor: "#ffffff",
    _hightLightFontColor: "#000000",
    _blurFontColor: "#c0c0c0",
    _normalBackgroundColor: "#ffffff",
    _normalFontColor: "#000000",
    
    _hintMessage: "Default message ...",
    _bindControl: null,
    
    _instanceEventOnFocus: null,
    _instanceEventOnBlur: null,
    
    Constructor: function(/* message, control */ )
    {
      this._instanceEventOnFocus = this._eventOnFocus.bind(this);
      this._instanceEventOnBlur = this._eventOnBlur.bind(this);
      
      System.invokeOn("str", function(message)
      {
        this._hintMessage = message;
      }, this);
      System.invokeOn("str, obj", function(message, control)
      {
        this._hintMessage = message;
        this._bindControl = control;
      }, this);
      this._bindToCtrl();
    },
    
    setMessage: function(message)
    {
      this._hintMessage = message;
      this._bindToCtrl;
    },
    
    bind: function(control)
    {
      this._bindControl = control;
      this._bindToCtrl();
    },
    
    unbind: function()
    {
      var ctrl = System.getElement(this._bindControl);
      if (ctrl != null)
      {
        HtmlDom.removeFunction(ctrl, "focus", this._instanceEventOnFocus);
        HtmlDom.removeFunction(ctrl, "blur", this._instanceEventOnBlur);
        
        ctrl._type = ThisClass().NORMAL;
        this._refreshInput();
        ctrl._type = System.UNDEFINED;
      }
    },
    
    _bindToCtrl: function()
    {
      if (this._bindControl != null)
      {
        var ctrl = System.getElement(this._bindControl);
        if (ctrl == null)
        {
          throw new Exception("Cannot find contrl: " + this._bindControl);
        }
        HtmlDom.attachFunction(ctrl, "focus", this._instanceEventOnFocus);
        HtmlDom.attachFunction(ctrl, "blur", this._instanceEventOnBlur);
        
        this._eventOnBlur();
      }
    },
    
    _eventOnFocus: function()
    {
      var ctrl = System.getElement(this._bindControl);
      if (ctrl._type == ThisClass().BLUR)
      {
        ctrl.value = "";
      }
      ctrl._type = ThisClass().FOCUS;
      this._refreshInput();
    },
    
    _eventOnBlur: function()
    {
      var ctrl = System.getElement(this._bindControl);
      if (ctrl.value.replace(/(^\s*)|(\s*$)/, "") == "")
      {
        ctrl._type = ThisClass().BLUR;
      }
      else
      {
        ctrl._type = ThisClass().NORMAL;
      }
      this._refreshInput();
    },
    
    _refreshInput: function()
    {
      var ctrl = System.getElement(this._bindControl);
      if (ctrl._type == ThisClass().BLUR)
      {
        ctrl.value = this._hintMessage;
        System.setStyle(ctrl, {"background": this._blurBackgroundColor, "color": this._blurFontColor});
      }
      else if (ctrl._type == ThisClass().FOCUS)
      {
        System.setStyle(ctrl, {"background": this._hightLightBackgroundColor, "color": this._hightLightFontColor});
      }
      else if (ctrl._type == ThisClass().NORMAL)
      {
        System.setStyle(ctrl, {"background": this._normalBackgroundColor, "color": this._normalFontColor});
      }
      else
      {
        throw new Exception("Control type error: " + ctrl._type);
      }
    }
  });
})();

