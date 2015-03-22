
```
window.addEventListener("message", receiveMessage, false);

function receiveMessage(event)
{
  if (event.origin !== "http://example.org:8080")
    return;

  // ...
}
```

https://developer.mozilla.org/zh-CN/docs/Web/API/Window.postMessage