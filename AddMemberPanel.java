import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static java.awt.geom.AffineTransform.*;
import java.awt.geom.AffineTransform;
/**
 * Represents the AddMember panel for the FamilyTree program.
 *
 * @Destiny Zhu, Melory So, Coco Kneer
 * @05/15/2018
 */
public class AddMemberPanel extends JPanel
{
    private Family family;
    private JPanel box1,box2,box3,box4,name;
    private JLabel l1,l2,l3,l4,l5,l6,l7,l8,label;
    private JTextField fName,lName,bYear,profileImage;
    private JComboBox bDay, bMonth,gender,addMethod,members,members2;
    private JButton button,newButton,titleButton,clearButton,newClear;
    private final String[] GENDERS = {"...","female","male"};
    private final String[] DAYS = {"...","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18",
                                   "19","20","21","22","23","24","25","26","27","28","29","30","31"};
    private final String[] MONTHS = {"...","January","February","March","April","May","June","July","August","September",
        "October","November","December"}; 
    private final String[] OPTIONS = {"...","Add Parent","Add Child", "Add Sibling", "Add Spouse"};
    /**
     * Constructor: creates the panel.
     */
    public AddMemberPanel(Family family)
    {
        this.family = family;
        
        setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        
        label = new JLabel("Fill in the information to add yourself, then click \"Add Me\".");
        
        box1 = new JPanel();
        box1.add(label);
        add(box1);
        box1.setBackground(Color.white);
        
        box2 = new JPanel();
        //box2.setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
        l1 = new JLabel("First Name:");
        l2 = new JLabel("Last Name:");
        l3 = new JLabel("Birth Day:");
        l4 = new JLabel("Birth Month:");
        l5 = new JLabel("Birth Year:");
        l6 = new JLabel("Gender");
        l7 = new JLabel("Profile Image");
        fName = new JTextField(10);
        lName = new JTextField(10);
        gender = new JComboBox(GENDERS);
        bYear = new JTextField(5);
        bMonth = new JComboBox(MONTHS);
        bDay = new JComboBox(DAYS);
        profileImage = new JTextField(10);
        button = new JButton("Add Me");
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ButtonListener());
        button.addActionListener(new ButtonListener());
        box2.add(l3);box2.add(bDay);
        box2.add(l4);box2.add(bMonth);
        box2.add(l5);box2.add(bYear);
        box2.add(l6);box2.add(gender);
        box2.add(l7);box2.add(profileImage);
        name = new JPanel();
        name.add(l1);name.add(fName);
        name.add(l2);name.add(lName);
        add(name);name.setBackground(Color.white);
        add(box2);box2.setBackground(Color.white);
        JPanel b = new JPanel();
        b.add(button);b.add(clearButton);
        add(b);b.setBackground(Color.white);
    }

    private int bMonthConversion(String bMonth){
        int result = 0;
        switch (bMonth) {
            case "January": result = 1;break;
            case "February": result = 2;break;                    
            case "March": result = 3;break;                            
            case "April": result = 4;break;                             
            case "May": result = 5;break;                       
            case "June": result = 6;break;                         
            case "July": result = 7;break;                         
            case "August": result = 8;break;                            
            case "September": result = 9;break;                              
            case "October": result = 10;break;                           
            case "November": result = 11;break;                             
            case "December": result = 12;break;                           
        }
        return result;
    }
    /**
     * Represents a listener for the button.
     */
    private class ButtonListener implements ActionListener
    //generate information based on the selected school information
    {   
        /**
         * Takes the event that is taken on the panel as an input and make certain changes to the panel 
         * based on that event. Represents when the button is pressed, a new School object is created and
         * added to the collection of School objects so far.
         * 
         * @param ActionEvent event an action that is taken on the panel, in this case we only care actions 
         * regarding button
         */
        public void actionPerformed (ActionEvent event){
            if(event.getSource() == button)
                //Addresses the user if not enough information is provided
                if (fName.getText().equals("")||lName.getText().equals("")||((String)bDay.getSelectedItem()).equals("...")||
                   ((String)bMonth.getSelectedItem()).equals("...")||((String)gender.getSelectedItem()).equals("...")||
                   bYear.getText().equals("")||profileImage.getText().equals("")){
                    label.setText("Something's missing...Please make sure to fill in ALL the information.");
                }else{
                //create and add the user                  
                FamilyMember user = new FamilyMember(fName.getText(), lName.getText(), Integer.valueOf((String)bDay.getSelectedItem()),
                    (bMonthConversion((String)bMonth.getSelectedItem())),Integer.valueOf(bYear.getText()),(String)gender.getSelectedItem());
                user.setImage(profileImage.getText());
                family.setUser(user); 
                
                //clear the field
                fName.setText("");lName.setText("");bYear.setText("");profileImage.setText("");
                bDay.setSelectedIndex(0);bMonth.setSelectedIndex(0);gender.setSelectedIndex(0);
                
                label.setText("Fill in the information, then click \"Add\"");
                button.setVisible(false);clearButton.setVisible(false);
                newButton = new JButton("Add");
                newClear = new JButton("Clear");
                addMethod = new JComboBox(OPTIONS);
                members = new JComboBox(family.getMembersArray());
                box3 = new JPanel();
                box3.add(new JLabel("Relationship"));
                box3.add(addMethod);
                newButton.addActionListener(new ButtonListener());
                newClear.addActionListener(new ButtonListener());
                box3.add(new JLabel("Select Existing Member"));
                box3.add(members);
                add(box3);box3.setBackground(Color.white);
                JPanel b2 = new JPanel();
                b2.add(newButton);b2.add(newClear);
                add(b2);
                
                box4 = new JPanel();
                members2 = new JComboBox(family.getMembersArray());
                l8 = new JLabel();
                titleButton = new JButton("Get Title");
                titleButton.addActionListener(new ButtonListener());
                box4.add(members2);
                box4.add(titleButton);
                box4.add(l8);box4.setBackground(Color.white);
                add(box4);
                }
                
            if(event.getSource() == newButton){
                if (fName.getText().equals("")||lName.getText().equals("")||((String)bDay.getSelectedItem()).equals("...")||
                   ((String)bMonth.getSelectedItem()).equals("...")||((String)gender.getSelectedItem()).equals("...")||
                   bYear.getText().equals("")||profileImage.getText().equals("")||((String)addMethod.getSelectedItem()).equals("...")||
                                                               ((String)members.getSelectedItem()).equals("...") ){
                    label.setText("Something's missing...Please make sure to fill in ALL the information.");
                }else{
                FamilyMember temp = new FamilyMember(fName.getText(), lName.getText(), Integer.valueOf((String)bDay.getSelectedItem()),
                    (bMonthConversion((String)bMonth.getSelectedItem())),Integer.valueOf(bYear.getText()),(String)gender.getSelectedItem());
                temp.setImage(profileImage.getText());
                FamilyMember current = family.getMember(((String)members.getSelectedItem()));
                switch((String)addMethod.getSelectedItem()){
                    case "Add Parent": family.addParent(current,temp);                                        
                                       break;
                    case "Add Sibling": family.addSibling(current,temp);                                        
                                       break;
                    case "Add Spouse": family.addSpouse(current,temp);                                        
                                       break;
                    case "Add Child": family.addChild(current,temp);                                        
                                       break;                   
                   }
                //clear the field
                fName.setText("");lName.setText("");bYear.setText("");profileImage.setText("");
                bDay.setSelectedIndex(0);bMonth.setSelectedIndex(0);gender.setSelectedIndex(0);addMethod.setSelectedIndex(0);
                
                label.setText("Fill in the information, then click \"Add\".");
                DefaultComboBoxModel model = new DefaultComboBoxModel(family.getMembersArray());
                DefaultComboBoxModel model2 = new DefaultComboBoxModel(family.getMembersArray());
                members.setModel(model);
                members2.setModel(model2);
                }    
        }
        
        if (event.getSource() == titleButton){
            //l8.setText("");
            FamilyMember temp = family.getMember(((String)members2.getSelectedItem()));
            l8.setText(family.getTitle(temp));
            ImageIcon icon = new ImageIcon(temp.getImage());
            l8.setIcon(icon);
        }
        
        if ((event.getSource() == clearButton)||(event.getSource() == newClear)){
            try{
            fName.setText("");lName.setText("");bYear.setText("");profileImage.setText("");
                bDay.setSelectedIndex(0);bMonth.setSelectedIndex(0);gender.setSelectedIndex(0);
                addMethod.setSelectedIndex(0);
                members.setSelectedIndex(0);members2.setSelectedIndex(0);
                l8.setIcon(null);l8.setText("");
            }catch (NullPointerException e){
            }
            }
        }
        
      }
    
    public static void main(String[] args){
        //System.out.println(bMonthConversion("May"));
    }
}
