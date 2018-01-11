'use strict';

peAdminApp
	.controller(
	'dressListingHomeCtrl',
	[
		'$scope',
		'productService',
		'$state',
		'$uibModal',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, productService, $state, $uibModal, CURRENT_HOST,
			CURRENT_VERSION) {

			$scope.searchClicked = false;
			$scope.noData = false;

			$scope.dressListingDropdown = [{
				id: '0',
				value: 'Deleted'
			}, {
				id: '1',
				value: 'Published'
			}, {
				id: '2',
				value: 'Pending Approval'
			}, {
				id: '3',
				value: 'Rejected'
			}];

			$scope.selectedDressOption = '2';

			$scope.currentStatus = '2';

			$scope.changeDressSelction = function (status) {
				$scope.currentStatus = status;
				if (status === "1") {
					$scope.getProductListByStatus('1');
				} else if (status === "2") {
					$scope.getProductListByStatus('2');
				} else if (status === "3") {
					$scope.getProductListByStatus('3');
				} else {
					$scope.getProductListByStatus('0');
				}
			};

			var headerCellTemplate = '<div class="ui-grid-cell-contents ui-grid-header-cell-primary-focus" ><span class="ui-grid-header-cell-label title="puneet" ng-binding" style="background:red"></span></div>';

			$scope.columns = [
				{
					field: 'ownerName',
					headerCellTooltip: 'ownerName',
					displayName: 'Username',
					enableColumnMenu: false,
					cellTemplate: '<div class="ui-grid-cell-contents">'
					+ '<a href="" ng-click="grid.appScope.openUserDetails(row.entity.ownerId)">{{row.entity.ownerName}}</a>'
					+ '</div>'
				},
				{
					field: 'title',
					displayName: 'Dress Title',
					enableColumnMenu: false,
					cellTemplate: '<div class="ui-grid-cell-contents">'
					+ '<a href="" ng-click="grid.appScope.openDressDetails(row.entity.ownerId, row.entity.id)">{{row.entity.title}}</a>'
					+ '</div>'
				}, {
					field: 'firstName',
					displayName: 'First Name',
					enableColumnMenu: false
				}, {
					field: 'lastName',
					displayName: 'Last Name',
					enableColumnMenu: false
				}, {
					field: 'ownerSchoolName',
					displayName: 'School Name',
					width: '32%',
					enableColumnMenu: false,
					cellTooltip: function (row, col) {
						return row.entity.ownerSchoolName;
					}
				}, {
					field: 'homeZipCode',
					displayName: 'Home zip code',
					enableColumnMenu: false
				}, {
					field: 'statusText',
					displayName: 'Status',
					enableColumnMenu: false,
					cellTooltip: function (row, col) {
						return row.entity.statusText;
					}
				}];

			var paginationOptions = {
				pageNumber: 1,
				pageSize: 30,
				sort: null
			};

			$scope.productOptions = {
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
						$scope.getProductListByStatus($scope.currentStatus);
					});
				}
			};

			uiGridHeight();

			$scope.getProductListByStatus = function (status) {

				$scope.isDev = getHost(CURRENT_HOST);

				$scope.noData = false;
				var query = {
					status: status,
					limit: paginationOptions.pageSize,
					offset: (paginationOptions.pageNumber - 1)
					* paginationOptions.pageSize,
					sort: 'modifiedOn desc',
					text: $scope.searchClicked ? $scope.searchText : null,
					timestamp: $scope.isDev ? new Date().getTime() : null,
					version: !$scope.isDev ? CURRENT_VERSION : null
				};

				productService.getProductList(query).then(function (response) {
					$scope.productOptions.totalItems = response.paging.total;
					$scope.productOptions.paginationPageSizes.indexOf(response.paging.total) === -1 ? $scope.productOptions.paginationPageSizes.push(response.paging.total) : '';
					if ($scope.productOptions.totalItems < 1) {
						$scope.noData = true;
					}
					$scope.productOptions.data = [];
					angular.forEach(response.data, function (dressListingData) {
						if (response.data) {
							if (status == 0) {
								if ($scope.columns.length > 7) {
									$scope.columns.shift();
								}
								if ($scope.columns.length < 8) {
									$scope.columns.unshift({
										field: 'modifiedOn',
										displayName: 'Listing Deleted On',
										cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"',
										width: '18%',
										enableColumnMenu: false

									});
								}
								dressListingData.statusText = 'Deleted';
							} else if (status == 1) {
								if ($scope.columns.length > 7) {
									$scope.columns.shift();
								}
								if ($scope.columns.length < 8) {
									$scope.columns.unshift({
										field: 'modifiedOn',
										displayName: 'Listing Published On',
										cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"',
										width: '18%',
										enableColumnMenu: false

									});
								}
								dressListingData.statusText = 'Published';
							} else if (status == 2) {
								if ($scope.columns.length > 7) {
									$scope.columns.shift();
								}
								if ($scope.columns.length < 8) {
									$scope.columns.unshift({
										field: 'modifiedOn',
										displayName: 'Listing Submitted On',
										cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"',
										width: '18%',
										enableColumnMenu: false

									});
								}
								if (dressListingData.images && (dressListingData.images[1] == null || dressListingData.images[2] == null)) {
									dressListingData.statusText = 'Corrupt'
								} else {
									dressListingData.statusText = 'Pending Approval';
								}
							} else if (status == 3) {
								if ($scope.columns.length > 7) {
									$scope.columns.shift();
								}
								if ($scope.columns.length < 8) {
									$scope.columns.unshift({
										field: 'modifiedOn',
										displayName: 'Listing Rejected On',
										cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"',
										width: '18%',
										enableColumnMenu: false
									});
								}
								dressListingData.statusText = 'Rejected';
							}
						}

						// switch (dressListingData.status) {
						// 	case 0: {
						// 		dressListingData.statusText = 'Deleted';
						// 		break;
						// 	}
						// 	case 1: {
						// 		dressListingData.statusText = 'Published';
						// 		break;
						// 	}
						// 	case 2: {
						// 		if (dressListingData.images.indexOf(1) < 0 || dressListingData.images.indexOf(2) < 0) {
						// 			dressListingData.statusText = 'Corrupt';
						// 		} else {
						// 			dressListingData.statusText = 'Pending Approval';
						// 		}
						// 		break;
						// 	} case 3: {
						// 		dressListingData.statusText = 'Rejected';
						// 		break;
						// 	}
						// }
						dressListingData.ownerSchoolName = dressListingData.ownerDetails.organizationName.split("#$#&#")[0];
						dressListingData.firstName = dressListingData.ownerDetails.name.split(" ")[0];
						dressListingData.lastName = dressListingData.ownerDetails.name.split(" ")[1];
						dressListingData.homeZipCode = dressListingData.ownerDetails.homeAddress.zipCode;
						$scope.productOptions.data.push(dressListingData);
					});
					// $scope.productOptions.data = response.data;

				});
			};

			$scope.getProductListByStatus('2');

			$scope.openDressDetails = function (ownerId, productId) {
				$state.go('dressDetail', {
					ownerId: ownerId,
					productId: productId
				});
				productService.resetNotifyFields();
			};

			$scope.openUserDetails = function (userId) {
				$state.go('userDetail', {
					userId: userId
				});
			};

			$scope.activateProduct = function (productId) {

				$scope.header = 'Approve Listing'
				var modalInstance = $uibModal.open({
					templateUrl: 'views/admin/deleteEntity.html',
					controller: 'deleteEntityCtrl',
					$scope: $scope,
					resolve: {
						data: function () {
							return productId;
						},
						header: function () {
							return $scope.header;
						}
					}
				});
				modalInstance.result.then(function (response) {
					$scope.getProductListByStatus($scope.currentStatus);
				});
			};

			$scope.search = function () {
				if (!$scope.searchClicked) {
					$scope.searchClicked = true;
					$scope.getProductListByStatus($scope.currentStatus);
				}
			};

			$scope.clearSearch = function () {
				$scope.searchClicked = false;
				$scope.searchText = "";
				$scope.getProductListByStatus($scope.currentStatus);
			};

			$scope.searchButtonState = function (searchText) {
				$scope.searchClicked = false;
				if (searchText.length < 1) {
					$scope.getProductListByStatus($scope.currentStatus);
				}
			};

		}]);