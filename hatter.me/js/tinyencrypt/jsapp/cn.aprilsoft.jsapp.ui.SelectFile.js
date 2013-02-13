/*
 * cn.aprilsoft.jsapp.ui.SelectFile.js
 * jsapp, ui select file functions
 * 
 * Copyright(C) Hatter Jiang
 */

/* copy the below codes to your html, cDialog is your dialog name:******
<OBJECT ID="cDialog" WIDTH="0px" HEIGHT="0px"
    CLASSID="CLSID:F9043C85-F6F2-101A-A3C9-08002B2F49FB"
    CODEBASE="http://activex.microsoft.com/controls/vb5/comdlg32.cab">
</OBJECT>
***********************************************************************/

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.ui.SelectFile", Extend(), Implement(),
  {
    _selectFileObj: null,
    
    Constructor: function(strSelectFileObjName)
    {
      System.checkArguments();
      
      this._selectFileObj = System.getElement(strSelectFileObjName);
      if (this._selectFileObj == null)
      {
        alert("Please firtly add select file object to current html:\n\
  <OBJECT ID=\"" + strSelectFileObjName + "\" WIDTH=\"0px\" HEIGHT=\"0px\"\n\
  CLASSID=\"CLSID:F9043C85-F6F2-101A-A3C9-08002B2F49FB\"\n\
  CODEBASE=\"http://activex.microsoft.com/controls/vb5/comdlg32.cab\">\n\
  </OBJECT>\"");
        throw new Exception("Select file object not added to current html.");
      }
    },
    
    createCtrl: Static(function(strSelectFileObjName)
    {
        document.write("<div style='display:none;visibility:hidden;'>\
        <OBJECT ID=\"" + strSelectFileObjName + "\" WIDTH=\"0px\" HEIGHT=\"0px\"\n\
  CLASSID=\"CLSID:F9043C85-F6F2-101A-A3C9-08002B2F49FB\"\n\
  CODEBASE=\"http://activex.microsoft.com/controls/vb5/comdlg32.cab\">\n\
  </OBJECT>\"\
  </div>");
    }),
    
    getSelectFile: function()
    {
      return this._selectFileObj;
    },
    
    selectOpenFile: function(strFilter)
    {
      if (strFilter == null)
      {
        strFilter = "All files (*.*)|*.*";
      }
      this._selectFileObj.Filter = strFilter;
      this._selectFileObj.FileName = "";
      this._selectFileObj.ShowOpen();
      if (this._selectFileObj.FileName == "")
      {
        return null;
      }
      else
      {
        return this._selectFileObj.FileName;
      }
    },
    
    selectSaveFile: function(strFilter)
    {
      if (strFilter == null)
      {
        strFilter = "All files (*.*)|*.*";
      }
      this._selectFileObj.Filter = strFilter;
      this._selectFileObj.ShowSave();
      return this._selectFileObj.FileName;
    }
  });
})();

