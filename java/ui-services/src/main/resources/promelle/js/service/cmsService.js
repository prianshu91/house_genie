'use strict';

peAdminApp.service('cmsService', [
		'$http',
		'$q',
		'PROMELLE_USER_URL',
		'$timeout',
		function($http, $q, PROMELLE_USER_URL, $timeout) {

			this.getFaq = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(PROMELLE_USER_URL + '/product/getDocumentContent', query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};
			this.getTnc = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(PROMELLE_USER_URL + '/product/getDocumentContent', query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};
			this.getPolicy = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(PROMELLE_USER_URL + '/product/getDocumentContent', query, {
					ignoreLoadingBar : true
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};


			this.saveFaq = function(requestData) {
				var defer = $q.defer();
				$http.post(PROMELLE_USER_URL + '/product/saveDocument', requestData, {
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};

			this.saveTnc = function(requestData) {
				var defer = $q.defer();
				$http.post(PROMELLE_USER_URL + '/product/saveDocument', requestData, {
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};
			this.publishPolicy = function(requestData) {
				var defer = $q.defer();
				$http.post(PROMELLE_USER_URL + '/product/saveDocument', requestData, {
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					defer.resolve(data);
				}).error(function(err) {
					defer.reject(err);
				});

				return defer.promise;
			};


		} ]);