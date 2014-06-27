<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:url var="resources" value="/resources" />

<t:mainPage activeMenu="unknown" pageTitle="Unknown Families" pageHeader="Unknown" pageSubheader="Families">

	<style type="text/css">

@media screen and (max-width: 479px) {
	#columns[data-columns]::before {
		content: '1 .col-xs-12';
	}
}

@media screen and (min-width: 480px) and (max-width: 767px) {
	#columns[data-columns]::before {
		content: '2 .col-xs-6';
	}
}

@media screen and (min-width: 768px) and (max-width: 991px) {
	#columns[data-columns]::before {
		content: '2 .col-sm-6';
	}
}

@media screen and (min-width: 992px) and (max-width: 1199px) {
	#columns[data-columns]::before {
		content: '3 .col-md-4';
	}
}

@media screen and (min-width: 1200px) {
	#columns[data-columns]::before {
		content: '4 .col-lg-3';
	}
}
</style>

	<%-- 	<table id="familyTable" class="table table-striped table-hover table-bordered" width="100%"></table> --%>

	<div class="container">
		<h1 class="col-xs-12">Books by Ernest Hemingway</h1>

		<div data-columns id="columns">
			<div></div>
			<div></div>
			<div></div>
		</div>
	</div>

	<script src="${resources}/js/salvattore.min.js"></script>
	<script>
		function append(title, content) {
			var grid = document.querySelector('#columns');
			var item = document.createElement('div');
			var h = '<div class="panel panel-default">';
			h += '<div class="panel-body">';
			h += content;
			h += '</div>';
			h += '<div class="panel-footer">' + title + '</div>'
			h += '</div>';
			salvattore['append_elements'](grid, [ item ])
			item.outerHTML = h;
		}

		$.getJSON("https://www.googleapis.com/books/v1/volumes?q=inauthor:Ernest+Hemingway&callback=?", function(data) {
			$(data.items).each(function(i, book) {
				append(book.volumeInfo.title, book.volumeInfo.description);
			});
		});
	</script>

	<script type="text/javascript">
		$(document).ready(function() {

			//setupEventBinding();

			//setupFamilyTable();

		});

		function setupEventBinding() {
			// Add event listener for opening and closing details
			$('#familyTable').on('click', 'td.details-control', function() {
				var $tr = $(this).closest('tr');
				var $row = $('#familyTable').DataTable().row($tr);

				if ($row.child.isShown()) {
					$(this).html('<i class="glyphicon glyphicon-chevron-down"></i>');
					// This row is already open - close it
					$row.child.hide();
					$tr.removeClass('shown');
				} else {
					$(this).html('<i class="glyphicon glyphicon-chevron-up"></i>');
					// Open this row
					$row.child(create()).show();
					$tr.addClass('shown');

				}
			});
		}

		function create() {
			return '<div id="freetile" data-columns>' + '<div class="note">A lovely thing to see through the paper windows hole, the Galaxy.</div>' + '<div class="note">poop my pantssuite</div>' + '<div class="note"></div>'
					+ '<div class="note"></div>' + '<div class="note"></div>' + '<div class="note">lkjasdlfjasldjf lkasjdf lkasdlf asdl jflas;dj flkajsd fasdfsadfasdfsadfasdfasdfsdfsdafasdfasd</div>' + '<div class="note"></div>' + '</div>';
		}

		function format(data) {
			console.log(data);
			var items = [
					"A lovely thing to see <br /> through the paper window\'s hole, <br /> the Galaxy.",
					"This pulsar map was sent as part of the plaques on Pioneers 10 and 11 and Voyager. It shows the location of earth's solar system with respect to 14 pulsars, whose precise periods are given. The two circles in the middle left is a drawing of the hydrogen atom in its two lowest states, with a connecting line and digit 1 to indicate that the time interval associated with the transition from one state to the other is to be used as the fundamental time scale.",
					"Tardigrades are frequently referred to as water bears or moss piglets. Pictured here in a color-enhanced electron micrograph, a millimeter-long tardigrade crawls on moss.",
					"There are many references to ravens in legends and literature. Most of these refer to the widespread common raven. Because of its black plumage, croaking call, and diet of carrion, the raven has long been considered a bird of ill omen and of interest to creators of myths and legends.",
					"The nitrogen in our DNA, the calcium in our teeth, the iron in our blood, the carbon in our apple pies were made in the interiors of collapsing stars. We are made of starstuff.",
					"lkjasdlfjasldjf lkasjdf lkasdlf asdl jflas;dj flkajsd fasdfsadfasdfsadfasdfasdfsdfsdafasdfasd",
					"Tardigrades are frequently referred to as water bears or moss piglets. Pictured here in a color-enhanced electron micrograph, a millimeter-long tardigrade crawls on moss." ];
			for (var i = 0; i < items.length; i++) {

			}
		}

		function setupFamilyTable() {

			$('#familyTable').DataTable({
				'ajax' : '<spring:url value="/family/getAllUnknownFamilies/"/>',
				'order' : [ [ 0, 'asc' ] ],
				'data' : [],
				'columns' : [ {
					'class' : 'details-control',
					'orderable' : false,
					'data' : null,
					'defaultContent' : '<i class="glyphicon glyphicon-chevron-down"></i>'
				}, {
					'title' : 'Family Name',
					'data' : 'familyName',
					'width' : '15%',
					'render' : familyNameRender
				}, {
					'title' : 'Organizations',
					'data' : 'organizations',
					'render' : setupOrganizations,
					'width' : '15%'
				}, {
					'title' : 'Address',
					'data' : 'address',
					'width' : '20%',
					'render' : addressRender,
					'sClass' : 'hidden-xs'
				}, {
					'title' : 'Phone Numbers',
					'data' : 'phoneNumbers',
					'width' : '20%',
					'sClass' : 'hidden-xs hidden-sm',
					'render' : setupPhoneNumbers
				}, {
					'title' : 'HT Companions',
					'data' : 'homeTeachingCompanions',
					'width' : '15%',
					'render' : setupTeachers
				}, {
					'title' : 'VT Companions',
					'data' : 'visitingTeachingCompanions',
					'width' : '15%',
					'render' : setupTeachers
				} ],
				'language' : {
					'infoEmpty' : 'No families to show',
					'emptyTable' : 'There are no moved families yet.'
				}
			});
		}

		function familyNameRender(data, type, full) {
			return '<a href="<spring:url value="/family/detail/"/>' + full.id + '">' + getFamilyAndHeadNames(data, full.people) + '</a>';
		}

		function setupTeachers(data, type, full) {
			var html = '';
			if (data != null) {
				html = '<a href="<spring:url value="/companion/detail/"/>' + data.id + '">' + data.allTeachers + '</a>';
			}
			return html;
		}
	</script>
</t:mainPage>