

var myApp = angular.module("app",['ngTable','ui.router','ngRoute',"ngResource"]);

myApp.config(['$stateProvider','$urlRouterProvider',function($stateProvider,$urlRouterProvider){

 
   
	$stateProvider.state("Login",{
		url : '/login',
		templateUrl : "views/Login.html"
	}).
	state("Register",{
		url : "/register",
		templateUrl : "views/Register.html"
	}).
	state("Admin",{
		url : "/Admin",
		templateUrl : "views/Admin/Admin.html",
		controller  : "AdminCtrl"
	})
	.state("Admin.Emp",{
		parent:'Admin',
		url : "/Employee",
		templateUrl : "views/Admin/Employee.html",
		controller  : "EmpCtrl"
	}).
	state("Admin.Emp.Profile",{
		parent:'Admin',
		url : "/Profile",
		templateUrl : "views/Admin/Profile.html",
		controller  : "ProfileCtrl"
	});
		//For any unmatched url, redirect to /state1
	 $urlRouterProvider.otherwise('/maidApp');

}])