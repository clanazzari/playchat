angular.module('app', [
    'ui.bootstrap',
    'angular-google-gapi',
    'app.route',
    'app.controller.login',
    'app.controller.chat',
    'app.controller.chat.room']).

run(function ($rootScope, $state, GApi, GAuth, GData, $window) {
        $rootScope.$state = $state;
        $rootScope.gdata = GData;

        // Executa configuracoes de conexao com o googleapi

        var CLIENT = '446354050442-s11c0d694tqdcvvqf3uv70tlqiufp0no.apps.googleusercontent.com';
        var BASE = 'https://shrouded-chamber-1869.herokuapp.com/';

        GApi.load('myContactApi', 'v1', BASE);
        GAuth.setClient(CLIENT);
        GAuth.setScope('https://www.googleapis.com/auth/userinfo.email');
        GAuth.checkAuth().then(
            function () {
                $state.go('chat.home');
            },
            function () {
                $state.go('login');
            }
        );
    }
);
