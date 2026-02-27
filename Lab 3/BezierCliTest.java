import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BezierCliTest {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.err.println("Usage: java BezierCliTest <data-file> <elevations>");
            System.exit(1);
        }

        int elevations = Integer.parseInt(args[1]);
        if (elevations < 0) {
            System.err.println("elevations must be non-negative");
            System.exit(2);
        }

        MyPolygon poly = new MyPolygon();
        Scanner in = new Scanner(new File(args[0]));
        while (in.hasNextInt()) {
            int x = in.nextInt();
            if (!in.hasNextInt()) break;
            int y = in.nextInt();
            poly.addPoint(x, y);
        }
        in.close();

        for (int i = 0; i < elevations; i++) {
            poly.elevateOnce();
        }

        if (elevations == 0 || !poly.elevated) {
            System.out.println("ELEVATED_NPOINTS=0");
            return;
        }

        System.out.println("ELEVATED_NPOINTS=" + poly.Elevated.npoints);
        System.out.println("FIRST=" + poly.Elevated.xpoints[0] + "," + poly.Elevated.ypoints[0]);
        System.out.println("LAST=" + poly.Elevated.xpoints[poly.Elevated.npoints - 1] + ","
                + poly.Elevated.ypoints[poly.Elevated.npoints - 1]);
    }
}
