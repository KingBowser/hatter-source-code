/*
 * cn.aprilsoft.jsapp.io.FileFilter.js
 * jsapp, io file filter functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.io
  Package("cn.aprilsoft.jsapp.io");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.io.FileFilter", Extend(), Implement(),
  {
    _pattern: null,
    _regexPattern: null,
    
    Constructor: function()
    {
      var isInvokeCalled = false;
      System.invokeOn("str", function(pattern)
      {
        isInvokeCalled = true;
        this._pattern = pattern;
        pattern = pattern.replace(/\\/g, "\\\\");
        pattern = pattern.replace(/\./g, "\\.");
        pattern = pattern.replace(/\,/g, "\\,");
        pattern = pattern.replace(/\-/g, "\\-");
        pattern = pattern.replace(/\+/g, "\\+");
        pattern = pattern.replace(/\=/g, "\\=");
        pattern = pattern.replace(/\!/g, "\\!");
        pattern = pattern.replace(/\^/g, "\\^");
        pattern = pattern.replace(/\$/g, "\\$");
        pattern = pattern.replace(/\(/g, "\\(");
        pattern = pattern.replace(/\)/g, "\\)");
        pattern = pattern.replace(/\[/g, "\\[");
        pattern = pattern.replace(/\]/g, "\\]");
        pattern = pattern.replace(/\{/g, "\\{");
        pattern = pattern.replace(/\}/g, "\\}");
        pattern = pattern.replace(/\./g, "\\.");
        pattern = pattern.replace(/\*/g, ".*");
        pattern = pattern.replace(/\?/g, ".?");
        this._regexPattern = new RegExp(regex + "$", "i");
      }, this);
      System.invokeOn("regex", function(regex)
      {
        isInvokeCalled = true;
        this._regexPattern = regex;
      }, this);
      
      if (!isInvokeCalled)
      {
        throw new Exception("Call constructor in a wrong way.");
      }
    },
    
    filter: function(fileName)
    {
      if (fileName == null)
      {
        throw new Exception("File name cannot be null.");
      }
      return (fileName.match(this.regexPattern) != null);
    }
  });
})();
