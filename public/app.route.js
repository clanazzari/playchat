angular.module('app.route', ['ui.router']).

config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.otherwise('/');

        $stateProvider
            .state('login', {
                url: '/',
                templateUrl: 'components/login/login.html',
                controller: 'LoginCtrl as vm'
            })
            .state('chat', {
                abstract: true,
                templateUrl: 'components/chat/chat.html',
                controller: 'ChatCtrl as vmc'
            })
            .state('chat.home', {
                url: '/chat/home',
                templateUrl: 'components/chat/home/home.html'
            })
            .state('chat.room', {
                url: '/chat/room',
                templateUrl: 'components/chat/room/room.html',
                controller: 'ChatRoomCtrl as vm',
                cache: false
            });
    }
);
