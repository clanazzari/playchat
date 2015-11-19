angular.module('app.controller.login', []).

controller('LoginCtrl', function ($scope, GAuth, GData, GApi, $state) {

    // Executa login no sistema
    $scope.doLogin = function () {
        GAuth.login().then(function () {
            $state.go('chat.home');
        });
    };

});
