Array.prototype.each = function(func/*(obj, index)*/) {
  for (var i = 0; i < this.length; i++) {
    func(this[i], i);
  }
};

$EACH = function(obj, func/*obj, index*/) {
  if (obj == null) { return; }
  if (obj instanceof Array) { obj.each(func); return; }
  if (obj instanceof java.util.List) {
    for (var i = 0; i < obj.size(); i++) {
      func(obj.get(i), i);
    }
    return;
  }
  if (obj instanceof java.lang.Iterable) {
    var itr = obj.iterator();
    var i = 0;
    while(itr.hasNext()) {
      var o = itr.next();
      func(o, i);
    }
    return;
  }
  if (obj instanceof java.util.Iterator) {
    var itr = obj;
    var i = 0;
    while(itr.hasNext()) {
      var o = itr.next();
      func(o, i);
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
