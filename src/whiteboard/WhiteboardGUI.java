package whiteboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import client.IClient;
import graph.Brush;
import graph.Circle;
import graph.Eraser;
import graph.FillCircle;
import graph.FillOval;
import graph.FillRect;
import graph.FillRoundRect;
import graph.Images;
import graph.Line;
import graph.Oval;
import graph.Pencil;
import graph.Rectangle;
import graph.RoundRect;
import graph.Shape;
import graph.Text;
import graph.Triangle;
import server.IServer;

public class WhiteboardGUI extends JFrame
{
    private static final long serialVersionUID = 1L;
    private static int saved = 0;
    private int lengthCount;
    private static String fontName = new String("Times New Roman");
    private static int fontSize = 20;
    public static int index = 0;
    public static Shape[] itemList = new Shape[10000];
    private static DrawPanel drawingPanel;
    private JLabel statusBar;
    private static int stroke = 2;
    protected static Color color1 = Color.black;
    protected static Color color2 = Color.white;
    private static int operationNum = 2;
    int length;
    private MyMenu menu;
    private ColorPanel colorPanel;
    private static File savedFile;
    private static ChatWindowPanel chatwindow;
    public static User user;
    public static ArrayList<User> userList;
    public static ArrayList<IClient> clientList;
    private static DefaultListModel<String> clientModel;

    public WhiteboardGUI(User user, IServer server)
    {
        WhiteboardGUI.user = user;
        init(user, server);
        setVisible(true);
    }

    public void init(User user, IServer server)
    {
        if(!user.isHost())
        {
            this.setTitle("Whiteboard - " + user.getUserName());
        }
        else
        {
            this.setTitle("Whiteboard - " + user.getUserName() + " (manager)");
        }
        this.setSize(1100, 750);
        this.setLocationRelativeTo(null);
        WhiteboardGUI.user = new User(user.getUserName());
        WhiteboardGUI.user.setClient(user.client);
        WhiteboardGUI.user.setHost(user.isHost());
        menu = new MyMenu(server, user.getUserName());
        new MyToolbar();
        colorPanel = new ColorPanel();
        add(colorPanel, BorderLayout.WEST);
        chatwindow = new ChatWindowPanel(user.getUserName(), this);
        add(chatwindow, BorderLayout.EAST);
        clientModel = new DefaultListModel<>();
        try
        {
            clientModel.addElement(WhiteboardGUI.user.client.getUserName());
        }
        catch (RemoteException e1)
        {
            e1.printStackTrace();
        }
        chatwindow.activeUser.setModel(clientModel);

        try
        {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/themeicon.png"));
            Image image = imageIcon.getImage();
            this.setIconImage(image);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "wrong image");
        }

		// set up drawing area
        drawingPanel = new DrawPanel();
        this.add(drawingPanel, BorderLayout.CENTER);
        drawingPanel.setBackground(Color.white);
        drawingPanel.newGraphics();

