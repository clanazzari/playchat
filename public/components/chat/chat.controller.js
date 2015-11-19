angular.module('app.controller.chat', []).

controller('ChatCtrl', function ($scope, GAuth, $state) {

    // logout do sistema
    this.doLogout = function () {
        GAuth.logout().then(function () {
            $state.go('login');
        });
    };

});
