(function() {
var
D = document, FN = Function,
N = null, F = false, T = true,
L = function(o) { return o.length; },
I = function(i, f) { return (i instanceof f); };

$t = function(t) { return D.getElementsByTagName(t); }
$n = function(n) { return D.getElementsByName(n); }
$i = function(i) { return D.getElementById(i); }
$ = function(i) {
  var c, n; (c = $i(i))? 1: n = $n(i);
  return c? c: ((L(n) > 0)? n[0]: N);
}
$l = function(l, f) {
  var a = [], u, i;
  u = f? f: function(g){ a.push(g); };
  for (i = 0; i < L(l); i++){ if (u(l[i]) === F) { return; }; };
  if (!f) { return a; }
}
$try = function(f) {
  f = (I(f, FN))? [f]: f;
  for (var i = 0; i < L(f); i++) { try { f[0](); return; } catch(e){} }
}
})();
