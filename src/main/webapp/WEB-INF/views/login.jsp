<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="./dayMod.jsp"%>

<t:base activeMenu="home" pageTitle="Home">

	<div class="jumbotron">
		<div class="container">

			<c:if test="${param.logout}">
				<div class="row">
					<div class="alert alert-success">
						<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
						You have successfully logged out.
					</div>
				</div>
			</c:if>

			<c:if test="${param.error}">
				<div class="row">
					<div class="alert alert-danger">
						<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
						Invalid login credentials.
					</div>
				</div>
			</c:if>
			
			<!--[if lte IE 9]>
				<div class="row">
					<div class="alert alert-danger">
						<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
						This application is not supported in Internet Explorer versions 9 or lower.  Please use a different browser or update Internet Explorer to version 10 or higher.
					</div>
				</div>
			<![endif]-->

			<div class="row">
				<div class="col-md-3 col-sm-4 visible-sm visible-md visible-lg">
					<img src="<spring:url value="/resources/img" />/<spring:message code="${dayMod}.image" />.jpg" class="img-thumbnail base-popover" data-content="<spring:message code='${dayMod}.image.title' />"
						data-trigger="hover" data-container="body" />
				</div>

				<div class="col-md-5 col-sm-8">
					<br />

					<form action="<spring:url value="/login-execute" />" method="POST" style="font-size:14px; line-height:1.428571429;">
						<div class="form-group">
							<input type="text" class="form-control" placeholder="Username" name="j_username" maxlength="50" />
						</div>
						<div class="form-group">
							<input type="password" class="form-control" placeholder="Password" name="j_password" maxlength="50" />
						</div>
						<div class="form-group">
							<label class="checkbox">
								<input type="checkbox" name="_spring_security_remember_me"> Remember me
							</label>
						</div>
						<button type="submit" class="btn btn-primary">
							<i class="glyphicon glyphicon-log-in"></i> Login
						</button>
					</form>
				</div>
			</div>
			<!-- /.row -->
		</div>
	</div>
</t:base>
