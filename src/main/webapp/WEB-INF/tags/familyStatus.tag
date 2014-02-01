<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ attribute name="pageHeader" required="true"%>
<%@ attribute name="pageTitle" required="true"%>
<%@ attribute name="activeMenu" required="true"%>
<%@ attribute name="pageSubheader" required="false"%>

<t:mainPage activeMenu="${activeMenu}" pageTitle="${pageTitle}" pageHeader="${pageHeader}" pageSubheader="${pageSubheader}">

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
					setupFamilyStatus(data);
				}
			});
		}

		function setupFamilyStatus(data) {

			var width = 320, height = width, radius = 100, margin = (height - (2 * radius)) / 2;

			var color = d3.scale.ordinal().range([ "#5cb85c", "#f0ad4e", "#d9534f", "#5bc0de", "#999" ]);

			var vis = d3.select('#familyStatusPie').append('svg:svg').data([ data ]).attr('width', width).attr('height', height).attr('class', '').append('svg:g').attr('transform',
					'translate(' + (radius + margin) + ',' + (radius + margin) + ')');

			var arc = d3.svg.arc().outerRadius(radius).innerRadius(radius - 70);

			var arcOver = d3.svg.arc().outerRadius(radius + 20).innerRadius(radius - 70);

			var arcUnder = d3.svg.arc().outerRadius(radius);

			var pie = d3.layout.pie().value(function(d) {
				return d.totalFamilies;
			});

			var arcs = vis.selectAll('g.slice').data(pie).enter().append('svg:g').attr('class', 'slice').on('click', function(d) {
				d3.selectAll('.slice').select('path').transition().duration(500).attr('d', arc);
				d3.select(this).select('path').transition().duration(500).attr('d', arcOver);
				var status = d.data.familyStatus;
				$('#familyTable').dataTable().fnFilter(status, 1, false, true, true, false);
			});

			arcs.append('svg:path').attr('fill', function(d, i) {
				return color(i);
			}).attr('d', arc);

			arcs.append('svg:text').attr('transform', function(d, i) {
				var labelr = radius - 12;
				if (d.data.familyStatus == 'Recent Convert') {
					labelr = radius;
				}
				var c = arc.centroid(d), x = c[0], y = c[1],
				// pythagorean theorem for hypotenuse
				h = Math.sqrt(x * x + y * y);
				return "translate(" + (x / h * labelr) + ',' + (y / h * labelr) + ")";

			}).attr('text-anchor', function(d) {
				// are we past the center?
				return d.data.familyStatus == 'Recent Convert' ? 'start' : d.data.familyStatus == 'Moved' ? 'middle' : (d.endAngle + d.startAngle) / 2 > Math.PI ? "end" : "start";
			}).text(function(d, i) {
				return data[i].familyStatus + ' ' + getPercentage($.trim(data[i].familyPercent));
			});
		}
	</script>
</t:mainPage>