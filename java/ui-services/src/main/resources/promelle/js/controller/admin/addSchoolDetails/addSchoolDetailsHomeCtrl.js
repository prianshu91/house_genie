'use strict';

peAdminApp.controller('addSchoolDetailsHomeCtrl', [ '$scope', '$stateParams', 'toaster',
		function($scope, $stateParams, toaster) {

			$scope.resetPage = function() {
				$scope.school = {};
			};

			$scope.saveSchool = function() {
				// api call for school save
				toaster.pop({
	                type: 'success',
	                title: 'Add school',
	                body: 'School added Successfully!'
	            });
				$scope.school = {};

			};

		} ]);