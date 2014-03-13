<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:url var="resources" value="/resources" />

<t:mainPage activeMenu="summaryStats" pageTitle="Summary Statistics" pageHeader="Summary Statistics" pageSubheader="Visualized">

	<div class="row">
		<div class="alert alert-info">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<strong>Welcome to the summary statistics page!</strong><br /> This page shows summary statistics of the ward and each organization in it.
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			getSummaryStatistics();
		});

		function getSummaryStatistics() {

			$.ajax({
				type : 'GET',
				url : '<spring:url value="/dashboard/getSummaryStatistics"/>',
				success : function(data) {
					console.log(data);
				}
			});
		}
	</script>

</t:mainPage>