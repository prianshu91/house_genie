'use strict';

peAdminApp
	.controller(
	'userProfileCtrl',
	[
		'$scope',
		'$stateParams',
		'adminService',
		'$uibModal',
		'peUserByIdFcty',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, $stateParams, adminService, $uibModal,
			peUserByIdFcty, CURRENT_HOST, CURRENT_VERSION) {

			$scope.isDev = getHost(CURRENT_HOST);

			peUserByIdFcty
				.get(
				{
					id: $stateParams.userId,
					timestamp: $scope.isDev ? new Date().getTime() : null,
					version: !$scope.isDev ? CURRENT_VERSION : null
				},
				function (response) {
					$scope.userData = response.data;
					$scope.userData.homeAddress.addressLine1 = $scope.userData.homeAddress.addressLine1 ? $scope.userData.homeAddress.addressLine1
						+ ', '
						: '';
					$scope.userData.homeAddress.addressLine2 = $scope.userData.homeAddress.addressLine2 ? $scope.userData.homeAddress.addressLine2
						+ ', '
						: '';
					$scope.userData.homeAddress.city = $scope.userData.homeAddress.city ? $scope.userData.homeAddress.city
						+ ', '
						: '';
					$scope.userData.homeAddress.state = $scope.userData.homeAddress.state ? $scope.userData.homeAddress.state
						+ ', '
						: '';
					$scope.userData.homeAddress.zipCode = $scope.userData.homeAddress.zipCode ? $scope.userData.homeAddress.zipCode
						: '';

					if ($scope.userData.school) {
						$scope.userData.schoolName = $scope.userData.school.schoolName;
						var schoolAddress1 = $scope.userData.school.schoolAddress.addressLine1 ? $scope.userData.school.schoolAddress.addressLine1 + ', ' : '';
						var schoolAddress2 = $scope.userData.school.schoolAddress.addressLine2 ? $scope.userData.school.schoolAddress.addressLine2 + ', ' : '';
						var schoolCity = $scope.userData.school.schoolAddress.city ? $scope.userData.school.schoolAddress.city + ', ' : '';
						var schoolState = $scope.userData.school.schoolAddress.state ? $scope.userData.school.schoolAddress.state + ' ' : '';
						var schoolZipCode = $scope.userData.school.schoolAddress.zipCode ? $scope.userData.school.schoolAddress.zipCode : '';
						$scope.userData.schoolAddress = schoolAddress1 + schoolAddress2 + schoolCity + schoolState + schoolZipCode;
					}
				});

			$scope.emailUser = function (emailId) {
				var modalInstance = $uibModal.open({
					templateUrl: 'views/admin/emailUser.html',
					controller: 'emailUserCtrl',
					$scope: $scope,
					resolve: {
						data: function () {
							return $scope.userData;
						}
					}
				});
			};

		}]);