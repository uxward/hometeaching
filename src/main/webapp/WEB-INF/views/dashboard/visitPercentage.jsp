<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<t:mainPage activeMenu="visitPercentage" pageTitle="Visit Percentage" pageHeader="Dashboard" pageSubheader="Visit Percentage">

	<style>
.d3 {
	font: 10px sans-serif;
}

.axis path,.axis line {
	fill: none;
	stroke: #000;
	shape-rendering: crispEdges;
}

.x.axis path {
	display: none;
}

.line {
	fill: none;
	stroke: steelblue;
	stroke-width: 1.5px;
}

circle {
	
}
</style>

	<div class="d3"></div>

	<script type="text/javascript">
		$(document).ready(function() {
			getDashboard();
		});

		function getDashboard() {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getVisitPercentage"/>',
				success : function(data) {
					setupD3(data);
				}
			});
		}

		function setupD3(dataset) {

			var parseDate = d3.time.format("%Y-%m").parse;

			var data = dataset.map(function(d) {
				return {
					date : parseDate(d.formattedDate),
					visitPercent : d.visitPercent,
					month : d.month,
					year : d.year,
					totalFamilies : d.totalFamilies
				};
			});

			var margin = {
				top : 20,
				right : 20,
				bottom : 30,
				left : 50
			}, width = 960 - margin.left - margin.right, height = 500 - margin.top - margin.bottom;

			var x = d3.time.scale().range([ 0, width ]);

			var y = d3.scale.linear().range([ height, 0 ]);

			var xAxis = d3.svg.axis().scale(x).orient("bottom").ticks(d3.time.months, 1);

			var yAxis = d3.svg.axis().scale(y).orient("left");

			var div = d3.select("body").append("div").attr("class", "tooltip").style("opacity", 0).style('min-width', '200px');

			var line = d3.svg.line().interpolate("basis").x(function(d) {
				return x(d.date);
			}).y(function(d) {
				return y(d.visitPercent);
			});

			var svg = d3.select(".d3").append("svg").attr("width", width + margin.left + margin.right).attr("height", height + margin.top + margin.bottom).append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

			x.domain(d3.extent(data, function(d) {
				return d.date;
			}));
			y.domain(d3.extent(data, function(d) {
				return d.visitPercent;
			}));

			svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + height + ")").call(xAxis);

			svg.append("g").attr("class", "y axis").call(yAxis);

			svg.append("path").datum(data).attr("class", "line").attr("d", line);

			var points = svg.selectAll(".point").data(data).enter().append("svg:circle").attr("fill", function(d, i) {
				return "black";
			}).attr("cx", function(d, i) {
				return x(d.date);
			}).attr("cy", function(d, i) {
				return y(d.visitPercent);
			}).attr("r", function(d, i) {
				return 10;
			}).attr('fill', function(d, i) {
				return d.visitPercent < .33 ? 'steelblue' : d.visitPercent < .66 ? 'steelblue' : 'steelblue';
			}).on('click', function(d) {
				getDetails(d, div, d3.event);
			});
		}

		function getDetails(d, div, event) {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getVisitPercentageDetails"/>',
				data : {
					'month' : d.month,
					'year' : d.year
				},
				success : function(data) {
					setupDetails(d, data, div, event);
				}
			});
		}

		function setupDetails(d3data, data, div, event) {
			console.log(d3data);
			var html = '';
			var totalFamilies = 0;
			var totalVisited = 0;
			for ( var i = 0; i < data.length; i++) {
				html += '<label><strong>' + data[i].familyStatus + '</strong>:  ' + data[i].totalVisited + ' of ' + data[i].totalFamilies + '  -  ' + getPercentage(data[i].percentVisited) + '</label><br/>';
				totalFamilies += data[i].totalFamilies;
				totalVisited += data[i].totalVisited;
			}

			html += '<label><strong>Total</strong>:  ' + totalVisited + ' of ' + totalFamilies + '  -  ' + getPercentage(d3data.visitPercent) + '</label><br/>';

			div.transition().duration(200).style('opacity', .9);
			div.html(html).style("left", (event.pageX + 25) + "px").style("top", (event.pageY - 50) + "px");
		}
	</script>
</t:mainPage>