'use strict';

peAdminApp.controller('loginCtrl', [
		'$scope',
		'$rootScope',
		'$location',
		'localStorageService',
		'$state',
		'authService',
		function($scope, $rootScope, $location, localStorageService, $state,
				authService) {
			localStorageService.set('showSidebar', false);

			$rootScope.showSidebar = localStorageService.get('showSidebar');
			$scope.loginErrorMsg = false;
			var strKey;
			$scope.login = function() {
				$scope.submitted = true;

				if ($scope.username && $scope.password) {
					if ($scope.username.indexOf("@") > -1) {
						strKey = "email"
					} else {
						strKey = "mobile"
					}
					var query = {
					"password" : $scope.password,
					"portalName" : 'promelle',
					"admin": true
					};
					query[strKey] = $scope.username;
				
					authService.loginUser(query).then(function(response) {
						if (response.data) {
							localStorageService.set('auth', true);
							//authService.setUserLoggedIn(true);
							$scope.loginErrorMsg = false;
							localStorageService.set('username', response.data.name);
							localStorageService.set('showSidebar', true);
							$state.go("admin");
							$rootScope.showSidebar = localStorageService.get('showSidebar');
							$rootScope.showUserName = localStorageService.get('username');
						} else {
							$scope.loginErrorMsg = true;
						}
					});
				}

				/*if ($scope.username == 'promelle123' && $scope.password == '12345') {
					$scope.loginErrorMsg = false;
					localStorageService.set('username', $scope.username);
					localStorageService.set('showSidebar', true);
					$state.go("admin");
					$rootScope.showSidebar = localStorageService.get('showSidebar');
					$rootScope.showUserName = localStorageService.get('username');
				} else {
					if ($scope.username && $scope.password) {
						$scope.loginErrorMsg = true;
					}*/
			};
		} ]);