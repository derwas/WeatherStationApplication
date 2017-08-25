//display thermometer
function plotThermometer(dataList) {
  var width = 180,
      height = 380,
      maxTemp = 0,
      minTemp = 0,
      currentTemp = dataList[5];

  var bottomY = height - 5,
      topY = 5,
      bulbRadius = 20,
      tubeWidth = 21.5,
      tubeBorderWidth = 1,
      mercuryColor = "#FF0000", 
      innerBulbColor = "#FFFFFF"
      tubeBorderColor = "#000000";

  var bulb_cy = bottomY - bulbRadius,
      bulb_cx = width/2,
      top_cy = topY + tubeWidth/2;


  var svg = d3.select("#thermometer")
    .append("svg")
    .attr("width", width)
    .attr("height", height);


  var defs = svg.append("defs");

  // Define the radial gradient for the bulb fill colour
  var bulbGradient = defs.append("radialGradient")
    .attr("id", "bulbGradient")
    .attr("cx", "50%")
    .attr("cy", "50%")
    .attr("r", "50%")
    .attr("fx", "50%")
    .attr("fy", "50%");

  bulbGradient.append("stop")
    .attr("offset", "0%")
    .style("stop-color", innerBulbColor);

  bulbGradient.append("stop")
    .attr("offset", "90%")
    .style("stop-color", mercuryColor);




  // Circle element for rounded tube top
  svg.append("circle")
    .attr("r", tubeWidth/2)
    .attr("cx", width/2)
    .attr("cy", top_cy)
    .style("fill", "#FFFFFF")
    .style("stroke", tubeBorderColor)
    .style("stroke-width", tubeBorderWidth + "px");


  // Rect element for tube
  svg.append("rect")
    .attr("x", width/2 - tubeWidth/2)
    .attr("y", top_cy)
    .attr("height", bulb_cy - top_cy)
    .attr("width", tubeWidth)
    .style("shape-rendering", "crispEdges")
    .style("fill", "#FFFFFF")
    .style("stroke", tubeBorderColor)
    .style("stroke-width", tubeBorderWidth + "px");


  // White fill for rounded tube top circle element
  // to hide the border at the top of the tube rect element
  svg.append("circle")
    .attr("r", tubeWidth/2 - tubeBorderWidth/2)
    .attr("cx", width/2)
    .attr("cy", top_cy)
    .style("fill", "#FFFFFF")
    .style("stroke", "none")



  // Main bulb of thermometermeter (empty), white fill
  svg.append("circle")
    .attr("r", bulbRadius)
    .attr("cx", bulb_cx)
    .attr("cy", bulb_cy)
    .style("fill", "#FFFFFF")
    .style("stroke", tubeBorderColor)
    .style("stroke-width", tubeBorderWidth + "px");


  // Rect element for tube fill colour
  svg.append("rect")
    .attr("x", width/2 - (tubeWidth - tubeBorderWidth)/2)
    .attr("y", top_cy)
    .attr("height", bulb_cy - top_cy)
    .attr("width", tubeWidth - tubeBorderWidth)
    .style("shape-rendering", "crispEdges")
    .style("fill", "#FFFFFF")
    .style("stroke", "none");


  // Scale step size
  var step = 5;

  // Determine a suitable range of the temperature scale
  var domain = [-20, 45];


  // D3 scale object
  var scale = d3.scale.linear()
    .range([bulb_cy - bulbRadius/2 - 8.5, top_cy])
    .domain(domain);


  var tubeFill_bottom = bulb_cy,
      tubeFill_top = scale(currentTemp);

  // Rect element for the red mercury column
  svg.append("rect")
    .attr("x", width/2 - (tubeWidth - 10)/2)
    .attr("y", tubeFill_top)
    .attr("width", tubeWidth - 10)
    .attr("height", tubeFill_bottom - tubeFill_top)
    .style("shape-rendering", "crispEdges")
    .style("fill", mercuryColor)


  // Main thermometermeter bulb fill
  svg.append("circle")
    .attr("r", bulbRadius - 6)
    .attr("cx", bulb_cx)
    .attr("cy", bulb_cy)
    .style("fill", "url(#bulbGradient)")
    .style("stroke", mercuryColor)
    .style("stroke-width", "2px");


  // Values to use along the scale ticks up the thermometermeter
  var tickValues = d3.range((domain[1] - domain[0])/5).map(function(v) { return domain[0] + v * 5; });


  // D3 axis object for the temperature scale
  var axis = d3.svg.axis()
    .scale(scale)
    .innerTickSize(7)
    .outerTickSize(0)
    .tickValues(tickValues)
    .orient("left");

  // Add the axis to the image
  var svgAxis = svg.append("g")
    .attr("id", "tempScale")
    .attr("transform", "translate(" + (width/2 - tubeWidth/2) + ",0)")
    .call(axis);

  // Format text labels
  svgAxis.selectAll(".tick text")
      .style("fill", "#777777")
      .style("font-size", "10px");

  // Set main axis line to no stroke or fill
  svgAxis.select("path")
    .style("stroke", "none")
    .style("fill", "none")

  // Set the style of the ticks 
  svgAxis.selectAll(".tick line")
    .style("stroke", tubeBorderColor)
    .style("shape-rendering", "crispEdges")
    .style("stroke-width", "1px");
}

