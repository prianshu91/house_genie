'use strict';
promelleApp.controller('aboutUsCtrl', [ 
	'$scope', 
	'productService',
	'$sce',
	function($scope, productService, $sce) {
		// $scope.isLoading = true;
		// var query = {
		// 	fileName: 'aboutus'
		// };
		// productService.getDocumentContent(query).then(function(response) {
		// 	$scope.isLoading = false;
		// 	$scope.aboutUsContent = $sce.trustAsHtml(response.data.content);
		// }, function(errorInfo) {
		// 	console.log(errorInfo);
		// });
	}
]);