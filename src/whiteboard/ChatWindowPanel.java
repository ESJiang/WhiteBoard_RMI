package whiteboard;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

public class ChatWindowPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private JLabel chatLabel;
    protected JTextArea chatBox;
    private JTextField textingBox;
    protected JButton sendButton;
    private JLabel activeUserLabel;
    protected JList<String> activeUser;
    private JButton kickOut;
    private WhiteboardGUI whiteBoard;
    private String username;


    public ChatWindowPanel(String username, WhiteboardGUI whiteboard)
    {
        this.whiteBoard = whiteboard;
        this.username = username;
        display();
    }

    private void display()
    {
        repaint();

        activeUserLabel = new JLabel("");
        activeUserLabel.setHorizontalAlignment(SwingConstants.LEFT);
        activeUserLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        JScrollPane jListScroll = new JScrollPane();
        activeUser = new JList<>();
        activeUser.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        jListScroll.setViewportView(activeUser);

        kickOut = new JButton("Kick Out");

        kickOut.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                String kickedUsername = activeUser.getSelectedValue();
                System.out.println("Kick user:" + kickedUsername +".");

                if  (kickedUsername == null)
                {
                    JOptionPane.showMessageDialog(null, "You need to select a user.", "Confirm", 0);
                }
                else
                {
                    try
                    {
                        whiteBoard.kickUser(kickedUsername);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });


        kickOut.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        JPanel chatPanel = new JPanel();
        this.add(chatPanel);

        chatLabel = new JLabel(" Chat Room");
        chatLabel.setHorizontalAlignment(SwingConstants.LEFT);
        chatLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        JScrollPane chatScrollPane = new JScrollPane();
        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        chatBox.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret)chatBox.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        chatScrollPane.setViewportView(chatBox);

        textingBox = new JTextField();
        textingBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        textingBox.setColumns(10);
        textingBox.requestFocusInWindow();

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        sendButton.addActionListener(new sendButtonListener());

        GroupLayout gl_chatPanel = new GroupLayout(chatPanel);
        gl_chatPanel.setHorizontalGroup(
            gl_chatPanel.createParallelGroup(Alignment.TRAILING)
            .addGroup(gl_chatPanel.createSequentialGroup()
                .addGroup(gl_chatPanel.createParallelGroup(Alignment.TRAILING)
                    .addComponent(activeUserLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(jListScroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(kickOut, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(chatLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(chatScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addGroup(gl_chatPanel.createSequentialGroup()
                        .addComponent(textingBox, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                        .addGap(1)
                        .addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))
                .addGap(0))
            );
        gl_chatPanel.setVerticalGroup(
            gl_chatPanel.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_chatPanel.createSequentialGroup()
                .addComponent(activeUserLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addGap(0)
                .addComponent(jListScroll, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addGap(1)
                .addComponent(kickOut, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addGap(20)
                .addComponent(chatLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addGap(0)
                .addComponent(chatScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(1)
                .addGroup(gl_chatPanel.createParallelGroup(Alignment.LEADING)
                    .addComponent(textingBox, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                .addGap(0))
            );

        chatPanel.setLayout(gl_chatPanel);

        if(!WhiteboardGUI.user.isHost())
        {
            kickOut.setEnabled(false);
        }
    }

    class sendButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if (textingBox.getText().length() > 0)
            {
                whiteBoard.sendMsg(textingBox.getText(), username);
                textingBox.setText("");
            }
            textingBox.requestFocusInWindow();
        }
    }

    public void setActiveUserLabel(String label)
    {
        activeUserLabel.setText(label);;
    }

    public JButton getButton() 
    {
        return sendButton;
    }
}
