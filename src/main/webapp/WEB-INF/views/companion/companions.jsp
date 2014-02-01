<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:mainPage activeMenu="allCompanions" pageTitle="Companion - Home" pageHeader="Companion" pageSubheader="Home">

	<table id="companionTable" class="table table-striped table-hover table-bordered">
	</table>

	<a href="#addCompanion" role="button" class="btn btn-primary" data-toggle="modal">Add Companion</a>

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
							<select name="hometeachers[0].id" class="companionSelect form-control" id="companion0">
								<option value="">Select Home Teacher</option>
								<c:forEach items="${hometeachers}" var="hometeacher">
									<option value="${hometeacher.id}" class="${empty hometeacher.activeCompanion ? 'notCompanion' : 'alreadyCompanion'}">${hometeacher.firstName}&nbsp;${hometeacher.family.familyName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label class="sr-only" for="companion1">2nd Companion</label>
							<select name="hometeachers[1].id" class="companionSelect form-control" id="companion1">
								<option value="">Select Home Teacher</option>
								<c:forEach items="${hometeachers}" var="hometeacher">
									<option value="${hometeacher.id}" class="${empty hometeacher.activeCompanion ? 'notCompanion' : 'alreadyCompanion'}">${hometeacher.firstName}&nbsp;${hometeacher.family.familyName}</option>
								</c:forEach>
							</select>
						</div>
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
					<button type="button" class="btn btn-primary" id="saveCompanion">Save Companion</button>
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
							<select name="hometeachers[0].id" class="companionSelect form-control" id="editFirstCompanion">
								<option value="">Select Home Teacher</option>
								<c:forEach items="${hometeachers}" var="hometeacher">
									<option value="${hometeacher.id}" class="${hometeacher.activeCompanion ? 'alreadyCompanion' : 'notCompanion'}">${hometeacher.firstName}&nbsp;${hometeacher.family.familyName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label class="sr-only" for="editSecondCompanion">2nd Companion</label>
							<select name="hometeachers[1].id" class="companionSelect form-control" id="editSecondCompanion">
								<option value="">Select Home Teacher</option>
								<c:forEach items="${hometeachers}" var="hometeacher">
									<option value="${hometeacher.id}" class="${hometeacher.activeCompanion ? 'alreadyCompanion' : 'notCompanion'}">${hometeacher.firstName}&nbsp;${hometeacher.family.familyName}</option>
								</c:forEach>
							</select>
						</div>
						<input type="hidden" name="id" id="editCompanionId" />
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
				if (companionValid()) {
					saveCompanion();
				}
			});

			$('#companionTable').on('click', '.editCompanions', function() {
				$('#editFirstCompanion').val($(this).data('firstPersonId'));
				$('#editSecondCompanion').val($(this).data('secondPersonId'));
				$('#editCompanionId').val($(this).data('companionId'));
				$('#editCompanionModal').modal('show');
			});

			$('#editCompanion').click(function() {
				if (editCompanionValid()) {
					var tr = $(this).closest('tr');
					editCompanion(tr[0]);
				}
			})

			$('#companionTable').on('click', '.removeCompanions', function() {
				if (confirm('Are you sure you want to remove this companionship?')) {
					var tr = $(this).closest('tr');
					removeCompanion($(this).data('companionId'), tr[0]);
				}
			});
		}

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
				}
				if (valid) {
					if (companionIds.indexOf($(this).val()) > -1) {
						valid = false;
					} else {
						companionIds.push($(this).val());
					}
				}

			});
			return valid;
		}

		function setupCompanionTable() {

			$('#companionTable').dataTable({
				'sAjaxSource' : '<spring:url value="/companion/getCompanions"/>',
				'aaData' : [],
				'aaSorting' : [ [ 0, 'desc' ] ],
				'aoColumns' : [ {
					'bVisible' : false,
					'mData' : 'id'
				}, {
					'sTitle' : 'Home Teachers',
					'mData' : 'hometeachers',
					'mRender' : setupHometeachers
				}, {
					'sTitle' : 'Assigned Families',
					'mData' : 'assignments',
					'mRender' : setupAssignments
				}, {
					'sTitle' : 'Actions',
					'mData' : 'id',
					'mRender' : setupActions
				} ]
			});
		}

		function setupHometeachers(data, type, full) {
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
			console.log(data);
			var html = '';
			for (var i = 0; i < data.length; i++) {
				if (i != 0) {
					html += '; ';
				}
				html += data[i].familyName + ', ' + data[i].headOfHousehold;
			}
			return html;
		}

		function setupActions(data, type, full) {
			var firstPersonId = full.hometeachers[0].id;
			var secondPersonId = full.hometeachers[1].id;

			return '<input type="button" class="btn btn-primary editCompanions button-medium" value="Edit"'
					+ 'data-companion-id="' + full.id + '" data-first-person-id="' + firstPersonId + '"data-second-person-id="' + secondPersonId + '" /> '
					+ '<input type="button" class="btn btn-primary removeCompanions button-medium" value="Remove" data-companion-id="' + full.id + '" />';
		}

		function saveCompanion() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/companion/save"/>',
				data : $('#companionForm').serialize(),
				success : function(data) {
					console.log(data);
					//clear form and hide modal
					$('#addCompanion').modal('hide');
					$('#companionForm')[0].reset();

					//add companion row to table
					addCompanionToTable(data);
				}
			});
		}

		function editCompanion(tr) {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/companion/edit"/>',
				data : $('#editCompanionForm').serialize(),
				success : function(data) {
					console.log(data);
					//clear form and hide modal
					$('#addCompanion').modal('hide');
					$('#companionForm')[0].reset();

					//remove old companion row from table
					$('#companionTable').dataTable().fnDeleteRow(tr);

					//add companion row to table
					addCompanionToTable(data);
				}
			});
		}

		function addCompanionToTable(data) {
			$('#companionTable').dataTable().fnAddData(data);
		}

		function removeCompanion(companionId, tr) {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/companion/remove"/>',
				data : {
					'companionId' : companionId
				},
				success : function(data) {

					//remove companion row from table
					$('#companionTable').dataTable().fnDeleteRow(tr);
				}
			});
		}
	</script>
</t:mainPage>