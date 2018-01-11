'use strict';

peAdminApp.directive('headerDirective', [ '$state', 'localStorageService',
		'$rootScope', 'authService',
		function($state, localStorageService, $rootScope, authService) {
			var p = {};

			p.transclude = true;
			p.replace = true;
			p.restrict = 'E';
			p.templateUrl = 'views/header.html';

			p.controller = function($scope) {

				function delete_cookie(name) {
					document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				}

				$rootScope.showUserName = localStorageService.get('username');
				$scope.logout = function() {
					delete_cookie('PROMELLE_SESS_UUID');
					localStorageService.clearAll();
					localStorageService.set('auth', false);
					// authService.setUserLoggedIn(false);
					$state.go("login");
				}
			}
			return p;
		} ]);