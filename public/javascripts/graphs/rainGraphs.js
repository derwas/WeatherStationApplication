// plots graphs for last 7 days

function plotGraphRainRaw7Days(mongoData) {
        // size configuration of the chart
    var margin = {top: 20, right: 30, bottom: 100, left: 20},
        margin2 = {top: 430, right: 30, bottom: 40, left: 20},
        width = 900 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom,
        height2 = 500 - margin2.top - margin2.bottom;

    var color = "#4682b4"


    // date format
    var parseDate = d3.time.format("%d-%m-%Y %H:%M");

    // x and y-Scale definition. domain is defined later
    var x = d3.time.scale().range([0, width]),
        x2 = d3.time.scale().range([0, width]), //because x scale is the same for both
        y = d3.scale.linear().range([height, 0]),
        y2 = d3.scale.linear().range([height2, 0]); // y scale differs because height of the brush area is not the same

    // x and y axis definition
    var format = d3.time.format("%d-%m %H:%M");
    var xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(format).innerTickSize(-width).outerTickSize(0).tickPadding(10); //main x-axis (focus area)
    var xAxis2 = d3.svg.axis().scale(x2).orient("bottom"); // x-axis of the bottom rectangle (context area)
    var yAxis = d3.svg.axis().scale(y).orient("left").innerTickSize(-width).outerTickSize(0).tickPadding(10); // y-axis for both context & focus

    // brush definition for the slider at the bottom (context area)
    var brush = d3.svg.brush()
        .x(x2)
        .on("brush", brushed); // brushed function called on brush event

    // element definition: 
    //line element for context part
    var line = d3.svg.line()
        .interpolate("monotone")
        .x(function(d) { return x(d.timestamp);})
        .y(function(d) { return y(d.value);});

    //line element for focus part
    var line2 = d3.svg.line()
        .interpolate("monotone")
        .x(function(d) { return x2(d.timestamp);})
        .y(function(d) { return y2(d.value);});



    // svg canvas definition
    var svg = d3.select("#graph7Days")
        .append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
        .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    // clippath for slider http://www.d3noob.org/2015/07/clipped-paths-in-d3js-aka-clippath.html
    svg.append("defs")
        .append("clipPath") 
            .attr("id", "clip")
        .append("rect")
            .attr("width", width)
            .attr("height", height); 

    // focus element definition (big area conaining main graph)
    var focus = svg.append("g")
        .attr("class", "focus")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    // context element definition (slider area)
    var context = svg.append("g")
        .attr("class", "context")
        .attr("transform", "translate(" + margin2.left + "," + margin2.top + ")");
    
    // parse the String dates to Date dates for every timestamp
    mongoData.forEach(function(d) {
        d.timestamp = new Date(d.timestamp);
        d.value = d.value;
    });

    // definition of domains
    x.domain(d3.extent(mongoData.map(function(d) { return d.timestamp; }))); //extent = min and max
    y.domain(d3.extent(mongoData.map(function(d) { return d.value; })));
    x2.domain(x.domain());
    y2.domain(y.domain());

/*    ====================================================================
      =     FOCUS PART (= the big area displaying the graph)             =   
      ====================================================================*/

    // draws line
    focus.append("path")
        .datum(mongoData)
        .attr("class", "line")
        .attr("stroke", color)
        .attr("d", line);

    // draws xAxis
    focus.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
        .append("text")
            .attr("y", -6)
            .attr("dy", ".25em")
            .attr("x", 660)
            .attr("dx", ".25em")
            .text("Precipitation evolution over time");

    // draws yAxis
    focus.append("g")
            .attr("class", "y axis")
            .call(yAxis)
        .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end")
            .text("Precipitations(mm)");


/*    ====================================================================
      =     CONTEXT PART (= brushing area at the bottom of the graph)    =   
      ====================================================================*/

    // draws line
    context.append("path")
        .datum(mongoData)
        .attr("class", "line")
        .attr("stroke", color)
        .attr("d", line2);

    //draws xAxis
    context.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height2 + ")")
        .call(xAxis2);

    //draws brush
    context.append("g")
        .attr("class", "x brush")
        .call(brush)
        .selectAll("rect")
            .attr("height", height2) // Make brush rects same height 


    //here make the line zoom on the brushed part
    function brushed() {
        x.domain(brush.empty() ? x2.domain() : brush.extent()); // If brush is empty then reset the x domain to default, if not then make it the brush extent 
        focus.select(".line").attr("d", line);
        focus.select(".x.axis").call(xAxis);
    };

}


