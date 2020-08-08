package whiteboard;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import client.StartClient;
import server.CreateWhiteBoard;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JPanel;

public class WelcomeWindow
{
    private JFrame frame = new JFrame();
    private JTextField ip = new JTextField();;
    private JTextField port = new JTextField();;
    private JTextField username = new JTextField();
    private JButton launch = new JButton("Launch");
    private boolean isHost = true;

	//Launch the application.
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                	new WelcomeWindow().frame.setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    private WelcomeWindow()
    {
        frame.setTitle("Whiteboard Login");
        frame.setBounds(100, 100, 500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel ipLable = new JLabel("Server IP");
        ipLable.setFont(new Font("Segoe UI Light", Font.BOLD, 18));

        ip.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        ip.setColumns(10);

        JLabel portLabel = new JLabel("Port");
        portLabel.setFont(new Font("Segoe UI Light", Font.BOLD, 20));

        port.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        port.setColumns(10);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI Light", Font.BOLD, 20));

        username.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        username.setColumns(10);

        launch.setFont(new Font("Segoe UI Light", Font.BOLD, 20));
        frame.getRootPane().setDefaultButton(launch);

        JRadioButton createBT = new JRadioButton("Create a new whiteboard");
        createBT.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        JRadioButton joinBT = new JRadioButton("Join a whiteboard");
        joinBT.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        createBT.setSelected(true);

        ButtonGroup createOrJoinBG = new ButtonGroup();
        createOrJoinBG.add(joinBT);
        createOrJoinBG.add(createBT);

        try
        {
            InetAddress localIP = InetAddress.getLocalHost();
            //default ip is disabled
            ip.setEnabled(false);
            ip.setText(localIP.getHostAddress());

            createBT.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    isHost = true;
                    ip.setEnabled(false);
                    ip.setText(localIP.getHostAddress());
                }
            });
            joinBT.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    isHost = false;
                    ip.setEnabled(true);
                    ip.setText(null);
                }
            });
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }

        launch.addActionListener(e ->
        {
            int error = 0;
            String errorMessage = "";

        	//validate inputs
            if(!validate(ip.getText().trim()))
            {
                errorMessage += "Please enter an valid IP address.\n";
                error++;
            }

            try
            {
                int portNum = Integer.parseInt(port.getText().trim());
                if(portNum <= 0 || portNum > 65535)
                {
                    errorMessage += "Please enter a valid port number (1-65535).\n";
                    error++;
                }
            }
            catch (NumberFormatException e2)
            {
                if(port.getText().trim().isEmpty() || !(port.getText().trim().matches("[0-9]+")))
                {
                    errorMessage += "Please enter a valid port number (1-65535).\n";
                    error++;
                }
            }

            if(username.getText().trim().isEmpty())
            {
                errorMessage += "Please enter an username.";
                error++;
            }
            String[] args = {ip.getText().trim(), port.getText().trim(), username.getText().trim()};
            if(error == 0 && isHost == true)
            {
                try
                {
                    CreateWhiteBoard.main(args);
                    System.out.println("Welcome manager \"" + username.getText().trim() + "\"");
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                frame.dispose();
            }
            else if (error == 0 && isHost == false)
            {
                try
                {
                    StartClient.main(args);
                    System.out.println("Welcome client \"" + username.getText().trim() + "\"");
                }
                catch (RemoteException | MalformedURLException | NotBoundException e1)
                {
                    e1.printStackTrace();
                }
                frame.dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(frame, errorMessage, "whiteBoard", JOptionPane.INFORMATION_MESSAGE);
            }
        });

		//layout of welcome panel
        JPanel topPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup()
              .addGap(178)
              .addComponent(launch)
              .addGap(178))
            .addGroup(groupLayout.createSequentialGroup()
              .addGap(50)
              .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(userLabel)
                .addComponent(ipLable)
                .addComponent(portLabel))
              .addGap(20)
              .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                .addComponent(ip, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addComponent(port, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addComponent(username, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
              .addGap(50))
            .addComponent(topPanel, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
            );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup()
              .addComponent(topPanel, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
              .addGap(20)
              .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                .addComponent(ipLable)
                .addComponent(ip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
              .addGap(20)
              .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                .addComponent(portLabel)
                .addComponent(port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
              .addGap(20)
              .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                .addComponent(userLabel)
                .addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
              .addGap(30)
              .addComponent(launch)
              .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

		//layout of top Panel
        GroupLayout gl_topPanel = new GroupLayout(topPanel);
        gl_topPanel.setHorizontalGroup(
            gl_topPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_topPanel.createSequentialGroup()
              .addGap(20)
              .addComponent(createBT, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
              .addGap(10)
              .addComponent(joinBT)
              .addContainerGap(26, Short.MAX_VALUE))
            );
        gl_topPanel.setVerticalGroup(
            gl_topPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_topPanel.createSequentialGroup()
              .addGap(10)
              .addGroup(gl_topPanel.createParallelGroup(Alignment.LEADING)
                .addComponent(createBT)
                .addComponent(joinBT)))
            );
        topPanel.setLayout(gl_topPanel);
        frame.getContentPane().setLayout(groupLayout);
    }



    private static boolean validate(final String ip)
    {
        String localhost = "localhost";
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return (ip.matches(PATTERN)||ip.matches(localhost));
    };
}
