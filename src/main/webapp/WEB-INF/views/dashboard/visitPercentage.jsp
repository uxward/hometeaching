<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:url var="resources" value="/resources" />

<t:mainPage activeMenu="visitPercentage" pageTitle="Visit Percentage" pageHeader="Visit Percentage" pageSubheader="Visualized">

	<style>
/*
	* SVG STYLING
	*/
.axis path, .axis line {
	fill: none;
	stroke: #000;
	shape-rendering: crispEdges;
}

.x.axis path {
	display: none;
}

.line {
	fill: none;
	stroke-width: 1.5px;
}

.point {
	stroke: red;
	stroke-width: 30px;
	stroke-opacity: 0;
}

.line-highpriests {
	stroke: #eab25b;
}

.point-highpriests {
	fill: #eab25b;
}

.line-reliefsociety {
	stroke: #f7504f;
}

.point-reliefsociety {
	fill: #f7504f;
}

.line-eldersquorum {
	stroke: #2271B2;
}

.point-eldersquorum {
	fill: #2271B2;
}

/*
* Legend styling
*/
.btn-reliefsociety:hover {
	color: #fff !important;
	background-color: #f7504f !important;
	border-color: #f7504f !important;
}

.btn-reliefsociety {
	background-color: #fff !important;
	color: #f7504f !important;
	border-color: #f7504f !important;
}

.btn-eldersquorum:hover {
	color: #fff !important;
	background-color: #2271B2 !important;
	border-color: #2271B2 !important;
}

.btn-eldersquorum {
	background-color: #fff !important;
	color: #2271B2 !important;
	border-color: #2271B2 !important;
}

.btn-highpriests:hover {
	color: #fff !important;
	background-color: #eab25b !important;
	border-color: #eab25b !important;
}

.btn-highpriests {
	background-color: #fff !important;
	color: #eab25b !important;
	border-color: #eab25b !important;
}

.btn-neutral {
	color: #fff;
	background-color: #999;
	border-color: #999;
}

