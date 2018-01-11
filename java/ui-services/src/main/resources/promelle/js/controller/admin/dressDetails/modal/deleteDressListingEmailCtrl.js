'use strict';

peAdminApp
	.controller(
	'deleteDressListingEmailCtrl',
	[
		'$scope',
		'productService',
		'$stateParams',
		'$uibModalInstance',
		'data',
		'productId',
		'ownerId',
		'adminService',
		'$state',
		'EMAIL_FROM_ADDRESS',
		'peUserByIdFcty',
		'peDressDeleteFcty',
		'toaster',
		function ($scope, productService, $stateParams, $uibModalInstance,
			data, productId, ownerId, adminService, $state,
			EMAIL_FROM_ADDRESS, peUserByIdFcty, peDressDeleteFcty, toaster) {

			$scope.deleteListData = data;
			$scope.disbleSave = true;

			peUserByIdFcty.get({
				id: ownerId
			}, function (response) {
				$scope.emailId = response.data.email;
				$scope.fullName = response.data.name;
			});

			$scope.reasonSelection = [];

			$scope.toggleReasonSelection = function (status) {
				var idx = $scope.reasonSelection.indexOf(status);

				// is currently selected
				if (idx > -1) {
					$scope.reasonSelection.splice(idx, 1);
					$scope.disbleSave = false;
				}

				// is newly selected
				else {
					$scope.reasonSelection.push(status);
					$scope.disbleSave = true;
				}
			};

			$scope.reasonArray = [
				'The listing has been inactive for too long',
				'The listing is inappropriate', 'Images are not clear',
				'Description is inadequate'];

			$scope.pickReason = function (reasonName) {
				$scope.deleteReason = '';
				$scope.deleteReason = reasonName;
			};

			$scope.sendDeleteNotify = function () {
				$scope.deleteReason = '';
				$scope.reasonSelection.forEach(function (reason) {
					$scope.deleteReason = $scope.deleteReason + '- ' + reason
						+ '<br/>'
				});

				var query = {
					to: EMAIL_FROM_ADDRESS,
					from: EMAIL_FROM_ADDRESS,
					bcc: $scope.emailId,
					subject: 'PromElle Listing Deleted!',
					body: '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;"> <div style="height: 5px;background-color: #ff8b7f;"></div><div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div> <div style="clear:both"></div><div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;"> <p style="margin-top: 0;"> <b> Dear '
					+ $scope.fullName
					+ ', </b> </p><p>We are sorry to inform you that your PromElle listing <b>'
					+ $scope.deleteListData.title
					+ '</b> has been deleted for the following reason(s):</p><p style="line-height: 24px; white-space: pre-wrap;"> <b>Reason(s): </b><br/>'
					+ $scope.deleteReason
					+ '<b>Explanation: </b>'
					+ $scope.explanationMessage
					+ '</p><p>If you have any questions regarding this, please write to us at <a href="mailto:contact@promelle.com">contact@promelle.com</a></p><p style="color:#000;line-height: 24px;font-size:15px;margin-bottom: 0px;"> Regards, <br/> Team PromElle </p></div></div>'
				};

				peDressDeleteFcty.deleteById({
					id: productId
				}, function (response) {
					if (response) {
						adminService.emailContact(query).then(
							function (res) {
								if (res) {
									$scope.close();
									toaster.pop({
										type: 'success',
										body: 'Deleted Listing Successfully!'
									});
									$state.go('dressLists');
								}
							},
							function (error) {
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