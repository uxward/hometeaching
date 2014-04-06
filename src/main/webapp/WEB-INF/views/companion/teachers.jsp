<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<t:mainPage activeMenu="allTeachers${organization.id}" pageTitle="Home Teachers" pageHeader="All" pageSubheader="Home Teachers">

	<table id="companionTable" class="table table-striped table-hover table-bordered" width="100%">
	</table>

	<sec:authorize access="hasRole('leader')">

		<a href="#addCompanion" role="button" class="btn btn-primary" data-toggle="modal">Add Companion</a>
		<a href="#" class="btn" id="emailAssignments">
			<i class="glyphicon glyphicon-envelope"></i> Email All Assignments
		</a>

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
								<label class="sr-only" for="companion0">1st Companion</label>
								<select name="autopopulatingPersonCompanions[0].personId" class="companionSelect form-control" id="companion0">
									<option value="">Select Home Teacher</option>
									<c:forEach items="${teachers}" var="teacher">
										<option value="${teacher.id}">${teacher.firstName}&nbsp;${teacher.family.familyName}&nbsp;-&nbsp;${teacher.family.familyStatus}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label class="sr-only" for="companion1">2nd Companion</label>
								<select name="autopopulatingPersonCompanions[1].personId" class="companionSelect form-control" id="companion1">
									<option value="">Select Home Teacher</option>
									<c:forEach items="${teachers}" var="teacher">
										<option value="${teacher.id}">${teacher.firstName}&nbsp;${teacher.family.familyName}&nbsp;-&nbsp;${teacher.family.familyStatus}</option>
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
								<label class="sr-only" for="editFirstCompanion">1st Companion</label>
								<select name="autopopulatingPersonCompanions[0].personId" class="companionSelect form-control" id="editFirstCompanion">
									<option value="">Select Home Teacher</option>
									<c:forEach items="${teachers}" var="teacher">
										<option value="${teacher.id}">${teacher.firstName}&nbsp;${teacher.family.familyName}&nbsp;-&nbsp;${teacher.family.familyStatus}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label class="sr-only" for="editSecondCompanion">2nd Companion</label>
								<select name="autopopulatingPersonCompanions[1].personId" class="companionSelect form-control" id="editSecondCompanion">
									<option value="">Select Home Teacher</option>
									<c:forEach items="${teachers}" var="teacher">
										<option value="${teacher.id}">${teacher.firstName}&nbsp;${teacher.family.familyName}&nbsp;-&nbsp;${teacher.family.familyStatus}</option>
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

	</sec:authorize>

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
				if ($(this).val() == null || $(this).val() == '') {
					valid = false;
					showModalError('You must select both companions to save this assignment.');
				}
				if (valid) {
					if (companionIds.indexOf($(this).val()) > -1) {
						valid = false;
						showModalError('You cannot select the same person twice for the companionship.');
					} else {
						companionIds.push($(this).val());
					}
				}

			});
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
			//remove perople from dropdown
			var firstPersonId = data.teachers[0].id;
			var secondPersonId = data.teachers[1].id;
			$('.companionSelect option').each(function() {
			    if ( $(this).val() == firstPersonId || $(this).val() == secondPersonId ) {
			        $(this).remove();
			    }
			});
			
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
					handleEditCompanion(data);
				}
			});
		}
		
		function handleEditCompanion(data){
			if (data.success) {
				handleEditCompanionSuccess(data);
				showNotificationSuccess('This companionship has been successfully edited.');
			} else {
				showModalError('<p>There was an unexpected error while editing this companionship.  If the issue continues please contact your organization leader.');
			}
		}
		
		function handleEditCompanionSuccess(data){
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
				'sAjaxSource' : '<spring:url value="/companion/getAllCompanions"/>/${organization.id}',
				'aaData' : [],
				'aaSorting' : [ [ 0, 'asc' ] ],
				'aoColumns' : [ {
					'sTitle' : 'Home Teachers',
					'sWidth' : '25%',
					'mData' : 'teachers',
					'mRender' : setupTeachers
				}, {
					'sTitle' : '# Families',
					'sWidth' : '10%',
					'mData' : 'assignments',
					'mRender' : setupNumFamilies
				}, {
					'sTitle' : 'Assigned Families',
					'sWidth' : '45%',
					'mData' : 'assignments',
					'mRender' : setupAssignments
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
			return '<a href="<spring:url value="/companion/homeTeachingDetail/' + full.id + '"/>">' + names + '</a>';
		}

		function setupAssignments(data, type, full) {
			var html = '';
			for (var i = 0; i < data.length; i++) {
				if (i != 0) {
					html += '; ';
				}
				html += data[i].familyName + ', ' + data[i].headOfHousehold;
			}
			return html;
		}
		
		function setupNumFamilies(data, type, full){
			return data.length;
		}

		function setupActions(data, type, full) {
			var firstPersonId = full.teachers[0].id;
			var secondPersonId = full.teachers[1].id;
			var actions = ''
				<sec:authorize access="hasRole('admin')">
					+'<input type="button" class="btn btn-primary editCompanions button-medium" value="Edit"'
					+ 'data-companion-id="' + full.id + '" data-first-person-id="' + firstPersonId + '"data-second-person-id="' + secondPersonId + '" /> '
				</sec:authorize>
					+ '<input type="button" class="btn btn-primary removeCompanions button-medium" value="Remove" data-companion-id="' + full.id + '" />';
			return actions;
		}
		
		/*
		*
		* Email assignments
		*
		*/

		function emailAssignments() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/email/allCompanions"/>',
				success : function(data) {
					if(data.success){
						showNotificationSuccess('An updated assignment email was successfully sent to all companionships.');
					} else {
						showNotificationError('There was an unexpected error while emailing at least one companionship.  Please verify that their email addresses are valid.  If the problem continues please contact the leader of your organization.');
					}
				}
			});
		}
	</script>
</t:mainPage>