'use strict';

peAdminApp
		.controller(
				'userAccountBlockedEmailCtrl',
				[
						'$scope',
						'$uibModalInstance',
						'data',
						'adminService',
						'$state',
						'EMAIL_FROM_ADDRESS',
						'toaster',
						'peDeactivateUserFcty',
						function($scope, $uibModalInstance, data, adminService, $state,
								EMAIL_FROM_ADDRESS, toaster, peDeactivateUserFcty) {

							$scope.emailId = data.email;
							$scope.userId = data.id;
							$scope.fullName = data.name;

							$scope.sendBlockNotify = function() {

								var query = {
									to : EMAIL_FROM_ADDRESS,
									from : EMAIL_FROM_ADDRESS,
									bcc : $scope.emailId,
									subject : '​Account Blocked',
									body : '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;"> <div style="height: 5px;background-color: #ff8b7f;"></div><div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div> <div style="clear:both"></div><div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;"> <p style="margin-top: 0;"> <b> Dear '
											+ $scope.fullName
											+ ', </b> </p><p>We regret to inform you that your PromElle user account has been blocked for the following reasons:</p><p style="white-space: pre-wrap;">'
											+ $scope.blockedReason
											+ '</p><p>Please fix the issues above in order to have your account restored. If you have any questions, please write to us at <a href="mailto:contact@promelle.com">contact@promelle.com</a></p><p style="color:#000;line-height: 24px;font-size:15px;margin-bottom: 0px;">Regards,<br/>Team PromElle </p></div></div>'
								};

								peDeactivateUserFcty.update({
									id : $scope.userId
								}, function(response) {
									if (response) {
										adminService.emailContact(query).then(
												function(res) {
													if (res) {
														$scope.close();
														toaster.pop({
															type : 'success',
															body : 'Account Blocked successfully!'
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