
'use strict';

peAdminApp
	.controller(
	'lenderPaymentCtrl',
	[
		'$scope',
		'$uibModalInstance',
		'data',
		'peUpdateLenderPaymentFcty',
		'paymentService',
		'peUserByIdFcty',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, $uibModalInstance, data,
			peUpdateLenderPaymentFcty, paymentService, peUserByIdFcty, CURRENT_HOST, CURRENT_VERSION) {

			var getPaymentData = function () {

				$scope.isOnlineChecked = {
					valueV: "true"
				};

				$scope.dateOptions = {};

				$scope.onlineCheck = {};
				$scope.handWrittenCheck = {};

				$scope.isPageLoading = true;
				$scope.earningId = data.id;
				$scope.disabledSave = true;

				$scope.isDev = getHost(CURRENT_HOST);

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

				$scope.amount = data.redeemableAmount ? data.redeemableAmount
					: 'N/A';
				$scope.lendingOrderId = data.orderId.slice(-7);
				$scope.nameOnCheck = data.chequeRecipient;

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
			};

			getPaymentData();

			$scope.close = function () {
				$uibModalInstance.close();
			};

			// $scope.uploadPic = function() {
			// var file = $scope.picFile;
			// paymentService.uploadFileToUrl(file).then(function(response) {
			// $scope.disabledSave = false;
			// $scope.uploadedCheckUrl = response;
			// });
			// }

			$scope.clearFields = function (isOnline) {
				if (isOnline == "true") {
					$scope.handWrittenCheck.checkDate = null;
					$scope.handWrittenCheck.checkNumber = null;
				} else {
					$scope.onlineCheck.checkDate = null;
					$scope.onlineCheck.checkNumber = null;
				}
			}

			$scope.save = function () {
				$scope.submitted = true;
				if (($scope.onlineCheck.checkDate
									&& $scope.onlineCheck.checkNumber)) {
					console.log('online check', $scope.notes)
					var postData = {
						'chequeNo': $scope.onlineCheck.checkNumber,
						'chequeDate': $scope.onlineCheck.checkDate.getTime()
					}
					if($scope.notes){
						postData['notes']= $scope.notes;
					}

					peUpdateLenderPaymentFcty.update({
						id: $scope.earningId,
						status: 'REDEEMED'
					}, postData, function (response) {
						$uibModalInstance.close(response.data);
					});
				} else if (($scope.handWrittenCheck.checkDate
									&& $scope.handWrittenCheck.checkNumber)) {
					console.log('hand check', $scope.notes)
					var postData = {
						'chequeNo': $scope.handWrittenCheck.checkNumber,
						'chequeDate': $scope.handWrittenCheck.checkDate.getTime()
					}
					if($scope.notes){
						postData['notes']= $scope.notes;
					}
					peUpdateLenderPaymentFcty.update({
						id: $scope.earningId,
						status: 'REDEEMED'
					}, postData, function (response) {
						$uibModalInstance.close(response.data);
					});
				}
			};

		}]);