<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="reset">
	<sec:authentication property="principal.reset" />
</c:set>

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
			<div class="row">
				<div class="col-md-3 visible-md visible-lg">
					<img src="<spring:url value="/resources/img" />/<spring:message code="${dayMod}.image" />.jpg" class="img-thumbnail base-popover" data-content="<spring:message code='${dayMod}.image.title' />"
						data-trigger="hover" data-container="body" />
				</div>

				<div class="col-md-9">
					<h1>Home Teaching</h1>

					<c:choose>
						<c:when test="${reset}">

							<p>
								Before you can get started,
								<a href="<spring:url value="/user/you" />">reset your password here.</a>
							</p>

						</c:when>
						<c:otherwise>

							<p>
								<spring:message code="${dayMod}.hero" />
							</p>
							<p class="">
								-
								<spring:message code="${dayMod}.hero.quote" />
							</p>

						</c:otherwise>
					</c:choose>

				</div>
			</div>
		</div>
	</div>

</t:base>