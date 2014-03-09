<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<spring:url var="resources" value="/resources" />

<t:mainPage activeMenu="familyStatus" pageTitle="Family Status"
	pageHeader="Visualize" pageSubheader="Family Status">

	<div class="row">
		<div class="alert alert-info">
			<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
			<strong>Welcome to the family status page!</strong><br /> These pie
			charts show the makeup of each organization in the ward by family
			status - active, inactive, recent convert, unknown, and do not
			contact. By clicking on a pie slice you can see how many families are
			in that group. Go ahead - give it a try! (Note that many families
			belong to multiple organizations)
		</div>
	</div>

	<div id="familyStatusPie" class="row"></div>

	<br />
	<br />

	<div class="well" id="info-well" style="display: none;">
		<span>There </span><span id="tense"></span>&nbsp;<span id="number"></span>&nbsp;<span
			id="status"></span><span id="familyTense"></span><span> in the
		</span><span id="group"></span>
	</div>

	<script src="${resources}/js/d3.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			getDashboards();
		});

		function getDashboards() {

			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getFamilyStatus"/>',
				success : function(data) {
					for (var i = 0; i < data.length; i++) {
						setupFamilyStatus(data[i].familyStatus, data[i].organizations, data.length);
					}
				}
			});
		}

		function setupFamilyStatus(data, organizations, numOrgs) {

			var display = organizations[0].organization == 'Aggregate' ? 'Ward' : organizations[0].organization;

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

			var svg = d3.select('#familyStatusPie').append('svg').data([ data ]).attr('width', width).attr('height', height).attr('class', 'center col-md-3 col-sm-6 ' + (display != 'Ward' && numOrgs > 1 ? 'hidden-xs' : ''));

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
				populateInfoForStatus(d, display);
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
				$('.pie-header').attr('style', 'font-size:16px;');
				$(this).find('.pie-header').attr('style', 'font-size:20px;font-weight:bold;');
				$(this).parent().find('.slice').each(function() {
					d3.select(this).select('path').transition().duration(500).attr('d', arcOver);
				});
				populateWellForOrganization(d, display);
			});
			legend.append('text').attr('text-anchor', 'middle').attr('class', 'pie-header').style('font-weight', 'bold').text(function(d) {
				return display;
			});
		}

		function populateInfoForStatus(d, display) {
			$('#tense').text(d.data.totalFamilies > 1 ? 'are' : 'is');
			$('#number').text(d.data.totalFamilies);
			$('#status').text(d.data.familyStatus.toLowerCase() + ' ').show();
			$('#familyTense').text(d.data.totalFamilies > 1 ? 'families' : 'family');
			$('#group').text(display.toLowerCase());
			$('#info-well').show();
		}

		function populateWellForOrganization(d, display) {
			$('#tense').text(d.totalFamilies > 1 ? 'are' : 'is');
			$('#number').text(d.totalFamilies);
			$('#status').text(' ').hide();
			$('#familyTense').text(d.totalFamilies > 1 ? 'families' : 'family');
			$('#group').text(display.toLowerCase());
			$('#info-well').show();
		}
	</script>

</t:mainPage>