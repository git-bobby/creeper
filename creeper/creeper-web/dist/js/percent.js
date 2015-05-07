$(document).ready(function() {
	$("#pb1").progressBar();
});

function showPercentInfo(percent) {
	$("#pb1").progressBar(percent);
}