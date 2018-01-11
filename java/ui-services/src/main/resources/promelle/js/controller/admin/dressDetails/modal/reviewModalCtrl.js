'use strict';

peAdminApp.controller('reviewModalCtrl', [ '$scope', 'productService',
		'$stateParams', '$uibModalInstance', 'data', function($scope, productService, $stateParams, $uibModalInstance, data) {
			
			$scope.reviewData = data;
			
			$scope.close = function() {
				$uibModalInstance.close();
			};
		} ]);