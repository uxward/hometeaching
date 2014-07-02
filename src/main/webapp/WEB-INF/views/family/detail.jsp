<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:url var="resources" value="/resources" />

<t:notePage activeMenu="yourFamily" pageTitle="Family Detail" pageHeader="${family.familyName}" pageSubheader="Family">

	<div>
		<ul class="nav nav-tabs">
			<li class="active"><a href="#familyInformation" data-toggle="tab"><span class="visible-xs">Info</span><span class="hidden-xs">Family Information</span></a></li>
			<li><a href="#personInformation" data-toggle="tab"><span class="visible-xs">Members</span><span class="hidden-xs">Family Members</span></a></li>
			<li><a href="#noteInformation" data-toggle="tab"><span class="visible-xs">Notes</span><span class="hidden-xs">Family Notes</span></a></li>
		</ul>
		<div class="tab-content">
			<!-- start view family information -->
			<div class="tab-pane active" id="familyInformation">
				<br />
				<div class="well">
					<p id="viewAddress">
						<strong>Address:</strong> ${family.address}
					</p>
					<p id="viewPhone">
						<strong>Phone Number:</strong> ${family.phoneNumber}
					</p>
					<p id="viewStatus" class="<sec:authorize access="!hasRole('council')">hidden</sec:authorize>">
						<strong>Status:</strong> ${family.familyStatus}
					</p>
					<p id="viewOrganization">
						<strong>Organization:</strong>
						<c:forEach items="${family.organizations}" var="organization" varStatus="status">${organization.name}<c:if test="${!status.last}">, </c:if>
						</c:forEach>
					</p>
					<p>
						<strong>Home teachers:</strong>
						<c:if test="${not empty family.homeTeachingCompanions}">
							<c:forEach items="${family.homeTeachingCompanions.teachers}" var="person" varStatus="status">${person.firstName}&nbsp;${person.family.familyName}<c:if test="${!status.last}">, </c:if>
							</c:forEach>
						</c:if>
					</p>
					<p>
						<strong>Visiting teachers:</strong>
						<c:if test="${not empty family.visitingTeachingCompanions}">
							<c:forEach items="${family.visitingTeachingCompanions.teachers}" var="person" varStatus="status">${person.firstName}&nbsp;${person.family.familyName}<c:if test="${!status.last}">, </c:if>
							</c:forEach>
						</c:if>
					</p>
				</div>

				<a href="#editFamilyModal" id="openFamilyModal" role="button" class="btn btn-primary" data-family-id="${family.id}" data-toggle="modal">Edit Family</a>
			</div>
			<!-- end view family information -->

			<!-- start family member information -->
			<div class="tab-pane" id="personInformation">
				<br />
				<table id="personTable" class="table table-striped table-hover table-bordered" width="100%">
				</table>

				<br /> <a href="#addPerson" role="button" class="btn btn-primary" data-toggle="modal">Add Family Member</a>
			</div>
			<!-- end family member information -->

			<!-- start note information -->
			<div class="tab-pane" id="noteInformation">
				<br />
				<div class="col-md-8 col-md-offset-2 col-lg-8 col-lg-offset-2 col-sm-8 col-sm-offset-2 col-xs-10 col-xs-offset-1" id="addNoteContainer"></div>
				<div class="clearfix"></div>
				<div data-columns id="columns" class="notes"></div>
			</div>
			<!-- end note information -->
		</div>
	</div>

	<!-- Add Person Modal -->
	<div id="addPerson" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addPersonLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="addPersonLabel">Add Family Member</h3>
				</div>

				<div class="modal-body">
					<form id="personForm">
						<div class="form-group">
							<label class="sr-only" for="firstName">First Name</label> <input class="form-control" type="text" name="firstName" id="firstName" placeholder="First Name" maxlength="30" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="firstName">Email</label> <input class="form-control" type="text" name="email" id="email" placeholder="Email" maxlength="30" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="firstName">Phone Number</label> <input class="form-control" type="text" name="phoneNumber" id="phoneNumber" placeholder="Phone Number" maxlength="20" />
						</div>
						<div class="form-group">
							<label class="checkbox"> <input type="checkbox" name="female" id="female"> Female
							</label>
						</div>
						<div class="form-group">
							<label class="checkbox"> <input type="checkbox" name="headOfHousehold" id="headOfHousehold"> Head of Household
							</label>
						</div>
						<div class="form-group <sec:authorize access="!hasRole('council')">hidden</sec:authorize>">
							<label class="checkbox"> <input type="checkbox" name="visitingTeacher" id="visitingTeacher"> Visiting Teacher
							</label>
						</div>
						<div class="form-group <sec:authorize access="!hasRole('council')">hidden</sec:authorize>">
							<label class="checkbox"> <input type="checkbox" name="homeTeacher" id="homeTeacher"> Home Teacher
							</label>
						</div>
						<div class="form-group <sec:authorize access="!hasRole('council')">hidden</sec:authorize>">
							<label class="checkbox"> <input type="checkbox" name="user" id="user"> User
							</label>
						</div>
						<input type="hidden" value="${family.id}" name="familyId" /> <input type="hidden" id="personId" name="id" />
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
					<button type="button" class="btn btn-primary" id="savePerson" data-action="save">Save Family Member</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Edit Family Modal -->
	<div id="editFamilyModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="editFamilyLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="editFamilyLabel">Edit Family</h3>
				</div>

				<div class="modal-body">
					<form id="familyForm">
						<div class="form-group">
							<label class="sr-only" for="familyName">Family Name</label> <input class="form-control" type="text" name="familyName" placeholder="Family Name" id="familyName" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="address">Address</label> <input class="form-control" type="text" name="address" placeholder="Address" id="address" />
						</div>
						<div class="form-group">
							<label class="sr-only" for="phoneNumber">Phone Number</label> <input class="form-control" type="text" name="phoneNumber" placeholder="Phone Number" id="familyPhoneNumber" />
						</div>
						<div class="form-group <sec:authorize access="!hasRole('council')">hidden</sec:authorize>">
							<label class="checkbox"> <input type="checkbox" name="familyMoved" id="familyMoved"> Family Moved
							</label>
						</div>

						<div class="form-group <sec:authorize access="!hasRole('council')">hidden</sec:authorize> <c:if test="${!family.familyMoved}">hidden</c:if>">
							<label class="checkbox"> <input type="checkbox" name="recordsMoved" id="recordsMoved"> Records Moved
							</label>
						</div>
						<div class="form-group <sec:authorize access="!hasRole('council')">hidden</sec:authorize>">
							<label class="checkbox"> <input type="checkbox" name="partMember" id="partMember"> Part Member
							</label>
						</div>

						<div class="form-group <sec:authorize access="!hasRole('council')">hidden</sec:authorize>">
							<label class="sr-only" for="familyStatus">Family Status</label> <select name="familyStatusId" class="form-control" id="familyStatus">
								<option value="">Select Status</option>
								<c:forEach items="${statuses}" var="status">
									<option value="${status.id}">${status.status}</option>
								</c:forEach>
							</select>
						</div>

						<div class="form-group <sec:authorize access="!hasRole('council')">hidden</sec:authorize>">
							<label class="sr-only" for="organization">Organization</label> <select name="familyOrganizationIds" class="form-control" id="familyOrganization" multiple="multiple">
								<option value="">Select Organization</option>
								<c:forEach items="${organizations}" var="organization">
									<option value="${organization.id}">${organization.name}</option>
								</c:forEach>
							</select>
						</div>
						<input type="hidden" value="${family.id}" name="id" />
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
					<button type="button" class="btn btn-primary" id="editFamily">Save Family</button>
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

			//get notes when display note panel
			$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
				if ($('#columns').is(':visible') && !$('#columns').data('loaded')) {					
					initNotes($('#columns'), $('#addNoteContainer'), $('#noteInformation'), '${family.id}');
					$('#columns').data('loaded', true);
				}
			});
		}

		function setupPersonTable() {
			$('#personTable').DataTable({
				'dom' : 't',
				'ajax' : '<spring:url value="/person/getByFamily/${family.id}"/>',
				'data' : [],
				'order' : [ [ 6, 'desc' ], [ 0, 'asc' ] ],
				'columns' : [ {
					'title' : 'First Name',
					'data' : 'firstName',
					'render' : setupFirstNameLink
				}, {
					'title' : 'Email',
					'data' : 'email'
				}, {
					'title' : 'Phone Number',
					'data' : 'phoneNumber',
					'render' : getPhoneNumber
				}, {
					'title' : 'Gender',
					'data' : 'female',
					'render' : maleFemale,
					'className' : 'hidden-xs hidden-sm'
				}, {
					'title' : 'Home Teacher',
					'data' : 'homeTeacher',
					'render' : setupTrueFalseAsYesNo,
					'className' : 'hidden-xs'
				}, {
					'title' : 'Visiting Teacher',
					'data' : 'visitingTeacher',
					'render' : setupTrueFalseAsYesNo,
					'className' : 'hidden-xs'
				}, {
					'title' : 'Head of Household',
					'data' : 'headOfHousehold',
					'render' : setupTrueFalseAsYesNo,
					'className' : 'hidden-xs  hidden-sm'
				} ],
				'language' : {
					'infoEmpty' : 'No people to show',
					'emptyTable' : 'There are no people in this family yet.  Add a person by clicking the button below.'
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

			if (data.female) {
				$('#female').prop('checked', true);
			}
			if (data.headOfHousehold) {
				$('#headOfHousehold').prop('checked', true);
			}
			if (data.visitingTeacher) {
				$('#visitingTeacher').prop('checked', true);
			}
			if (data.homeTeacher) {
				$('#homeTeacher').prop('checked', true);
			}
			if (data.user) {
				$('#user').prop('checked', true);
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
			$('#viewPhone').html('<strong>Phone Number: </strong>' + (data.phoneNumber != null ? getPhoneNumber(data.phoneNumber) : ''));
			$('#viewStatus').html('<strong>Status: </strong>' + data.familyStatus);

			var organizations = '';
			for (var i = 0; i < data.organizations.length; i++) {
				if (i != 0) {
					organizations += ', ';
				}
				organizations += data.organizations[i].name;
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
</t:notePage>