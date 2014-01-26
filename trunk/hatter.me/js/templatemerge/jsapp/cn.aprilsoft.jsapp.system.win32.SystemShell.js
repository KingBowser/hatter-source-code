/*
 * cn.aprilsoft.jsapp.system.win32.SystemShell.js
 * jsapp, system shell functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system.win32
  Package("cn.aprilsoft.jsapp.system.win32");

  Class("cn.aprilsoft.jsapp.system.win32.SystemShell", Extend(), Implement(),
  {
    _getShellApp: Static(function()
    {
      var shellApp = new ActiveXObject("Shell.Application");
      return shellApp;
    }),
    
    cascadeWindows: Static(function()
    {
      ThisClass()._getShellApp().CascadeWindows();
    }),
    
    tileHorizontally: Static(function()
    {
      ThisClass()._getShellApp().TileHorizontally();
    }),
    
    tileVertically: Static(function()
    {
      ThisClass()._getShellApp().TileVertically();
    }),
    
    controlPanelItem: Static(function(pName)
    {
      // param like: sysdm.cpl
      ThisClass()._getShellApp().ControlPanelItem(pName);
    }),
    
    ejectPC: Static(function()
    {
      ThisClass()._getShellApp().EjectPC();
    }),
    
    explore: Static(function(dir)
    {
      ThisClass()._getShellApp().Explore(dir);
    }),
    
    open: Static(function(dir)
    {
      ThisClass()._getShellApp().Open(dir);
    }),
    
    fileRun: Static(function()
    {
      ThisClass()._getShellApp().FileRun();
    }),
    
    findComputer: Static(function()
    {
      ThisClass()._getShellApp().FindComputer();
    }),
    
    findFiles: Static(function()
    {
      ThisClass()._getShellApp().FindFiles();
    }),
    
    help: Static(function()
    {
      ThisClass()._getShellApp().Help();
    }),
    
    minimizeAll: Static(function()
    {
      ThisClass()._getShellApp().MinimizeAll();
    }),
    
    undoMinimizeALL: Static(function()
    {
      ThisClass()._getShellApp().UndoMinimizeALL();
    }),
    
    refreshMenu: Static(function()
    {
      ThisClass()._getShellApp().RefreshMenu();
    }),
    
    setTime: Static(function()
    {
      ThisClass()._getShellApp().SetTime();
    }),
    
    trayProperties: Static(function()
    {
      ThisClass()._getShellApp().TrayProperties();
    }),
    
    shutdownWindows: Static(function()
    {
      ThisClass()._getShellApp().ShutdownWindows();
    }),
    
    suspend: Static(function()
    {
      ThisClass()._getShellApp().Suspend();
    }),
    
    shellExecute: Static(function(cmd)
    {
      ThisClass()._getShellApp().ShellExecute(cmd);
    }),
    
    windows: Static(function()
    {
      return ThisClass()._getShellApp().Windows();
    }),
    
    nameSpace: Static(function(vDir)
    {
      return ThisClass()._getShellApp().NameSpace(vDir);
    }),
    
    browseForFolder: Static(function(hwnd, sTitle, iOptions, vRootFolder)
    {
      return ThisClass()._getShellApp().BrowseForFolder(hwnd, sTitle, iOptions, vRootFolder);
    })
  });
})();