// plots graphs for last day
/* sources: http://jsfiddle.net/n7joxbn6/4/
            http://bl.ocks.org/DStruths/9c042e3a6b66048b5bd4
            */
function plotGraphRainRaw1Day(mongoData) {
    // size configuration of the chart
    var margin = {top: 10, right: 130, bottom: 100, left: 20},
        margin2 = {top: 430, right: 130, bottom: 40, left: 20},
        width = 1000 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom,
        height2 = 500 - margin2.top - margin2.bottom;

    var color = "#4682b4"

    var newMongoData = []; // will host the new array with only the values of the actual day

    // we need to keep only the actual day's datas
    // first, get the latest timestamp and deduce the beginning of the day in timestamp value
    var date = new Date(mongoData[0]["timestamp"])
    date.setHours(0) //set the date to the beginning of the same day
    var newTime = date.getTime();

    // then, keep only the values greater than the timestamp obtained (which are in the same day)
    mongoData.forEach(function(d) {
        if(d.timestamp > newTime) { //still in the same day
            newMongoData.push({
                timestamp: d.timestamp,
                unit: d.unit,
                value: d.value
            });
        } //else do nothing because we don't need the other values
    });

    // date format
    var parseDate = d3.time.format("%d-%m-%Y %H:%M");

    // x and y-Scale definition. domain is defined later
    var x = d3.time.scale().range([0, width]),
        x2 = d3.time.scale().range([0, width]), //because x scale is the same for both
        y = d3.scale.linear().range([height, 0]),
        y2 = d3.scale.linear().range([height2, 0]); // y scale differs because height of the brush area is not the same


    var format = d3.time.format("%d-%m %H:%M");
    var xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(format).innerTickSize(-width).outerTickSize(0).tickPadding(10);
    var xAxis2 = d3.svg.axis().scale(x2).orient("bottom");
    var yAxis = d3.svg.axis().scale(y).orient("left").innerTickSize(-width).outerTickSize(0).tickPadding(10);

    var brush = d3.svg.brush()//for slider bar at the bottom
        .x(x2) 
        .on("brush", brushed);


    var line = d3.svg.line()
        .interpolate("monotone")
        .x(function(d) { return x(d.timestamp);})
        .y(function(d) { return y(d.value);});


    var line2 = d3.svg.line()
        .interpolate("monotone")
        .x(function(d) { return x2(d.timestamp);})
        .y(function(d) { return y2(d.value);});

/*    var area2 = d3.svg.area()
        .interpolate("monotone")
        .x(function(d) { return x2(d.timestamp);})
        .y0(height2)
        .y1(function(d) { return y2(d.value);});*/

    var svg = d3.select("#graphToday")
        .append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
        .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    svg.append("defs")
        .append("clipPath") 
            .attr("id", "clip")
        .append("rect")
            .attr("width", width)
            .attr("height", height); 

    var focus = svg.append("g")
        .attr("class", "focus")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var context = svg.append("g")
        .attr("class", "context")
        .attr("transform", "translate(" + margin2.left + "," + margin2.top + ")");


    newMongoData.forEach(function(d) {
        d.timestamp = new Date(d.timestamp);
        d.value = d.value;
    });

    x.domain(d3.extent(newMongoData.map(function(d) { return d.timestamp; })));
    y.domain(d3.extent(newMongoData.map(function(d) { return d.value; })));
    x2.domain(x.domain());
    y2.domain(y.domain());





/*    ====================================================================
      =     FOCUS PART (= the big area displaying the graph)             =   
      ====================================================================*/


    focus.append("path")
        .datum(newMongoData)
        .attr("class", "line")
        .attr("stroke", color)
        .attr("d", line);


    focus.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
        .append("text")
            .attr("y", -6)
            .attr("dy", ".25em")
            .attr("x", 660)
            .attr("dx", ".25em")
            .text("Precipitation evolution over time");

    focus.append("g")
            .attr("class", "y axis")
            .call(yAxis)
        .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end")
            .text("Precipitations(mm)");


/*    ====================================================================
      =     CONTEXT PART (= brushing area at the bottom of the graph)    =   
      ====================================================================*/

    context.append("path")
        .datum(newMongoData)
        .attr("class", "line")
        .attr("stroke", color)
        .attr("d", line2);

    context.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height2 + ")")
        .call(xAxis2);

    context.append("g")
        .attr("class", "x brush")
        .call(brush)
        .selectAll("rect")
            .attr("height", height2) // Make brush rects same height 


    //here make the line zoom on the brushed part
    function brushed() {
        x.domain(brush.empty() ? x2.domain() : brush.extent()); // If brush is empty then reset the x domain to default, if not then make it the brush extent 
        focus.select(".line").attr("d", line);
        focus.select(".x.axis").call(xAxis);
    };

