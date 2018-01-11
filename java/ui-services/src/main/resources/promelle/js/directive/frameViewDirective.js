'use strict';

peAdminApp.directive('frameView', [ function() {
	var p = {};

	p.transclude = true;
	p.replace = true;
	p.restrict = 'E';
	p.templateUrl = 'views/frameView.html';

	p.controller = function($scope) {

	}
	return p;
} ]);