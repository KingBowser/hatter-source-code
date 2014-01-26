/*
 * cn.aprilsoft.jsapp.system.win32.ListFolder.js
 * jsapp, list folder functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system.win32
  Package("cn.aprilsoft.jsapp.system.win32");
  
  var Debug = Using("cn.aprilsoft.jsapp.common.Debug");
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Thread = Using("cn.aprilsoft.jsapp.system.Thread");
  var ArrayUtil = Using("cn.aprilsoft.jsapp.common.ArrayUtil");
  var FileUtil = Using("cn.aprilsoft.jsapp.common.FileUtil");

  Class("cn.aprilsoft.jsapp.system.win32.ListFolder", Extend(), Implement(),
  {
    STEP_LIST_INIT: Static(0),
    
    STEP_LIST_FOLDER: Static(1),
    
    STEP_LIST_FILES: Static(2),
    
    STEP_LIST_FINISH: Static(3),
    
    STEP_LIST_ABORT: Static(4),
    
    _step: null,
    
    _filter: null,
    
    _progresscallback: null,
    
    _endcallback: null,
    
    _thread: null,
    
    Constructor: function(filter, progresscallback, endcallback)
    {
      this._step = ThisClass().STEP_LIST_INIT;
      this._filter = filter;
      this._progresscallback = progresscallback;
      this._endcallback = endcallback;
      this._thread = new Thread();
      
      this._thread.setError(function(ex)
      {
        // error occured when runing thread
        try
        {
          this.end();
        }
        catch(e)
        {
          Debug.alertError(e);
        }
        Debug.alertError(ex);
      });
    },
    
    start: function(folder, recursive)
    {
      var context =
      {
        folder: folder,
        recursive: recursive,
        pendingFolderList: [folder],
        folderList: [],
        fileList: [],
        filteredFileList: [],
        folderIndex: 0
      };
      this._thread.getObject().context = context;
      this._listFolder();
    },
    
    stop: function()
    {
      this._step = ThisClass().STEP_LIST_ABORT;
      this._thread.end();
    },
    
    _listFolder: function()
    {
      this._step = ThisClass().STEP_LIST_FOLDER;
      var thisListFolder = this;
      var listFolderSlice = function()
      {
        var endFlag = false;
        var context = this.getObject().context;
        if (!context.recursive)
        {
          context.folderList.push(context.pendingFolderList.pop());
          endFlag = true;
        }
        else
        {
          var folder = context.pendingFolderList.pop();
          // callback
          thisListFolder._progresscallback(thisListFolder._step, folder);
          
          var folderList = FileUtil.listFolders(folder);
          ArrayUtil.pushArray(context.pendingFolderList, folderList);
          context.folderList.push(folder);
          
          endFlag = (context.pendingFolderList.length == 0);
        }
        if (endFlag)
        {
          this.end();
          thisListFolder._listFile();
        }
      };
      this._thread.setTimeSlice(listFolderSlice);
      this._thread.start();
    },
    
    _listFile: function()
    {
      this._step = ThisClass().STEP_LIST_FILES;
      var thisListFolder = this;
      var listFileSlice = function()
      {
        var context = this.getObject().context;
        var i = context.folderIndex;
        if (i < context.folderList.length)
        {
          var folder = context.folderList[i];
          // callback
          thisListFolder._progresscallback(thisListFolder._step, folder, i, context.folderList.length);
          
          var fileList = FileUtil.listFiles(folder);
          var filteredFileList = ArrayUtil.match(fileList, (function(i, o)
          {
            return this._doFilter(o);
          }).bind(thisListFolder));
          ArrayUtil.pushArray(context.fileList, fileList);
          ArrayUtil.pushArray(context.filteredFileList, filteredFileList);
          
          context.folderIndex += 1;
        }
        else
        {
          this.end();
          thisListFolder._end();
        }
      };
      this._thread.setTimeSlice(listFileSlice);
      this._thread.start();
    },
    
    _end: function()
    {
      this._step = ThisClass().STEP_LIST_FINISH;
      this._endcallback();
    },
    
    _doFilter: function(filename)
    {
      if (this._filter == null)
      {
        return true;
      }
      if (System.isString(this._filter))
      {
        this._filter = this._makeFilter(this._filter);
      }
      if (System.isRegExp(this._filter))
      {
        return (filename.match(this._filter) != null);
      }
      if (System.isFunction(this._filter))
      {
        return this._filter(filename);
      }
      throw new Exception("Filter " + this._filter + " type error.");
    },
    
    _mamkeFilter: function(filter)
    {
      // ^ and $ remain
      var re = filter;
      re = re.replace(/\\/g, "\\\\");
      re = re.replace(/\./g, "\\.");
      re = re.replace(/\[/g, "\\[");
      re = re.replace(/\]/g, "\\]");
      re = re.replace(/\(/g, "\\(");
      re = re.replace(/\)/g, "\\)");
      re = re.replace(/\-/g, "\\-");
      re = re.replace(/\+/g, "\\+");
      re = re.replace(/\*/g, ".*");
      re = re.replace(/\?/g, ".");
      
      return new RegExp(re);
    }
  });
})();

