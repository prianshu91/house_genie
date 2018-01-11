peAdminApp
    .factory(
    'sessionRecoverer',
    [
        '$q',
        '$injector',
        function ($q, $injector) {
            var sessionRecoverer = {

                    responseError: function (response) {
                    // Session has expired
                    if (response.status == 401 || response.status == 403) {
                        var stateService = $injector.get('$state');
                        stateService.go('login');
                    }
                    return $q.reject(response);
                }
            };
            return sessionRecoverer;
        }]);