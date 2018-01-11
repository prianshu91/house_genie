'use strict';

promelleApp.directive('headerDirective', [
		function() {
			var p = {};

			p.transclude = true;
			p.replace = true;
			p.restrict = 'E';
			p.templateUrl = 'public/view/header.html';

			p.controller = function() {}
			return p;
		} ]);