'use strict';

peAdminApp.service('adminService', [
		'$http',
		'$q',
		'PROMELLE_USER_URL',
		'$timeout',
		function($http, $q, PROMELLE_USER_URL, $timeout) {

			this.getUserList = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(PROMELLE_USER_URL + '/user/list', query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.getProductListingData = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(PROMELLE_USER_URL + '/product/list', query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.emailContact = function(requestData) {
				var defer = $q.defer(), query = JSON.stringify(requestData);
				$http.post(PROMELLE_USER_URL + '/communication/email', query, {
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

			this.broadcastEmail = function(requestData) {
				var defer = $q.defer();
				$http.post(PROMELLE_USER_URL + '/communication/email/broadcast', requestData, {
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



			this.getLendingOrdersList = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(PROMELLE_USER_URL + '/product/shipment/history', query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.getLendingOrdersData = function(orderId, includeShipment,
					includeEarning, timestamp, version) {
				var defer = $q.defer(), query = {
					params : {
						includeShipments : includeShipment,
						includeEarnings : includeEarning,
						timestamp : timestamp ? timestamp : null,
						version : version ? version : null
					}
				};
				$http.get(PROMELLE_USER_URL + '/product/order/' + orderId, query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.getRentalOrdersList = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(PROMELLE_USER_URL + '/product/order/history', query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.getRentalOrderData = function(orderId, includeShipment,
					includeEarning, timestamp, version) {
				var defer = $q.defer(), query = {
					params : {
						includeShipments : includeShipment,
						includeEarnings : includeEarning,
						timestamp : timestamp ? timestamp : null,
						version : version ? version : null
					}
				};
				$http.get(PROMELLE_USER_URL + '/product/order/' + orderId, query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.getPodsList = function(queryParams) {
				var defer = $q.defer(),
					query = {
						params : queryParams
					};
				$http.get(PROMELLE_USER_URL + '/user/listActiveSchools', query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.getPodDetail = function(queryParams, id) {
				var defer = $q.defer(),
					query = {
						params : queryParams
					};
				$http.get(PROMELLE_USER_URL + '/user/school/' + id, query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.uploadFileToUrl = function(file, id) {
				var defer = $q.defer();
				var fd = new FormData();
				fd.append('file', file);
				$http.post(PROMELLE_USER_URL + '/user/uploadPodLogo?podId='+id, fd, {
					transformRequest : angular.identity,
					transformResponse : angular.identity,
					headers : {
						'Content-Type' : undefined
					}
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});
				return defer.promise;
			};

		} ]);

peAdminApp.factory('peUserByIdFcty', [ '$http', '$resource', '$q', '$log',
		function($http, $resource, $q, $log) {

			return $resource('/service/user/:id', {
				id : '@id'
			}, {
				findById : {
					method : 'GET',
					ignoreLoadingBar : true
				}
			});
		} ]);

peAdminApp.factory('peUserDeleteFcty', [ '$http', '$resource', '$q', '$log',
		function($http, $resource, $q, $log) {

			return $resource('/service/user/:id', {
				id : '@id'
			}, {

				deleteById : {
					method : 'DELETE',
					ignoreLoadingBar : true
				}
			});
		} ]);

peAdminApp.factory('peDeactivateUserFcty', [ '$http', '$resource', '$q',
		'$log', function($http, $resource, $q, $log) {

			return $resource('/service/user/disable/:id', {
				id : '@id'
			}, {
				update : {
					method : 'PUT'
				}
			});
		} ]);

peAdminApp.factory('peActivateUserFcty', [ '$http', '$resource', '$q', '$log',
		function($http, $resource, $q, $log) {

			return $resource('/service/user/enable/:id', {
				id : '@id'
			}, {
				update : {
					method : 'PUT'
				}
			});
		} ]);
