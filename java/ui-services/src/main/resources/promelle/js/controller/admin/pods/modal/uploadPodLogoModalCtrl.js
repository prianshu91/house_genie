'use strict';

peAdminApp.controller('uploadPodLogoModalCtrl', [
'$scope',
'adminService',
'$uibModalInstance',
'CURRENT_HOST',
'CURRENT_VERSION',
'podId',
'Upload',
'PROMELLE_USER_URL',
function($scope, adminService, $uibModalInstance, CURRENT_HOST, CURRENT_VERSION, podId, Upload, PROMELLE_USER_URL) {
    $scope.podId = podId;
    $scope.myImage='';
    $scope.picFile='';
    $scope.myCroppedImage = '';

    $scope.handleFileSelect = function(evt) {
      var file=evt.currentTarget.files[0];
      var reader = new FileReader();
      reader.onload = function (evt) {
        $scope.$apply(function($scope){
          $scope.myImage=evt.target.result;
        });
      };
      reader.readAsDataURL(file);
    };
    
    $scope.uploadPic = function(dataUrl, name) {
        var url = PROMELLE_USER_URL + '/user/uploadPodLogo?podId='+$scope.podId
        Upload.upload({
            url: url,
            data: {
                file: Upload.dataUrltoBlob(dataUrl, name)
            },
        }).then(function (response) {
            var logoUrl = response.data.data+'?decache=' + Math.random();
            $uibModalInstance.close(logoUrl);
        });
    };

    $scope.close = function() {
        $uibModalInstance.close();
    };
} ]);