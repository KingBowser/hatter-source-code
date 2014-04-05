Array.prototype.each = function(func/*(obj, index)*/) {
  for (var i = 0; i < this.length; i++) {
    func(this[i], i);
  }
};