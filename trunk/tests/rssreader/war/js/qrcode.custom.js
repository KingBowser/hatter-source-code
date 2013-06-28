var _countBits = function(_c) {
    var cnt = 0;
    while(_c > 0) {
        cnt++;
        _c = _c >>> 1;
    }
    return cnt;
};
function UnicodeToUtf8Bytes2(code) {
        if ((code == null) || (code < 0) ||
        (code > (Math.pow(2, 31) -1))) {
        return ["?".charCodeAt(0)];
    }
    if (code < 0x80) {
        return [code];
    }
    var arr = [];
    while ((code >>> 6) > 0) {
        arr.push(0x80 | (code & 0x3F));
        code = code >>> 6;
    }
    if ((arr.length + 2 + (_countBits(code))) > 8) {
        arr.push(0x80 | code);
        code = 0;
    }
    var pre = 0x80;
    for (var i = 0; i < arr.length; i++) {
      pre |= (0x80 >>> (i + 1));
    }
    arr.push(pre | code);
    return arr.reverse();
}

QR8bitByte.prototype.getLength = function(buffer) {
  var len = 0;
  for (var i = 0; i < this.data.length; i++) {
    var bytes = UnicodeToUtf8Bytes2(this.data.charCodeAt(i));
    len += bytes.length;
  }
  return len;
};

QR8bitByte.prototype.write = function(buffer) {
  for (var i = 0; i < this.data.length; i++) {
    var bytes = UnicodeToUtf8Bytes2(this.data.charCodeAt(i));
    for (var x = 0; x < bytes.length; x++) {
      buffer.put(bytes[x], 8);
    }
  }
};
