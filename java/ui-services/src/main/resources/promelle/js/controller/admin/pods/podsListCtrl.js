'use strict';

peAdminApp.controller('podsListCtrl', [
'$scope',
'adminService',
'$stateParams',
'$uibModal',
'$state',
'CURRENT_HOST',
'CURRENT_VERSION',
function($scope, adminService, $stateParams, $uibModal, $state, CURRENT_HOST, CURRENT_VERSION) {
	$scope.selectedPodStatus = 'all';
	$scope.currentOption = 'all';
	$scope.noData = false;

	$scope.podListings = {
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
			enableColumnMenu : false,
			cellTemplate : '<div class="pT5">'
					+ '<a href="" ng-click="grid.appScope.openListingDetails(row.entity.id)">{{row.entity.schoolPodName}}</a>'
					+ '</div>'
		},{
            field: 'createdOn',
            displayName: 'Created On',
            cellFilter: 'date:"\MMM dd, yyyy HH:mm:ss\"',
            enableColumnMenu: false
        }]
	};

	uiGridHeight();

	$scope.getPodsListByStatus = function(status){
		$scope.noData = false;
		$scope.isDev = getHost(CURRENT_HOST);
		var query = {
			status : status,
			limit : "-1",
			offset : "-1",
			timestamp : $scope.isDev ? new Date().getTime() : null,
			version : !$scope.isDev ? CURRENT_VERSION : null
		};

		adminService.getPodsList(query).then(function(response) {
			$scope.podListings.data = response.data;
			if ($scope.podListings.totalItems < 1) {
				$scope.noData = true;
			}
		}, function(error){
			console.log("error while getting pods list", error);
		});
	}

	$scope.getPodsListByStatus($scope.currentOption);
	

	$scope.podStatusOptions = [{
		id: '0',
		value: 'Inactive'
	}, {
		id: '1',
		value: 'Active'
	}, {
		id: 'all',
		value: 'All'
	}];

	$scope.changePodStatus = function (optionId) {
		$scope.currentOption = optionId;
		if (optionId === "1") {
			$scope.getPodsListByStatus('1');
		} else if (optionId === "0") {
			$scope.getPodsListByStatus('0');
		} else {
			$scope.getPodsListByStatus('all');
		}
	};

	$scope.openListingDetails = function(listingId) {
		$state.go('podDetail', {
			podId : listingId
		});
	}
} ]);