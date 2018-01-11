'use strict';

peAdminApp.controller('promoCodeInUseCtrl', [
'$scope',
'adminService',
'$stateParams',
'$uibModal',
'$state',
'CURRENT_HOST',
'CURRENT_VERSION',
function($scope, adminService, $stateParams, $uibModal, $state, CURRENT_HOST, CURRENT_VERSION) {
    $scope.oneAtATime = true;
    
    $scope.status = {
        isCustomHeaderOpen: false,
        isFirstOpen: true,
        isFirstDisabled: false
    };
} ]);