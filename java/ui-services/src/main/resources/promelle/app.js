'use strict';

var peAdminApp = angular.module('peAdminApp', ['toaster', 'ngResource',
	'ui.router', 'ui.bootstrap', 'ui.grid', 'ui.grid.pagination',
	'ui.grid.resizeColumns', 'LocalStorageModule', 'datatables',
	'bootstrapLightbox', 'ngFileUpload', 'angular-loading-bar', 'ui.tinymce', 'textAngular','angularUtils.directives.dirPagination','ui.grid.selection', 'ngImgCrop']);

peAdminApp.run([
	'$rootScope',
	'localStorageService',
	'authService',
	'$state',
	function ($rootScope, localStorageService, authService, $state) {
		$rootScope.showSidebar = localStorageService.get('showSidebar');
		$rootScope.$on('$stateChangeStart', function (event, toState, toParams,
			fromState, fromParams) {
			// app authentication
			if (toState.name !== 'login') {
				if (!authService.getUserLoggedIn()) {
					event.preventDefault();
					$state.go('login');
				} else {
					if (toState.name !== '404') {
						localStorageService.set('showSidebar', true);
					} else {
						localStorageService.set('showSidebar', false);
					}
				}
			} else {
				return false;
			}
			
			//set active class to route
			$rootScope.isActive = toState.routeName;
		});
	}]);

peAdminApp.config(['cfpLoadingBarProvider', function (cfpLoadingBarProvider) {
	cfpLoadingBarProvider.includeSpinner = false;
	cfpLoadingBarProvider.latencyThreshold = 1000;
}]);


peAdminApp.constant('EMAIL_FROM_ADDRESS', 'contact@promelle.com');

peAdminApp.constant('CURRENT_VERSION', '2.0.0');

peAdminApp.factory('CURRENT_HOST', function ($location) {
	if ($location.absUrl().split('/')[2].indexOf('api') != -1) {
		return "production";
	} else {
		// if ($location.absUrl().split('/')[2].contains('dev'))
		return "development";
	}
});

peAdminApp.factory('PROMELLE_USER_URL', function ($location) {
	return 'http://' + $location.absUrl().split('/')[2] + '/service';
});
