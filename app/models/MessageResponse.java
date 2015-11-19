package models;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collection;

public class MessageResponse {

    private MessageTP type;
    private String message;
    private JsonNode user;
    private Collection<JsonNode> users;

    public MessageResponse(MessageTP type, String message, JsonNode user, Collection<JsonNode> users) {
        this.type = type;
        this.message = message;
        this.user = user;
        this.users = users;
    }

    public void setType(MessageTP type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsers(Collection<JsonNode> users) {
        this.users = users;
    }

    public String getMessage() {

        return message;
    }

    public Collection<JsonNode> getUsers() {
        return users;
    }

    public MessageTP getType() {
        return type;
    }

    public void setUser(JsonNode user) {
        this.user = user;
    }

    public JsonNode getUser() {

        return user;
    }
}
