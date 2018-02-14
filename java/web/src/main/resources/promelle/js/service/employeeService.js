'use strict';

promelleApp.service("employeeService", [
    '$http',
    '$q',
    'PROMELLE_SERVICE_URL',
    '$timeout',
    function($http, $q, PROMELLE_SERVICE_URL, $timeout) {

        this.ratings = [];
        this.employee = {};
        this.getSortingOptions = [];
        const sortingData = [{
            "sortingName": "High to Low",
            "sortingOrder": "-ratings"
        }, {
            "sortingName": "Low to High",
            "sortingOrder": "ratings"
        }, {
            "sortingName": "Recent",
            "sortingOrder": "-date"
        }, {
            "sortingName": "Oldest",
            "sortingOrder": "date"
        }];

        this.setEmployee = function(employee) {
            this.employee = employee;
        }

        this.getEmployee = function() {
            return this.employee;
        }

        this.setRatings = function(rating) {
            this.ratings = rating;
        }

        this.getRatings = function() {
            return this.ratings;
        }

        this.getSortingOptions = function() {
            return this.sortingData;
        }

        this.getSortingOrder = function(sortingName) {
            var sortObject = sortingData.find(function(obj) {
                return obj.sortingName === sortingName
            });
            return sortObject ? sortObject.sortingOrder : "ratings";
        }

        this.getEmployeeList = function() {
            var defer = $q.defer(),
                query = {
                    params: queryParams
                };

            $http.get(PROMELLE_SERVICE_URL + '/employee/list', query)
                .success(function(data) {
                    defer.resolve(data);
                }).error(function(err) {
                    defer.reject(err);
                });
            return defer.promise;
        };

        this.getEmployeeById = function(id) {
            var defer = $q.defer();
            $http.get(PROMELLE_SERVICE_URL + '/employee/' + id).success(
                function(data) {
                    defer.resolve(data);
                }).error(function(err) {
                defer.reject(err);
            });
            return defer.promise;
        };

        this.getStateList = function() {
            var defer = $q.defer();

            $http.get(PROMELLE_SERVICE_URL + '/state').success(
                function(data) {
                    defer.resolve(data);
                }).error(function(err) {
                defer.reject(err);
            });
            return defer.promise;
        }

        this.getCityList = function(state) {
            var defer = $q.defer();

            $http.get(PROMELLE_SERVICE_URL + '/state/' + id).success(
                function(data) {
                    defer.resolve(data);
                }).error(function(err) {
                defer.reject(err);
            });
            return defer.promise;
        }

        this.getAreaList = function(state, city) {
            var defer = $q.defer();
            var query = {
                city: city,
                state: state
            };
            $http.get(PROMELLE_SERVICE_URL + '/state/', query).success(
                function(data) {
                    defer.resolve(data);
                }).error(function(err) {
                defer.reject(err);
            });
            return defer.promise;
        }

        this.getReviewList = function(queryParams) {
            var defer = $q.defer(),
                query = {
                    params: queryParams
                };

            $http.get(PROMELLE_SERVICE_URL + '/review/list', query, {
                ignoreLoadingBar: true
            }).success(function(data) {
                defer.resolve(data);
            }).error(function(err) {
                defer.reject(err);
            });

            return defer.promise;
        }
    }
]);