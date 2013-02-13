/*
 * cn.aprilsoft.jsapp.ui.Drag.js
 * jsapp, system drag functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.ui
  Package("cn.aprilsoft.jsapp.ui");

  var System = Using("cn.aprilsoft.jsapp.system.System");
  var JSEvent = Using("cn.aprilsoft.jsapp.ui.JSEvent");

  Class("cn.aprilsoft.jsapp.ui.Drag", Extend(), Implement(),
  {
    _target: null,
    
    _click: null;
    
    Constructor: function(target, click)
    {
      click = (click == null)? target: click;
      this._target = System.getElement(target);
      this._click = System.getElement(click);
      
      this._target._dragState.dragStart = false;
      this._target._dragState = {};
      dragState = this._target._dragState;
      
      this._click.onmousedown = function()
      {
        dragState = {};
        dragState.dragStart = true;
      };
      
      this._click.onmouseup = function()
      {
        dragState.dragStart = false;
      };
      
      this._click.onmousemove = function()
      {
        if (dragState.dragStart)
        {
          var event = new JSEvent();
          this.targetobj.style.left=this.offsetx+evtobj.clientX-this.x+"px"
          this.targetobj.style.top=this.offsety+evtobj.clientY-this.y+"px"
        }
      };
    },
    
    drag: function()
    {
      this._target._dragAble = true;
    },
    
    undrag: function()
    {
      this._target._dragAble = false;
    }
  });
})();