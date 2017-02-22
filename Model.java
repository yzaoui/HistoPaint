
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private Graphics2D gc;
    private int canvasW, canvasH;
    private int oldX, oldY;
    private int strokeCount;
    private Color color;
    private BasicStroke stroke;
    private ArrayList<StrokeStruct> strokeRecords;
    private int strokeIndex;

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
        if (strokeIndex != strokeCount) {
            this.strokeRecords.subList(strokeIndex, strokeCount).clear();
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

        notifyObservers();
    }

    public int getStrokeIndex() {
        return strokeIndex;
    }

    public void setStrokeIndex(int index) {
        this.strokeIndex = index;

        gc.setColor(Color.white);
        gc.fillRect(0, 0, canvasW, canvasH);

        for (int i = 0; i < strokeIndex; i++) {
            StrokeStruct str = strokeRecords.get(i);
            gc.setColor(str.getColor());
            gc.setStroke(str.getStroke());

            int oldX = str.iterator().next().getX();
            int oldY = str.iterator().next().getY();

            for (StrokeStruct.Point p : str) {
                gc.drawLine(oldX, oldY, p.getX(), p.getY());
                oldX = p.getX();
                oldY = p.getY();
            }
        }

        notifyObservers();
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
}
