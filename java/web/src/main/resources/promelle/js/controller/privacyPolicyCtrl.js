'use strict';
promelleApp.controller('privacyPolicyCtrl', [ 
	'$scope', 
	'productService',
	'$sce',
	function($scope, productService, $sce) {
		$scope.isLoading = true;
		var query = {
			fileName: 'policy'
		};
		productService.getDocumentContent(query).then(function(response) {
			$scope.isLoading = false;
			$scope.privacyContent = $sce.trustAsHtml(response.data.content);
		}, function(errorInfo) {
			console.log(errorInfo);
		});
	}
]);