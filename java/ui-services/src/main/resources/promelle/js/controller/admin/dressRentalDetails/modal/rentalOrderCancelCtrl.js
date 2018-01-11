/**
 * 
 */
peAdminApp.controller('rentalOrderCancelCtrl', ['$scope', '$uibModalInstance',
	'$log', '$state', 'toaster', '$uibModal',
	function ($scope, $uibModalInstance, $log, $state, toaster, $uibModal) {
		'use strict';

		$scope.cancelOrder = function () {
			$uibModalInstance.close($scope.user.type);
		};

		$scope.close = function () {
			$uibModalInstance.close();
		};

	}]);