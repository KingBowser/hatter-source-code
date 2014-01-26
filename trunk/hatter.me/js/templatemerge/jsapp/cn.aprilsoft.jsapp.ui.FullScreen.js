/*
 * cn.aprilsoft.jsapp.ui.MessageBox.js
 * jsapp, ui fullscreen functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  Class("cn.aprilsoft.jsapp.ui.FullScreen", Extend(), Implement(),
  {
    launchDocument: Static(function() {
      ThisClass().launch(document.documentElement);
    }),
    
    launch: Static(function(element) {
      if (element.requestFullScreen) {
        element.requestFullScreen();
      } else if (element.mozRequestFullScreen) {
        element.mozRequestFullScreen();
      } else if (element.webkitRequestFullScreen) {
        element.webkitRequestFullScreen();
      }
    }),
    
    cancel: Static(function() {
      if (document.cancelFullScreen) {
        document.cancelFullScreen();
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      } else if (document.webkitCancelFullScreen) {
        document.webkitCancelFullScreen();
      }
    }),
    
    getFullScreenElement: Static(function() {
      return document.fullScreenElement || document.mozFullScreenElement || document.webkitFullScreenElement;
    }),
    
    isFullScreenEnabled: Static(function() {
      return document.fullScreenEnabled || document.mozScreenEnabled || document.webkitScreenEnabled;
    })
  });
})();

