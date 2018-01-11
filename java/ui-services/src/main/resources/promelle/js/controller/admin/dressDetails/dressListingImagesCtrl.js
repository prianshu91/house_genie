'use strict';

peAdminApp
	.controller(
	'dressListingImagesCtrl',
	[
		'$scope',
		'$state',
		'productService',
		'$stateParams',
		'Lightbox',
		'peActivateProductFcty',
		'$uibModal',
		'peProductByIdFcty',
		'toaster',
		'CURRENT_HOST',
		'CURRENT_VERSION',
		function ($scope, $state, productService, $stateParams, Lightbox,
			peActivateProductFcty, $uibModal, peProductByIdFcty, toaster, CURRENT_HOST, CURRENT_VERSION) {

			var init = function () {
				$scope.disableConfirm = false;
				$scope.disableReject = false;
				$scope.setDefaultImageError = false;
				$scope.listImage = {};
				$scope.showNoImageFound = false;
				$scope.corruptData = false;

				$scope.deleteHeader = "Delete dress listing"

				var setButtonState = function () {
					$scope.notifyFields = productService.getNotifyFields();
					if ($scope.notifyFields.length) {
						$scope.disableConfirm = true;
						$scope.disableReject = false;
					} else {
						$scope.disableConfirm = false;
						$scope.disableReject = true;
					}
				}

				setButtonState();

				var listImages = function () {

					$scope.isDev = getHost(CURRENT_HOST);

					$scope.images = [];
					$scope.productData;

					peProductByIdFcty
						.get(
						{
							id: $stateParams.productId,
							timestamp: $scope.isDev ? new Date().getTime() : null,
							version: !$scope.isDev ? CURRENT_VERSION : null
						},
						function (response) {
							$scope.isReadyToActive = true;
							$scope.isReadyToDelete = true;
							$scope.productData = response.data;
							$scope.dressTitle = $scope.productData.title;
							if ($scope.productData.status == 1
								|| $scope.productData.status == 0 || $scope.productData.status == 3) {
								$scope.isReadyToActive = false;
								$scope.isReadyToDelete = false;
							}
							// if ($scope.productData.status == 0) {
							// $scope.isReadyToDelete = false;
							// }

							if ($scope.productData && $scope.productData.images) {
								$scope.showNoImageFound = false;
								var keys = Object.keys($scope.productData.images), caption, arr = [];

								if (keys.indexOf("1") === -1
									|| keys.indexOf("2") == -1) {
									$scope.disableConfirm = true;
									$scope.disableReject = false;
									$scope.setDefaultImageError = true;
								}

								if (keys.indexOf("1") === -1 || keys.indexOf("2") === -1) {
									$scope.corruptData = true;
								}

								if (keys.indexOf("3") === -1) {
									$scope.productData.images['3'] = "/ui/promelle/images/default_image.png"
								}

								if (keys.indexOf("4") === -1) {
									$scope.productData.images['4'] = "/ui/promelle/images/default_image.png"
								}

								Object.keys($scope.productData.images).map(
									function (key) {
										if (key == 1) {
											caption = "Image 1 - Front Image";
										}
										if (key == 2) {
											caption = "Image 2 - Back Image";
										}
										if (key == 3) {
											caption = "Image 3 - Other Image";
										}
										if (key == 4) {
											caption = "Image 4 - Other Image";
										}
										arr.push({
											caption: caption,
											url: $scope.productData.images[key]
										})
									});

								arr.forEach(function (val) {
									var thumbImgUrl = getThumbnailImg(val.url);
									$scope.images.push({
										url: val.url + '?' + (new Date()).getTime(),
										thumbUrl: thumbImgUrl + '?'
										+ (new Date()).getTime(),
										caption: val.caption
									});
								});
							} else {
								$scope.showNoImageFound = true;
								$scope.disableConfirm = true;
								$scope.disableReject = false;
							}
						});

					$scope.notifyFields.forEach(function (field) {
						if (field.type == "image" && field.caption) {
							$scope.listImage[field.caption] = true;
						}
					});
				}

				listImages();

				$scope.openLightboxModal = function (index) {
					Lightbox.openModal($scope.images, index);
				};

				$scope.stateChanged = function (url, caption) {
					if (url === 'Other') {
						productService.setNotifyFields(url, "text");
					} else {
						var imgObj = {
							url: getThumbnailImg(url),
							caption: caption
						};
						productService.setNotifyFields(imgObj, "image");
					}
					setButtonState();
				}

				$scope.confirmListing = function () {
					$scope.header = 'Approve Listing'
					var productId = $stateParams.productId;
					var modalInstance = $uibModal.open({
						templateUrl: 'views/admin/deleteEntity.html',
						controller: 'deleteEntityCtrl',
						$scope: $scope,
						resolve: {
							data: function () {
								return productId;
							},
							header: function () {
								return $scope.header;
							}
						}
					});
					modalInstance.result.then(function (response) {
						if (response) {
							$state.go('dressLists');
						}
					});
				};

				$scope.rejectListing = function () {
					$scope.notifyFields = productService.getNotifyFields();
					var modalInstance = $uibModal
						.open({
							templateUrl: 'views/admin/dressDetails/modal/rejectDressListing.html',
							controller: 'rejectDressListingModalCtrl',
							$scope: $scope,
							resolve: {
								data: function () {
									return $scope.notifyFields;
								},
								ownerId: function () {
									return $stateParams.ownerId;
								},
								productId: function () {
									return $stateParams.productId;
								},
								setDefaultImageError: function () {
									return $scope.setDefaultImageError;
								},
								dressTitle: function () {
									return $scope.dressTitle;
								}

							}
						});
				};

				$scope.deleteListing = function () {
					var id = $stateParams.productId;
					var modalInstance = $uibModal
						.open({
							templateUrl: 'views/admin/dressDetails/modal/deleteDressConfirm.html',
							controller: 'deleteDressConfirmCtrl',
							$scope: $scope,
							resolve: {
								data: function () {
									return $scope.productData;
								},
								ownerId: function () {
									return $stateParams.ownerId;
								},
								productId: function () {
									return $stateParams.productId;
								}
							}
						});
				};
			};

			init();

			$scope.$on('activeTabName', function (event, args) {
				if (args.tabName == 'showTab4') {
					init();
				}
			});

		}]);