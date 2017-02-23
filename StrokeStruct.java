import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class StrokeStruct implements Iterable<StrokeStruct.Line>, Serializable {
    private int strokeWidth;
    private Color color;
    private ArrayList<Line> lines;

    public StrokeStruct(int x, int y, Color color, int strokeWidth) {
        this.color = color;
        this.strokeWidth = strokeWidth;

        this.lines = new ArrayList<>();
        this.pushLine(x, y, x, y);
    }

    public void pushLine(int x1, int y1, int x2, int y2) {
        lines.add(new Line(x1, y1, x2, y2));
    }

    public Color getColor() {
        return this.color;
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public List<StrokeStruct.Line> getLines() {
        return lines;
    }

    @Override
    public Iterator<StrokeStruct.Line> iterator() {
        return lines.iterator();
    }

    public class Line implements Serializable {
        int x1, y1, x2, y2;

        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public int getX1() {
            return x1;
        }

        public int getY1() {
            return y1;
        }

        public int getX2() {
            return x2;
        }

        public int getY2() {
            return y2;
        }
    }
}
