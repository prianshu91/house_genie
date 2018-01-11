'use strict';

peAdminApp.controller('emailUserCtrl', [ '$scope', 'adminService', 'data', '$uibModalInstance', 'EMAIL_FROM_ADDRESS', 'toaster', function($scope, adminService, data, $uibModalInstance, EMAIL_FROM_ADDRESS, toaster) {

			$scope.userData = data;
			$scope.emailId = $scope.userData.email;

			$scope.sendEmail = function(){
				var query = {
					to: $scope.emailId,
					from: EMAIL_FROM_ADDRESS,
					body: '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;"> <div style="height: 5px;background-color: #ff8b7f;"></div><div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div> <div style="clear:both"></div><div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;"> <p style="margin-top: 0;"> <b> Dear '+ $scope.userData.name +', </b> </p><p style="line-height: 24px; white-space: pre-line;">'+$scope.messageBody+'</p><p>Please contact us if you have any questions.</p><p style="color:#000;line-height: 24px;font-size:15px;margin-bottom: 0px;"> Thank you, <br/> Team PromElle </p></div></div>',
					subject: $scope.subject
				};

				adminService.emailContact(query).then(function(res){
					if(res){
						toaster.pop({
			                type: 'success',
				            body: 'Email Sent Successfully!'
			            });
						$scope.close();
					}
				});
			};

			$scope.close = function() {
				$uibModalInstance.close();
			};

		} ]);