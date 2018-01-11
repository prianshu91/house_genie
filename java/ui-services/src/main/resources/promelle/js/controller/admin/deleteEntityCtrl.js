/**
 * 
 */
peAdminApp
		.controller(
				'deleteEntityCtrl',
				[
						'$scope',
						'$uibModalInstance',
						'data',
						'$log',
						'$state',
						'header',
						'peReviewDeleteFcty',
						'peActivateUserFcty',
						'peDressDeleteFcty',
						'peActivateProductFcty',
						'toaster',
						'$uibModal',
						'adminService',
						function($scope, $uibModalInstance, data, $log, $state, header,
								peReviewDeleteFcty, peActivateUserFcty, peDressDeleteFcty,
								peActivateProductFcty, toaster, $uibModal) {
							'use strict';

							var init = function() {

								$scope.header = header;
								if ($scope.header == 'Approve Listing') {
									$scope.id = data;
								} else {
									$scope.id = data.id;
								}

								if ($scope.header == 'Delete User') {
									$scope.warning = 'Are you sure that you want to delete: ';
									$scope.userName = data.name;
									$scope.action = 'Confirm';
								} else if ($scope.header == 'Delete Review') {
									$scope.warning = 'Are you sure that you want to delete: ';
									$scope.userName = 'review by ' + data.userName;
									$scope.action = 'Delete';
								} else if ($scope.header == 'Block User') {
									$scope.warning = 'Are you sure that you want to block: ';
									$scope.userName = data.name;
									$scope.action = 'Confirm';
								} else if ($scope.header == 'Unblock User') {
									$scope.warning = 'Are you sure that you want to unblock: ';
									$scope.userName = data.name;
									$scope.action = 'Unblock';
								} else if ($scope.header == 'Approve Listing') {
									$scope.warning = 'Are you sure you would like to Approve this Listing?';
									$scope.userName = '';
									$scope.action = 'Approve';
								}
							}

							init();

							$scope.close = function() {
								$uibModalInstance.close();
							};

							$scope.applyAction = function() {

								var id = {
									id : $scope.id
								};

								if ($scope.header == 'Delete User') {
									$scope.close();
									openDeleteUserMail();
								} else if ($scope.header == 'Delete Review') {
									peReviewDeleteFcty.deleteById(id, function(response) {
										toaster.pop({
											type : 'success',
											body : 'Review Deleted Successfully!'
										});
										$uibModalInstance.close(response);
									}, function(errorInfo) {
										console.log("Error in Review delete");
									});
								} else if ($scope.header == "Block User") {
									$scope.close();
									openBlockUserMail();
								} else if ($scope.header == "Unblock User") {
									peActivateUserFcty.update(id, function(response) {
										toaster.pop({
											type : 'success',
											body : 'User Unblocked successfully!'
										});
										var unblockUserQuery = {
												to : 'contact@promelle.com',
												from : 'contact@promelle.com',
												bcc : data.email,
												subject: 'Account Unblocked',
												body: '<div class="mail" style="font-size:14px;color #0a0a0a;font-family: Arial;max-width: 600px;float: left;">'
														  + '<div style="height: 5px;background-color: #ff8b7f;"></div>'
														  + '<div class="img" style="float: right;"><img src="http://nebula.wsimg.com/da4075db270e4bbdc83824e9e43abba4?AccessKeyId=E8004760A1EA743B3DBC&amp;disposition=0&amp;alloworigin=1" style="vertical-align:middle;width:113px;height:113px;"></div>'
														  + '<div style="clear:both"></div>'
														  + '<div style="padding: 15px;border: 2px solid #ddd;margin-top: 10px;">'
														      + '<p style="line-height: 30px;">Dear <b>'+ $scope.userName +'</b>!</p><br>'
														      + '<p>We are pleased to inform you that your PromElle user account has been unblocked. You may now continue renting and lending on PromElle.</p><br>'
														      + '<p style="color:#000;line-height: 24px;font-size:15px;margin-bottom: 0px;">'
														          + 'Regards,<br>Team PromElle'
														      + '</p>'
														   + '</div>'
														+ '</div>'
										}
										adminService.emailContact(unblockUserQuery).then(function (res) {
											//do nothing with the response
										});
										$uibModalInstance.close(response);
									}, function(errorInfo) {
										console.log("Error in User unblock");
									});
								} else if ($scope.header == 'Approve Listing') {
									peActivateProductFcty.update(id, function(response) {
										toaster.pop({
											type : 'success',
											body : 'Approved Listing Successfully!'
										});
										$uibModalInstance.close(response);
									}, function(errorInfo) {
										console.log(errorInfo);
									});
								}
							}

							var openBlockUserMail = function() {
								setTimeout(
										function() {
											var modalInstance = $uibModal
													.open({
														templateUrl : 'views/admin/userDetails/modal/userAccountBlockedEmail.html',
														controller : 'userAccountBlockedEmailCtrl',
														$scope : $scope,
														resolve : {
															data : function() {
																return data;
															}
														}
													});
										}, 100);
							};

							var openDeleteUserMail = function() {
								setTimeout(
										function() {
											var modalInstance = $uibModal
													.open({
														templateUrl : 'views/admin/userDetails/modal/userAccountDeletedEmail.html',
														controller : 'userAccountDeletedEmailCtrl',
														$scope : $scope,
														resolve : {
															data : function() {
																return data;
															}
														}
													});
										}, 100);
							};

						} ]);