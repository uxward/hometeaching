<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<t:mainPage activeMenu="visitHistory" pageTitle="Visit History" pageHeader="${fn:length(months)} Month" pageSubheader="Visit  History">

	<table id="historyTable" class="table table-striped table-hover table-bordered" width="100%"></table>


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
					'sWidth' : '15%',
					'mData' : 'familyViewModel',
					'mRender' : setupFamilyName
				},{
					'sTitle' : 'Status',
					'sWidth' : '10%',
					'mData' : 'familyViewModel.familyStatus'
				},{
					'sTitle' : 'Organization',
					'mData' : 'familyViewModel.organizations',
					'mRender' : setupOrganizations,
					'sWidth' : '10%'
				},{
					'sTitle' : 'Home Teachers',
					'sWidth' : '15%',
					'mData' : 'familyViewModel.homeTeachingCompanions',
					'mRender' : setupCompanions
				},{
					'sTitle' : 'Visiting Teachers',
					'sWidth' : '15%',
					'mData' : 'familyViewModel.visitingTeachingCompanions',
					'mRender' : setupCompanions
				},
				<c:forEach items="${months}" var="month" varStatus="status">{
					'sTitle' : '${month}',
					'sWidth' : '${35 / fn:length(months)}%',
					'mData' : 'familyVisits.${status.index}.visits',
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
			var html = '', content, id, visited, visitingTeaching;
			for(var i = 0; i < data.length; i++){
				html += (i == 0 ? 'HT: ' : 'VT: ');
				if(data[i].id != null){
					content = data[i].notes;
					id = data[i].id;
					visited = data[i].visited;
					visitingTeaching = data[i].visitingTeaching;
					html +='<a href="#" class="visitPopover" data-visit-id="' + id + '" data-trigger="manual" data-content="' + content + '">' + setupTrueFalseAsYesNo(visited) + '</a>';
				}
				if(i == 0){
					html += '<br/>';
				}
			}
			return html;
		}
		
		function setupCompanions(data, type, full) {
			return '<a href="<spring:url value="/companion/detail/"/>'
					+ data.id + '">' + data.allTeachers + '</a>';
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