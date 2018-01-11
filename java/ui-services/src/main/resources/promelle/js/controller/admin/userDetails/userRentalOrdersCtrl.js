'use strict';

peAdminApp
	.controller(
	'userRentalOrdersCtrl',
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

			$scope.userRentalOrders = {
				enableColumnResize: true,
				enableHorizontalScrollbar: 0,
				enableVerticalScrollbar: 0,
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
						field: 'shipments[0].items[0].startDate',
						displayName: 'Rental Start Date',
						enableColumnMenu: false,
						width: '20%',
						cellFilter: 'date:"\MMM dd, yyyy\"'
					},
					{
						field: 'shipments[0].items[0].endDate',
						displayName: 'Rental End Date',
						enableColumnMenu: false,
						width: '20%',
						cellFilter: 'date:"\MMM dd, yyyy\"'
					},
					{
						field: 'rentalOrderDetails',
						displayName: 'Rental Order #',
						enableColumnMenu: false,
						cellTemplate: '<div class="pT5">'
						+ '<a href="" ng-click="grid.appScope.openRentalOrderDetails(row.entity)">{{row.entity.id}}</a>'
						+ '</div>'
					}]
			};

			var rentalOrderData = function () {

				$scope.isDev = getHost(CURRENT_HOST);

				var query = {
					userId: $stateParams.userId ? $stateParams.userId : null,
					includeShipments: true,
					timestamp: $scope.isDev ? new Date().getTime() : null,
					version: !$scope.isDev ? CURRENT_VERSION : null
				};

				adminService
					.getRentalOrdersList(query)
					.then(
					function (response) {
						angular
							.forEach(
							response.data,
							function (rentalOrderItem) {
								rentalOrderItem.rentalDuration = (((
									rentalOrderItem.shipments[0].items[0].endDate
									- rentalOrderItem.shipments[0].items[0].startDate) / (24 * 60 * 60 * 1000)) + 1).toFixed(0);
								$scope.userRentalOrders.data
									.push(rentalOrderItem);
							});
					});
			};

			rentalOrderData();

			$scope.openRentalOrderDetails = function (rentalOrder) {
				$state.go('rentalOrderDetails', {
					orderId: rentalOrder.id
				});
			};
		}]);