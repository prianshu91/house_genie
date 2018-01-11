'use strict';

promelleApp.controller('productDetailCtrl', [ 
	'$scope', 
	'productService',
	'$stateParams',
	'$state',
	function($scope, productService, $stateParams, $state) {

		$scope.reviewLimit = 3;
		$scope.dressData = {};
		$scope.isReadyResponse = false;
		$scope.sizeArr = [];
		$scope.typeArr = [];

		productService.getProductById($stateParams.productId).then(function(response){
			$scope.dressData = response.data;
			if($scope.dressData.size.length){
				$scope.dressData.size.forEach(function(size){
					$scope.sizeArr.push(correctString(size));
				});
			} else {
				$scope.sizeArr.push('N/A');
			}

			$scope.typeArr = $scope.dressData.type;
			
			var price = $scope.dressData.rentalPrice;
			$scope.dressData.rentalPrice = calculatePrice($scope.dressData.rentalPrice);
			$scope.dressData.originalPrice = calculatePrice($scope.dressData.originalPrice);
			$scope.dressData.type = $scope.dressData.type.toString();
			if($scope.dressData.rentalPeriod == 4){
				$scope.rental4days = $scope.dressData.rentalPrice;
			} 

			if($scope.dressData.availForOtherPeriod) {
				$scope.rental8days = calculatePrice(1.15*price);
			} else {
				$scope.rental8days = 'Not Available';
			}

			var arr = [],
				caption;
			$scope.images = [];
			Object.keys($scope.dressData.images).map(function(key) {
				if (key == 1) {
					caption = "Image 1 - Front Image";
				}
				if (key == 2) {
					caption = "Image 2 - Back Image";
				}
				if (key == 3) {
					caption = "Image 3 - Other Image";
				}
				if (key == 4) {
					caption = "Image 4 - Other Image";
				}
				arr.push({
					caption : caption,
					url : $scope.dressData.images[key]
				})
			});

			arr.forEach(function(val) {
				var thumbImgUrl = getThumbnailImg(val.url);
				$scope.images.push({
					url : val.url + '?' + (new Date()).getTime(),
					thumbUrl : thumbImgUrl + '?'
							+ (new Date()).getTime(),
					caption : val.caption
				});
			});
		},function(errorInfo) {
			console.log(errorInfo);
		});

		productService.getUserById($stateParams.ownerId).then(function(response){
			$scope.ownerData = response.data;
			$scope.ownerData.homeAddress.city = $scope.ownerData.homeAddress.city ? capitalizeText($scope.ownerData.homeAddress.city) + ', ': null;
			$scope.ownerData.homeAddress.state = $scope.ownerData.homeAddress.state ? capitalizeText($scope.ownerData.homeAddress.state) : null;
			$scope.ownerData.school = $scope.ownerData.organizationName ? $scope.ownerData.organizationName.split('#$#&#'): null;

			if ($scope.ownerData.school == "NoSchool") {
				$scope.schoolName = 'N/A';
				$scope.schoolAddress = 'N/A';
			} else {
				$scope.schoolName = $scope.ownerData.school[0];
				$scope.schoolAddress = $scope.ownerData.school[1];
			}
		},function(errorInfo) {
			console.log(errorInfo);
		});

		var calculatePrice = function(price){
			if(price >= 1){
				price = price+'.00';
			} else {
				price = price+'0';
			}
			var finalPrice = parseFloat(price);
			return '$'+finalPrice.toFixed(2);
		};
		
		var query = {
			productId : $stateParams.productId,
			limit : "-1",
			offset : "-1",
			sort : "timestamp desc"
		}
		/*review data*/
		productService.getReviewDetails(query).then(function(response){
			$scope.isReadyResponse = true;
			$scope.dressReviewData = response.data;
			$scope.reviewCount = $scope.dressReviewData.length;
			var totalReviewVal = 0,
				counter = 0;

			$scope.dressReviewData.forEach(function(review){
				if(review.rating){
					totalReviewVal = totalReviewVal + review.rating;
					counter ++;
				}
			});

			if($scope.reviewCount >= 1){
				$scope.averageRatingCount =  Math.ceil(totalReviewVal / counter);
			} else {
				$scope.averageRatingCount = 0;
			}
		}, function(errorInfo) {
			console.log(errorInfo);
		});

		$scope.showAllReviews = function(id){
			$state.go('reviewDetail', {
				productId: id
			});
		}
	}	
]);