var express = require('express');
var http = require('http');
var nunjucks = require('nunjucks');

var app = express();

nunjucks.configure('views', {
  autoescape: true,
  express: app
});

app.get('/', function(req, res) {
  res.render('index.html');
});

app.get('/create', function(req, res) {
  res.render('create.html');
});

app.get('/reports', function(req, res) {
  res.render('create.html');
})

app.get('/realtime', function(req, res) {
  res.render('realtime.html');
})

app.get('/stream', function(req, res) {
  if (req.headers.accept && req.headers.accept == 'text/event-stream') {
    res.writeHead(200, {
      'Content-Type': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive'
    });

    var id = (new Date()).toLocaleTimeString();

    setInterval(function() {
      sendSSE(res, id, (new Date()).toLocaleTimeString());
    }, 2000);

    sendSSE(res, id, (new Date()).toLocaleTimeString());

  } else {
    res.writeHead(500);
    res.end();
  }
})

function sendSSE(res, id, data) {
  res.write('id: ' + id + '\n');
  res.write("data: " + data + '\n\n');
}

var server = http.createServer(app);

server.listen(3000, function() {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port)
});
