/*
 * cn.aprilsoft.jsapp.config.ConfigManager.js
 * jsapp, config manager functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.config
  Package("cn.aprilsoft.jsapp.config");

  var System = Using("cn.aprilsoft.jsapp.system.System");
  var FileUtil = Using("cn.aprilsoft.jsapp.common.FileUtil");
  var XMLObject = Using("cn.aprilsoft.jsapp.xml.XMLObject");

  Class("cn.aprilsoft.jsapp.config.ConfigManager", Extend(), Implement(),
  {
    _fileName: null,
    
    _objObj: {},
    
    Constructor: function(strFileName)
    {
      System.notIe(function()
      {
        throw new Exception("This calss is for IE only!");
      });
      
      this._objObj = {};
      
      this._fileName = strFileName;
      
      var configXml = FileUtil.readTxtFile(this._fileName);
      
      var xmlDom = new XMLDOM();
      xmlDom.setXML(configXml);
      
      var xmlObj = XMLObject.parseXml(xmlDom);
      
      this._objObj = xmlObj.getObject();
    },
    
    saveToFile(strFileName)
    {
      if (arguments.length != 0)
      {
        this._fileName = strFileName;
      }
      
      var xmlObj = XMLObject.parseXml(this._objObj);
      
      FileUtil.writeTxtFile(this._fileName, xmlObj.getXml());
    },
    
    get: function(strKey)
    {
      return this._objObj[strKey];
    },
    
    set: function(value)
    {
      return this._objObj[strKey] = value;
    },
    
    getObject: function()
    {
      return this._objObj;
    },
    
    setObject: function(value)
    {
      this._objObj = value;
    }
  });
})();

