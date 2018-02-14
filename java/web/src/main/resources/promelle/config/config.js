var promelleApp = angular.module("promelleApp", [ 'ngAnimate', 'ngTouch', 'ncy-angular-breadcrumb', 'ui.bootstrap',
		'infinite-scroll', 'ui.router', 'rzModule', 'LocalStorageModule','ngMaterial', 'ngMessages']);

promelleApp.config([
		'$stateProvider',
		'$urlRouterProvider',
		'$locationProvider',
		'$httpProvider',
		'$breadcrumbProvider',
		function($stateProvider, $urlRouterProvider, $locationProvider,
				$httpProvider, $breadcrumbProvider) {

			$urlRouterProvider.when('/', '');
			$urlRouterProvider.when('', '/');

			$urlRouterProvider.otherwise('/home');

			$stateProvider.state('home', {
				url : '/home',
				templateUrl : 'view/home.html',
				controller : 'homeCtrl',
				friendlyName : 'Home',
				ncyBreadcrumb: {
		        	label: 'Home'
		        }
			}).state('productList', {
				url : '/productList',
				templateUrl : 'view/productListing.html',
				controller : 'productListingCtrl',
				friendlyName : 'Product List',
				ncyBreadcrumb: {
		        	label: 'Dress Listing'
		        }
			}).state('about', {
				url : '/about',
				templateUrl : 'view/about.html',
				controller: 'aboutUsCtrl',
				friendlyName : 'About',
				ncyBreadcrumb: {
		        	label: 'About Us'
		        }
			}).state('privacyPolicy', {
				url : '/privacyPolicy',
				templateUrl : 'view/privacyPolicy.html',
				controller : 'privacyPolicyCtrl',
				friendlyName : 'Privacy Policy',
				ncyBreadcrumb: {
		        	label: 'Privacy Policy'
		        }
			}).state('termsCondition', {
				url : '/termsCondition',
				templateUrl : 'view/termsCondition.html',
				controller : 'termsConditionCtrl',
				friendlyName : 'Terms Condition',
				ncyBreadcrumb: {
		        	label: 'Terms Condition'
		        }
			}).state('contact-us', {
				url : '/contact-us',
				templateUrl : 'view/contact.html',
				controller : 'contactCtrl',
				friendlyName : 'Contact Us',
				ncyBreadcrumb: {
		        	label: 'Contact Us'
		        }
			}).state('faq', {
				url : '/faq',
				templateUrl : 'view/faq.html',
				controller : 'FAQCtrl',
				friendlyName : 'FAQ',
				ncyBreadcrumb: {
		        	label: 'FAQ'
		        }
			}).state('productDetail', {
				url : '/productDetail?ownerId?productId',
				templateUrl : 'view/productDetail.html',
				controller : 'productDetailCtrl',
				friendlyName : 'Product Detail',
				ncyBreadcrumb: {
		        	label: 'Dress Detail',
		        	parent: 'productList'
		        }
			}).
			state('employeeDetail', {
				url : '/profile',
				templateUrl : 'view/employeeDetail.html',
				controller : 'employeeDetailCtrl',
				friendlyName : 'Employee Detail',
				ncyBreadcrumb: {
		        	label: 'Empoloyee Detail',
		        	parent: 'productList'
		        }
			}).state('reviewDetail', {
				url : '/reviewDetail/:productId',
				templateUrl : 'view/reviewDetail.html',
				controller : 'reviewDetailCtrl',
				friendlyName : 'Review Detail',
				ncyBreadcrumb: {
		        	label: 'Review Details'
		        }
			}).state('testimonials', {
				url : '/testimonials',
				templateUrl : 'view/testimonials.html',
				friendlyName : 'Testimonials',
				ncyBreadcrumb: {
		        	label: 'Testimonials'
		        }
			})

			$breadcrumbProvider.setOptions({
		    	prefixStateName: 'home',
		    	template: 'bootstrap3'
		    });
			//$locationProvider.html5Mode(true).hashPrefix('!');;
		} ]);

promelleApp.run([ '$rootScope', '$state', '$anchorScroll', '$breadcrumb', function($rootScope, $state, $anchorScroll, $breadcrumb) {
	$rootScope.$state = $state;
	$rootScope.$on("$stateChangeSuccess", function() {
        window.scrollTo(0, 0);
    });
    $rootScope.isActive = function(stateName) {
      return $state.includes(stateName);
    }

    $rootScope.getLastStepLabel = function() {
      return 'Angular-Breadcrumb';
    }
} ]);

promelleApp.factory('PROMELLE_SERVICE_URL', function($location) {
	return 'http://' + $location.absUrl().split('/')[2] + '/service';
});

promelleApp.constant('PROMELLE_EMAIL_ADDRESS', 'contact@promelle.com');

promelleApp.filter('isEmpty', [function() {
  return function(object) {
    return angular.equals({}, object);
  }
}]);

promelleApp.filter('toArray', function() { return function(obj) {
    if (!(obj instanceof Object)) return obj;
    return _.map(obj, function(val, key) {
        return Object.defineProperty(val, '$key', {__proto__: null, value: key} );
    });
}});