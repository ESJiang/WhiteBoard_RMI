package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import graph.Shape;
import whiteboard.User;

public interface IClient extends Remote {
    void setUserName(String name) throws RemoteException;

    void updatePanel(Shape[] shapes, int index) throws RemoteException;

    void updateUsers(ArrayList<User> list) throws RemoteException;

    void updateClient(ArrayList<IClient> list) throws RemoteException;

    void updateMessage(String message) throws RemoteException;

    void kickedByHost() throws RemoteException;

    void hostClosed() throws RemoteException;

    String getUserName() throws RemoteException;
}