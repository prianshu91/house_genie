'use strict';

peAdminApp
	.controller(
	'rentalOrderCancelledEmailCtrl',
	[
		'$scope',
		'$stateParams',
		'$uibModalInstance',
		'adminService',
		'$state',
		'EMAIL_FROM_ADDRESS',
		'peUserByIdFcty',
		'data',
		'origin',
		function ($scope, $stateParams, $uibModalInstance, adminService,
			$state, EMAIL_FROM_ADDRESS, peUserByIdFcty, data, origin) {

			var getEmail = function (id) {
				peUserByIdFcty.get({
					id: id
				}, function (response) {
					return response.data.email;
				});
			};

			$scope.otherChecked = false;
			$scope.checkBoxChecked = false;

			if (origin === 'dressRental') {
				$scope.deleteListData = data;
				$scope.orderId = data.OrderItem.orderId.slice(-7);
				$scope.renterUsername = data.OrderItem.userName;
				$scope.ownerId = data.OrderItem.product.ownerId;
				$scope.shipmentId = data.OrderItem.shipmentId;
				$scope.renterEmailId = data.renter.email;
				$scope.lenderEmailId = data.lender.email;
			} else {
				$scope.orderId = data.id.slice(-7);
				$scope.renterUsername = data.shipments[0].userName;
				$scope.ownerId = data.shipments[0].ownerId;
				$scope.shipmentId = data.shipments[0].id;
				$scope.renterEmailId = data.shipments[0].user.email;
				$scope.lenderEmailId = data.shipments[0].owner.email;
			}
			$scope.reasonSelection = [];

			$scope.toggleReasonSelection = function (status) {
				var idx = $scope.reasonSelection.indexOf(status);

				if (status === 'Other' && idx == -1) {
					$scope.otherChecked = true;
				} else if (status === 'Other' && idx == 0) {
					$scope.otherChecked = false;
				}
				// is currently selected
				if (idx > -1) {
					$scope.reasonSelection.splice(idx, 1);
				}

				// is newly selected
				else {
					$scope.reasonSelection.push(status);
				}

				if ($scope.reasonSelection.length) {
					$scope.checkBoxChecked = true;
				} else {
					$scope.checkBoxChecked = false;
				}
			};

			$scope.reasonArray = ['Item does not fit',
				'Item is not in acceptable condition', 'Other'];

			$scope.sendCancelNotify = function () {
				$scope.deleteReason = '';
				$scope.reasonSelection.sort();
				$scope.reasonSelection.forEach(function (reason) {
					if (reason === 'Other') {
						$scope.deleteReason = $scope.deleteReason + '- ' + reason
							+ ': ' + $scope.blockedReason + '<br/>'
					} else {
						$scope.deleteReason = $scope.deleteReason + '- ' + reason
							+ '<br/>'
					}
				});

				var query = {
					to: EMAIL_FROM_ADDRESS,
					from: EMAIL_FROM_ADDRESS,
					bcc: $scope.lenderEmailId + "," + $scope.renterEmailId,
					subject: 'Order # ' + $scope.orderId
					+ ' has been canceled by renter ' + $scope.renterUsername,
					body: '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;"><div style="height: 5px;background-color:#ff8b7f;"></div><div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div><div style="clear:both"></div><div style="padding:15px;border: 2px solid #ddd;margin-top: 10px;"> <p style="margin-top: 0;"> Dress rental <b>order # '
					+ $scope.orderId
					+ '</b> has been canceled by '
					+ $scope.renterUsername
					+ ' for the following reason: </p><p style="line-height: 24px; white-space: pre-wrap;"> <b>Reason(s): </b><br/>'
					+ $scope.deleteReason
					+ '</p><p> PromElle will work with the renter and arrange to have your dress returned. We apologize for any inconvenience. Please write to us at <a href="mailto:contact@promelle.com">contact@promelle.com</a> if you have any questions or concerns.</p>'
					+ '<p style="color:#000;line-height: 24px;font-size:15px;margin-bottom: 0px;"> Thanks, <br/>Team PromElle </p></div></div>'
				};
				adminService.emailContact(query).then(function (res) {
					if (res) {
						$uibModalInstance.close($scope.shipmentId);
					}
				});
			};

			$scope.close = function () {
				$uibModalInstance.close();
			};
		}]);