'use strict';

peAdminApp
	.controller(
	'dressListingDetailsCtrl',
	[
		'$scope',
		'productService',
		'adminService',
		'$stateParams',
		'peProductByIdFcty',
		'peUserByIdFcty',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, productService, adminService, $stateParams,
			peProductByIdFcty, peUserByIdFcty, CURRENT_HOST, CURRENT_VERSION) {

			var getListingDetails = function () {
				$scope.isDev = getHost(CURRENT_HOST);

				peProductByIdFcty
					.get(
					{
						id: $stateParams.productId,
						timestamp: $scope.isDev ? new Date().getTime() : null,
						version: !$scope.isDev ? CURRENT_VERSION : null
					},
					function (response) {
						$scope.getData = response.data;

						peUserByIdFcty
							.get(
							{
								id: $scope.getData.ownerId,
								timestamp: $scope.isDev ? new Date().getTime() : null,
								version: !$scope.isDev ? CURRENT_VERSION : null
							},
							function (response) {
								$scope.isReadyToActive = true;
								$scope.userName = response.data.username;
								$scope.fullName = response.data.name;
								$scope.dressData = $scope.getData;
								// $scope.size = $scope.dressData.size ? correctString($scope.dressData.size)
								// 	: null;
								// $scope.fit = $scope.dressData.fit ? $scope.dressData.fit
								// 	: "N/A";
								if ($scope.dressData.status == 1
									|| $scope.dressData.status == 0) {
									$scope.isReadyToActive = false;
								}
							});

					});
			};

			// getListingDetails();

			$scope.stateChanged = function (field) {
				productService.setNotifyFields(field, "text");
			}
		}]);