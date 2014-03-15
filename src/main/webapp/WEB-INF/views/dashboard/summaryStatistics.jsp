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

	<div class="row">
		<c:forEach var="organization" items="${organizations}" varStatus="status">
			<div class="col-md-6 col-sm-6">

				<fieldset>
					<legend> ${organization.organization} </legend>
				</fieldset>
				<table class="table table-hover table-condensed summaryStatistics" data-organization-id="${organization.id}" width="100%">

					<tfoot>
						<tr>
							<th></th>
							<th></th>
							<th></th>
						</tr>
					</tfoot>
				</table>
				<br /> <br />
			</div>
		</c:forEach>
	</div>

	<script src="${resources}/js/d3.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			getSummaryStatistics();
		});

		function getSummaryStatistics() {
			$('.summaryStatistics').each(function() {
				$(this).dataTable({
					'sDom' : 't',
					'sAjaxSource' : '<spring:url value="/dashboard/getFamilyStatusByOrganizationId"/>?organizationId=' + $(this).data('organizationId'),
					'aaData' : [],
					'aaSorting' : [ [ 1, 'desc' ] ],
					'aoColumns' : [ {
						'sTitle' : '% of Group',
						'mData' : 'familyPercent',
						'sWidth' : '25%',
						'mRender' : getPercentage
					}, {
						'sTitle' : '# Families',
						'mData' : 'totalFamilies',
						'sWidth' : '25%'
					}, {
						'sTitle' : 'Group',
						'mData' : 'familyStatus',
						'sWidth' : '50%'
					} ],
					'fnRowCallback' : function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
						var colorClass = {
							'Active' : 'success',
							'Recent Convert' : 'warning',
							'Inactive' : 'danger',
							'Unknown' : 'info',
							'Do Not Contact' : 'active'
						};
						nRow.className = colorClass[aData.familyStatus];
					},
					'fnFooterCallback' : function(nFooter, aaData, iStart, iEnd, aiDisplay) {
						var totalFamilies = 0;
						for (var i = 0; i < aaData.length; i++) {
							totalFamilies += aaData[i].totalFamilies;
						}
						nFooter.getElementsByTagName('th')[1].innerHTML = totalFamilies;
						nFooter.getElementsByTagName('th')[2].innerHTML = 'Total Families';
					},
					'oLanguage' : {
						'sInfoEmpty' : 'There are no groups to show',
						'sEmptyTable' : 'There are no families in this organization.  Add a family on the families page.'
					}
				});
			});
		}
	</script>

</t:mainPage>