/*
 * cn.aprilsoft.jsapp.text.MessageFormat.js
 * jsapp, message format functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.text
  Package("cn.aprilsoft.jsapp.text");

  Class("cn.aprilsoft.jsapp.text.MessageFormat", Extend(), Implement(),
  {
    _message: null,
    
    _translateMap:  {
                      "d": "_translate_d"
                    },
    
    Constructor: function(message)
    {
      this._message = message;
    },
    
    format: function(/* ... */)
    {
      if (this._message == null)
      {
        return null;
      }
      else
      {
        var args = Array.prototype.slice.call(arguments);
        return this._format(this._message, args);
      }
    },
    
    _format: function(message, args)
    {
      var result = [];
      
      var bracketChars = "";
      var inBrackets = false;
      for (var i = 0; i < message.length; i++)
      {
        var c = message.charAt(i);
        var nextChar = (i < (message.length - 1))? message.charAt(i + 1): null;
        
        if (c == "\\")
        {
          if (nextChar != null)
          {
            result.push(nextChar);
            i += 1;
          }
          else
          {
            result.push(c);
          }
        }
        else if (inBrackets)
        {
          if (c != "}")
          {
            bracketChars += c;
          }
          else
          {
            result.push(this._translate(bracketChars, args));
            bracketChars = "";
            inBrackets = false;
          }
        }
        else if (c == "{")
        {
          inBrackets = true;
        }
        else
        {
          result.push(c);
        }
      }
      
      if (bracketChars != "")
      {
        result.push("{" + bracketChars);
      }
      
      return result.join("");
    },
    
    _translate: function(inString, args)
    {
      var commonIndex = inString.indexOf(":");
      var idx = 0;
      var param = "";
      if (commonIndex < 0)
      {
        idx = parseInt(inString, 10);
      }
      else
      {
        idx = parseInt(inString.substring(0, idx), 10);
        param = inString.substring(idx + 1);
      }
      if (commonIndex < 0)
      {
        return this._get(inString, idx, param, args);
      }
      else
      {
        var translateFunc = this._translateMap[param];
        try
        {
          if (translateFunc != null)
          {
            if (this._has(args, idx))
            {
              return translateFunc(param, args[idx]);
            }
            else
            {
              return this._orient(inString);
            }
          }
        }
        catch(e)
        {
          return this._orient(inString);
        }
        return this._get(inString, idx, param, args);
      }
    },
    
    _has: function(args, idx)
    {
      return ((!isNaN(idx)) && (idx >= 0) && (idx < args.length));
    },
    
    _get: function(inString, idx, param, args)
    {
      if (!(this._has(args, idx)))
      {
        return this._orient(inString);
      }
      return args[idx];
    },
    
    _orient: function(inString)
    {
      return ("{" + inString + "}");
    }
  });
})();

