(function($) {
	var settings;

	$.fn.noteDataTable = function(options) {
		settings = updateTableOptions(options);// $.extend({color: "#556b2f", backgroundColor: "white"}, options);
		setupEventBinding(this);
		initTable(this, settings);

		return this;
	}
	
	function updateTableOptions(options){
		options.tableOptions.columns.unshift({
			'class' : 'details-control',
			'orderable' : false,
			'data' : 'id',
			'render' : collapseIconRender
		});
		return options;
	}

	function initTable($this, settings) {
		$this.DataTable(settings.tableOptions);
	}

	function setupEventBinding($this) {
		// Add event listener for opening and closing details
		$this.on('click', 'td.details-control', function() {
			var $tr = $(this).closest('tr');
			var $row = $this.DataTable().row($tr);

			if ($row.child.isShown()) {
				hideRow($(this), $tr, $row);
			} else {
				showRow($(this), $tr, $row);
			}
		});
	}

	function hideRow($this, $tr, $row) {
		$this.find('.family-id').toggleClass('glyphicon-chevron-down glyphicon-chevron-up');
		$row.child.hide();
		$tr.removeClass('shown');
	}

	function showRow($this, $tr, $row) {
		var familyId = $this.find('.family-id').data('familyId');

		// close all other rows
		$('tr.shown').each(function() {
			$(this).find('td.details-control').click();
		});

		// open this row
		$row.child(createNoteRow(familyId)).show();
		$tr.addClass('shown');
		$('#noteWrapper' + familyId).flatNotes({
			viewContainer : $('#columns' + familyId),
			familyId : familyId
		});
		$this.find('.family-id').toggleClass('glyphicon-chevron-down glyphicon-chevron-up');
	}

	function createNoteRow(familyId) {
		return '<div id="noteWrapper' + familyId + '"><div class="clearfix"></div><div data-columns id="columns' + familyId + '" class="notes"></div></div>';
	}

	function collapseIconRender(data, type, full) {
		return '<i class="glyphicon glyphicon-chevron-down family-id" data-family-id="' + data + '"></i>'
	}

}(jQuery));