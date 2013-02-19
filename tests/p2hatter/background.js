// Called when the user clicks on the browser action icon.
chrome.browserAction.onClicked.addListener(function(tab) {
  if (/.*hatter.me.*/.test(tab.url + "")) {
    return;
  }
  if (!/https?:.*/.test(tab.url + "")) {
    return;
  }
  chrome.tabs.update(tab.id, {'url': "https://hatter.me/p?url=" + encodeURIComponent(tab.url)});
});

