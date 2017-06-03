

var app = angular.module("app");

app.controller("EmpCtrl",['$rootScope','$scope', '$http', '$window','NgTableParams','$state', 
    function($rootScope,$scope, $http, $window,NgTableParams,$state) {


	 $scope.sortType     = 'name'; // set the default sort type
  	 $scope.sortReverse  = false;  // set the default sort order
     $scope.searchFish   = '';     // set the default search/filter term
  
       var tableData = [];
    $scope.init = function() {
    	var users = {};
    	$http.get("/viewAll").then(function(response){
            $scope.users = response.data;
         });
        $scope.usersTable = new NgTableParams({ }, { dataset: $scope.users });
    }

    $scope.open = function(userId){
    	var url = "/view/" + userId;
    	$scope.userDetails  = {};
    	$http.get(url).then(function(response){
    		$rootScope.userDetails = response.data;
    	});
        
        $state.go("Admin.Emp.Profile");
    };

}]);