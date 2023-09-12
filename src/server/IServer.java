package server;

import java.rmi.Remote;
import client.*;
import java.rmi.RemoteException;
import graph.Shape;

public interface IServer extends Remote {
    void regClient(IClient IClient) throws RemoteException;

    void unregClient(IClient IClient) throws RemoteException;

    void draw(Shape[] Shapes, int index) throws RemoteException;

    void broadcastMessage(String message) throws RemoteException;

    void broadcastClientList() throws RemoteException;

    void broadcastDraw(Shape[] shapes, int index) throws RemoteException;

    void disconnect(IClient client) throws RemoteException;

    void hostClosed() throws RemoteException;

    void drawNew() throws RemoteException;

    void addListener(ChatServerListener listener) throws RemoteException;

    int notifyListener(String msg) throws RemoteException;

    void notifyListenerQuit(String msg) throws RemoteException;
}