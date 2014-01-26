/*
 * cn.aprilsoft.jsapp.ui.SelectFontColor.js
 * jsapp, ui select file functions
 * 
 * Copyright(C) Hatter Jiang
 */

/* copy the below codes to your html, cDialog is your dialog name:******
<OBJECT ID="cFontColor" WIDTH="0px" HEIGHT="0px"
    CLASSID="clsid:3050f819-98b5-11cf-bb82-00aa00bdce0b">
</OBJECT>
***********************************************************************/

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.ui.SelectFontColor", Extend(), Implement(),
  {
    _fontcharset:
      [
        ["ANSI_CHARSET",	0],
        ["DEFAULT_CHARSET",	1],
        ["SYMBOL_CHARSET",	2],
        ["SHIFTJIS_CHARSET",	128],
        ["HANGEUL_CHARSET",	129],
        ["HANGUL_CHARSET",	129],
        ["GB2312_CHARSET",	134],
        ["CHINESEBIG5_CHARSET",	136],
        ["OEM_CHARSET",	255],
        ["MAC_CHARSET",	77],
        ["JOHAB_CHARSET",	130],
        ["GREEK_CHARSET",	161],
        ["TURKISH_CHARSET",	162],
        ["VIETNAMESE_CHARSET",	163],
        ["HEBREW_CHARSET",	177],
        ["ARABIC_CHARSET",	178],
        ["BALTIC_CHARSET",	186],
        ["RUSSIAN_CHARSET",	204],
        ["THAI_CHARSET",	222],
        ["EASTEUROPE_CHARSET",	238]
      ],
    
    _selectFontColorObj: null,
    
    Constructor: function(strSelectFontColorObjName)
    {
      System.checkArguments();
      
      this._selectFontColorObj = System.getElement(strSelectFontColorObjName);
      if (this._selectFontColorObj == null)
      {
        alert("Please firtly add select font&color object to current html:\n\
  <OBJECT ID=\"" + strSelectFontColorObjName + "\" WIDTH=\"0px\" HEIGHT=\"0px\"\n\
      CLASSID=\"clsid:3050f819-98b5-11cf-bb82-00aa00bdce0b\">\n\
  </OBJECT>\"");
        throw new Exception("Select font&color object not added to current html.");
      }
    },
    
    createCtrl: Static(function(strSelectFontColorObjName)
    {
        document.write("<div style='display:none;visibility:hidden;'>\
  <OBJECT ID=\"" + strSelectFontColorObjName + "\" WIDTH=\"0px\" HEIGHT=\"0px\"\n\
      CLASSID=\"clsid:3050f819-98b5-11cf-bb82-00aa00bdce0b\">\n\
  </OBJECT>\
  </div>");
    }),
    
    getSelectFontColor: function()
    {
      return this._selectFontColorObj;
    },
    
    getSystemFonts: function()
    {
      var lstFonts = [];
      for (var i = 1; i < this._selectFontColorObj.fonts.count; i++)
      {
        lstFonts.push(this._selectFontColorObj.fonts(i));
      }
      return lstFonts;
    },
    
    getFontCharacterset: function(strFontname)
    {
      var charsetType = this._selectFontColorObj.getCharset(strFontname);
      var charsetTypeName = null;
      System.walkArray(this._fontcharset, function(intIndex, objArray)
      {
        if (charsetType == objArray[1])
        {
          charsetTypeName = objArray[0];
          return;
        }
      });
      return charsetTypeName;
    },
    
    chooseColor: function(sInitColor)
    {
      if (sInitColor == null)
      {
      	var sColor = this._selectFontColorObj.ChooseColorDlg();
      }
      else
      {
      	var sColor = this._selectFontColorObj.ChooseColorDlg(sInitColor);
      }
      sColor = sColor.toString(16);
      if (sColor.length < 6)
      {
        	var sTempString = "000000".substring(0, 6 - sColor.length);
        	sColor = sTempString.concat(sColor);
      }
      return sColor;
    }
  });
})()

