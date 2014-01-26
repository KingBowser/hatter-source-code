/*
 * cn.aprilsoft.jsapp.ui.TextPopup.js
 * jsapp, ui show text popup functions
 * 
 * Copyright(C) Hatter Jiang
 */

/* copy the below codes to your html, cPopCtrl is your dialog name:*****
<OBJECT ID="cPopCtrl" TYPE="application/x-oleobject"
    CLASSID="clsid:adb880a6-d8ff-11cf-9377-00aa003b7a11"
    CODEBASE="HHCtrl.ocx#Version=4,73,8259,0">
</OBJECT>
***********************************************************************/

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.ui.TextPopup", Extend(), Implement(),
  {
    _popupObject: null,
    
    Constructor: function(strPopupObjName)
    {
      System.checkArguments();
      
      this._popupObject = System.getElement(strPopupObjName);
      if (this._popupObject == null)
      {
        alert("Please firtly add text popup object to current html:\n\
  <OBJECT ID=\"" + strPopupObjName + "\" TYPE=\"application/x-oleobject\"\n\
      CLASSID=\"clsid:adb880a6-d8ff-11cf-9377-00aa003b7a11\"\n\
      CODEBASE=\"HHCtrl.ocx#Version=4,73,8259,0\">\n\
  </OBJECT>\"");
        throw new Exception("Text popup object not added to current html.");
      }
    },
    
    createCtrl: Static(function(strPopupObjName)
    {
        document.write("<div style='display:none;visibility:hidden;'>\
  <OBJECT ID=\"" + strPopupObjName + "\" TYPE=\"application/x-oleobject\"\n\
      CLASSID=\"clsid:adb880a6-d8ff-11cf-9377-00aa003b7a11\"\n\
      CODEBASE=\"HHCtrl.ocx#Version=4,73,8259,0\">\n\
  </OBJECT>\
  </div>");
    }),
    
    getTextPopup: function()
    {
      return this._popupObject;
    },
    
    showTextPopup: function(text, font, arg0, arg1, arg2, arg3)
    {
      // i.e. MyText,"Verdana,10",9,9,-1,-1
      return this._popupObject.TextPopup(text, font, arg0, arg1, arg2, arg3);
    }
  });
})();

