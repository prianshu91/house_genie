'use strict';

peAdminApp
	.controller(
	'lenderOrderCancelledEmailCtrl',
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

			console.log('origin lender : ' + origin);

			if (origin === 'dressRental') {
				// $scope.deleteListData = data;
				$scope.orderId = data.OrderItem.orderId.slice(-7);
				$scope.lenderUsername = data.OrderItem.product.ownerName;
				$scope.shipmentId = data.OrderItem.shipmentId;
				$scope.renterEmailId = data.renter.email;
				$scope.lenderEmailId = data.lender.email;
				$scope.listingTitle = data.OrderItem.product.title;
				$scope.renterName = data.renter.name;
			} else {
				$scope.orderId = data.id.slice(-7);
				$scope.lenderUsername = data.shipments[0].ownerName;
				$scope.shipmentId = data.shipments[0].id;
				$scope.renterEmailId = data.shipments[0].user.email;
				$scope.lenderEmailId = data.shipments[0].owner.email;
				$scope.listingTitle = data.shipments[0].product.title;
				$scope.renterName = data.shipments[0].user.name;
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

			$scope.reasonArray = ['Dress is not in acceptable condition',
				'Other'];

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

				var emailForLender = {
					to:  EMAIL_FROM_ADDRESS,
					from: EMAIL_FROM_ADDRESS,
					bcc: $scope.lenderEmailId,
					subject: 'Order # ' + $scope.orderId + ' for Listing ' + $scope.listingTitle + ' has been canceled',
					body: '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;">'
							+ '<div style="height: 5px;background-color: #ff8b7f;"></div>'
							+ '<div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div>'
							+ '<div style="clear:both"></div>'
							+ '<div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;">'
								+ 'This is to confirm your cancellation of rental order #  "Rental order # for listing ' + $scope.listingTitle + ' for the following reason:<br/>'
								+ $scope.deleteReason + '<br/>'
								+ '<p style="color:#000;line-height: 24px;font-size:15px;margin-bottom: 0px;"> Thanks, <br/>Team PromElle'
							+ '</div>'
						+ '</div>'
				};
				var emailForRenter = {
						to : EMAIL_FROM_ADDRESS,
						from : EMAIL_FROM_ADDRESS,
						bcc : $scope.renterEmailId,
						subject: 'Order # ' + $scope.orderId + ' for Listing ' + $scope.listingTitle + ' has been canceled by Lender',
						body: '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;">'
								+ '<div style="height: 5px;background-color: #ff8b7f;"></div>'
								+ '<div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div>'
								+ '<div style="clear:both"></div>'
								+ '<div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;">'
									+ '<p style="line-height: 30px;">Dear <b>' + $scope.renterName + '</b>!</p>'
									+ 'We are sorry to inform you that your rental order for ' + $scope.listingTitle + ' has been canceled by the Lender for the following reason(s):'
									+ $scope.deleteReason + '<br/>'
									+ '<p>Your payment for this order # ***** will be refunded to you within the next 5 business days.</p>'
									+ 'Please <a href="mailto:contact@promelle.com">Contact Us</a> if you have any questions.'
									+ '<p style="color:#000;line-height: 24px;font-size:15px;margin-bottom: 0px;"> Thanks, <br/>Team PromElle'
								+ '</div>'
							+ '</div>'
				}
				adminService.emailContact(emailForLender).then(function (res) {
					if (res) {
						$uibModalInstance.close($scope.shipmentId);
					}
				});
				adminService.emailContact(emailForRenter).then(function (res) {
					//do nothing with the response
				});
			};

			$scope.close = function () {
				$uibModalInstance.close();
			};
		}]);