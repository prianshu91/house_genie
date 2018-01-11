app.controller('DashboardCtrl', function($scope, $http) {
	
	$scope.tabs = ['Product', 'Category', 'Occasion', 'Length', 'Type', 'Color', 'Size', 'Care Instructions', 'Review'];

	$scope.currentTab = 'Product';

	$scope.onClickTab = function (tab) {
	    $scope.currentTab = tab;
	}

	$scope.isActiveTab = function(tab) {
	    return tab == $scope.currentTab;
	}

	$scope.loadProducts = function() {
		$http.get('/service/product/list?offset=-1&limit=-1').success(function(data) {
			$scope.products = data.data;
			$scope.createNewProduct();
		});
	}

	$scope.createNewProduct = function() {
		$scope.newProduct = {};
	}

	$scope.editProduct = function(product) {
		if (product.category != undefined) {
			product['categoryStr'] = product.category.join();
		}
		if (product.occasion != undefined) {
			product['occasionStr'] = product.occasion.join();
		}
		if (product.color != undefined) {
			product['colorStr'] = product.color.join();
		}
		if (product.careInstruction != undefined) {
			product['careInstructionStr'] = product.careInstruction.join();
		}
		$scope.newProduct = product;
	}

	$scope.addProduct = function(product) {
		if (product == undefined) {
			product = {};
		}
		if (product.title == undefined || product.title.trim() == '') {
			product.error = "Title is a required field!!";
			return;
		}
		if (product.ownerId == undefined || product.ownerId.trim() == '') {
			product.error = "Owner Id is a required field!!";
			return;
		}
		if (product.categoryStr == undefined || product.categoryStr.trim() == '') {
			product.error = "Category is a required field!!";
			return;
		}
		delete product.error;
		
		var category = [];
		category[0] = product.categoryStr;
		product['category'] = category;
		delete product.categoryStr;

		var occasion = [];
		occasion[0] = product.occasionStr;
		product['occasion'] = occasion;
		delete product.occasionStr;

		var color = [];
		color[0] = product.colorStr;
		product['color'] = color;
		delete product.colorStr;

		var careInstruction = [];
		careInstruction[0] = product.careInstructionStr;
		product['careInstruction'] = careInstruction;
		delete product.careInstructionStr;

		console.log(product);
		if (product.id == undefined) {
			$http.post('/service/product', product).success(function(data) {
				$scope.loadProducts();
			});
		} else {
			$http.put('/service/product/' + product.id, product).success(function(data) {
				$scope.loadProducts();
			});
		}
	}

	$scope.deleteProduct = function(productId) {
		$http.delete('/service/product/' + productId).success(function(data) {
			$scope.loadProducts();
		});
	}

	$scope.getProductDetail = function(product) {
		if (product.category != undefined) {
			product['categoryStr'] = product.category.join();
		}
		if (product.occasion != undefined) {
			product['occasionStr'] = product.occasion.join();
		}
		if (product.color != undefined) {
			product['colorStr'] = product.color.join();
		}
		if (product.careInstruction != undefined) {
			product['careInstructionStr'] = product.careInstruction.join();
		}
		$scope.selectedProduct = product;
	}

	$scope.loadCategories = function() {
		$http.get('/service/product/categories').success(function(data) {
			$scope.categories = data.data;
		});
	}

	$scope.loadOccasions = function() {
		$http.get('/service/product/occasions').success(function(data) {
			$scope.occasions = data.data;
		});
	}

	$scope.loadLengths = function() {
		$http.get('/service/product/lengths').success(function(data) {
			$scope.lengths = data.data;
		});
	}

	$scope.loadTypes = function() {
		$http.get('/service/product/types').success(function(data) {
			$scope.types = data.data;
		});
	}

	$scope.loadColors = function() {
		$http.get('/service/product/colors').success(function(data) {
			$scope.colors = data.data;
		});
	}

	$scope.loadSizes = function() {
		$http.get('/service/product/sizes').success(function(data) {
			$scope.sizes = data.data;
		});
	}

	$scope.loadCareInstructions = function() {
		$http.get('/service/product/careInstructions').success(function(data) {
			$scope.careInstructions = data.data;
		});
	}

	$scope.activateProduct = function(productId) {
		$http.put('/service/product/' + productId +  '/activate').success(function(data) {
			$scope.loadProducts();
		});
	}

	$scope.loadReviews = function(productId) {
		$http.get('/service/review/list?offset=-1&limit=-1' +  (productId != undefined &&
				productId != 'none' ? ('&productId=' + productId) : '')).success(function(data) {
			$scope.reviews = data.data;
			$scope.createNewReview();
		});
	}

	$scope.createNewReview = function() {
		$scope.newReview = {};
	}

	$scope.editReview = function(review) {
		$scope.newReview = review;
	}

	$scope.addReview = function(review) {
		if (review == undefined) {
			review = {};
		}
		if (review.title == undefined || review.title.trim() == '') {
			review.error = "Title is a required field!!";
			return;
		}
		if (review.userId == undefined || review.userId.trim() == '') {
			review.error = "User Id is a required field!!";
			return;
		}
		if (review.userName == undefined || review.userName.trim() == '') {
			review.error = "User Name is a required field!!";
			return;
		}
		delete review.error;

		console.log(review);
		if (review.id == undefined) {
			$http.post('/service/review', review).success(function(data) {
				$scope.loadReviews(review.productId);
			});
		} else {
			$http.put('/service/review/' + review.id, review).success(function(data) {
				$scope.loadReviews(review.productId);
			});
		}
	}

	$scope.deleteReview = function(review) {
		$http.delete('/service/review/' + review.id).success(function(data) {
			$scope.loadReviews(review.productId);
		});
	}

	$scope.getReviewDetail = function(review) {
		$scope.selectedReview = review;
	}

	$scope.loadProducts();
	$scope.loadCategories();
	$scope.loadOccasions();
	$scope.loadLengths();
	$scope.loadTypes();
	$scope.loadColors();
	$scope.loadSizes();
	$scope.loadCareInstructions();
	$scope.loadReviews();

});
