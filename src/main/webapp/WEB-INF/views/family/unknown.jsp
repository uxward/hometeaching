<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<t:mainPage activeMenu="unknown" pageTitle="Unknown Families"
	pageHeader="Unknown" pageSubheader="Families">

	<table id="familyTable"
		class="table table-striped table-hover table-bordered" width="100%"></table>

	<script type="text/javascript">
		$(document).ready(function() {

			setupFamilyTable();

		});

		function setupFamilyTable() {

			$('#familyTable').dataTable({
				'sAjaxSource' : '<spring:url value="/family/getAllUnknownFamilies/"/>',
				'aaSorting' : [ [ 0, 'asc' ] ],
				'aaData' : [],
				'aoColumns' : [ {
					'sTitle' : 'Family Name',
					'mData' : 'familyName',
					'sWidth' : '25%',
					'mRender' : familyNameRender
				},{
					'sTitle' : 'Organizations',
					'mData' : 'organizations',
					'mRender' : setupOrganizations,
					'sWidth' : '15%'
				}, {
					'sTitle' : 'Address',
					'mData' : 'address',
					'sWidth' : '25%',
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
					'sWidth' : '15%'
					,'mRender' : setupCompanions
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
				html = data.allHometeachers;
				<sec:authorize access="hasRole('leader')">
					html = '<a href="<spring:url value="/companion/detail/"/>' + data.id + '">' + data.allHometeachers + '</a>'
				</sec:authorize>
			}
			return html;
		}
	</script>
</t:mainPage>