var express = require('express');
var http = require('http');
var nunjucks = require('nunjucks');
var redis = require('redis');

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

app.get('/fraud/:id', function(req, res) {
  var id = req.params.id;
  res.render('fraud.html', {id:id});
})

app.get('/stream', function(req, res) {

  var channelName = "reportCounts";
  req.socket.setTimeout(Infinity);

  if (req.headers.accept && req.headers.accept == 'text/event-stream') {
    res.writeHead(200, {
      'Content-Type': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive'
    });

    res.write('\n');

    var subscriber = redis.createClient();
    subscriber.subscribe(channelName);

    req.on("close", function() {
      subscriber.unsubscribe();
      subscriber.quit();
      console.log("SSE Disconnected");
    });

    subscriber.on("error", function(err) {
      subscriber.quit();
      console.log("Redis Error: " + err);
    });


    console.log("New SSE connected");

    var id = (new Date()).toLocaleTimeString();

    subscriber.on("message", function(channel, message) {
      var high = 5000,
        low = 0,
        rand = Math.random() * (high - low) + low;

      id = (new Date()).toLocaleTimeString();
      var num = parseInt(message) || 0;
      sendSSE(res, id, num);

      console.log(message);
    });

    sendSSE(res, id, 0);
  } else {
    res.writeHead(500);
    res.end();
  }
})


app.get('/fraudalert', function(req, res) {
  var channelName = "reportFraudAlert";

  req.socket.setTimeout(Infinity);

  if (req.headers.accept && req.headers.accept == 'text/event-stream') {
    res.writeHead(200, {
      'Content-Type': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive'
    });

    res.write('\n');

    var subscriber = redis.createClient();
    subscriber.subscribe(channelName);

    req.on("close", function() {
      subscriber.unsubscribe();
      subscriber.quit();
      console.log("SSE Disconnected");
    });

    subscriber.on("error", function(err) {
      subscriber.quit();
      console.log("Redis Error: " + err);
    });

    console.log("New SSE connected");
    var id = (new Date()).toLocaleTimeString();

    subscriber.on("message", function(channel, message) {
      id = (new Date()).toLocaleTimeString();
      sendSSE(res, id, message);
      console.log(message);
    });

  } else {
    res.writeHead(500);
    res.end();
  }
})

function sendSSE(res, id, data) {
  res.write('id: ' + id + '\n');
  res.write("data: " + data + '\n\n');
}

app.use(express.static('public'));

var server = http.createServer(app);

server.listen(3000, function() {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port)
});
