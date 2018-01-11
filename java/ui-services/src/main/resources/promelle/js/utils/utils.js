var uiGridHeight = function() {
	var screenHeight = $(window).height() - 250;
	$('body').append(
			"<style>.gridTable{height:" + screenHeight
					+ "px !Important;}</style>");
}

var correctString = function(str) {
	var crtStr = str.replace("_", " ");
	// crtStr = crtStr.toLowerCase();
	return crtStr.replace(/\w\S*/g, function(txt) {
		//return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
		return txt.charAt(0).toUpperCase() + txt.substr(1);
	});
}

var capitalizeText = function(str) {
	str = str.toLowerCase();
	return str.replace(/\w\S*/g, function(txt) {
		return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
	});
}

var getThumbnailImg = function(imgUrl) {
	var imageName = imgUrl.substr(0, imgUrl.lastIndexOf('.'));
	var ext = imgUrl.substr(imgUrl.lastIndexOf('.'));
	var thumbImgUrl = imageName + "-thumb" + ext;
	return thumbImgUrl;
}

var statusString = function(str) {
	var crtStr = str.replace(" ", "_");
	crtStr = crtStr.toUpperCase();
	return crtStr;
}

var validateQty = function(evt) {
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
};

var toggleOffcanvas = function() {
	if (document.getElementById("mainContainer").className
			.match(/(?:^|\s)active(?!\S)/)) {
		document.getElementById("mainContainer").className = document
				.getElementById("mainContainer").className.replace(
				/(?:^|\s)active(?!\S)/g, '')
	} else {
		document.getElementById("mainContainer").className += " active";
	}
}

var differenceTimestamp = function(date1, date2) {
	var oneDay = 24 * 60 * 60 * 1000; // hours*minutes*seconds*milliseconds
	var firstDate = new Date(date1);
	var secondDate = new Date(date2);
	var diffDays = Math.round(Math.abs((firstDate.getTime() - secondDate
			.getTime())
			/ (oneDay)));

	return diffDays + 1;
}

var dateFieldOnTab = function(event) {
	var x = event.which || event.keyCode;
	if (x == 9 || x == 13) {
		return true;
	} else {
		return false;
	}
}

var getHost = function(CURRENT_HOST) {
	if (CURRENT_HOST == "development") {
		return true;
	} else if (CURRENT_HOST == "production") {
		return false;
	}
}

var replaceString = function(str, find, replace) {
	var correctStr = correctString(str);
	var crtStr = correctStr.match(",") ? correctStr.replace(new RegExp(find, 'g'), replace) : correctStr;
	return crtStr;
}

var upperText = function(str) {
	return str.toUpperCase();
}