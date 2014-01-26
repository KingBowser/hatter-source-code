/*
 * cn.aprilsoft.jsapp.ui.FlatButton.js
 * jsapp, ui flat button functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.ui.FlatButton", Extend(), Implement(),
  {
    BTN_STYLE_NORMAL: Static("background: #ece9d8; "
                           + "border: 1px solid #ece9d8;"
  			                   + "margin: 1; "
                           + "cursor:normal;"),
    
    BTN_STYLE_RAISE: Static("border-top: 1px solid buttonhighlight;"
  			                  + "border-left: 1px solid buttonhighlight;"
  			                  + "border-bottom: 1px solid buttonshadow;"
  			                  + "border-right: 1px solid buttonshadow;"
  			                  + "background: #ece9d8;"
  			                  + "margin: 1;"
                          + "cursor:normal;"),
    
    
    BTN_STYLE_PRESS: Static("border-top: 1px solid buttonshadow;"
  			                  + "border-left: 1px solid buttonshadow;"
  			                  + "border-bottom: 1px solid buttonhighlight;"
  			                  + "border-right: 1px solid buttonhighlight;"
  			                  + "background: #ece9d8;"
  			                  + "margin: 1;"
                          + "cursor:normal;"),
    
    _tableUid: "",
    
    _buttonTitle: "",
    
    Constructor: function(title)
    {
      this._tableUid = "_flat_btn_" + (new Date()).getTime().toString();
      
      this._buttonTitle = title;
    },
    
    bind: function(ctrl)
    {
      try
      {
        System.getElement(ctrl).innerHTML = this._getFlatButtonHTML();
        this._initFlatButtonEvents();
      }
      catch(e)
      {
        throw NewException(e);
      }
    },
    
    setTitle: function(title)
    {
      var flatButtonCtrl = System.getElement(this._tableUid + "_title");
      flatButtonCtrl.innerHTML = title;
    },
    
    getTitle: function()
    {
      var flatButtonCtrl = System.getElement(this._tableUid + "_title");
      return flatButtonCtrl.innerHTML;
    },
    
    setWidth: function(width)
    {
      var flatButtonCtrl = System.getElement(this._tableUid);
      flatButtonCtrl.style.width = width;
    },
    
    getWidth: function()
    {
      var flatButtonCtrl = System.getElement(this._tableUid);
      return flatButtonCtrl.style.width;
    },
    
    setHeight: function(height)
    {
      var flatButtonCtrl = System.getElement(this._tableUid);
      flatButtonCtrl.style.height = height;
    },
    
    getHeight: function()
    {
      var flatButtonCtrl = System.getElement(this._tableUid);
      return flatButtonCtrl.style.height;
    },
    
    _getFlatButtonHTML: function()
    {
      var html = "<table "
               + "id=" + "\"" + this._tableUid + "\""
               + ">"
               + "<tr>"
               + "<td "
               + "id=" + "\"" + this._tableUid + "_title" + "\""
               + "align=\"center\""
               + ">"
               + this._buttonTitle
               + "</td>"
               + "</tr>"
               + "</table>";
      
      return html;
    },
    
    _initFlatButtonEvents: function()
    {
      var flatButtonCtrl = System.getElement(this._tableUid);
      
      CurrClass = ThisClass();
      System.setStyle(flatButtonCtrl, CurrClass.BTN_STYLE_NORMAL);
      
      flatButtonCtrl.onmouseover = function()
      {
        System.setStyle(flatButtonCtrl, CurrClass.BTN_STYLE_RAISE);
      };
      
      flatButtonCtrl.onmousedown = function()
      {
        System.setStyle(flatButtonCtrl, CurrClass.BTN_STYLE_PRESS);
      };
      
      flatButtonCtrl.onmouseup = function()
      {
        System.setStyle(flatButtonCtrl, CurrClass.BTN_STYLE_RAISE);
      };
      
      flatButtonCtrl.onmouseout = function()
      {
        System.setStyle(flatButtonCtrl, CurrClass.BTN_STYLE_NORMAL);
      };
      
      var This = this;
      flatButtonCtrl.onclick = function()
      {
        if (This["onclick"] != null)
        {
          try
          {
            This["onclick"]();
          }
          catch(e)
          {
            throw NewException(e);
          }
        }
      };
    }
  });
})();

