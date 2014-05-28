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

					<form style="font-size: 14px; line-height: 1.428571429;" id="updateForm">
						<div class="form-group">
							<input id="password" type="text" class="form-control" placeholder="New Password" name="password" maxlength="50" />
						</div>
						<div class="form-group">
							<input id="confirmPassword" type="text" class="form-control" placeholder="Confirm Password" maxlength="50" />
						</div>
						<input type="button" class="btn btn-primary" id="resetPassword" value="Reset Password" /> <input type="hidden" name="token" value="${token}" />
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
			var $password = $('#password');
			var $confirmPassword = $('#confirmPassword');

			var password = $password.val();
			var confirmPassword = $confirmPassword.val();

			if ($.trim(password).length > 0 && password == confirmPassword) {
				$password.parent().removeClass('has-error');
				$confirmPassword.parent().removeClass('has-error');
			} else {
				valid = false;
				$password.parent().addClass('has-error');
				$confirmPassword.parent().addClass('has-error');
			}

			return valid;
		}

		function resetPassword() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/recover/update"/>',
				data : $('#updateForm').serialize(),
				success : function(data) {
					handleReset(data);
				}
			});
		}

		function handleReset(data) {

		}
	</script>
</t:base>
