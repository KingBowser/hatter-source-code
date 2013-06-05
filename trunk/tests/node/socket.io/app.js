var app = require('http').createServer(handler)
  , io = require('socket.io').listen(app)
  , fs = require('fs');

app.listen(8000);

function handler(req, res) {

  var u = req.url;

  if (u.indexOf("/cmd") == 0) {

    var cmd = u.substring("/cmd".length);

    if (cmd == "" || cmd =="/") {
      res.setHeader("Content-Type", "text/plain");
      res.writeHead(200);
      var outs = [];
      outs.push("Usage of cmd:");
      outs.push("\t/cmd\t for this page");
      outs.push("\t/cmd/client/count\t show client count");
      res.end(outs.join("\n"));
      return;
    }

    if (cmd == "/client/count") {
      res.writeHead(200);
      res.end("Count: 0");
      return;
    }

    // command error
    res.writeHead(500);
    res.end("Command format error!");
    return;
  }

  fs.readFile(__dirname + '/index.html',
  function (err, data) {
    if (err) {
      res.writeHead(500);
      return res.end('Error loading index.html');
    }

    res.writeHead(200);
    res.end(data);
  });
}

io.sockets.on('connection', function (socket) {
  socket.emit('news', { hello: 'world' });
  socket.on('my other event', function (data) {
    console.log(data);
  });
});
