'use strict';

peAdminApp
	.controller(
	'lendingOrderDetailsCtrl',
	[
		'$scope',
		'adminService',
		'$stateParams',
		'$uibModal',
		'peUpdateOrderStatusFcty',
		'peUpdateShipmentStatusFcty',
		'EMAIL_FROM_ADDRESS',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, adminService, $stateParams, $uibModal,
			peUpdateOrderStatusFcty, peUpdateShipmentStatusFcty,
			EMAIL_FROM_ADDRESS, CURRENT_HOST, CURRENT_VERSION) {

			$scope.cancelOrder = function (dress) {
				var modalInstance = $uibModal
					.open({
						templateUrl: 'views/admin/dressRentalDetails/modal/rentalOrderCancel.html',
						controller: 'rentalOrderCancelCtrl',
						size: 'md',
						$scope: $scope,
						resolve: {
							data: function () {
								return dress;
							}
						}
					});
				modalInstance.result
					.then(function (success) {
						if (success) {
							if (success === 'renter') {
								var modalInstance = $uibModal
									.open({
										templateUrl: 'views/admin/dressRentalDetails/modal/renterOrderCancelledEmail.html',
										controller: 'rentalOrderCancelledEmailCtrl',
										size: 'lg',
										$scope: $scope,
										resolve: {
											data: function () {
												return dress;
											},
											origin: function () {
												return 'userManagement';
											}
										}
									});
								modalInstance.result.then(function (shipmentId) {
									if (shipmentId) {
										peUpdateShipmentStatusFcty.update({
											shipmentId: shipmentId,
											shipmentStatus: 'SHIPMENT_CANCELLED'
										}, function (response) {
											$scope.lendingData();
										});
									}
								});
							} else if (success === 'lender') {
								var modalInstance = $uibModal
									.open({
										templateUrl: 'views/admin/dressRentalDetails/modal/lenderOrderCancelledEmail.html',
										controller: 'lenderOrderCancelledEmailCtrl',
										size: 'lg',
										$scope: $scope,
										resolve: {
											data: function () {
												return dress;
											},
											origin: function () {
												return 'userManagement';
											}
										}
									});
								modalInstance.result.then(function (shipmentId) {
									if (shipmentId) {
										peUpdateShipmentStatusFcty.update({
											shipmentId: shipmentId,
											shipmentStatus: 'SHIPMENT_CANCELLED'
										}, function (response) {
											$scope.lendingData();
										});
									}
								});
							}
						}
					});
			};


			$scope.lendingData = function () {

				$scope.isDev = getHost(CURRENT_HOST);
				var timestamp = $scope.isDev ? new Date().getTime() : null;
				var version = !$scope.isDev ? CURRENT_VERSION : null;
				$scope.isPageLoading = true;
				$scope.disableSave = true;
				$scope.orderToggled = false;
				$scope.deliveryToggled = false;

				adminService
					.getLendingOrdersData($stateParams.orderId, true, true,
					timestamp, version)
					.then(
					function (response) {
						$scope.isPageLoading = false;
						$scope.emailData = response.data;
						// Column 1

						$scope.dressTitle = response.data.shipments[0].items[0].product.title;
						$scope.dressImage = getThumbnailImg(response.data.shipments[0].items[0].product.images[1]);

						// Column 2

						$scope.size = [];
						if(response.data.shipments[0].items[0].product.size){
							response.data.shipments[0].items[0].product.size.forEach(function(size){
								$scope.size.push(replaceString(size, ",", ", "))
							});
						}
						$scope.dressLocation = response.data.shipments[0].items[0].product.dressLocation;
						$scope.ownerSchoolId = response.data.shipments[0].owner.schoolId;
						$scope.userSchoolId = response.data.shipments[0].user.schoolId;
						$scope.brand = response.data.shipments[0].items[0].product.brand ? response.data.shipments[0].items[0].product.brand : 'N/A';
						$scope.lenderUserName = response.data.shipments[0].ownerName;
						$scope.renterUserName = response.data.userName;
						$scope.orderId = response.data.id.slice(-7);
						$scope.orderDate = response.data.createdOn;
						$scope.rentalStartDate = response.data.shipments[0].items[0].startDate;
						$scope.rentalEndDate = response.data.shipments[0].items[0].endDate;

						$scope.rentalPeriod = response.data.shipments[0].items[0].product.availForOtherPeriod ? (((
							$scope.rentalEndDate - $scope.rentalStartDate) / (24 * 60 * 60 * 1000)) + 1).toFixed(0)
							+ ' days'
							: response.data.shipments[0].items[0].product.rentalPeriod
							+ ' days';

						// Column 3

						$scope.shipmentInfo = correctString(
							response.data.shipments[0].shipmentStatus)
							.toUpperCase();

						$scope.shipmentId = response.data.shipments[0].id;

						$scope.orderStatus = ['ORDER PLACED',
							'ORDER COMPLETED', 'ORDER CANCELED']; // to be
						// added : cancelled by renter/lender

						$scope.deliveryStatus = [
							'DELIVERY TO RENTER PENDING',
							'DELIVERED TO RENTER', 'DELIVERED TO LENDER']; // to
						// be added : return delivery to lender pending

						var x = [], y = [], p = [], q = [];
						switch ($scope.shipmentInfo) {
							case 'SHIPMENT PENDING':
								x = ['ORDER PLACED'];
								y = ['ORDER PLACED', 'ORDER COMPLETED'];
								p = ['DELIVERY TO RENTER PENDING'];
								q = ['DELIVERY TO RENTER PENDING',
									'DELIVERED TO LENDER'];
								break;
							case 'SHIPMENT DELIVERED':
								x = ['ORDER PLACED'];
								y = ['ORDER PLACED'];
								p = ['DELIVERY TO RENTER PENDING',
									'DELIVERED TO RENTER'];
								q = ['DELIVERY TO RENTER PENDING',
									'DELIVERED TO RENTER', 'DELIVERED TO LENDER'];
								break;
							case 'SHIPMENT COMPLETED':
								x = ['ORDER PLACED', 'ORDER COMPLETED'];
								y = ['ORDER PLACED', 'ORDER COMPLETED',
									'ORDER CANCELED'];
								p = ['DELIVERY TO RENTER PENDING',
									'DELIVERED TO RENTER', 'DELIVERED TO LENDER'];
								q = ['DELIVERY TO RENTER PENDING',
									'DELIVERED TO RENTER', 'DELIVERED TO LENDER'];
								break;
							case 'SHIPMENT CANCELLED':
								x = ['ORDER PLACED', 'ORDER CANCELED'];
								y = ['ORDER PLACED', 'ORDER CANCELED',
									'ORDER COMPLETED'];
								p = ['DELIVERY TO RENTER PENDING',
									'DELIVERED TO RENTER'];
								q = ['DELIVERY TO RENTER PENDING',
									'DELIVERED TO RENTER', 'DELIVERED TO LENDER'];
								break;
						}

						$scope.orderCheckboxDisable = false;
						$scope.deliveryCheckboxDisable = false;

						$scope.orderSelection = x;
						$scope.orderDisable = y;
						$scope.deliverySelection = p;
						$scope.deliveryDisable = q;

						$scope.toggleOrderSelection = function toggleOrderSelection(
							status) {
							$scope.currentStatus = '';
							$scope.currentStatus = status;

							var idx = $scope.orderSelection.indexOf(status);

							// is currently selected
							if (idx > -1) {
								$scope.orderSelection.splice(idx, 1);
								$scope.orderDisable.splice(idx, 1);
								$scope.deliveryCheckboxDisable = false;
								$scope.disableSave = true;
								$scope.orderToggled = false;
							}

							// is newly selected
							else {
								$scope.orderSelection.push(status);
								$scope.deliveryCheckboxDisable = true;
								if (status == 'ORDER COMPLETED') {
									$scope.orderDisable.push('ORDER CANCELED');
								} else if (status == 'ORDER CANCELED') {
									$scope.orderDisable.push('ORDER COMPLETED');
								}
								$scope.disableSave = false;
								$scope.orderToggled = true;
							}
						};

						$scope.toggleDeliverySelection = function toggleDeliverySelection(
							status) {
							$scope.currentStatus = '';
							$scope.currentStatus = status;

							var idx = $scope.deliverySelection.indexOf(status);

							// is currently selected
							if (idx > -1) {
								$scope.deliverySelection.splice(idx, 1);
								$scope.orderCheckboxDisable = false;
								$scope.disableSave = true;
								$scope.deliveryToggled = false;
							}

							// is newly selected
							else {
								$scope.deliverySelection.push(status);
								$scope.orderCheckboxDisable = true;
								$scope.disableSave = false;
								$scope.deliveryToggled = true;
							}
						};

						$scope.deliveryOption = response.data.inPersonDelivery ? 'In-person delivery at my school'
							: 'PromElle Delivery';

						var addressLine1 = response.data.shippingAddress.addressLine1 ? response.data.shippingAddress.addressLine1
							+ ', '
							: '';
						var addressLine2 = response.data.shippingAddress.addressLine2 ? response.data.shippingAddress.addressLine2
							+ ', '
							: '';
						var city = response.data.shippingAddress.city ? response.data.shippingAddress.city
							+ ', '
							: '';
						var state = response.data.shippingAddress.state ? response.data.shippingAddress.state
							+ ' '
							: '';
						var zipcode = response.data.shippingAddress.zipCode ? response.data.shippingAddress.zipCode
							: '';

						$scope.schoolName = response.data.shippingAddress.name ? response.data.shippingAddress.name : null;
						$scope.deliveryAddress = addressLine1 + addressLine2
							+ city + state + zipcode;
						// Column 4

						$scope.rentalPrice = response.data.shipments[0].earning ? response.data.shipments[0].earning.rentalPrice
							: response.data.shipments[0].items[0].price;
						$scope.earnings = response.data.shipments[0].earning ? response.data.shipments[0].earning.amount
							: $scope.rentalPrice * 0.75;
						$scope.shippingCharges = response.data.shipments[0].earning ? response.data.shipments[0].earning.deliveryCharge
							: response.data.inPersonDelivery ? 0 : 5;
						if ($scope.shipmentInfo == 'SHIPMENT CANCELLED') {
							$scope.redeemableEarnings = 0;
						} else {
							$scope.redeemableEarnings = response.data.shipments[0].earning ? response.data.shipments[0].earning.redeemableAmount
								: null;
						}
						$scope.paymentStatus = response.data.shipments[0].earning ? response.data.shipments[0].earning.redeemStatus
							: null;
					});
			}

			$scope.lendingData();

			$scope.save = function () {

				if ($scope.orderToggled) {
					var orderStatus = $scope.orderSelection[$scope.orderSelection.length - 1];
					var testOrder = orderStatus;
					if (testOrder == 'ORDER COMPLETED') {
						// shipment completed
						peUpdateShipmentStatusFcty.update({
							shipmentId: $scope.shipmentId,
							shipmentStatus: 'SHIPMENT_COMPLETED'
						}, function (response) {
							$scope.lendingData();
						});
					} else if (testOrder == 'ORDER CANCELED') {
						// shipment cancelled

						$scope.cancelOrder($scope.emailData);

						// peUpdateShipmentStatusFcty.update({
						// 	shipmentId: $scope.shipmentId,
						// 	shipmentStatus: 'SHIPMENT_CANCELLED'
						// }, function (response) {
						// 	$scope.$scope.lendingData();
						// });
					}
				}

				if ($scope.deliveryToggled) {
					var deliveryStatus = $scope.deliverySelection[$scope.deliverySelection.length - 1];
					var testDelivery = deliveryStatus;
					if (testDelivery == 'DELIVERED TO RENTER') {
						// shipment delivered
						peUpdateShipmentStatusFcty.update({
							shipmentId: $scope.shipmentId,
							shipmentStatus: 'SHIPMENT_DELIVERED'
						}, function (response) {
							$scope.lendingData();
						});
					} else if (testDelivery == 'DELIVERED TO LENDER') {
						// shipment completed OR cancelled
					}
				}

				// done from backend : SHIPMENT COMPLETED e-mail

			};

			$scope.close = function () {
				$uibModalInstance.close();
			};

		}]);