// how to add other data serie:
/*    svg.append("svg:path")
        .attr("d", line(NAME_OF_THE_ARRAY_CONTAINING_DATA_WITH_FORMATED_DATES(important!))
        .attr("stroke", "red")
        .attr("fill", "none");*/
}


function plotGraphRainCustom(mongoData, periodType, month, year) {
/*    mongoData is a JS Object containing the fields timestamp (Number), min (Number), max (Number) and average (Number)
    PeriodType is a String and can be either "month", "Month", "year" or "Year". Any other value will lead to an error and the graph not working.
    month is a Number corresponding to the month's number (js style, january is 0, december is 11)
    year is a Number corresponding to the year*/


    //first, get the correct time period in timestamp
    var dateLowerLimit, dateUpperLimit;
    var year = parseInt(year);
    var graph = false;
    if(periodType == "month" || periodType == "Month") {
        console.log("data sent:")
        console.log(mongoData);
        console.log(periodType);
        console.log(month);
        console.log(year);
        //lower and upper limit for the date period
        dateLowerLimit = new Date(year, month, 0, 0, 0, 0, 0);

        // if month is december, increment year and move month to january. else, increment month
        if (month == 11) {
            year += 1;
            month = 0;
        } else {
            month += 1;
        }
        dateUpperLimit = new Date(year, month, 0, 0, 0, 0, 0);
    } else if (periodType == "year" || periodType == "Year") {
        console.log("data sent:")
        console.log(mongoData);
        console.log(periodType);
        console.log(month);
        console.log(year);
        dateLowerLimit = new Date(year, 0, 0, 0, 0, 0, 0);
        dateUpperLimit = new Date(year+1, 0, 0, 0, 0, 0, 0);
    }

    var timeLowerLimit = dateLowerLimit.getTime(); // transforms date into timestamp in milliseconds
    var timeUpperLimit = dateUpperLimit.getTime(); // transforms date into timestamp in milliseconds

    //now, check for data between this interval
    var newMongoData = []; // will host the new array with only the values within the interval

    mongoData.forEach(function(d) {
        if((d.timestamp >= timeLowerLimit) && (d.timestamp <= timeUpperLimit)) { 
            newMongoData.push({
                timestamp: d.timestamp,
                average: d.average,
                max: d.max,
                min: d.min
            });
        } // else do nothing because we don't need the other values
    });

    if (newMongoData.length == 0) {
        document.getElementById("graphCustom").innerHTML = "No data available for this interval"
    } else {

        //Now, plot the graph

        // size configuration
        var margin = {top: 10, right: 130, bottom: 100, left: 20},
            margin2 = {top: 430, right: 130, bottom: 40, left: 20},
            width = 900 - margin.left - margin.right,
            height = 500 - margin.top - margin.bottom,
            height2 = 500 - margin2.top - margin2.bottom;


        // date format
        var parseDate = d3.time.format("%d-%m-%Y %H:%M");

        // x and y-Scale definition. domain is defined later
        var x = d3.time.scale().range([0, width]),
            x2 = d3.time.scale().range([0, width]), //because x scale is the same for both
            y = d3.scale.linear().range([height, 0]),
            y2 = d3.scale.linear().range([height2, 0]); // y scale differs because height of the brush area is not the same

        var newMongoDataAv = [];
        var newMongoDataMi = [];
        var newMongoDataMa = [];

        newMongoData.forEach(function(d) {
            newMongoDataAv.push({
                timestamp: d.timestamp,
                value: d.average
            });

            newMongoDataMi.push({
                timestamp: d.timestamp,
                value: d.min
            });

            newMongoDataMa.push({
                timestamp: d.timestamp,
                value: d.max
            });
        });

        console.log(newMongoDataAv)
        console.log(newMongoDataMi)
        console.log(newMongoDataMa)

        var format = d3.time.format("%d-%m %H:%M");
        var xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(format).innerTickSize(-height).outerTickSize(0).tickPadding(10).ticks(6);
        var xAxis2 = d3.svg.axis().scale(x2).orient("bottom");
        var yAxis = d3.svg.axis().scale(y).orient("left").innerTickSize(-width).outerTickSize(0).tickPadding(10);

        var brush = d3.svg.brush()//for slider bar at the bottom
            .x(x2) 
            .on("brush", brushed);

        // line context area
        var line = d3.svg.line()
            .interpolate("monotone")
            .x(function(d) { return x(d.timestamp);})
            .y(function(d) { return y(d.value);});


        // line focus area
        var line2 = d3.svg.line()
            .interpolate("monotone")
            .x(function(d) { return x(d.timestamp);})
            .y(function(d) { return y(d.value);});


        var svg = d3.select("#graphCustom")
            .append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom);

        var inner = svg.append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        inner.append("defs")
            .append("clipPath") 
                .attr("id", "clip")
            .append("rect")
                .attr("width", width)
                .attr("height", height); 

        var focus = inner.append("g")
            .attr("class", "focus")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        var context = inner.append("g")
            .attr("class", "context")
            .attr("transform", "translate(" + margin2.left + "," + margin2.top + ")");


        newMongoData.forEach(function(d) {
            d.timestamp = new Date(d.timestamp);
            d.value = d.value;
        });

        x.domain(d3.extent(newMongoData.map(function(d) { return d.timestamp; })));
        y.domain([d3.min(newMongoData.map(function(d) { return d.min-1; })), d3.max(newMongoData.map(function(d) { return d.max+1; }))]);
        x2.domain(x.domain());
        y2.domain(y.domain());





    /*    ====================================================================
          =     FOCUS PART (= the big area displaying the graph)             =   
          ====================================================================*/


        focus.append("path")
            .datum(newMongoDataAv)
            .attr("class", "line")
            .attr("stroke", "#f48411")
            .attr("data-legend", "Averages")
            .attr("d", line);


        focus.append("path")
            .datum(newMongoDataMa)
            .attr("class", "line")
            .attr("stroke", "#db2020")
            .attr("data-legend", "Maxima")
            .attr("d", line);

        focus.append("path")
            .datum(newMongoDataMi)
            .attr("class", "line")
            .attr("stroke", "#0164ec")
            .attr("data-legend", "Minima")
            .attr("d", line);

        focus.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis)
            .append("text")
                .attr("y", -6)
                .attr("dy", ".25em")
                .attr("x", 600)
                .attr("dx", ".25em")
                .text("Precipitation evolution over time");

        focus.append("g")
                .attr("class", "y axis")
                .call(yAxis)
            .append("text")
                .attr("transform", "rotate(-90)")
                .attr("y", 6)
                .attr("dy", ".71em")
                .style("text-anchor", "end")
                .text("Precipitations(mm)");




    /*    ====================================================================
          =     CONTEXT PART (= brushing area at the bottom of the graph)    =   
          ====================================================================*/

          //does not display the way I want, so commented for now
