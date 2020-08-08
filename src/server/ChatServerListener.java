package server;
import java.util.EventListener;

public interface ChatServerListener extends EventListener
{
    int serverEvent(ChatServerEvent paramChatServerEvent);
    void serverEventQuit(ChatServerEvent paramChatServerEvent);
}
