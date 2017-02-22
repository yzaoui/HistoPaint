
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private Graphics2D gc;
    private int canvasW, canvasH;
    private int oldX, oldY;
    private Color color;
    private BasicStroke stroke;
    private ArrayList<StrokeStruct> strokeRecords;
    private int strokeIndex;
    private int strokeCount;
    private int pointIndex;
    private int pointCount;
    private int maxPointsPerStroke;

    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList();
        this.strokeCount = 0;
        this.color = Color.black;
        this.stroke = new BasicStroke(3);
        this.strokeRecords = new ArrayList<>();
        this.strokeIndex = 0;
        this.strokeCount = 0;
        this.pointIndex = 0;
        this.pointCount = 0;
        this.maxPointsPerStroke = 0;
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    public void setGraphics(Graphics2D graphics, int width, int height) {
        this.gc = graphics;
        this.canvasW = width;
        this.canvasH = height;
    }

    public void strokeStart(int x, int y) {
        gc.setColor(this.color);
        gc.setStroke(this.stroke);
        gc.drawLine(x, y, x, y);

        this.oldX = x;
        this.oldY = y;

        //If not at the end, overwrite existing suffix array
        if (pointIndex != pointCount) {
            List<StrokeStruct.Point> points = strokeRecords.get(strokeIndex).getPoints();
            points.subList((pointIndex - strokeIndex * maxPointsPerStroke), points.size()).clear();
            this.strokeRecords.subList(strokeIndex + 1, strokeCount).clear();
        }

        this.strokeRecords.add(new StrokeStruct(x, y, this.color, this.stroke));

        notifyObservers();
    }

    public void strokeContinue(int x, int y) {
        gc.setColor(this.color);
        gc.setStroke(this.stroke);
        gc.drawLine(this.oldX, this.oldY, x, y);

        this.oldX = x;
        this.oldY = y;

        this.strokeRecords.get(strokeIndex).pushPoint(x, y);

        notifyObservers();
    }

    public void strokeEnd() {
        strokeIndex++;
        strokeCount = strokeRecords.size();

        int maxDelta = 0;
        for (StrokeStruct str : strokeRecords) {
            int pointsInStroke = str.getPoints().size();
            if (pointsInStroke > maxDelta) {
                maxDelta = pointsInStroke;
            }
        }
        maxPointsPerStroke = maxDelta;
        pointIndex = strokeIndex * maxPointsPerStroke;
        pointCount = pointIndex;

        notifyObservers();
    }

    public int getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(int index) {
        if (index != this.pointIndex) { //Avoid redrawing, not too necessary
            gc.setColor(Color.white);
            gc.fillRect(0, 0, canvasW, canvasH);

            this.pointIndex = index;
            if (index == 0) {
                this.strokeIndex = index;
            } else { //If there's anything to draw
                this.strokeIndex = (this.pointIndex - 1) / maxPointsPerStroke + 1;

                for (int i = 0; i < strokeIndex; i++) {
                    StrokeStruct str = strokeRecords.get(i);
                    gc.setColor(str.getColor());
                    gc.setStroke(str.getStroke());

                    //This is guaranteed to be a valid integer index in this stroke
                    int relativeEndPointIndex = (int)(Math.min(maxPointsPerStroke, pointIndex - i*maxPointsPerStroke) * str.getPoints().size() * 1.0 / maxPointsPerStroke);
                    List<StrokeStruct.Point> points = str.getPoints().subList(0, relativeEndPointIndex);

                    if (points.size() > 0) {
                        int oldX = points.get(0).getX();
                        int oldY = points.get(0).getY();

                        for (StrokeStruct.Point p : points) {
                            gc.drawLine(oldX, oldY, p.getX(), p.getY());
                            oldX = p.getX();
                            oldY = p.getY();
                        }
                    }
                }
            }

            notifyObservers();
        }
    }

    public float getStrokeWidth() {
        return stroke.getLineWidth();
    }

    public void setStrokeWidth(int newStrokeWidth) {
        this.stroke = new BasicStroke(newStrokeWidth);
        notifyObservers();
    }

    public void setColor(Color color) {
        this.color = color;
        notifyObservers();
    }

    public int getStrokeCount() {
        return strokeCount;
    }

    public int getPointsPerStroke() {
        return maxPointsPerStroke;
    }
}
