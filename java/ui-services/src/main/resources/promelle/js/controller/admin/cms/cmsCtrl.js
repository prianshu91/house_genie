'use strict';

peAdminApp
	.controller(
	'cmsCtrl',
	[
		'$scope',
		'productService',
		'$state',
		'$uibModal',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		'cmsService',
		'$rootScope',
		function ($scope, productService, $state, $uibModal, CURRENT_HOST, CURRENT_VERSION, cmsService,$rootScope) {
			console.log('bahar karaya hai',cmsService);
			$scope.loadFaqSelected = true;
			$scope.htmlContent = '';
			$scope.htmlTab = 'faq';
			
			$scope.loadFaq = function() {
				$scope.htmlTab = 'faq';
				$scope.loadAboutUsSelected = false;
				$scope.loadFaqSelected = true;
				$scope.loadTncSelected = false;
				$scope.loadPolicySelected = false;
				var query = {
					fileName: 'faq'
				};
				cmsService.getFaq(query).then(
				function (res) {
					if (res) {
						$scope.htmlContent = (res.data && res.data.content) ? res.data.content : '';
						$scope.$$phase || $scope.$apply();
					}
				},
				function (error) {
					console.log("Error in saveFaq. Error Info :" + error);
				});
			}

			$scope.loadAboutUs = function() {
				$scope.htmlTab = 'aboutus';
				$scope.loadAboutUsSelected = true;
				$scope.loadFaqSelected = false;
				$scope.loadTncSelected = false;
				$scope.loadPolicySelected = false;
				var query = {
					fileName: 'aboutus'
				};
				cmsService.getFaq(query).then(
				function (res) {
					if (res) {
						$scope.htmlContent = (res.data && res.data.content) ? res.data.content : '';
						$scope.$$phase || $scope.$apply();
					}
				},
				function (error) {
					console.log("Error in saveFaq. Error Info :" + error);
				});
			}



			$scope.loadTnc = function() {
				$scope.htmlTab = 'tnc';
				$scope.loadAboutUsSelected = false;
				$scope.loadFaqSelected = false;
				$scope.loadTncSelected = true;
				$scope.loadPolicySelected = false;
				var query = {
					fileName: 'tnc'
				};
				cmsService.getTnc(query).then(
				function (res) {
					
					if (res) {
						$scope.htmlContent = (res.data && res.data.content) ? res.data.content : '';
						$scope.$$phase || $scope.$apply();
					}
				},
				function (error) {
					console.log("Error in loadTnc. Error Info :" + error);
				});
			}

			$scope.loadPolicy = function() {
				$scope.htmlTab = 'policy';
				$scope.loadAboutUsSelected = false;
				$scope.loadFaqSelected = false;
				$scope.loadTncSelected = false;
				$scope.loadPolicySelected = true;
				var query = {
					fileName: 'policy'
				};
				cmsService.getPolicy(query).then(
				function (res) {
					if (res) {
						$scope.htmlContent = (res.data && res.data.content) ? res.data.content : '';
						$scope.$$phase || $scope.$apply();
					}
				},
				function (error) {
					console.log("Error in loadPolicy. Error Info :" + error);
				});
			}


			
			$scope.loadFaq();

			$scope.publish = function (cmsForm) {
				console.log('tab', $scope.htmlTab);
				if($scope.htmlTab == 'faq')
					$scope.publishFaq(cmsForm);
				else if ($scope.htmlTab == 'tnc')
					$scope.publishTnc(cmsForm)
				else if ($scope.htmlTab == 'policy') 
					$scope.publishPolicy(cmsForm);
				else if ($scope.htmlTab == 'aboutus') 
					$scope.publishAboutUs(cmsForm);

			}

			$scope.saveDraft = function (cmsForm) {
				console.log('tab', $scope.htmlTab);
				if($scope.htmlTab == 'faq')
					$scope.draftFaq(cmsForm);
				else if ($scope.htmlTab == 'tnc')
					$scope.draftTnc(cmsForm)
				else if ($scope.htmlTab == 'policy') 
					$scope.draftPolicy(cmsForm);
			}



			$scope.publishFaq = function (cmsForm) {
				var query = {'content':$scope.htmlContent, 'name':'faq'};
				cmsService.saveFaq(query).then(
				function (res) {
					if (res) {
						console.log('publish faq', res);
						$scope.htmlContent = (res.data && res.data.content) ? res.data.content : '';
						$scope.$$phase || $scope.$apply();
						alert('FAQ published successfully.');
					}
				},
				function (error) {
					console.log("Error in saveFaq. Error Info :" + error);
				});
			}

			$scope.publishTnc = function (cmsForm) {
				var query = {'content':$scope.htmlContent, 'name':'tnc'};
				cmsService.saveTnc(query).then(
				function (res) {
					if (res) {
						console.log('publish faq', res);
						$scope.htmlContent = (res.data && res.data.content) ? res.data.content : '';
						$scope.$$phase || $scope.$apply();
						alert('Terms & Condition published successfully.');
					}
				},
				function (error) {
					console.log("Error in saveTnc. Error Info :" + error);
				});
			}
			
			$rootScope.tinymceOptions = {
		        plugins: ["advlist autolink lists link image charmap print preview hr anchor pagebreak",
		                  "searchreplace wordcount visualblocks visualchars code fullscreen textcolor",
		                  "insertdatetime template paste nonbreaking save table contextmenu directionality"],
		        toolbar1: "undo | redo | bold italic | alignleft | aligncenter | alignright | alignjustify | outdent | indent | link | forecolor | code | table",
		        toolbar2: " fontselect | fontsizeselect | emoticons",
		        auto_focus: "bodyTinymce",                    
		        menubar: false,
		        resize:false,
		       templates: [],
		        height : 350,
		        extended_valid_elements:"div[id|style|class|title],i[id|style|class]",
		        forced_root_block : "div",
		       force_br_newlines : true,
		       force_p_newlines : true,
		       formats : {italic : {inline : 'span', styles : {fontStyle : 'italic'}}}
		    };

			$scope.publishPolicy = function (cmsForm) {
				var query = {'content':$scope.htmlContent, 'name':'policy'};
				cmsService.saveTnc(query).then(
				function (res) {
					if (res) {
						console.log('Publish Policy', res);
						$scope.htmlContent = (res.data && res.data.content) ? res.data.content : '';
						$scope.$$phase || $scope.$apply();
						alert('Policy published successfully.');
					}
				},
				function (error) {
					console.log("Error in publish Policy. Error Info :" + error);
				});
			}
			$scope.publishAboutUs = function (cmsForm) {
				var query = {'content':$scope.htmlContent, 'name':'aboutus'};
				cmsService.saveTnc(query).then(
				function (res) {
					if (res) {
						console.log('About Us', res);
						$scope.htmlContent = (res.data && res.data.content) ? res.data.content : '';
						$scope.$$phase || $scope.$apply();
						alert('About Us published successfully.');
					}
				},
				function (error) {
					console.log("Error in publish Policy. Error Info :" + error);
				});
			}


			$scope.draftFaq = function (cmsForm) {
				var query = 'content='+encodeURI($scope.htmlContent)+'&name=faq_draft';
				cmsService.saveFaq(query).then(
				function (res) {
					if (res) {
						console.log('draft faq', res);
						alert('FAQ draft saved successfully.');
					}
				},
				function (error) {
					console.log("Error in saveFaq. Error Info :"
						+ error);
				});
			}

			$scope.draftTnc = function (cmsForm) {
				var query = 'content='+encodeURI($scope.htmlContent)+'&name=tnc_draft';
				cmsService.saveTnc(query).then(
				function (res) {
					if (res) {
						console.log('draft faq', res);
						alert('Terms & Condition draft saved successfully.');
					}
				},
				function (error) {
					console.log("Error in saveTnc. Error Info :" + error);
				});
			}

			$scope.draftPolicy = function (cmsForm) {
				var query = 'content='+encodeURI($scope.htmlContent)+'&name=policy_draft';
				cmsService.saveTnc(query).then(
				function (res) {
					if (res) {
						console.log('draft Policy', res);
						alert('Policy draft saved successfully.');
					}
				},
				function (error) {
					console.log("Error in publish Policy. Error Info :" + error);
				});
			}



		}]);