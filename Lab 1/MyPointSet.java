import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.geom.Line2D;

public class MyPointSet extends Vector<MyPoint> {

    private int imin, imax, xmin, xmax;
    private boolean xySorted;
    private Vector<MyPoint>  theHull;
    public static final long serialVersionUID = 24362462L;
    public MyPointSet() {
       xySorted = false;
    }

    public void addPoint(int x, int y) {
       MyPoint p = new MyPoint(x,y);
       addElement(p);
       xySorted = false;
    }

    private int next(int i) {
       return (i = (i+1) % size());
    }

    private int previous(int i) {
       return (i = (i-1+size()) % size());
    }

    private int hullnext(int i) {
       return (i = (i+1) % theHull.size());
    }

    private int hullprevious(int i) {
       return (i = (i-1+theHull.size()) % theHull.size());
    }

    public void sortByXY() {
       int i;
       MyPoint p, q;
       boolean clean;

       // Lexicographic sort by x then y.
       if (size() < 2) {
           xySorted = true;
           return;
       }

       do {
           clean = true;
           for (i = 0; i < size() - 1; i++) {
               p = elementAt(i);
               q = elementAt(i + 1);
               if (p.x > q.x || (p.x == q.x && p.y > q.y)) {
                   setElementAt(q, i);
                   setElementAt(p, i + 1);
                   clean = false;
               }
           }
       } while (!clean);

       xySorted = true;

       //Here's some code that is useful for debugging 
       for (i=0; i<size(); i++) {
           p = elementAt(i);
           System.out.println(i+": "+p.x+" "+p.y);
       }
       
       return;
    }

    private boolean samePoint(MyPoint a, MyPoint b) {
        return a.x == b.x && a.y == b.y;
    }

    private boolean between(MyPoint a, MyPoint b, MyPoint c) {
        return b.x >= Math.min(a.x, c.x) && b.x <= Math.max(a.x, c.x)
            && b.y >= Math.min(a.y, c.y) && b.y <= Math.max(a.y, c.y);
    }

    private void simplifyHull() {
        if (theHull.size() < 2) return;

        // Remove adjacent duplicates.
        for (int i = 0; i < theHull.size() && theHull.size() > 1; i++) {
            int j = hullnext(i);
            if (samePoint(theHull.elementAt(i), theHull.elementAt(j))) {
                theHull.removeElementAt(j);
                i = -1;
            }
        }

        // Remove interior collinear points.
        boolean changed = true;
        while (changed && theHull.size() > 2) {
            changed = false;
            for (int i = 0; i < theHull.size(); i++) {
                int prev = hullprevious(i);
                int next = hullnext(i);
                MyPoint a = theHull.elementAt(prev);
                MyPoint b = theHull.elementAt(i);
                MyPoint c = theHull.elementAt(next);
                if (b.collinear(a, c) && between(a, b, c)) {
                    theHull.removeElementAt(i);
                    changed = true;
                    break;
                }
            }
        }
    }

    private void buildHullMonotonic(Vector<MyPoint> sorted) {
        Vector<MyPoint> lower = new Vector<MyPoint>(sorted.size(), sorted.size());
        Vector<MyPoint> upper = new Vector<MyPoint>(sorted.size(), sorted.size());

        for (int i = 0; i < sorted.size(); i++) {
            MyPoint p = sorted.elementAt(i);
            while (lower.size() >= 2) {
                MyPoint a = lower.elementAt(lower.size() - 2);
                MyPoint b = lower.elementAt(lower.size() - 1);
                if (!p.left(a, b)) lower.removeElementAt(lower.size() - 1);
                else break;
            }
            lower.addElement(p);
        }

        for (int i = sorted.size() - 1; i >= 0; i--) {
            MyPoint p = sorted.elementAt(i);
            while (upper.size() >= 2) {
                MyPoint a = upper.elementAt(upper.size() - 2);
                MyPoint b = upper.elementAt(upper.size() - 1);
                if (!p.left(a, b)) upper.removeElementAt(upper.size() - 1);
                else break;
            }
            upper.addElement(p);
        }

        theHull.clear();
        for (int i = 0; i < lower.size(); i++) theHull.addElement(lower.elementAt(i));
        for (int i = 1; i < upper.size() - 1; i++) theHull.addElement(upper.elementAt(i));
        simplifyHull();
    }

    private void enumerateHull() {  
       
       System.out.println("");
       System.out.print("Current chain is: ");
       for (int index=0; index<theHull.size(); index++) {
              MyPoint tmppoint;
           tmppoint = theHull.elementAt(index);
           System.out.print(" ("+tmppoint.x+", "+tmppoint.y+")");
       }
       System.out.println("");
    }

