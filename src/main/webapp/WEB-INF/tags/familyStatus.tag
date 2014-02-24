<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ attribute name="pageHeader" required="true"%>
<%@ attribute name="pageTitle" required="true"%>
<%@ attribute name="activeMenu" required="true"%>
<%@ attribute name="pageSubheader" required="false"%>

<t:mainPage activeMenu="${activeMenu}" pageTitle="${pageTitle}"
	pageHeader="${pageHeader}" pageSubheader="${pageSubheader}">

	<div class="center">
		<div id="familyStatusPie"></div>
	</div>

	<jsp:doBody />

	<script type="text/javascript">
		$(document).ready(function() {
			getDashboards();
		});

		function getDashboards() {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getFamilyStatusPercentage"/>',
				success : function(data) {
					setupFamilyStatus(data, 'Aggregate');
				}
			});

			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getFamilyStatusPercentageByOrganization"/>',
				data : {
					'organizationId' : 1
				},
				success : function(data) {
					setupFamilyStatus(data.familyStatus, data.organization);
				}
			});

			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getFamilyStatusPercentageByOrganization"/>',
				data : {
					'organizationId' : 2
				},
				success : function(data) {
					setupFamilyStatus(data.familyStatus, data.organization);
				}
			});

			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getFamilyStatusPercentageByOrganization"/>',
				data : {
					'organizationId' : 3
				},
				success : function(data) {
					setupFamilyStatus(data.familyStatus, data.organization);
				}
			});
		}

		function setupFamilyStatus(data, display) {

			var width = 280, height = 280, radius = 100, margin = {
				x : 0,
				y : 10
			};

			var color = {
				'Active' : '#5cb85c',
				'Recent Convert' : '#f0ad4e',
				'Inactive' : '#d9534f',
				'Unknown' : '#5bc0de',
				'Do Not Contact' : '#333'
			};

			var svg = d3.select('#familyStatusPie').append('svg').data([ data ]).attr('width', width).attr('height', height).attr('class', '')

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
				var status = d.data.familyStatus;
				$('#familyTable').dataTable().fnFilter(status, 1, false, true, true, false);
			});

			arcs.append('path').attr('fill', function(d, i) {
				return color[d.data.familyStatus];
			}).attr('d', arc);

			arcs.append('text').attr('transform', function(d, i) {
				var labelr = radius - 50;

				if (d.data.familyStatus == 'Recent Convert') {
					labelr = radius - 25;
				}
				var c = arc.centroid(d), x = c[0], y = c[1],
				// pythagorean theorem for hypotenuse
				h = Math.sqrt(x * x + y * y);
				return 'translate(' + (x / h * labelr) + ',' + (y / h * labelr) + ')';
			}).attr('text-anchor', function(d) {
				// are we past the center?
				return d.data.familyStatus == 'Recent Convert' ? 'middle' : (d.endAngle + d.startAngle) / 2 > Math.PI ? 'end' : 'start';
			}).text(function(d, i) {
				return data[i].familyStatus + ' ' + getPercentage($.trim(data[i].familyPercent));
			});

			var legend = svg.selectAll('.legend').data([ {
				'display' : 'display'
			} ]).enter().append('g').attr('class', 'legend').attr('transform', function(d, i) {
				return 'translate(' + width / 2 + ',' + margin.y + ')';
			});
			legend.append('text').attr('text-anchor', 'middle').style('font-weight', 'bold').text(function(d) {
				return display;
			});
		}
	</script>
</t:mainPage>