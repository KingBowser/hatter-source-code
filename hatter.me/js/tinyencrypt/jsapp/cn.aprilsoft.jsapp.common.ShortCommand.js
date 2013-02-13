/*
 * cn.aprilsoft.jsapp.common.ShortCommand.js
 * jsapp, common short commands functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  var System = Using("cn.aprilsoft.jsapp.system.System");
  var StringUtil = Using("cn.aprilsoft.jsapp.common.StringUtil");

  Class("cn.aprilsoft.jsapp.common.ShortCommand", Extend(), Implement(),
  {
    Constructor: function()
    {
      throw new Exception("This class cannot create instance.");
    }
  });

  $ = function(obj)
  {
    return System.getElement(obj);
  };

  $w = function()
  {
    return window;
  };

  $d = function()
  {
    return window.document;
  };

  $bits = function(strBits)
  {
    return parseInt(strBits, 2);
  };

  $empty = function(str)
  {
    return ((str == null) || (str == ""));
  };

  $join = function()
  {
    var params = arguments;
    if (arguments.length == 1)
    {
      if (System.isArray(arguments[0]))
      {
        params = arguments[0];
      }
    }
    var rl = [];
    for (var i = 0; i < params.length; i++)
    {
      rl.push(params[i]);
    }
    return rl.join("");
  };

  $name = function(obj)
  {
    return document.getElementsByName(obj);
  };

  $tag = function(obj)
  {
    return document.getElementsByTagName(obj);
  };

  $create = function(obj)
  {
    return document.createElement(obj);
  };

  $doCheck = function(obj)
  {
    var htmlobj = $(obj);
    htmlobj.checked = true;
  };

  $doUnCheck = function(obj)
  {
    var htmlobj = $(obj);
    htmlobj.checked = false;
  };

  $isChecked = function(obj)
  {
    var htmlobj = $(obj);
    return htmlobj.checked;
  };

  $isNotChecked = function(obj)
  {
    var htmlobj = $(obj);
    return (!htmlobj.checked);
  };

  $value = function(obj, value)
  {
    var htmlobj = $(obj);
    if (htmlobj == null)
    {
      throw new Exception("Cannot found html object:" + obj);
    }
    if (!(System.isUndefined(value)))
    {
      if ((htmlobj.tagName != null)
        && (htmlobj.tagName.toUpperCase)
        && (htmlobj.tagName.toUpperCase() == "SELECT"))
      {
        for (var i = 0; i < htmlobj.options.length; i++)
        {
          if (htmlobj.options[i].value == value)
          {
            htmlobj.options[i].selected = true;
            return value;
          }
        }
        return null;
      }
      else
      {
        htmlobj.value = value;
        return value;
      }
    }
    else
    {
      return htmlobj.value;
    }
  };

  $innerHTML = function(obj, value)
  {
    var htmlobj = $(obj);
    if (htmlobj == null)
    {
      throw new Exception("Cannot found html object:" + obj);
    }
    if (!(System.isUndefined(value)))
    {
      htmlobj.innerHTML = value;
      return value;
    }
    else
    {
      return htmlobj.innerHTML;
    }
  };

  $innerText = function(obj, value)
  {
    var htmlobj = $(obj);
    if (htmlobj == null)
    {
      throw new Exception("Cannot found html object:" + obj);
    }
    if (!(System.isUndefined(value)))
    {
      if (typeof(htmlobj.innerText) != "undefined")
      {
        htmlobj.innerText = value;
        return value;
      }
      else
      {
        htmlobj.textContent = value;
        return value;
      }
    }
    else
    {
      if (typeof(htmlobj.innerText) != "undefined")
      {
        return htmlobj.innerText;
      }
      else
      {
        return htmlobj.textContent;
      }
    }
  };

  $index = function(obj, objarray)
  {
    if (objarray == null)
    {
      throw new Exception("Search array is @null.");
    }
    for (var i = 0; i < objarray.length; i++)
    {
      if (objarray[i] == obj)
      {
        return i;
      }
    }
    return -1;
  };

  $disable = function(obj)
  {
    var htmlobj = $(obj);
    if (htmlobj == null)
    {
      throw new Exception("Cannot found html object:" + obj);
    }
    htmlobj.disabled = true;
  };

  $enable = function(obj)
  {
    var htmlobj = $(obj);
    if (htmlobj == null)
    {
      throw new Exception("Cannot found html object:" + obj);
    }
    htmlobj.disabled = false;
  };

  $isDisabled = function(obj)
  {
    var htmlobj = $(obj);
    if (htmlobj == null)
    {
      throw new Exception("Cannot found html object:" + obj);
    }
    return htmlobj.disabled;
  };

  $isEnabled = function(obj)
  {
    return (!isDisabled(obj));
  };

  $show = function(obj)
  {
    var htmlobj = $(obj);
    if (htmlobj == null)
    {
      throw new Exception("Cannot found html object:" + obj);
    }
    htmlobj.style.visibility = "visibile";
    htmlobj.style.display = "block";
  };

  $hide = function(obj)
  {
    var htmlobj = $(obj);
    if (htmlobj == null)
    {
      throw new Exception("Cannot found html object:" + obj);
    }
    htmlobj.style.visibility = "hidden";
    htmlobj.style.display = "none";
  };

  $repeat = function(strStr, intNum)
  {
    return StringUtil.repeat(strStr, intNum);
  };

  $style = function(domObj, objStyle)
  {
    System.setStyle(domObj, objStyle);
  };

  var _getConvertObjParamToString = function(objParam)
  {
    var lstParamList = [];
    if (objParam != null)
    {
      for (var k in objParam)
      {
        if (StringUtil.contains(objParam[k], "\""))
        {
          throw new Exception("HTML tag attribute cannot include (\")!");
        }
        lstParamList.push(k + "=\"" + objParam[k] + "\"");
      }
    }
    return lstParamList.join(" ");
  };

  $blank = function(count)
  {
    if (System.isUndefined(count))
    {
      return "&nbsp;";
    }
    else
    {
      var result = [];
      for (var i = 0; i < count; i++)
      {
        result.push("&nbsp;");
      }
      
      return result.join("");
    }
  };

  $lt = function()
  {
    if (System.isUndefined(count))
    {
      return "&lt;";
    }
    else
    {
      var result = [];
      for (var i = 0; i < count; i++)
      {
        result.push("&lt;");
      }
      
      return result.join("");
    }
  };

  $gt = function()
  {
    if (System.isUndefined(count))
    {
      return "&gt;";
    }
    else
    {
      var result = [];
      for (var i = 0; i < count; i++)
      {
        result.push("&gt;");
      }
      
      return result.join("");
    }
  };

  $br = function(objParam)
  {
    return StringUtil.format("<br %s/>", _getConvertObjParamToString(objParam));
  };

  $hr = function(objParam)
  {
    return StringUtil.format("<hr %s/>", _getConvertObjParamToString(objParam));
  };

  $h1 = function(strStr)
  {
    return StringUtil.format("<h1>%s</h1>", strStr);
  };

  $h2 = function(strStr)
  {
    return StringUtil.format("<h2>%s</h2>", strStr);
  };

  $h3 = function(strStr)
  {
    return StringUtil.format("<h3>%s</h3>", strStr);
  };

  $h4 = function(strStr)
  {
    return StringUtil.format("<h4>%s</h4>", strStr);
  };

  $h5 = function(strStr)
  {
    return StringUtil.format("<h5>%s</h5>", strStr);
  };

  $h6 = function(strStr)
  {
    return StringUtil.format("<h6>%s</h6>", strStr);
  };

  $td = function(strStr, objParam)
  {
    return StringUtil.format("<td%s>%s</td>", _getConvertObjParamToString(objParam), strStr);
  };

  $th = function(strStr, objParam)
  {
    return StringUtil.format("<th %s>%s</th>", _getConvertObjParamToString(objParam), strStr);
  };

  $tr = function(strStr, objParam)
  {
    return StringUtil.format("<tr %s>%s</tr>", _getConvertObjParamToString(objParam), strStr);
  };

  $thead = function(strStr, objParam)
  {
    return StringUtil.format("<thead %s>%s</thead>", _getConvertObjParamToString(objParam), strStr);
  };

  $tbody = function(strStr, objParam)
  {
    return StringUtil.format("<tbody %s>%s</tbody>", _getConvertObjParamToString(objParam), strStr);
  };

  $table = function(strStr, objParam)
  {
    return StringUtil.format("<table %s>%s</table>", _getConvertObjParamToString(objParam), strStr);
  };

  $a = function(strStr, objParam)
  {
    return StringUtil.format("<a %s>%s</a>", _getConvertObjParamToString(objParam), strStr);
  };

  $button = function(strStr, objParam)
  {
    return StringUtil.format("<button %s>%s</button>", _getConvertObjParamToString(objParam), strStr);
  };

  $input = function(objParam)
  {
    return StringUtil.format("<input %s/>", _getConvertObjParamToString(objParam));
  };

  $div = function(strStr, objParam)
  {
    return StringUtil.format("<div %s>%s</div>", _getConvertObjParamToString(objParam), strStr);
  };

  $font = function(strStr, objParam)
  {
    return StringUtil.format("<font %s>%s</font>", _getConvertObjParamToString(objParam), strStr);
  };

  $title = function(strStr, objParam)
  {
    return StringUtil.format("<title %s>%s</title>", _getConvertObjParamToString(objParam), strStr);
  };

  $strong = function(strStr, objParam)
  {
    return StringUtil.format("<strong %s>%s</strong>", _getConvertObjParamToString(objParam), strStr);
  };

  $b = function(strStr, objParam)
  {
    return StringUtil.format("<b %s>%s</b>", _getConvertObjParamToString(objParam), strStr);
  };

  $i = function(strStr, objParam)
  {
    return StringUtil.format("<i %s>%s</i>", _getConvertObjParamToString(objParam), strStr);
  };

  $u = function(strStr, objParam)
  {
    return StringUtil.format("<u %s>%s</u>", _getConvertObjParamToString(objParam), strStr);
  };

  $s = function(strStr, objParam)
  {
    return StringUtil.format("<s %s>%s</s>", _getConvertObjParamToString(objParam), strStr);
  };

  $tt = function(strStr, objParam)
  {
    return StringUtil.format("<tt %s>%s</tt>", _getConvertObjParamToString(objParam), strStr);
  };

  $label = function(strStr, objParam)
  {
    return StringUtil.format("<label %s>%s</label>", _getConvertObjParamToString(objParam), strStr);
  };

  $field = function(strStr, objParam)
  {
    return StringUtil.format("<field %s>%s</field>", _getConvertObjParamToString(objParam), strStr);
  };

  $p = function(strStr, objParam)
  {
    return StringUtil.format("<p %s>%s</p>", _getConvertObjParamToString(objParam), strStr);
  };

  $form = function(strStr, objParam)
  {
    return StringUtil.format("<form %s>%s</form>", _getConvertObjParamToString(objParam), strStr);
  };

  $legend = function(strStr, objParam)
  {
    return StringUtil.format("<legend %s>%s</legend>", _getConvertObjParamToString(objParam), strStr);
  };

  $textarea = function(strStr, objParam)
  {
    return StringUtil.format("<textarea %s>%s</textarea>", _getConvertObjParamToString(objParam), strStr);
  };

  $code = function(strStr, objParam)
  {
    return StringUtil.format("<code %s>%s</code>", _getConvertObjParamToString(objParam), strStr);
  };

  $meta = function(strStr, objParam)
  {
    return StringUtil.format("<meta %s>%s</meta>", _getConvertObjParamToString(objParam), strStr);
  };

  $center = function(strStr, objParam)
  {
    return StringUtil.format("<center %s>%s</center>", _getConvertObjParamToString(objParam), strStr);
  };

  $color = function(strStr, objParam)
  {
    return StringUtil.format("<font style=\"color:%s;\">%s</font>", objParam, strStr);
  };

  $size = function(strStr, objParam)
  {
    return StringUtil.format("<font style=\"font-size:%s;\">%s</font>", objParam, strStr);
  };
})();

