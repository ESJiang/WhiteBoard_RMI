package server;

import java.util.EventObject;

class ChatServerEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    private String message;

    ChatServerEvent(Object src, String message) {
        super(src);
        setMessage(message);
    }

    String getMessage() {
        return this.message;
    }

    void setMessage(String message) {
        this.message = message;
    }
}