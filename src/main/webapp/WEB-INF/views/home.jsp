<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="reset">
	<sec:authentication property="principal.reset" />
</c:set>

<%@ include file="./dayMod.jsp" %>

<t:base activeMenu="home" pageTitle="Home Teaching">

	<div class="jumbotron">
		<div class="container">
			<div class="row">
				<div class="col-md-3 col-sm-4 visible-sm visible-md visible-lg">
					<img src="<spring:url value="/resources/img" />/<spring:message code="${dayMod}.image" />.jpg" class="img-thumbnail base-popover" data-content="<spring:message code='${dayMod}.image.title' />"
						data-trigger="hover" data-container="body" />
				</div>

				<div class="col-md-9 col-sm-8">
					<br />

					<c:choose>
						<c:when test="${reset}">

							<p>
								Before you can get started,
								<a href="<spring:url value="/user/you" />">reset your password here.</a>
							</p>

						</c:when>
						<c:otherwise>
							<spring:message code="${dayMod}.hero" />
							<p>
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