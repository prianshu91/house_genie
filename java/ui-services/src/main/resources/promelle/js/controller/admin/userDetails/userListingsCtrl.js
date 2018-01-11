'use strict';

peAdminApp
		.controller(
				'userListingsCtrl',
				[
						'$scope',
						'adminService',
						'$stateParams',
						'$uibModal',
						'$state',
						'CURRENT_HOST',
						'CURRENT_VERSION',
						function($scope, adminService, $stateParams, $uibModal, $state,
								CURRENT_HOST, CURRENT_VERSION) {

							$scope.userListings = {
								enableColumnResize : true,
								enableHorizontalScrollbar : 0,
								enableVerticalScrollbar : 1,
								enableSorting : true,
								enableFiltering : false,
								paginationPageSizes : [ 10, 20 ],
								paginationPageSize : 10,
								columnDefs : [
										{
											field : 'createdOn',
											displayName : 'Listing Date',
											enableColumnMenu : false,
											cellFilter : 'date:"medium"'
										},
										{
											field : 'title',
											displayName : 'Liting Title',
											enableColumnMenu : false,
											cellTemplate : '<div class="pT5">'
													+ '<a href="" ng-click="grid.appScope.openListingDetails(row.entity.id)">{{row.entity.title}}</a>'
													+ '</div>'
										} ]
							};

							$scope.isDev = getHost(CURRENT_HOST);

							var query = {
								ownerId : $stateParams.userId ? $stateParams.userId : null,
								limit : "-1",
								offset : "-1",
								timestamp : $scope.isDev ? new Date().getTime() : null,
								version : !$scope.isDev ? CURRENT_VERSION : null
							};

							adminService.getProductListingData(query).then(
									function(response) {
										$scope.userListings.data = response.data;
									});

							$scope.openListingDetails = function(listingId) {

								$state.go('dressDetail', {
									ownerId : $stateParams.userId,
									productId : listingId
								});
							}
						} ]);