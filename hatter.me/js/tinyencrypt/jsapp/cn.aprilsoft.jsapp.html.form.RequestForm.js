/*
 * cn.aprilsoft.jsapp.html.form.RequestForm.js
 * jsapp, request form functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.html.form
  Package("cn.aprilsoft.jsapp.html.form");

  Class("cn.aprilsoft.jsapp.html.form.RequestForm", Extend(), Implement(),
  {
    _formId: null,
    _formElement: null,
    _names: null,
    _nameValueMap: null,
    _nameValueList: null,
    
    Constructor: function(formId)
    {
      this._formId = formId;
      this._formElement = document.getElementById(formId);
      if (this._formElement == null)
      {
        var formElementsByName = document.getElementsByName(formId);
        if ((formElementsByName != null) && (formElementsByName.length > 0))
        {
          this._formElement = formElementsByName;
        } 
      }
      if (this._formElement == null) {
          throw new Exception("We cannot find form by id or name: " + formId);
      }
      this.refresh();
    },
    
    bind: Static(function(formId)
    {
      return ThisClass().newInstance(formId);
    }),
    
    names: function()
    {
      return this._names;
    },
    
    value: function(name)
    {
      var values = this.values(name);
      if (values.length > 0)
      {
        return values[0];
      }
      return null;
    },
    
    values: function(name)
    {
      var values = this._nameValueMap[name];
      return ((values == null)? []: values);
    },
    
    refresh: function()
    {
      this._names = [];
      this._nameValueMap = {}; // struct Map<String, List<String>>
      this._nameValueList = []; // struct List<Array[2]/* key and value */>
      this._fill();
      return this;
    },
    
    buildQuery: function()
    {
      var query = [];
      for (var i = 0; i < this._nameValueList.length; i++)
      {
        var nameValue = this._nameValueList[i];
        query.push(encodeURIComponent(nameValue[0]) + "=" + encodeURIComponent(nameValue[1]));
      }
      return query.join("&");
    },
    
    _fill: function()
    {
      var elements = this._formElement.elements;
      for (var i = 0; i < elements.length; i++)
      {
        var element = elements[i];
        if ((element != null) && (element.name != ""))
        {
          // get tag name and convert to lower case
          var tagName = (element.tagName + "").toLowerCase();
          if (tagName == "input")
          {
            // get type if tag name is input
            var type = (element.type + "").toLowerCase();
            if (type == "radio")
            {
              // if radio
              if (element.checked)
              {
                this._addNameValue(element.name, element.value);
              }
            }
            else if (type == "checkbox")
            {
              // if check box
              if (element.checked)
              {
                this._addNameValue(element.name, element.value);
              }
            }
            else if ((type == "submit") || (type == "reset"))
            {
              // ignore
            }
            else
            {
              // others
              this._addNameValue(element.name, element.value);
            }
          }
          else if (tagName == "button")
          {
            // ignore
          }
          else
          {
            // if not input
            this._addNameValue(element.name, element.value);
          }
        }
      }
    },
    
    _addNameValue: function(name, value)
    {
      var values = this._nameValueMap[name];
      if (values == null)
      {
        values = [];
        this._nameValueMap[name] = values;
      }
      this._names.push(name);
      values.push(value);
      this._nameValueList.push([name, value]);
    }
  });
})();

