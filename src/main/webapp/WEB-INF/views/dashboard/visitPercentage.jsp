<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<spring:url var="resources" value="/resources" />

<t:mainPage activeMenu="visitPercentage" pageTitle="Visit Percentage"
	pageHeader="Dashboard" pageSubheader="Visit Percentage">

	<style>
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
</style>

	<div class="d3"></div>


	<script src="${resources}/js/d3.min.js"></script>
	<script type="text/javascript">
		var svg, x, y, color;
		$(document).ready(function() {
			setupGraph();
			getDashboard();
		});

		function setupGraph() {
			margin = {
				top : 50,
				right : 200,
				bottom : 50,
				left : 50
			}, width = 1200 - margin.left - margin.right, height = 600 - margin.top - margin.bottom;

			svg = d3.select('.d3').append('svg').attr('width', width + margin.left + margin.right).attr('height', height + margin.top + margin.bottom).append('g').attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

			color = {
				'Active' : '#5cb85c',
				'Recent Convert' : '#f0ad4e',
				'Inactive' : '#d9534f',
				'Unknown' : '#5bc0de',
				'Moved' : '#999',
				'Do Not Contact' : '#333'
			};
		}

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
			var parseDate = d3.time.format('%Y-%m').parse;

			var data = dataset.map(function(d) {
				return {
					date : parseDate(d.formattedDate),
					visitPercent : d.visitPercent,
					month : d.month,
					year : d.year,
					totalFamilies : d.totalFamilies
				};
			});

			x = d3.time.scale().range([ 0, width ]).domain(d3.extent(data, function(d) {
				return d.date;
			}));

			y = d3.scale.linear().range([ height, 0 ]).domain([ 0, 1 ]);

			var xAxis = d3.svg.axis().scale(x).orient('bottom').ticks(d3.time.months, 1);

			var yAxis = d3.svg.axis().scale(y).orient('left').tickFormat(d3.format('.0%'));

			var line = d3.svg.line().interpolate('basis').x(function(d) {
				return x(d.date);
			}).y(function(d) {
				return y(d.visitPercent);
			});

			svg.append('g').attr('class', 'x axis').attr('transform', 'translate(0,' + height + ')').call(xAxis);

			svg.append('g').attr('class', 'y axis').call(yAxis);

			svg.append('path').datum(data).attr('class', 'line').attr('d', line);

			var points = svg.selectAll('.point').data(data).enter().append('svg:circle').attr('cx', function(d, i) {
				return x(d.date);
			}).attr('cy', function(d, i) {
				return y(d.visitPercent);
			}).attr('r', function(d, i) {
				return 5;
			}).attr('fill', function(d, i) {
				return d.visitPercent < .33 ? 'steelblue' : d.visitPercent < .66 ? 'steelblue' : 'steelblue';
			}).attr('stroke', function(d, i) {
				return 'red';
			}).attr('stroke-width', function(d, i) {
				return 30;
			}).attr('stroke-opacity', function(d, i) {
				return 0;
			}).on('click', function(d) {
				getDetails(d);
			});
		}

		function getDetails(d) {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getVisitPercentageDetails"/>',
				data : {
					'month' : d.month,
					'year' : d.year
				},
				success : function(data) {
					setupDetails(d, data);
				}
			});
		}

		function setupDetails(d3data, dataset) {
			svg.selectAll('.wrapper').remove();

			var totalFamilies = 0;

			var data = dataset.map(function(d) {
				totalFamilies += d.totalFamilies;
				return {
					date : d3data.date,
					percent : d.percentVisited,
					status : d.familyStatus,
					families : d.totalFamilies,
					visited : d.totalVisited
				};
			});

			console.log(d3data);

			var wrappers = svg.selectAll('.point').data(data).enter().append('g').attr('class', 'wrapper');

			wrappers.append('circle').attr('cx', function(d) {
				//return x(d.date);
				return x(d3data.date);
			}).attr('cy', function(d) {
				// 				return y(d.percent);
				return y(d3data.visitPercent);
			}).attr('fill', function(d, i) {
				return color[d.status];
			}).attr('r', function(d) {
				var r = 50 * d.families / totalFamilies;
				return r > 5 ? r : 5;
			}).attr('stroke', function(d, i) {
				return 'red';
			}).attr('stroke-width', function(d, i) {
				return 30;
			}).attr('stroke-opacity', function(d, i) {
				return 0;
			}).attr('class', 'expanded-circle').transition().duration(500).attr('cx', function(d) {
				return x(d.date);
			}).attr('cy', function(d) {
				return y(d.percent);
			}).each('end', function() {
				$('.circle-label').show();
			});

			//TODO move if in same space as another circle - move left/right

			wrappers.append('text').attr('x', function(d) {
				return x(d.date) - 50;
			}).attr('y', function(d) {
				return y(d.percent);
			}).text(function(d) {
				return d.status + ': ' + d.visited + ' of ' + d.families + ', ' + getPercentage(d.percent);
			}).attr('class', 'circle-label').attr('display', 'none');
		}
	</script>
</t:mainPage>