<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<t:mainPage activeMenu="feedback" pageTitle="Feedback" pageHeader="Feedback" pageSubheader="Current">

	<table id="feedbackTable" class="table table-striped table-hover" width="100%"></table>

	<script type="text/javascript">
		$(document).ready(function() {

			setupFeedbackTable();

		});

		function setupFeedbackTable() {

			$('#feedbackTable').dataTable({
				'sAjaxSource' : '<spring:url value="/feedback/getAllFeedback/"/>',
				'aaSorting' : [ [ 1, 'asc' ], [ 3, 'asc'] ],
				'aaData' : [],
				'aoColumns' : [{
					'sTitle' : 'Feedback',
					'mData' : 'feedback',
					'sWidth' : '50%'
				}, {
					'sTitle' : 'Priority',
					'mData' : 'priority.priority',
					'sWidth' : '10%'
				}, {
					'sTitle' : 'Person',
					'mData' : 'person',
					'mRender' : setupPersonFullName,
					'sWidth' : '20%'
				}, {
					'sTitle' : 'Date',
					'mData' : 'date',
					'mRender' : setupFullDate,
					'sWidth' : '20%',
					'sType' : 'date'
				} ],
				'oLanguage' : {
					'sInfoEmpty' : 'No feedback to show',
					'sEmptyTable' : 'There is no feedback yet.'
				}
			});
		}
	</script>
</t:mainPage>