/*
 * cn.aprilsoft.jsapp.io.FileLister.js
 * jsapp, io file functions
 * 
 * Copyright(C) Hatter Jiang
 */
(function()
{
  // New package: cn.aprilsoft.jsapp.io
  Package("cn.aprilsoft.jsapp.io");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");
  var Thread = Using("cn.aprilsoft.jsapp.system.Thread");
  var File = Using("cn.aprilsoft.jsapp.io.File");
  var FileFilter = Using("cn.aprilsoft.jsapp.io.FileFilter");

  Class("cn.aprilsoft.jsapp.io.FileLister", Extend(), Implement(),
  {
    _recursive: null /* boolean */,
    _fileFilter: null /* instance of FileFilter */,
    _listedFolder: null /* instance of File */,
    _processCallback: null /* function(totalCount, currentIndex) */,
    _onError: null /* function(exception) */,
    
    Constructor: function()
    {
    },
    
    list: function()
    {
      var listedList = [];
      var tobeListedList = [];
      
      tobeListedList.push(this._listedFolder);
      
      // list folder
      var thread = new Thread();
      thread.setTimeSlice(function(_this)
      {
        var fileOrFolder = tobeListedList.shift();
        if (fileOrFolder == null)
        {
          _this.end();
        }
      });
    }
  });
})();
