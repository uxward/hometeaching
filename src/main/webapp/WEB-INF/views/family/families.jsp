<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:familyStatus activeMenu="allFamilies" pageTitle="Families" pageHeader="All" pageSubheader="Families">

	<table id="familyTable" class="table table-striped table-hover table-bordered" width="100%"></table>

	<a href="#addFamily" class="btn btn-primary" data-toggle="modal">Add Family</a>

	<!-- Add Family Modal -->
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
							<label class="sr-only" for="familyName">Family Name</label>
							<input class="form-control" type="text" id="familyName" name="familyName" placeholder="Family Name" maxlength="30" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="address">Address</label>
							<input class="form-control" type="text" id="address" name="address" placeholder="Address" maxlength="100" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="phoneNumber">Phone Number</label>
							<input class="form-control" type="text" id="phoneNumber" name="phoneNumber" placeholder="Phone Number" maxlength="20" />
						</div>
						<div class="checkbox">
							<label class="checkbox">
								<input type="checkbox" name="moved" id="moved"> Moved
							</label>
						</div>
						<div class="checkbox">
							<label class="checkbox">
								<input type="checkbox" name="partMember" id="partMember"> Part Member
							</label>
						</div>
						<div class="form-group">
							<label class="sr-only" for="familyStatus">Family Status</label>
							<select name="familyStatusId" class="form-control" id="familyStatus">
								<option value="">Select Status</option>
								<c:forEach items="${statuses}" var="status">
									<option value="${status.id}">${status.status}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label class="sr-only" for="organization">Organization</label>
							<select name="familyOrganizationIds" class="form-control" id="familyOrganization" multiple="multiple">
								<option value="">Select Organization</option>
								<c:forEach items="${organizations}" var="organization">
									<option value="${organization.id}">${organization.name}</option>
								</c:forEach>
							</select>
						</div>
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-primary" id="saveFamily">Save Family</button>
				</div>

			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			setupEventBinding();

			setupFamilyTable();

		});

		function setupEventBinding() {
			$('#saveFamily').click(function() {
				if (canSaveFamily()) {
					saveFamily();
				}
			});
		}

		function setupFamilyTable() {

			$('#familyTable').dataTable({
				'sAjaxSource' : '<spring:url value="/family/getAllFamilies/"/>',
				'aaSorting' : [ [ 0, 'asc' ] ],
				'aaData' : [],
				'aLengthMenu' : [ [ 10, 25, 50, 100, -1 ], [ 10, 25, 50, 100, 'All' ] ],
				'aoColumns' : [ {
					'sTitle' : 'Family Name',
					'mData' : 'familyName',
					'sWidth' : '16%',
					'mRender' : familyNameRender
				}, {
					'sTitle' : 'Status',
					'mData' : 'familyStatus',
					'sWidth' : '12%'
				}, {
					'sTitle' : 'Organization',
					'mData' : 'organizations',
					'mRender' : setupOrganizations,
					'sWidth' : '15%'
				}, {
					'sTitle' : 'Address',
					'mData' : 'address',
					'sWidth' : '15%',
					'mRender' : addressRender,
					'sClass' : 'hidden-xs'
				}, {
					'sTitle' : 'Phone Numbers',
					'mData' : 'phoneNumbers',
					'sWidth' : '16%',
					'sClass' : 'hidden-xs hidden-sm',
					'mRender' : setupPhoneNumbers
				}, {
					'sTitle' : 'Home Teachers',
					'mData' : 'homeTeachingCompanions',
					'sWidth' : '13%',
					'mRender' : setupTeachers
				}, {
					'sTitle' : 'Visiting Teachers',
					'mData' : 'visitingTeachingCompanions',
					'sWidth' : '13%',
					'mRender' : setupTeachers
				} ],
				'oLanguage' : {
					'sInfoEmpty' : 'No families to show',
					'sEmptyTable' : 'There are no families yet.  Add a family by clicking the button below.'
				}
			});
		}

		function familyNameRender(data, type, full) {
			return '<a href="<spring:url value="/family/detail/"/>' + full.id + '">' + getFamilyAndHeadNames(data, full.people) + '</a>';
		}

		function setupTeachers(data, type, full) {
			var html = '';
			if (data != null) {
				html = '<a href="<spring:url value="/companion/detail/"/>' + data.id + '">' + data.allTeachers + '</a>';
			}
			return html;
		}

		function canSaveFamily() {
			var valid = true;

			var $familyName = $('#familyName');
			if ($.trim($familyName.val()).length < 1) {
				valid = false;
				$familyName.parent().addClass('has-error');
			} else {
				$familyName.parent().removeClass('has-error');
			}

			var $status = $('#familyStatus');
			if ($.trim($status.val()).length < 1) {
				valid = false;
				$status.parent().addClass('has-error');
			} else {
				$status.parent().removeClass('has-error');
			}

			var $organization = $('#familyOrganization');
			if ($.trim($organization.val()).length < 1) {
				valid = false;
				$organization.parent().addClass('has-error');
			} else {
				$organization.parent().removeClass('has-error');
			}

			return valid;
		}

		function saveFamily() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/family/save"/>',
				data : $('#familyForm').serialize(),
				success : function(data) {
					//clear form and hide modal
					$('#addFamily').modal('hide');
					$('#familyForm')[0].reset();

					//add family row to table
					addFamilyToTable(data);
				}
			});
		}

		function addFamilyToTable(data) {
			$('#familyTable').dataTable().fnAddData(data);
		}
	</script>
</t:familyStatus>