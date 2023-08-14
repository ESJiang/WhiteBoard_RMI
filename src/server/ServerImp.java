package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import graph.Shape;
import whiteboard.WhiteboardGUI;
import client.IClient;

class ServerImp extends UnicastRemoteObject implements IServer {
    private static final long serialVersionUID = 1L;
    private ArrayList<IClient> clientList;
    private List<ChatServerListener> listeners = new ArrayList<ChatServerListener>();
    public Shape[] shapes;
    public int index;

    ServerImp() throws RemoteException {
        this.clientList = new ArrayList<IClient>();
        this.shapes = new Shape[5000];
    }

    @Override
    public synchronized void regClient(IClient client) throws RemoteException {
        clientList.add(client);
    }

    @Override
    public synchronized void unregClient(IClient client) throws RemoteException {
        client.kickedByHost();
        clientList.remove(client);
        broadcastClientList();
        String msg = "<\"" + client.getUserName() + "\"" + " has been kicked." + ">\n";
        broadcastMessage(msg);
    }

    @Override
    public synchronized void draw(Shape[] shapes, int index) throws RemoteException {
        this.shapes = shapes;
        this.index = index + 1;
        broadcastDraw(shapes, index);
    }

    @Override
    public synchronized void broadcastDraw(Shape[] shapes, int index) throws RemoteException {
        for (int i = 0; i < clientList.size(); i++) {
            WhiteboardGUI.index = index;
            WhiteboardGUI.itemList = shapes;
            clientList.get(i).updatePanel(this.shapes, index);
        }
    }

    @Override
    public synchronized void drawNew() throws RemoteException {
        if (this.index != 0) {
            broadcastDraw(this.shapes, this.index - 1);
        }
    }

    @Override
    public synchronized void broadcastMessage(String message) throws RemoteException {
        System.out.println("broadcastMessage");
        for (int i = 0; i < clientList.size(); i++) {
            clientList.get(i).updateMessage(message);
        }
    }

    @Override
    public synchronized void broadcastClientList() throws RemoteException {
        for (int i = 0; i < clientList.size(); i++) {
            clientList.get(i).updateClient(clientList);
        }
    }

    @Override
    public synchronized void disconnect(IClient client) throws RemoteException {
        clientList.remove(client);
        broadcastClientList();
        String msg = "<\"" + client.getUserName() + "\"" + " has left the whiteboard." + ">\n";
        broadcastMessage(msg);
    }

    @Override
    public synchronized void hostClosed() throws RemoteException {
        System.out.println("clientList length:" + clientList.size());
        for (int i = 0; i < clientList.size(); i++) {
            clientList.get(i).hostClosed();
        }
    }

    @Override
    public synchronized int notifyListener(String msg) throws RemoteException {
        Iterator<ChatServerListener> itr = listeners.iterator();
        ChatServerEvent evt = new ChatServerEvent(this, msg);
        int n = 1;
        while (itr.hasNext()) {
            n = ((ChatServerListener) itr.next()).serverEvent(evt);
        }
        return n;
    }

    @Override
    public synchronized void notifyListenerQuit(String msg) throws RemoteException {
        Iterator<ChatServerListener> itr = listeners.iterator();
        ChatServerEvent evt = new ChatServerEvent(this, msg);
        while (itr.hasNext()) {
            ((ChatServerListener) itr.next()).serverEventQuit(evt);
        }
    }

    public void addListener(ChatServerListener listener) {
        listeners.add(listener);
    }
}