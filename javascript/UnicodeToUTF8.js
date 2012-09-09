
function UnicodeToUtf8Bytes(code) {
  var bits = code.toString(2)
  if (bits.length < 8) {
    return [code];
  }
  if (bits.length > 31) {
    return [];
  }
  var arr = [];
  var doBits = bits;
  while (doBits.length > 6) {
    var sixbits = doBits.substring(doBits.length - 6, doBits.length);
    doBits = doBits.substring(0, doBits.length - 6);
    arr.push(parseInt("10" + sixbits, 2));
  }
  if (arr.length + 2 + doBits.length > 8) {
    arr.push(parseInt("10" + "00000000".substring(0, 8 - 2 - doBits.length) + doBits, 2));
    doBits = "";
  }
  var pre1 = "11111111".substring(0, arr.length + 1) + "0";
  arr.push(parseInt(pre1 + "00000000".substring(0, 8 - pre1.length - doBits.length) + doBits, 2));
  return arr.reverse();
}

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
