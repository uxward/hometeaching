<%@ tag description="Standard Page Template With Heading" pageEncoding="UTF-8"%>
<%@ attribute name="pageTitle" required="true"%>
<%@ attribute name="activeMenu" required="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>

<spring:url var="home" value="/" />
<spring:url var="dashboard" value="/dashboard" />
<spring:url var="family" value="/family" />
<spring:url var="companion" value="/companion" />
<spring:url var="visit" value="/visit" />
<spring:url var="user" value="/user" />
<spring:url var="feedback" value="/feedback" />
<spring:url var="resources" value="/resources" />
<spring:url var="image" value="/resources/img" />

<c:set var="reset">
	<sec:authorize access="isAuthenticated()">
		<sec:authentication property="principal.reset" />
	</sec:authorize>
</c:set>

<jsp:useBean id="now" class="java.util.Date" />
<c:set var="day">
	<fmt:formatDate value="${now}" pattern="D" />
</c:set>
<c:set var="dayMod">
	${day % 3}
</c:set>

<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>${pageTitle}</title>
<link rel="shortcut icon" href="${image}/<spring:message code="${dayMod}.icon" />.png">

<!--  Bootstrap 3
--------------------------------------------------->
<link rel="stylesheet" href="${resources}/css/bootstrap.min.css" />
<link rel="stylesheet" href="${resources}/css/datatable.css" />
<link rel="stylesheet" href="${resources}/css/datepicker.css" />
<link rel="stylesheet" href="${resources}/css/custom.css" />

<script src="${resources}/js/jquery-2.0.3.min.js"></script>
<script src="${resources}/js/jquery-ui.min.js"></script>
<script src="${resources}/js/bootstrap.min.js"></script>
<script src="${resources}/js/jquery.dataTables.min.js"></script>
<script src="${resources}/js/bootstrap-datatable.js"></script>
<script src="${resources}/js/bootstrap-datepicker.js"></script>
<script src="${resources}/js/dateformatter.js"></script>
<script src="${resources}/js/custom.js"></script>
<script src="${resources}/js/browser-detect.js"></script>
<script src="${resources}/js/d3.min.js"></script>
<!-- <script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script> -->
<script src="${resources}/js/d3-bootstrap.min.js"></script>
<script src="${resources}/js/google-analytics.js"></script>
</head>

