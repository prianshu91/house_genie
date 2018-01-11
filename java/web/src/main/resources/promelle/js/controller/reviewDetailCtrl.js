'use strict';
promelleApp.controller('reviewDetailCtrl', [ 
	'$scope', 
	'productService',
	'$stateParams',
	function($scope, productService, $stateParams) {
		$scope.isLoading = true;

		$scope.itemsPerPage = 10;
  		$scope.currentPage = 1;

  		var paginationOptions = {
			pageNumber : 0,
			pageSize : 10,
			sort : 'timestamp desc',
			productId: $stateParams.productId
		};

		

		$scope.getReviewData = function(){
			productService.getReviewDetails(paginationOptions).then(function(response){
				$scope.isLoading = false;
				$scope.reviewData = response.data;
				$scope.totalItems = response.paging.total;
				
				$scope.dressData = productService.getProductById();
				$scope.dressData.imageUrl = getThumbnailImg($scope.dressData.images[1]);
			}, function(errorInfo) {
				console.log(errorInfo);
			});
		};

		$scope.getReviewData();

		$scope.pageChanged = function(page) {
			$scope.currentPage = page;
			paginationOptions.pageNumber = ($scope.currentPage - 1) * paginationOptions.pageSize;
		    $scope.getReviewData();
		};
	}
]);