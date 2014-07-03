<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:url var="resources" value="/resources" />

<t:notePage activeMenu="unknown" pageTitle="Unknown Families" pageHeader="Unknown" pageSubheader="Families">

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

	<table id="familyTable" class="table table-striped table-hover table-bordered" width="100%"></table>

	<script type="text/javascript">

		$(document).ready(function() {

			setupFamilyTable();

		});

		function setupFamilyTable() {

			$('#familyTable').noteDataTable({ tableOptions : {
				'ajax' : '<spring:url value="/family/getAllUnknownFamilies/"/>',
				'order' : [ [ 1, 'asc' ] ],
				'data' : [],
				'columns' : [ {
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
			}});
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
</t:notePage>