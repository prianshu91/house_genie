'use strict';

peAdminApp
	.controller(
	'dressReviewAndRatingsCtrl',
	[
		'$scope',
		'productService',
		'$stateParams',
		'$uibModal',
		'toaster',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, productService, $stateParams, $uibModal, toaster, CURRENT_HOST, CURRENT_VERSION) {

			$scope.header = 'Delete Review';

			$scope.reviewAndRatingsOptions = {
				enableColumnResize: true,
				enableHorizontalScrollbar: 0,
				enableVerticalScrollbar: 1,
				enableSorting: true,
				enableFiltering: false,
				paginationPageSizes: [10, 20],
				paginationPageSize: 10,
				columnDefs: [
					{
						field: 'timestamp',
						displayName: 'Date of Review',
						enableColumnMenu: false,
						cellFilter: 'date:"medium"'
					},
					{
						field: 'userName',
						displayName: 'Username',
						enableColumnMenu: false
					},
					{
						field: 'view',
						displayName: 'View',
						enableColumnMenu: false,
						cellTemplate: '<div>'
						+ '<a href="" ng-click="grid.appScope.openReviewDetails(row.entity)"><i class="fa fa-search" aria-hidden="true"></i></a>'
						+ '</div>'
					},
					{
						field: 'deleteEntity',
						displayName: 'Delete',
						enableColumnMenu: false,
						cellTemplate: '<label type="button" class="btn btn-danger btn-xs" type="submit" ng-click="grid.appScope.deleteModal(row.entity)" >'
						+ '<span aria-hidden="true" class="fa  fa-trash fa-lg"></span> Delete'
						+ '</label>'
					}]
			};

			var data = function () {

				$scope.isDev = getHost(CURRENT_HOST);

				var query =
					{
						productId: $stateParams.productId,
						limit: "-1",
						offset: "-1",
						sort: "timestamp desc",
						timestamp: $scope.isDev ? new Date().getTime() : null,
						version: !$scope.isDev ? CURRENT_VERSION : null
					}

				productService.getReviewDetails(query).then(
					function (response) {
						$scope.reviewAndRatingsOptions.data = [];
						angular.forEach(response.data, function (dressReview) {
							if (!dressReview.status && !(dressReview.status == 0)) {
								$scope.reviewAndRatingsOptions.data = response.data;
							}
						});
					});
			}

			data();

			$scope.openReviewDetails = function (data) {
				var modalInstance = $uibModal
					.open({
						templateUrl: 'views/admin/dressDetails/modal/reviewModal.html',
						controller: 'reviewModalCtrl',
						$scope: $scope,
						resolve: {
							data: function () {
								return data;
							}
						}
					});
			}

			$scope.deleteModal = function (review) {
				var modalInstance = $uibModal.open({
					templateUrl: 'views/admin/deleteEntity.html',
					controller: 'deleteEntityCtrl',
					$scope: $scope,
					resolve: {
						data: function () {
							return review;
						},
						header: function () {
							return $scope.header;
						}
					}
				});
				modalInstance.result.then(function () {
					data();
				});
			}

		}]);