import javax.swing.*;
import java.awt.event.ActionListener;

public class PlayTimer extends Timer {
    public PlayTimer (int delay, ActionListener listener) {
        super(delay, listener);
        this.setRepeats(true);
    }
}
