<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:mainPage activeMenu="moved" pageTitle="Moved Families" pageHeader="Moved" pageSubheader="Families">

	<table id="familyTable" class="table table-striped table-hover table-bordered" width="100%"></table>

	<script type="text/javascript">
		$(document).ready(function() {

			setupFamilyTable();

		});

		function setupFamilyTable() {

			$('#familyTable').dataTable({
				'sAjaxSource' : '<spring:url value="/family/getAllMovedFamilies/"/>',
				'aaSorting' : [ [ 1, 'asc' ] ],
				'aaData' : [],
				'aoColumns' : [ {
					'sTitle' : 'Family Name',
					'mData' : 'familyName',
					'sWidth' : '20%',
					'mRender' : familyNameRender
				}, {
					'sTitle' : 'Records Moved',
					'mData' : 'recordsMoved',
					'mRender' : setupTrueFalseAsYesNo,
					'sWidth' : '5%'
				}, {
					'sTitle' : 'Status',
					'mData' : 'familyStatus',
					'sWidth' : '10%'
				}, {
					'sTitle' : 'Organizations',
					'mData' : 'organizations',
					'mRender' : setupOrganizations,
					'sWidth' : '10%'
				}, {
					'sTitle' : 'Address',
					'mData' : 'address',
					'sWidth' : '20%',
					'mRender' : addressRender,
					'sClass' : 'hidden-xs'
				}, {
					'sTitle' : 'Phone Numbers',
					'mData' : 'phoneNumbers',
					'sWidth' : '20%',
					'sClass' : 'hidden-xs hidden-sm',
					'mRender' : setupPhoneNumbers
				}, {
					'sTitle' : 'Companions',
					'mData' : 'companions',
					'sWidth' : '15%',
					'mRender' : setupCompanions
				} ],
				'oLanguage' : {
					'sInfoEmpty' : 'No families to show',
					'sEmptyTable' : 'There are no moved families yet.'
				}
			});
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
</t:mainPage>