'use strict';

peAdminApp.controller('promelleSettingsHomeCtrl', [ '$scope', '$stateParams',
		'adminService', '$uibModal', '$sce', '$timeout',
		function($scope, $stateParams, adminService, $uibModal, $sce, $timeout) {
			$scope.tinyMceOptions = {
                setup: function(editor) {
                    $timeout(function () {
                        editor.focus();
                    }, 200)
                },
                statusbar: false,
                menubar: true,
                height: 300,
                resize: false
                //toolbar: 'formatselect | bold italic underline | bullist numlist | undo redo '
            };

            $scope.updateHtml = function() {
		    	$scope.tinymceHtml = $sce.trustAsHtml($scope.tinymce);
		    };
		} ]);