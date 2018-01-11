'use strict';

peAdminApp
	.controller(
	'broadcastsCtrl',
	[
		'$scope',
		'adminService',
		'$state',
		'$uibModal',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		'EMAIL_FROM_ADDRESS',
		function ($scope, adminService, $state, $uibModal, CURRENT_HOST,
			CURRENT_VERSION, EMAIL_FROM_ADDRESS) {
			$scope.searchClicked;
			$scope.searchText;
			$scope.userList;
			$scope.sortKey = "createdOn desc";
			$scope.mySelectedRows = [];

			$scope.setSortKey = function (param) {
				$scope.sortKey = param;
				$scope.reverse = !$scope.reverse;
			}

			var paginationOptions = {
				pageNumber: 1,
				pageSize: 30,
				sort: null
			};

			$scope.broadcastListOptions = {
				enableColumnResize: true,
				enableHorizontalScrollbar: 0,
				enableVerticalScrollbar: 1,
				enableSorting: true,
				enableFiltering: false,
				paginationPageSizes: [30, 60],
				paginationPageSize: 30,
				useExternalPagination: true,
				columnDefs: [
					{
						field: 'name',
						displayName: 'User Name',
						enableColumnMenu: false
					}, {
						field: 'mobile',
						displayName: 'Mobile Number',
						enableColumnMenu: false
					}, {
						field: 'email',
						displayName: 'Email Address',
						enableColumnMenu: false
					}],
				onRegisterApi: function (gridApi) {
					$scope.gridApi = gridApi;
					gridApi.pagination.on.paginationChanged($scope, function (
						newPage, pageSize) {
						paginationOptions.pageNumber = newPage;
						paginationOptions.pageSize = pageSize;
						$scope.getUserData();
					});
					gridApi.selection.on.rowSelectionChanged($scope, function (row) {
						$scope.mySelectedRows = $scope.gridApi.selection.getSelectedRows();
					});

				}
			};

			$scope.getUserData = function (params) {
				$scope.isDev = getHost(CURRENT_HOST);
				$scope.noData = false;
				var query = {
					status: 1,
					limit: paginationOptions.pageSize,
					offset: (paginationOptions.pageNumber - 1)
					* paginationOptions.pageSize,
					sort: $scope.sortKey,
					text: $scope.searchText ? $scope.searchText : null,
					timestamp: $scope.isDev ? new Date().getTime() : null,
					version: !$scope.isDev ? CURRENT_VERSION : null
				};

				adminService
					.getUserList(query)
					.then(
					function (response) {
						$scope.broadcastListOptions.totalItems = response.paging.total;
						if ($scope.broadcastListOptions.totalItems < 1) {
							$scope.noData = true;
						}
						$scope.broadcastListOptions.paginationPageSizes.indexOf(response.paging.total) === -1 ? $scope.broadcastListOptions.paginationPageSizes.push(response.paging.total) : '';
						$scope.broadcastListOptions.data = [];
						$scope.broadcastListOptions.data = response.data;
					});
			}
			$scope.getUserData();

			$scope.search = function () {
				if (!$scope.searchClicked) {
					$scope.searchClicked = true;
					$scope.getUserData();
				}
			};

			$scope.clearSearch = function () {
				$scope.searchClicked = false;
				$scope.searchText = $scope.searchText ? "" : null;
				$scope.searchDate = $scope.searchDate ? "" : null;
				$scope.getUserData();
			};

			$scope.searchButtonState = function (searchText, searchDate) {
				$scope.searchClicked = false;
				if (searchText && searchText.length < 1) {
					$scope.getUserData();
				} else if (!searchDate) {
					$scope.getUserData();
				}
			};



			$scope.getSelectedRows = function () {
				$scope.mySelectedRows = $scope.gridApi.selection.getSelectedRows();
				return $scope.mySelectedRows;
			};

			//   $scope.broadcastMessages = function () {

			//   	var userEmailData = [];
			//   	angular.forEach($scope.broadcastListOptions.data, function (user) {
			//           if(user.Selected){
			//           	userEmailData.push(user.email);
			//           }
			//       });
			//       var query = {
			// 	email_address:  userEmailData.join(',')
			// };
			// adminService.broadcastEmail(query).then(function (res) {
			// 	if (res) {
			// 		alert('Email(s) broadcast successfully.');
			// 		$scope.selectedAll = false;
			// 		angular.forEach($scope.broadcastListOptions.data, function (user) {
			//             if(user.Selected){
			//             	user.Selected = false;
			//             }
			//         });
			// 	}
			// },
			// function (error) {
			// 	$scope.selectedAll = false;
			// 	angular.forEach($scope.broadcastListOptions.data, function (user) {
			//             if(user.Selected){
			//             	user.Selected = false;
			//             }
			//         });
			// 	console.log("Error in sending broadcast email. Error Info :" + error);
			// });
			//   }


			$scope.open = function () {

				var modalInstance = $uibModal.open({
					templateUrl: 'views/admin/broadcasts/modal/myModalContent.html', // loads the template
					backdrop: true, // setting backdrop allows us to close the modal window on clicking outside the modal window
					windowClass: 'modal', // windowClass - additional CSS class(es) to be added to a modal window template
					controller: function ($scope, $uibModalInstance, users, EMAIL_FROM_ADDRESS, selectedAll, selectedRows, clearSelection) {
						$scope.submit = function () {
							var userEmailData = [];
							angular.forEach(selectedRows, function (user) {
								userEmailData.push(user.email);
							});
							$scope.mySelectedRows = [];
							clearSelection.clearSelectedRows();
							var query = {
								bcc: userEmailData.join(','),
								to: EMAIL_FROM_ADDRESS,
								from: EMAIL_FROM_ADDRESS,
								subject: $scope.subject,
								body: $scope.body
							};
							adminService.broadcastEmail(query).then(function (res) {
								if (res) {
									alert('Email(s) broadcast successfully.');
								}
							},
								function (error) {
									console.log("Error in sending broadcast email. Error Info :" + error);
								});
							$uibModalInstance.close('cancel'); // dismiss(reason) - a method that can be used to dismiss a modal, passing a reason
						}
						$scope.cancel = function () {
							clearSelection.clearSelectedRows();
							$uibModalInstance.close('cancel');
						};
					},
					resolve: {
						users: function () {
							return $scope.broadcastListOptions.data;
						},
						selectedAll: function () {
							return $scope.selectedAll;
						},
						selectedRows: function () {
							return $scope.getSelectedRows();
						},
						clearSelection: function () {
							return $scope.gridApi.selection;
						}

					}
				});
				modalInstance.result.then(function (result) {
					if (result == 'cancel') {
						$scope.mySelectedRows = [];
					}
				});
			}; // end of scope.open function
		}]);