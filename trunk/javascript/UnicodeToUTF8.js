
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
  if (arr.length + 1 + doBits.length > 8) {
    arr.push(parseInt("10" + "00000000".substring(0, 8 - 2 - doBits.length) + doBits, 2));
    doBits = "";
  }
  var pre1 = "11111111".substring(0, arr.length + 1) + "0";
  arr.push(parseInt(pre1 + "00000000".substring(0, 8 - pre1.length - doBits.length) + doBits, 2));
  return arr.reverse();
}

