package whiteboard;
import java.io.Serializable;
import client.ClientImp;

public class User implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String ip,port;
    private String userName;
    private boolean isHost;
    public ClientImp client;

    public User(String ip, String port, String userName, int userID, boolean isHost, ClientImp client)
    {
        super();
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.isHost = isHost;
        this.client = client;
    }

    public User()
    {
        this.userName = "Guest";
    }

    public User(String name)
    {
        this.userName = name;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getUserName()
    {
        return userName;
    }


    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public boolean isHost()
    {
        return isHost;
    }

    public void setHost(boolean isHost)
    {
        this.isHost = isHost;
    }

    public ClientImp getClient()
    {
        return client;
    }

    public void setClient(ClientImp client)
    {
        this.client = client;
    }

    @Override
    public String toString()
    {
        return "User [ip=" + ip + ", port=" + port + ", userName=" + userName + ", isHost="
        + isHost + ", client=" + client + "]";
    }
}