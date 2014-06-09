<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="../dayMod.jsp"%>

<t:base activeMenu="home" pageTitle="Home">

	<div class="jumbotron">
		<div class="container">

			<div class="row">
				<div class="col-md-3 col-sm-4 visible-sm visible-md visible-lg">
					<img src="<spring:url value="/resources/img" />/<spring:message code="${dayMod}.image" />.jpg" class="img-thumbnail base-popover" data-content="<spring:message code='${dayMod}.image.title' />" data-trigger="hover" data-container="body" />
				</div>

				<div class="col-md-5 col-sm-8">
					<br />

					<form style="font-size: 14px; line-height: 1.428571429;" id="resetForm">
						<div class="form-group">
							<input id="email" type="text" class="form-control" placeholder="Email" name="email" maxlength="50" />
						</div>
						<div class="form-group">
							<input id="confirmEmail" type="text" class="form-control" placeholder="Confirm Email" maxlength="50" />
						</div>
						<input type="button" class="btn btn-primary" id="resetPassword" value="Reset Password" />
						<a href="<spring:url value="/" />" class="btn btn-default" >Cancel</a>
					</form>
				</div>
			</div>
			<!-- /.row -->
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			$('#resetPassword').click(function() {
				if (resetPasswordValid()) {
					resetPassword();
				}
			});
		});

		function resetPasswordValid() {
			var valid = true;
			var $email = $('#email');
			var $confirmEmail = $('#confirmEmail');

			var email = $email.val();
			var confirmEmail = $confirmEmail.val();

			if ($.trim(email).length > 0 && email == confirmEmail) {
				$email.parent().removeClass('has-error');
				$confirmEmail.parent().removeClass('has-error');
			} else {
				valid = false;
				$email.parent().addClass('has-error');
				$confirmEmail.parent().addClass('has-error');
			}

			return valid;
		}

		function resetPassword() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/recover/request"/>',
				data : $('#resetForm').serialize(),
				success : function(data) {
					handleReset(data);
				}
			});
		}

		function handleReset(data) {
			showNotificationSuccess("An email was sent to you with instructions for resetting your password.");
		}
	</script>
</t:base>
