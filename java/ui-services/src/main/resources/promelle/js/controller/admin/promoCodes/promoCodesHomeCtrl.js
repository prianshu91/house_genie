'use strict';

peAdminApp.controller('promoCodesHomeCtrl', [
'$scope',
'adminService',
'$stateParams',
'$uibModal',
'$state',
'CURRENT_HOST',
'CURRENT_VERSION',
function($scope, adminService, $stateParams, $uibModal, $state, CURRENT_HOST, CURRENT_VERSION) {
	$scope.setFlag = function(flag) {
		$scope[flag] = true;
	};
} ]);