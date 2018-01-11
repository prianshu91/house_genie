'use strict';

promelleApp.controller('homeCtrl', [ 
	'$scope', 
	'productService',
	'$state',
	'$window',
	function($scope, productService, $state, $window) {
		$scope.myInterval = 3000;
		$scope.noWrapSlides= false;
		$scope.slides = [
		    {
		    	id: 0,
		    	caption: 'Welcome to HouseGenie!',
		    	sub_caption: 'The unlimited closet for all your needs!',
		    	image: 'images/sslide-1.jpg'
		    },
		    {
		    	id: 1,
		    	caption: 'Hire and live life in a easy way',
		    	sub_caption: null,
		    	image: 'images/sslide-2.jpg'
		    },
		    {
		    	id: 2,
		    	caption: 'A happy smile on our customers is all we want!',
		    	sub_caption: null,
		      image: 'images/sslide-3.jpg'
		    },
		    {
		    	id: 3,
		    	caption: 'We are a team of experts!',
		    	sub_caption: null,
		      image: 'images/sslide-4.jpg'
		    }
		];


		$scope.browseProducts = function(){
			$state.go('productList');
		};

		var paginationOptions = {
			pageNumber : 0,
			pageSize : 10,
			sort : 'modifiedOn desc',
			status: 1
		};

		$scope.getFilterResults = function(query) {
			$scope.listingData = [];
			productService.getProductList(query).then(function(response) {
				$scope.listingData = response.data;
				$scope.listingData.forEach(function(list, index){
					list.size = correctString(list.size);
					if(list.images && list.images[1]){
						list.imageUrl = getThumbnailImg(list.images[1]);
					} else {
						list.imageUrl = 'images/No-image-found.jpg';
					}
			    });
			}, function(errorInfo) {
				console.log(errorInfo);
			});
		};

		$scope.getFilterResults(paginationOptions);

		$scope.openDressDetails = function(data) {
			var url = $state.href('productDetail', 
				{
					ownerId : data.ownerId,
					productId : data.id
				}, 
				{
					absolute: true
				}
			);
			$window.open(url,'_blank');
		}

		$scope.viewAllTestimonials = function() {
			$state.go('testimonials');
		};
	}	
]);