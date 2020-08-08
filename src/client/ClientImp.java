package client;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import graph.Shape;
import server.IServer;
import whiteboard.User;
import whiteboard.WhiteboardGUI;

public class ClientImp extends UnicastRemoteObject implements IClient
{
    private static final long serialVersionUID = 1L;
    public IServer server;
    protected String name;

    public ClientImp(IServer server, String name) throws RemoteException
    {
        super();
        this.server = server;
        this.name = name;
        server.regClient(this);
    }

    @Override
    public void updatePanel(Shape[] shapes,int index) throws RemoteException
    {
        WhiteboardGUI.updatePanel(shapes,index);
    }

    @Override
    public void updateUsers(ArrayList<User> userList) throws RemoteException
    {
        WhiteboardGUI.userList = userList;
    }

    @Override
    public void updateClient(ArrayList<IClient> clientList) throws RemoteException
    {
        WhiteboardGUI.clientList = clientList;
        System.out.println("Size of clientList:" + clientList.size());
        WhiteboardGUI.updateClient();
    }

    @Override
    public void updateMessage(String message) throws RemoteException
    {
        WhiteboardGUI.updateMsg(message);
    }

    @Override
    public void setUserName(String name) throws RemoteException
    {
        this.name = name;
    }

    @Override
    public String getUserName() throws RemoteException
    {
        return name;
    }

    @Override
    public void kickedByHost() throws RemoteException
    {
        WhiteboardGUI.windowClosed();
    }

    public void hostClosed() throws RemoteException
    {
        WhiteboardGUI.hostClosed();
    }
}