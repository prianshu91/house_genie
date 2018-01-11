peAdminApp.factory('pePaymentToLenderUserList', [ '$http', '$resource', '$q',
		'$log', function($http, $resource, $q, $log) {

			return $resource('/service/product/earnings', {
				redeemStatus : '@redeemStatus'
			}, {
				method : 'GET',
				ignoreLoadingBar : true,
				params : {
					offset : '@offset',
					limit : '@limit',
					sort : '@sort'
				}
			});
		} ]);

peAdminApp.factory('peUpdateLenderPaymentFcty', [ '$http', '$resource', '$q',
		'$log', function($http, $resource, $q, $log) {

			return $resource('/service/product/earning/:id/redeemStatus/:status', {
				id : '@id',
				status : '@status'
			}, {
				update : {
					method : 'PUT'
				}
			});

		} ]);

peAdminApp.service('paymentService', [ '$http', '$q', 'PROMELLE_USER_URL',
		'$timeout', function($http, $q, PROMELLE_USER_URL, $timeout) {

			this.uploadFileToUrl = function(file) {
				var defer = $q.defer();
				var fd = new FormData();
				fd.append('file', file);
				$http.post(PROMELLE_USER_URL + '/product/uploadChequeImage', fd, {
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
