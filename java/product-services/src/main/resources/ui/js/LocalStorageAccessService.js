'use strict';

app.service('LocalStorageAccessService', ['localStorageService', function(localStorageService) { 

	this.getValue = function(key) {
		return localStorageService.get(key);
	}
	this.setValue = function(key, value) {
		localStorageService.set(key, value);
	}

	this.removeValue = function(key) {
		localStorageService.remove(key);
	}

}]);