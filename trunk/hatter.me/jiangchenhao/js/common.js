// <!-- TRACE CLICK START -->
(function() {
  var allA = document.getElementsByTagName("a");
  for (var i = 0; i < allA.length; i++) {
    var a = allA[i];
    if ((a.href != "") && (a.href != "#")) {
      a.onmousedown = (function(a) {
        return function() {
        	if (/.*\/redirect\?.*/.test(a.href)) {
        		return;
        	}
        	var oldHref = a.href;
          a.href = "//hatter.me/redirect?url=" + encodeURIComponent(a.href);
          setTimeout(function() {
          	if (/.*\/redirect\?.*/.test(a.href)) {
          	  a.href = oldHref;
          	}
          }, 1000);
        };
      })(a);
    }
  }
})();
// <!-- TRACE CLICK ENDED -->

// <!-- COMMENT SCRIPT START -->
(function() {
	var dsq = document.createElement('script');
	dsq.type = 'text/javascript';
	dsq.async = true;
	dsq.src = ('https:' == document.location.protocol ? "/p?url=": "") + 'http://hatterjiangswebsite.disqus.com/embed.js';
    (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
})();
// <!-- COMMENT SCRIPT ENDED -->

// <!-- TRACE START -->
var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-372687-1']);
_gaq.push(['_setDomainName', 'aprilsoft.cn']);
_gaq.push(['_setAllowLinker', true]);
_gaq.push(['_trackPageview']);
(function() {
  var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
  ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();
// <!-- TRACE ENDED -->


