app.controller('DashboardCtrl', function($scope, $http) {
	
	$scope.tabs = ['Message', 'Inbox'];

	$scope.currentTab = 'Message';

	$scope.onClickTab = function (tab) {
	    $scope.currentTab = tab;
	}

	$scope.isActiveTab = function(tab) {
	    return tab == $scope.currentTab;
	}

	$scope.loadMessages = function(receiverId) {
		$http.get('/service/communication/message/list?offset=-1&limit=-1').success(function(data) {
			$scope.messages = data.data;
			$scope.createNewMessage();
		});
	}

	$scope.getInbox = function(receiverId) {
		if (receiverId == null || receiverId == 'none') {
			$scope.userMessages.empty();
		} else {
			$http.get('/service/communication/message/list?offset=-1&limit=-1&receiverId=' + receiverId).success(function(data) {
				$scope.userMessages = data.data;
			});
		}
	}

	$scope.getMessages = function(receiverId, senderId) {
		$http.get('/service/communication/message/list?offset=-1&limit=-1&receiverId=' + receiverId + '&senderId=' + senderId).success(function(data) {
			$scope.singleUserMessages = data.data;
		});
	}

	$scope.createNewMessage = function() {
		$scope.newMessage = {};
	}

	$scope.addMessage = function(message) {
		if (message == undefined) {
			message = {};
		}
		if (message.text == undefined || message.text.trim() == '') {
			message.error = "Text is a required field!!";
			return;
		}
		if (message.senderId == undefined || message.senderId.trim() == '') {
			message.error = "Sender Id is a required field!!";
			return;
		}
		if (message.senderName == undefined || message.senderName.trim() == '') {
			message.error = "Sender Name is a required field!!";
			return;
		}
		if (message.receiverId == undefined || message.receiverId.trim() == '') {
			message.error = "Receiver Id is a required field!!";
			return;
		}
		if (message.receiverName == undefined || message.receiverName.trim() == '') {
			message.error = "Receiver Name is a required field!!";
			return;
		}
		delete message.error;

		console.log(message);
		$http.post('/service/communication/message', message).success(function(data) {
			$scope.loadMessages();
		});
	}

	$scope.deleteMessage = function(messageId) {
		$http.delete('/service/communication/message/' + messageId).success(function(data) {
			$scope.loadMessages();
		});
	}

	$scope.loadUsers = function() {
		$http.get('/service/communication/message/users').success(function(data) {
			$scope.users = data.data;
		});
	}

	$scope.loadMessages();
	$scope.loadUsers();

});
