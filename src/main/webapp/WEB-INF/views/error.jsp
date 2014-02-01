<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="image" value="/resources/img" />

<t:base activeMenu="home" pageTitle="Home">

	<div class="jumbotron">
		<div class="container">
			<div class="row alert alert-danger">
				<div class="col-md-3 hidden-xs hidden-sm">
					<img src="${image}/weyland.png" class="img-thumbnail" />
				</div>

				<div class="col-md-9">
					<h1>An error occurred</h1>
					<p>...while building better worlds.</p>
				</div>
			</div>
		</div>
	</div>
</t:base>