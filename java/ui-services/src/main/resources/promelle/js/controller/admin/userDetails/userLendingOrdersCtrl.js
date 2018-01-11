'use strict';

peAdminApp
	.controller(
	'userLendingOrdersCtrl',
	[
		'$scope',
		'$state',
		'adminService',
		'$stateParams',
		'$uibModal',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, $state, adminService, $stateParams, $uibModal,
			CURRENT_HOST, CURRENT_VERSION) {

			$scope.userLendingOrders = {
				enableColumnResize: true,
				enableHorizontalScrollbar: 0,
				enableVerticalScrollbar: 1,
				enableSorting: true,
				enableFiltering: false,
				paginationPageSizes: [10, 20],
				paginationPageSize: 10,
				columnDefs: [
					{
						field: 'createdOn',
						displayName: 'Order Date',
						enableColumnMenu: false,
						width: '20%',
						cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"'
					},
					{
						field: 'rentalDuration',
						displayName: 'Rental Length',
						enableColumnMenu: false,
						width: '12%',
						cellTemplate: '<div class="pT5">'
						+ '{{row.entity.rentalDuration}} days' + '</div>'
					},
					{
						field: 'items[0].startDate',
						displayName: 'Rental Start Date',
						enableColumnMenu: false,
						width: '20%',
						cellFilter: 'date:"\MMM dd, yyyy\"'
					},
					{
						field: 'items[0].endDate',
						displayName: 'Rental End Date',
						enableColumnMenu: false,
						width: '20%',
						cellFilter: 'date:"\MMM dd, yyyy\"'
					},
					{
						field: 'orderId',
						displayName: 'Lending Order #',
						enableColumnMenu: false,
						cellTemplate: '<div class="pT5">'
						+ '<a href="" ng-click="grid.appScope.openLendingOrderDetails(row.entity)">{{row.entity.orderId}}</a>'
						+ '</div>'
					}]
			};

			var lendingOrderData = function () {

				$scope.isDev = getHost(CURRENT_HOST);

				var query = {
					ownerId: $stateParams.userId ? $stateParams.userId : null,
					includeShipments: true,
					timestamp: $scope.isDev ? new Date().getTime() : null,
					version: !$scope.isDev ? CURRENT_VERSION : null
				};

				adminService.getLendingOrdersList(query).then(
					function (response) {
						angular.forEach(response.data,
							function (lendingOrderItem) {
								lendingOrderItem.rentalDuration = (((
									lendingOrderItem.items[0].endDate
									- lendingOrderItem.items[0].startDate) / (24 * 60 * 60 * 1000)) + 1).toFixed(0);
								$scope.userLendingOrders.data
									.push(lendingOrderItem);
							});
					});
			};

			lendingOrderData();

			$scope.openLendingOrderDetails = function (lendingOrder) {
				$state.go('lendingOrderDetails', {
					orderId: lendingOrder.orderId
				});
			}
		}]);