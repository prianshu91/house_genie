'use strict';
promelleApp.controller('reviewDetailCtrl', [ 
	'$scope', 
	'productService',
	'$stateParams',
	function($scope, productService, employeeService, $stateParams,$uibModalInstance) {
		$scope.isLoading = true;
		$scope.options = ["High to Low", "Low to High", "Recent", "Oldest"];
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
		
		
		let size = $scope.ratings.length;
		for (var i = 0; i < size; i++) {
			var comment_date = new Date(Date.parse($scope.ratings[i].comment_date));
			var rating = {
					"user": $scope.ratings[i].user_id,
					"rating": parseFloat($scope.ratings[i].ratings),
					"comment": $scope.ratings[i].comment,
					"date": comment_date,
					"visibleDate": $scope.ratings[i].comment_date
			};
			ratingData.push(rating);
		}
		
		//Model Dialog functions
		$scope.loadOptions = function() {
			$scope.options = options;
		}
		$scope.ok = function() {
			$uibModalInstance.hide();
		};

		$scope.cancel = function() {
			$uibModalInstance.cancel();
		};

		//Function to sort ratings
		$scope.sortRatings = function(order) {
			$scope.sort = employeeService.getSortingOrder(order);
		}
		
		
		
	}
]);