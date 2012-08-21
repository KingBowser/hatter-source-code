(function() {
var D = document, E = escape, U = unescape, N = null, Q = "=";
getcookie = function(k) {
  var c, v, i, t;
  if ((c = D.cookie) == N) { return N; }
  if ((v = c.split(";")) == N) { return N; }
  for (i = 0; i < v.length; i++) {
    if (v[i].indexOf(Q) < 0) { continue; }
    t = v[i].split(Q);
    if (U(t[0].replace(/\s/, "")) == k) { return U(t[1]); }
  }
  return N;
}

setcookie = function(k, v) {
  D.cookie = E(k) + Q + E(v) + "; expires=" + (new Date(2099, 12, 31)).toGMTString();
}
})();
