'use strict';
promelleApp.controller('FAQCtrl', [ 
	'$scope', 
	'productService',
	'$sce',
	function($scope, productService, $sce) {
		$scope.isLoading = true;
		var query = {
			fileName: 'faq'
		};
		productService.getDocumentContent(query).then(function(response) {
			$scope.isLoading = false;
			$scope.faqContent = $sce.trustAsHtml(response.data.content);
		}, function(errorInfo) {
			console.log(errorInfo);
		});
	}
]);