<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:url var="image" value="/resources/img" />

<t:mainPage activeMenu="users" pageTitle="Users" pageHeader="All" pageSubheader="Users">

	<table id="userTable" class="table table-striped table-hover table-bordered" width="100%">
	</table>

	<sec:authorize access="hasRole('leader')">

		<a href="#addUser" class="btn btn-primary" data-toggle="modal">Add User</a>

		<!-- Add User Modal -->
		<div id="addUser" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addUserLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">

					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="addUserLabel">Add User</h3>
					</div>

					<div class="modal-body">
						<form id="userForm">
							<div class="form-group">
								<label class="sr-only" for="person">Person</label>
								<select name="personId" class="form-control" id="person">
									<option value="">Select Person</option>
									<c:forEach items="${unassigned}" var="person">
										<option value="${person.id}">${person.firstName }&nbsp;${person.family.familyName}</option>
									</c:forEach>
								</select>
							</div>

							<div class="form-group">
								<label class="sr-only" for="username">Username</label>
								<input class="form-control" type="text" id="username" name="username" placeholder="Username" maxlength="30" />
							</div>

							<div class="form-group">
								<label class="sr-only" for="email">Email</label>
								<input class="form-control" type="email" id="email" name="email" placeholder="Email" maxlength="30" />
							</div>

							<div class="form-group">
								<label class="sr-only" for="password">Password</label>
								<input class="form-control" type="password" id="password" name="password" placeholder="Password" maxlength="100" />
							</div>

							<div class="form-group">
								<label class="sr-only" for="password">Confirm Password</label>
								<input class="form-control" type="password" id="confirmPassword" placeholder="Password" maxlength="100" />
							</div>

							<div class="form-group">
								<label class="sr-only" for="role">Role</label>
								<select name="userRoleIds" class="form-control" id="role" multiple="multiple">
									<option value="">Select Role</option>
									<c:forEach items="${roles}" var="role">
										<option value="${role.role}">${role.display}</option>
									</c:forEach>
								</select>
							</div>

							<div class="form-group">
								<label class="sr-only" for="organization">Organization</label>
								<select name="userOrganizationIds" class="form-control" id="organization" multiple="multiple">
									<option value="">Select Organization</option>
									<c:forEach items="${organizations}" var="organization">
										<option value="${organization.id}">${organization.organization}</option>
									</c:forEach>
								</select>
							</div>
						</form>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
						<button type="button" class="btn btn-primary" id="saveUser">Save User</button>
					</div>

				</div>
			</div>
		</div>

		<!-- Edit User Modal -->
		<div id="editUserModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="editUserLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">

					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="editUserLabel">Edit User</h3>
					</div>

					<div class="modal-body">
						<form id="editUserForm">

							<div class="form-group">
								<label class="sr-only" for="username">Username</label>
								<input class="form-control" type="text" id="editUsername" name="username" placeholder="Username" maxlength="30" />
							</div>

							<div class="form-group">
								<label class="sr-only" for="email">Email</label>
								<input class="form-control" type="email" id="editEmail" name="email" placeholder="Email" maxlength="30" />
							</div>

							<div class="form-group">
								<label class="sr-only" for="password">Password</label>
								<input class="form-control" type="password" id="editPassword" name="password" placeholder="Password" maxlength="100" />
							</div>

							<div class="form-group">
								<label class="sr-only" for="password">Confirm Password</label>
								<input class="form-control" type="password" id="editConfirmPassword" placeholder="Confirm Password" maxlength="100" />
							</div>

							<div class="form-group">
								<label class="checkbox">
									<input type="checkbox" name="reset" id="reset"> Require Password Reset
								</label>
							</div>

							<div class="form-group">
								<label class="sr-only" for="role">Role</label>
								<select name="userRoleIds" class="form-control" id="editRole" multiple="multiple">
									<option value="">Select Role</option>
									<c:forEach items="${roles}" var="role">
										<option value="${role.role}">${role.display}</option>
									</c:forEach>
								</select>
							</div>

							<div class="form-group">
								<label class="sr-only" for="organization">Organization</label>
								<select name="userOrganizationIds" class="form-control" id="editOrganization" multiple="multiple">
									<option value="">Select Organization</option>
									<c:forEach items="${organizations}" var="organization">
										<option value="${organization.id}">${organization.organization}</option>
									</c:forEach>
								</select>
							</div>

							<input type="hidden" name="id" id="editUserId" />
						</form>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
						<button type="button" class="btn btn-primary" id="editUser">Update User</button>
					</div>

				</div>
			</div>
		</div>

	</sec:authorize>

	<script type="text/javascript">
		$(document).ready(function() {
			setupEventBinding();

			setupUserTable();
		});

		function setupEventBinding() {
			$('#person').change(function(){
				getNewUserInformation($(this).val());
			});
			
			$('#saveUser').click(function() {
				if (canSaveUser()) {
					saveUser();
				}
			});

			$('#userTable').on('click', '.editUser', function() {
				getEditUser($(this));
			});

			$('#userTable').on('click', '.toggleEnable', function() {
				var $this = $(this);
				var text = $this.val().toLowerCase();
				if (confirm('Are you sure you want to ' + text + ' this user?')) {
					toggleEnableUser($this, $this.closest('tr')[0]);
				}
			});

			$('#userTable').on('click', '.emailInvitation', function() {
				var $this = $(this);
				if (confirm('Are you sure you want to email an invitation for using this site to this user?')) {
					sendEmailInvite($this, $this.closest('tr')[0]);
				}
			});

			$('#editUser').click(function() {
				if (canEditUser()) {
					editUser($(this).data('tr')[0]);
				}

			});
		}

		function setupUserTable() {
			$('#userTable').dataTable({
				'sAjaxSource' : '<spring:url value="/user/getAllUsers/"/>',
				'aaData' : [],
				'aaSorting' : [ [ 5, 'desc' ] ],
				'aoColumns' : [ {
					'mData' : 'id',
					'bVisible' : false
				}, {
					'sTitle' : 'Name',
					'mData' : 'name',
					'mRender' : setupNameCompanionLink,
					'sWidth' : '15%'
				}, {
					'sTitle' : 'Username',
					'mData' : 'username',
					'sWidth' : '15%'
				}, {
					'sTitle' : 'Role',
					'mData' : 'roles',
					'mRender' : setupRoles,
					'sWidth' : '15%'
				}, {
					'sTitle' : 'Organization',
					'mData' : 'organizations',
					'mRender' : setupOrganizations,
					'sWidth' : '15%'
				}, {
					'sTitle' : 'Last Login',
					'mData' : 'lastLogin',
					'mRender' : setupFullDate,
					'sWidth' : '18%'
				}, {
					'sTitle' : 'Setup Account',
					'mData' : 'reset',
					'mRender' : setupTrueFalseAsYesNoOpposite,
					'sWidth' : '5%'
				}
				<sec:authorize access="hasRole('leader')">
					, {
						'sTitle' : 'Actions',
						'mData' : 'id',
						'mRender' : setupActions,
						'sWidth' : '17%'
					}
				</sec:authorize>
				],
				'oLanguage' : {
					'sInfoEmpty' : 'No users to show',
					'sEmptyTable' : 'There are no users yet.  Add a user by clicking the button below.'
				}
			});
		}
		
		function setupNameCompanionLink(data, type, full){
			var html = data;
			if(full.companionId != null){
				html = '<a href="<spring:url value="/companion/detail/"/>' + full.companionId + '">' + data + '</a>';
			}
			return html;
		}

		function setupRoles(data, type, full) {
			var roles = '';
			for (var i = 0; i < data.length; i++) {
				if (i > 0) {
					roles += ', ';
				}
				roles += data[i].display;
			}
			return roles;
		}

		function setupOrganizations(data, type, full) {
			var organizations = '';
			for (var i = 0; i < data.length; i++) {
				if (i > 0) {
					organizations += ', ';
				}
				organizations += data[i].organization;
			}
			return organizations;
		}

		function setupActions(data, type, full) {
			return '<input type="button" class="btn btn-primary editUser button-medium" data-user-id="' + data + '" value="Edit"/>'
				+ ' <input type="button" class="btn btn-primary toggleEnable button-medium" data-user-id="' + data + '" value="'+ (full.enabled ? 'Disable' : 'Enable') + '"/>'
				+ ' <a href="#" class="btn emailInvitation " data-user-id="' + data + '"><i class="glyphicon glyphicon-envelope"></i> Email Invitation</a>';
		}
		
		function canSaveUser() {
			var valid = true;

			//make sure all fields have data
			$.each($('#userForm .form-control'), function() {
				var value = $.trim($(this).val());
				if (!(value.length > 0)) {
					valid = false;
					$(this).parent().addClass('has-error');
				} else {
					$(this).parent().removeClass('has-error');
				}
			});

			if (valid) {
				//make sure new passwords match
				var $password = $('#password');
				var $confirm = $('#confirmPassword');
				var password = $.trim($password.val());
				var confirm = $.trim($confirm.val());
				if (password != confirm) {
					valid = false;
					$password.parent().addClass('has-error');
					$confirm.parent().addClass('has-error');
				} else {
					$password.parent().removeClass('has-error');
					$confirm.parent().removeClass('has-error');
				}

				var $email = $('#email');
				if (!isValidEmail($.trim($email.val()))) {
					valid = false;
					$email.parent().addClass('has-error');
				} else {
					$email.parent().removeClass('has-error');
				}
			}

			return valid;
		}

		function saveUser() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/user/save"/>',
				data : $('#userForm').serialize(),
				success : function(data) {
					handleSaveUser(data);
				}
			});
		}

		function handleSaveUser(data) {
			if (data.success) {
				showNotificationSuccess('This user was saved successfully.');
				//clear form and hide modal
				$('#addUser').modal('hide');
				resetFormElements('userForm');

				//remove user from dropdown
				$('#person option').filter(function() {
					return $(this).val() == data.personId;
				}).remove();

				//add family row to table
				addUserToTable(data);
			} else if (data.duplicate) {
				showModalError('This username is not available, please choose a different one.');
			} else if (data.error) {
				showModalError('There was an unexpected error while creating this user.  If the problem continues please submit feedback with a description of the problem.');
			}
		}

		function addUserToTable(data) {
			$('#userTable').dataTable().fnAddData(data);
		}

		function getEditUser($this) {
			$.ajax({
				type : 'GET',
				url : '<spring:url value="/user/getUserDetails"/>',
				data : {
					'userId' : $this.data('userId')
				},
				success : function(data) {
					setupEditUser(data, $this);
				}
			});
		}

		function setupEditUser(data, $this) {
			$('#editUserLabel').text('Update ' + data.name);
			$('#editUsername').val(data.username);
			$('#editEmail').val(data.email);
			$('#editReset').attr('checked', data.reset);
			$('#editUserId').val(data.id);
			$('#editUserPersonId').val(data.personId);

			//setup roles
			var roles = new Array();
			for (var i = 0; i < data.roles.length; i++) {
				roles.push(data.roles[i].role);
			}
			$('#editRole').val(roles);

			//setup orgs
			var orgs = new Array();
			for (var i = 0; i < data.organizations.length; i++) {
				orgs.push(data.organizations[i].id);
			}
			$('#editOrganization').val(orgs);

			$('#editUser').data('tr', $this.closest('tr'));
			$('#editUserModal').modal('show');
		}

		function canEditUser() {
			var valid = true;
			var resetPassword = $('#passwordReset').is(':checked');

			//make sure all fields have data
			$.each($('#editUserForm .form-control'), function() {
				if (!$(this).is(':password') || resetPassword) {
					var value = $.trim($(this).val());
					if (!(value.length > 0)) {
						valid = false;
						$(this).parent().addClass('has-error');
					} else {
						$(this).parent().removeClass('has-error');
					}
				}
			});

			if (valid) {
				//make sure new passwords match
				var $password = $('#editPassword');
				var $confirm = $('#editConfirmPassword');
				var password = $.trim($password.val());
				var confirm = $.trim($confirm.val());
				if (password != confirm) {
					valid = false;
					$password.parent().addClass('has-error');
					$confirm.parent().addClass('has-error');
				} else {
					$password.parent().removeClass('has-error');
					$confirm.parent().removeClass('has-error');
				}
			}

			return valid;
		}

		function editUser(tr) {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/user/updateUserDetails"/>',
				data : $('#editUserForm').serialize(),
				success : function(data) {
					handleEditUser(data, tr);
				}
			});
		}

		function handleEditUser(data, tr) {
			if (data.success) {
				//clear form and hide modal
				$('#editUserModal').modal('hide');
				resetFormElements('editUserForm');
				$('#userTable').dataTable().fnUpdate(data, tr);
				showModalSuccess('The user was successfully saved.');
			} else if (data.duplicate) {
				showModalError('This username is not available, please choose a different one.');
			} else if (data.error) {
				showModalError('There was an unexpected error while updating this user.  If the problem continues please submit feedback with a description of the problem.');
			}
		}
		
		function getNewUserInformation(personId){
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/person/get"/>',
				data : {
					'personId' : personId
				},
				success : function(data) {
					$('#username').val(data.email);
					$('#email').val(data.email);
					var password = data.fullName.toLowerCase().replace(' ','');
					$('#password').val(password);
					$('#confirmPassword').val(password);
				}
			});
		}

		function toggleEnableUser($this, tr) {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/user/toggleEnabled"/>',
				data : {
					'id' : $this.data('userId')
				},
				success : function(data) {
					$('#userTable').dataTable().fnUpdate(data, tr);
				}
			});
		}
		
		function sendEmailInvite($this){
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/email/newUser"/>',
				data : {
					'userId' : $this.data('userId')
				},
				success : function(data) {
					if(data.success){
						showNotificationSuccess('An invitation email was successfully sent to this user.');
					} else {
						showNotificationError('There was an unexpected error while emailing this user.  Please verify that the email address is valid.  If the problem continues please contact the leader of your organization.');
					}
				}
			});
		}
	</script>
</t:mainPage>