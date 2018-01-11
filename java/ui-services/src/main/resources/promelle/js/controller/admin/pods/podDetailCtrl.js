'use strict';

peAdminApp.controller('podDetailCtrl', [
'$scope',
'adminService',
'$stateParams',
'$uibModal',
'$state',
'CURRENT_HOST',
'CURRENT_VERSION',
'$rootScope',
function($scope, adminService, $stateParams, $uibModal, $state, CURRENT_HOST, CURRENT_VERSION, $rootScope) {
    $scope.podId = $stateParams.podId
    $scope.disabledSave = true;
    $scope.noData = false;

    $scope.podMembersListing = {
		enableColumnResize : true,
		enableHorizontalScrollbar : 0,
		enableVerticalScrollbar : 1,
		enableSorting : true,
		enableFiltering : false,
		paginationPageSizes : [ 30, 60 ],
		paginationPageSize : 30,
		columnDefs : [{
            field : 'name',
            displayName : 'Name',
            enableColumnMenu : false
        }, {
            field : 'username',
            displayName : 'Username',
            enableColumnMenu : false,
            cellTemplate: '<div class="pT5">'
            + '<a href="" ng-click="grid.appScope.openUserDetails(row.entity)">{{row.entity.username}}</a>'
            + '</div>'
        }, {
            field: 'createdOn',
            displayName: 'Date Of Joining',
            cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"',
            enableColumnMenu: false
        }]
	};


    $scope.getPodDetail = function(){
        $scope.noData = false;
        $scope.isDev = getHost(CURRENT_HOST);
		var query = {
			schoolPod : true,
			limit : "-1",
			offset : "-1",
			timestamp : $scope.isDev ? new Date().getTime() : null,
			version : !$scope.isDev ? CURRENT_VERSION : null
		};

		adminService.getPodDetail(query, $scope.podId).then(function(response) {
			$scope.podData = response.data;
            $scope.podMembersListing.data = response.data.podMembers;
            if ($scope.podMembersListing.totalItems < 1) {
				$scope.noData = true;
			}
		}, function(error){
			console.log("error while getting pod detail", error);
		});
    }

    $scope.getPodDetail();

    $scope.openUserDetails = function(userObj){
        $state.go('userDetail', {
            userId: userObj.id
        });
    };

    $scope.uploadLogo = function() {
        var modalInstance = $uibModal.open({
            templateUrl: 'views/admin/pods/modal/uploadPodLogoModal.html',
            controller: 'uploadPodLogoModalCtrl',
            $scope: $scope,
            resolve: {
                podId: function () {
                    return $scope.podId;
                }
            }
        });
        modalInstance.result.then(function (logoUrl) {
            if(logoUrl){
                $scope.podData.schoolPodLogo = logoUrl;
            }
        });
    };
} ]);