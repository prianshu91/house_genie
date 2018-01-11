'use strict';

peAdminApp
	.controller(
	'lenderPaidCtrl',
	[
		'$scope',
		'$stateParams',
		'$uibModalInstance',
		'data',
		'Lightbox',
		'peUserByIdFcty',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, $stateParams, $uibModalInstance, data, Lightbox,
			peUserByIdFcty, CURRENT_HOST, CURRENT_VERSION) {

			var getPaymentData = function () {

				$scope.isDev = getHost(CURRENT_HOST);

				// $scope.checkImageUrl = data.chequeImage;
				$scope.isPageLoading = true;

				peUserByIdFcty
					.get(
					{
						id: data.ownerId,
						timestamp: $scope.isDev ? new Date().getTime() : null,
						version: !$scope.isDev ? CURRENT_VERSION : null
					},
					function (response) {
						$scope.lenderFullName = response.data.name;
						$scope.lenderMobile = response.data.mobile ? response.data.mobile
							: null;
						$scope.isPageLoading = false;
					});

				$scope.checkNumber = data.chequeNo;
				$scope.amount = data.amount ? data.amount : 'N/A';
				$scope.nameOnCheck = data.chequeRecipient ? data.chequeRecipient
					: 'N/A';
				$scope.lendingOrderId = data.orderId.slice(-7);

				var address1 = data.chequeAddress.addressLine1 ? data.chequeAddress.addressLine1
					+ ','
					: '';
				var address2 = data.chequeAddress.addressLine2 ? data.chequeAddress.addressLine2
					+ ','
					: '';
				var city = data.chequeAddress.city ? data.chequeAddress.city
					+ ',' : '';
				var state = data.chequeAddress.state ? upperText(data.chequeAddress.state)
					+ ' ' : '';
				var zipcode = data.chequeAddress.zipCode ? data.chequeAddress.zipCode
					: ''

				$scope.checkAddress = address1 + address2 + city + state
					+ zipcode;
				// $scope.images = [];

				// $scope.images.push({
				// url : $scope.checkImageUrl
				// });

			};

			getPaymentData();

			$scope.close = function () {
				$uibModalInstance.close();
			};

			// $scope.openLightboxModal = function() {
			// Lightbox.openModal($scope.images, 0);
			// };

		}]);