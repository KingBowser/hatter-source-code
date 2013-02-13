/*
 * cn.aprilsoft.jsapp.collection.LinkedList.js
 * jsapp, linked list functions
 * 
 * Copyright(C) Hatter Jiang
 */
 
(function()
{
  // New package: cn.aprilsoft.jsapp.collection
  Package("cn.aprilsoft.jsapp.collection");
  
  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.collection.LinkedList", Extend(), Implement(),
  {
    _head: null,
    _tail: null,
    _size: 0,
    
    Constructor: function()
    {
      this._size = 0;
      System.invokeOn("arr", function(arr)
      {
        this.add.apply(this, arr);
      }, this);
      System.invokeOn(ThisClass(), function(linkedList)
      {
        this.add.apply(this, linkedList.toArray());
      }, this);
    },
    
    size: function()
    {
      return this._size;
    },
    
    add: function(/* arguments */)
    {
      for (var i = 0; i < arguments.length; i++)
      {
        this._addOne(arguments[i]);
      }
    },
    
    push: function(/* arguments */)
    {
      this.add.apply(this, arguments);
    },
    
    unshift: function(/* arguments */)
    {
      for (var i = 0; i < arguments.length; i++)
      {
        this._unshiftOne(arguments[i]);
      }
    },
    
    remove: function(index)
    {
      if (index < 0)
      {
        throw new Exception("Index cannot be less than 0.");
      }
      if (index >= this._size)
      {
        throw new Exception("Index cannot be more than " + (this._size - 1) + ".");
      }
      if (index == 0)
      {
        return this.shift();
      }
      if (index == (this._size - 1))
      {
        return this.pop();
      }
      var p = this._get(index);
      p.previous.next = p.next;
      p.next.previous = p.previous;
      this._size -= 1;
      
      return p.value;
    },
    
    get: function(index)
    {
      return this._get(index).value;
    },
    
    shift: function()
    {
      var firstEntry = this._head;
      if (firstEntry == null)
      {
        return null;
      }
      this._head = firstEntry.next;
      if (this._head == null)
      {
        this._tail = null;
      }
      else
      {
        this._head.previous = null;
      }
      this._size -= 1;
      
      return firstEntry.value;
    },
    
    pop: function()
    {
      var lastEntry = this._tail;
      if (lastEntry == null)
      {
        return null;
      }
      this._tail = lastEntry.previous;
      if (this._tail == null)
      {
        this._head = null;
      }
      else
      {
        this._tail.next = null;
      }
      this._size -= 1;
      
      return lastEntry.value;
    },
    
    toArray: function()
    {
      var result = [];
      var p = this._head;
      while (p != null)
      {
        result.push(p.value);
        p = p.next;
      }
      
      return result;
    },
    
    join: function(str)
    {
      return this.toArray().join(str);
    },
    
    clear: function()
    {
      this._head = null;
      this._tail = null;
      this._size = 0;
    },
    
    asString: function()
    {
      return "[" + this.toArray().join(", ") + "]";
    },
    
    _unshiftOne: function(item)
    {
      var newEntry = this._newEntry(item);
      if (this._head == null)
      {
        this._head = newEntry;
        this._tail = newEntry;
      }
      else
      {
        this._head.previous = newEntry;
        newEntry.next = this._head;
        this._head = newEntry;
      }
      this._size += 1;
    },
    
    _addOne: function(item)
    {
      var newEntry = this._newEntry(item);
      if (this._head == null)
      {
        this._head = newEntry;
        this._tail = newEntry;
      }
      else
      {
        this._tail.next = newEntry;
        newEntry.previous = this._tail;
        this._tail = newEntry;
      }
      this._size += 1;
    },
    
    _newEntry: function(value)
    {
      return {"value": value, "next": null, "previous": null};
    },
    
    _get: function(index)
    {
      if (index < 0)
      {
        throw new Exception("Index cannot be less than 0.");
      }
      if (index >= this._size)
      {
        throw new Exception("Index cannot be more than " + (this._size - 1) + ".");
      }
      if (index == 0)
      {
        return this._head;
      }
      if (index == (this._size - 1))
      {
        return this._tail;
      }
      var currentIndex = 0;
      var p = this._head;
      while (p != null)
      {
        if (currentIndex == index)
        {
          break;
        }
        currentIndex += 1;
        p = p.next;
      }
      return p;
    }
  });
})();
