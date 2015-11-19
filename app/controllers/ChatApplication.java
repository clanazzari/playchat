package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.MessageResponse;
import models.MessageTP;
import play.Logger;
import play.libs.EventSource;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatApplication extends Controller {

    private static final String USER = "user";
    private static final String USER_NAME = "name";
    private static final String MESSAGE = "message";

    private static Map<String, EventSource> EVENTS_BY_USER = new HashMap<>();
    private static Map<String, JsonNode> DATA_BY_USER = new HashMap<>();

    // Comparator para ordenar usuarios
    private Comparator<JsonNode> userComparator = (usr1, usr2) ->
            usr1.findPath(USER_NAME).asText().compareTo(usr2.findPath(USER_NAME).asText());

    /**
     * Responsavel por receber os eventos SSE
     *
     * @param user
     * @return
     */
    public Result eventListener(String user) {
        // Se usuario for desconectado, deve ser removido
        if (EVENTS_BY_USER.get(user) == null) {
            removeUserByName(user);
            return forbidden();
        }

        return ok(EVENTS_BY_USER.get(user));
    }

    /**
     * Envia Mensagem aos eventos ativos
     *
     * @return
     */
    public Result sendMessage() {
        final JsonNode data = request().body().asJson();
        final String name = data.findPath(USER_NAME).asText();
        final String user = data.findPath(USER).asText();
        final String message = data.findPath(MESSAGE).asText();

        sendMessageToEvents(new MessageResponse(
                MessageTP.MESSAGE,
                name + " disse: " + message,
                DATA_BY_USER.get(user),
                DATA_BY_USER.values().stream()
                        .sorted(userComparator)
                        .collect(Collectors.toList())));
        return ok();
    }

    /**
     * Adiciona usuario na sala
     *
     * @return
     */
    public Result addUser() {
        final JsonNode jsonBody = request().body().asJson();
        final String user = jsonBody.findPath(USER).asText();
        final String name = jsonBody.findPath(USER_NAME).asText();

        // Se o usuario ja existir, retorna erro
        if (EVENTS_BY_USER.containsKey(user)) {
            return forbidden();
        }

        // insere data localmente
        addUserOnMap(user, jsonBody);

        addUserOnMap(user, new EventSource() {
            @Override
            public void onConnected() {
                EventSource current = this;
                // envia mensagem de usuario adicionado
                sendMessageToEvents(new MessageResponse(
                        MessageTP.USER_ENTER,
                        name + " entrou na sala.",
                        jsonBody,
                        DATA_BY_USER.values().stream()
                                .sorted(userComparator)
                                .collect(Collectors.toList())));

                this.onDisconnected(() -> {
                    removeUserExpired(user);
                });
            }
        });

        return ok();
    }

    /**
     * Remove usuario na sala
     *
     * @return
     */
    public Result removeUser(String user) {
        final JsonNode data = DATA_BY_USER.get(user);
        // se usuario ja for detetado,
        if (data == null) {
            sendMessageToEvents(new MessageResponse(
                    MessageTP.USER_QUIT,
                    user + " saiu da sala.",
                    null,
                    DATA_BY_USER.values().stream()
                            .sorted(userComparator)
                            .collect(Collectors.toList())));
        } else {

            final String name = DATA_BY_USER.get(user).findPath(USER_NAME).asText();

            // remove data
            removeUserByName(user);

            // envia mensagem de usuario adicionado

            sendMessageToEvents(new MessageResponse(
                    MessageTP.USER_QUIT,
                    name + " saiu da sala.",
                    data,
                    DATA_BY_USER.values().stream()
                            .sorted(userComparator)
                            .collect(Collectors.toList())));

        }
        return ok();
    }

    /**
     * Remove usuario na sala por tempo expirado
     *
     * @return
     */
    private void removeUserExpired(String user) {
        final JsonNode data = DATA_BY_USER.get(user);
        if (data == null) {
            // certifica que dados estao removidos
            removeUserByName(user);

        } else {
            final String name = DATA_BY_USER.get(user).findPath(USER_NAME).asText();

            // envia mensagem de usuario adicionado
            sendMessageToEvents(new MessageResponse(
                    MessageTP.USER_QUIT_EXPIRED,
                    name + " saiu da sala por tempo expirado.",
                    data,
                    DATA_BY_USER.values().stream()
                            .sorted(userComparator)
                            .collect(Collectors.toList())));

            // remove data
            removeUserByName(user);

        }
    }

    /**
     * Metodo auxiliar que Desconecta usuario pelo nome
     *
     * @return
     */
    private void removeUserByName(String user) {
        Logger.info("User " + user + " removed.");
        EVENTS_BY_USER.entrySet().removeIf(entry -> entry.getKey().equals(user));
        DATA_BY_USER.entrySet().removeIf(entry -> entry.getKey().equals(user));
    }

    /**
     * Metodo auxiliar que adiciona eventos
     *
     * @param user
     * @param event
     */
    private void addUserOnMap(String user, EventSource event) {
        EVENTS_BY_USER.put(user, event);
    }

    private void addUserOnMap(String user, JsonNode data) {
        DATA_BY_USER.put(user, data);
    }

    private void sendMessageToEvents(MessageResponse message) {
        EVENTS_BY_USER.forEach((key, session) -> {
            session.send(EventSource.Event.event(Json.toJson(message)));
        });
    }

}
