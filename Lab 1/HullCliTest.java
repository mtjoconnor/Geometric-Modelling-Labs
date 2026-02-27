import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HullCliTest {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.err.println("Usage: java HullCliTest <data-file>");
            System.exit(1);
        }

        MyPointSet pts = new MyPointSet();
        Scanner in = new Scanner(new File(args[0]));
        while (in.hasNextInt()) {
            int x = in.nextInt();
            if (!in.hasNextInt()) break;
            int y = in.nextInt();
            pts.addPoint(x, y);
        }
        in.close();

        Polygon hull = pts.hullThePolygon();
        System.out.println("HULL_NPOINTS=" + hull.npoints);
        for (int i = 0; i < hull.npoints; i++) {
            System.out.println("HULL_POINT_" + i + "=" + hull.xpoints[i] + "," + hull.ypoints[i]);
        }
    }
}
