import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*********************/
/* class  ConvexHull*/                                                    
/*********************/

public class ConvexHull {
    
    public static final long serialVersionUID = 24362462L;
    Button bClear;
    Button bCH;
    Button bRead;
    Button bQuit;
    static Frame myFrame=null;
    Panel  mainPanel;
    private MyGraphics myG;
    public Color paintColor, hullColor;
    public int borderSize;
    

    public void clearMe() {
        Graphics g = myFrame.getGraphics();
        Dimension dimension = myFrame.getSize();
        Color col = g.getColor();
        g.setColor(myFrame.getBackground());
        g.fillRect(0, 0, dimension.width, dimension.height);
        g.setColor(col);
    }

    public void init() {

        borderSize = 10000;
        paintColor = Color.green;
        hullColor    = Color.blue;
        myFrame.setBackground(Color.gray);
        myFrame.setLayout(new BorderLayout());
        mainPanel  = new Panel();
        mainPanel.setBackground(Color.lightGray);
        Panel panel2 = new Panel();
        panel2.setBackground(Color.lightGray);
        bCH = new Button("Convex Hull");
        panel2.add(bCH);
        bRead = new Button("Read sample points");
        panel2.add(bRead);
        bClear = new Button("Clear all");
        panel2.add(bClear);
        
        if (myFrame != null)
        {
            bQuit = new Button("Quit");
            panel2.add(bQuit);
        }
        myFrame.add("North",  panel2);
        myFrame.add("South",  mainPanel);
        myG = new MyGraphics(paintColor, hullColor);

        //Convex hull button
        bCH.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Graphics g = myFrame.getGraphics();
                g.setColor(hullColor);
                myG.hullThePolygon(g);
                g.setPaintMode();
            }
        });

        //Read points button
        bRead.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x, y;

                File f = new File(".","sample.data");
                if (!f.exists()) throw new Error("Sample Point File Not Found"); 
                else {
                    // get rid of existing polygon
                    myG.clear(mainPanel, myFrame.getBackground());
                    // clear screen
                    Graphics g = myFrame.getGraphics();
                    g.setColor(paintColor);
                    clearMe();
                    // read in points from file
                    try{
                        Reader r = new BufferedReader(
                                   new InputStreamReader(
                                   new FileInputStream(f)));
                        StreamTokenizer st = new StreamTokenizer(r);
                        
                        int i=0;
                        while (st.nextToken()!=StreamTokenizer.TT_EOF) {
                            x = (int)(st.nval);
                            st.nextToken();
                            y = (int)(st.nval);
                            System.out.println("Read point (" + x + " , "+y+")");
                            myG.addPolyPoint(g, x, y);
                        }                
                    }
                    catch(Exception exc){
                        System.out.println("Cannot read from file"+f);
                    }
                    g.setPaintMode();
                }
            }
        });
        
        //Clear button
        bClear.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // get rid of components
                myG.clear(mainPanel, myFrame.getBackground());
                // clear screen
                Graphics g = myFrame.getGraphics();
                g.setColor(paintColor);
                //System.out.println("Clear all");
                clearMe();
                g.setPaintMode();
            }
        });
        
        //Quit button
        bQuit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });


        //Click on the canvas listener...
        myFrame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Graphics g = myFrame.getGraphics();
                g.setColor(paintColor);
                int x = e.getX();
                int y = e.getY();
                // System.out.println("Cick at "+x+" "+y);
                myG.addPolyPoint(g, x, y);
                g.setPaintMode();
            }
        });
        // Dispose the window if the X button is clicked.
        myFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                myFrame.dispose();
            }
        });
    }

    /****************************************************************************/
    /* a standard overwriting of update()                                       */
    /****************************************************************************/
    public void update(Graphics g) { 
        paint(g); 
    }
    
    public void paint(Graphics g) {
        myFrame.paintComponents(g); 
        myG.drawPoints(g);
    }

    public static void main(String[] args) {
        ConvexHull myConvexHullApplet = new ConvexHull(); 

        myFrame = new Frame("Convex hull application"); // create frame with title
        myConvexHullApplet.init();
        myFrame.pack(); // set window to appropriate size (for its elements)
        myFrame.setSize(600, 500);
        myFrame.setVisible(true); // usual step to make frame visible
    }

}