		// set up status bar
        statusBar = new JLabel();
        this.add(statusBar, BorderLayout.SOUTH);
        statusBar.setText("Censor location ");

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                int exit = 0;
                if (saved == 0)
                {
                    if (user.isHost())
                    {
                        exit = JOptionPane.showConfirmDialog(null, "Quit without saving?", "Exit",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        exit = JOptionPane.showConfirmDialog(null, "Exit the whiteboard?", "Exit",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    }

                    if (exit == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            user.client.server.notifyListenerQuit(user.getUserName());
                        }
                        catch (RemoteException e2)
                        {
                            e2.printStackTrace();
                        }

                        if(user.isHost())
                        {
                            try
                            {
                                server.hostClosed();
                            }
                            catch (RemoteException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                        System.out.println("exit");
                        disconnect();
                        setVisible(false);
                        dispose();
                        System.exit(0);
                    }
                }
                else
                {
                    try
                    {
                        user.client.server.notifyListenerQuit(user.getUserName());
                    }
                    catch (RemoteException e2)
                    {
                        e2.printStackTrace();
                    }

                    if(user.isHost())
                    {
                        try
                        {
                            server.hostClosed();
                        }
                        catch (RemoteException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    disconnect();
                    setVisible(false);
                    dispose();
                    System.exit(0);
                }
            }
        });
        this.setDefaultCloseOperation(0);
        getRootPane().setDefaultButton(chatwindow.getButton());
        try
        {
            user.client.server.broadcastClientList();
        }
        catch (RemoteException e1)
        {
            e1.printStackTrace();
        }
    }

    class DrawPanel extends JPanel
    {
        private static final long serialVersionUID = 1L;
        public DrawPanel()
        {
            this.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    itemList[index].x1 = itemList[index].x2 = e.getX();
                    itemList[index].y1 = itemList[index].y2 = e.getY();
                    switch (operationNum)
                    {
                        case 2:
                        case 3:
                        case 4:
                        lengthCount = 0;
                        index++;
                        lengthCount++;
                        newGraphics();
                        break;
                        case 15:
                        operationNum = 2;
                        break;
                    }
                    statusBar.setText("Censor location: [" + e.getX() + "," + e.getY() + "]");
                }

                @Override
                public void mouseReleased(MouseEvent e)
                {
                    itemList[index].x2 = e.getX();
                    itemList[index].y2 = e.getY();

                    switch (operationNum)
                    {
                        case 2:
                        case 3:
                        case 4:
                        itemList[index].x1 = e.getX();
                        itemList[index].y1 = e.getY();
                        lengthCount++;
                        itemList[index].length = lengthCount;
                        break;
                    }
                    repaint();
                    index++;
                    newGraphics();
                    WhiteboardGUI.updatePanelOnServer(WhiteboardGUI.itemList, index);
                    statusBar.setText("Censor location: [" + e.getX() + "," + e.getY() + "]");
                }

                public void mouseEntered(MouseEvent e)
                {
                    statusBar.setText("Censor location: [" + e.getX() + "," + e.getY() + "]");
                }

                public void mouseExited(MouseEvent e)
                {
                    statusBar.setText("Censor location: [" + e.getX() + "," + e.getY() + "]");
                }
            });
            this.addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseDragged(MouseEvent e)
                {
                    itemList[index].x2 = e.getX();
                    itemList[index].y2 = e.getY();
                    switch (operationNum)
                    {
                        case 2:
                        case 3:
                        case 4:
                        itemList[index - 1].x1 = itemList[index].x2 = itemList[index].x1 = e.getX();
                        itemList[index - 1].y1 = itemList[index].y2 = itemList[index].y1 = e.getY();
                        index++;
                        lengthCount++;
                        newGraphics();
                        break;
                    }
                    repaint();
                    statusBar.setText("Censor location: [" + e.getX() + "," + e.getY() + "]");
                }

