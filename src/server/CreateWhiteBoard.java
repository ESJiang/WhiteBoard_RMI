package server;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import client.StartClient;
import whiteboard.User;
import whiteboard.WhiteboardGUI;

public class CreateWhiteBoard extends JFrame implements ChatServerListener
{
    private JTable jg_table;
    private JScrollPane jsp;
    private Vector<List<String>> vect = new Vector<List<String>>();
    private Vector<String> list = new Vector<String>();
    private final String[] columnNames = { "Identification", "UserName", "Online?", "Time"};
    private static final long serialVersionUID = 1L;
    private static String[] client = new String[100];
    private int count = 1;
    private static int start = 0;
    private static String name;
    private static String url;

    public static void main(String[] args) throws IOException
    {
		//initilization arguments: "//localhost:5000/Friday615 5000 Tom"
        try
        {
            String address = "//" + args[0] + ":" + args[1] + "/" + "whiteboard";
            client[start] = args[2];
            IServer serverImp = new ServerImp();
            LocateRegistry.createRegistry(Integer.parseInt(args[1]));
            Naming.rebind(address, serverImp);
            CreateWhiteBoard frame = new CreateWhiteBoard(args[2]);
            CreateWhiteBoard.name = args[2];
            CreateWhiteBoard.url = address;
            serverImp.addListener(frame);
            frame.setTitle("Online peer list");
            frame.setDefaultCloseOperation(0);
            frame.setVisible(true);
            frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    int n = JOptionPane.showConfirmDialog(null, "Do you want to quit the server?", "Exit",
                      JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (n == 0)
                    {
                        System.exit(0);
                    }
                }
            });
            JOptionPane.showMessageDialog(null,"Welcome " + args[2] + "!\nYou are the manager of the whiteboard.",
                "Confirm", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Successfully creating a new server！");
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        User user = new User(name);
                        user.setHost(true);
                        user.client = StartClient.startNewClient(user.getUserName(), url);
                        new WhiteboardGUI(user,serverImp);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch(ExportException e)
        {
            System.out.println("The port is already used.");
            System.exit(0);
        }
        catch (MalformedURLException e)
        {
            System.out.println("Wrong URL or parsing issue");
            System.exit(0);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Wrong data type parsing");
            System.exit(0);
        }
        catch (RemoteException e)
        {
            System.out.println("Connection failed");
            System.exit(0);
        }
    }

    public CreateWhiteBoard()
    {

    }