    private int removeChain(int bottom, int top) {
       // removes the chain between bottom+1 and top-1 inclusive
       // N.B. the size of the hull decreases by 1 at each step
       // returns the index of the last valid element

       int i, howmany;
       MyPoint q;

       System.out.println("  Removing chain between "+bottom+" and "+top+
                        " in hull of size "+theHull.size());

       if (bottom == top) return bottom; // nothing to remove

       if (bottom < top) {
           howmany = top-bottom-1;
           System.out.println("   0 I want "+howmany+" elements");
           for (i=0; i<howmany; i++) {
              q = theHull.elementAt(bottom+1);
              System.out.println("   0 Removing element at "+bottom+1+": ("+q.x+", "+q.y+")");
              theHull.removeElementAt(bottom+1);
           }
       }

       else { // top < bottom so wrap along chain end
           System.out.println(" \n  WRAPPING AROUND THE END \n");
           howmany = theHull.size()-bottom-1;
           System.out.println("   1 I want "+howmany+" elements between "+(bottom+1)+" and "+(theHull.size()-1)+" inclusive");
           for (i=0; i<howmany; i++) {
              q = theHull.elementAt(bottom+1);
              System.out.println("   1 Removing element at "+(bottom+1)+": ("+q.x+", "+q.y+")");
              theHull.removeElementAt(bottom+1);
           }
           howmany = top;
           System.out.println("   plus "+howmany+" elements between "+0+" and "+(top-1)+" inclusive");
           for (i=0; i<howmany; i++) {
              // could remove top-1 but then need to change top
              q = theHull.elementAt(0);
              System.out.println("   2 Removing element at "+0+": ("+q.x+", "+q.y+")");
              theHull.removeElementAt(0);
           }

           if (bottom >= theHull.size()) bottom = theHull.size()-1;
       }

       return bottom; // index of last valid element
    }
    
    private void hullIncremental() {    
       int k,n;
       MyPoint p, q;
       int top, bottom;
       
       n = size();

       if (n < 1) {
           System.out.println("\u0007Can't compute convex hull");
           return;
       }

       theHull   = new Vector<MyPoint>(n,n);

       if (!xySorted) sortByXY();

       // Build a duplicate-free sorted list.
       Vector<MyPoint> sorted = new Vector<MyPoint>(n,n);
       for (k = 0; k < n; k++) {
           q = elementAt(k);
           if (sorted.size() == 0 || !samePoint(q, sorted.lastElement())) {
               sorted.addElement(q);
           }
       }

       if (sorted.size() == 1) {
           theHull.addElement(sorted.elementAt(0));
           return;
       }

       if (sorted.size() == 2) {
           theHull.addElement(sorted.elementAt(0));
           theHull.addElement(sorted.elementAt(1));
           return;
       }

       // The incremental routine assumes a clear leftmost/rightmost progression.
       // If either extreme x-column has multiple points, use a robust fallback.
       if (sorted.elementAt(0).x == sorted.elementAt(1).x
           || sorted.elementAt(sorted.size() - 1).x == sorted.elementAt(sorted.size() - 2).x) {
           buildHullMonotonic(sorted);
           return;
       }

       // Find first non-collinear triple.
       int i2 = 2;
       while (i2 < sorted.size()
           && sorted.elementAt(i2).collinear(sorted.elementAt(0), sorted.elementAt(1))) {
           i2++;
       }

       // If all points are collinear, hull is just the two extremes.
       if (i2 == sorted.size()) {
           theHull.addElement(sorted.elementAt(0));
           theHull.addElement(sorted.lastElement());
           return;
       }

       // Initial triangle, stored anti-clockwise in the screen coordinate system.
       MyPoint a = sorted.elementAt(0);
       MyPoint b = sorted.elementAt(1);
       MyPoint c = sorted.elementAt(i2);
       if (c.left(a, b)) {
           theHull.addElement(a);
           theHull.addElement(b);
       } else {
           theHull.addElement(b);
           theHull.addElement(a);
       }
       theHull.addElement(c);

       // rightmost point in hull is at index 2 and is visible from the next point
       top = bottom = 2;

       // Add remaining points incrementally in increasing x order.
       for (k = i2 + 1; k < sorted.size(); k++) {
           p = sorted.elementAt(k);

           // Find lower tangent boundary by walking clockwise.
           while (theHull.size() > 1) {
               int prev = hullprevious(bottom);
               MyPoint prevelem = theHull.elementAt(prev);
               MyPoint botelem = theHull.elementAt(bottom);
               if (!p.leftOn(prevelem, botelem)) {
                   bottom = prev;
                   if (bottom == top) break;
               } else {
                   break;
               }
           }

           // Find upper tangent boundary by walking anti-clockwise.
           while (theHull.size() > 1) {
               int next = hullnext(top);
               MyPoint topelem = theHull.elementAt(top);
               MyPoint nextelem = theHull.elementAt(next);
               if (!p.leftOn(topelem, nextelem)) {
                   top = next;
                   if (top == bottom) break;
               } else {
                   break;
               }
           }

           // Remove visible chain and insert the new point.
           bottom = removeChain(bottom, top);
           int insertAt = bottom + 1;
           if (insertAt > theHull.size()) insertAt = theHull.size();
           theHull.insertElementAt(p, insertAt);
           top = bottom = insertAt;
           enumerateHull();
       }

       simplifyHull();
    }

    public Polygon hullDraw() {
       int i;
       MyPoint q;
       Polygon p;

       p = new Polygon();
       System.out.println("The Hull has size "+theHull.size());
       System.out.println("The Hull is:");
       for (i=0; i<theHull.size(); i++) {
           q = theHull.elementAt(i);
           System.out.println("-> ("+q.x+", "+q.y+")");
           p.addPoint(q.x,q.y);
       }       
       System.out.println("===================================");
       return p;
    }

    public Polygon hullThePolygon() {
       int i;
       Polygon chp;

       sortByXY();
       hullIncremental();
       chp = hullDraw();

       return chp;
    }
}
