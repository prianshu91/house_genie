'use strict';

peAdminApp.service('productService', ['$http', '$q', 'PROMELLE_USER_URL',
	'$timeout', function ($http, $q, PROMELLE_USER_URL, $timeout) {

		this.notifyFieldsData = [];
		var valIndex;

		this.setNotifyFields = function (field, type) {
			if (type === 'text') {
				if (this.notifyFieldsData.length) {
					var found = this.notifyFieldsData.some(function (el, index) {
						if (el.value === field) {
							valIndex = index;
							return true;
						}
					});
					if (!found) {
						this.notifyFieldsData.push({
							value: field,
							type: "text"
						});
					} else {
						this.notifyFieldsData.splice(valIndex, 1);
					}
				} else {
					this.notifyFieldsData.push({
						value: field,
						type: "text"
					});
				}
			} else {
				if (this.notifyFieldsData.length) {
					var found = this.notifyFieldsData.some(function (el, index) {
						if (el.value === field.url) {
							valIndex = index;
							return true;
						}
					});
					if (!found) {
						this.notifyFieldsData.push({
							value: field.url,
							caption: field.caption,
							type: "image"
						});
					} else {
						this.notifyFieldsData.splice(valIndex, 1);
					}
				} else {
					this.notifyFieldsData.push({
						value: field.url,
						caption: field.caption,
						type: "image"
					});
				}
			}
		};

		this.getNotifyFields = function () {
			return this.notifyFieldsData;
		};

		this.resetNotifyFields = function () {
			this.notifyFieldsData = [];
		}

		this.getProductList = function (queryParams) {
			var defer = $q.defer(), query = {
				params: queryParams
			};
			$http.get(PROMELLE_USER_URL + '/product/list', query, {
				ignoreLoadingBar: false
			}).success(function (data) {
				defer.resolve(data);
			}).error(function (err) {
				defer.reject(err);
			});

			return defer.promise;
		};

		this.getReviewDetails = function (queryParams) {
			var defer = $q.defer(), query = {
				params: queryParams 
			};
			$http.get(PROMELLE_USER_URL + '/review/list', query, {
				ignoreLoadingBar: true
			}).success(function (data) {
				defer.resolve(data);
			}).error(function (err) {
				defer.reject(err);
			});

			return defer.promise;
		};

		this.activateProduct = function (queryParams) {
			var defer = $q.defer(), query = {
				params: queryParams
			};
			$http.put(PROMELLE_USER_URL + '/product/', query, {
				ignoreLoadingBar: true
			}).success(function (data) {
				defer.resolve(data);
			}).error(function (err) {
				defer.reject(err);
			});

			return defer.promise;
		};

	}]);

peAdminApp.factory('peActivateProductFcty', ['$http', '$resource', '$q',
	'$log', function ($http, $resource, $q, $log) {

		return $resource('/service/product/:id/activate', {
			id: '@id'
		}, {
				update: {
					method: 'PUT',
					ignoreLoadingBar: true
				}
			});

	}]);

peAdminApp.factory('peProductByIdFcty', ['$http', '$resource', '$q', '$log',
	function ($http, $resource, $q, $log) {

		return $resource('/service/product/:id', {
			id: '@id'
		}, {
				findById: {
					method: 'GET',
					ignoreLoadingBar: true
				}
			});
	}]);

peAdminApp.factory('peReviewDeleteFcty', ['$http', '$resource', '$q', '$log',
	function ($http, $resource, $q, $log) {

		return $resource('/service/review/:id', {
			id: '@id'
		}, {

				deleteById: {
					method: 'DELETE',
					ignoreLoadingBar: true
				}
			});
	}]);

peAdminApp.factory('peUpdateOrderStatusFcty', ['$http', '$resource', '$q',
	'$log', function ($http, $resource, $q, $log) {

		return $resource('/service/product/order/:orderId/status/:orderStatus', {
			orderId: '@orderId',
			orderStatus: '@orderStatus'
		}, {
				update: {
					method: 'PUT'
				}
			});

	}]);

peAdminApp.factory('peUpdateShipmentStatusFcty', [
	'$http',
	'$resource',
	'$q',
	'$log',
	function ($http, $resource, $q, $log) {

		return $resource(
			'/service/product/shipment/:shipmentId/status/:shipmentStatus', {
				shipmentId: '@shipmentId',
				shipmentStatus: '@shipmentStatus'
			}, {
				update: {
					method: 'PUT'
				}
			});

	}]);

peAdminApp.factory('peDressDeleteFcty', ['$http', '$resource', '$q', '$log',
	function ($http, $resource, $q, $log) {

		return $resource('/service/product/:id', {
			id: '@id'
		}, {
				deleteById: {
					method: 'DELETE'
				}
			});
	}]);

peAdminApp.factory('peRentedProductFcty', ['$http', '$resource', '$q', '$log',
	function ($http, $resource, $q, $log) {

		return $resource('/service/product/rentedDresses', {}, {
			method: 'GET',
			ignoreLoadingBar: true
		});
	}]);

peAdminApp.factory('peDressRejectFcty', [
	'$http',
	'$resource',
	'$q',
	'$log',
	function ($http, $resource, $q, $log) {

		return $resource(
			'/service/product/:id/reject', {
				id: '@id'
			}, {
				update: {
					method: 'PUT'
				}
			});

	}]);