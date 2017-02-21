
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private Graphics2D gc;
    private int oldX, oldY;
    private int strokeCount;
    private Color color;
    private BasicStroke stroke;

    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList();
        this.strokeCount = 0;
        this.color = Color.black;
        this.stroke = new BasicStroke(3);
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

    public void setGraphics(Graphics2D graphics) {
        gc = graphics;
    }

    public void strokeStart(int x, int y) {
        gc.setColor(this.color);
        gc.setStroke(this.stroke);
        gc.drawLine(x, y, x, y);

        this.oldX = x;
        this.oldY = y;

        notifyObservers();
    }

    public void strokeContinue(int x, int y) {
        gc.setColor(this.color);
        gc.setStroke(this.stroke);
        gc.drawLine(this.oldX, this.oldY, x, y);

        this.oldX = x;
        this.oldY = y;

        notifyObservers();
    }

    public void strokeEnd() {
        strokeCount++;

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
