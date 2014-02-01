<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ attribute name="pageHeader" required="true"%>
<%@ attribute name="pageTitle" required="true"%>
<%@ attribute name="activeMenu" required="true"%>
<%@ attribute name="pageSubheader" required="false"%>
<%@ attribute name="requireReset" required="false" %>

<c:set var="reset">
	<sec:authentication property="principal.reset" />
</c:set>

<t:base activeMenu="${activeMenu}" pageTitle="${pageTitle}">

	<c:choose>
		<c:when test="${reset && requireReset eq null}">
			<c:redirect url="/hometeaching" context="/"></c:redirect>
		</c:when>
		<c:otherwise>
			<br />

			<div class="container">
				<div class="page-header">
					<h1>
						${pageHeader} <small>${pageSubheader}</small>
					</h1>
				</div>

				<jsp:doBody />
			</div>
		</c:otherwise>

	</c:choose>


</t:base>