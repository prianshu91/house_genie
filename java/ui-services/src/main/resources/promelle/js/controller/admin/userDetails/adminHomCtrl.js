'use strict';

peAdminApp
	.controller(
	'adminHomCtrl',
	[
		'$scope',
		'adminService',
		'$rootScope',
		'$state',
		'$uibModal',
		'toaster',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, adminService, $rootScope, $state, $uibModal,
			toaster, CURRENT_HOST, CURRENT_VERSION) {

			$scope.selectedSearchType = '0';

			$scope.searchClicked = false;
			$scope.noData = false;

			$scope.dateOptions = {
				maxDate: new Date()
			};

			$scope.searchOptionsDropdown = [{
				id: '0',
				value: 'All'
			}, {
				id: '1',
				value: 'Date'
			}];

			$scope.changeSearchSelection = function (searchId) {
				if ($scope.searchDate) {
					$scope.searchDate = null;
				}
				if ($scope.searchText) {
					$scope.searchText = null;
				}
				if ($scope.searchClicked) {
					$scope.searchClicked = false;
				}

				$scope.getUserByStatus($scope.currentOption);

			};

			$scope.deleteHeader = 'Delete User';
			$scope.blockHeader = 'Block User';
			$scope.unblockHeader = 'Unblock User';

			$scope.userOptionsDropdown = [{
				id: '0',
				value: 'Deleted'
			}, {
				id: '1',
				value: 'Active'
			}, {
				id: '3',
				value: 'Blocked'
			},];

			$scope.selectedUserOption = '1';

			$scope.currentOption = '1';

			$scope.changeUserSelction = function (optionId) {
				$scope.currentOption = optionId;
				if (optionId === "1") {
					$scope.getUserByStatus('1');
				} else if (optionId === "3") {
					$scope.getUserByStatus('3');
				} else {
					$scope.getUserByStatus('0');
				}
			};

			$scope.columns = [
				{
					field: 'createdOn',
					displayName: 'Date Of Joining',
					cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"',
					width: '16%',
					enableColumnMenu: false
				},
				{
					field: 'username',
					displayName: 'Username',
					enableColumnMenu: false,
					cellTemplate: '<div class="pT5">'
					+ '<a href="" ng-click="grid.appScope.openUserDetails(row.entity.id)">{{row.entity.username}}</a>'
					+ '</div>'
				}, {
					field: 'firstName',
					displayName: 'First Name',
					enableColumnMenu: false,
					cellTooltip: function (row, col) {
						return row.entity.firstName;
					}
				}, {
					field: 'lastName',
					displayName: 'Last Name',
					enableColumnMenu: false,
					cellTooltip: function (row, col) {
						return row.entity.lastName;
					}
				}, {
					field: 'homeAddress.zipCode',
					displayName: 'Zipcode',
					enableColumnMenu: false
				}, {
					field: 'school.schoolName',
					displayName: 'School Name',
					enableColumnMenu: false,
					width: '25%',
					cellTooltip: function (row, col) {
						return row.entity.schoolName;
					}
				}];

			var paginationOptions = {
				pageNumber: 1,
				pageSize: 30,
				sort: null
			};

			$scope.usersOptions = {
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
						$scope.getUserByStatus($scope.currentOption);
					});
				}
			};

			uiGridHeight();

			$scope.getUserByStatus = function (status) {

				$scope.isDev = getHost(CURRENT_HOST);

				$scope.noData = false;
				var query = {
					status: status,
					limit: paginationOptions.pageSize,
					offset: (paginationOptions.pageNumber - 1)
					* paginationOptions.pageSize,
					sort: 'createdOn desc',
					searchText: $scope.searchClicked ? $scope.searchText ? $scope.searchText
						: null
						: null,
					searchDate: $scope.searchClicked ? $scope.searchDate ? $scope.searchDate
						.getTime()
						: null
						: null,
					timestamp: $scope.isDev ? new Date().getTime() : null,
					version: !$scope.isDev ? CURRENT_VERSION : null
				};

				adminService
					.getUserList(query)
					.then(
					function (response) {
						$scope.usersOptions.totalItems = response.paging.total;
						if ($scope.usersOptions.totalItems < 1) {
							$scope.noData = true;
						}
						$scope.usersOptions.data = [];
						$scope.usersOptions.paginationPageSizes.indexOf(response.paging.total) === -1 ? $scope.usersOptions.paginationPageSizes.push(response.paging.total) : '';
						if (query.status === '1' || query.status === '3') {
							if ($scope.columns.length < 7) {
								$scope.columns
									.push({
										field: 'action',
										displayName: 'Action',
										enableColumnMenu: false,
										enableSorting: false,
										width: '15%',
										cellTemplate: '<label type="button" ng-show="row.entity.status == 1" class="btn btn-danger btn-xs mt4" type="submit" ng-click="grid.appScope.blockModal(row.entity)" >'
										+ '<span aria-hidden="true" class="fa  fa-ban fa-sm"></span> Block'
										+ '</label>&nbsp;'
										+ '<label type="button" ng-show="row.entity.status !== 1" class="btn btn-primary btn-xs mt4" type="submit" ng-click="grid.appScope.unblockModal(row.entity)" >'
										+ '<span aria-hidden="true" class="fa  fa-thumbs-up fa-sm"></span> Unblock'
										+ '</label>&nbsp;'
										+ '<label type="button" class="btn btn-default btn-xs mt4" type="submit" ng-click="grid.appScope.deleteModal(row.entity)" >'
										+ '<span aria-hidden="true" class="fa  fa-trash fa-sm"></span> Delete'
										+ '</label>'
									});
							}
							// school.schoolName
							angular.forEach(response.data, function (userData) {
								// var schoolName = userData.organizationName
								// 	.split('#$#&#');
								// userData.schoolName = schoolName[0];
								userData.firstName = userData.name.split(' ')[0];
								userData.lastName = userData.name.split(' ')[1];
								$scope.usersOptions.data.push(userData);
							});
						} else if (query.status === '0') {
							if ($scope.columns.length == 7) {
								$scope.columns.splice($scope.columns.length - 1, 1);
							}
							angular.forEach(response.data, function (userData) {
								// var schoolName = userData.organizationName
								// 	.split('#$#&#');
								// userData.schoolName = schoolName[0];
								userData.firstName = userData.name.split(' ')[0];
								userData.lastName = userData.name.split(' ')[1];
								$scope.usersOptions.data.push(userData);
							});
						}
					});
			};

			$scope.getUserByStatus($scope.currentOption);

			$scope.openUserDetails = function (userId) {
				$state.go('userDetail', {
					userId: userId
				});
			};

			$scope.deleteModal = function (user) {
				var modalInstance = $uibModal.open({
					templateUrl: 'views/admin/deleteEntity.html',
					controller: 'deleteEntityCtrl',
					$scope: $scope,
					resolve: {
						data: function () {
							return user;
						},
						header: function () {
							return $scope.deleteHeader;
						}
					}
				});
			};

			$scope.blockModal = function (user) {
				var modalInstance = $uibModal.open({
					templateUrl: 'views/admin/deleteEntity.html',
					controller: 'deleteEntityCtrl',
					$scope: $scope,
					resolve: {
						data: function () {
							return user;
						},
						header: function () {
							return $scope.blockHeader;
						}
					}
				});
			};

			$scope.unblockModal = function (user) {
				var modalInstance = $uibModal.open({
					templateUrl: 'views/admin/deleteEntity.html',
					controller: 'deleteEntityCtrl',
					$scope: $scope,
					resolve: {
						data: function () {
							return user;
						},
						header: function () {
							return $scope.unblockHeader;
						}
					}
				});
				modalInstance.result.then(function () {
					$scope.changeUserSelction($scope.currentOption);
				});
			};

			$scope.search = function () {
				if (!$scope.searchClicked) {
					$scope.searchClicked = true;
					$scope.getUserByStatus($scope.currentOption);
				}
			};

			$scope.clearSearch = function () {
				$scope.searchClicked = false;
				$scope.searchText = $scope.searchText ? "" : null;
				$scope.searchDate = $scope.searchDate ? "" : null;
				$scope.getUserByStatus($scope.currentOption);
			};

			$scope.searchButtonState = function (searchText, searchDate) {
				$scope.searchClicked = false;
				if (searchText && searchText.length < 1) {
					$scope.getUserByStatus($scope.currentOption);
				} else if (!searchDate) {
					$scope.getUserByStatus($scope.currentOption);
				}
			};

		}]);