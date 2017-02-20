
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private Graphics2D gc;
    private int oldX, oldY;
    private int strokeCount;

    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList();
        this.strokeCount = 0;
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

    public void strokeStart(Color color, Stroke stroke, int x, int y) {
        gc.setColor(color);
        gc.setStroke(stroke);
        gc.drawLine(x, y, x, y);

        oldX = x;
        oldY = y;

        notifyObservers();
    }

    public void strokeContinue(Color color, Stroke stroke, int x, int y) {
        gc.setColor(color);
        gc.setStroke(stroke);
        gc.drawLine(oldX, oldY, x, y);

        oldX = x;
        oldY = y;

        notifyObservers();
    }

    public void strokeEnd() {
        strokeCount++;

        notifyObservers();
    }

    public int getStrokeCount() {
        return strokeCount;
    }
}