/*        context.append("path")
            .datum(newMongoDataAv)
            .attr("class", "line")
            .attr("stroke", color)
            .attr("d", line2);

        context.append("path")
            .datum(newMongoDataMa)
            .attr("class", "line")
            .attr("stroke", color)
            .attr("d", line2);

        context.append("path")
            .datum(newMongoDataMi)
            .attr("class", "line")
            .attr("stroke", color)
            .attr("d", line2);
*/
        // adds x-axis in context area
        context.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height2 + ")")
            .call(xAxis2);

        // adds brush in context area
        context.append("g")
            .attr("class", "x brush")
            .call(brush)
            .selectAll("rect")
                .attr("height", height2) // Make brush rects same height 



    /*    ====================================================================
          =     LEGEND PART (uses d3-legend.js)                              =   
          ====================================================================*/
        
        var legend = svg.append("g")
            .attr("class","legend")
            .attr("transform","translate(820,50)")
            .style("font-size","12px")
            .call(d3.legend); // d3-legend.js


    /*    ====================================================================
          =     MOUSEOVER PART (uses d3-legend.js)                              =   
          ====================================================================*/


        //var mouseG = focus.append("g")
        //    .attr("class", "mouse-over-effects");

        //mouseG.append("path") // this is the black vertical line to follow mouse
        //    .attr("class", "mouse-line")
        //    .style("stroke", "black")
        //    .style("stroke-width", "1px")
        //    .style("opacity", "0");

        //var lines = document.getElementsByClassName('line'); //keep references to focus lines

        //var mousePerLine = mouseG.selectAll('.mouse-per-line')
        //    .data(newMongoDataAv)
        //    .enter()
        //    .append("g")
        //    .attr("class", "mouse-per-line");

        //mousePerLine.append("circle")
        //    .attr("r", 7)
        //    .style("stroke", "black")
        //    .style("fill", "none")
        //    .style("stroke-width", "1px")
        //    .style("opacity", "0");

        //mousePerLine.append("text")
        //    .attr("transform", "translate(10,3)");

        //mouseG.append('svg:rect') // append a rect to catch mouse movements on canvas
        //    .attr('width', width) // can't catch mouse events on a g element
        //    .attr('height', height)
        //    .attr('fill', 'none')
        //    .attr('pointer-events', 'all')
        //    .on('mouseout', function() { // on mouse out hide line, circles and text
        //       d3.select(".mouse-line")
        //           .style("opacity", "0");
        //        d3.selectAll(".mouse-per-line circle")
        //            .style("opacity", "0");
        //        d3.selectAll(".mouse-per-line text")
        //            .style("opacity", "0");
        //    })
        //   .on('mouseover', function() { // on mouse in show line, circles and text
        //        d3.select(".mouse-line")
        //            .style("opacity", "1");
        //        d3.selectAll(".mouse-per-line circle")
        //            .style("opacity", "1");
        //        d3.selectAll(".mouse-per-line text")
        //            .style("opacity", "1");
        //    })
        //    .on('mousemove', function() { // mouse moving over canvas
        //        var mouse = d3.mouse(this);
        //        d3.select(".mouse-line")
        //            .attr("d", function() {
        //                var d = "M" + mouse[0] + "," + height;
        //                d += " " + mouse[0] + "," + 0;
        //                return d;
        //            });

        //        d3.selectAll(".mouse-per-line")
        //            .attr("transform", function(d, i) {
        //                console.log("mouse[0]: ",mouse[0])
        //               console.log("mouse[1]: ",mouse[1])
        //                var xDate = x.invert(mouse[0]),
        //                bisect = d3.bisector(function(d) { return d.timestamp; }).right;
        //                idx = bisect(d.value, xDate);
            
        //                var beginning = 0,
        //                    end = lines[i].getTotalLength(),
        //                    target = null;

        //                while (true){
        //                    target = Math.floor((beginning + end) / 2);
        //                    pos = lines[i].getPointAtLength(target);
        //                    if ((target === end || target === beginning) && pos.x !== mouse[0]) {
        //                        break;
        //                    }
        //                    if (pos.x > mouse[0])      end = target;
        //                    else if (pos.x < mouse[0]) beginning = target;
        //                    else break; //position found
        //                }
                    
        //                d3.select(this).select('text')
        //                    .text(y.invert(pos.y).toFixed(2));
                      
        //                return "translate(" + mouse[0] + "," + pos.y +")";

        //            });
        //    });

        //here make the line zoom on the brushed part
        function brushed() {
            x.domain(brush.empty() ? x2.domain() : brush.extent()); // If brush is empty then reset the x domain to default, if not then make it the brush extent 
            focus.selectAll(".line").attr("d", line);
            focus.select(".x.axis").call(xAxis);
        };

    }

}