'use strict';

peAdminApp.service('authService', [
		'$http',
		'$q',
		'PROMELLE_USER_URL',
		'$timeout',
		'localStorageService',
		function($http, $q, PROMELLE_USER_URL, $timeout, localStorageService) {

			this.authentication = false;

			this.loginUser = function(queryParams) {
				var defer = $q.defer(), query = JSON.stringify(queryParams);

				$http.post(PROMELLE_USER_URL + '/user/login', query).success(
						function(data) {
							defer.resolve(data);
						}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.setUserLoggedIn = function() {
				this.authentication = localStorageService.get('auth') ? localStorageService.get('auth') : isAuthenticated;
			};

			this.getUserLoggedIn = function() {
				var isAuthenticated = localStorageService.get('auth') ? localStorageService.get('auth') : this.authentication;
				return isAuthenticated;
			};

		} ]);
