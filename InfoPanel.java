import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
/**
 * Display the information of the program.
 *
 * @Destiny Zhu, Melory So, Coco Kneer
 * @05/15/2018
 */
public class InfoPanel extends JPanel
{
    
    /**
     * Constructor: creates the panel.
     */
    public InfoPanel()
    {
        setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
        ImageIcon icon = new ImageIcon("jia.png");
        add(new JLabel(icon));
        add(new JLabel("Create your personalized family tree."));
        add(new JLabel("See each of your family member's title."));
        add(new JLabel("------"));
        add(new JLabel("创造一个属于你的家谱。"));
        add(new JLabel("看看你应该如何称呼你的每一位家人。"));
        add(new JLabel("------"));
        add(new JLabel("Created by Destiny Zhu, Melory So, Coco Kneer"));
        setBackground(Color.white);
    }
}
