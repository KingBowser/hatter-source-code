/*
 * cn.aprilsoft.jsapp.common.FileUtil.js
 * jsapp, Windows util functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  var StringUtil = Using("cn.aprilsoft.jsapp.common.StringUtil");

  Class("cn.aprilsoft.jsapp.common.FileUtil", Extend(), Implement(),
  {
    copyFile: Static(function(fileSrc, fileDest)
    {
      var fso = new ActiveXObject("Scripting.FileSystemObject");
      fso.CopyFile(fileSrc, fileDest);
    }),
    
    isFileExists: Static(function(fileName)
    {
      var fso = new ActiveXObject("Scripting.FileSystemObject");
      return fso.FileExists(fileName);
    }),
    
    isFolderExists: Static(function(folerName)
    {
      var fso = new ActiveXObject("Scripting.FileSystemObject");
      return fso.FolderExists(folerName);
    }),

    getDesktopPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("Desktop");
    }),
    
    getAllUsersDesktopPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("AllUsersDesktop");
    }),
    
    getAllUsersStartMenuPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("AllUsersStartMenu");
    }),
    
    getAllUsersProgramsPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("AllUsersPrograms");
    }),
    
    getAllUsersStartupPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("AllUsersStartup");
    }),
    
    getFavoritesPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("Favorites");
    }),
    
    getFontsPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("Fonts");
    }),
    
    getMyDocumentsPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("MyDocuments");
    }),
    
    getNetHoodPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("NetHood");
    }),
    
    getPrintHoodPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("PrintHood");
    }),
    
    getProgramsPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("Programs");
    }),
    
    getRecentPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("Recent");
    }),
    
    getSendToPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("SendTo");
    }),
    
    getStartMenuPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("StartMenu");
    }),
    
    getStartupPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("Startup");
    }),
    
    getTemplatesPath: Static(function()
    {
      var shell = new ActiveXObject("WScript.Shell");
      return shell.SpecialFolders("Templates");
    }),
    
    openFile: Static(function(fileName)
    {
      var shell = new ActiveXObject("WScript.Shell");
      shell.Run("\"" + fileName + "\"");
    }),
    
    createShortCut: Static(function(path, filename, url)
    {
      if (path == null)
      {
        path = "";
      }
      if ((!(StringUtil.endWith(path, "\\"))) && (!(StringUtil.endWith(path, "/"))))
      {
        path += "\\";
      }
      if (!(StringUtil.endWith(filename, ".url")))
      {
        filename += ".url";
      }
      if (!(StringUtil.startWith(url, "http://")))
      {
        url = "http://" + url;
      }
      var shell = new ActiveXObject("WScript.Shell");
      var desktopPath = shell.SpecialFolders("Desktop");
      if (path == "\\")
      {
        path = desktopPath;
      }
      var urlShortCut = shell.CreateShortcut(path + "\\" + filename);
      urlShortCut.TargetPath = url;
      urlShortCut.Save();
    }),
    
    getShortCutTarget: Static(function(filepathname)
    {
      // test failed
      var shell = new ActiveXObject("WScript.Shell");
      var shortCut = shell.CreateShortcut(filepathname);
      return shortCut.TargetPath;
    }),
    
    getFileName: Static(function(filename)
    {
      var getfilename = "";
      if (filename != null)
      {
        var filenameseplist = filename.split(/[\\\/]/);
        getfilename = filenameseplist[filenameseplist.length - 1];
      }
      return getfilename;
    }),
    
    listFiles: Static(function(path)
    {
      try
      {
        var fso, f, fc;
        var filelist = new Array();
        fso = new ActiveXObject("Scripting.FileSystemObject");
        f = fso.GetFolder(path);
        fc = new Enumerator(f.files);
        for (; !fc.atEnd(); fc.moveNext())
        {
            filelist.push(fc.item());
        }
        return filelist;
      }
      catch(e)
      {
        throw new Exception(e, "list files in folder:`" + path + "' error.");
      }
    }),
    
    listFolders: Static(function(path)
    {
      try
      {
        var fso, f, fc;
        var filelist = new Array();
        fso = new ActiveXObject("Scripting.FileSystemObject");
        f = fso.GetFolder(path);
        fc = new Enumerator(f.SubFolders);
        s = "";
        for (;!fc.atEnd(); fc.moveNext())
        {
            filelist.push(fc.item());
        }
        return filelist;
      }
      catch(e)
      {
        throw new Exception(e, "list folders in folder:`" + path + "' error.");
      }
    }),
    
    readTxtFile: Static(function(filename)
    {
      try
      {
        var fso, f, r;
        var ForReading = 1, ForWriting = 2;
        fso = new ActiveXObject("Scripting.FileSystemObject");
        f = fso.OpenTextFile(filename, ForReading);
        r = f.ReadAll();
        f.Close();
        return r;
      }
      catch(e)
      {
        try{f.Close();}catch(e){}
        throw new Exception(e, "read file :`" + filename + "' failed.");
      }
    }),
    
    writeTxtFile: Static(function(filename, text)
    {
      try
      {
        var fso, f, r;
        var ForReading = 1, ForWriting = 2;
        fso = new ActiveXObject("Scripting.FileSystemObject");
        f = fso.OpenTextFile(filename, ForWriting, true);
        f.Write(text);
        f.Close();
      }
      catch(e)
      {
        try{f.Close();}catch(e){}
        throw new Exception(e, "write file :`" + filename + "' failed.");
      }
    }),
    
    getCurrentPath: Static(function()
    {
      var fullPath = window.location.pathname;
      return fullPath.substring(0, fullPath.lastIndexOf("\\"));
    }),
    
    combinePath: Static(function(path1, path2)
    {
      if (StringUtil.endsWith(path1, "\\")
        || StringUtil.endsWith(path1, "\/"))
      {
        return path1 + path2;
      }
      else
      {
        if (StringUtil.contains(path1, "\\"))
        {
          return path1 + "\\" + path2;
        }
        else
        {
          return path1 + "\/" + path2;
        }
      }
    })
  });
})();


// add by hatter jiang
// readme of filesystemobject
/*
The FileSystemObject object's properties and methods are described below:
Properties
Property 	Description
Drives 	Returns a collection of all Drive objects on the computer
Methods
Method 	Description
BuildPath 	Appends a name to an existing path
CopyFile 	Copies one or more files from one location to another
CopyFolder 	Copies one or more folders from one location to another
CreateFolder 	Creates a new folder
CreateTextFile 	Creates a text file and returns a TextStream object that can be used to read from, or write to the file
DeleteFile 	Deletes one or more specified files
DeleteFolder 	Deletes one or more specified folders
DriveExists 	Checks if a specified drive exists
FileExists 	Checks if a specified file exists
FolderExists 	Checks if a specified folder exists
GetAbsolutePathName 	Returns the complete path from the root of the drive for the specified path
GetBaseName 	Returns the base name of a specified file or folder
GetDrive 	Returns a Drive object corresponding to the drive in a specified path
GetDriveName 	Returns the drive name of a specified path
GetExtensionName 	Returns the file extension name for the last component in a specified path
GetFile 	Returns a File object for a specified path
GetFileName 	Returns the file name or folder name for the last component in a specified path
GetFolder 	Returns a Folder object for a specified path
GetParentFolderName 	Returns the name of the parent folder of the last component in a specified path
GetSpecialFolder 	Returns the path to some of Windows' special folders
GetTempName 	Returns a randomly generated temporary file or folder
MoveFile 	Moves one or more files from one location to another
MoveFolder 	Moves one or more folders from one location to another
OpenTextFile 	Opens a file and returns a TextStream object that can be used to access the file
*/

