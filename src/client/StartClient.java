package client;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;
import server.IServer;
import whiteboard.User;
import whiteboard.WhiteboardGUI;

public class StartClient
{
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException
    {
        try
        {
            String address = "//" + args[0] + ":" + args[1] + "/" + "whiteboard";
            IServer r = (IServer) Naming.lookup(address);
            if ( r.notifyListener(args[2])== 0 )
            {
                ClientImp client = new ClientImp(r, args[2]);
                User user = new User(client.name);
                user.client = client;
                user.setUserName(client.name);
                new WhiteboardGUI(user,r);
                WhiteboardGUI.updateNewPanel();
                String msg = "<\"" + client.getUserName() + "\"" + " has joined the whiteboard." + ">\n";
                client.server.broadcastMessage(msg);
            }
            else
            {
                System.out.println("Host deny your request.");
                JOptionPane.showMessageDialog(null,"Your request has been rejected by the manager.", "Confirm", 0);
                System.exit(0);
            }
        }
        catch (RemoteException e)
        {
            System.out.println("Connection failed. Please check if the server is available or the port number is correct.");
            System.exit(0);
        }
        catch (MalformedURLException e)
        {
            System.out.println("Wrong URL or parsing issue.");
            System.exit(0);
        }
        catch (NotBoundException e)
        {
            System.out.println("Cannot look up the content in registry.");
            System.exit(0);
        }
    }

    public static ClientImp startNewClient(String username, String url) throws RemoteException, MalformedURLException, NotBoundException
    {
        IServer server = (IServer) Naming.lookup(url);
        System.out.println("Binding success！");
        ClientImp client = new ClientImp(server, username);
		//new Thread(client).start();
        return client;
    }
}