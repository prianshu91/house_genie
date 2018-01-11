'use strict';

peAdminApp.controller('userDetailsCtrl', [
		'$scope',
		'peUserByIdFcty',
		'$stateParams',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function($scope, peUserByIdFcty, $stateParams, CURRENT_HOST,
				CURRENT_VERSION) {

			$scope.setFlag = function(flag) {
				$scope[flag] = true;
			};

			$scope.isDev = getHost(CURRENT_HOST);

			peUserByIdFcty.get({
				id : $stateParams.userId,
				timestamp : $scope.isDev ? new Date().getTime() : null,
				version : !$scope.isDev ? CURRENT_VERSION : null
			}, function(response) {
				$scope.userNameDetail = response.data.username + "'s" + " Details";
				$scope.userEmail = response.data.email;
			});

		} ]);