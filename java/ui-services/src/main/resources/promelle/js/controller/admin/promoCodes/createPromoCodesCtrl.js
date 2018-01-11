'use strict';

peAdminApp.controller('createPromoCodesCtrl', [
'$scope',
'adminService',
'$stateParams',
'$uibModal',
'$state',
'CURRENT_HOST',
'CURRENT_VERSION',
function($scope, adminService, $stateParams, $uibModal, $state, CURRENT_HOST, CURRENT_VERSION) {
    $scope.today = function() {
        $scope.dt = new Date();
        $scope.startMinDate = $scope.dt;
    };
    $scope.today();

    $scope.clear = function() {
        $scope.dt = null;
    };

    $scope.open1 = function() {
        $scope.popup1.opened = true;
    };

    $scope.open2 = function() {
        $scope.endMinDate = $scope.dt;
        $scope.popup2.opened = true;
    };

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.format = 'dd-MMMM-yyyy';

    $scope.popup1 = {
        opened: false
    };
    $scope.popup2 = {
        opened: false
    };
} ]);