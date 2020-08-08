package whiteboard;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private JPanel paneldownchild;
    public static JButton bt1;
    public static JButton bt2;
    public static JButton btChosen;
    private BevelBorder bevel0;
    private BevelBorder bevel1;
    private JPanel defaultPanel;
    private JPanel Palette;

	// default color
    private Color[] colors =
    {
        new Color(255, 255, 255), new Color(0, 0, 0), new Color(191, 191, 191),
        new Color(128, 128, 128), new Color(207, 124, 137), new Color(136, 0, 21), new Color(248, 161, 164),
        new Color(237, 28, 36), new Color(255, 204, 169), new Color(255, 127, 39), new Color(255, 250, 153),
        new Color(255, 242, 0), new Color(152, 224, 173), new Color(34, 177, 76), new Color(148, 216, 246),
        new Color(0, 162, 232), new Color(170, 174, 235), new Color(63, 72, 204), new Color(218, 170, 219),
        new Color(163, 73, 164)
    };

    public ColorPanel()
    {
        addColorPanel();
        btChosen = bt1;
    }

    public void addColorPanel()
    {
        this.setPreferredSize(new Dimension(92, 380));
        this.setLayout(null);
        this.setBackground(new Color(241, 241, 241));

		// add child panel to default panel
        paneldownchild = new JPanel();
        paneldownchild.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        paneldownchild.setBounds(10, 10, 60, 360);
        paneldownchild.setToolTipText("color");
        this.add(paneldownchild);

		// button bevel
        bevel0 = new BevelBorder(0, Color.gray, Color.white);
        bevel1 = new BevelBorder(1, Color.gray, Color.white);

        defaultPanel = new JPanel();
        defaultPanel.setBackground(Color.white);
        defaultPanel.setLayout(null);
        defaultPanel.setBorder(bevel0);
        defaultPanel.setPreferredSize(new Dimension(60, 60));

		// buttons for two chosen colors
        bt1 = new JButton();
        bt1.setBounds(7, 7, 30, 30);
        bt1.setBorder(bevel0);
        bt1.setContentAreaFilled(false);
        bt1.setOpaque(true);
        bt1.setBackground(Color.black);
        bt1.setSize(30, 30);
        bt2 = new JButton();
        bt2.setBorder(bevel1);
        bt2.setContentAreaFilled(false);
        bt2.setOpaque(true);
        bt2.setBackground(Color.white);
        bt2.setBounds(23, 23, 30, 30);
        defaultPanel.add(bt1);
        defaultPanel.add(bt2);

        bt1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                btChosen = bt1;
            }
        });

        bt2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                btChosen = bt2;
            }
        });

		// palette contains 20 colors
        Palette = new JPanel();
        Palette.setBackground(new Color(195, 195, 195));
        Palette.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        Palette.setPreferredSize(new Dimension(60, 330));

        paneldownchild.add(defaultPanel);
        paneldownchild.add(Palette);

		// add color to palette
        for (int i = 0; i < 20; i++)
        {
            JButton addColor = new JButton();
            addColor.setPreferredSize(new Dimension(30, 30));
            addColor.setBorder(bevel0);
            addColor.setContentAreaFilled(false);
            addColor.setOpaque(true);
            addColor.setBackground(colors[i]);
			// RBG
            addColor.setToolTipText("(" + colors[i].getRed() + ", " + colors[i].getGreen() + ", " + colors[i].getBlue() + ")");
            addColor.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JButton jbt = (JButton) e.getSource();
                    Color c = jbt.getBackground();
                    btChosen.setBackground(c);

                    if (btChosen == bt1)
                    {
                        WhiteboardGUI.color1 = c;
                    }
                    else
                    {
                        WhiteboardGUI.color2 = c;
                    }
                }
            });
            Palette.add(addColor);
        }
    }
}