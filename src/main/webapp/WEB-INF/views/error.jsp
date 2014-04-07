<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="image" value="/resources/img" />

<%@ include file="./dayMod.jsp" %>

<t:base activeMenu="home" pageTitle="Error">

	<div class="jumbotron">
		<div class="container">
			<div class="row alert alert-danger">
				<div class="col-md-3 col-sm-4 visible-sm visible-md visible-lg">
					<img
						src="<spring:url value="/resources/img" />/<spring:message code="${dayMod}.image" />.jpg"
						class="img-thumbnail base-popover"
						data-content="<spring:message code='${dayMod}.image.title' />"
						data-trigger="hover" data-container="body" />
				</div>

				<div class="col-md-9">
					<h1>An error occurred</h1>
					<p>If the problem continues please contact the leader of your organization.</p>
				</div>
			</div>
		</div>
	</div>
</t:base>