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

<%@ include file="../views/dayMod.jsp"%>

<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>${pageTitle}</title>
<link rel="shortcut icon" href="${image}/<spring:message code="${dayMod}.icon" />.png">

<link rel="stylesheet" href="${resources}/css/bootstrap.min.css" />
<link rel="stylesheet" href="${resources}/css/datatable.css" />
<link rel="stylesheet" href="${resources}/css/datepicker.css" />
<link rel="stylesheet" href="${resources}/css/custom.css" />

<script src="${resources}/js/jquery-2.0.3.min.js"></script>
<script src="${resources}/js/bootstrap.min.js"></script>
<script src="${resources}/js/jquery.dataTables.min.js"></script>
<script src="${resources}/js/bootstrap-datatable.js"></script>
<script src="${resources}/js/bootstrap-datepicker.js"></script>
<script src="${resources}/js/dateformatter.js"></script>
<script src="${resources}/js/custom.js"></script>
<script src="${resources}/js/browser-detect.js"></script>
<script src="${resources}/js/google-analytics.js"></script>
</head>

<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<sec:authorize access="isAuthenticated()">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
				</sec:authorize>
				<a class="navbar-brand ${activeMenu ==  'home' ? 'active' : '' }" href="${home}">
					<span class="glyphicon glyphicon-asterisk"></span>
					<sec:authorize access="!isAuthenticated()">Dallas 4th Ward Home and Visiting Teaching</sec:authorize>
					<c:if test="${reset}">D4 Home and Visiting Teaching</c:if>
				</a>
			</div>
			<div class="navbar-collapse collapse">
				<sec:authorize access="isAuthenticated()">

					<ul class="nav navbar-nav">
						<c:if test="${!reset}">

							<sec:authorize access="hasRole('council')">
								<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">
										Leaders <b class="caret"></b>
									</a>
									<ul class="dropdown-menu">
										<li class="${activeMenu ==  'users' ? 'active' : '' }"><a href="${user}/all">Users</a></li>
										<c:forEach items="${userOrganizations}" var="organization">
											<li class="${activeMenu ==  organization.name ? 'active' : '' }"><a href="${companion}/all/${organization.id}">
													<c:choose>
														<c:when test="${organization.id == 1 || organization.id == 2}">${organization.abbreviation} Home</c:when>
														<c:otherwise>Visiting</c:otherwise>
													</c:choose>
													Teaching
												</a></li>
										</c:forEach>
										<li class="${activeMenu ==  'visitHistory' ? 'active' : '' }"><a href="${visit}/history/3">Visit History</a></li>
										<li class="${activeMenu ==  'feedback' ? 'active' : '' }"><a href="${feedback}">Feedback</a></li>
									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('membership')">

								<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">
										Members <b class="caret"></b>
									</a>
									<ul class="dropdown-menu">
										<li class="${activeMenu ==  'allFamilies' ? 'active' : '' }"><a href="${family}/all">All Families</a></li>
										<li class="${activeMenu ==  'moved' ? 'active' : '' }"><a href="${family}/moved">Moved Families</a></li>
										<li class="${activeMenu ==  'unknown' ? 'active' : '' }"><a href="${family}/unknown">Unknown Families</a></li>
										<li class="${activeMenu ==  'summaryStats' ? 'active' : '' }"><a href="${dashboard}/summaryStatistics">Summary Statistics</a></li>
									</ul></li>

							</sec:authorize>

							<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">
									Visualize <b class="caret"></b>
								</a>
								<ul class="dropdown-menu">
									<li class="${activeMenu ==  'visitPercentage' ? 'active' : '' }"><a href="${dashboard}/visitPercentage">Visit Percentage</a></li>
									<%-- 									<li class="${activeMenu ==  'familyStatus' ? 'active' : '' }"><a href="${dashboard}/familyStatus">Family Status</a></li> --%>
								</ul></li>

							<li class="${activeMenu ==  'yourFamily' ? 'active' : '' }"><a href="${family}/you">Family</a></li>

							<sec:authentication property="principal.person.homeTeacher" var="homeTeacher" />
							<c:if test="${homeTeacher}">
								<li class="${activeMenu ==  'homeTeachingDetail' ? 'active' : '' }"><a href="${companion}/you/false">Home Teaching</a></li>
							</c:if>

							<sec:authentication property="principal.person.visitingTeacher" var="visitingTeacher" />
							<c:if test="${visitingTeacher}">
								<li class="${activeMenu ==  'visitingTeachingDetail' ? 'active' : '' }"><a href="${companion}/you/true">Visiting Teaching</a></li>
							</c:if>

						</c:if>
					</ul>

					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">
								<sec:authentication property="principal.person.fullName" />
								<b class="caret"></b>
							</a>
							<ul class="dropdown-menu">
								<li class="${activeMenu == 'user' ? 'active' : '' }"><a href="${user}/you">Your Account</a></li>
								<li><a href="#leaveFeedback" data-toggle="modal"> Feedback</a></li>
								<li><a href="<spring:url value="/logout"/>">
										<i class="glyphicon glyphicon-log-out"></i> Logout
									</a></li>
							</ul></li>
					</ul>

				</sec:authorize>

			</div>
		</div>
	</div>

	<!-- 	<div class="navbar navbar-fixed-top notification-navbar" id="notificationNavbar"> -->
	<div class="navbar navbar-fixed-top notification-navbar alert alert-danger col-md-4 col-md-offset-4" style="display: none;">
		<button type="button" class="close alert-close" aria-hidden="true">&times;</button>
		<p class="text">
			<strong>Warning!</strong>
		</p>
	</div>
	<div class="navbar navbar-fixed-top notification-navbar alert alert-success col-md-4 col-md-offset-4" style="display: none;">
		<button type="button" class="close alert-close" aria-hidden="true">&times;</button>
		<p class="text">
			<strong>Success!</strong>
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
		// function overriding alert messages, primarily to avoid error alerts from datatables (ie when ajax request gets server session timeout response)
		function alert(message) {
			console.log("From alert:  " + message);
		}

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

			$('.base-popover').popover();

			$.each($('.phone-number'), function() {
				$(this).html(getPhoneNumber($(this).text()));
			});

			//add placeholder to datatables search and preselect them.
			$('.dataTables_filter input').attr('placeholder', 'Search').focus();
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
				showModalError('<p>There was an unexpected error while saving your feedback.  If the issue continues please contact your organiation leader.');
			}
		}

		$(document).ajaxError(function(event, jqxhr, settings, exception) {
			handleAJAXLogin(jqxhr);
		});

		$(document).ajaxStart(function(event, xmlHttpRequest, ajaxOptions) {
			$('.btn-primary').prop('disabled', true);
		});

		$(document).ajaxComplete(function(event, xmlHttpRequest, ajaxOptions) {
			$('.btn-primary').prop('disabled', false);
		});

		function handleAJAXLogin(jqxhr) {
			if (jqxhr.responseText.indexOf('login') >= 0) {
				location.reload();
			}
		}
	</script>
</body>