<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<t:mainPage activeMenu="user"
	pageTitle="${user.person.firstName}'s Account"
	pageHeader="${user.person.firstName}'s" pageSubheader="Account"
	requireReset="false">

	<c:if test="${passwordError}">
		<div class="row">
			<div class="alert alert-danger">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				Invalid password, please try again.
			</div>
		</div>
	</c:if>

	<c:if test="${usernameError}">
		<div class="row">
			<div class="alert alert-danger">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				That username is already being used, please try another one.
			</div>
		</div>
	</c:if>

	<c:if test="${success}">
		<div class="row">
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				Your account was updated successfully.
			</div>
		</div>
	</c:if>

	<div class="row">
		<div class="alert alert-info">
			You have the following role:
			<sec:authentication property='principal.authorities' />
		</div>
	</div>

	<form id="userForm" action="<spring:url value="/user/update" />"
		method="POST">
		<div class="form-group">
			<label class="sr-only" for="username">Username</label> <input
				class="form-control" type="text" id="username" name="username"
				maxlength="50" value="${user.username}" placeholder="Username" />
		</div>
		<div class="form-group">
			<label class="sr-only" for="oldPassword">Old Password</label> <input
				class="form-control" type="password" id="oldPassword"
				name="oldPassword" maxlength="50" placeholder="Old Password" />
		</div>
		<div class="form-group">
			<label class="sr-only" for="newPassword">New Password</label> <input
				class="form-control" type="password" id="newPassword"
				name="password" maxlength="50" placeholder="New Password" />
		</div>
		<div class="form-group">
			<label class="sr-only" for="confirmNewPassword">Confirm New
				Password</label> <input class="form-control" type="password"
				id="confirmNewPassword" maxlength="50"
				placeholder="Confirm New Password" />
		</div>
	</form>

	<button type="button" class="btn btn-primary" id="update">Update</button>

	<script type="text/javascript">
		$(document).ready(function() {

			$('#update').click(function() {
				if (valid()) {
					$('#userForm').submit();
				}
			});

			$('#userForm input').keypress(function(event) {
				if (event.which == 13) {
					event.preventDefault();
					if (valid()) {
						$('#userForm').submit();
					}
				}
			});

		});

		function valid() {
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
				var password = $.trim($('#newPassword').val());
				var confirm = $.trim($('#confirmNewPassword').val());
				if (password != confirm) {
					valid = false;
					//	 				showError('Your new passwords don\'t match.');
					$('#newPassword').parent().addClass('has-error');
					$('#confirmNewPassword').parent().addClass('has-error');
				} else {
					$('#newPassword').parent().removeClass('has-error');
					$('#confirmNewPassword').parent().removeClass('has-error');
				}
			}

			return valid;
		}
	</script>
</t:mainPage>