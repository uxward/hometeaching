<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:useBean id="now" class="java.util.Date" />
<c:set var="day">
	<fmt:formatDate value="${now}" pattern="D" />
</c:set>
<c:set var="dayMod">
	${day % 3}
</c:set>

<t:base activeMenu="home" pageTitle="Home">

	<div class="jumbotron">
		<div class="container">

			<c:if test="${param.logout}">
				<div class="row">
					<div class="alert alert-success">
						<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
						<strong>Success!</strong> You have successfully logged out.
					</div>
				</div>
			</c:if>

			<c:if test="${param.error}">
				<div class="row">
					<div class="alert alert-danger">
						<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
						<strong>Warning!</strong> Invalid login credentials.
					</div>
				</div>
			</c:if>

			<div class="row">
				<div class="col-md-3 visible-md visible-lg">
					<img src="<spring:url value="/resources/img" />/<spring:message code="${dayMod}.image" />.jpg" class="img-thumbnail base-popover" data-content="<spring:message code='${dayMod}.image.title' />" data-trigger="hover" data-container="body"/>
				</div>

				<div class="col-md-5">
					<h1>Login Page</h1>

					<form action="<spring:url value="/login-execute" />" method="POST">
						<div class="form-group">
							<input type="text" class="form-control" placeholder="Username" name="j_username" maxlength="50" />
						</div>
						<div class="form-group">
							<input type="password" class="form-control" placeholder="Password" name="j_password" maxlength="50" />
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
