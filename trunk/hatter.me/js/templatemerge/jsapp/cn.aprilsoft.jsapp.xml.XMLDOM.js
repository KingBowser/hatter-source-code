/*
 * cn.aprilsoft.jsapp.xml.XMLDOM.js
 * jsapp, xml dom functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.xml
  Package("cn.aprilsoft.jsapp.xml");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.xml.XMLDOM", Extend(), Implement(),
  {
    _xmlDomObject: null,
    
    Constructor: function(strXml)
    {
      this.setXMLDOM(strXml);
    },
    
    setXMLDOM: function(strXml)
    {
      var This = this;
      var processedFlag = false;
      System.ff(function()
      {
        processedFlag = true;
        
        // define selectSingleNode
        XMLDocument.prototype.selectSingleNode
        =
        Element.prototype.selectSingleNode = function(xpath)
        {
          var x = This.selectNodes(xpath)
          if ( !x || x.length < 1)
          {
            return null;
          }
          return x[0];
        };
        
        // define selectNodes
        XMLDocument.prototype.selectNodes
        =
        Element.prototype.selectNodes = function(xpath)
        {
          var xpe = new XPathEvaluator();
          var nsResolver = xpe.createNSResolver(
            This.ownerDocument ==  null? This .documentElement: This.ownerDocument.documentElement);
          var result = xpe.evaluate(xpath, This, nsResolver, 0, null);
          var found = [];
          var res;
          while (res = result.iterateNext())
          {
            found.push(res);
          }
          return found;
        };
        
        var parser = new DOMParser();
        This._xmlDomObject = parser.parseFromString(strXml, "text/xml");
      });
      
      System.ie(function()
      {
        processedFlag = true;
        This._xmlDomObject = new ActiveXObject("Microsoft.XMLDOM");
        This._xmlDomObject.loadXML(strXml);
      });
      
      if (!(processedFlag))
      {
        throw new Exception("This class only support Firefox and IE!");
      }
    },
    
    getXMLDOM: function()
    {
      return this._xmlDomObject;
    }
  });
})();

