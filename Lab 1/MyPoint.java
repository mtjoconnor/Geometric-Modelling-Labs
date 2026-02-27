import java.util.*;
import java.awt.geom.Line2D;

public class MyPoint {
   public int x;
   public int y;
    
   public MyPoint(int xc, int yc) {
      x = xc; y=yc;
   }
    
   public MyPoint(float xc, float yc) {
      x = (int)xc; y = (int)yc;
   }
    
   public boolean leftOn (MyPoint a, MyPoint b) {
   // is this point left of or on line ab ?
   //       System.out.println(" ... is ("+x+", "+y+") left of "+
   //                        "("+a.x+", "+a.y+") ("+b.x+", "+b.y+") ?");
   return (
      (b.x - a.x)*(  y - a.y) -
      (  x - a.x)*(b.y - a.y)
      <=
      0
      );
   }
    
    public boolean left (MyPoint a, MyPoint b) {
   // is this point left of line ab ?
   //       System.out.println(" ... is ("+x+", "+y+") left of "+
   //                        "("+a.x+", "+a.y+") ("+b.x+", "+b.y+") ?");
   return (
      (b.x - a.x)*(  y - a.y) -
      (  x - a.x)*(b.y - a.y)
      <
      0
      );
   }
    
    public boolean collinear (MyPoint a, MyPoint b) {
   // is this point left of line ab ?
   //       System.out.println(" ... is ("+x+", "+y+") left of "+
   //                        "("+a.x+", "+a.y+") ("+b.x+", "+b.y+") ?");
   return (
      (b.x - a.x)*(  y - a.y) -
      (  x - a.x)*(b.y - a.y)
      ==
      0
      );
   }
}