<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<sec:authorize access="isAuthenticated()">
					<c:if test="${!reset}">
						<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
					</c:if>
				</sec:authorize>
				<a class="navbar-brand ${activeMenu ==  'home' ? 'active' : '' }" href="${home}">
					<span class="glyphicon glyphicon-asterisk"></span>
					Home Teaching
				</a>
			</div>
			<div class="navbar-collapse collapse">
				<sec:authorize access="isAuthenticated()">

					<ul class="nav navbar-nav">
						<c:if test="${!reset}">
							<sec:authorize access="hasRole('leader')">

								<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">
										Admin <b class="caret"></b>
									</a>
									<ul class="dropdown-menu">
										<li class="${activeMenu ==  'visitPercentage' ? 'active' : '' }"><a href="${dashboard}/visitPercentage">Visit Percentage</a></li>
										<li class="${activeMenu ==  'users' ? 'active' : '' }"><a href="${user}/all">Users</a></li>
										<li class="${activeMenu ==  'visitHistory' ? 'active' : '' }"><a href="${visit}/history/3">Visit History</a></li>
										<sec:authorize access="hasRole('admin')">
											<li class="${activeMenu ==  'feedback' ? 'active' : '' }"><a href="${feedback}">Feedback</a></li>
										</sec:authorize>
									</ul></li>

								<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">
										Families <b class="caret"></b>
									</a>
									<ul class="dropdown-menu">
										<li class="${activeMenu ==  'allFamilies' ? 'active' : '' }"><a href="${family}/all">All Families</a></li>
										<li class="${activeMenu ==  'yourFamily' ? 'active' : '' }"><a href="${family}/you">Your Family</a></li>
									</ul></li>

								<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">
										Companions <b class="caret"></b>
									</a>
									<ul class="dropdown-menu">
										<li class="${activeMenu ==  'allCompanions' ? 'active' : '' }"><a href="${companion}/all">All Companions</a></li>
										<li class="${activeMenu ==  'yourCompanion' ? 'active' : '' }"><a href="${companion}/you">Your Companionship</a></li>
									</ul></li>

							</sec:authorize>
							<sec:authorize access="!hasRole('admin')">
								<li class="${activeMenu ==  'yourFamily' ? 'active' : '' }"><a href="${family}/you">Family</a></li>
								<li class="${activeMenu ==  'yourCompanion' ? 'active' : '' }"><a href="${companion}/you">Companionship</a></li>
							</sec:authorize>
						</c:if>
						<li class="visible-xs visible-sm"><a href="${user}/you">
								Welcome,
								<sec:authentication property="principal.person.firstName" />
							</a></li>
					</ul>

					<ul class="nav navbar-nav navbar-right">
						<li class="visible-md visible-lg">
							<p class="navbar-text">
								Welcome,
								<a href="${user}/you">
									<sec:authentication property="principal.person.firstName" />
								</a>
							</p>
						</li>
						<li><a href="#leaveFeedback" data-toggle="modal">
								<i class="glyphicon glyphicon-comment"></i> Feedback
							</a></li>

						<li><a href="<spring:url value="/logout"/>">
								<i class="glyphicon glyphicon-log-out"></i> Logout
							</a></li>
					</ul>

				</sec:authorize>

			</div>
		</div>
	</div>

	<!-- 	<div class="navbar navbar-fixed-top notification-navbar" id="notificationNavbar"> -->
	<div class="navbar navbar-fixed-top notification-navbar alert alert-danger col-md-4 col-md-offset-4" style="display: none;">
		<button type="button" class="close alert-close" aria-hidden="true">&times;</button>
		<p class="text">
			<strong>Warning!</strong> Best check yo self, youre not looking too good.
		</p>
	</div>
	<div class="navbar navbar-fixed-top notification-navbar alert alert-success col-md-4 col-md-offset-4" style="display: none;">
		<button type="button" class="close alert-close" aria-hidden="true">&times;</button>
		<p class="text">
			<strong>Warning!</strong> Best check yo self, youre not looking too good.
		</p>
	</div>
	<!-- 	</div> -->

	<br />

	<jsp:doBody />

	<!-- Give Feedback Modal -->
	<div id="leaveFeedback" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="leaveFeedbackLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="leaveFeedbackLabel">Leave Feedback</h3>
				</div>

				<div class="modal-body">
					<form id="feedbackForm">
						<div class="form-group" style="max-width: 535px;">
							<label class="sr-only" for="notes">Feedback</label>
							<textarea class="form-control" name="notes" id="feedback" placeholder="Feedback" maxlength="495" rows="8" cols="75"></textarea>
						</div>
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-primary" id="submitFeedback">Submit Feedback</button>
				</div>

			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			setupBaseEventBinding();
		});

		function setupBaseEventBinding() {
			$('#submitFeedback').click(function() {
				if (canSubmitFeedback()) {
					submitFeedback();
				}
			});

			setupModalAlerts();
			setupAlertBinding();

			$('.modal').on('hidden.bs.modal', function() {
				resetModalAlerts();
			});

			$('.modal').each(function(i) {
				$(this).draggable({
					handle : '.modal-header'
				});
			});

			$('.base-popover').popover();
		}

		function canSubmitFeedback() {
			var valid = true;

			var $feedback = $('#feedback');
			if ($.trim($feedback.val()) == '') {
				valid = false;
				$feedback.parent().addClass('has-error');
				showModalError('You must include a note to save your feedback');
			} else {
				$feedback.parent().removeClass('has-error');
				resetModalAlerts();
			}

			return valid;
		}

		function submitFeedback() {
			$.ajax({
				type : 'POST',
				url : '<spring:url value="/feedback/save"/>',
				data : $('#feedbackForm').serialize(),
				success : function(data) {
					handleSaveFeedback(data);
				}
			});
		}

		function handleSaveFeedback(data) {
			if (data.success) {
				//clear form and hide modal
				$('#leaveFeedback').modal('hide');
				$('#feedbackForm')[0].reset();
				showNotificationSuccess('Thank you very much for your feedback.');
			} else {
				showModalError('<p>There was an unexpected error while saving your feedback.  If the issue persists please contact the owner of this site.');
			}
		}
	</script>
</body>