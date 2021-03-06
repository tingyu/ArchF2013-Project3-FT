$(function() {
	$.fn.editable.defaults.mode = 'inline';
});

var makeEditable = function(event, nameDict, callUrl) {
	event.stopPropagation();
	$.each(nameDict, function(key, val) {
		
		$('#' + key).editable({
			type : 'text',
			pk : val,
			url : callUrl,
			success : function(response) {
				document.location.reload(true);
			}
		});
		$('#' + key).click();
	});
};