<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<jsp:useBean id="now" class="java.util.Date" />
<c:set var="currentMonthYear">
	<fmt:formatDate value="${now}" type="both" pattern="MM-yyyy" />
</c:set>

<c:set var="companionship">
	<c:forEach var="hometeacher" items="${companion.hometeachers}" varStatus="status">
		<span class="pull-left">${hometeacher.firstName}&nbsp;</span>
		<span class="hidden-sm hidden-xs pull-left">${hometeacher.family.familyName}&nbsp;</span>
		<c:if test="${!status.last}">
			<span class="pull-left">and&nbsp;</span>
		</c:if>
	</c:forEach>
</c:set>

<spring:url var="dashboard" value="/dashboard" />

<t:mainPage activeMenu="yourCompanion" pageTitle="Companion Detail" pageHeader="${companionship}" pageSubheader="Companion">

	<div class="row">
		<div class="alert alert-info">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<fieldset>
				<legend> Companion Contact Information </legend>
			</fieldset>
			<c:forEach var="hometeacher" items="${companion.hometeachers}" varStatus="status">
				<strong>${hometeacher.firstName} phone number:</strong>
				<span class="phone-number">${hometeacher.phoneNumber}</span>
				<c:if test="${!status.last}">
					<br />
				</c:if>
			</c:forEach>
		</div>
	</div>

	<fieldset>
		<legend> Assigned Families </legend>
	</fieldset>

	<!--  Family Assignment Table
	---------------------------------------------------->
	<table id="assignmentTable" class="table table-striped table-hover table-bordered" data-companion-id="${companion.id}" width="100%">
	</table>

	<br />

	<sec:authorize access="hasRole('leader')">
		<a href="#addFamily" class="btn btn-primary" data-toggle="modal">Add Family</a>
		<a href="#" class="btn" id="emailAssignments">
			<i class="glyphicon glyphicon-envelope"></i> Email Assignments
		</a>

		<!--  Add Family Modal
	---------------------------------------------------->
		<div id="addFamily" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addFamilyLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="addFamilyLabel">Add Family</h3>
					</div>

					<div class="modal-body">
						<form id="familyForm">
							<div class="form-group">
								<select name="autopopulatingAssignments[0].familyId" id="familySelect" class="form-control">
									<option value="">Select Family</option>
									<c:forEach items="${families}" var="family">
										<option value="${family.id}">${family.familyName},&nbsp;${family.headOfHousehold}&nbsp;</option>
									</c:forEach>
								</select>
							</div>
							<input type="hidden" value="${companion.id}" name="id" />
						</form>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
						<button type="button" class="btn btn-primary" id="saveAssignment" data-action="save">Add Family</button>
					</div>
				</div>
			</div>
		</div>

		<br />
		<br />

	</sec:authorize>

	<!--  Visit History
	---------------------------------------------------->

	<fieldset>
		<legend>Visit History</legend>
	</fieldset>

	<!--  Accordion visit history
	---------------------------------------------------->
	<div id="visitHistory">
		<ul class="nav nav-tabs">
			<c:forEach var="family" items="${companion.assignments}" varStatus="status">
				<li class="${status.first ? 'active' : ''}" id="${family.id}-tab-link"><a href="#${family.id}-tab" data-toggle="tab" class="hidden-xs">${family.familyName},&nbsp;${family.headOfHousehold}</a>
					<a href="#${family.id}-tab" data-toggle="tab" class="hidden-sm hidden-md hidden-lg">${family.familyName}</a></li>
			</c:forEach>
		</ul>
		<div class="tab-content">
			<c:forEach var="family" items="${companion.assignments}" varStatus="status">
				<div class="tab-pane ${status.first ? 'active' : ''}" id="${family.id}-tab">
					<br />

					<table class="table table-striped table-hover table-bordered visitHistory" data-family-id="${family.id}" width="100%">
					</table>
					<c:if test="${canAction}">
						<a href="#recordVisit" role="button" class="btn btn-primary recordVisit" data-assignment-id="${family.assignmentId}" data-family-id="${family.id}" data-toggle="modal">Record Visit for the
							${family.familyName} family</a>
					</c:if>
				</div>
			</c:forEach>
		</div>
	</div>

	<!--  Record Visit Modal
	---------------------------------------------------->
	<div id="recordVisit" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="recordVisitLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="recordVisitLabel">Record Visit</h3>
				</div>

				<div class="modal-body">
					<form id="visitForm">
						<div class="form-group">
							<select name="familyId" id="visitFamilySelect" class="form-control" disabled>
								<option value="">Select Family</option>
								<c:forEach var="family" items="${companion.assignments}">
									<option value="${family.id}">${family.familyName}&nbsp;${family.headOfHousehold}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" id="datepicker"> <input type="hidden" name="visitDate" id="visitDate" />
						</div>
						<div class="form-group">
							<label class="checkbox">
								<input type="checkbox" name="visited" id="visited"> Visited
							</label>
						</div>
						<div class="form-group">
							<textarea class="form-control" name="notes" id="notes" placeholder="Notes" maxlength="400"></textarea>
						</div>
						<div>
							<input type="hidden" name="id" id="visitId" /> <input type="hidden" name="assignmentId" id="assignmentId" /><input type="hidden" name="familyId" id="familyId" />
						</div>
					</form>
				</div>

				<div class="modal-footer">
					<button class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
					<button class="btn btn-primary" id="saveVisit">Record Visit</button>
				</div>

			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {

			setupEventBinding();

			setupAssignmentTable();

			setupVisitHistoryTables();

			setupModal();

			setupDatePicker();
		});

		function setupEventBinding() {
			$('#saveAssignment').click(function() {
				if (assignmentValid()) {
					saveAssignment($(this));
				}
			});

			$('#saveVisit').click(function() {
				if (visitValid()) {
					saveVisit();
				}
			});

			$('.recordVisit').click(function() {
				$('#visitFamilySelect').val($(this).data('familyId'));
				$('#familyId').val($(this).data('familyId'));
				$('#assignmentId').val($(this).data('assignmentId'));
				$('#saveVisit').data('familyId',$(this).data('familyId')).data('action', 'save');
			});

			$('#assignmentTable').on('click', '.removeFamily', function() {
				if (confirm('Are you sure you want to remove this family?')) {
					removeAssignment($(this));
				}
			});
			
			$('.visitHistory').on('click', '.editVisit', function() {
				showEditVisit($(this));
			});

			$('#emailAssignments').click(function() {
				if (confirm('Are you sure you want to email these assignments?')) {
					emailAssignments();
				}
			});
		}
		
		function showEditVisit($this){
			$('#visitId').val($this.data('visitId'));
			$('#saveVisit').data('familyId', $this.data('familyId')).data('action', 'edit').data('editRow', $this.closest('tr')[0]);
			$('#saveVisit').data('action', 'edit').data('editRow', $this.closest('tr')[0]);
			$('#visitFamilySelect').val($this.data('familyId'));
			$('#familyId').val($(this).data('familyId'));
			$('#assignmentId').val($this.data('assignmentId'));
			$('#notes').val($this.data('notes'));
			$('#created').val($this.data('created'));
			$('#visited').prop('checked', $this.data('visited'));
			$('#datepicker').datepicker('update', new Date($this.data('visitDate')));
			$('#recordVisit').modal('show');
		}

		function visitValid() {
			var valid = true;
			var error = '<p>In order to save this visit you must ';

			var $visitDate = $('#datepicker');
			var visitDate = new Date($visitDate.datepicker('getDate'));
			if (isNaN(visitDate.getTime())) {
				error += 'select a month';
				valid = false;
				$visitDate.parent().addClass('has-error');
			} else {
				$visitDate.parent().removeClass('has-error');
			}

			var $family = $('#visitFamilySelect');
			if ($family.val() == null || $family.val() == '') {
				valid = false;
				$family.parent().addClass('has-error');
			} else {
				$family.parent().removeClass('has-error');
			}

			var $notes = $('#notes');
			if ($.trim($notes.val()) == '') {
				error += (!valid ? ' and ' : '') + 'enter notes';
				valid = false;
				$notes.parent().addClass('has-error');
			} else {
				$notes.parent().removeClass('has-error');
			}
			
			if(!valid){
				showModalError(error + '.</p>');
			}

			return valid;
		}

		function saveVisit() {
			$('#visitDate').val(
					new Date($('#datepicker').datepicker('getDate')));
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/visit/saveVisit"/>',
				data : $('#visitForm').serialize(),
				success : function(data) {
					handleSaveVisit(data);
				}
			});
		}
		
		function handleSaveVisit(data){
			if(data.success){
				handleVisitSuccess(data);
				showNotificationSuccess('<strong>This visit has been saved!</strong><br/>Thanks for recording your visit.  If you\'d like to see how visits are going for the entire elders quorum, check out <a href="${dashboard}/visitPercentage">the dashboard here</a>');
			} else if(data.duplicate){
				showModalError('A visit was already saved for this family for this month.  If you need to edit the visit information you can do so from the visit history table.');
			} else if(data.error){
				showModalError('There was an unexpected error while trying to save this visit.  If the problem continues please submit feedback with a description of the problem.');
			}
		}

		function handleVisitSuccess(data) {
			var $saveVisit = $('#saveVisit');
			
			var familyId = $saveVisit.data('familyId');
			if($saveVisit.data('action') == 'save'){
				$('#' + familyId + '-tab').find('.visitHistory').dataTable().fnAddData(data);
			} else {
				$('#' + familyId + '-tab').find('.visitHistory').dataTable().fnUpdate(data, $saveVisit.data('editRow'));
			}
			
			
			$('#recordVisit').modal('hide');
			resetFormElements('visitForm');
			$('#visitId').val('');
		}

		function setupDatePicker() {
			$("#datepicker").datepicker({
				format : 'MM yyyy',
				minViewMode : 'months',
				autoclose : true
			});
		}

		function assignmentValid() {
			return $('#familySelect').val() != null
					&& $('#familySelect').val() != ''
					&& new Number($('#familySelect').val()) > 0;
		}

		function setupVisitHistoryTables() {
			$('.visitHistory').each(function() {
				$(this).dataTable({
					'sDom' : 't'
					,'sAjaxSource' : '<spring:url value="/visit/view/"/>?familyId=' + $(this).data('familyId')
					,'aaData' : []
					,'aaSorting': [[ 1, 'desc' ]]
					,'aoColumns' : [ {
						'sTitle' : 'ID',
						'mData' : 'id',
						'bVisible' : false
					}, {
						'sTitle' : 'Visit Date',
						'mData' : 'monthYear',
						'bVisible' : false
					}, {
						'sTitle' : 'Visit Month',
						'mData' : 'monthYear',
						'sWidth' : '12%',
						'mRender' : setupMonthYear
					}, {
						'sTitle' : 'Visited',
						'mData' : 'visited',
						'sWidth' : '8%',
						'mRender' : setupTrueFalseAsYesNo
					}, {
						'sTitle' : 'Notes',
						'mData' : 'notes',
						'sWidth' : '70%'
					} 
					<c:if test="${canAction}">
					, {
						'sTitle' : 'Actions',
						'mData' : 'id',
						'sWidth' : '10%',
						'mRender' : setupVisitActions
					}
					</c:if>
					],
					'oLanguage' : {
						'sInfoEmpty' : 'No visits to show',
						'sEmptyTable' : 'There are no visits recorded yet.  Add a visit by clicking the button below.'
					}
				});
			});
		}

		function setupAssignmentTable() {
			$('#assignmentTable')
					.dataTable(
							{
								'sDom' : 't',
								'sAjaxSource' : '<spring:url value="/companion/getAssignments/"/>?companionId=' + $('#assignmentTable').data('companionId'),
								'aaData' : [],
								'aoColumns' : [{
									'sTitle' : 'Family',
									'mData' : 'familyName',
									'mRender' : setupFamilyName
								}, {
									'sTitle' : 'Status',
									'sClass' : 'hidden-xs hidden-sm',
									'mData' : 'familyStatus'
								}, {
									'sTitle' : 'Children',
									'mData' : 'people'
									,'mRender' : getChildrenNames
								}, {
									'sTitle' : 'Address',
									'mData' : 'address',
									'mRender' : addressRender
								}, {
									'sTitle' : 'Phone Numbers',
									'mData' : 'phoneNumbers',
									'mRender' : setupPhoneNumbers
								}
								<sec:authorize access="hasRole('leader')">
								, {
									'sTitle' : 'Actions',
									'mData' : 'id'
									,'mRender' : setupActions
								} 
								</sec:authorize>
								],
								'oLanguage' : {
									'sInfoEmpty' : 'No families to show',
									'sEmptyTable' : 'There are no families assigned yet.  Add a family by clicking the button below.'
								}
							});
		}

		function setupModal() {
			$('#addFamily').on('hidden.bs.modal', function() {
				$('#familyForm')[0].reset();
				$('.form-group').removeClass('has-error');
			});

			$('#recordVisit').on('hidden.bs.modal', function() {
				$('#visitForm')[0].reset();
				$('.form-group').removeClass('has-error');
			});
		}

		function saveAssignment() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/companion/addAssignment"/>',
				data : $('#familyForm').serialize(),
				success : function(data) {
					handleSaveAssignment(data);
				}
			});
		}

		function handleSaveAssignment(data) {
			if (data.success) {
				showNotificationSuccess('This family has been successfully added to this companionship.');
				handleSaveAssignmentSuccess(data);
			} else {
				showModalError('<p>There was an unexpected error while adding the family to the companionship.  If the issue continues please contact your organization leader.');
			}
		}
		
		function handleSaveAssignmentSuccess(data){
			//clear form and hide modal
			$('#addFamily').modal('hide');
			//remove family from add family select
			$('#familySelect option').each(function() {
			    if ( $(this).val() == data.id) {
			        $(this).remove();
			    }
			});

			//add family row to table
			$('#assignmentTable').dataTable().fnAddData(data);
			//add family to visit dropdown
			$('#visitFamilySelect').append(new Option(data.familyName, data.id));
			
			//TODO add family to visit section
		}

		function removeAssignment($this) {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/companion/removeAssignment"/>',
				data : {
					'companionId' : $this.data('companionId'),
					'familyId' : $this.data('familyId')
				},
				success : function(data) {
					handleRemoveAssignment(data, $this);
				}
			});
		}
		
		function handleRemoveAssignment(data, $this){
			if (data.success) {
				showNotificationSuccess('This family has been successfully removed from this companionship.');
				handleRemoveAssignmentSuccess($this);
			} else {
				showModalError('<p>There was an unexpected error while removing the family from this companionship.  If the issue continues please contact your organization leader.');
			}
		}
		
		function handleRemoveAssignmentSuccess($this){
			$('#' + $this.data('familyId') + '-tab-link').remove();
			$('#' + $this.data('familyId') + '-tab').remove();
			var tr = $this.closest('tr')[0];
			//remove companion row from table
			$('#assignmentTable').dataTable().fnDeleteRow(tr);
		}
		
		function setupFamilyName(data, type, full){
			return getFamilyAndHeadNames(data, full.people);
		}
		
		function setupVisitActions(data, type, full){
			return '<input type="button" class="btn btn-primary editVisit" value="Edit" data-family-id="' + full.familyId + '" data-visit-id="' + data + '" data-assignment-id="' + full.assignmentId + '" data-visited="' + full.visited + '" data-visit-date="' + full.monthYear + '" data-notes="' + full.notes + '"/>';
		}
		
		function setupActions(data, type, full){
			return '<input type="button" class="btn btn-primary removeFamily" value="Remove" data-companion-id="' + '${companion.id}' + '" data-family-id="' + data + '" />';
		}
		
		function emailAssignments() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/email/byCompanion"/>',
				data : {
					'companionId' : '${companion.id}'
				},
				success : function(data) {
					if(data.success){
						showNotificationSuccess('An updated assignment email was successfully sent to this companionship.');
					} else {
						showNotificationError('There was an unexpected error while emailing this companionship.  Please verify that their email addresses are valid.  If the problem continues please contact the leader of your organization.');
					}
				}
			});
		}
	</script>
</t:mainPage>