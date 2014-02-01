<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<t:mainPage activeMenu="visitHistory" pageTitle="Visit - History" pageHeader="Visit" pageSubheader="${fn:length(months)} Month History">

	<table id="historyTable" class="table table-striped table-hover table-bordered"></table>


	<label id="monthSelect" style="margin-left: 10px;">
		and <select size="1" class="form-control input-sm" style="width: 75px;" id="numberOfMonths">
			<c:forEach var="n" begin="1" end="12" step="1">
				<option value="${n}" ${n == fn:length(months) ? 'selected' : ''}>${n}</option>
			</c:forEach>
		</select> Months
	</label>

	<script type="text/javascript">
		$(document).ready(function() {
			setupHistoryTable();
			
			setupEventBinding();
		});

		function setupHistoryTable() {
			$('#historyTable').dataTable({
				'sAjaxSource' : '<spring:url value="/visit/getHistory/"/>',
				'aaData' : [],
				'fnServerParams': function ( aoData ) {
		            aoData.push( { 'name' : 'n', 'value' : '${fn:length(months)}' } );
		        },
				'aoColumns' : [{
					'sTitle' : 'Family',
					'sWidth' : '20%',
					'mData' : 'familyViewModel',
					'mRender' : setupFamilyName
				},{
					'sTitle' : 'Status',
					'sWidth' : '10%',
					'mData' : 'familyViewModel.familyStatus'
				},{
					'sTitle' : 'Home Teachers',
					'sWidth' : '20%',
					'mData' : 'familyViewModel.companions',
					'mRender' : setupCompanions
				},
				<c:forEach items="${months}" var="month" varStatus="status">{
					'sTitle' : '${month}',
					'sWidth' : '${50 / fn:length(months)}%',
					'mData' : 'visits.${status.index}',
					'mRender' : setupVisit
				} <c:if test="${!status.last}">,</c:if>
				</c:forEach>
				]
			});
			
			$('#monthSelect').appendTo('#historyTable_length');
		}
		
		function setupFamilyName(data, type, full){
			return data.familyName + ', ' + data.headOfHousehold;
		}
		
		function setupVisit(data, type, full){
			var content = data.notes;
			return '<a href="#" class="visitPopover" data-visit-id="' + data.id + '" data-trigger="manual" data-content="' + content + '">' + setupTrueFalseAsYesNo(data.visited, type, full) + '</a>';
		}
		
		function setupCompanions(data, type, full) {
			return '<a href="<spring:url value="/companion/detail/"/>'
					+ data.id + '">' + data.allHometeachers + '</a>';
		}
		
		function setupEventBinding(){
			$('#historyTable').on(
					'click',
					'.visitPopover',
					function() {
						getVisitHistory($(this));
					});
			
			$('#numberOfMonths').change(function(){
				window.location.href = '<spring:url value="/visit/history/"/>' + $(this).val();
			});
		}
		
		function getVisitHistory($this){
			$('.visitPopover').popover('hide');
			$this.popover('show');
			//TODO get more info from ajax call
		}
	</script>
</t:mainPage>