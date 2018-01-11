peAdminApp.run([
		'$rootScope',
		'$location',
		'$window',
		'$state',
		'$httpBackend',
		'localStorageService',
		'$templateCache',
		function($rootScope, $location, $window, $state, $httpBackend,
				localStorageService, $templateCache) {

			$rootScope.$on("$stateChangeStart", function(event, next, current) {
				if (typeof (current) !== 'undefined') {
					$templateCache.remove(current.templateUrl);
				}
				$rootScope.error = null;
				$rootScope.showSidebar = localStorageService.get('showSidebar');
			});

			var actions = [];

			// $httpBackend.whenGET("/api/actions").respond(actions);
			// $httpBackend.whenGET().passThrough();
			// $httpBackend.whenPOST().passThrough();
			// $httpBackend.whenDELETE().passThrough();
			// $httpBackend.whenPUT().passThrough();
		} ]);
