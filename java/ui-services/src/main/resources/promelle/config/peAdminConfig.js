peAdminApp
	.config([
		'$stateProvider',
		'$urlRouterProvider',
		'$locationProvider',
		'$httpProvider',
		function ($stateProvider, $urlRouterProvider, $locationProvider,
			$httpProvider) {

			var access = routingConfig.accessLevels;

			// $locationProvider.html5Mode(true).hashPrefix('!');

			$httpProvider.interceptors.push('sessionRecoverer');

			$urlRouterProvider.otherwise("404");

			$urlRouterProvider.when('/', '');
			$urlRouterProvider.when('', '/login');

			$stateProvider
				.state('admin', {
					url: '/admin',
					templateUrl: 'views/admin/userDetails/adminHome.html',
					controller: 'adminHomCtrl',
					access: [access.anon],
					friendlyName: 'User Management',
					routeName: 'users'
				}).state('userDetail', {
					url: '/userDetail?userId',
					templateUrl: 'views/admin/userDetails/userDetails.html',
					controller: 'userDetailsCtrl',
					access: [access.user],
					friendlyName: 'User Details',
					routeName: 'users'
				}).state('dressLists', {
					url: '/dressLists',
					templateUrl: 'views/admin/dressDetails/dressListingHome.html',
					controller: 'dressListingHomeCtrl',
					access: [access.user],
					friendlyName: 'Dress Listings',
					routeName: 'dressListing'
				}).state('dressDetail', {
					url: '/dressDetail?ownerId?productId',
					templateUrl: 'views/admin/dressDetails/dressDetails.html',
					controller: 'dressDetailsCtrl',
					access: [access.user],
					friendlyName: 'Dress Details',
					routeName: 'dressListing'
				}).state('paymentOptions', {
					url: '/payment',
					templateUrl: 'views/admin/paymentDetails/paymentDetails.html',
					controller: 'paymentDetailsHomeCtrl',
					access: [access.user],
					friendlyName: 'Payment Details',
					routeName: 'payment'
				}).state('addSchool', {
					url: '/addSchool',
					templateUrl: 'views/admin/addSchoolDetails/addSchoolDetailsHome.html',
					controller: 'addSchoolDetailsHomeCtrl',
					access: [access.user],
					friendlyName: 'Add School',
					routeName: 'addSchool'
				}).state('dressRental', {
					url: '/dressRental',
					templateUrl: 'views/admin/dressRentalDetails/dressRentalDetailsHome.html',
					controller: 'dressRentalDetailsHomeCtrl',
					access: [access.user],
					friendlyName: 'Dress Rental',
					routeName: 'dressRental'
				}).state('promelleSettings', {
					url: '/promelleSettings',
					templateUrl: 'views/admin/promelleSettings/promelleSettingsHome.html',
					controller: 'promelleSettingsHomeCtrl',
					access: [access.user],
					friendlyName: 'FAQ text',
					routeName: 'promelleSettings'
				}).state('rentalOrderDetails', {
					url: '/rentalOrderDetails?orderId',
					templateUrl: 'views/admin/userDetails/modal/rentalOrderDetails.html',
					controller: 'rentalOrderDetailsCtrl',
					access: [access.user],
					friendlyName: 'Rental Order Details',
					routeName: 'users'
				}).state('broadcasts', {
					url: '/broadcasts',
					templateUrl: 'views/admin/broadcasts/modal/broadcasts.html',
					controller: 'broadcastsCtrl',
					access: [access.user],
					friendlyName: 'Broadcast Email & Messages',
					routeName: 'broadcasts'
				}).state('cms', {
					url: '/cms',
					templateUrl: 'views/admin/cms/modal/cms.html',
					controller: 'cmsCtrl',
					access: [access.user],
					friendlyName: 'Cms for FAQ, TNC & Policy',
					routeName: 'cms'
				}).state('pods', {
					url: '/pods',
					templateUrl: 'views/admin/pods/podsList.html',
					controller: 'podsListCtrl',
					access: [access.user],
					friendlyName: 'Pods',
					routeName: 'pods'
				}).state('podDetail', {
					url: '/pods/detail?podId',
					templateUrl: 'views/admin/pods/podDetail.html',
					controller: 'podDetailCtrl',
					access: [access.user],
					friendlyName: 'Pod Detail',
					routeName: 'pods'
				}).state('promoCodes', {
					url: '/promoCodes',
					templateUrl: 'views/admin/promoCodes/promoCodesHome.html',
					controller: 'promoCodesHomeCtrl',
					access: [access.user],
					friendlyName: 'Promo Codes',
					routeName: 'promoCodes'
				}).state('lendingOrderDetails', {
					url: '/lendingOrderDetails?orderId',
					templateUrl: 'views/admin/userDetails/modal/lendingOrdersDetails.html',
					controller: 'lendingOrderDetailsCtrl',
					access: [access.user],
					friendlyName: 'Lending Order Details',
					routeName: 'users'
				}).state('login', {
					url: '/login',
					templateUrl: 'views/login/login.html',
					controller: 'loginCtrl',
					access: [access.anon],
					hideInMenu: true
				}).state('404', {
					url: '/404',
					templateUrl: 'views/pe404Ctrl.html',
					access: [access.public],
					hideInMenu: true
				})

			// initialize get if not there
			if (!$httpProvider.defaults.headers.get) {
				$httpProvider.defaults.headers.get = {};
			}
			// disable IE request caching
			$httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
			$httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
			$httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

			// $httpProvider.interceptors.push('evmRequestInterceptor');
			// $httpProvider.interceptors.push('rsAuthInterceptorFactory');
			$httpProvider.defaults.useXDomain = true;
			delete $httpProvider.defaults.headers.common['X-Requested-With'];
		}]);
