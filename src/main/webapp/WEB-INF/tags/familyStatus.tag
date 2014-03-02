<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ attribute name="pageHeader" required="true"%>
<%@ attribute name="pageTitle" required="true"%>
<%@ attribute name="activeMenu" required="true"%>
<%@ attribute name="pageSubheader" required="false"%>

<spring:url var="resources" value="/resources" />

<t:mainPage activeMenu="${activeMenu}" pageTitle="${pageTitle}"
	pageHeader="${pageHeader}" pageSubheader="${pageSubheader}">

	<div id="familyStatusPie" class="row"></div>

	<jsp:doBody />


	<script src="${resources}/js/d3.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			getDashboards();
		});

		function getDashboards() {

			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getFamilyStatusPercentage"/>',
				success : function(data) {
					for (var i = 0; i < data.length; i++) {
						setupFamilyStatus(data[i].familyStatus, data[i].organizations, data.length);
					}
				}
			});
		}

		function setupFamilyStatus(data, organizations, numOrgs) {

			var display = organizations[0].organization;

			var width = 280, height = 280, radius = 100, margin = {
				x : 0,
				y : 20
			};

			var color = {
				'Active' : '#5cb85c',
				'Recent Convert' : '#f0ad4e',
				'Inactive' : '#d9534f',
				'Unknown' : '#5bc0de',
				'Do Not Contact' : '#999'
			};

			var svg = d3.select('#familyStatusPie').append('svg').data([ data ]).attr('width', width).attr('height', height).attr('class', 'center col-md-3 col-sm-6 ' + (display != 'Aggregate' && numOrgs > 1 ? 'hidden-xs' : ''));

			var vis = svg.append('g').attr('transform', 'translate(' + width / 2 + ',' + (height / 2 + margin.y) + ')');

			var arc = d3.svg.arc().outerRadius(radius).innerRadius(radius - 70);

			var arcOver = d3.svg.arc().outerRadius(radius + 20).innerRadius(radius - 70);

			var arcUnder = d3.svg.arc().outerRadius(radius);

			var pie = d3.layout.pie().value(function(d) {
				return d.totalFamilies;
			});

			var arcs = vis.selectAll('g.slice').data(pie).enter().append('g').attr('class', 'slice').on('click', function(d) {
				d3.selectAll('.slice').select('path').transition().duration(500).attr('d', arc);
				d3.select(this).select('path').transition().duration(500).attr('d', arcOver);
				$('.pie-header').attr('style', 'font-size:16px;');
				$(this).closest('svg').find('.pie-header').attr('style', 'font-size:20px;font-weight:bold;');
				var status = d.data.familyStatus;
				$('#familyTable').dataTable().fnFilter(status, 1, false, true, true, false);
				$('#familyTable').dataTable().fnFilter((display == 'Aggregate' ? '' : display), 2, false, true, true, false);
			});

			arcs.append('path').attr('fill', function(d, i) {
				return color[d.data.familyStatus];
			}).attr('d', arc);

			arcs.append('text').attr('transform', function(d, i) {
				var labelr = radius - 50;
				if (d.data.familyStatus == 'Recent Convert') {
					labelr = radius;
				}
				var c = arc.centroid(d), x = c[0], y = c[1],
				// pythagorean theorem for hypotenuse
				h = Math.sqrt(x * x + y * y);
				return 'translate(' + (x / h * labelr) + ',' + (y / h * labelr) + ')';
			}).attr('text-anchor', function(d) {
				// are we past the center?
				return (d.endAngle + d.startAngle) / 2 > Math.PI ? 'end' : 'start';
			}).text(function(d, i) {
				return data[i].familyStatus + ' ' + getPercentage($.trim(data[i].familyPercent));
			});

			var legend = svg.selectAll('.legend').data(organizations).enter().append('g').attr('class', 'legend').attr('transform', function(d, i) {
				return 'translate(' + width / 2 + ',' + margin.y + ')';
			}).on('click', function(d) {
				//show all records again
				d3.selectAll('.slice').select('path').transition().duration(500).attr('d', arc);
				$('#familyTable').dataTable().fnFilter('', 1);
				$('#familyTable').dataTable().fnFilter('', 2);
				$('#familyTable').dataTable().fnFilter('');
				$('#familyTable').dataTable().fnFilter((display == 'Aggregate' ? '' : display), 2, false, true, true, false);
				$('.pie-header').attr('style', 'font-size:16px;');
				$(this).find('.pie-header').attr('style', 'font-size:20px;font-weight:bold;');
				$(this).parent().find('.slice').each(function() {
					d3.select(this).select('path').transition().duration(500).attr('d', arcOver);
				});
			});
			legend.append('text').attr('text-anchor', 'middle').attr('class', 'pie-header').style('font-weight', 'bold').text(function(d) {
				return display;
			});
		}
	</script>
</t:mainPage>