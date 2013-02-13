/*
 * cn.aprilsoft.jsapp.system.Thread.js
 * jsapp, Mutil thread functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.system
  Package("cn.aprilsoft.jsapp.system");

  Class("cn.aprilsoft.jsapp.system.Thread", Extend(), Implement(),
  {
    //all threads list
    _system_threads_list: Static([]),
    
    _get_first_priority: Static(function()
    {
      var Thread = cn.aprilsoft.jsapp.system.Thread;
      var threadslist = Thread._system_threads_list;
      if (threadslist.length < 1)
      {
        return -1;
      }
      var firstmaxpriorityindex = 0;
      for (var i = 1; i < threadslist.length; i++)
      {
        if (threadslist[i]._current_priority > threadslist[firstmaxpriorityindex]._current_priority)
        {
          firstmaxpriorityindex = i;
        }
      }
      return firstmaxpriorityindex;
    }),
    
    _system_threads_runner: Static(function()
    {
      var Thread = cn.aprilsoft.jsapp.system.Thread;
      var threadslist = Thread._system_threads_list;
      if  (
            (threadslist != null)
          &&
            (threadslist.length > 0)
          )
      {
        for (var i = 0; i < threadslist.length; i++)
        {
          threadslist[i]._do_count_time_slice(this);
        }
        var thradindex = Thread._get_first_priority();
        if (thradindex > -1)
        {
          threadslist[thradindex]._do_thread_slice();
        }
        setTimeout(Thread._system_threads_runner, 1);
      }
    }),
    
    //thread store object
    _thread_object: {},
    
    _default_priority: 100,
    
    _current_priority: 100,
    
    _thread_end_flag: true,
    
    _onstart: null,
    
    _onend: null,
    
    _ontimeslice: null,
    
    _onerror: null,
    
    _ontimeslicebeforecallback: null,
    
    _ontimeslicebaftercallback: null,
    
    Constructor: function()
    {
      // init
      this._thread_object = {};
      this.stack = [];
    },
    
    _do_count_time_slice: function()
    {
      this._current_priority++;
    },
    
    _do_thread_slice: function()
    {
      this._current_priority = this._default_priority;
      if (this._thread_end_flag)
      {
        //thread ended
        return;
      }
      //before slice
      if (this._ontimeslicebeforecallback != null)
      {
        try
        {
          this._ontimeslicebeforecallback();
        }
        catch(e)
        {
          if (this._onerror == null)
          {
            //donot throw exception here
            throw new Exception(e, "run thread start error");
          }
          else
          {
            try
            {
              this._onerror(e);
            }
            catch(e2)
            {
              throw new Exception(e2, "run thread start error");
            }
          }
        }
      }
      
      //slice
      if (this._ontimeslice != null)
      {
        try
        {
          this._ontimeslice();
        }
        catch(e)
        {
          if (this._onerror == null)
          {
            //donot throw exception here
            throw new Exception(e, "run thread start error");
          }
          else
          {
            try
            {
              this._onerror(e);
            }
            catch(e2)
            {
              throw new Exception(e2, "run thread start error");
            }
          }
        }
      }
      
      //after slice
      if (this._ontimeslicebaftercallback != null)
      {
        try
        {
          this._ontimeslicebaftercallback();
        }
        catch(e)
        {
          if (this._onerror == null)
          {
            //donot throw exception here
            throw new Exception(e, "run thread start error");
          }
          else
          {
            try
            {
              this._onerror(e);
            }
            catch(e2)
            {
              throw new Exception(e2, "run thread start error");
            }
          }
        }
      }
    },
    
    _get_thread_object_position: function()
    {
      var threadslist = cn.aprilsoft.jsapp.system.Thread._system_threads_list;
      for (var i = 0; i < threadslist.length; i++)
      {
        if (threadslist[i] == this)
        {
          return i;
        }
      }
      return -1;
    },
    
    //thread stack
    stack: [],
    
    setPriority: function(priority)
    {
      this._current_priority = priority;
      this._default_priority = priority;
    },
    
    getObject: function()
    {
      return this._thread_object;
    },
    
    setObject: function(obj)
    {
      this._thread_object = obj;
    },
    
    setStart: function(startfunc)
    {
      this._onstart = startfunc;
    },
    
    setEnd: function(endfunc)
    {
      this._onend = endfunc;
    },
    
    setTimeSlice: function(timeslicefunc)
    {
      this._ontimeslice = timeslicefunc;
    },
    
    setError: function(errorfunc)
    {
      this._onerror = errorfunc;
    },
    
    setTimeSliceBeforeCallBack: function(timeslicebeforecallbackfunc)
    {
      this._ontimeslicebeforecallback = timeslicebeforecallbackfunc;
    },
    
    setTimeSliceAfterCallBack: function(timesliceaftercallbackfunc)
    {
      this._ontimeslicebaftercallback = timesliceaftercallbackfunc;
    },
    
    start: function()
    {
      var doruner = false;
      var threadslist = cn.aprilsoft.jsapp.system.Thread._system_threads_list;
      if (threadslist.length == 0)
      {
        doruner = true;
      }
      if (this._get_thread_object_position() < 0)
      {
        threadslist.push(this);
      }
      this._thread_end_flag = false;
      if (this._onstart != null)
      {
        try
        {
          this._onstart();
        }
        catch(e)
        {
          if (this._onerror == null)
          {
            //donot throw exception here
            throw new Exception(e, "run thread start error");
          }
          else
          {
            try
            {
              this._onerror(e);
            }
            catch(e2)
            {
              throw new Exception(e2, "run thread start error");
            }
          }
        }
      }
      if (doruner)
      {
        cn.aprilsoft.jsapp.system.Thread._system_threads_runner();
      }
    },
    
    end: function()
    {
      var threadslist = cn.aprilsoft.jsapp.system.Thread._system_threads_list;
      var pos = this._get_thread_object_position();
      if (this._onend != null)
      {
        try
        {
          this._onend();
        }
        catch(e)
        {
          if (this._onerror == null)
          {
            //donot throw exception here
            throw new Exception(e, "run thread start error");
          }
          else
          {
            try
            {
              this._onerror(e);
            }
            catch(e2)
            {
              throw new Exception(e2, "run thread start error");
            }
          }
        }
      }
      this._thread_end_flag = true;
      if (pos >= 0)
      {
        threadslist.splice(pos, 1);
      }
    },
    
    pause: function()
    {
      this._thread_end_flag = true;
    },
    
    restart: function()
    {
      this._thread_end_flag = false;
    }
  });
})();

