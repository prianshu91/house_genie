'use strict';

peAdminApp
	.controller(
	'dressRentalDetailsHomeCtrl',
	[
		'$scope',
		'$state',
		'$stateParams',
		'peRentedProductFcty',
		'$uibModal',
		'peUpdateShipmentStatusFcty',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, $state, $stateParams, peRentedProductFcty,
			$uibModal, peUpdateShipmentStatusFcty, CURRENT_HOST,
			CURRENT_VERSION) {

			$scope.rentalListDropdown = [{
				id: '0',
				value: 'Order Placed'
			}, {
				id: '1',
				value: 'Order Delivered to Renter'
			}, {
				id: '2',
				value: 'Order Canceled'
			}, {
				id: '3',
				value: 'Order Completed'
			},];

			$scope.selectedRentalOption = '0';

			$scope.currentStatus = 'SHIPMENT_PENDING';

			$scope.changeRentalSelection = function (status) {
				if (status === "1") {
					$scope.currentStatus = 'SHIPMENT_DELIVERED';
					$scope.getRentedDresses($scope.currentStatus);
				} else if (status === "2") {
					$scope.currentStatus = 'SHIPMENT_CANCELLED';
					$scope.getRentedDresses($scope.currentStatus);
				} else if (status === "3") {
					$scope.currentStatus = 'SHIPMENT_COMPLETED';
					$scope.getRentedDresses($scope.currentStatus);
				} else {
					$scope.currentStatus = 'SHIPMENT_PENDING';
					$scope.getRentedDresses($scope.currentStatus);
				}
			};

			$scope.columns = [
				{
					field: 'OrderItem.createdOn',
					displayName: 'Rented On',
					cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"',
					enableColumnMenu: false,
					enableSorting: true,
					sort: {
						direction: 'desc',
						priority: 0
					},
					width: "18%"
				},
				{
					field: 'OrderItem.product.title',
					displayName: 'Dress Title',
					enableColumnMenu: false,
					enableSorting: false,
					cellTemplate: '<div class="pT5">'
					+ '<a href="" ng-click="grid.appScope.openDressDetails(row.entity.OrderItem.product.ownerId, row.entity.OrderItem.product.id)">{{row.entity.OrderItem.product.title}}</a>'
					+ '</div>'
				},
				{
					field: 'lender.userName',
					displayName: 'Lender\'s\nUsername',
					enableColumnMenu: false,
					enableSorting: false,
					cellTemplate: '<div class="pT5">'
					+ '<a href="" ng-click="grid.appScope.openUserDetails(row.entity.OrderItem.product.ownerId)">{{row.entity.OrderItem.product.ownerName}}</a>'
					+ '</div>'
				},
				{
					field: 'lender.homeAddress.zipCode',
					displayName: 'Lender\'s\nZipcode',
					enableSorting: false,
					enableColumnMenu: false
				},
				{
					field: 'renter.userName',
					displayName: 'Renter\'s\nUsername',
					enableColumnMenu: false,
					enableSorting: false,
					cellTemplate: '<div class="pT5">'
					+ '<a href="" ng-click="grid.appScope.openUserDetails(row.entity.OrderItem.userId)">{{row.entity.OrderItem.userName}}</a>'
					+ '</div>'
				},
				{
					field: 'renter.homeAddress.zipCode',
					displayName: 'Renter\'s\nZipcode',
					enableSorting: false,
					enableColumnMenu: false
				},
				{
					field: 'orderString',
					displayName: 'Order #',
					enableColumnMenu: false,
					enableSorting: false,
					cellTemplate: '<div class="pT5">'
					+ '<a href="" ng-click="grid.appScope.openOrderDetails(row.entity.OrderItem.orderId)">{{row.entity.orderString}}</a>'
					+ '</div>'
				}];

			var paginationOptions = {
				pageNumber: 1,
				pageSize: 30,
				sort: null
			};

			$scope.rentalOptions = {
				enableColumnResize: true,
				enableHorizontalScrollbar: 0,
				enableVerticalScrollbar: 1,
				enableSorting: true,
				enableFiltering: false,
				paginationPageSizes: [30, 60],
				paginationPageSize: 30,
				useExternalPagination: true,
				columnDefs: $scope.columns,
				onRegisterApi: function (gridApi) {
					$scope.gridApi = gridApi;
					gridApi.pagination.on.paginationChanged($scope, function (
						newPage, pageSize) {
						paginationOptions.pageNumber = newPage;
						paginationOptions.pageSize = pageSize;
						$scope.getRentedDresses($scope.currentStatus);
					});
				}
			};

			uiGridHeight();

			$scope.getRentedDresses = function (status) {

				$scope.isDev = getHost(CURRENT_HOST);

				$scope.noData = false;
				peRentedProductFcty
					.get(
					{
						limit: paginationOptions.pageSize,
						offset: (paginationOptions.pageNumber - 1)
						* paginationOptions.pageSize,
						sort: 'modifiedOn desc',
						shipmentStatus: status,
						timestamp: $scope.isDev ? new Date().getTime()
							: null,
						version: !$scope.isDev ? CURRENT_VERSION : null
					},
					function (response) {
						if (response.paging) {
							$scope.rentalOptions.totalItems = response.paging.total;
							$scope.rentalOptions.paginationPageSizes.indexOf($scope.rentalOptions.totalItems) === -1 ? $scope.rentalOptions.paginationPageSizes.push($scope.rentalOptions.totalItems) : '';
						} else {
							$scope.rentalOptions.totalItems = 0;
						}
						if ($scope.rentalOptions.totalItems < 1) {
							$scope.noData = true;
						}
						$scope.rentalOptions.data = [];
						if (response.data) {
							if (status === 'SHIPMENT_PENDING') {
								if ($scope.columns.length < 8) {
									$scope.columns
										.push({
											field: 'action',
											displayName: 'Action',
											enableColumnMenu: false,
											enableSorting: false,
											cellTemplate: '<div class="pT5"><button class="btn btn-danger btn-xs" type="submit" ng-click="grid.appScope.cancelRental(row.entity)">'
											+ 'Cancel' + '</button><div>'
										});
								}
								angular.forEach(response.data, function (
									rentalData) {
									var orderString = rentalData.OrderItem.orderId
										.slice(-7);
									rentalData.orderString = orderString;
									$scope.rentalOptions.data.push(rentalData);
								});
							} else {
								if ($scope.columns.length > 7) {
									$scope.columns.splice(
										$scope.columns.length - 1, 1);
								}
								angular.forEach(response.data, function (
									rentalData) {
									var orderString = rentalData.OrderItem.orderId
										.slice(-7);
									rentalData.orderString = orderString;
									$scope.rentalOptions.data.push(rentalData);
								});
							}
						}
					});
			};

			$scope.getRentedDresses('SHIPMENT_PENDING');

			$scope.openDressDetails = function (ownerId, productId) {
				$state.go('dressDetail', {
					ownerId: ownerId,
					productId: productId
				});
			};

			$scope.openUserDetails = function (userId) {
				$state.go('userDetail', {
					userId: userId
				});
			};

			$scope.openOrderDetails = function (orderId) {
				$state.go('rentalOrderDetails', {
					orderId: orderId
				});
			}

			$scope.cancelRental = function (dress) {
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
												return 'dressRental'
											}
										}
									});
								modalInstance.result.then(function (shipmentId) {
									if (shipmentId) {
										peUpdateShipmentStatusFcty.update({
											shipmentId: shipmentId,
											shipmentStatus: 'SHIPMENT_CANCELLED'
										}, function (response) {
											$scope.getRentedDresses($scope.currentStatus);
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
												return 'dressRental'
											}
										}
									});
								modalInstance.result.then(function (shipmentId) {
									if (shipmentId) {
										peUpdateShipmentStatusFcty.update({
											shipmentId: shipmentId,
											shipmentStatus: 'SHIPMENT_CANCELLED'
										}, function (response) {
											$scope.getRentedDresses($scope.currentStatus);
										});
									}
								});
							}
						}
					});
			}

		}]);