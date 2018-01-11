'use strict';

peAdminApp
		.controller(
				'userAccountDeletedEmailCtrl',
				[
						'$scope',
						'$uibModalInstance',
						'data',
						'adminService',
						'$state',
						'EMAIL_FROM_ADDRESS',
						'toaster',
						'peUserDeleteFcty',
						function($scope, $uibModalInstance, data, adminService, $state,
								EMAIL_FROM_ADDRESS, toaster, peUserDeleteFcty) {

							$scope.emailId = data.email;
							$scope.userId = data.id;
							$scope.fullName = data.name;

							$scope.sendDeleteNotify = function() {

								var query = {
									to : EMAIL_FROM_ADDRESS,
									from : EMAIL_FROM_ADDRESS,
									bcc : $scope.emailId, 
									subject : '​PromElle Account Deleted',
									body : '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;"> <div style="height: 5px;background-color: #ff8b7f;"></div><div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div> <div style="clear:both"></div>'
												+ '<div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;">' 
													+ '<p style="margin-top: 0;"> <b> Dear '+$scope.fullName+', </b> </p>'
													+ '<p>Per your request, your PromElle account has been deleted. We are sorry to see you go. If you have feedback or any questions, please write to us at <a href="mailto:contact@promelle.com">contact@promelle.com</a>.</p>'
												+'</div>'
											+'</div>'
								};

								peUserDeleteFcty.deleteById({
									id : $scope.userId
								}, function(response) {
									if (response) {
										adminService.emailContact(query).then(
												function(res) {
													if (res) {
														$scope.close();
														toaster.pop({
															type : 'success',
															body : 'Account Deleted successfully!'
														});
														$state.reload();
													}
												},
												function(error) {
													console.log("Error in Sending Email. Error Info :"
															+ error);
												});
									}
								}, function(error) {
									console.log("Error in Sending Email. Error Info :" + error);
								});
							};

							$scope.close = function() {
								$uibModalInstance.close();
							};
						} ]);