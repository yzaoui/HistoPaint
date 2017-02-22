import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class StrokeStruct implements Iterable<StrokeStruct.Point> {
    private Stroke stroke;
    private Color color;
    private ArrayList<Point> points;

    public StrokeStruct(int x, int y, Color color, Stroke stroke) {
        this.color = color;
        this.stroke = stroke;

        this.points = new ArrayList<>();
        this.pushPoint(x, y);
    }

    public void pushPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    public Color getColor() {
        return this.color;
    }

    public Stroke getStroke() {
        return this.stroke;
    }

    public List<StrokeStruct.Point> getPoints() {
        return points;
    }

    @Override
    public Iterator<StrokeStruct.Point> iterator() {
        return points.iterator();
    }

    public class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
