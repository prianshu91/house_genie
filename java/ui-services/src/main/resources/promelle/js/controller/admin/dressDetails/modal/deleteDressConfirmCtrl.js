'use strict';

peAdminApp.controller('deleteDressConfirmCtrl', [ 
	'$scope', 
	'$uibModalInstance', 
	'data', 
	'productId',
	'ownerId',
	'$uibModal',
	function($scope, $uibModalInstance, data, productId, ownerId, $uibModal) {
			
			$scope.data = data;

			$scope.confirmDelete = function(){
				$scope.close();
				setTimeout(function() {
					var modalInstance = $uibModal.open({
						templateUrl : 'views/admin/dressDetails/modal/deleteDressListingEmail.html',
						controller : 'deleteDressListingEmailCtrl',
						$scope : $scope,
						resolve : {
							data : function() {
								return data;
							},
							productId : function() {
								return productId;
							},
							ownerId : function() {
								return ownerId;
							}
						}
					});
				}, 100);
			};

			$scope.close = function() {
				$uibModalInstance.close();
			};
		} ]);
