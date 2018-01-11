'use strict';

peAdminApp.controller('renterPaymentCtrl', [ '$scope', 'productService',
		'$stateParams', '$uibModalInstance', 'data',
		function($scope, productService, $stateParams, $uibModalInstance, data) {

			$scope.reviewData = data;

			$scope.close = function() {
				$uibModalInstance.close();
			};
		} ]);