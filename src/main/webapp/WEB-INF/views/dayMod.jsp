<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:useBean id="now" class="java.util.Date" />
<c:set var="day">
	<fmt:formatDate value="${now}" pattern="D" />
</c:set>
<c:set var="dayMod">
	${day % 4}
</c:set>