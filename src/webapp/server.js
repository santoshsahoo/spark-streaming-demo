var express = require('express');
var http = require('http');
var nunjucks = require('nunjucks');
var kafka = require('kafka-node'),
  Consumer = kafka.Consumer,
  client = new kafka.Client('localhost:2181/expensekafka');

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
  req.socket.setTimeout(Infinity);
  if (req.headers.accept && req.headers.accept == 'text/event-stream') {
    res.writeHead(200, {
      'Content-Type': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive'
    });

    var id = (new Date()).toLocaleTimeString();

    var consumer = new Consumer(client, [{
      topic: 'expense.counts',
      partition: 0,
      offset: -1,
      fromOffset: true
    }], {
      autoCommit: false
    });

    consumer.on('error', function(err) {
      consumer.close();
      console.log(err);
    })

    console.log("New SSE connected");

    consumer.on('message', function(message) {
      var high = 5000, low = 0, rand = Math.random() * (high - low) + low;
      var num = parseInt(message.value) || 0;;
      sendSSE(res, id, num);

      console.log(message);
    });

    // var high = 5000, low = 0, rand = Math.random() * (high - low) + low;
    // setInterval(function() {
    //   sendSSE(res, id, rand);
    // }, 2000);

    sendSSE(res, id, 0);
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
