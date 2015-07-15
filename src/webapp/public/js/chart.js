var n = 40,
  random = d3.random.normal(3000, 1000);

function chart(domain, interpolation, container) {
  var data = d3.range(n).map(function() {
    return 0; //random();
  });

  var margin = {
      top: 6,
      right: 0,
      bottom: 6,
      left: 40
    },
    width = 960 - margin.right,
    height = 120 - margin.top - margin.bottom;

  var x = d3.scale.linear()
    .domain(domain)
    .range([0, width]);

  var y = d3.scale.linear()
    .domain([0, 10000])
    .range([height, 0]);

  var line = d3.svg.line()
    .interpolate(interpolation)
    .x(function(d, i) {
      return x(i);
    })
    .y(function(d, i) {
      return y(d);
    });

  var svg = d3.select(container).append("svg")
    .append("g")
    .attr("transform", "translate(50,20)");

  svg.append("defs").append("clipPath")
    .attr("id", "clip")
    .append("rect")
    .attr("width", width)
    .attr("height", height);

  svg.append("g")
    .attr("class", "y axis")
    .call(d3.svg.axis().scale(y).ticks(5).orient("left"));

  var path = svg.append("g")
    .attr("clip-path", "url(#clip)")
    .append("path")
    .datum(data)
    .attr("class", "line")
    .attr("d", line);

  var tick = function(val) {
    data.push(val);

    // redraw the line, and then slide it to the left
    path
      .attr("d", line)
      .attr("transform", null)
      .transition()
      .attr("transform", "translate(" + x(-1) + ")");

    // pop the old data point off the front
    data.shift();
  };

  // window.setInterval(function () {
  //   tick(0);
  // }, 15000);

  if (!!window.EventSource) {
    var source = new EventSource('/stream');
    source.addEventListener('message', function(e) {
      var val = parseInt(e.data) || 0
      tick(val);
    }, false);

    var source2 = new EventSource('/fraudalert');
    source2.addEventListener('message', function(e) {
      var val = e.data || 0
      notifyMe(val);
    }, false);
  } else {
    alert('No SSE for ya!');
  }

};
