'use strict';

peAdminApp.controller('listingDetailsCtrl', [ '$scope', 'adminService', 'data',
		'listingId', '$uibModalInstance',
		function($scope, adminService, data, listingId, $uibModalInstance) {

			$scope.close = function() {
				$uibModalInstance.close();
			};

		} ]);