angular.module('app.controller.chat.room', []).

controller('ChatRoomCtrl', function ($http, $scope, $state, GData) {

    var _this = this;
    this.msg = "";
    $scope.messages = [];
    $scope.users = [];

    // envia mensagem
    this.sendMessage = function () {

        if (this.msg.length > 0 && this.msg.length <= 1000) {

            $http.post("/chat/message", {
                message: this.msg,
                name: GData.getUser().name,
                picture: GData.getUser().picture,
                user: GData.getUser().email
            }).then(function () {
                _this.msg = "";
            });
        }
    }

    // sai da sala
    this.quitRoom = function () {
        if ($scope.sse != undefined) {
            $scope.sse.close();
        }
        $state.go('chat.home');
    }

    this.quitRoomAndDelete = function () {
        $http.delete("/chat/user/" + GData.getUser().email);
        _this.quitRoom();
    }

    // resebe as mensagens pelo servidor (callback)
    $scope.receiveMessage = function (jsonBody) {
        var data = JSON.parse(jsonBody.data);

        // envia mensagem
        $scope.$apply(function () {
            $scope.messages.push(data.message);
        });

        // para usuario entrou
        if (data.type == 'USER_ENTER' || data.type == 'USER_QUIT' || data.type == 'USER_EXPIRED') {
            _this.updateUsers(data.users);
        }

        // caso o usuario tenha sido expirado, deve ser removido da tela
        if (data.type == 'USER_EXPIRED' && data.user.user == GData.getUser().email) {
            // sai do chat
            _this.quitRoom();
        }
    }

    this.updateUsers = function (users) {
        $scope.$apply(function () {
            $scope.users = [];
            for (var i in users) {
                $scope.users.push(users[i]);
            }
        });
    }

    // caso ocorra um erro, o usuario e desconectado
    $scope.receiveError = function (data) {
        console.log(data);
        // sai do chat
        //_this.quitRoom();
    }

    // Adiciona novo usuario na sala
    this.addUser = function () {
        $http.post("/chat/user", {
            name: GData.getUser().name,
            picture: GData.getUser().picture,
            user: GData.getUser().email
        }).then(function () {
            _this.startListenerSSE();
        }, function () {
            // caso retorne erro, sai do chat
            _this.quitRoom();
        });
    };

    // listener responsavel por receber mensagens do server-side
    this.startListenerSSE = function () {
        $scope.sse = new EventSource("/chat/receiver/" + GData.getUser().email);
        $scope.sse.addEventListener("message", $scope.receiveMessage, false);
        $scope.sse.addEventListener("error", $scope.receiveError, false);
    };

    this.addUser();
});
