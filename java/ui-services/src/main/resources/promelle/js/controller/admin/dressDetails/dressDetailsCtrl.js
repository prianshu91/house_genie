'use strict';

peAdminApp.controller('dressDetailsCtrl', ['$scope', '$rootScope',
	'$stateParams', 'peProductByIdFcty', 'peUserByIdFcty', 'CURRENT_HOST',
	'CURRENT_VERSION',
	function ($scope, $rootScope, $stateParams, peProductByIdFcty, peUserByIdFcty,
		CURRENT_HOST, CURRENT_VERSION) {

		$scope.setFlag = function (flag) {
			$scope[flag] = true;
			if (flag === 'showTab4') {
				$rootScope.$broadcast('activeTabName', {
					tabName: 'showTab4'
				});
			}
		};

		$scope.isDev = getHost(CURRENT_HOST);
		$scope.size = [];
		$scope.bustFit = [];
		$scope.heightFit = [];

		peProductByIdFcty.get({
			id: $stateParams.productId,
			timestamp: $scope.isDev ? new Date().getTime() : null,
			version: !$scope.isDev ? CURRENT_VERSION : null
		}, function (response) {
			$scope.dressTitle = response.data.title;
			$scope.testValue = 'Test value from mail ctrl.';
			$scope.listingData = response.data;

			peUserByIdFcty.get(
				{
					id: $scope.listingData.ownerId,
					timestamp: $scope.isDev ? new Date().getTime() : null,
					version: !$scope.isDev ? CURRENT_VERSION : null
				}, function (response) {
					$scope.isReadyToActive = true;

					// Listing Details tab

					$scope.userName = response.data.username;
					$scope.fullName = response.data.name;
					if($scope.listingData.size.length){
						$scope.listingData.size.forEach(function(size){
							$scope.size.push(correctString(size))
						});
					}
					
					// Dress Fit

					$scope.dressLength = $scope.listingData.dressLength ? $scope.listingData.dressLength + '\"'
						: "N/A";

					$scope.bustFit =  $scope.listingData.bustFit;
					$scope.heightFit = $scope.listingData.heightFit;

					$scope.dressData = $scope.listingData;
					if ($scope.dressData.status == 1
						|| $scope.dressData.status == 0) {
						$scope.isReadyToActive = false;
					}

					// Rental Options tab

					$scope.listAs = $scope.listingData.listAs ? capitalizeText($scope.listingData.listAs)
						: '';

					if ($scope.listingData.status == 1
						|| $scope.listingData.status == 0) {
						$scope.isReadyToActive = false;
					}

					$scope.fourDayRentalPrice = $scope.listingData.rentalPrice ? $scope.listingData.rentalPrice
						: '';
					$scope.originalPrice = $scope.listingData.originalPrice ? $scope.listingData.originalPrice
						: '';
					$scope.rentalPeriod = $scope.listingData.rentalPeriod ? $scope.listingData.rentalPeriod
						+ " days"
						: '';

					if ($scope.listingData.availForOtherPeriod) {
						$scope.rentalPeriod = $scope.rentalPeriod
							+ ' / 8 days';
						$scope.eightDayRentalPrice = $scope.fourDayRentalPrice * 1.15;
					}
				});
		});
	}]);