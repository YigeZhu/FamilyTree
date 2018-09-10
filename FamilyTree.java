import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * The FamilyTree Program.
 *
 * @Destiny Zhu, Melory So, Coco Kneer
 * @05/15/2018
 */
public class FamilyTree
{
    private static Family family;
    
    public static void main(String[] args){
        
        family = new Family();
        
        JFrame frame = new JFrame ("Family Tree");
        //frame.setSize(1000,1000);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        
        InfoPanel panel1 = new InfoPanel();
        tabbedPane.addTab("About",panel1);
        
        AddMemberPanel panel2 = new AddMemberPanel(family);
        //InfoPanel panel2 = new InfoPanel();
        tabbedPane.addTab("Add Member",panel2);
        
        TreePanel tree = new TreePanel(family);
        tabbedPane.addTab("Tree",tree);
        
        frame.getContentPane().add(tabbedPane);
        frame.pack();
        frame.setVisible(true);
    }
}
