package ru.butorin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonAutoDetect
@JsonRootName(value = "Request")
public class ClientJson {
    private Client client;
    private Message message;

    public ClientJson() {
    }

    public ClientJson(Client client, Message message) {
        this.client = client;
        this.message = message;
    }

    @JsonProperty("User")
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @JsonProperty("Message")
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

class Client {

    private String secondName;

    private String name;

    public Client() {
    }

    public Client(String secondName, String name) {
        this.secondName = secondName;
        this.name = name;
    }

    @JsonProperty("SecondName")
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Message {

    private String body;

    private String timestamp;

    public Message() {
    }

    public Message(String body, String timestamp) {
        this.body = body;
        this.timestamp = timestamp;
    }

    @JsonProperty("Body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JsonProperty("Timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}