import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model implements Serializable {
    /** The observers that are watching this model for changes. */
    //Ties with Views are transient
    private transient List<Observer> observers;
    private transient Graphics2D gc;
    private transient BufferedImage img;
    private transient PlayTimer timer;
    //Properties of the model
    private int canvasW, canvasH;
    private int oldX, oldY;
    private Color color;
    private int strokeWidth;
    private ArrayList<StrokeStruct> strokeRecords;
    private int strokeIndex;
    private int strokeCount;
    private int lineIndex;
    private int lineCount;
    private int maxLinesPerStroke;
    private long lastUpdate;
    private double delay;
    private double excess;
    private boolean strokeStarted;
    private boolean inCanvas;

    /**
     * Create a new model.
     */
    public Model() {
        this.strokeCount = 0;
        this.color = Color.black;
        this.strokeWidth = 3;
        this.strokeRecords = new ArrayList<>();
        this.strokeIndex = 0;
        this.strokeCount = 0;
        this.lineIndex = 0;
        this.lineCount = 0;
        this.maxLinesPerStroke = 0;
        this.strokeStarted = false;
        this.inCanvas = false;
        this.canvasW = 400;
        this.canvasH = 300;
        this.initTransient();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.initTransient();
    }

    private void initTransient() {
        this.observers = new ArrayList();
        this.timer = new PlayTimer(0, (ActionEvent e) -> {
            int toSkip = 0; //Number of lines to skip

            if (excess <= 0) { //If there is time debt
                toSkip = -1 * (int)(excess / delay); //Skip as many cycles as there are in the debt
                excess = excess % delay; //Remove all full cycles from debt
            } else if (excess > delay) { //If there is time surplus
                toSkip = -1; //Since the player should never go backwards, at most stay at current frame
                excess -= delay; //Remove a cycle at a time from surplus
            }
            //Otherwise, there is not enough surplus for a cycle, so play normally

            int index = Math.min(this.getLineIndex() + 1 + toSkip, this.getLineCount());
            this.setLineIndex(index);

            if (index >= this.getLineCount()) {
                //If player is at the end, stop playback
                this.stopPlayback();
            } else {
                //Add surplus/debt since last frame
                excess += delay - (int)(System.currentTimeMillis() - lastUpdate);
                lastUpdate = System.currentTimeMillis();
            }
        });
        this.img = new BufferedImage(canvasW, canvasH, BufferedImage.TYPE_INT_ARGB);
        this.gc = this.img.createGraphics();
        this.setLineIndex(this.lineIndex); //Do this because Graphics2d is not serializable
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

    public BufferedImage getBufferedImage() {
        return this.img;
    }

    public void strokeStart(int x, int y) {
        if (this.isPlaying()) { return; } //Disallow drawing during playback
        gc.setColor(this.color);
        gc.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gc.drawLine(x, y, x, y);

        this.oldX = x;
        this.oldY = y;

        //If not at the end, overwrite existing suffix array
        if (lineIndex != lineCount) {
            if (lineIndex > 0) {
                List<StrokeStruct.Line> lines = strokeRecords.get(strokeIndex - 1).getLines();
                lines.subList((int)((lineIndex - (strokeIndex - 1) * maxLinesPerStroke) * ((double)lines.size() / maxLinesPerStroke)), lines.size()).clear();
            }
            this.strokeRecords.subList(strokeIndex, strokeCount).clear();
        }

        this.strokeRecords.add(new StrokeStruct(x, y, this.color, this.strokeWidth));
        this.strokeStarted = true;
        this.inCanvas = true;

        notifyObservers();
    }

    public void strokeContinue(int x, int y) {
        if (!strokeStarted) { return; } //If not in a legal stroke
        if (!(x >= 0 && x < canvasW && y >= 0 && y < canvasH)) { //Only draw in canvas
            this.inCanvas = false;
            return;
        }
        if (!inCanvas) { //If just coming back from the outside, consider this like a new stroke
            this.inCanvas = true;
            this.oldX = x;
            this.oldY = y;
        }
        gc.drawLine(this.oldX, this.oldY, x, y);

        this.strokeRecords.get(strokeIndex).pushLine(this.oldX, this.oldY, x, y);

        this.oldX = x;
        this.oldY = y;

        notifyObservers();
    }

    public void strokeEnd() {
        if (!strokeStarted) { return; }
        strokeIndex++;
        strokeCount = strokeRecords.size();

        int maxDelta = 0;
        for (StrokeStruct str : strokeRecords) {
            int linesInStroke = str.getLines().size();
            if (linesInStroke > maxDelta) {
                maxDelta = linesInStroke;
            }
        }
        maxLinesPerStroke = maxDelta;
        lineIndex = strokeIndex * maxLinesPerStroke;
        lineCount = lineIndex;

        this.strokeStarted = false;

        notifyObservers();
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int index) {
            gc.setColor(Color.white);
            gc.fillRect(0, 0, canvasW, canvasH);

            this.lineIndex = index;
            if (index == 0) {
                this.strokeIndex = index;
            } else { //If there's anything to draw
                this.strokeIndex = (this.lineIndex - 1) / maxLinesPerStroke + 1;

                for (int i = 0; i < strokeIndex; i++) {
                    StrokeStruct str = strokeRecords.get(i);
                    gc.setColor(str.getColor());
                    gc.setStroke(new BasicStroke(str.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    //This is guaranteed to be a valid integer index in this stroke
                    int relativeEndLineIndex = (int)(Math.min(maxLinesPerStroke, lineIndex - i* maxLinesPerStroke) * str.getLines().size() * 1.0 / maxLinesPerStroke);
                    List<StrokeStruct.Line> lines = str.getLines().subList(0, relativeEndLineIndex);

                    if (lines.size() > 0) {
                        for (StrokeStruct.Line l : lines) {
                            gc.drawLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
                        }
                    }
                }
            }

            notifyObservers();
    }

    public void playForward() {
        delay = 1000.0 / maxLinesPerStroke;
        timer.setDelay((int)(delay + 0.5));
        lastUpdate = System.currentTimeMillis();
        excess = 0;
        timer.start();

        notifyObservers();
    }

    public void stopPlayback() {
        this.timer.stop();

        notifyObservers();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;

        notifyObservers();
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        notifyObservers();
    }

    public int getLinesPerStroke() {
        return maxLinesPerStroke;
    }

    public int getLineCount() {
        return strokeCount * maxLinesPerStroke;
    }

    public boolean isPlaying() {
        return timer.isRunning();
    }

    public int getCanvasWidth() {
        return canvasW;
    }

    public int getCanvasHeight() {
        return canvasH;
    }
}
