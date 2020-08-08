package server;
import java.util.EventObject;

public class ChatServerEvent extends EventObject
{
    private static final long serialVersionUID = 1L;
    private String message;
    public ChatServerEvent(Object src, String message)
    {
        super(src);
        setMessage(message);
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}

