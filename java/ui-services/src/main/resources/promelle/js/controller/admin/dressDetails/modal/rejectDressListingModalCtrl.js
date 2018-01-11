'use strict';

peAdminApp
	.controller(
	'rejectDressListingModalCtrl',
	[
		'$scope',
		'adminService',
		'$stateParams',
		'$uibModalInstance',
		'data',
		'ownerId',
		'productId',
		'setDefaultImageError',
		'peUserByIdFcty',
		'EMAIL_FROM_ADDRESS',
		'dressTitle',
		'$state',
		'productService',
		'toaster',
		'peDressRejectFcty',
		function ($scope, adminService, $stateParams, $uibModalInstance,
			data, ownerId, productId, setDefaultImageError, peUserByIdFcty,
			EMAIL_FROM_ADDRESS, dressTitle, $state, productService, toaster, peDressRejectFcty) {

			$scope.rejectListingData = [];
			$scope.rejectListingData = data;
			$scope.dressTitle = dressTitle;

			peUserByIdFcty.get({
				id: ownerId
			}, function (response) {
				$scope.emailId = response.data.email;
				$scope.userName = response.data.name;
			})

			if (setDefaultImageError) {
				$scope.additionalMessage = 'Either of "Image 1 - Front Image" or "Image 2 - Back Image" are not uploaded, Please check and reupload';
			}

			$scope.sendRejectListingEmail = function () {
				$scope.submitted = true;
				$scope.messageBody = '';
				$scope.rejectListingData.forEach(function (rejectData, index) {
					if (rejectData.text == undefined) {
						throw ("Reason of Rejection field cannot be empty.");
						return false;
					} else {
						var indexVal = index
						rejectData.serialNo = indexVal + 1;
						if (rejectData.type === 'image') {
							rejectData.value = rejectData.caption;
						}
						$scope.messageBody = $scope.messageBody + '<div>'
							+ rejectData.serialNo + ". " + rejectData.value + ": "
							+ rejectData.text + '</div><br/>';
					}
				});

				$scope.additionalMessage = $scope.additionalMessage ? $scope.additionalMessage
					: 'No Additional Comments';
				var query = {
					to: EMAIL_FROM_ADDRESS,
					from: EMAIL_FROM_ADDRESS,
					bcc: $scope.emailId,
					subject: 'PromElle Listing needs to be revised!',
					body: '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;"> <div style="height: 5px;background-color: #ff8b7f;"></div><div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div> <div style="clear:both"></div><div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;"> <p style="margin-top: 0;"> <b> Dear '
					+ $scope.userName
					+ ', </b> </p><p>Your listing <b>'
					+ $scope.dressTitle
					+ '</b> on PromElle needs to be revised for the following reason(s):</p><p style="line-height: 24px;">'
					+ $scope.messageBody
					+ '</p><p style="white-space: pre-wrap;">Additional Comments: '
					+ $scope.additionalMessage
					+ '</p><p>Please fix the above issues and re-submit for approval. You can view and edit your listing from the Rejected section of your Profile.</p><p style="color:#000;line-height: 24px;font-size:15px;margin-bottom: 0px;"> Thank you, <br/> Team PromElle </p></div></div>'
				};

				peDressRejectFcty.update({
					id: productId
				}, function (response) {
					if (response) {
						adminService.emailContact(query).then(function (res) {
							if (res) {
								productService.resetNotifyFields();
								$scope.close();
								toaster.pop({
									type: 'success',
									body: 'Rejected Listing Successfully!'
								});
								$state.go('dressLists');
							}
						}, function (error) {
							console.log("Error in Sending Email. Error Info :"
								+ error);
						});
					}
				}, function (error) {
					console.log("Error in Sending Email. Error Info :" + error);
				});
			};

			$scope.close = function () {
				$uibModalInstance.close();
			};
		}]);