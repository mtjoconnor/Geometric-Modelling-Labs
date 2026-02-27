import java.awt.*;
import java.awt.event.*;
import java.io.*;

/****************/
/* class Bezier */                                                    
/****************/

public class Bezier {
    
    public static final long serialVersionUID = 24362462L;
    static Frame myFrame=null;
    Button bClear;
    Button bRead;
    Button bElevate;
    Button bQuit;
    Panel  mainPanel;
    private MyGraphics myG;
    public Color controlPolyColor, bkColor, frameColor, elevationColor;
    public int borderSize;
    
    /*************************/
    /* Initialise the applet */
    /*************************/
    
    public Bezier()
    {
        borderSize = 10000;
        controlPolyColor = Color.blue;
        bkColor    = Color.white;
        frameColor = Color.lightGray;
        elevationColor = Color.red;
        bClear     = new Button("Clear all");
        bRead      = new Button("Read sample points");
        bElevate    = new Button("Elevate once");
    }

    public void clearMe() {
        Graphics g = myFrame.getGraphics();
        Dimension dimension = myFrame.getSize();
        Color col = g.getColor();
        g.setColor(myFrame.getBackground());
        g.fillRect(0, 0, dimension.width, dimension.height);
        g.setColor(col);
    }

    public void init() {
        myFrame.setBackground(Color.white);
        myFrame.setLayout(new BorderLayout());
        mainPanel  = new Panel();
        mainPanel.setBackground(frameColor);
        Panel panel2 = new Panel();
        panel2.setBackground(frameColor);
        bRead = new Button("Read sample points");
        panel2.add(bRead);
        bElevate = new Button("Elevate once");
        panel2.add(bElevate);
        bClear = new Button("Clear all");
        panel2.add(bClear);
        bQuit = new Button("Quit");
        panel2.add(bQuit);

        myFrame.add("North",  panel2);
        myFrame.add("South",  mainPanel);
        myG = new MyGraphics(controlPolyColor, elevationColor);
    

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
                    g.setColor(controlPolyColor);
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
        
        bElevate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Graphics g = myFrame.getGraphics();
                g.setColor(controlPolyColor);
                clearMe();
                myG.elevateOnce(g);
                g.setPaintMode();
          }
        });//elevate once

        bClear.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // get rid of components
                myG.clear(mainPanel, myFrame.getBackground());
                // clear screen
                Graphics g = myFrame.getGraphics();
                g.setColor(controlPolyColor);
                //System.out.println("Clear all");
                clearMe();
                g.setPaintMode();
            }
        });
        bQuit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        
        //Click in the canvas
        myFrame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Graphics g = myFrame.getGraphics();
                g.setColor(controlPolyColor);
                int x = e.getX();
                int y = e.getY();
                // System.out.println("Click at "+x+" "+y);
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
        myG.redrawThePolygon(g);
    }

    public void stop() {
        // Stop the bouncer thread if necessary.
        System.out.println("stop");
    }  

    public static void main(String[] args) {
        Bezier myBezier= new Bezier();
        myFrame = new Frame("Bezier degree application"); // create frame with title
        myBezier.init(); 
        myFrame.pack(); // set window to appropriate size (for its elements)
        myFrame.setSize(600, 500);
        myFrame.setVisible(true); // usual step to make frame visible
    }

}
