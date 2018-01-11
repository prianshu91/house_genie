'use strict';
promelleApp.controller('contactCtrl', [ 
	'$scope', 
	'productService',
	'$state',
	'PROMELLE_EMAIL_ADDRESS',
	'$timeout',
	function($scope, productService, $state, PROMELLE_EMAIL_ADDRESS, $timeout) {
		$scope.user = {
			reciveUpdates: false
		};

		$scope.showSuccess = false;

	    $scope.sendEmail = function(){
	    	var query = {
				from : $scope.user.email,
				subject : $scope.user.subject,
				body : '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;"> <div style="height: 5px;background-color: #ff8b7f;"></div><div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div> <div style="clear:both"></div><div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;">'+
						+'<table style="border-collapse: collapse;width: 100%;"><tr><th style="border: 1px solid #dddddd;text-align: left;padding: 8px;>Full Name:<td style="border: 1px solid #dddddd;text-align: left;padding: 8px;>'+$scope.user.name+'<tr><th style="border: 1px solid #dddddd;text-align: left;padding: 8px;>Email:<td style="border: 1px solid #dddddd;text-align: left;padding: 8px;>'+$scope.user.email+'<tr><th style="border: 1px solid #dddddd;text-align: left;padding: 8px;>Comments:<td style="border: 1px solid #dddddd;text-align: left;padding: 8px;>'+$scope.user.message+'</table></div></div>'
			};
	    	productService.emailContact(query).then(function(res) {
				if (res) {
					$scope.showSuccess = true;
				}
			}, function(error) {
				console.log("Error in Sending Email. Error Info :" + error);
			});
	    }
	}
]);