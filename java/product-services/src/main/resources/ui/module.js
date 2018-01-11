var app = angular.module('manageApp', [ 'ngRoute', 'ui.router', 'LocalStorageModule', 'ngSanitize', 'frapontillo.bootstrap-switch' ]);

app.config(function myAppConfig($stateProvider, $urlRouterProvider) {

	$stateProvider.state('dashboard', {
		url : "/dashboard",
		pageTitle : 'PromElle Product Dashboard',
		views : {
			'main@' : {
				templateUrl : 'views/dashboard.html',
				controller : 'DashboardCtrl'
			}
		}
	})

	$urlRouterProvider.otherwise('/dashboard');
});

app.run(function($rootScope, $state) {
    $rootScope.$on('$stateChangeStart', function(event, current) {
        $rootScope.pageTitle = current.pageTitle;
    });
});

function getheight() {
	var d= document.documentElement;
	var b= document.body;
	var who= b.offsetHeight? b: d ;
	return Math.max(who.scrollHeight, who.offsetHeight);
}

app.directive('elementsListHeight', function($window, $timeout) {
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			$timeout(function() {
				jQuery(element).height(getheight() - 150 + (attr.elementsListHeight != null && attr.elementsListHeight.length >= 0 
									? parseInt(attr.elementsListHeight) : 0));
			});

		}
	};
});
