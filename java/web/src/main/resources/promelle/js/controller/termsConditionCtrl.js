'use strict';
promelleApp.controller('termsConditionCtrl', [ 
	'$scope', 
	'productService',
	'$sce',
	function($scope, productService, $sce) {
		$scope.isLoading = true;
		var query = {
			fileName: 'tnc'
		};
		productService.getDocumentContent(query).then(function(response) {
			$scope.isLoading = false;
			$scope.termsConditionContent = $sce.trustAsHtml(response.data.content);
		}, function(errorInfo) {
			console.log(errorInfo);
		});
	}
]);