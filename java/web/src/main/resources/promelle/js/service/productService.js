'use strict';

promelleApp.service('productService', [ 
	'$http', 
	'$q', 
	'PROMELLE_SERVICE_URL',
	'$timeout', 
	function($http, $q, PROMELLE_SERVICE_URL, $timeout) {

		this.getProductList = function(queryParams) {
			var defer = $q.defer(), 
				query = {
					params : queryParams
				};

			$http.get(PROMELLE_SERVICE_URL + '/product/list', query).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.getCategoriesList = function(queryParams) {
			var defer = $q.defer(), 
				query = {
					params : queryParams
				};

			$http.get(PROMELLE_SERVICE_URL + '/product/categories2', query).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.getUserById = function(id) {
			var defer = $q.defer();

			$http.get(PROMELLE_SERVICE_URL + '/user/' + id).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.getProductById = function(id) {
			var defer = $q.defer();

			$http.get(PROMELLE_SERVICE_URL + '/product/' + id).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.getReviewDetails = function(queryParams) {
			var defer = $q.defer(), 
				query = {
					params : queryParams
				};
			
			$http.get(PROMELLE_SERVICE_URL + '/review/list', query, {
				ignoreLoadingBar : true
			}).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});

			return defer.promise;
		};

		this.getDressLength = function() {
			var defer = $q.defer(), 
				query = {
					params : {
						limit : '-1',
						offset : '-1'
					}
				};

			$http.get(PROMELLE_SERVICE_URL + '/product/lengths', query).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.getDressTypes = function() {
			var defer = $q.defer(), 
				query = {
					params : {
						limit : '-1',
						offset : '-1'
					}
				};

			$http.get(PROMELLE_SERVICE_URL + '/product/types', query).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.getDressOccasions = function() {
			var defer = $q.defer(), 
				query = {
					params : {
						limit : '-1',
						offset : '-1'
					}
				};

			$http.get(PROMELLE_SERVICE_URL + '/product/occasions', query).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.getDressColors = function() {
			var defer = $q.defer(), 
				query = {
					params : {
						limit : '-1',
						offset : '-1'
					}
				};

			$http.get(PROMELLE_SERVICE_URL + '/product/colors', query).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.getDressSizes = function() {
			var defer = $q.defer(), 
				query = {
					params : {
						limit : '-1',
						offset : '-1'
					}
				};

			$http.get(PROMELLE_SERVICE_URL + '/product/sizes', query).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});
			return defer.promise;
		};

		this.emailContact = function(requestData) {
			var defer = $q.defer(), 
				query = JSON.stringify(requestData);
			
			$http.post(PROMELLE_SERVICE_URL + '/communication/email/admin', query, {
				headers : {
					'Content-Type' : 'application/json'
				}
			}).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});

			return defer.promise;
		};

		this.getDocumentContent = function(queryParams) {
			var defer = $q.defer(), 
				query = {
					params : queryParams
				};
			
			$http.get(PROMELLE_SERVICE_URL + '/product/getDocumentContent', query).success(function(data) {
				defer.resolve(data);
			}).error(function(err) {
				defer.reject(err);
			});

			return defer.promise;
		}
	}
]);