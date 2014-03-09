<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<spring:url var="image" value="/resources/img" />

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
			<div class="row alert alert-danger">
				<div class="col-md-3 col-sm-4 visible-sm visible-md visible-lg">
					<img
						src="<spring:url value="/resources/img" />/<spring:message code="${dayMod}.image" />.jpg"
						class="img-thumbnail base-popover"
						data-content="<spring:message code='${dayMod}.image.title' />"
						data-trigger="hover" data-container="body" />
				</div>

				<div class="col-md-9">
					<h1>No active assignments</h1>
					<p>If you think this is a mistake please contact your
						organization leader.</p>
				</div>
			</div>
		</div>
	</div>
</t:base>