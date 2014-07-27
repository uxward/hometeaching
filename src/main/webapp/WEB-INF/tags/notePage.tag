<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ attribute name="pageHeader" required="true"%>
<%@ attribute name="pageTitle" required="true"%>
<%@ attribute name="activeMenu" required="true"%>
<%@ attribute name="pageSubheader" required="false"%>

<spring:url var="resources" value="/resources" />

<t:mainPage activeMenu="${activeMenu}" pageTitle="${pageTitle}" pageHeader="${pageHeader}" pageSubheader="${pageSubheader}">
	<link rel="stylesheet" href="${resources}/css/salvattore.css" />

	<jsp:doBody />

	<div class="hidden" id="add-note-cloner">
		<br />
		<div class="col-md-8 col-md-offset-2 col-lg-8 col-lg-offset-2 col-sm-8 col-sm-offset-2 col-xs-10 col-xs-offset-1">
			<div class="panel panel-default note-panel">
				<div class="panel-body">
					<form>
						<textarea class="flat-input note-text" style="width: 100%" placeholder="Add Note" name="note"></textarea>
						<input type="hidden" name="id" class="note-id" /> <input type="hidden" name="visibleRole" class="note-visibility" /><input type="hidden" name="familyId" class="family-id" />
						<div class="hidden note-actions">
							<div class="pull-right">
								<input type="button" value="Save" class="btn btn-primary save-note" />
							</div>
							<div class="dropdown pull-right">
								<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
									Visibility<span class="text"></span> <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" role="menu">
									<c:forEach items="${roles}" var="role">
										<li role="presentation"><a role="menuitem" tabindex="-1" data-role="${role.role}" href="javascript:;">${role.display}</a></li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="hidden" id="view-note-cloner">
		<div class="panel panel-default note-panel">
			<div class="panel-heading">
				Visibility<span class="text"></span>
			</div>
			<div class="panel-body">
				<form>
					<textarea class="flat-input note-text" style="width: 100%" name="note" placeholder="Add Note"></textarea>
					<input type="hidden" name="id" class="note-id" /> <input type="hidden" name="visibleRole" class="note-visibility" /><input type="hidden" name="familyId" class="family-id" />
					<div class="hidden note-actions">
						<div class="pull-right">
							<input type="button" value="Save" class="btn btn-primary save-note" />
						</div>
						<div class="dropdown pull-right">
							<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
								Visibility<span class="text"></span> <span class="caret"></span>
							</button>
							<ul class="dropdown-menu" role="menu">
								<c:forEach items="${roles}" var="role">
									<li role="presentation"><a role="menuitem" tabindex="-1" data-role="${role.role}" href="#">${role.display}</a></li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		(function($) {
			var settings;

			$.fn.flatNotes = function(options) {

				settings = options;//$.extend({color: "#556b2f", backgroundColor: "white"}, options);
				initNotes(this, settings.familyId);

				return this;
			}

			function initNotes($wrapperContainer, familyId) {
				setupAddNote($wrapperContainer, familyId);
				setupViewNoteContainer($wrapperContainer);
				$.getScript('${resources}/js/salvattore.min.js').done(function() {
					setupViewNotes($wrapperContainer, familyId);
				});
				setupNoteEventBinding($wrapperContainer);
			}

			function setupNoteEventBinding($wrapperContainer) {
				//display note visibility selection from dropdown select
				$wrapperContainer.on('click', '.note-actions .dropdown-menu li a', function() {
					var $this = $(this);
					$this.parents('form').find('.note-visibility').val($this.data('role'));
					$this.parents('.note-panel').find('.text').text(': ' + $this.text());
				});

				//handle note save
				$wrapperContainer.on('click', '.save-note', function() {
					var $note = $(this).parents('.note-panel');
					if (canSaveNote($note)) {
						saveNote($note, $wrapperContainer);
					}
				});

				//hide/show note actions when click in/out of note panel
				$(document).click(function(e) {
					if (!$(e.target).parents('.note-actions').length) {
						$('.note-panel').each(function() {
							$(this).removeClass('panel-danger').find('.note-actions').addClass('hidden');
						});
					}
				});

				//hide/show note actions when click in/out of note panel
				$wrapperContainer.on('click', '.note-panel', function(e) {
					var $thisPanel = $(this);
					$('.note-panel').each(function() {
						if ($thisPanel != $(this)) {
							$(this).removeClass('panel-danger').find('.note-actions').addClass('hidden');
						}
					});
					$(this).find('.note-actions').removeClass('hidden');
					if (!$(e.target).parents('.note-actions').length) {
						e.stopPropagation();
					}
				});
			}

			function setupViewNotes($wrapperContainer, familyId) {
				$.getJSON('<spring:url value="/note/getByFamily/' + familyId + '"/>', function(data) {
					$(data).each(function(i, note) {
						prepend(note, familyId, $wrapperContainer);
					});
				});
			}

			function prepend(note, familyId, $wrapperContainer) {
				var $item = $('<div></div>');
				salvattore['prepend_elements']($wrapperContainer.find('.notes').first()[0], [ $item[0] ]);
				var $clone = $('#view-note-cloner .note-panel').clone();
				$clone.find('.note-text').val(note.note);
				$clone.find('.family-id').val(familyId);
				$clone.find('.note-visibility').val(note.role.role);
				$clone.find('.note-id').val(note.id);
				$clone.find('.text').text(': ' + note.role.display);
				$clone.appendTo($item);
				$('.flat-input').autogrow();
			}

			function setupAddNote($wrapperContainer, familyId) {
				var $clone = $('#add-note-cloner').children().clone();
				$clone.find('.family-id').val(familyId);
				$clone.prependTo($wrapperContainer);
				$('.flat-input').autogrow();
			}

			function setupViewNoteContainer($wrapperContainer) {
				$('<div class="clearfix"></div><div data-columns class="notes"></div>').appendTo($wrapperContainer);
			}

			function canSaveNote($note) {
				var valid = true;

				var $text = $note.find('.note-text');
				if ($.trim($text.val()).length < 1) {
					valid = false;
				}

				var $role = $note.find('.note-visibility');
				if (!($.trim($role.val()).length > 0)) {
					valid = false;
				}

				if (valid) {
					$note.removeClass('panel-danger');
				} else {
					$note.addClass('panel-danger');
				}

				return valid;
			}

			function saveNote($note, $wrapperContainer) {
				$.ajax({
					type : 'POST',
					url : '<spring:url value="/note/save"/>',
					data : $note.find('form').serialize(),
					success : function(data) {
						if ($.trim($note.find('.note-id').val()).length < 1) {
							//add note row to table
							prepend(data, data.familyId, $wrapperContainer);

							//reset add note
							$note.find('.note-visibility').data('role', '').find('.text').text('');
							$note.find('.note-text').val('');
						}

						$('.note-panel').each(function() {
							$(this).removeClass('panel-danger').find('.note-actions').addClass('hidden');
						});
					}
				});
			}

		}(jQuery));
	</script>
</t:mainPage>