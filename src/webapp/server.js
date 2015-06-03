var express = require('express');
var	http = require('http');
var	nunjucks = require('nunjucks');

var app = express();

nunjucks.configure('views', {
    autoescape: true,
    express: app
});

app.get('/', function (req, res) {
	res.render('index.html');
})

app.get('/create', function (req, res) {
	res.render('create.html');
})


var server = http.createServer(app);

server.listen(3000, function () {

  var host = server.address().address
  var port = server.address().port

  console.log('Example app listening at http://%s:%s', host, port)

});
