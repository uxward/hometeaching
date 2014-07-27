<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<c:set var="teacherPrefix">
	<c:choose>
		<c:when test="${visitingTeaching}"></c:when>
		<c:otherwise>${organization.abbreviation}&nbsp;</c:otherwise>
	</c:choose>
</c:set>

<c:set var="teacherType">
	<c:choose>
		<c:when test="${visitingTeaching}">Visiting Teacher</c:when>
		<c:otherwise>Home Teacher</c:otherwise>
	</c:choose>
</c:set>

<c:set var="assignmentType">
	<c:choose>
		<c:when test="${visitingTeaching}">Sisters</c:when>
		<c:otherwise>Families</c:otherwise>
	</c:choose>
</c:set>

<t:mainPage activeMenu="${organization.name}" pageTitle="${teacherPrefix}${teacherType}s" pageHeader="All" pageSubheader="${teacherPrefix}${teacherType}s">

	<ul class="nav nav-tabs">
		<li class="active"><a href="#assignments" data-toggle="tab">Assignments</a></li>
		<li><a href="#unassignedTeachers" data-toggle="tab">Unassigned Teachers</a></li>
		<li><a href="#unassignedFamilies" data-toggle="tab">Unassigned ${assignmentType}</a></li>
	</ul>

	<div class="tab-content">
		<!-- start view assignments -->
		<div class="tab-pane active" id="assignments">
			<br />
			<table id="companionTable" class="table table-striped table-hover" width="100%">
			</table>

			<sec:authorize access="hasRole('leader')">

				<a href="#addCompanion" role="button" class="btn btn-primary" data-toggle="modal">Add Companion</a>
				<a href="#" class="btn" id="emailAssignments"> <i class="glyphicon glyphicon-envelope"></i> Email All Assignments
				</a>
				<a href="#" class="btn" id="emailReportUpdate"> <i class="glyphicon glyphicon-envelope"></i> Request Previous Month Update
				</a>
				<a href="#" class="btn" id="emailVisitReminder"> <i class="glyphicon glyphicon-envelope"></i> Email Visit Reminder
				</a>
			</sec:authorize>
		</div>
		<!-- end view assignments -->

		<!-- start view unassigned teachers -->
		<div class="tab-pane" id="unassignedTeachers">
			<br />
			<table id="unassignedTeacherTable" class="table table-striped table-hover" width="100%">
			</table>
		</div>
		<!-- end view unassigned teachers -->

		<!-- start view unassigned families -->
		<div class="tab-pane" id="unassignedFamilies">
			<br />
			<table id="unassignedFamilyTable" class="table table-striped table-hover" width="100%">
			</table>
		</div>
		<!-- end view unassigned families -->
	</div>



	<!-- Add companion modal -->
	<div id="addCompanion" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addFamilyLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="addFamilyLabel">Add Companion</h3>
				</div>

				<div class="modal-body">
					<form id="companionForm">
						<div class="form-group">
							<label class="sr-only" for="companion0">1st Companion</label> <select name="autopopulatingPersonCompanions[0].personId" class="companionSelect form-control" id="companion0">
								<option value="">Select ${teacherType}</option>
								<c:forEach items="${teachers}" var="teacher">
									<option value="${teacher.id}">${teacher.family.familyName},&nbsp;${teacher.firstName}&nbsp;</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label class="sr-only" for="companion1">2nd Companion</label> <select name="autopopulatingPersonCompanions[1].personId" class="companionSelect form-control" id="companion1">
								<option value="">Select ${teacherType}</option>
								<c:forEach items="${teachers}" var="teacher">
									<option value="${teacher.id}">${teacher.family.familyName},&nbsp;${teacher.firstName}&nbsp;</option>
								</c:forEach>
							</select>
						</div>
						<input type="hidden" name="organizationId" value="${organization.id}" />
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
					<button type="button" class="btn btn-primary" id="saveCompanion" data-loading-text="Saving...">Save Companion</button>
				</div>

			</div>
		</div>
	</div>

	<!-- Edit companion modal -->
	<div id="editCompanionModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="editCompanionLabel" aria-hidden="true" data-show="false">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="editCompanionLabel">Edit Companion</h3>
				</div>

				<div class="modal-body">
					<form id="editCompanionForm">
						<div class="form-group">
							<label class="sr-only" for="editFirstCompanion">1st Companion</label> <select name="autopopulatingPersonCompanions[0].personId" class="companionSelect form-control" id="editFirstCompanion">
								<option value="">Select ${teacherType}</option>
								<c:forEach items="${teachers}" var="teacher">
									<option value="${teacher.id}">${teacher.family.familyName},&nbsp;${teacher.firstName}&nbsp;</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label class="sr-only" for="editSecondCompanion">2nd Companion</label> <select name="autopopulatingPersonCompanions[1].personId" class="companionSelect form-control" id="editSecondCompanion">
								<option value="">Select ${teacherType}</option>
								<c:forEach items="${teachers}" var="teacher">
									<option value="${teacher.id}">${teacher.family.familyName},&nbsp;${teacher.firstName}&nbsp;</option>
								</c:forEach>
							</select>
						</div>
						<input type="hidden" name="organizationId" value="${organization.id}" /> <input type="hidden" name="id" id="editCompanionId" />
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
					<button type="button" class="btn btn-primary" id="editCompanion">Edit Companion</button>
				</div>

			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {

			setupEventBinding();

			setupCompanionTable();

		});

		function setupEventBinding() {
			$('#editCompanionModal').modal();

			$('#saveCompanion').click(function() {
				if (saveCompanionValid()) {
					saveCompanion();
				}
			});

			$('#companionTable').on('click', '.editCompanions', function() {
				setupEditCompanion($(this));
			});

			$('#editCompanion').click(function() {
				if (editCompanionValid()) {
					var tr = $(this).data('tr');
					editCompanion(tr[0]);
				}
			})

			$('#companionTable').on('click', '.removeCompanions', function() {
				if (confirm('Are you sure you want to remove this companionship?')) {
					var tr = $(this).closest('tr');
					removeCompanion($(this).data('companionId'), tr[0]);
				}
			});

			$('#emailAssignments').click(function() {
				if (confirm('Are you sure you want to email all assignments?')) {
					emailAssignments();
				}
			});

			$('#emailReportUpdate').click(function() {
				if (confirm('Are you sure you want to email all assignments?')) {
					emailReportUpdate();
				}
			});
			
			$('#emailVisitReminder').click(function(){
				if (confirm('Are you sure you want to email all assignments?')) {
					emailVisitReminder();
				}
			});
			
			//load tables when display tab
			$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
				if ($('#unassignedTeachers').is(':visible') && !$('#unassignedTeachers').data('loaded')) {
					setupUnassignedTeachersTable();
					$('#unassignedTeachers').data('loaded', true);
				} else if ($('#unassignedFamilies').is(':visible') && !$('#unassignedFamilies').data('loaded')){
					setupUnassignedFamiliesTable();
					$('#unassignedFamilies').data('loaded', true);
				}
			});
		}
		
		function setupUnassignedTeachersTable(){
			$('#unassignedTeacherTable').dataTable({
				'ajax' : '<spring:url value="/person/getUnassignedTeachers"/>/${organization.id}',
				'data' : [],
				'sorting' : [ [ 0, 'asc' ] ],
				'columns' : [ {
					'title' : 'Name',
					'data' : 'fullName'
				}, {
					'title' : 'Address',
					'data' : 'family.address',
					'render' : addressRender
				}, {
					'title' : 'Phone Number',
					'data' : 'phoneNumber',
					'render' : getPhoneNumber
				}]
			});
		}
		
		function setupUnassignedFamiliesTable(){
			$('#unassignedFamilyTable').dataTable({
				'ajax' : '<spring:url value="/family/getUnassignedFamilies"/>/${organization.id}',
				'data' : [],
				'sorting' : [ [ 0, 'asc' ] ],
				'columns' : [ {
					'title' : 'Family',
					'data' : 'teachers',
					'render' : setupTeachers
				}, {
					'title' : 'Status',
					'data' : 'assignments',
					'render' : setupNumFamilies
				}, {
					'title' : 'Address',
					'data' : 'assignments',
					'render' : setupAssignments
				}]
			});
		}
		
		/*
		*
		* Save and edit companion
		*
		*/

		function saveCompanionValid() {
			return companionValid('#companionForm');
		}

		function editCompanionValid() {
			return companionValid('#editCompanionForm');
		}

		function companionValid(formId) {
			var valid = true;
			var companionIds = [];
			$(formId + ' .companionSelect').each(function() {
				var companionId = $(this).val();
				if(companionId != '' && companionId != null){
					companionIds.push(companionId);
				}
			});
			
			if(companionIds.length < 1){
				valid = false;
				showModalError('You must select at least one companion to save this assignment.');
			} else if(companionIds.length > 1 && companionIds[0] == companionIds[1]){
				valid = false;
				showModalError('You cannot select the same person twice for the companionship.');
			} 
			
			return valid;
		}

		function saveCompanion() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/companion/save"/>',
				data : $('#companionForm').serialize(),
				success : function(data) {
					handleSaveCompanion(data);
				}
			});
		}
		
		function handleSaveCompanion(data){
			if (data.success) {
				handleSaveCompanionSuccess(data);
				showNotificationSuccess('This companionship has been successfully created.');
			} else {
				showModalError('<p>There was an unexpected error while saving this companionship.  If the issue continues please contact your organization leader.');
			}
		}
		
		function handleSaveCompanionSuccess(data){
			
			//clear form and hide modal
			$('#addCompanion').modal('hide');
			$('#companionForm')[0].reset();

			//add companion row to table
			addCompanionToTable(data);
		}

		function editCompanion(tr) {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/companion/edit"/>',
				data : $('#editCompanionForm').serialize(),
				success : function(data) {
					handleEditCompanion(data, tr);
				}
			});
		}
		
		function handleEditCompanion(data, tr){
			if (data.success) {
				handleEditCompanionSuccess(data, tr);
				showNotificationSuccess('This companionship has been successfully edited.');
			} else {
				showModalError('<p>There was an unexpected error while editing this companionship.  If the issue continues please contact your organization leader.');
			}
		}
		
		function handleEditCompanionSuccess(data, tr){
			//clear form and hide modal
			$('#editCompanionModal').modal('hide');
			$('#editCompanionForm')[0].reset();

			//remove old companion row from table
			$('#companionTable').dataTable().fnDeleteRow(tr);

			//add companion row to table
			addCompanionToTable(data);
		}

		function addCompanionToTable(data) {
			$('#companionTable').dataTable().fnAddData(data);
		}
		
		function setupEditCompanion($this){
			$('#editFirstCompanion').val($this.data('firstPersonId'));
			$('#editSecondCompanion').val($this.data('secondPersonId'));
			$('#editCompanionId').val($this.data('companionId'));
			$('#editCompanion').data('tr', $this.closest('tr'));
			$('#editCompanionModal').modal('show');
		}

		function removeCompanion(companionId, tr) {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/companion/remove"/>',
				data : {
					'companionId' : companionId
				},
				success : function(data) {
					handleRemoveCompanion(data, tr);
				}
			});
		}
		
		function handleRemoveCompanion(data, tr){
			if (data.success) {
				//remove companion row from table
				$('#companionTable').dataTable().fnDeleteRow(tr);
				showNotificationSuccess('This companionship has been successfully removed.');
			} else {
				showModalError('<p>There was an unexpected error while removing this companionship.  If the issue continues please contact your organization leader.');
			}
		}
		
		/*
		*
		* Companion table
		*
		*/

		function setupCompanionTable() {
			$('#companionTable').dataTable({
				'sAjaxSource' : '<spring:url value="/companion/getAll"/>/${organization.id}',
				'aaData' : [],
				'aaSorting' : [ [ 0, 'asc' ] ],
				'aoColumns' : [ {
					'sTitle' : 'Home Teachers',
					'sWidth' : '25%',
					'mData' : 'teachers',
					'mRender' : setupTeachers
				}, {
					'sTitle' : 'Assigned Families',
					'sWidth' : '45%',
					'mData' : 'assignments',
					'mRender' : setupAssignments
				}, {
					'sTitle' : '# Fams.',
					'sWidth' : '10%',
					'mData' : 'assignments',
					'mRender' : setupNumFamilies
				}
				<sec:authorize access="hasRole('leader')">
					, {
						'sTitle' : 'Actions',
						'sWidth' : '20%',
						'mData' : 'id',
						'mRender' : setupActions
					}
				</sec:authorize>
				]
			});
		}

		function setupTeachers(data, type, full) {
			var names = '';
			for (var i = 0; i < data.length; i++) {
				if (i != 0) {
					names += ' and ';
				}
				names += data[i].fullName;
			}
			return '<a href="<spring:url value="/companion/detail/' + full.id + '"/>">' + names + '</a>';
		}

		function setupAssignments(data, type, full) {
			var html = '';
			for (var i = 0; i < data.length; i++) {
				if (i != 0) {
					html += '; ';
				}
				html += data[i].familyName + ', ' + (${visitingTeaching} ? data[i].womenHeadOfHousehold : data[i].headOfHousehold);
			}
			return html;
		}
		
		function setupNumFamilies(data, type, full){
			return data.length;
		}

		function setupActions(data, type, full) {
			var firstPersonId = full.teachers[0].id;
			var secondPersonId = full.teachers[1] != null ? full.teachers[1].id : null;
			var actions = '<input type="button" class="btn btn-primary editCompanions button-medium" value="Edit" data-companion-id="' + full.id + '" data-first-person-id="' + firstPersonId + '"data-second-person-id="' + secondPersonId + '" /> '
					+ '<input type="button" class="btn btn-primary removeCompanions button-medium" value="Remove" data-companion-id="' + full.id + '" />';
			return actions;
		}
		
		/*
		*
		* Email
		*
		*/

		function emailAssignments() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/email/updatedAssignment"/>/${organization.id}',
				success : function(data) {
					if(data.success){
						showNotificationSuccess('An updated assignment email was successfully sent to all companionships.');
					} else {
						showNotificationError('There was an unexpected error while emailing at least one companionship.  Please verify that their email addresses are valid.  If the problem continues please contact the leader of your organization.');
					}
				}
			});
		}

		function emailReportUpdate() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/email/reportUpdate"/>/${organization.id}',
				success : function(data) {
					if(data.success){
						showNotificationSuccess('An email requesting a teaching report was successfully sent to all companionships.');
					} else {
						showNotificationError('There was an unexpected error while emailing at least one companionship.  Please verify that their email addresses are valid.  If the problem continues please contact the leader of your organization.');
					}
				}
			});
		}

		function emailVisitReminder() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/email/visitReminder"/>/${organization.id}',
				success : function(data) {
					if(data.success){
						showNotificationSuccess('A visit reminder email was successfully sent to all companionships.');
					} else {
						showNotificationError('There was an unexpected error while emailing at least one companionship.  Please verify that their email addresses are valid.  If the problem continues please contact the leader of your organization.');
					}
				}
			});
		}
	</script>
</t:mainPage>