                public void mouseMoved(MouseEvent e)
                {
                    statusBar.setText("Censor location: [" + e.getX() + "," + e.getY() + "]");
                }
            });
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for (int j = 0; j <= index; j++)
            {
                draw(g2d, itemList[j]);
            }
        }

        void draw(Graphics2D g2d, Shape itemList)
        {
            itemList.draw(g2d);
        }

        public void newGraphics()
        {
            // set cursor image for eraser
            switch (operationNum)
            {
                case 4:
                try
                {
                    Image cursorImage = new ImageIcon(getClass().getResource("/image/cursor.png")).getImage();
                    Cursor eraserCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(15, 15),
                      "norm");
                    drawingPanel.setCursor(eraserCursor);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, "abnormal cursor");
                }
                break;

				// set cursor for text input
                case 15:
                drawingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                break;
				// set default cursor
                default:
                drawingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                break;
            }

            if (operationNum == 0)
                itemList[index] = new Images();
            if (operationNum == 2)
                itemList[index] = new Pencil();
            if (operationNum == 3)
                itemList[index] = new Brush();
            if (operationNum == 4)
                itemList[index] = new Eraser();
            if (operationNum == 5)
                itemList[index] = new Line();
            if (operationNum == 6)
                itemList[index] = new Oval();
            if (operationNum == 7)
                itemList[index] = new Circle();
            if (operationNum == 8)
                itemList[index] = new Rectangle();
            if (operationNum == 9)
                itemList[index] = new RoundRect();
            if (operationNum == 10)
                itemList[index] = new Triangle();
            if (operationNum == 11)
                itemList[index] = new FillOval();
            if (operationNum == 12)
                itemList[index] = new FillCircle();
            if (operationNum == 13)
                itemList[index] = new FillRect();
            if (operationNum == 14)
                itemList[index] = new FillRoundRect();
            if (operationNum == 15) {
                itemList[index] = new Text();
                itemList[index].text = JOptionPane.showInputDialog("Please enter your texts");
                itemList[index].fontName = fontName;
                itemList[index].fontSize = fontSize;
            }

            itemList[index].stroke = stroke;
            addMouseListener(new MouseListener()
            {
                public void mousePressed(MouseEvent me)
                {
                    if (me.getButton() == MouseEvent.BUTTON1)
                    {
                        itemList[index].color = color1;
                    }
					// if(me.getButton() == MouseEvent.BUTTON2) {
					// label.setText("Middle Click!");
					// }
                    if (me.getButton() == MouseEvent.BUTTON3)
                    {
                      itemList[index].color = color2;
                  }
              }

              public void mouseReleased(MouseEvent me) {
              }

              public void mouseEntered(MouseEvent me) {
              }

              public void mouseExited(MouseEvent me) {
              }

              public void mouseClicked(MouseEvent me) {
              }
          });
        }
    }

    class MyMenu
    {
        private JMenuBar jMenuBar;
        private JMenu fileMenu, helpMenu, strokeMenu;
        private JMenuItem newFile, openFile, saveFile, saveAs, exitFile, info, use;
        private JMenuItem[] strokeList;
        private String strokes[] = { "/image/stroke1.png", "/image/stroke2.png", "/image/stroke3.png",
        "/image/stroke4.png" };

        public MyMenu(IServer r, String username)
        {
            addMenu(r, username);
        }

        public void addMenu(IServer r, String username)
        {
            jMenuBar = new JMenuBar();
            setJMenuBar(jMenuBar);

			// setup file menu
            fileMenu = new JMenu("File");
            jMenuBar.add(fileMenu);
            newFile = new JMenuItem("New", new ImageIcon(getClass().getResource("/image/new.png")));
            openFile = new JMenuItem("Open", new ImageIcon(getClass().getResource("/image/open.png")));
            saveFile = new JMenuItem("Save", new ImageIcon(getClass().getResource("/image/save.png")));
            saveAs = new JMenuItem("Save As", new ImageIcon(getClass().getResource("/image/saveAs.png")));
            exitFile = new JMenuItem("Exit", new ImageIcon(getClass().getResource("/image/exit.png")));
            fileMenu.add(newFile);
            fileMenu.add(openFile);
            fileMenu.add(saveFile);
            fileMenu.add(saveAs);
            fileMenu.add(exitFile);

			// System.out.println("isHost:" + WhiteboardGUI.user.isHost());

            if (!WhiteboardGUI.user.isHost())
            {
                newFile.setEnabled(false);
                openFile.setEnabled(false);
                saveFile.setEnabled(false);
                saveAs.setEnabled(false);
            }

            newFile.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    switch (saved)
                    {
                        case 1:
                        menu.newFile();
                        WhiteboardGUI.updatePanelOnServer(WhiteboardGUI.itemList, index);
                        break;
                        case 0:
                        int confirmNew = JOptionPane.showConfirmDialog(null, "Do you want to save changes?", "New File",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        if (confirmNew == 1)
                        {
                            menu.newFile();
                            WhiteboardGUI.updatePanelOnServer(WhiteboardGUI.itemList, index);
                        }
                        break;
                    }
                }
            });

            saveFile.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    menu.saveFile();
                }
            });

            saveAs.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    menu.saveAsFile(WhiteboardGUI.itemList, index, drawingPanel);
                }
            });

            openFile.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    menu.openFile();
                    saved = 1;
                }
            });

            exitFile.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    int exit = 0;
                    if (saved == 0)
                    {
                        if (user.isHost())
                        {
                            exit = JOptionPane.showConfirmDialog(null, "Quit the server without saving?", "Exit",
                                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        }
                        else
                        {
                            exit = JOptionPane.showConfirmDialog(null, "Exit the whiteboard?", "Exit",
                                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        }

                        if (exit == JOptionPane.YES_OPTION)
                        {
                            try
                            {
                                user.client.server.notifyListenerQuit(user.getUserName());
                            }
                            catch (RemoteException e2)
                            {
                                e2.printStackTrace();
                            }

                            if(user.isHost())
                            {
                                try
                                {
                                    user.client.server.hostClosed();
                                }
                                catch (RemoteException e1)
                                {
                                    e1.printStackTrace();
                                }
                            }

                            System.out.println("exit");
                            disconnect();
                            setVisible(false);
                            dispose();
                            System.exit(0);
                        }
                    }
                    else
                    {
                        try
                        {
                            user.client.server.notifyListenerQuit(user.getUserName());
                        }
                        catch (RemoteException e2)
                        {
                         e2.printStackTrace();
                     }

                     if(user.isHost())
                     {
                         try
                         {
                            user.client.server.hostClosed();
                        } catch (RemoteException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    disconnect();
                    setVisible(false);
                    dispose();
                    System.exit(0);
                }
            }
        });

			// setup set menu
            strokeMenu = new JMenu("Thickness");
            jMenuBar.add(strokeMenu);
            strokeList = new JMenuItem[4];

            for (int i = 0; i < 4; i++)
            {
                strokeList[i] = new JMenuItem("", new ImageIcon(getClass().getResource(strokes[i])));
                strokeMenu.add(strokeList[i]);
                strokeList[i].addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        for (int j = 0; j < strokes.length; j++)
                        {
                            if (e.getSource() == strokeList[j])
                            {
                                stroke = 7 * j + 2;
                                //itemList[index].stroke = stroke;
                            }
                        }
                    }
                });
            }

            // setup help menu
            helpMenu = new JMenu("Help");
            jMenuBar.add(helpMenu);
            use = new JMenuItem("Manual");
            info = new JMenuItem("Developer");
            helpMenu.add(use);
            helpMenu.add(info);
            info.addActionListener(new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    JOptionPane.showMessageDialog(null,
                      "" + "Property:\n" + "The product is developed by\n" + "Friday615\n", "Property",
                      JOptionPane.PLAIN_MESSAGE);
                }
            });

            use.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    JOptionPane.showMessageDialog(null,
                      "" + "User Manual:\r\n" + "The product has the follwing features: \r\n"
                      + "（1）Drawing lines, ovals, circles, rectangles, rounded rectangles,\r\n"
                      + "triangles, and the filled shapes respectively.\r\n"
                      + "（2）Draw with pencil or brush with sixteen colors.\r\n"
                      + "（3）Set the thickness of the pen and the eraser.\r\n"
                      + "（4）Set the name and size of the font.\r\n" + ""
                      + "（5）Save, import and export files.\r\n",
                      "User Manual", JOptionPane.PLAIN_MESSAGE);
                }
            });
        }

        public void saveFile()
        {
            if (saved == 1)
            {
                try
                {
                    FileOutputStream fileOutputStream = new FileOutputStream(savedFile);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(itemList);
                    objectOutputStream.close();
                    fileOutputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                saveAsFile(itemList, index, drawingPanel);
            }
        }

        public void saveAsFile(Shape[] shapes, int index, DrawPanel panel)
        {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter wbFilter = new FileNameExtensionFilter("WhiteBoard (*.wb)", "wb");
            FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("JPG File(*.jpg)", "jpg");

            fileChooser.setFileFilter(wbFilter);
            fileChooser.setFileFilter(imgFilter);

            int option = fileChooser.showDialog(null, "Save");

            if (option == 0)
            {
                File file = fileChooser.getSelectedFile();
                String fileName = fileChooser.getName(file);
                FileOutputStream fileOutputStream;
                ObjectOutputStream objectOutputStream;
                try
                {
                    if (fileChooser.getFileFilter() == wbFilter)
                    {
                        if (!fileName.contains(".wb"))
                        {
                            file = new File(fileChooser.getCurrentDirectory(), fileName + ".wb");
                        }
                        fileOutputStream = new FileOutputStream(file);
                        objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(shapes);
                        objectOutputStream.close();
                        fileOutputStream.close();
                        savedFile = file;
                        saved = 1;

                    }
                    else if (fileChooser.getFileFilter() == imgFilter)
                    {
                        if (!fileName.contains(".jpg"))
                        {
                            file = new File(fileChooser.getCurrentDirectory(), fileName + ".jpg");
                        }
                        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(),
                            BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics2D = image.createGraphics();
                        panel.paintAll(graphics2D);
                        ImageIO.write(image, "jpg", file);

                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                saved = 0;
            }
        }

        public Shape[] openFile()
        {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter wbFilter = new FileNameExtensionFilter("WhiteBoardGUI File(*.wb)", "wb");
            fileChooser.setFileFilter(wbFilter);
            int option = fileChooser.showDialog(null, "Open");
            Shape[] shapes = new Shape[5000];
            if (option == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    File file = fileChooser.getSelectedFile();
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    shapes = (Shape[]) objectInputStream.readObject();
                    fileInputStream.close();
                    fileInputStream.close();

                    int counter = 0;
                    for (int i = 0; i < shapes.length; i++)
                    {
                        if (shapes[i].x1 != 0)
                        {
                            ++counter;
                        }
                        else
                        {
                            break;
                        }
                    }
                    saved = 1;
                    savedFile = file;
                    WhiteboardGUI.itemList = shapes;
                    WhiteboardGUI.index = counter;
                    WhiteboardGUI.updatePanelOnServer(shapes, index);

                }
                catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            return shapes;
        }

        public void newFile()
        {
            index = 0;
            operationNum = 2;
            color1 = Color.black;
            stroke = 1;
            drawingPanel.newGraphics();
            repaint();
        }
    }

    class MyToolbar
    {
        private JButton[] paintButton;
        private JComboBox<String> jfont_name;
        private JComboBox<String> jfont_size;
        private JToolBar toolbar;
        private String images[] = { "/image/save.png", "/image/refresh.png", "/image/pencil.png", "/image/brush.png",
        "/image/eraser.png", "/image/line.png", "/image/oval.png", "/image/circle.png", "/image/rectangle.png",
        "/image/rectangle2.png", "/image/triangle.png", "/image/oval2.png", "/image/fillcircle.png",
        "/image/rectangle3.png", "/image/rectangle4.png", "/image/font.png", };
        private String tipText[] = { "save", "clear", "pencil", "brush", "eraser", "line", "oval", "circle",
        "rectangle", "round rectangle", "triangle", "filled oval", "filled circle", "filled rectangle",
        "filled round rectangle", "text", "stroke" };
        private String fontSizeList[] = { "20", "24", "28", "36", "42", "48", "60"};
        private String fontNameList[] = { "Arial", "Calibri", "Courier New", "Times New Roman", "Verdana", "Monospaced" };

        public MyToolbar()
        {
            setToorbar();
        }

        public void setToorbar()
        {
            paintButton = new JButton[images.length];
            toolbar = new JToolBar("toolbar");
            toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
            toolbar.setBackground(Color.lightGray);
            add(toolbar, BorderLayout.NORTH);

            jfont_size = new JComboBox<String>(fontSizeList);
            jfont_size.setPreferredSize(new Dimension(80, 30));
            jfont_name = new JComboBox<String>(fontNameList);
            jfont_name.setPreferredSize(new Dimension(130, 30));

            for (int i = 0; i < images.length; i++)
            {
                // System.out.println(images[i]);
                paintButton[i] = new JButton();
                paintButton[i].setIcon(new ImageIcon(getClass().getResource(images[i])));
                paintButton[i].setToolTipText(tipText[i]);
                paintButton[i].setPreferredSize(new Dimension(40, 40));
                paintButton[i].setBackground(Color.WHITE);
                toolbar.add(paintButton[i]);
            }

            if (!WhiteboardGUI.user.isHost())
            {
                paintButton[0].setEnabled(false);
                paintButton[1].setEnabled(false);
            }

            paintButton[0].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    menu.saveFile();
                    //saved = 1;
                }
            });

            paintButton[1].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    menu.newFile();
                    System.out.println("Clear");
                    WhiteboardGUI.updatePanelOnServer(WhiteboardGUI.itemList, index);
                }
            });

            for (int i = 2; i < images.length; i++)
            {
                paintButton[i].addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        for (int j = 0; j < images.length; j++)
                        {
                            if (e.getSource() == paintButton[j])
                            {
                                operationNum = j;
                                drawingPanel.newGraphics();
                                repaint();
                            }
                        }
                    }
                });
            }

            toolbar.add(jfont_size);
            jfont_size.addItemListener(new ItemListener()
            {
                public void itemStateChanged(ItemEvent e)
                {
                    fontSize = Integer.parseInt(fontSizeList[jfont_size.getSelectedIndex()]);
                }
            });

            toolbar.add(jfont_name);
            jfont_name.addItemListener(new ItemListener()
            {
                public void itemStateChanged(ItemEvent e)
                {
                    fontName = fontNameList[jfont_name.getSelectedIndex()];
                }
            });
        }
    }

    public static void updatePanel(Shape[] shapes, int index)
    {
        WhiteboardGUI.itemList = shapes;
        WhiteboardGUI.index = index;
        WhiteboardGUI.drawingPanel.repaint();
    }

    public static void updateNewPanel()
    {
        try
        {
            user.client.server.drawNew();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public static void updatePanelOnServer(Shape[] shapes, int index)
    {
        try
        {
            user.client.server.draw(shapes, index);

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        WhiteboardGUI.itemList = shapes;
        WhiteboardGUI.index = index;
    }

    public static void updateClient()
    {
        WhiteboardGUI.clientModel.removeAllElements();
        for (int i = 0; i < clientList.size(); i++)
        {
            try
            {
                WhiteboardGUI.clientModel.addElement(clientList.get(i).getUserName());
                chatwindow.setActiveUserLabel(" Active User: " + clientList.size());
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void updateMsg(String msg)
    {
        WhiteboardGUI.chatwindow.chatBox.append(msg);
    }

    public void sendMsg(String msg, String username)
    {
        try
        {
            user.client.server.broadcastMessage(username + ": " + msg + "\n");
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public static void disconnect()
    {
        try
        {
            user.client.server.disconnect(user.client);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public void kickUser(String userName) throws RemoteException
    {
        IClient kickedClient = null;
        for (int i = 0; i < clientList.size(); i++)
        {
            try
            {
                if (clientList.get(i).getUserName().equals(userName))
                {
                    kickedClient = clientList.get(i);
                }
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        if (WhiteboardGUI.user.getUserName().equals(kickedClient.getUserName()))
        {
            JOptionPane.showMessageDialog(null, "Cannot kick the manager.", "Confirm", 0);
            return;
        }
        System.out.println("kick username:" + kickedClient.getUserName());
        user.client.server.unregClient(kickedClient);
    }

    public static void windowClosed()
    {
        if (!user.isHost())
        {
            SwingUtilities.invokeLater(() ->
            {
                try
                {
                    user.client.server.notifyListenerQuit(user.getUserName());
                }
                catch (RemoteException e2)
                {
                    e2.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "You are kicked by the host.");
                System.exit(0);
            });
        }
    }

    public static void hostClosed()
    {
        if (!user.isHost())
        {
            SwingUtilities.invokeLater(() ->
            {
                JOptionPane.showMessageDialog(null, "Host is disconnected. Click \"OK\" to close the window.");
                System.exit(0);
            });
        }
    }
}