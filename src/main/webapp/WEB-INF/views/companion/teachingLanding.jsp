<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<c:set var="teachingType">
	<c:choose>
		<c:when test="${visitingTeaching}">Visiting Teaching</c:when>
		<c:otherwise>Home Teaching</c:otherwise>
	</c:choose>
</c:set>

<c:set var="teacherType">
	<c:choose>
		<c:when test="${visitingTeaching}">Visiting Teachers</c:when>
		<c:otherwise>Home Teachers</c:otherwise>
	</c:choose>
</c:set>

<c:set var="teachingActive">
	<c:choose>
		<c:when test="${visitingTeaching}">visitingTeachingDetail</c:when>
		<c:otherwise>homeTeachingDetail</c:otherwise>
	</c:choose>
</c:set>

<t:mainPage activeMenu="${teachingActive}" pageTitle="${teachingType} Landing" pageHeader="${person.fullName}'s" pageSubheader="${teachingType}">

	<div class="row">
		<div class="alert alert-info">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<strong>Click the companionship you want to view!</strong><br /> It looks like you have multiple ${fn:toLowerCase(teachingType)} assignments, so which one do you want to see?
		</div>
	</div>

	<!--  Family Assignment Table
	---------------------------------------------------->
	<table id="assignmentTable" class="table table-striped table-hover table-bordered" data-person-id="${person.id}" width="100%">
	</table>


	<script type="text/javascript">
		$(document).ready(function() {

			setupEventBinding();

			setupAssignmentTable();
		});

		function setupEventBinding() {
		}

		function setupAssignmentTable() {
			$('#assignmentTable').dataTable({
				'sDom' : "<'row'<'col-sm-12'<'pull-right'f>r<'clearfix'>>>t",
				'sAjaxSource' : '<spring:url value="/companion/getByPerson/"/>/${visitingTeaching}?personId=' + $('#assignmentTable').data('personId'),
				'aaData' : [],
				'aoColumns' : [ {
					'sTitle' : '${teacherType}',
					'sWidth' : '33%',
					'mData' : 'teachers',
					'mRender' : setupTeachers
				}, {
					'sTitle' : 'Assigned Families',
					'sWidth' : '67%',
					'mData' : 'assignments',
					'mRender' : setupAssignments
				} ],
				'oLanguage' : {
					'sInfoEmpty' : 'No assignments to show',
					'sEmptyTable' : 'There are no assignments yet.  Talk with your organization leader if you think that\'s a mistake.'
				}
			});
		}

		function setupTeachers(data, type, full) {
			var names = '';
			for (var i = 0; i < data.length; i++) {
				if (i != 0) {
					names += ' and ';
				}
				names += data[i].fullName;
			}
			return '<a href="<spring:url value="/companion/detail/' + full.id + '"/>">' + names + '</a>';
		}

		function setupAssignments(data, type, full) {
			var html = '';
			for (var i = 0; i < data.length; i++) {
				if (i != 0) {
					html += '; ';
				}
				html += data[i].familyName + ', ' + data[i].headOfHousehold;
			}
			return html;
		}
	</script>
</t:mainPage>