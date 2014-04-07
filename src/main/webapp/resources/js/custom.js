function getMapUrlPrefix() {
	if (BrowserDetect.OS == 'iOS') {
		return 'http://maps.apple.com/?q=';
	} else {
		return 'http://maps.google.com/maps?daddr=';
	}
}

function getMapTarget() {
	if (BrowserDetect.OS == 'Windows') {
		return 'target="_blank"';
	} else {
		return '';
	}
}

function getPhoneNumber(number) {
	return '<a href="tel:' + number + '">' + number + '</a>';
}

function getPhoneNumberWithName(number, name) {
	return '<a href="tel:' + number + '">' + name + '</a>';
}

function addressRender(address) {
	return address != null ? ('<a href="' + getMapUrlPrefix() + address + '" ' + getMapTarget() + '>' + address + '</a>') : '';
}

function getFamilyAndHeadNames(familyName, people) {
	var first = true;
	for (var i = 0; i < people.length; i++) {
		if (people[i].headOfHousehold) {
			if (first) {
				familyName += ', ';
				first = false;
			} else {
				familyName += ' and ';
			}

			familyName += people[i].firstName;
		}
	}
	return familyName;
}

function getChildrenNames(people) {
	var names = '';
	var first = true;
	for (var i = 0; i < people.length; i++) {
		if (!people[i].headOfHousehold) {
			if (!first) {
				names += ', ';
			}
			names += people[i].firstName;
			first = false;
		}
	}
	return names;
}

function setupTrueFalseAsYesNo(data, type, full) {
	if (data == null) {
		return '';
	}
	return data == 'true' || data == true && data != false && data != 'false' ? 'Yes' : 'No';
}

function setupTrueFalseAsYesNoOpposite(data, type, full) {
	return data == 'true' || data == true && data != false && data != 'false' ? 'No' : 'Yes';
}

function setupMonthYear(data, type, full) {
	return data != null ? dateFormat(data, "mmmm yyyy") : '';
}

function setupFullDate(data, type, full) {
	return data != null ? dateFormat(data, "m/d/yy h:MM TT Z") : '';
}

function setupPersonFullName(person, type, full) {
	var name = person.firstName;
	if (person.family != null) {
		name += (' ' + person.family.familyName);
		name = '<a href="family/detail/' + person.family.id + '">' + name + '</a>';
	}
	return name;
}

function setupPhoneNumbers(data, type, full) {
	var html = '';
	for (var i = 0; i < data.length; i++) {
		html += '<span class="hidden-xs hidden-sm pull-left">' + data[i].firstName + ': ' + getPhoneNumber(data[i].phoneNumber);
		if (i != data.length - 1) {
			html += ',</span> ';
		} else {
			html += '</span> ';
		}
		html += '<span class="hidden-md hidden-lg pull-left">' + getPhoneNumberWithName(data[i].phoneNumber, data[i].firstName);
		if (i != data.length - 1) {
			html += ',</span> ';
		} else {
			html += '</span> ';
		}
	}
	return html;
}

function setupOrganizations(data, type, full) {
	var html = '';
	for (var i = 0; i < data.length; i++) {
		if (i != 0) {
			html += ', ';
		}
		html += data[i].name;
	}
	return html;
}

function isValidEmail(email) {
	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	if (emailReg.test(email)) {
		return true;
	} else {
		return false;
	}
}

function getPercentage(number) {
	return Math.round(new Number(number) * 100) + '%';
}

function resetFormElements(form) {
	$('#' + form + ' :input').not(':button, :submit, :reset, :hidden').removeAttr('checked').removeAttr('selected').not(':checkbox, :radio').val('');
}

function setupAlertBinding() {
	$('.alert-close').click(function() {
		$(this).closest('.alert').hide().find('.text').html('');
	});
}

function setupModalAlerts() {
	var alertDanger = '<div class="alert alert-danger" style="display:none;">' + '<button type="button" class="close alert-close" aria-hidden="true">&times;</button>'
			+ '<p class="text"><strong>Warning!</strong> Best check yo self, youre not looking too good.</p>' + '</div>';
	var alertSuccess = '<div class="alert alert-success" style="display:none;">' + '<button type="button" class="close alert-close" aria-hidden="true">&times;</button>'
			+ '<p class="text"><strong>Warning!</strong> Best check yo self, youre not looking too good.</p>' + '</div>';

	$('.modal-body').prepend(alertDanger).prepend(alertSuccess);
}

function showModalError(message) {
	showModalAlert(message, '.alert-danger');
}

function showModalSuccess(message) {
	showModalAlert(message, '.alert-success');
}

function showModalAlert(message, type) {
	$('.modal-body').find(type).show().find('.text').html(message);
}

function resetModalAlerts() {
	$('.modal-body .alert').hide().find('.text').html('');
}

function showNotificationError(message) {
	showNotificationAlert(message, '.alert-danger');
}

function showNotificationSuccess(message) {
	showNotificationAlert(message, '.alert-success');
}

function showNotificationAlert(message, type) {
	$('.notification-navbar' + type).show().find('.text').html(message);
}