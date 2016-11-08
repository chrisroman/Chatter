package edu.cornell.chatter.HelperClasses;

public class ChatMessage {
    String name;
    String message;
    long timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String name, String message) {
        this.name = name;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
