<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<t:mainPage activeMenu="yourFamily"
	pageTitle="${family.familyName} Family"
	pageHeader="${family.familyName}" pageSubheader="Family">

	<div class="well" id="viewInfo">
		<p id="viewAddress">
			<strong>Address:</strong> ${family.address}
		</p>
		<p id="viewPhone">
			<strong>Phone Number:</strong> ${family.phoneNumber}
		</p>
		<p id="viewStatus"
			class="<sec:authorize access="!hasRole('admin')">hidden</sec:authorize>">
			<strong>Status:</strong> ${family.familyStatus}
		</p>
		<p id="viewOrganization">
			<strong>Organization:</strong>
			<c:forEach items="${family.organizations}" var="organization"
				varStatus="status">${organization.organization}<c:if
					test="${!status.last}">, </c:if>
			</c:forEach>
		</p>
		<p id="viewHometeachers">
			<strong>Home teachers:</strong>
			<c:if test="${not empty family.companions}">
				<c:forEach items="${family.companions.hometeachers}" var="person"
					varStatus="status">${person.firstName}&nbsp;${person.family.familyName}<c:if
						test="${!status.last}">, </c:if>
				</c:forEach>
			</c:if>
		</p>
	</div>

	<a href="#editFamilyModal" id="openFamilyModal" role="button"
		class="btn btn-primary" data-family-id="${family.id}"
		data-toggle="modal">Edit Family</a>

	<br />
	<br />

	<fieldset>
		<legend>Family Members</legend>

		<table id="personTable"
			class="table table-striped table-hover table-bordered">
		</table>

		<br /> <a href="#addPerson" role="button" class="btn btn-primary"
			data-toggle="modal">Add Family Member</a>

	</fieldset>

	<!-- Add Person Modal -->
	<div id="addPerson" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="addPersonLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="addPersonLabel">Add Family Member</h3>
				</div>

				<div class="modal-body">
					<form id="personForm">
						<div class="form-group">
							<label class="sr-only" for="firstName">First Name</label> <input
								class="form-control" type="text" name="firstName" id="firstName"
								placeholder="First Name" maxlength="30" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="firstName">Email</label> <input
								class="form-control" type="text" name="email" id="email"
								placeholder="Email" maxlength="30" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="firstName">Phone Number</label> <input
								class="form-control" type="text" name="phoneNumber"
								id="phoneNumber" placeholder="Phone Number" maxlength="20" />
						</div>
						<div class="form-group">
							<label class="checkbox"> <input type="checkbox"
								name="female" id="female"> Female
							</label>
						</div>
						<div class="form-group">
							<label class="checkbox"> <input type="checkbox"
								name="headOfHousehold" id="headOfHousehold"> Head of
								Household
							</label>
						</div>
						<div
							class="form-group <sec:authorize access="!hasRole('leader')">hidden</sec:authorize>">
							<label class="checkbox"> <input type="checkbox"
								name="hometeacher" id="hometeacher"> Home Teacher
							</label>
						</div>
						<div
							class="form-group <sec:authorize access="!hasRole('leader')">hidden</sec:authorize>">
							<label class="sr-only" for="organization">Organization</label> <select
								name="organizationId" class="form-control"
								id="personOrganization">
								<option value="">Select Organization</option>
								<c:forEach items="${organizations}" var="organization">
									<option value="${organization.id}">${organization.organization}</option>
								</c:forEach>
							</select>
						</div>
						<input type="hidden" value="${family.id}" name="familyId" /> <input
							type="hidden" id="personId" name="id" />
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						aria-hidden="true">Cancel</button>
					<button type="button" class="btn btn-primary" id="savePerson"
						data-action="save">Save Family Member</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Edit Family Modal -->
	<div id="editFamilyModal" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="editFamilyLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="editFamilyLabel">Edit Family</h3>
				</div>

				<div class="modal-body">
					<form id="familyForm">
						<div class="form-group">
							<label class="sr-only" for="familyName">Family Name</label> <input
								class="form-control" type="text" name="familyName"
								placeholder="Family Name" id="familyName" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="address">Address</label> <input
								class="form-control" type="text" name="address"
								placeholder="Address" id="address" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="phoneNumber">Phone Number</label> <input
								class="form-control" type="text" name="phoneNumber"
								placeholder="Phone Number" id="familyPhoneNumber" />
						</div>
						<div
							class="form-group <sec:authorize access="!hasRole('leader')">hidden</sec:authorize>">
							<label class="checkbox"> <input type="checkbox"
								name="familyMoved" id="familyMoved"> Family Moved
							</label>
						</div>

						<div
							class="form-group <sec:authorize access="!hasRole('leader')">hidden</sec:authorize> <c:if test="${!family.familyMoved}">hidden</c:if>">
							<label class="checkbox"> <input type="checkbox"
								name="recordsMoved" id="recordsMoved"> Records Moved
							</label>
						</div>
						<div
							class="form-group <sec:authorize access="!hasRole('leader')">hidden</sec:authorize>">
							<label class="checkbox"> <input type="checkbox"
								name="partMember" id="partMember"> Part Member
							</label>
						</div>

						<div
							class="form-group <sec:authorize access="!hasRole('leader')">hidden</sec:authorize>">
							<label class="sr-only" for="familyStatus">Family Status</label> <select
								name="familyStatusId" class="form-control" id="familyStatus">
								<option value="">Select Status</option>
								<c:forEach items="${statuses}" var="status">
									<option value="${status.id}">${status.status}</option>
								</c:forEach>
							</select>
						</div>

						<div
							class="form-group <sec:authorize access="!hasRole('leader')">hidden</sec:authorize>">
							<label class="sr-only" for="organization">Organization</label> <select
								name="familyOrganizationIds" class="form-control"
								id="familyOrganization" multiple="multiple">
								<option value="">Select Organization</option>
								<c:forEach items="${organizations}" var="organization">
									<option value="${organization.id}">${organization.organization}</option>
								</c:forEach>
							</select>
						</div>
						<input type="hidden" value="${family.id}" name="id" />
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						aria-hidden="true">Cancel</button>
					<button type="button" class="btn btn-primary" id="editFamily">Save
						Family</button>
				</div>

			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {

			setupEventBinding();

			setupPersonTable();

			setupModals();

			setupViewInfo();
		});

		function setupEventBinding() {
			$('#savePerson').click(function() {
				if (canSavePerson()) {
					savePerson($(this));
				}
			});

			$('#editFamily').click(function() {
				if (canEditFamily()) {
					editFamily();
				}
			});

			$('#personTable').on('click', '.firstName', function() {
				editPerson($(this));
			});
		}

		function setupPersonTable() {
			$('#personTable').dataTable({
				'sDom' : 't',
				'sAjaxSource' : '<spring:url value="/person/getByFamily/${family.id}"/>',
				'aaData' : [],
				'aaSorting' : [ [ 8, 'desc' ], [ 1, 'asc' ] ],
				'aoColumns' : [ {
					'mData' : 'id',
					'bVisible' : false
				}, {
					'sTitle' : 'First Name',
					'mData' : 'firstName',
					'mRender' : setupFirstNameLink
				}, {
					'sTitle' : 'Email',
					'mData' : 'email'
				}, {
					'sTitle' : 'Phone Number',
					'mData' : 'phoneNumber',
					'mRender' : getPhoneNumber
				}, {
					'sTitle' : 'Organization',
					'mData' : 'organization',
					'mRender' : setupOrganization
				}, {
					'sTitle' : 'Gender',
					'mData' : 'female',
					'mRender' : maleFemale,
					'sClass' : 'hidden-xs hidden-sm'
				}, {
					'sTitle' : 'Home Teacher',
					'mData' : 'hometeacher',
					'mRender' : setupTrueFalseAsYesNo,
					'sClass' : 'hidden-xs'
				}, {
					'sTitle' : 'Head of Household',
					'mData' : 'headOfHousehold',
					'mRender' : setupTrueFalseAsYesNo,
					'sClass' : 'hidden-xs  hidden-sm'
				} ],
				'oLanguage' : {
					'sInfoEmpty' : 'No people to show',
					'sEmptyTable' : 'There are no people in this family yet.  Add a person by clicking the button below.'
				}
			});
		}

		function setupModals() {
			$('#addPerson').on('show.bs.modal, hidden.bs.modal', function() {
				$('#personForm')[0].reset();
				$('.form-group').removeClass('has-error');
			});

			$('#editFamilyModal').on('show.bs.modal', function() {
				getFamilyInformation();
			}).on('hidden.bs.modal', function() {
				$('#familyForm')[0].reset();
				$('.form-group').removeClass('has-error');
			});
		}

		function setupViewInfo() {
			$('#viewAddress').html('<strong>Address:</strong> ' + addressRender('${family.address}'));
			$('#viewPhone').html('<strong>Phone Number:</strong> ' + getPhoneNumber('${family.phoneNumber}'));
		}

		function setupFirstNameLink(data, type, full) {
			return '<a href="#" class="firstName" data-person-id="' + full.id + '">' + data + '</a>';
		}

		function maleFemale(data, type, full) {
			return (data ? 'Female' : 'Male');
		}

		function setupHometeacher(data, type, full) {
			return (data ? 'Yes' : 'No');
		}

		function setupOrganization(data, type, full) {
			var html = '';
			if (data != null) {
				html = data.organization;
			}
			return html;
		}

		function editPerson($this) {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/person/get"/>',
				data : {
					'personId' : $this.data('personId')
				},
				success : function(data) {
					setupEditPerson(data, $this);
				}
			});
		}

		function setupEditPerson(data, $this) {
			$('#addPersonLabel').text('Edit Family Member');
			$('#firstName').val(data.firstName);
			$('#email').val(data.email);
			$('#phoneNumber').val(data.phoneNumber);
			$('#personId').val(data.id);
			if (data.organization != null) {
				$('#personOrganization').val(data.organization.id);
			}

			if (data.female) {
				$('#female').prop('checked', true);
			}
			if (data.headOfHousehold) {
				$('#headOfHousehold').prop('checked', true);
			}
			if (data.hometeacher) {
				$('#hometeacher').prop('checked', true);
			}
			var row = $('#personTable').dataTable().fnGetPosition($this.closest('tr')[0]);

			$('#savePerson').data('action', 'edit').data('row', row);
			$('#addPerson').modal('show');
		}

		function canSavePerson() {
			var valid = true;

			var $firstName = $('#firstName');
			if ($.trim($firstName.val()).length < 1) {
				valid = false;
				$firstName.parent().addClass('has-error');
			} else {
				$firstName.parent().removeClass('has-error');
			}

			var $organization = $('#personOrganization');
			if ($('#headOfHousehold').is(':checked') && $.trim($organization.val()).length < 1) {
				valid = false;
				$organization.parent().addClass('has-error');
			} else {
				$organization.parent().removeClass('has-error');
			}

			return valid;
		}

		function savePerson($this) {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/person/save"/>',
				data : $('#personForm').serialize(),
				success : function(data) {
					handleSaveSuccess(data, $this);
				}
			});
		}

		function handleSaveSuccess(data, $this) {
			//clear form and hide modal
			$('#addPerson').modal('hide');

			//add family row to table
			if ($this.data('action') != 'edit') {
				addPersonToTable(data);
			} else {
				updatePersonInTable(data, $this);
			}
			$this.data('action', 'save');
		}

		function addPersonToTable(data) {
			$('#personTable').dataTable().fnAddData(data);
		}

		function updatePersonInTable(data, $this) {
			$('#personTable').dataTable().fnUpdate(data, $this.data('row'));
		}

		function getFamilyInformation() {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/family/get"/>',
				data : {
					'familyId' : $('#openFamilyModal').data('familyId')
				},
				success : function(data) {
					setupFamilyModal(data);
				}
			});
		}

		function setupFamilyModal(data) {
			$('#familyName').val(data.familyName);
			$('#address').val(data.address);
			$('#familyPhoneNumber').val(data.phoneNumber);

			if (data.familyMoved) {
				$('#familyMoved').prop('checked', true);
			}
			if (data.recordsMoved) {
				$('#recordsMoved').prop('checked', true);
			}
			if (data.partMember) {
				$('#partMember').prop('checked', true);
			}

			$("#familyStatus option").each(function() {
				if ($(this).text() == data.familyStatus) {
					$(this).attr('selected', true);
				}
			});

			//setup orgs
			var orgs = new Array();
			for (var i = 0; i < data.organizations.length; i++) {
				orgs.push(data.organizations[i].id);
			}
			$('#familyOrganization').val(orgs);
		}

		function handleEditFamily(data) {
			$('#editFamilyModal').modal('hide');
			$('#addPersonLabel').text('Add Family Member');

			$('.page-header').find('h1').html(data.familyName + ' <small>Family</small>');
			$('#viewAddress').html('<strong>Address: </strong>' + addressRender(data.address));
			$('#viewPhone').html('<strong>Phone Number: </strong>' + (data.phoneNumber != null ? data.phoneNumber : ''));
			$('#viewStatus').html('<strong>Status: </strong>' + data.familyStatus);

			var organizations = '';
			for (var i = 0; i < data.organizations.length; i++) {
				if (i != 0) {
					organizations += ', ';
				}
				organizations += data.organizations[i].organization;
			}

			$('#viewOrganization').html('<strong>Organizations: </strong>' + organizations);

		}

		function canEditFamily() {
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

		function editFamily() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/family/edit"/>',
				data : $('#familyForm').serialize(),
				success : function(data) {
					handleEditFamily(data);
				}
			});
		}
	</script>
</t:mainPage>