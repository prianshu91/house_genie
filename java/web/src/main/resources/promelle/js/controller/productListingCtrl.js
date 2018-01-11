'use strict';

promelleApp.controller('productListingCtrl', [
	'$scope',
	'productService',
	'$state',
	'localStorageService',
	'$window',
	function ($scope, productService, $state, localStorageService, $window) {
		$scope.isLoading = true;
		$scope.isResetDisabled = true;
		$scope.showSideBar = false;
		$scope.showWaistList = false;
		$scope.toogleSideBar = function (status) {
			if (status == 'open') {
				$scope.showSideBar = true;
			} else {
				$scope.showSideBar = false;
			}
		};

		$scope.setAccordionStatus = function () {
			$scope.status = {
				open1: true,
				open2: true,
				open3: true,
				open4: true,
				open5: true,
				open6: true
			};
		};

		$scope.setAccordionStatus();

		$scope.minSlider = {
			value: 500,
			options: {
				ceil: 500,
				floor: 0,
				translate: function (value) {
					return '$' + value;
				},
				onEnd: function () {
					$scope.isFilterApplied = true;
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
				}
			}
		};

		$scope.itemsPerPage = 30
		$scope.currentPage = 1;

		var paginationOptions = {
			pageNumber: 0,
			pageSize: 30,
			sort: 'modifiedOn desc'
		};

		$scope.getFilterResults = function (query) {
			$scope.listingData = [];
			$scope.isLoading = true;
			productService.getProductList(query).then(function (response) {
				$scope.isLoading = false;
				$scope.listingData = response.data;
				$scope.totalItems = response.paging.total;
				$scope.listingData.forEach(function (list) {
					if(list.size.length){
						list.size.forEach(function(item, index){
							list.size.splice(index, 1);
							list.size.push(correctString(item));
						})			
					}
					if (list.images && list.images[1]) {
						list.imageUrl = getThumbnailImg(list.images[1]);
					} else {
						list.imageUrl = 'images/No-image-found.jpg';
					}
				});
			}, function (errorInfo) {
				console.log(errorInfo);
			});
		};

		$scope.listQuery = {
			status: 1,
			offset: paginationOptions.pageNumber,
			limit: paginationOptions.pageSize,
			sort: paginationOptions.sort
		};

		$scope.getFilterResults($scope.listQuery);

		/*------------------ Get All Filter Options -------------------*/
		productService.getCategoriesList().then(function (response) {
			$scope.categoriesList = response.data.filters;
			$scope.categoriesList['All'] = {};
			$scope.categoriesList = reverseObject($scope.categoriesList);
		}, function (errorInfo) {
			console.log(errorInfo);
		});

		productService.getDressOccasions().then(function (response) {
			$scope.dressOccasionsList = response.data;
			$scope.dressOccasionsList.unshift('All');
		}, function (errorInfo) {
			console.log(errorInfo);
		});

		$scope.dressColorsList = [];
		productService.getDressColors().then(function (response) {
			response.data.colors.forEach(function (color) {
				color.code = '#' +color.code;
			})
			$scope.dressColorsList = response.data.colors
		}, function (errorInfo) {
			console.log(errorInfo);
		});

		$scope.dressSizesList = [];
		productService.getDressSizes().then(function (response) {
			for (var key in response.data) {
				$scope.dressSizesList.push(response.data[key]);
			}
		}, function (errorInfo) {
			console.log(errorInfo);
		});


		$scope.waistList = ['23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38'];

		/*---------------------- Get Selected Filter Values ------------------*/

		$scope.occasionSelection = [];
		$scope.dressSelection = [];
		$scope.lengthSelection = [];
		$scope.colorSelection = [];
		$scope.sizeSelection = [];
		$scope.sizeSelectionOrignal = [];
		$scope.categoriesSelection = [];
		$scope.categoriesTypeSelection = [];
		$scope.categoriesTypeLengthSelection = [];
		$scope.categoriesSleeveSelection = [];
		$scope.waistSelection = [];


		$scope.categoriesTypeList = [];
		$scope.typeList = {};

		$scope.categoriesSleeveList = [];
		$scope.sleeveList = [];

		$scope.categoriesTypeLengthList = [];
		$scope.typeLengthList = [];

		$scope.isTypeNotFound = false;

		// TYPE FORMATTER
		$scope.toggleCategories = function (key, myObj) {
			$scope.categoriesTypeList = [];
			$scope.categoriesTypeLengthList = [];
			$scope.categoriesSleeveList = [];
			$scope.typeList = {};
			$scope.typeLengthList = [];
			$scope.sleeveList = [];
			$scope.categoriesTypeSelection = [];
			$scope.categoriesTypeLengthSelection = [];
			$scope.categoriesSleeveSelection = [];
			var idx = $scope.categoriesSelection.indexOf(key);
			if (idx === -1) {
				if(myObj.length){
					$scope.pushToSleeveList(key, myObj);
					if(!myObj[0].Type){
						$scope.isTypeNotFound = true;
						$scope.typeLengthList = [];
						$scope.pushToTypeLengthList(key, myObj[0]);
						$scope.typeList = {};
					} else{
						$scope.isTypeNotFound = false;
						$scope.pushToTypeList(key, myObj);
					}
				}
			} else {
				$scope.categoriesTypeList.splice(idx, 1);
				$scope.categoriesSleeveList.splice(idx, 1);
				$scope.getTypeList();
				$scope.getSleeveList();
				if($scope.categoriesTypeLengthList.length){
					$scope.categoriesTypeLengthList.forEach(function (item, index) {
						Object.keys(item).forEach(function (internalKey) {
							var obj = item[internalKey];
							if(obj.parent === key){
								$scope.categoriesTypeLengthList.splice(index, 1);
								$scope.getTypeLengthList();
							}
						});
					});
				}
				
				if(myObj[0].Type){
					myObj[0].Type.forEach(function(val){
						Object.keys(val).forEach(function (key) {
							var objArray = val[key];
							objArray.forEach(function(internalVal){
								var internalIndex = $scope.categoriesTypeLengthSelection.indexOf(internalVal);
								if(internalIndex > -1){
									$scope.categoriesTypeLengthSelection.splice(internalIndex, 1);
								}
							});
							var index = $scope.categoriesTypeSelection.indexOf(key);
							if(index > -1){
								$scope.categoriesTypeSelection.splice(index, 1);
							}
						});
					});
				}
			}
		};

		$scope.pushToTypeList = function (key, myObj) {
			var values = [];
			Object.keys(myObj[0]).forEach(function (key) {
				values = myObj[0][key];
			});
			var localObj = {};
			localObj[key] = {
				parent: key,
				types: values
			};
			$scope.categoriesTypeList.push(localObj);
			$scope.getTypeList();
		}

		$scope.getTypeList = function () {
			$scope.typeList = {};
			$scope.categoriesTypeList.forEach(function (item) {
				Object.keys(item).forEach(function (key) {
					var objArray = item[key];
					if(objArray.types.length){
						objArray.types.forEach(function (internalObj) {
							Object.keys(internalObj).forEach(function (internalKey) {
								var typeObj = {
									parent: objArray.parent,
									length: internalObj[internalKey]
								}
								$scope.typeList[internalKey] = typeObj;
							})
						})
					}
				});
			});
		}

		// LENGTH FORMATTER
		$scope.toogleType = function (key, typeArr) {
			if ($scope.categoriesTypeLengthList.length) {
				var idx = $scope.categoriesTypeSelection.indexOf(key);
				if (idx === -1) {
					$scope.pushToTypeLengthList(key, typeArr);
				} else {
					$scope.categoriesTypeLengthList.splice(idx, 1);
					$scope.getTypeLengthList();
					typeArr.length.forEach(function(val){
						var index = $scope.categoriesTypeLengthSelection.indexOf(val);
						if(index > -1){
							$scope.categoriesTypeLengthSelection.splice(index, 1);
						}
					});
				}

			} else {
				$scope.pushToTypeLengthList(key, typeArr);
			}
		}

		$scope.pushToTypeLengthList = function (key, myArr) {
			var localObj = {};
			var values = [];
			if($scope.isTypeNotFound){
				Object.keys(myArr).forEach(function (key1) {
					values = myArr[key1];
				});
				localObj[key] = {
					parent: key,
					length: values
				};
			} else{
				localObj[key] = {
					parent: myArr.parent,
					length: myArr.length
				};
			}
			$scope.categoriesTypeLengthList.push(localObj);
			$scope.getTypeLengthList();
		}

		$scope.getTypeLengthList = function () {
			$scope.typeLengthList = [];
			var localArr = [];
			$scope.categoriesTypeLengthList.forEach(function (item) {
				Object.keys(item).forEach(function (key) {
					var objArray = item[key];
					localArr = localArr.concat(objArray.length);
					localArr.forEach(function (val) {
						if ($scope.typeLengthList.indexOf(val) == -1) {
							$scope.typeLengthList.push(val);
						}
					});
				});
			});
		}

		// SLEEVE FORMATTER

		$scope.pushToSleeveList = function (key, myObj) {
			var values = [];
			if(myObj.length > 1){
				Object.keys(myObj[1]).forEach(function (key) {
					values = myObj[1][key];
				});
				var localObj = {};
				localObj[key] = {
					parent: key,
					sleeves: values
				};
				$scope.categoriesSleeveList.push(localObj);
				$scope.getSleeveList();
			}
		}

		$scope.getSleeveList = function () {
			$scope.sleeveList = [];
			$scope.categoriesSleeveList.forEach(function (item) {
				Object.keys(item).forEach(function (key) {
					var objArray = item[key];
					if(objArray.sleeves.length){
						objArray.sleeves.forEach(function (internalObj) {
							var sleeveObj = {
								parent: objArray.parent,
								sleeves: internalObj
							}
							$scope.sleeveList.push(sleeveObj);
						})
					}
				});
			});
		}

		$scope.toggleSelection = function (selectedItem, selection) {
			$scope.isLoading = true;
			switch (selection) {
				case 'occasion':
					if(selectedItem !== 'All'){
						var allIdx = $scope.occasionSelection.indexOf('All');
						if(allIdx > -1){
							$scope.occasionSelection.splice(allIdx, 1);
						}
					} else {
						$scope.occasionSelection = [];
					}
					var idx = $scope.occasionSelection.indexOf(selectedItem);
					if (idx > -1) {
						$scope.occasionSelection.splice(idx, 1);
					} else {
						$scope.occasionSelection.push(selectedItem);
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
					break;
				case 'categories':
					var idx = $scope.categoriesSelection.indexOf(selectedItem);
					if (idx > -1) {
						$scope.categoriesSelection.splice(idx, 1);
					} else {
						$scope.categoriesSelection.splice(idx, 1);
						$scope.categoriesSelection.push(selectedItem);
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
					break;
				case 'categoriesType':
					var idx = $scope.categoriesTypeSelection.indexOf(selectedItem);
					if (idx > -1) {
						$scope.categoriesTypeSelection.splice(idx, 1);
					} else {
						$scope.categoriesTypeSelection.push(selectedItem);
					}
					if($scope.categoriesTypeSelection.indexOf('Jeans') === -1 && $scope.categoriesTypeSelection.indexOf('Pants') === -1) {
						$scope.showWaistList = false;
					} else {
						$scope.showWaistList = true;
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
					break;
				case 'categoriesTypeLength':
					var idx = $scope.categoriesTypeLengthSelection.indexOf(selectedItem);
					if (idx > -1) {
						$scope.categoriesTypeLengthSelection.splice(idx, 1);
					} else {
						$scope.categoriesTypeLengthSelection.push(selectedItem);
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
					break;
				case 'categoriesSleeve':
					var idx = $scope.categoriesSleeveSelection.indexOf(selectedItem);
					if (idx > -1) {
						$scope.categoriesSleeveSelection.splice(idx, 1);
					} else {
						$scope.categoriesSleeveSelection.push(selectedItem);
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
					break;
				case 'color':
					var idx = $scope.colorSelection.indexOf(selectedItem);
					if (idx > -1) {
						$scope.colorSelection.splice(idx, 1);
					} else {
						$scope.colorSelection.push(selectedItem);
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
					break;
				case 'regular':
					var idx = $scope.sizeSelectionOrignal.indexOf(selectedItem);
					if (idx > -1) {
						$scope.sizeSelection.splice(idx, 1);
						$scope.sizeSelectionOrignal.splice(idx, 1);
					} else {
						var item = 'Regular_' + selectedItem;
						$scope.sizeSelection.push(item);
						$scope.sizeSelectionOrignal.push(selectedItem);
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
					break;
				case 'junior':
					var idx = $scope.sizeSelectionOrignal.indexOf(selectedItem);
					if (idx > -1) {
						$scope.sizeSelection.splice(idx, 1);
						$scope.sizeSelectionOrignal.splice(idx, 1);
					} else {
						var item = 'Juniors_' + selectedItem;
						$scope.sizeSelection.push(item);
						$scope.sizeSelectionOrignal.push(selectedItem);
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
					break;
				case 'waist':
					var idx = $scope.waistSelection.indexOf(selectedItem);
					if (idx > -1) {
						$scope.waistSelection.splice(idx, 1);
					} else {
						$scope.waistSelection.push(selectedItem);
					}
					paginationOptions.pageNumber = 0;
					$scope.filterResults();
				default:
					console.log("error in selection");
			}
		};

		$scope.filterResults = function () {
			backToTop();
			if ($scope.occasionSelection.length || $scope.categoriesSelection.length || $scope.categoriesTypeSelection.length || $scope.categoriesTypeLengthSelection.length || $scope.colorSelection.length || $scope.sizeSelection.length || $scope.minSlider.value > 0) {
				$scope.isResetDisabled = false;
			} else {
				$scope.isResetDisabled = true;
			}

			var query = {
				occasion: $scope.occasionSelection.length ? $scope.occasionSelection.toString() : null,
				type: $scope.categoriesTypeSelection.length ? $scope.categoriesTypeSelection.toString() : null,
				length: $scope.categoriesTypeLengthSelection.length ? $scope.categoriesTypeLengthSelection.toString() : null,
				categories: $scope.categoriesSelection.length ? $scope.categoriesSelection.toString() : null,
				rentalPriceStart: '0',
				rentalPriceEnd: $scope.minSlider.value == 0 ? '0' : $scope.minSlider.value,
				color: $scope.colorSelection.length ? $scope.colorSelection.toString() : null,
				size: $scope.sizeSelection.length ? $scope.sizeSelection.toString() : null,
				waist: $scope.waistSelection.length ? $scope.waistSelection.toString() : null,
				sleeve: $scope.categoriesSleeveSelection.length ? $scope.categoriesSleeveSelection.toString() : null,
				offset: paginationOptions.pageNumber,
				limit: paginationOptions.pageSize,
				sort: paginationOptions.sort,
				status: 1
			};
			
			if(query.occasion === 'All'){
				delete query.occasion;
			}

			if(query.categories === 'All'){
				delete query.categories;
				delete query.type;
				delete query.length;
				delete query.sleeve;
			}

			$scope.getFilterResults(query);
		};

		$scope.clearResults = function () {
			backToTop();
			$scope.currentPage = 1;
			$scope.occasionSelection = [];
			$scope.colorSelection = [];
			$scope.sizeSelection = [];
			$scope.categoriesSelection = [];
			$scope.categoriesTypeSelection = [];
			$scope.sizeSelectionOrignal = [];
			$scope.minSlider.value = 500;
			$scope.getFilterResults($scope.listQuery);
			$scope.isResetDisabled = true;
			//$scope.setAccordionStatus();
		}

		$scope.pageChanged = function (page) {
			$scope.currentPage = page;
			paginationOptions.pageNumber = ($scope.currentPage - 1) * paginationOptions.pageSize;
			$scope.filterResults();
		};


		$scope.openDressDetails = function (data) {
			var url = $state.href('productDetail',
				{
					ownerId: data.ownerId,
					productId: data.id
				},
				{
					absolute: true
				}
			);
			$window.open(url, '_blank');
		}
	}
]);