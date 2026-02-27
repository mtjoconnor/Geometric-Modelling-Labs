import java.awt.*;
import java.awt.geom.Line2D;

public class MyGraphics {

    public Color paintColor, hullColor;
    public int pointRadius;
    public MyPointSet thePoints;

    public MyGraphics(Color paint, Color bk)
    {
        pointRadius = 4;
        thePoints = new MyPointSet();
        paintColor = paint;
        hullColor = bk;
    }

    public void clear(Component component, Color paramColor)
    {
        thePoints = new MyPointSet();
        component.repaint();
    }

    private void drawPoint(Graphics g, int x, int y)
    {
        g.fillOval(x - pointRadius, y - pointRadius, 2*pointRadius, 2*pointRadius);
    }

    // Overloaded method to convert
    private void drawPoint(Graphics g, MyPoint p)
    {
        drawPoint(g, p.x, p.y);
    }

    public void addPolyPoint(Graphics g, int x, int y)
    {
        thePoints.addPoint(x,y);
        drawPoint(g, x, y);
        //System.out.println("addPolyPoint "+x+" "+y);
    }


    public void drawPoints(Graphics g)
    {
        Color color = g.getColor();
        g.setColor(paintColor);
        for (int i=0; i<thePoints.size(); i++) {
            System.out.println("i="+i+" x="+thePoints.elementAt(i).x+" y="+thePoints.elementAt(i).y);
            drawPoint(g, thePoints.elementAt(i));
        }
        g.setColor(color);
    }


    
    public void hullThePolygon(Graphics g) 
    {
        Polygon hull = thePoints.hullThePolygon();
        Color color = g.getColor();
        g.setColor(hullColor);
        g.drawPolygon(hull);
        for (int i=0; i<hull.npoints; i++) {
            drawPoint(g, hull.xpoints[i], hull.ypoints[i]);
        }
        g.setColor(color);
    }
}
