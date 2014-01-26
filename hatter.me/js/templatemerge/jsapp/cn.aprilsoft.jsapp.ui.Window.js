/*
 * cn.aprilsoft.jsapp.ui.SelectFile.js
 * jsapp, ui window functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.ui.Window", Extend(), Implement(),
  {
    _minWindowObj: null,
    
    _maxWindowObj: null,
    
    _closeWindowObj: null,
    
    Constructor: function()
    {
      var caller = arguments.callee.caller;
      if ((caller == null)
        || (caller._funcName == null)
        || (!(/.*cn\.aprilsoft\.jsapp\.ui\.Window*/.test(caller._funcName))))
      {
        throw new Exception("Cannot create instance directory!");
      }
    },
    
    /* copy the below codes to your html, cMinWindow is your ctrl name:*****
    <object id="cMinWindow" classid="clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11">
      <param name="Command" value="Minimize">
    </object>
    ***********************************************************************/
    newMinWindow: Static(function(strMinWindowObjName)
    {
      System.checkArguments();
      var obj = new Window();
      obj._minWindowObj = System.getElement(strMinWindowObjName);
      if (obj._minWindowObj == null)
      {
        alert("Please firtly add min window object to current html:\n\
  <object id=\"" + strMinWindowObjName + "\" classid=\"clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11\">\n\
    <param name=\"Command\" value=\"Minimize\">\n\
  </OBJECT>\"");
        throw new Exception("Min window object not added to current html.");
      }
      return obj;
    }),
    
    /* copy the below codes to your html, cMaxWindow is your ctrl name:*****
    <object id="cMaxWindow" classid="clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11">
      <param name="Command" value="Maximize">
    </object>
    ***********************************************************************/
    newMaxWindow: Static(function(strMaxWindowObjName)
    {
      System.checkArguments();
      var obj = new Window();
      obj._maxWindowObj = System.getElement(strMaxWindowObjName);
      if (obj._maxWindowObj == null)
      {
        alert("Please firtly add min window object to current html:\n\
  <object id=\"" + strMaxWindowObjName + "\" classid=\"clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11\">\n\
    <param name=\"Command\" value=\"Maximize\">\n\
  </OBJECT>\"");
        throw new Exception("Max window object not added to current html.");
      }
      return obj;
    }),
    
    /* copy the below codes to your html, cCloseWindow is your ctrl name:***
    <object id="cCloseWindow" classid="clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11">
      <param name="Command" value="Close">
    </object>
    ***********************************************************************/
    newCloseWindow: Static(function(strCloseWindowObjName)
    {
      System.checkArguments();
      var obj = new Window();
      obj._closeWindowObj = System.getElement(strCloseWindowObjName);
      if (obj._closeWindowObj == null)
      {
        alert("Please firtly add min window object to current html:\n\
  <object id=\"" + strCloseWindowObjName + "\" classid=\"clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11\">\n\
    <param name=\"Command\" value=\"Close\">\n\
  </OBJECT>\"");
        throw new Exception("Max window object not added to current html.");
      }
      return obj;
    }),
    
    doMinWindow: function()
    {
      if (this._minWindowObj == null)
      {
        throw new Exception("newMinWindow() should be called.");
      }
      this._minWindowObj.Click();
    },
    
    doMaxWindow: function()
    {
      if (this._maxWindowObj == null)
      {
        throw new Exception("newMaxWindow() should be called.");
      }
      this._maxWindowObj.Click();
    },
    
    doCloseWindow: function()
    {
      if (this._closeWindowObj == null)
      {
        throw new Exception("newCloseWindow() should be called.");
      }
      this._closeWindowObj.Click();
    }
  });
})();

