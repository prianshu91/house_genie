app.controller('DashboardCtrl', function($scope, $http) {
	
	$scope.tabs = ['Permissions', 'Roles', 'Accounts', 'Users', 'Groups'];

	$scope.currentTab = 'Permissions';

	$scope.onClickTab = function (tab) {
	    $scope.currentTab = tab;
	}

	$scope.isActiveTab = function(tab) {
	    return tab == $scope.currentTab;
	}

	$scope.loadPermissions = function() {
		$http.get('/service/permission/list?offset=-1&limit=-1').success(function(data) {
			$scope.permissions = data.data;
		});
	}

	$scope.loadRoles = function() {
		$http.get('/service/role/list?offset=-1&limit=-1').success(function(data) {
			$scope.roles = data.data;
		});
	}

	$scope.getRolePermissions = function(role) {
		$http.get('/service/role/' + role.id + '/permissions').success(function(data) {
			$scope.selectedRole = role.name;
			$scope.selectedRolePermissions = data.data;
		});
	}

	$scope.createNewAccount = function() {
		$scope.newAccount = {};
	}

	$scope.loadAccounts = function() {
		$http.get('/service/account/list?offset=-1&limit=-1').success(function(data) {
			$scope.accounts = data.data;
			$scope.createNewAccount();
		});
	}

	$scope.addAccount = function(account) {
		if (account == undefined) {
			account = {};
		}
		if (account.email == undefined || account.email.trim() == '') {
			account.error = "Email Address is a required field!!";
			return;
		}
		if (account.name == undefined || account.name.trim() == '') {
			account.error = "Name is a required field!!";
			return;
		}
		if (account.accountName == undefined || account.accountName.trim() == '') {
			account.error = "Account Name is a required field!!";
			return;
		}
		if (account.mobile == undefined || account.mobile.trim() == '') {
			account.error = "Mobile is a required field!!";
			return;
		}
		delete account.error;
		console.log(account);
		$http.post('/service/account/register', account).success(function(data) {
			$scope.loadAccounts();
		});
	}

	$scope.deleteAccount = function(accountId) {
		$http.delete('/service/account/' + accountId).success(function(data) {
			$scope.loadAccounts();
		});
	}

	$scope.getAccountUsers = function(account) {
		$http.get('/service/user/list?accountId=' + account.id + '&offset=-1&limit=-1').success(function(data) {
			$scope.selectedAccount = account.name;
			$scope.selectedAccountUsers = data.data;
		});
	}

	$scope.createNewUser = function() {
		$scope.newUser = {};
	}

	$scope.selectAccountForUsers = function(accountId) {
		$http.get('/service/user/list?accountId=' + accountId + '&offset=-1&limit=-1').success(function(data) {
			$scope.selectedAccountUsers = data.data;
			$scope.accountSelect = accountId;
			$scope.createNewUser();
		});
		$scope.selectAccountForGroups(accountId);
	}

	$scope.addUser = function(user) {
		if (user == undefined) {
			user = {};
		}
		if (user.email == undefined || user.email.trim() == '') {
			user.error = "Email Address is a required field!!";
			return;
		}
		if (user.name == undefined || user.name.trim() == '') {
			user.error = "Name is a required field!!";
			return;
		}
		delete user.error;
		user.accountId = $scope.accountSelect;
		if (user.id == undefined) {
			$http.post('/service/user', user).success(function(data) {
				if (data.errorCode != undefined) {
					alert(data.errors[0]);
				} else {
					$scope.selectAccountForUsers($scope.accountSelect);
				}
			});
		} else {
			var dataUser = angular.copy(user);
			delete dataUser.permissions;
			delete dataUser.roles;
			delete dataUser.groups;
			$http.put('/service/user/' + dataUser.id, dataUser).success(function(data) {
				if (data.errorCode != undefined) {
					alert(data.errors[0]);
				} else {
					$scope.selectAccountForUsers($scope.accountSelect);
				}
			});
		}
	}

	$scope.editUser = function(user) {
		$scope.newUser = user;
	}

	$scope.deleteUser = function(userId) {
		$http.delete('/service/user/' + userId).success(function(data) {
			$scope.selectAccountForUsers($scope.accountSelect);
		});
	}

	$scope.getUserAccess = function(user) {
		$scope.userAccess = user;
		$http.get('/service/user/' + user.id + '/permissions').success(function(data) {
			var permissionIds = [];
			var userPermissions = data.data;
			for (userPermissionIndex in userPermissions) {
				var userPermission = userPermissions[userPermissionIndex];
				permissionIds.push(userPermission.id);
			}
			var userPermissions = angular.copy($scope.permissions);
			for (userPermissionIndex in userPermissions) {
				var userPermission = userPermissions[userPermissionIndex];
				userPermission['isSelected'] = permissionIds.indexOf(userPermission.id) != -1;
			}
			$scope.userAccess["permissions"] = userPermissions;
		});
		$http.get('/service/user/' + user.id + '/roles').success(function(data) {
			var roleIds = [];
			var userRoles = data.data;
			for (userRoleIndex in userRoles) {
				var userRole = userRoles[userRoleIndex];
				roleIds.push(userRole.id);
			}
			var userRoles = angular.copy($scope.roles);
			for (userRoleIndex in userRoles) {
				var userRole = userRoles[userRoleIndex];
				userRole['isSelected'] = roleIds.indexOf(userRole.id) != -1;
			}
			$scope.userAccess["roles"] = userRoles;
		});
		$http.get('/service/user/' + user.id + '/groups').success(function(data) {
			var groupIds = [];
			var userGroups = data.data;
			for (userGroupIndex in userGroups) {
				var userGroup = userGroups[userGroupIndex];
				groupIds.push(userGroup.id);
			}
			var userGroups = angular.copy($scope.selectedAccountGroups);
			for (userGroupIndex in userGroups) {
				var userGroup = userGroups[userGroupIndex];
				userGroup['isSelected'] = groupIds.indexOf(userGroup.id) != -1;
			}
			$scope.userAccess["groups"] = userGroups;
		});
	}

	$scope.updateUserAccess = function(userAccess) {
		var permissionsToAdd = [];
		var permissions = userAccess["permissions"];
		for (permissionIndex in permissions) {
			var permission = permissions[permissionIndex];
			if (permission.isSelected == true) {
				permissionsToAdd.push(permission.id);
			}
		}
		$http.put('/service/user/'+ userAccess.id + '/updatePermissions', permissionsToAdd).success(function(data) {
			console.log(data);
		});

		var rolesToAdd = [];
		var roles = userAccess["roles"];
		for (roleIndex in roles) {
			var role = roles[roleIndex];
			if (role.isSelected == true) {
				rolesToAdd.push(role.id);
			}
		}
		$http.put('/service/user/'+ userAccess.id + '/updateRoles', rolesToAdd).success(function(data) {
			console.log(data);
		});

		var groupsToAdd = [];
		var groups = userAccess["groups"];
		for (groupIndex in groups) {
			var group = groups[groupIndex];
			if (group.isSelected == true) {
				groupsToAdd.push(group.id);
			}
		}
		$http.put('/service/user/'+ userAccess.id + '/updateGroups', groupsToAdd).success(function(data) {
			console.log(data);
		});

	}
	
	$scope.getAccountGroups = function(account) {
		$http.get('/service/group/account/' + account.id).success(function(data) {
			$scope.selectedAccount = account.name;
			$scope.selectedAccountGroups = data.data;
		});
	}

	$scope.createNewGroup = function() {
		$scope.newGroup = {};
	}

	$scope.selectAccountForGroups = function(accountId) {
		$http.get('/service/group/account/' + accountId).success(function(data) {
			$scope.selectedAccountGroups = data.data;
			$scope.accountSelect = accountId;
			$scope.createNewGroup();
		});
	}

	$scope.addGroup = function(group) {
		if (group == undefined) {
			group = {};
		}
		if (group.name == undefined || group.name.trim() == '') {
			group.error = "Name is a required field!!";
			return;
		}
		if (group.code == undefined || group.code.trim() == '') {
			group.error = "Code is a required field!!";
			return;
		}
		group.accountId = $scope.accountSelect;
		$http.post('/service/group', group).success(function(data) {
			$scope.selectAccountForGroups($scope.accountSelect);
		});
	}

	$scope.deleteGroup = function(groupId) {
		$http.delete('/service/group/' + groupId).success(function(data) {
			$scope.selectAccountForGroups($scope.accountSelect);
		});
	}

	$scope.getGroupRoles = function(group) {
		$scope.selectedGroup = group;
		$http.get('/service/group/' + group.id + '/roles').success(function(data) {
			var roleIds = [];
			var groupRoles = data.data;
			for (groupRoleIndex in groupRoles) {
				var groupRole = groupRoles[groupRoleIndex];
				roleIds.push(groupRole.id);
			}
			var groupRoles = angular.copy($scope.roles);
			for (groupRoleIndex in groupRoles) {
				var groupRole = groupRoles[groupRoleIndex];
				groupRole['isSelected'] = roleIds.indexOf(groupRole.id) != -1;
				groupRole['orig'] = roleIds.indexOf(groupRole.id) != -1;
			}
			$scope.groupRoles = groupRoles;
		});
	}

	$scope.saveGroupRoles = function(groupRoles) {
		var rolesToAdd = [];
		for (groupRoleIndex in groupRoles) {
			var groupRole = groupRoles[groupRoleIndex];
			if (groupRole.isSelected == true) {
				if (groupRole.orig != true) {
					delete groupRole.orig;
					delete groupRole.isSelected;
					rolesToAdd.push(groupRole.id);
				}
			} else {
				if (groupRole.orig == true) {
					$http.delete('/service/group/'+ $scope.selectedGroup.id + '/role/' + groupRole.id).success(function(data) {
						console.log(data);
					});
				}
			}
		}
		var dataToPush = {};
		dataToPush["roleIds"] = rolesToAdd.join();
		$http.post('/service/group/'+ $scope.selectedGroup.id + '/roles', dataToPush).success(function(data) {
			console.log(data);
		});
	}

	$scope.activateUserSelect = function(user) {
		$scope.activateUser = user;
	}

	$scope.activate = function(user, password, confirmPassword) {
		if (password == undefined || password.trim() == '') {
			alert("Password is a required field!!");
			return;
		}
		if (confirmPassword == undefined || confirmPassword.trim() == '') {
			alert("Confirm Password is a required field!!");
			return;
		}
		if (password.trim() != confirmPassword.trim()) {
			alert("Password mismatch!!");
			return;
		}
		user["password"] = password;
		$http.post('/service/user/activate', user).success(function(data) {
			$scope.selectAccountForUsers(user.accountId);
		});
	}

	$scope.loginUserSelect = function(user, loginType) {
		$scope.loginUser = user;
		$scope.loginType = loginType;
	}

	$scope.login = function(user, password) {
		var loginUser = {};
		if ($scope.loginType == 'email') {
			loginUser["email"] = user["email"];
		} else if ($scope.loginType == 'mobile') {
			loginUser["mobile"] = user["mobile"];
		}
		loginUser["portalName"] = user["portalName"];
		loginUser["password"] = password;
		$http.post('/service/user/login', loginUser).success(function(data) {
			alert(JSON.stringify(data));
		});
	}

	$scope.loadPermissions();
	$scope.loadRoles();
	$scope.loadAccounts();
});
