/*
 * cn.aprilsoft.jsapp.xml.XMLObject.js
 * jsapp, xml object functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.xml
  Package("cn.aprilsoft.jsapp.xml");

  Class("cn.aprilsoft.jsapp.xml.XMLObject", Extend(), Implement(),
  {
    _xmlstring: null,
    
    _objObject: null,
    
    _parsed_object_list: [],
    
    Constructor: function(objObj)
    {
      this._parsed_object_list = [];
      
      var caller = arguments.callee.caller;
      if ((caller == null)
        || (caller._funcName == null)
        || (
              (!(/.*parseObject.*/.test(caller._funcName)))
           && (!(/.*parseXml.*/.test(caller._funcName))))
           )
      {
        throw new Exception("Cannot create instance directory!");
      }
    },
    
    parseObject: Static(function(objObj)
    {
      var XMLObject = cn.aprilsoft.jsapp.xml.XMLObject;
      var xmlObject = new XMLObject();
      xmlObject._xmlstring = "<xmlobject version=\"1.0\">"
                           + xmlObject._convertObjectToXml(objObj)
                           + "</xmlobject>";
      return xmlObject;
    }),
    
    getXml: Static(function()
    {
    }),
    
    parseXml: Static(function()
    {
    }),
    
    getObject: Static(function()
    {
    }),
    
    _getTranslatedString: function(strStr)
    {
      if (strStr == null)
      {
        return strStr;
      }
      strStr = strStr.replace(/&/g, "&amp;");
      strStr = strStr.replace(/</g, "&lt;");
      strStr = strStr.replace(/>/g, "&gt;");
      return strStr;
    },
    
    _convertObjectToXml: function(objObj)
    {
      var type = typeof objObj;
      if (objObj === null)
      {
        return "<object type=\"null\"/>";
      }
      else if (type == "undefined")
      {
        return "<undefined/>";
      }
      else if (type == "boolean")
      {
        if (objObj)
        {
          return "<boolean>true</boolean>";
        }
        else
        {
          return "<boolean>false</boolean>";
        }
      }
      else if (type == "number")
      {
        return "<number>" + objObj.toString() + "</number>";
      }
      else if (type == "string")
      {
        return "<string>" + this._getTranslatedString(objObj) + "</string>";
      }
      else if (type == "function")
      {
        return "<function>" + this._getTranslatedString(objObj.toString()) + "</function>";
      }
      else if (type == "object")
      {
        return this._convertTypeObjectToXml(objObj);
      }
      return "<unknown/>";
    },
    
    _convertTypeObjectToXml: function(objObj)
    {
      if (objObj instanceof Boolean)
      {
        if (objObj)
        {
          return "<object type=\"boolean\">true</object>";
        }
        else
        {
          return "<object type=\"boolean\">false</object>";
        }
      }
      else if (objObj instanceof Number)
      {
        return "<object type=\"number\">" + objObj.toString() + "</object>";
      }
      else if (objObj instanceof String)
      {
        return "<object type=\"string\">" + this._getTranslatedString(objObj.toString()) + "</object>";
      }
      else if (objObj instanceof Function)
      {
        return "<object type=\"function\">" + this._getTranslatedString(objObj.toString()) + "</object>";
      }
      else if (objObj instanceof Array)
      {
        // is a array
        var lstXmlArray = [];
        lstXmlArray.push("<object type=\"array\">");
        for (var i = 0; i < objObj.length; i++)
        {
          lstXmlArray.push(this._convertObjectToXml(objObj[i]));
        }
        lstXmlArray.push("</object>");
        return lstXmlArray.join("");
      }
      else if ((window.ActiveXObject) && (objObj instanceof ActiveXObject))
      {
        throw new Exception("ActiveXObject cannot parseObject!");
      }
      // TODO check if is DOM object then trow new Exception()
      else
      {
        var lstXmlArray = [];
        lstXmlArray.push("<object type=\"object\">");
        try
        {
          for (var k in objObj)
          {
            lstXmlArray.push("<element>");
            lstXmlArray.push("<key>");
            lstXmlArray.push(k);
            lstXmlArray.push("</key>");
            lstXmlArray.push("<value>");
            lstXmlArray.push(this._convertObjectToXml(objObj[k]));
            lstXmlArray.push("</value>");
            lstXmlArray.push("</element>")
          }
        }
        catch(e)
        {
          throw new Exception(e, "For in object error!");
        }
        lstXmlArray.push("</object>");
        return lstXmlArray.join("");
      }
    },
    
    _convertXmlToObject: function()
    {
      // TODO
    }
  });
})();

