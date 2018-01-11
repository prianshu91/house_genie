'use strict';

peAdminApp
	.controller(
	'paymentDetailsHomeCtrl',
	[
		'$scope',
		'adminService',
		'$rootScope',
		'$state',
		'$uibModal',
		'pePaymentToLenderUserList',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, adminService, $rootScope, $state, $uibModal,
			pePaymentToLenderUserList, CURRENT_HOST, CURRENT_VERSION) {

			$scope.searchText;
			var statusAwaiting = 'AWAITING';
			var statusRedeem = 'REDEEM';
			var statusRedeemed = 'REDEEMED';

			// $scope.userTypeOptionsDropdown = [{
			// 	type: 'lender',
			// 	value: 'Lender'
			// }, {
			// 	type: 'renter',
			// 	value: 'Renter'
			// }];

			$scope.paymentTypeOptionsDropdown = [
				// {
				// 	type: 'awaiting',
				// 	value: 'Awaiting'
				// }, {
				// 	type: 'redeemed',
				// 	value: 'Redeemed'
				// }

				{
					type: 'redeem',
					value: 'Yet to redeem'
				}, {
					type: 'awaiting',
					value: 'Redeemed'
				}, {
					type: 'redeemed',
					value: 'Paid'
				}
			];

			// $scope.selectedUserTypeOption = 'lender';
			// $scope.currentUserType = 'lender';

			// $scope.changeUserTypeSelection = function (userType) {
			// 	$scope.currentUserType = userType;
			// 	switch (userType) {
			// 		case 'lender': {
			// 			if ($scope.currentPaymentType == 'awaiting') {
			// 				$scope.getLenderList(statusAwaiting);
			// 			} else if ($scope.currentPaymentType == 'redeemed') {
			// 				$scope.getLenderList(statusRedeemed);
			// 			}
			// 			break;
			// 		}
			// 		case 'renter': {
			// 			$scope.getRenterList();
			// 			break;
			// 		}
			// 	}
			// };

			$scope.selectedPaymentTypeOption = 'redeem';
			$scope.currentPaymentType = 'redeem';

			$scope.getList = function () {
				// switch (currentType) {
				// 	case 'lender': {
				if ($scope.currentPaymentType == 'awaiting') {
					$scope.getLenderList(statusAwaiting);
				} else if ($scope.currentPaymentType == 'redeemed') {
					$scope.getLenderList(statusRedeemed);
				} else if ($scope.currentPaymentType == 'redeem') {
					$scope.getLenderList(statusRedeem);
				}
				// 	break;
				// }
				// case 'renter': {
				// 	break;
				// }
				// }
			};

			$scope.changePaymentTypeSelection = function (selectedPaymentType) {
				$scope.currentPaymentType = selectedPaymentType;
				// switch (selectedPaymentType) {
				// 	case 'redeem': {
				// 		// if ($scope.currentUserType == 'lender') {
				// 		$scope.getLenderList(statusRedeem);
				// 		// } else {
				// 		// 	$scope.getRenterList();
				// 		// }
				// 		break;
				// 	}
				// 	case 'awaiting': {
				// 		// if ($scope.currentUserType == 'lender') {
				// 		$scope.getLenderList(statusAwaiting);
				// 		// } else {
				// 		// 	$scope.getRenterList();
				// 		// }
				// 		break;
				// 	}
				// 	case 'redeemed': {
				// 		// if ($scope.currentUserType == 'lender') {
				// 		$scope.getLenderList(statusRedeemed);
				// 		// } else {
				// 		// 	$scope.getRenterList();
				// 		// }
				// 		break;
				// 	}
				// }

				$scope.getList();
			};

			$scope.columns = [
				{
					field: 'orderId',
					displayName: 'Lending Order #',
					enableColumnMenu: false,
					cellTemplate: '<div class="pT5">'
					+ '<div ng-if="row.entity.redeemStatus == \'REDEEM\'" ng-click="grid.appScope.openParentPaymentCtrl(row.entity)">{{row.entity.orderId.slice(-7)}}</div>'
					+ '<a href="" ng-if="(row.entity.redeemStatus == \'AWAITING\') || (row.entity.redeemStatus == \'REDEEMED\')" ng-click="grid.appScope.openParentPaymentCtrl(row.entity)">{{row.entity.orderId.slice(-7)}}</a>'
					+ '</div>'
				},
				{
					field: 'ownerName',
					displayName: 'Lender username',
					enableColumnMenu: false,
					cellTemplate: '<div class="pT5">'
					+ '<a href="" ng-click="grid.appScope.openUserDetails(row.entity)">{{row.entity.ownerName}}</a>'
					+ '</div>'
				}];

			var paginationOptions = {
				pageNumber: 1,
				pageSize: 30,
				sort: null
			};

			$scope.userListOptions = {
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
						$scope.getList($scope.currentUserType);
					});
				}
			};

			uiGridHeight();

			$scope.getLenderList = function (status) {
				if ($scope.currentPaymentType == 'awaiting') {
					status = statusAwaiting;
				} else if ($scope.currentPaymentType == 'redeemed') {
					status = statusRedeemed;
				} else if ($scope.currentPaymentType == 'redeem') {
					status = statusRedeem;
				}
				$scope.isDev = getHost(CURRENT_HOST);
				$scope.noData = false;
				$scope.userListOptions.data = [];
				pePaymentToLenderUserList.get({
					text: $scope.searchClicked ? $scope.searchText ? $scope.searchText
						: null
						: null,
					redeemStatus: status,
					limit: paginationOptions.pageSize,
					offset: (paginationOptions.pageNumber - 1)
					* paginationOptions.pageSize,
					sort: 'modifiedOn desc',
					timestamp: $scope.isDev ? new Date().getTime() : null,
					version: !$scope.isDev ? CURRENT_VERSION : null
				}, function (response) {
					if (response.data.length) {
						if (response.data[0].redeemStatus == 'REDEEM') {
							if ($scope.columns.length == 2) {
								$scope.columns.push({
									field: 'createdOn',
									displayName: 'Created Date',
									cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"'
								});
							} else if ($scope.columns.length == 3) {
								$scope.columns.splice($scope.columns.length - 1, 1);
								$scope.columns.push({
									field: 'createdOn',
									displayName: 'Created Date',
									cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"'
								});
							}
						} else if (response.data[0].redeemStatus == 'AWAITING') {
							if ($scope.columns.length == 3) {
								$scope.columns.splice($scope.columns.length - 1, 1);
								$scope.columns.push({
									field: 'redeemOn',
									displayName: 'Redeem Date',
									cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"'
								});
							}
						} else if (response.data[0].redeemStatus == 'REDEEMED') {
							if ($scope.columns.length == 3) {
								$scope.columns.splice($scope.columns.length - 1, 1);
								$scope.columns.push({
									field: 'redeemedOn',
									displayName: 'Redeemed Date',
									cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"'
								});
							}
						}
					}
					$scope.userListOptions.totalItems = response.paging.total;
					if ($scope.userListOptions.totalItems < 1) {
						$scope.noData = true;
					}
					$scope.userListOptions.paginationPageSizes.indexOf($scope.userListOptions.totalItems) === -1 ? $scope.userListOptions.paginationPageSizes.push($scope.userListOptions.totalItems) : '';
					$scope.userListOptions.data = [];
					$scope.userListOptions.data = response.data;
				});
			};

			// $scope.getRenterList = function () {
			// 	// service call for renter
			// 	$scope.userListOptions.data = [];
			// };

			$scope.getList();

			$scope.openUserDetails = function (owner) {
				$state.go('userDetail', {
					userId: owner.ownerId
				});
			}

			$scope.openParentPaymentCtrl = function (shipment) {
				var shipmentObj;
				if ($scope.currentPaymentType == 'awaiting') {
					shipmentObj = {
						templateUrl: 'views/admin/paymentDetails/modal/lenderPayment.html',
						controller: 'lenderPaymentCtrl',
						shipmentData: shipment
					};
					$scope.openLenderPaymentCtrl(shipmentObj);
				} else if ($scope.currentPaymentType == 'redeemed') {
					shipmentObj = {
						templateUrl: 'views/admin/paymentDetails/modal/lenderPaid.html',
						controller: 'lenderPaidCtrl',
						shipmentData: shipment
					};
					$scope.openLenderPaymentCtrl(shipmentObj);
				}
			}

			$scope.openLenderPaymentCtrl = function (shipmentObj) {
				var modalInstance = $uibModal.open({
					templateUrl: shipmentObj.templateUrl,
					controller: shipmentObj.controller,
					$scope: $scope,
					resolve: {
						data: function () {
							return shipmentObj.shipmentData;
						}
					}
				});
				modalInstance.result.then(function (shipmentId) {
					if (shipmentId && shipmentId == shipmentObj.shipmentData.id) {
						$scope.getList($scope.currentUserType);
					}
				});
			};

			$scope.search = function () {
				if (!$scope.searchClicked) {
					$scope.searchClicked = true;
					$scope.getList($scope.currentUserType);
				}
			};

			$scope.clearSearch = function () {
				$scope.searchClicked = false;
				$scope.searchText = $scope.searchText ? "" : null;
				$scope.getList($scope.currentUserType);
			};

			$scope.searchButtonState = function (searchText) {
				$scope.searchClicked = false;
				if (searchText.length < 1) {
					$scope.getList($scope.currentUserType);
				}
			};

		}]);