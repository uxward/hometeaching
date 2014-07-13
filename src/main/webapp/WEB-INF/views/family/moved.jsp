<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:notePage activeMenu="moved" pageTitle="Moved Families" pageHeader="Moved" pageSubheader="Families">

	<table id="familyTable" class="table table-striped table-hover" width="100%"></table>

	<script type="text/javascript">
		$(document).ready(function() {

			setupFamilyTable();

		});

		function setupFamilyTable() {

			$('#familyTable').noteDataTable({tableOptions : {
				'ajax' : '<spring:url value="/family/getAllMovedFamilies/"/>',
				'order' : [ [ 2, 'asc' ], [1, 'asc'] ],
				'data' : [],
				'columns' : [ {
					'title' : 'Family Name',
					'data' : 'familyName',
					'width' : '20%',
					'render' : familyNameRender
				}, {
					'title' : 'Records Moved',
					'data' : 'recordsMoved',
					'render' : setupTrueFalseAsYesNo,
					'width' : '5%'
				}, {
					'title' : 'Status',
					'data' : 'familyStatus',
					'width' : '10%'
				}, {
					'title' : 'Organizations',
					'data' : 'organizations',
					'render' : setupOrganizations,
					'width' : '10%'
				}, {
					'title' : 'Address',
					'data' : 'address',
					'width' : '20%',
					'render' : addressRender,
					'class' : 'hidden-xs'
				}, {
					'title' : 'Phone Numbers',
					'data' : 'phoneNumbers',
					'width' : '20%',
					'class' : 'hidden-xs hidden-sm',
					'render' : setupPhoneNumbers
				}, {
					'title' : 'Companions',
					'data' : 'companions',
					'width' : '15%',
					'render' : setupCompanions
				} ],
				'language' : {
					'infoEmpty' : 'No families to show',
					'emptyTable' : 'There are no moved families yet.'
				}
			}});
		}

		function familyNameRender(data, type, full) {
			return '<a href="<spring:url value="/family/detail/"/>' + full.id + '">' + getFamilyAndHeadNames(data, full.people) + '</a>';
		}

		function setupCompanions(data, type, full) {
			var html = '';
			if (data != null) {
				html = '<a href="<spring:url value="/companion/detail/"/>' + data.id + '">' + data.allHometeachers + '</a>';
			}
			return html;
		}
	</script>
</t:notePage>