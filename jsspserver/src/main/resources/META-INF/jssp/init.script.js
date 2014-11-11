Array.prototype.each = function(func/*(obj, index)*/) {
  for (var i = 0; i < this.length; i++) {
    func(this[i], i);
  }
};

if (!Array.prototype.forEach)  {
  Array.prototype.forEach = Array.prototype.each;
}

$EQUALS = function(o1, o2) {
  if (o1 == null) { return o2 == null; }
  return o1.equals(o2);
};

$STR_EQUALS = function(s1, s2) {
  if (s1 == null) { return s2 == null; }
  return $EQUALS(s1 + "", s2 + "");
};

$EACH = function(obj, func/*obj, index, size*/) {
  if (obj == null) { return; }
  if (obj instanceof Array) { obj.each(func); return; }
  if (obj instanceof java.util.List) {
    var _size = obj.size();
    for (var i = 0; i < _size; i++) {
      func(obj.get(i), i, _size);
    }
    return;
  }
  if (obj instanceof java.lang.Iterable) {
    var itr = obj.iterator();
    var i = 0;
    while(itr.hasNext()) {
      var o = itr.next();
      func(o, i, -1);
    }
    return;
  }
  if (obj instanceof java.util.Iterator) {
    var itr = obj;
    var i = 0;
    while(itr.hasNext()) {
      var o = itr.next();
      func(o, i, -1);
    }
    return;
  }
  // all not match?
  func(obj, 0);
};

$ARRAY = function(obj) {
  var a = [];
  $EACH(obj, function(o, i) { a.push(o); });
  return a;
};

$MAP_EACH = function(map, func/*key, value*/) {
  if (map == null) { return; }
  if (map instanceof java.util.Map) {
    $EACH(map.entrySet(), function(o, i) {
      func(o.getKey(), o.getValue());
    });
  }
};

$BOOL = function(obj) {
  if (obj == null) { return false; }
  if (obj instanceof java.lang.Boolean) {
    obj = obj.booleanValue();
  }
  if (obj instanceof java.lang.Integer) {
    obj = obj.intValue();
  }
  if (obj instanceof java.lang.Long) {
    obj = obj.longValue();
  }
  return (!!obj);
};

$INT = function(obj) {
  if (obj == null) { return 0; }
  if (obj instanceof java.lang.Integer) {
    return obj.intValue();
  }
  if (obj instanceof java.lang.Number) {
    return obj.intValue();
  }
  return parseInt(obj);
};

$STR = function(obj) {
  if (obj == null) { return null; }
  return ("" + obj);
};

$STR_EMPTY = function(obj) {
  if (obj == null) { return true; }
  if (obj instanceof java.lang.String) {
    return (obj.length() == 0);
  }
  return ("" + obj).length == 0;
};

$STR_BLANK = function(obj) {
  if ($STR_EMPTY(obj)) { return true; }
  return /^\s+$/.test("" + obj);
};
