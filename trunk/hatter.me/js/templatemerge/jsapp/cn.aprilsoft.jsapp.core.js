/**
 * cn.aprilsoft.jsapp.core.js
 * JavaScript Application(jsapp), core functions
 * The core was browsers(including ie, ff, opera, safari, chrome etc.) compatible.
 * And the core support Package, Class, and Exception etc.
 * When use jsapp, all the key words should not be re-defined in you programme.
 * This framework was designed for javascript application(i.e. Ajax etc.).
 * Now you can use this framework to create google gadgets, wscript or cscript programmes.
 * 
 * And notice that this framework was released under LGPL.
 * Some code is from third party, these codes may not published under LGPL.
 * --> see: http://www.aprilsoft.cn/copyleft/lgpl.html
 * 
 * Author: Hatter Jiang (jht5945[at]gmail.com) (since 16th Oct, 2006)
 */

(function()
{
  var PACKAGE_NAME_ID_FIELD = "_package_name_id_field";
  
  var INTERFACE_NAME_ID_FIELD = "_interface_name_id_field";
  
  var CLASS_NAME_ID_FIELD     = "_class_name_id_field";
  
  var IMPLEMENT_INTERFACELIST = "_implement_interfacelist";
  
  var EXTEND_SUPER_CLASSLIST  = "_extend_super_classlist";
  
  var INTERFACE_TYPE_FUNCTION = "_interface_type_function";
  
  var INTERFACE_TYPE_VARIANT  = "_interface_type_variant";
  
  var INTERFACE_FUNCITON_LIST = "_interface_variant_list";
  
  var INTERFACE_VARIANT_LIST  = "_interface_variant_list";
  
  var MEMBER_TYPE_STATIC      = "_member_type_static";
  
  var MEMBER_TYPE_ABSTRACT    = "_member_type_abstract";
  
  var CLASS_PROROTYPE         = "prototype";
  
  var HAS_TYPE_FLAG           = "_has_type_flag";
  
  // when implement do not copy properties below
  var EXTENDTION_DO_NOT_COPY_MAP = {};
  EXTENDTION_DO_NOT_COPY_MAP._intfName = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP._className = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP[CLASS_PROROTYPE] = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP[PACKAGE_NAME_ID_FIELD] = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP[CLASS_NAME_ID_FIELD] = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP[INTERFACE_NAME_ID_FIELD] = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP[EXTEND_SUPER_CLASSLIST] = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP[IMPLEMENT_INTERFACELIST] = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP[INTERFACE_TYPE_VARIANT] = HAS_TYPE_FLAG;
  EXTENDTION_DO_NOT_COPY_MAP[INTERFACE_FUNCITON_LIST] = HAS_TYPE_FLAG;
  
  // path end with "/" or "\"
  var _sys_include_base_path = "";
  
  // include package type
  // 0 -- filename
  // 1 -- folder
  SYS_CORE_PACKAGE_TYPE_FILENAME = 0;
  SYS_CORE_PACKAGE_TYPE_FOLDER = 1;
  var _sys_include_package_type = SYS_CORE_PACKAGE_TYPE_FILENAME;
  
  var _try_show_message = function(message)
  {
    try
    {
      alert(message);
    }
    catch(e)
    {
      // do nothing
    }
  };
  
  var _sys_getPackage = function(strPackage)
  {
    try
    {
      var packageObject = eval(strPackage);
      if (typeof(packageObject) == "undefined")
      {
        throw new Error("Package not found!");
      }
      return packageObject;
    }
    catch(e)
    {
      throw new Exception("Error occured when get package object.");
    }
  };
  
  var _sys_getObjectValue = function(obj, key)
  {
    var k;
    for (k in obj)
    {
      if (k == key)
      {
        return obj[k];
      }
    }
    return null;
  };
  
  var _sys_getSystemMethod = function(classobj)
  {
    var systemMethods = _sys_getSystemMethod._systemMethods;
    if (systemMethods == null)
    {
      systemMethods = {};
      systemMethods["_className"] = "";
      systemMethods["_classStatic"] = "";
      systemMethods["getClass"] = "";
      systemMethods["getClassName"] = "";
      systemMethods["getShortClassName"] = "";
      systemMethods["Super"] = "";
      systemMethods["Implements"] = "";
      systemMethods["Extends"] = "";
      systemMethods["InstanceOf"] = "";
      systemMethods["newInstance"] = "";
      systemMethods["toString"] = "";
      systemMethods["_toString"] = "";
      // systemMethods[""] = "";
      
      _sys_getSystemMethod._systemMethods = systemMethods;
    }
    for (var k in classobj)
    {
      if (systemMethods[k] != null)
      {
        return k;
      }
    }
    return null;
  };
  
  var _sys_setRetSysCoreFunc = function(func)
  {
    var funcName = func;
    func = eval(func);
    func._funcName = "KeyWord#" + funcName;
    
    var oldToString = func.toString;
    func["_sys_toString"] = oldToString;
    
    var functionValue = func.toString();
    functionValue = functionValue.replace(/\s+/g, " ");
    var firstLB = functionValue.indexOf("(");
    var firstRB = functionValue.indexOf(")");
    var paramLine = functionValue.substring((firstLB + 1), firstRB);
    paramLine = paramLine.replace(/((^\s*)|(\s*$))/g, "");
    var paramList = paramLine.split(/\s*,\s*/);
    
    func.toString = function()
    {
      return "function(" + paramList.join(", ") + ")\r\n{\r\n  [jsapp core]\r\n}";
    };
  };
  
  // is interface
  var _sys_isInterface = function(interfaceObject)
  {
    return (typeof(interfaceObject[INTERFACE_NAME_ID_FIELD]) != "undefined")? true: false;
  };
  
  // is class
  var _sys_isClass = function(classObject)
  {
    return (typeof(classObject[CLASS_NAME_ID_FIELD]) != "undefined")? true: false;
  };
  
  // is static
  var _sys_isStatic = function(staticObject)
  {
    if (staticObject == null)
    {
      return false;
    }
    return (typeof(staticObject[MEMBER_TYPE_STATIC]) != "undefined")? true: false;
  };
  
  // is error
  var _sys_isError = function(objError)
  {
    return (objError instanceof Error);
  };
  
  // is exception
  var _sys_isException = function(objException)
  {
    return (objException instanceof Exception);
  };
  
  // is abstract function
  var _sys_isAbstractFunction = function(abstractObject)
  {
    if (abstractObject == null)
    {
      return false;
    }
    if ((typeof(abstractObject) == "function") || (abstractObject instanceof Function))
    {
      return (typeof(abstractObject[MEMBER_TYPE_ABSTRACT]) != "undefined")? true: false;
    }
    else
    {
      return false;
    }
  };
  
  // base class object
  var _sys_baseClass = function(){};
  _sys_baseClass._className = "cn.aprilsoft.jsapp.core.Object";
  _sys_baseClass[EXTEND_SUPER_CLASSLIST] = [];
  _sys_baseClass[IMPLEMENT_INTERFACELIST] = [];
  _sys_baseClass._toString = _sys_baseClass.toString;
  _sys_baseClass.toString = function()
  {
    return "class $" + this._className;
  };
  _sys_baseClass.toString._funcName = _sys_baseClass._className + "#Static(toString)";
  _sys_baseClass.getClassName = function()
  {
    return this._className;
  };
  _sys_baseClass.getClassName._funcName = _sys_baseClass._className + "#Static(getClassName)";
  _sys_baseClass.getShortClassName = function()
  {
    return this._className.replace(/.*\.([^\.]+)$/, "$1");
  };
  _sys_baseClass.getShortClassName._funcName = _sys_baseClass._className + "#Static(getShortClassName)";
  _sys_baseClass.prototype.Super = function(strMemberName)
  {
    var memberObj = this["_super_" + strMemberName];
    if (typeof(memberObj) == "function")
    {
      var args = [];
      if (arguments.length > 1)
      {
        for (var i = 1; i < arguments.length; i++)
        {
          if (i > 1)
          {
            args.push(", ");
          }
          args.push("arguments[" + i + "]");
        }
      }
      return eval("this._super_" + strMemberName + "(" + args.join("") + ")");
    }
    return eval("this._super_" + strMemberName);
  };
  _sys_baseClass.prototype.Super._funcName = _sys_baseClass._className + "#Super";
  
  // get package
  Package = function(packagePath, packageObject)
  {
    var packageName = [];
    var currentPackage;
    if ((packagePath != null) && (packagePath != ""))
    {
      var packagePathList = packagePath.split(".");
      packageName.push(packagePathList[0]);
      try
      {
        currentPackage = eval(packagePathList[0]);
      }
      catch(e)
      {
        currentPackage = eval(packagePathList[0] + "=  {}");
        currentPackage[PACKAGE_NAME_ID_FIELD] = packageName.join(".");
      }
      
      if (Package._rootPackageList.indexOf(packagePathList[0]) < 0)
      {
        Package._rootPackageList.push(packagePathList[0]);
      }
      
      var topPackage = currentPackage;
      for(var i = 1; i < packagePathList.length; i++)
      {
        packageName.push(packagePathList[i]);
        if (typeof(currentPackage[packagePathList[i]]) == "undefined")
        {
          currentPackage[packagePathList[i]] = {};
          currentPackage[packagePathList[i]][PACKAGE_NAME_ID_FIELD] = packageName.join(".");
        }
        currentPackage = currentPackage[packagePathList[i]];
      }
      if (typeof(packageObject) != "undefined")
      {
        var k;
        for (k in packageObject)
        {
          currentPackage[k] = packageObject[k];
        }
      }
    }
  };
  Package._rootPackageList = ["cn"];
  
  // load file
  Load = function(filename, autoaddjsflag, charset, loadCallBack)
  {
    if ((autoaddjsflag == null) || (autoaddjsflag == true))
    {
      if (!(/\.js$/.test(filename)))
      {
        filename += ".js";
      }
    }
    var includescript = document.createElement("script");
    includescript.type = "text/javascript";
    includescript.charset = ((charset == null)? "UTF-8": charset);
    includescript.src = _sys_include_base_path + filename;
    
    if (loadCallBack != null)
    {
      var callbackInvokeFlag = false;
      includescript.onreadystatechange = function()
      {
        if ((includescript.readyState == "loaded") || (includescript.readyState == "complete"))
        {
          includescript.onreadystatechange = null;
          if (!callbackInvokeFlag)
          {
            callbackInvokeFlag = true;
            loadCallBack(true);
          }
        }
      };
      includescript.onload = function()
      {
        includescript.onload = null;
        if (!callbackInvokeFlag)
        {
          callbackInvokeFlag = true;
          loadCallBack(true);
        }
      };
    }
    
    var headelement = document.getElementsByTagName("head")[0];
    var scriptelements = headelement.getElementsByTagName("script");
    // test if already loaded
    for(var i = 0; i < scriptelements.length; i++)
    {
      if (scriptelements[i].src == filename)
      {
        return;
      }
    }
    headelement.appendChild(includescript);
  };
  
  // set include path
  SetIncludePath = function(path)
  {
    _sys_include_base_path = path;
  };
  
  // set include package type
  SetIncludePackagType = function(packageType)
  {
    _sys_include_package_type = packageType;
  };
  
  if (typeof(_sys_include_handle) == "undefined")
  {
    _sys_include_handle = function(filename, charset)
    {
      document.write("<" + "script "
                 + "type=\"text/javascript\" "
                 + "charset=\"" + charset + "\" "
                 + "src=\"" + filename + "\">");
      document.write("</" + "script" + ">\n");
    };
  }
  
  // include file
  Include = function(filename, autoaddjsflag, charset)
  {
    if ((autoaddjsflag == null) || (autoaddjsflag == true))
    {
      if (!(/\.js$/.test(filename)))
      {
        filename += ".js";
      }
    }
    if (_sys_include_package_type == SYS_CORE_PACKAGE_TYPE_FOLDER)
    {
      // package is folder (like java)
      filename = filename.replace(/\./g, "/");
      filename = filename.replace(/\/js$/, ".js");
    }
    charset = ((charset == null)? "UTF-8": charset);
    _sys_include_handle(_sys_include_base_path + filename, charset);
  };
  
  // using package
  Using = function(packagePath, hideTryMessage)
  {
    try
    {
      var packageObject = eval(packagePath);
      if (typeof(packageObject) == "undefined")
      {
        throw new Error("Package not found!");
      }
      return packageObject;
    }
    catch(e)
    {
      // package not found
      if (!hideTryMessage)
      {
        _try_show_message("Using package:\"" + packagePath + "\" failed!");
      }
      throw new Exception("Package not found!");
    }
  };
  
  // using package when runtime
  Using.F = function(packagePath)
  {
    var usingF = function()
    {
      return Using(packagePath);
    };
    usingF.isLoaded = function()
    {
      return (usingF.tryUsing() != null);
    };
    usingF.tryUsing = function()
    {
      try
      {
        return Using(packagePath, true/* hide try message */);
      }
      catch(e)
      {
        return null;
      }
    };
    usingF.toString = function()
    {
      return "using.f: " + packagePath;
    };
    return usingF;
  };
  
  // import package
  Import = function(packagePath)
  {
    var pathList = packagePath.split(".");
    if (pathList[pathList.length - 1] != "*")
    {
      var packageObject = Using(packagePath);
      if (packageObject != null)
      {
        eval(pathList[pathList.length - 1] + "=" + packagePath);
      }
    }
    else
    {
      pathList.pop();
      packagePath = pathList.join(".");
      var packageObject = Using(packagePath);
      var k;
      for (k in packageObject)
      {
        eval(k + "=" + packagePath + "." + k);
      }
    }
  };
  
  // implement
  Implement = function()
  {
    var implementList = [];
    for (var i = 0; i < arguments.length; i++)
    {
      var implementInterface = arguments[i];
      if ((implementInterface != null) && (typeof(implementInterface) == "string"))
      {
        implementInterface = Using(implementInterface);
      }
      if (!(_sys_isInterface(implementInterface)))
      {
        _try_show_message("Object " + arguments[i] + " is not interface.");
        throw new Exception("Object " + arguments[i] + " is not interface.");
      }
      if (implementList.indexOf(implementInterface) < 0)
      {
        for (var ind = 0; ind < implementInterface[IMPLEMENT_INTERFACELIST].length; ind++)
        {
          if (implementList.indexOf(implementInterface[IMPLEMENT_INTERFACELIST][ind]) < 0)
          {
            implementList.push(implementInterface[IMPLEMENT_INTERFACELIST][ind]);
          }
        }
        implementList.push(implementInterface);
      }
    }
    return implementList;
  };
  
  // extend
  Extend = function(extendClass)
  {
    var extendClassName = extendClass;
    if (arguments.length > 1)
    {
      _try_show_message("More than one super class");
      throw new Exception("more than one super class");
    }
    if ((extendClass != null) && (typeof(extendClass) == "string"))
    {
      extendClass = Using(extendClass);
    }
    if ((extendClass != null) && (!(_sys_isClass(extendClass))))
    {
      _try_show_message("Object " + extendClassName + " is not class.");
      throw new Exception("Object " + extendClassName + " is not class.");
    }
    return (extendClass == null)? _sys_baseClass: extendClass;
  };
  
  // extends
  Extends = function(objectObject, classObject)
  {
    if ((objectObject != null) && (typeof(objectObject.Extends) == "function"))
    {
      return objectObject.Extends(classObject);
    }
    else
    {
      return false;
    }
  };
  
  // imelements
  Implements = function(objectObject, interfaceObject)
  {
    if ((objectObject != null) && (typeof(objectObject.Implements) == "function"))
    {
      return objectObject.Implements(interfaceObject);
    }
    else
    {
      return false;
    }
  };
  
  // instanceof
  InstanceOf = function(objectObject, classOrInterfaceObject)
  {
    if ((objectObject != null) && (typeof(objectObject.InstanceOf) == "function"))
    {
      return objectObject.InstanceOf(classOrInterfaceObject);
    }
    else
    {
      return false;
    }
  };
  
  // interface
  Interface = function(interfaceName, implementInterface, newInterfaceObject)
  {
    var classobjsysmethod = _sys_getSystemMethod(newInterfaceObject);
    if (classobjsysmethod != null)
    {
      _try_show_message("Method:`" + classobjsysmethod + "' is system reserved method.");
      throw new Exception("Method:`" + classobjsysmethod + "' is system reserved method.");
    }
    
    var interfaceObject = {};
    interfaceObject.prototype = {};
    
    if (implementInterface != null)
    {
      for (var i = 0; i < implementInterface.length; i++)
      {
        var newImplementInterface = implementInterface[i];
        
        for (var k in newImplementInterface)
        {
          if (EXTENDTION_DO_NOT_COPY_MAP[k] != HAS_TYPE_FLAG)
          {
            interfaceObject[k] = newImplementInterface[k];
          }
        }
        
        for (var k in newImplementInterface.prototype)
        {
          interfaceObject.prototype[k] = newImplementInterface.prototype[k];
        }
      }
    }
    
    interfaceObject[INTERFACE_NAME_ID_FIELD] = interfaceName;
    interfaceObject._intfName = interfaceName;
    interfaceObject[IMPLEMENT_INTERFACELIST] = implementInterface;
    
    interfaceObject.getInterfaceName = function()
    {
      return this._intfName;
    };
    interfaceObject.getInterfaceName._funcName = interfaceObject._intfName + "#Static(getInterfaceName)";
    interfaceObject.getShortInterfaceName = function()
    {
      return this._intfName.replace(/.*\.([^\.]+)$/, "$1");
    };
    interfaceObject.getShortInterfaceName._funcName = interfaceObject._intfName + "#Static(getShortInterfaceName)";
    
    for (var k in newInterfaceObject)
    {
      if (k == "Constructor")
      {
        _try_show_message("Interface " + interfaceName + " cannot have a contructor.");
        throw new Exception("Interface " + interfaceName + " cannot have a contructor.");
      }
      else if (k == "Class")
      {
        _try_show_message("Interface " + interfaceName + " cannot have init function.");
        throw new Exception("Interface " + interfaceName + " cannot have init function.");
      }
      else
      {
        if (_sys_isStatic(newInterfaceObject[k]))
        {
          var staticObject = newInterfaceObject[k].staticObject;
          if ((typeof(staticObject) == "function") ||  (staticObject instanceof Function))
          {
            _try_show_message("Interface " + interfaceName + " cannot have static function.");
            throw new Exception("Interface " + interfaceName + " cannot have static function.");
          }
          interfaceObject[k] = staticObject;
        }
        else if (!(_sys_isAbstractFunction(newInterfaceObject[k])))
        {
          _try_show_message("Interface " + interfaceName + " cannot have non-abstract function.");
          throw new Exception("Interface " + interfaceName + " cannot have non-abstract function.");
        }
        else
        {
          interfaceObject.prototype[k] = newInterfaceObject[k];
          interfaceObject.prototype[k]._funcName = interfaceName + "#" + k;
        }
      }
    }
    
    // implements
    interfaceObject.Implements = function(paramInterfaceObject)
    {
      return (interfaceObject[IMPLEMENT_INTERFACELIST].indexOf(paramInterfaceObject) >= 0)
    };
    interfaceObject.Implements._funcName = interfaceName + "#Static(Implements)";
    
    // toString
    interfaceObject._toString = interfaceObject.toString;
    interfaceObject.toString = function()
    {
      return "interface $" + interfaceName;
    };
    interfaceObject.toString._funcName =  interfaceName + "#Static(toString)";
    
    // start set interface object to interface name
    try
    {
      eval(interfaceName + " = interfaceObject;");
    }
    catch(e)
    {
      _try_show_message("Pease make package first. In Interface `" + interfaceName + "`.");
      throw new Exception("Pease make package first. In Interface `" + interfaceName + "`.");
    }
    // end set interface object to interface name
    
    return interfaceObject;
  };
  
  // is interface
  Interface.IS = function(interfaceObject)
  {
    if (interfaceObject == null)
    {
      return false;
    }
    return (typeof(interfaceObject[INTERFACE_NAME_ID_FIELD]) != "undefined");
  };
    
  // static
  Static = function(staticValue)
  {
    var staticObjectContainer = {};
    staticObjectContainer[MEMBER_TYPE_STATIC] = HAS_TYPE_FLAG;
    staticObjectContainer.staticObject = staticValue;
    return staticObjectContainer;
  };
  
  // private
  Private = function(privateFunction)
  {
    return privateFunction;
  };
  
  // abstract
  Abstract = function()
  {
    var abstractFunction = function()
    {
      throw new Exception("Abstractor funciton.");
    };
    abstractFunction[MEMBER_TYPE_ABSTRACT] = HAS_TYPE_FLAG;
    
    return abstractFunction;
  };
  
  // abstract class
  Abstract.Class = function(className, extendClass, implementInterface, newClassObject)
  {
    Class(className, extendClass, implementInterface, newClassObject, true);
  };
  
  // static class
  Static.Class = function(className, extendClass, implementInterface, newClassObject, isAbstract)
  {
    newClassObject.Constructor = function()
    {
      throw new Exception("Class " + className + " is a static class, cannot create instance.");
    };
    Class(className, extendClass, implementInterface, newClassObject, isAbstract);
  };
  
  // class
  Class = function(className, extendClass, implementInterface, newClassObject, isAbstract)
  {
    isAbstract = (isAbstract == null)? false: isAbstract;
    var abstractFunctionMap = {};
    var classobjsysmethod = _sys_getSystemMethod(newClassObject);
    if (classobjsysmethod != null)
    {
      _try_show_message("Method:`" + classobjsysmethod + "' is system reserved method.");
      throw new Exception("Method:`" + classobjsysmethod + "' is system reserved method.");
    }
    
    var tmp_constructor = _sys_getObjectValue(newClassObject, "Constructor");
    //var classObject = (tmp_constructor == null)? function(){}: tmp_constructor;
    var func;
    var classObject;
    // get constructor
    if ((extendClass == _sys_baseClass) || (extendClass == null))
    {
      extendClass = _sys_baseClass;
      classObject = (tmp_constructor == null)
                    ? ((isAbstract)?  function()
                                      {
                                        throw new Exception("Cannot create instance of abstract class.");
                                      }
                                   :  function(){}
                      )
                    : tmp_constructor;
      classObject._super__constructor = function(){};
    }
    else
    {
      // not a good copy method
      classObject = (tmp_constructor == null)?
                    eval("func = " + extendClass._constructor.toString()):
                    tmp_constructor;
      classObject._super__constructor = eval("func = " + extendClass._constructor.toString());
    }
    classObject._constructor = eval("func = " + classObject.toString());
    
    // copy extended classes
    classObject[EXTEND_SUPER_CLASSLIST] = [];
    if (extendClass[EXTEND_SUPER_CLASSLIST] != null)
    {
      for (var i = 0; i < extendClass[EXTEND_SUPER_CLASSLIST].length; i++)
      {
        classObject[EXTEND_SUPER_CLASSLIST].push(extendClass[EXTEND_SUPER_CLASSLIST][i]);
      }
    }
    classObject[EXTEND_SUPER_CLASSLIST].push(extendClass);
    
    // copy implemented interfaces
    classObject[IMPLEMENT_INTERFACELIST] = [];
    if (extendClass[IMPLEMENT_INTERFACELIST] != null)
    {
      for (var i = 0; i < extendClass[IMPLEMENT_INTERFACELIST].length; i++)
      {
        classObject[IMPLEMENT_INTERFACELIST].push(extendClass[IMPLEMENT_INTERFACELIST][i]);
      }
    }
    if (implementInterface != null)
    {
      for (var i = 0; i < implementInterface.length; i++)
      {
        classObject[IMPLEMENT_INTERFACELIST].push(implementInterface[i]);;
      }
    }
    if (implementInterface != null)
    {
      for (var i = 0; i < implementInterface.length; i++)
      {
        var newImplementInterface = implementInterface[i];
        
        /* WE SHOULD NOT COPY STATIC PROPERTIES
        for (var k in newImplementInterface)
        {
          // if (_sys_isAbstractFunction(newImplementInterface[k]))
          // {
          // abstract function should not be static
          if (EXTENDTION_DO_NOT_COPY_MAP[k] != HAS_TYPE_FLAG)
          {
            classObject[k] = newImplementInterface[k];
          }
          // }
        }
        */
        
        for (var k in newImplementInterface.prototype)
        {
          if (_sys_isAbstractFunction(newImplementInterface.prototype[k]))
          {
            abstractFunctionMap[k] = HAS_TYPE_FLAG;
            classObject.prototype[k] = newImplementInterface.prototype[k];
          }
        }
      }
    }
    
    var k;
    // super class static members
    /* WE SHOULD NOT COPY STATIC PROPERTIES
    if (extendClass != null)
    {
      for (k in extendClass)
      {
        // bugfix in ff,chrome etc., do not copy prototype
        if (EXTENDTION_DO_NOT_COPY_MAP[k] != HAS_TYPE_FLAG)
        {
          classObject[k] = extendClass[k];
        }
      }
    }
    */
    
    // super class non-static members
    if ((extendClass != null) && (extendClass.prototype != null))
    {
      for (k in extendClass.prototype)
      {
        var lenOfSuper = "_super_".length;
        if ((k.length < lenOfSuper) || (k.substring(0, lenOfSuper) != "_super_"))
        {
          if (_sys_isAbstractFunction(extendClass.prototype[k]))
          {
            abstractFunctionMap[k] = HAS_TYPE_FLAG;
          }
        }
        classObject.prototype[k] = extendClass.prototype[k];
      }
    }
    classObject[CLASS_NAME_ID_FIELD] = className;
    classObject._className = className;
    
    classObject.getClassName = function()
    {
      return this._className;
    };
    classObject.getClassName._funcName = classObject._className + "#Static(getClassName)";
    classObject.getShortClassName = function()
    {
      return this._className.replace(/.*\.([^\.]+)$/, "$1");
    };
    classObject.getShortClassName._funcName = classObject._className + "#Static(getShortClassName)";
    
    for (k in newClassObject)
    {
      if (k == "Constructor")
      {
        newClassObject[k]._funcName = className + "#Constructor";
      }
      else if (k == "Class")
      {
        if (!(_sys_isStatic(newClassObject[k])))
        {
          _try_show_message(className + "#Static() cannot be non-static.");
          throw new Exception(className + "#Static() cannot be non-static.");
        }
        newClassObject[k].staticObject._funcName = className + "#Static()";
        classObject._classStatic = newClassObject[k].staticObject;
      }
      else
      {
        var objfunc;
        if (_sys_isStatic(newClassObject[k]))
        {
          // class static members
          objfunc = newClassObject[k].staticObject;
          classObject[k] = newClassObject[k].staticObject;
        }
        else
        {
          // class non-static members
          if (typeof(classObject.prototype[k]) != "undefined")
          {
            classObject.prototype["_super_" + k] = classObject.prototype[k];
          }
          objfunc = newClassObject[k];
          classObject.prototype[k] = newClassObject[k];
        }
        // set function "_funcName"
        if ((typeof(objfunc) == "function") || (objfunc instanceof Function))
        {
          if (_sys_isStatic(newClassObject[k]))
          {
            if (_sys_isAbstractFunction(newClassObject[k]))
            {
              _try_show_message("Static function " + k + " cannot be abstract.");
              throw new Exception("Static function " + k + " cannot be abstract.");
            }
            objfunc._funcName = className + "#Static(" + k + ")";
          }
          else
          {
            if (_sys_isAbstractFunction(newClassObject[k]))
            {
              abstractFunctionMap[k] = HAS_TYPE_FLAG;
            }
            else
            {
              delete abstractFunctionMap[k];
            }
            objfunc._funcName = className + "#" + k;
          }
        }
      }
    }
    // class object
    classObject.prototype.getClass = function()
    {
      return classObject;
    };
    classObject.prototype.getClass._funcName =  className + "#getClass";
    // extends
    classObject.Extends = classObject.prototype.Extends = function(classObject)
    {
      return (ThisClass()[EXTEND_SUPER_CLASSLIST].indexOf(classObject) >= 0);
    };
    classObject.Extends._funcName = className + "#Static(Extends)";
    classObject.prototype.Extends._funcName = className + "#Extends";
    // implements
    classObject.Implements = classObject.prototype.Implements = function(interfaceObject)
    {
      return (ThisClass()[IMPLEMENT_INTERFACELIST].indexOf(interfaceObject) >= 0);
    };
    classObject.Implements._funcName = className + "#Static(Implements)";
    classObject.prototype.Implements._funcName = className + "#Implements";
    // instanceof
    classObject.prototype.InstanceOf = function(classOrInterfaceObject)
    {
      var thisClass = this.getClass();
      if (thisClass == classOrInterfaceObject)
      {
        return true;
      }
      return (thisClass.Extends(classOrInterfaceObject) || thisClass.Implements(classOrInterfaceObject));
    };
    classObject.prototype.InstanceOf._funcName = className + "#InstanceOf";
    // instance toString
    classObject.prototype._toString = classObject.prototype.toString;
    var clsPrototypeAsString = classObject.prototype.asString;
    if ((typeof(clsPrototypeAsString) == "function") || (clsPrototypeAsString instanceof Function))
    {
      // if has asString then replace toString
      classObject.prototype.toString = classObject.prototype.asString;
    }
    else
    {
      classObject.prototype.toString = function()
      {
        return "[" + "object " + classObject.getClassName() + "]"
      };
    }
    // get instance
    classObject.newInstance = function()
    {
      var args = arguments;
      var newobject;
      switch(args.length)
      {
        case 0:
          newobject = new classObject();
          break;
        case 1:
          newobject = new classObject(args[0]);
          break;
        case 2:
          newobject = new classObject(args[0], args[1]);
          break;
        default:
          var argsList = [];
          for (var i = 0; i < args.length; i++)
          {
            argsList.push("args[" + i + "]");
          }
          newobject = eval("new classObject(" + argsList.join(", ") + ")");
          break;
      }
      return newobject;
    };
    classObject.newInstance._funcName =  className + "#Static(newInstance)";
    // class toString
    classObject._toString = classObject.toString;
    classObject.toString = function()
    {
      return "class $" + className;
    };
    classObject.toString._funcName =  className + "#Static(toString)";
    
    // check abstract function
    var abstractFunctionArray = [];
    for (var k in abstractFunctionMap)
    {
      if (abstractFunctionMap[k] == HAS_TYPE_FLAG)
      {
        abstractFunctionArray.push(k);
      }
    }
    if (isAbstract)
    {
      if (abstractFunctionArray.length == 0)
      {
        _try_show_message("Abstract class " + className + " has no abstract function.");
        throw new Exception("Abstract class " + className + " has no abstract function.");
      }
    }
    else
    {
      if (abstractFunctionArray.length > 0)
      {
        _try_show_message("Non-abstract class " + className + " has abstract function(s).");
        throw new Exception("Non-abstract class " + className + " has abstract function(s).");
      }
    }
    
    // start set class object to class name
    try
    {
      eval(className + " = classObject;");
    }
    catch(e)
    {
      _try_show_message("Pease make package first. In Class `" + className + "`.");
      throw new Exception("Pease make package first. In Class `" + className + "`.");
    }
    // end set class object to class name
    
    try
    {
      if (classObject._classStatic != null)
      {
        classObject._classStatic();
      }
    }
    catch(e)
    {
      _try_show_message("Error occured in invoke " + classObject + "#Static()");
      throw new Exception(e, "Error occured in invoke " + classObject + "#Static()");
    }
    
    return classObject;
  };
  
  // is interface
  Class.IS = function(classObject)
  {
    if (classObject == null)
    {
      return false;
    }
    return (typeof(classObject[CLASS_NAME_ID_FIELD]) != "undefined");
  };
  
  // class.forname
  Class.forName = function(strClassName)
  {
    return _sys_getPackage(strClassName);
  };
  
  // this class
  ThisClass = function()
  {
    var caller = arguments.callee.caller;
    if ((caller == null) || (caller._funcName == null))
    {
      throw new Exception("Caller is not a class method or constructor.");
    }
    var funcName = caller._funcName;
    var className = funcName.replace(/^([^#]+)#.*/, "$1");
    try
    {
      var classReference = Class.forName(className);
      return classReference;
    }
    catch(e)
    {
      throw new Exception(e, "Get class reference:" + className + " failed.");
    }
  };
  
  //exception
  Exception =  function()
  {
    if (arguments.length == 1)
    {
      if (arguments[0] instanceof Exception)
      {
        return arguments[0];
      }
    }
    if (this.constructor != Exception)
    {
      return null;
    }
    if (arguments.length > 0)
    {
      for (var i = 0; i < arguments.length; i++)
      {
        if (arguments[i] instanceof Error)
        {
          var errstrlist = [];
          var k;
          for (k in arguments[i])
          {
            errstrlist.push(k + " - " + ((arguments[i][k] == null)? "@null": arguments[i][k]));
          }
          this["arg" + i]  = errstrlist.join("\n...........: ");
        }
        else
        {
          this["arg" + i]  = (arguments[i] == null)? "@null": arguments[i];
        }
      }
    }
    // get trace here
    var trac = new Array();
    var call = arguments.callee.caller;
    var depth = 0;
    while (call != null)
    {
      var callFuncName = "";
      // check "_funcName"
      if (call._funcName != null)
      {
        // this will not work well if use class inherit
        callFuncName = call._funcName + ": ";
      }
      trac.push(callFuncName + call.toString().split(/(\r\n)|(\n)|(\r)/)[0] 
              + "\n              " + "(" + Array.prototype.slice.call(call.arguments).join(", ") + ") ");
      call = call.caller;
      depth++;
      
      if (depth >= 20)
      {
        trac.push("(more than 20 ...)");
        break;
      }
    }
    this.trace = trac.join("\n       ...... ");
    
    return this;
  };
  
  Exception.prototype.toString = function()
  {
    var thisArray = [];
    for (var k in this)
    {
      if (k != "toString")
      {
        thisArray.push(k + ":" + this[k]);
      }
    }
    return thisArray.join("\n");
  };
  
  // new exception
  // please use "new Exception(e)" instand
  NewException = function(e)
  {
    if (!(_sys_isException(e)))
    {
      var ex = new Exception(e);
      for (var i = 1; i < arguments.length; i++)
      {
        ex["arg" + i] = (arguments[i] == null)? "@null": arguments[i];
      }
      return ex;
    }
    return e;
  };
  
  // below is for ie5 or other browers
  // array prototype push
  if (!(Array.prototype.push))
  {
    Array.prototype.push = function()
    {
      for (var i = 0; i < arguments.length; i++)
      {
        this[this.length] = arguments[i];
      }
      return this.length;
    };
    Array.prototype.push._funcName = "Array#push";
  }
  
  // array prototype concat
  if (!(Array.prototype.concat))
  {
    Array.prototype.concat = function()
    {
      var a = [];
      Array.prototype.push.apply(a, this);
      for (var i = 0; i < arguments.length; i++)
      {
        if (arguments[i] instanceof Array)
        {
          Array.prototype.push.apply(a, arguments[i]);
        }
        else
        {
          a.push(arguments[i]);
        }
      }
      return a;
    };
    Array.prototype.concat._funcName = "Array#concat";
  }
  
  // array prototype pop
  if (!(Array.prototype.pop))
  {
    Array.prototype.pop = function()
    {
      if (this.length > 0)
      {
        var lastObject = this[this.length - 1];
        this.length -= 1;
        return lastObject;
      }
      else
      {
        return undefined;
      }
    };
    Array.prototype.push._funcName = "Array#push";
  }
  
  // array prototype shift
  if (!(Array.prototype.shift))
  {
    Array.prototype.shift = function()
    {
      if (this.length > 0)
      {
        var result = this[0];
        for (var i = 0; i < (this.length - 1); i++)
        {
          this[i] = this[i + 1];
        }
        this.length -= 1;
        return result;
      }
      else
      {
        return undefined;
      }
    };
    Array.prototype.shift._funcName = "Array#shift";
  }
  
  // array prototype unshift
  if (!(Array.prototype.unshift))
  {
    Array.prototype.unshift = function()
    {
      if ((this.length > 0) && (arguments.length > 0))
      {
        for (var i = (this.length + arguments.length - 1); i > (arguments.length - 1); i--)
        {
          this[i] = this[i - arguments.length];
        }
      }
      for (var i = 0; i < arguments.length; i++)
      {
        this[arguments.length - i] = arguments[i];
      }
      return this.length;
    };
    Array.prototype.unshift._funcName = "Array#unshift";
  }
  
  // array prototype slice
  if (!(Array.prototype.slice))
  {
    Array.prototype.slice = function(index, count)
    {
      if (index == null)
      {
        index = 0;
      }
      if (count == null)
      {
        count = this.length;
      }
      if ((index >= 0) && (index < this.length) && (count > 0))
      {
        var returnArray = [];
        for (var i = index; ((i < (index + count)) && (i < this.length)); i++)
        {
          returnArray.push(this[i]);
        }
        return returnArray;
      }
      else
      {
        return [];
      }
    };
    Array.prototype.slice._funcName = "Array#slice";
  }
  
  // array prototype splice
  if (!(Array.prototype.splice))
  {
    Array.prototype.splice = function(index, count)
    {
      if (count == null)
      {
        count = this.length;
      }
      if ((index >= 0) && (index < this.length) && (count > 0))
      {
        var returnArray = [];
        for (var i = index; ((i < (index + count)) && (i < this.length)); i++)
        {
          returnArray.push(this[i]);
        }
        if ((index + count) < this.length)
        {
          for (var i = index; (i + count) < this.length; i++)
          {
            this[i] = this[i + count];
          }
        }
        this.length -= returnArray.length;
        return returnArray;
      }
      else
      {
        return [];
      }
    };
    Array.prototype.splice._funcName = "Array#splice";
  }
  
  // array prototype indexOf
  if (!(Array.prototype.indexOf))
  {
    Array.prototype.indexOf = function(obj)
    {
      for (var i = 0; i < this.length; i++)
      {
        if (this[i] == obj)
        {
          return i;
        }
      }
      return -1;
    };
    Array.prototype.indexOf._funcName = "Array#indexOf";
  }
  
  // array prototype lastIndexOf
  if (!(Array.prototype.lastIndexOf))
  {
    Array.prototype.lastIndexOf = function(obj)
    {
      for (var i = (this.length - 1); i >= 0; i--)
      {
        if (this[i] == obj)
        {
          return i;
        }
      }
      return -1;
    };
    Array.prototype.lastIndexOf._funcName = "Array#lastIndexOf";
  }
  
  // function prototype apply
  if (!(Function.prototype.apply))
  {
    Function.prototype.apply = function(thisObject, argumentList)
    {
      var funcCall;
      var tempThisObjectKey;
      var result;
      
      var argumentListStringList = [];
      if (argumentList != null)
      {
        for (var i = 0; i < argumentList.length; i++)
        {
          argumentListStringList.push("argumentList" + "[" + i + "]");
        }
      }
      var argumentListString = argumentListStringList.join(", ");
      if (thisObject == null)
      {
        funcCall = this;
        try
        {
          eval("result = funcCall (" + argumentListString + ");");
        }
        catch(e)
        {
          throw new Exception(e);
        }
      }
      else
      {
        tempThisObjectKey = "_sys_function_call" + (new Date()).getTime();
        thisObject[tempThisObjectKey] = this;
        try
        {
          eval("result = thisObject[tempThisObjectKey] (" + argumentListString + ");");
          delete thisObject[tempThisObjectKey];
        }
        catch(e)
        {
          delete thisObject[tempThisObjectKey];
          throw new Exception(e);
        }
      }
      return result;
    };
    Function.prototype.apply._funcName = "Function#apply";
  }
  
  // function prototype call
  if (!(Function.prototype.call))
  {
    Function.prototype.call = function()
    {
      var thisObject = arguments[0];
      var argumentList = [];
      for (var i = 1; i < arguments.length; i++)
      {
        argumentList.push(arguments[i]);
      }
      return this.apply(thisObject, argumentList);
    };
    Function.prototype.call._funcName = "Function#call";
  }
  
  // function prototype bind
  if (!(Function.prototype.bind))
  {
    Function.prototype.bind = function(_this)
    {
      var func = this;
      var bindArguments = Array.prototype.slice.call(arguments, 1);
      var bindFunc =  function()
      {
        var callArguments = Array.prototype.slice.call(arguments);
        return func.apply(_this, bindArguments.concat(callArguments));
      };
      
      var caller = arguments.callee.caller;
      if ((caller != null) && (caller._funcName != null) && (func._funcName == null))
      {
        func._funcName = caller._funcName + "!anonymous()";
      }
      
      bindFunc._sourceFunction = func;
      if (func._funcName != null)
      {
        bindFunc._funcName = func._funcName;
      }
      
      return bindFunc;
    };
    Function.prototype.bind._funcName = "Function#bind";
  }
  
  // set system keywords' "toString"
  var _sys_func_list = ["Package", "Load", "Include", "Using", "Using.F", "Import", "Implement", "Extend",
                        "Interface", "Interface.IS", "Static", "Abstract", "Class", "Class.IS", "Class.forName",
                        "Abstract.Class", "Static.Class", "Exception", "Extends", "Implements", "InstanceOf"];
  for (var i = 0; i < _sys_func_list.length; i++)
  {
    _sys_setRetSysCoreFunc(_sys_func_list[i]);
  }
  
  // make system base class (cn.aprilsoft.jsapp.core.Object)
  Package("cn.aprilsoft.jsapp.core");
  cn.aprilsoft.jsapp.core.Object = _sys_baseClass;
  
  // clonealbe
  Interface("cn.aprilsoft.jsapp.core.Cloneable", Implement(),
  {
    clone: Abstract()
  });
  // destroyable
  Interface("cn.aprilsoft.jsapp.core.Destroyable", Implement(),
  {
    destroy: Abstract()
  });
  // closeable
  Interface("cn.aprilsoft.jsapp.core.Closeable", Implement(),
  {
    close: Abstract()
  });
  // comparable
  Interface("cn.aprilsoft.jsapp.core.Comparable", Implement(),
  {
    compare: Abstract( /* object: same class as this */ )
  });
})();

