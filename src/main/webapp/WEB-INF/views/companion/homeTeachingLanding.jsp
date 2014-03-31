<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<t:mainPage activeMenu="homeTeachingDetail" pageTitle="Home Teaching Detail" pageHeader="" pageSubheader="Home Teaching">

	<fieldset>
		<legend> Active Assignments </legend>
	</fieldset>

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
			$('#assignmentTable')
					.dataTable(
							{
								'sDom' : 't',
								'sAjaxSource' : '<spring:url value="/companion/getAllHomeTeachingCompanions/"/>?personId=' + $('#assignmentTable').data('personId'),
								'aaData' : [],
								'aoColumns' : [{
									'sTitle' : 'Family',
									'mData' : 'familyName'
								}, {
									'sTitle' : 'Status',
									'sClass' : 'hidden-xs hidden-sm',
									'mData' : 'familyStatus'
								}, {
									'sTitle' : 'Children',
									'mData' : 'people'
								}, {
									'sTitle' : 'Address',
									'mData' : 'address'
								}, {
									'sTitle' : 'Phone Numbers',
									'mData' : 'phoneNumbers'
								}
								<sec:authorize access="hasRole('leader')">
								, {
									'sTitle' : 'Actions',
									'mData' : 'id'
								} 
								</sec:authorize>
								],
								'oLanguage' : {
									'sInfoEmpty' : 'No families to show',
									'sEmptyTable' : 'There are no families assigned yet.  Add a family by clicking the button below.'
								}
							});
		}
	</script>
</t:mainPage>