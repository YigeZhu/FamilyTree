import static java.awt.geom.AffineTransform.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.util.*;

/**
 * A customized JCompenent that takes a FamilyMember collection and draws a tree
 * according to it.
 */
public class DisplayTree extends JComponent {
    private final int ARR_SIZE = 4;
    private Family family;
    
    /**
     * Constructor
     * @param Family
     */
    public DisplayTree(Family family){
        this.family = family;
    }
    
    /**
     * Helper method that draws an arrow using the coordinates user input.
     * @param Graphics g1, int x1, int y1, int x2, int y2
     */
    private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
    
    /**
     * Draws the tree.
     */
    public void paintComponent(Graphics g) {
        LinkedList<FamilyMember> gen0,gen1,gen2;  
        gen0 = new LinkedList<FamilyMember>();
        gen1 = new LinkedList<FamilyMember>();
        gen2 = new LinkedList<FamilyMember>();
        for(FamilyMember member: family.getMembersLinkedList()){
            int gen = family.getGeneration(member,family.getUser());
            switch(gen){
                case 0: if (!gen0.contains(member)){
                        gen0.add(member);}
                        break;
                case 1: if (!gen1.contains(member)){
                        gen1.add(member);}
                        break;
                case 2: if (!gen2.contains(member)){
                        gen2.add(member);}
                        break;
            }
            FontMetrics fm = g.getFontMetrics();
            double textWidth;String s;
            int x = 50; int y;
            for (FamilyMember member0: gen0){
               y = 300;
               g.drawOval(x,y, 25, 25);
               s = member0.getName();
               textWidth = fm.getStringBounds(s, g).getWidth();
               g.drawString(s, (int) (x - textWidth/2), (y + fm.getMaxAscent() / 2));
               if (family.isSibling(family.getUser(),member0)){
                   g.drawLine(x,300,50,300);
                }
               x+=100;
            }
            x = 50; int mama = 0; int baba = 0;
            for (FamilyMember member1: gen1){
               y = 200;
               g.drawOval(x,y, 25, 25);
               s = member1.getName();
               textWidth = fm.getStringBounds(s, g).getWidth();
               g.drawString(s, (int) (x - textWidth/2), (y + fm.getMaxAscent() / 2));
               if ((family.isMother(family.getUser(),member1))||
                        family.isFather(family.getUser(),member1)){                            
                   drawArrow(g, x, 200, 50, 300);
                   if (family.isFemale(member1)) 
                       mama = x;
                   else 
                       baba = x;
                }
               String t = family.getTitle(member1);
               switch(t){
                  case "Gu Gu": g.drawLine(x,200,baba,200);
                  case "Bo Bo": g.drawLine(x,200,baba,200);
                  case "Jiu Jiu": g.drawLine(x,200,mama,200);
                  case "Yi Ma": g.drawLine(x,200,baba,200);
                }
               x+=100;
            }
            x = 50;
            for (FamilyMember member2: gen2){
               y = 100;
               g.drawOval(x,y, 25, 25);
               s = member2.getName();
               textWidth = fm.getStringBounds(s, g).getWidth();
               g.drawString(s, (int) (x - textWidth/2), (y + fm.getMaxAscent() / 2));
               String title = family.getTitle(member2);
               switch(title){
                   case "Wai Po": drawArrow(g, x, 100, mama, 200);break;
                   case "Wai Gong": drawArrow(g, x, 100, mama, 200);break;
                   case "Nai Nai": drawArrow(g, x, 100, baba, 200);break;
                   case "Ye Ye": drawArrow(g, x, 100, baba, 200);break;
                }
               x+=100;
            }
        }
    }
 }

