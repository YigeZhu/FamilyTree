import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import static java.awt.geom.AffineTransform.*;
import java.awt.geom.AffineTransform;
/**
 * Represents the TreePanel in FamilyTree program.
 *
 * @Destiny Zhu, Coco Kneer, Melory So
 * @05/17/2018
 */
public class TreePanel extends JPanel
{
    private Family family;
    private JButton button;
    private DisplayTree tree;
    /**
     * Constructor
     * @param a Family collection
     */
    public TreePanel(Family family)
    {
        this.family = family;
        setLayout(new BorderLayout());
        tree = new DisplayTree(family);
        add(tree);
    }
    
}
