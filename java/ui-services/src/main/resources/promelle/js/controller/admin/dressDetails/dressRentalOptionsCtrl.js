'use strict';

peAdminApp
	.controller(
	'dressRentalOptionsCtrl',
	[
		'$scope',
		'productService',
		'$stateParams',
		'peProductByIdFcty',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, productService, $stateParams, peProductByIdFcty, CURRENT_HOST, CURRENT_VERSION) {

			var getRentalDetails = function () {


				$scope.isDev = getHost(CURRENT_HOST);

				peProductByIdFcty
					.get(
					{
						id: $stateParams.productId,
						timestamp: $scope.isDev ? new Date().getTime() : null,
						version: !$scope.isDev ? CURRENT_VERSION : null
					},
					function (response) {
						$scope.isReadyToActive = true;
						$scope.rentalOptions = response.data;

						$scope.listAs = $scope.rentalOptions.listAs ? capitalizeText($scope.rentalOptions.listAs)
							: '';

						if ($scope.rentalOptions.status == 1
							|| $scope.rentalOptions.status == 0) {
							$scope.isReadyToActive = false;
						}

						$scope.fourDayRentalPrice = $scope.rentalOptions.rentalPrice ? $scope.rentalOptions.rentalPrice
							: '';
						$scope.originalPrice = $scope.rentalOptions.originalPrice ? $scope.rentalOptions.originalPrice
							: '';
						$scope.rentalPeriod = $scope.rentalOptions.rentalPeriod ? $scope.rentalOptions.rentalPeriod
							+ " days"
							: '';

						if ($scope.rentalOptions.availForOtherPeriod) {
							$scope.rentalPeriod = $scope.rentalPeriod
								+ ' / 8 days';
							$scope.eightDayRentalPrice = $scope.fourDayRentalPrice * 1.15;
						}
					});

			};

			// getRentalDetails();

			$scope.stateChanged = function (field) {
				productService.setNotifyFields(field, "text");
			};
		}]);