.btn-neutral:hover {
	color: #fff;
	background-color: #7F7F7F;
	border-color: #7F7F7F;
}
</style>

	<div class="row">
		<div class="alert alert-info">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<strong>Welcome to the visit percentage page!</strong><br /> This graph shows the home teaching visit percentage trends over past several months. If you click on an organization's button or graph point for a particular month you can see how well we did for the different
			family groupings - active, inactive, recent convert, and unknown. Go ahead - give it a try!
		</div>
	</div>

	<div style="text-align: center;" class="center-block">
		<div class="btn btn-reliefsociety btn-lg toggle-expand" data-org="reliefsociety">
			<span class="glyphicon glyphicon-resize-full"></span> Relief Society
		</div>
		<div class="btn btn-highpriests btn-lg toggle-expand" data-org="highpriests">
			<span class="glyphicon glyphicon-resize-full"></span> High Priests
		</div>
		<div class="btn btn-eldersquorum btn-lg toggle-expand" data-org="eldersquorum">
			<span class="glyphicon glyphicon-resize-full"></span> Elders
		</div>
	</div>

	<div class="d3"></div>

	<div style="text-align: center;">
		<div class="center-block">
			<h4>Family Status Legend</h4>
		</div>
		<div class="center-block">
			<div class="btn btn-success">Active</div>
			<div class="btn btn-danger">Inactive</div>
			<div class="btn btn-info">Unknown</div>
			<div class="btn btn-warning">Recent Convert</div>
			<div class="btn btn-neutral">Do Not Contact</div>
		</div>
	</div>

	<script src="${resources}/js/d3.min.js"></script>
	<script type="text/javascript">
		var svg, x, y, color;

		//setup custom manual click call for d3
		jQuery.fn.d3Click = function() {
			this.each(function(i, e) {
				var evt = document.createEvent("MouseEvents");
				evt.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
				e.dispatchEvent(evt);
			});
		};

		jQuery.fn.reverse = [].reverse;

		$(document).ready(function() {
			setupEventBinding();
			setupGraph();
			getDashboard();
		});

		function setupEventBinding() {
			$('.toggle-expand').click(function() {
				var org = $(this).data('org');
				var $span = $(this).find('span');
				if ($span.hasClass('glyphicon-resize-full')) {
					$('.point-' + org).reverse().each(function(i) {
						$(this).delay(10 * i).queue(function(nxt) {
							$(this).d3Click();
							nxt();
						});
					});
				} else {
					$('.wrapper.' + org).reverse().each(function(i) {
						$(this).delay(10 * i).queue(function(nxt) {
							$(this).d3Click();
							nxt();
						});
					});
				}

				$span.toggleClass('glyphicon-resize-full glyphicon-resize-small');
			});
		}

		function setupGraph() {
			margin = {
				left : 50,
				right : 0,
				top : 40,
				bottom : 20
			}, width = 1200 - margin.left - margin.right, height = 600 - margin.top - margin.bottom;

			svg = d3.select('.d3').append('svg').attr('width', width + margin.left + margin.right).attr('height', height + margin.top + margin.bottom).append('g').attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

			color = {
				'Active' : '#5cb85c',
				'Recent Convert' : '#f0ad4e',
				'Inactive' : '#d9534f',
				'Unknown' : '#5bc0de',
				'Do Not Contact' : '#999'
			};

			var today = new Date();
			var yearAgo = today.getFullYear() - 1;
			var sixMonthsLater = today.getMonth() + 6;
			var oneYearAgo = new Date(yearAgo, sixMonthsLater, today.getDay());

			x = d3.time.scale().range([ margin.left, width ]).domain([ oneYearAgo, today ]);

			y = d3.scale.linear().range([ height - margin.bottom, 0 ]).domain([ 0, 1 ]);

			xAxis = d3.svg.axis().scale(x).orient('bottom').ticks(d3.time.months, 1);

			yAxis = d3.svg.axis().scale(y).orient('left').tickFormat(d3.format('.0%'));

			line = d3.svg.line().interpolate('basis').x(function(d) {
				return x(d.date);
			}).y(function(d) {
				return y(d.visitPercent);
			});

			svg.append('g').attr('class', 'x axis').attr('transform', 'translate(0,' + height + ')').call(xAxis);

			svg.append('g').attr('class', 'y axis').call(yAxis);
		}

		function getDashboard() {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getVisitPercentage"/>',
				success : function(data) {
					for (var i = 0; i < data.length; i++) {
						setupD3(data[i].visits);
					}
				}
			});
		}

		function setupD3(dataset) {
			if (dataset.length > 0) {
				var parseDate = d3.time.format('%Y-%m').parse;

				var data = dataset.map(function(d) {
					return {
						date : parseDate(d.formattedDate),
						visitPercent : d.visitPercent,
						month : d.month,
						year : d.year,
						totalFamilies : d.totalFamilies,
						organization : d.organization
					};
				});

				var organization = dataset[0].organization.name.replace(' ', '');
				svg.append('path').datum(data).attr('class', 'line line-' + organization.toLowerCase()).attr('d', line);

				var points = svg.selectAll('.point-').data(data).enter().append('svg:circle').attr('class', 'point point-' + organization.toLowerCase()).attr('cx', function(d, i) {
					return x(d.date);
				}).attr('cy', function(d, i) {
					return y(d.visitPercent);
				}).attr('r', function(d, i) {
					return 5;
				}).on('click', function(d) {
					getDetails(d);
				});
			}
		}

		function getDetails(d) {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getVisitPercentageDetails"/>',
				data : {
					'month' : d.month,
					'year' : d.year,
					'organizationId' : d.organization.id
				},
				success : function(data) {
					setupDetails(d, data);
				}
			});
		}

		function setupDetails(d3data, dataset) {
			var org = d3data.organization.name.toLowerCase().replace(' ', '');

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

			var wrappers = svg.selectAll('.detail-point').data(data).enter().append('g').attr('class', 'wrapper ' + org).attr('id', function(d) {
				return d.status.replace(/ /g, '') + org + d.date.getTime();
			}).on('click', function(d) {
				var $wrapper = svg.selectAll('#' + d.status.replace(/ /g, '') + org + d.date.getTime());
				$wrapper.select('.circle-label').remove();
				var $point = $wrapper.select('circle');
				$point.transition().duration(500).attr('cy', function(d) {
					return y(d3data.visitPercent);
				}).remove();
			});

			wrappers.append('circle').attr('cx', function(d) {
				return x(d3data.date);
			}).attr('cy', function(d) {
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
			}).attr('class', 'expanded-circle').transition().duration(500).attr('cy', function(d) {
				return y(d.percent);
			}).each('end', function() {
				$('.circle-label').show();
			});

			//TODO move if in same space as another circle - move left/right

			wrappers.append('text').attr('x', function(d) {
				return x(d.date) - 35;
			}).attr('y', function(d) {
				return y(d.percent);
			}).text(function(d) {
				return d.visited + '/' + d.families + ', ' + getPercentage(d.percent);
			}).attr('class', 'circle-label').attr('display', 'none');
		}
	</script>
</t:mainPage>