    public CreateWhiteBoard(String s)
    {
        setDefaultCloseOperation(0);
        setBounds(1500, 1000, 800, 500);
        getContentPane().setLayout(new BorderLayout(0, 0));
        AbstractTableModel tm = new AbstractTableModel()
        {
            private static final long serialVersionUID = 1L;
            @Override
            public int getColumnCount()
            {
                return columnNames.length;
            }

            @Override
            public int getRowCount()
            {
                return vect.size();
            }

            @Override
            public Object getValueAt(int row, int column)
            {
                if (!vect.isEmpty())
                {
                    return ((Vector<String>) vect.elementAt(row)).elementAt(column);
                }
                else
                {
                    return null;
                }
            }

            @Override
            public String getColumnName(int column)
            {
                return columnNames[column];
            }
        };

        vect.removeAllElements();
        tm.fireTableStructureChanged();

        list.add("Manager");
        list.add(s);
        list.add("Yes");
        list.add(Calendar.getInstance().getTime().toString());
        vect.addElement(list);

        tm.fireTableStructureChanged();
        jg_table = new JTable(tm);
        jg_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jg_table.setCellSelectionEnabled(false);
        jg_table.setShowVerticalLines(true);
        jg_table.setShowHorizontalLines(true);
        setTableStyle(jg_table);

        jsp = new JScrollPane(jg_table);

        jsp.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                resizeTable(true);
            }
        });
        getContentPane().add(jsp, BorderLayout.CENTER);
    }

    public void resizeTable(boolean bool)
    {
        Dimension containerwidth = null;
        if (!bool)
        {
            containerwidth = jsp.getPreferredSize();
        }
        else
        {
            containerwidth = jsp.getSize();
        }
        int allwidth = jg_table.getIntercellSpacing().width;
        for (int j = 0; j < jg_table.getColumnCount(); j++)
        {
            int max = 0;
            for (int i = 0; i < jg_table.getRowCount(); i++)
            {
                int width = jg_table.getCellRenderer(i, j).getTableCellRendererComponent(jg_table,jg_table.
                    getValueAt(i, j), false, false, i, j).getPreferredSize().width;
                if (width > max)
                {
                    max = width;
                }
            }
            int headerwidth = jg_table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(jg_table,
                jg_table.getColumnModel().getColumn(j).getIdentifier(), false, false, -1, j).getPreferredSize().width;
            max += headerwidth;
            jg_table.getColumnModel().getColumn(j).setPreferredWidth(max);
            allwidth += max + jg_table.getIntercellSpacing().width;
        }
        allwidth += jg_table.getIntercellSpacing().width;
        if (allwidth > containerwidth.width)
        {
            jg_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        else
        {
            jg_table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
    }

    public void setTableStyle(JTable table)
    {
        DefaultTableCellRenderer r = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer r1 = new DefaultTableCellRenderer();
        r1.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class,r);
    }

    public Vector<List<String>> getVect()
    {
        return vect;
    }

    public JTable getJg_table()
    {
      return jg_table;
  }

  public synchronized int serverEvent(ChatServerEvent evt)
  {
      int n = JOptionPane.showConfirmDialog(null,
       evt.getMessage() + " wants to join the whiteboard.\nDo you agree?", "Confirm",
       JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
      if (n == 0)
      {
       if(!Arrays.asList(client).contains(String.valueOf(evt.getMessage())))
       {
        client[count] = String.valueOf(evt.getMessage());
        count++;
        Vector<String> list = new Vector<String>();
        list.add("Client");
        list.add(String.valueOf(evt.getMessage()) + "\n");
        list.add("Yes");
        String c = Calendar.getInstance().getTime().toString();
        list.add(c);
        getVect().add(list);
        ((AbstractTableModel)getJg_table().getModel()).fireTableDataChanged();
        JOptionPane.showMessageDialog(null,evt.getMessage() + " is registered.", "Confirm", 1);
    }
    else
    {
        JOptionPane.showMessageDialog(null,evt.getMessage() + "'s request has been refused due to a duplicate name.", "Confirm", 0);
        n = 1;
    }
}
else
{
   JOptionPane.showMessageDialog(null,evt.getMessage() + "'s request has been refused by the manager.", "Confirm", 0);
}
return n;
}

public synchronized void serverEventQuit(ChatServerEvent evt)
{
  Vector<String> list = new Vector<String>();
  list.add("Client");
  list.add(String.valueOf(evt.getMessage()) + "\n");
  list.add("No");
  String c = Calendar.getInstance().getTime().toString();
  list.add(c);
  getVect().add(list);
  ((AbstractTableModel)getJg_table().getModel()).fireTableDataChanged();
  //JOptionPane.showMessageDialog(null,evt.getMessage() + " has been removed from the user list table.", "Confirm", 1);
  client = delete(getIndex(client, String.valueOf(evt.getMessage())));
  --count;
}

public String[] delete(int value)
{
    String[] newData = new String[client.length - 1];
    for (int x = 0; x < value; x++)
    {
        newData[x] = client[x];
    }
    for (int x = value + 1; x < client.length; x++)
    {
        newData[x-1] = client[x];
    }
    return newData;
}

public int getIndex(String[] arr, String value)
{
    for (int i = 0; i < arr.length; i++)
    {
        if (arr[i].equals(value))
        {
            return i;
        }
    }
    return -1;
}
}