/*
 * cn.aprilsoft.jsapp.config.INIFile.js
 * jsapp, ini file functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.config
  Package("cn.aprilsoft.jsapp.config");

  var FileUtil = Using("cn.aprilsoft.jsapp.common.FileUtil");
  var URIEncoder = Using("cn.aprilsoft.jsapp.encode.URIEncoder");

  Class("cn.aprilsoft.jsapp.config.INIFile", Extend(), Implement(),
  {
    _FileLines: new Array(),
    
    _iniFileName: null,
    
    _defaultSection: null,
    
    Constructor: function(filename, section)
    {
      this._FileLines = new Array();
      
      if (filename != null)
      {
        this._iniFileName = filename;
      }
      
      if (section != null)
      {
        this._defaultSection = section;
      }
    },
    
    readFromFile: function(filename)
    {
      if (filename == null)
      {
        filename = this._iniFileName;
      }
      else
      {
        this._iniFileName = filename;
      }
      
      try
      {
        var filecontent = FileUtil.readTxtFile(filename);
      }
      catch(e)
      {
        // no such file
        return;
      }
      this._FileLines = filecontent.split(/(\r\n)|(\r)|(\n)/);
    },
    
    saveToFile: function(filename)
    {
      if (filename == null)
      {
        filename = this._iniFileName;
      }
      else
      {
        this._iniFileName = filename;
      }
      
      FileUtil.writeTxtFile(filename, this._FileLines.join("\r\n"));
    },
    
    setValue: function(section, key, value)
    {
      if (section == null)
      {
        section = "";
      }
      
      value = URIEncoder.getEncodeAll(value);
      
      var lenOfKey = key.length;
      
      if (section == "")
      {
        for (var i = 0; i < this._FileLines.length; i++)
        {
          if (!(/\[.*\]/.test(this._FileLines[i])))
          {
            if (this._FileLines[i].substring(0, (lenOfKey + 1)) == (key + "="))
            {
              this._FileLines[i] = key + "=" + value;
              return;
            }
          }
          else 
          {
            this._FileLines.splice(i, 0, key + "=" + value);
            return;
          }
        }
        this._FileLines.push(key + "=" + value);
      }
      else
      {
        for (var i = 0; i < this._FileLines.length; i++)
        {
          if (this._FileLines[i] == "[" + section + "]")
          {
            for (i++ ; i < this._FileLines.length; i++)
            {
              if (!(/\[[^=*\]]/.test( this._FileLines[i])))
              {
                if (this._FileLines[i].substring(0, (lenOfKey + 1)) == (key + "="))
                {
                  this._FileLines[i] = key + "=" + value;
                  return;
                }
              }
              else
              {
                this._FileLines.splice((i + 0), 0, key + "=" + value);
                return;
              }
            } 
            this._FileLines.push(key + "=" + value);
            return;
          }
        }
        this._FileLines.push("[" + section + "]");
        this._FileLines.push(key + "=" + value); 
      }
    },
    
    getValue: function(section, key)
    {
      if (section == null)
      {
        section = "";
      }
      
      if (this._FileLines.length < 1) 
      {
        return null;
      }
      
      var lenOfKey = key.length;
      
      if (section == "")
      {
        for (var i = 0; i < this._FileLines.length; i++)
        {
          if (!(/\[[^=*]\]/.test(this._FileLines [i])))
          {
            if (this._FileLines[i].substring(0, (lenOfKey + 1)) == key + "=")
            {
              return URIEncoder.getDecodeAll(this._FileLines[i].substring(lenOfKey + 1));
            }
          }
        }
      }
      else
      {
        for (var i = 0; i < this._FileLines.length; i++)
        {
          if (this._FileLines[i] == "[" + section + "]")
          {
            for ( ; i < this._FileLines.length ; i++)
            {
              if (!(/\[[^=*]\]/.test(this._FileLines[i])))
              {
                if (this._FileLines[i].substring(0, (lenOfKey + 1)) == key + "=")
                {
                  return URIEncoder.getDecodeAll(this._FileLines[i].substring(lenOfKey + 1));
                }
              }
            }
            return null;
          }
        }
      }
      return null;
    },
    
    setDefaultSection: function(section)
    {
      this._defaultSection = section;
    },
    
    setDefaultValue: function(key, value)
    {
      this.setValue(this._defaultSection, key, value);
    },
    
    getDefaultValue: function(key)
    {
      return this.getValue(this._defaultSection, key);
    },
    
    setDefault: function(key, value)
    {
      this.setDefaultValue(key, value);
    },
    
    getDefault: function(key)
    {
      return this.getDefaultValue(key);
    }
  });
})();
