import javax.swing.*;
import java.awt.event.ActionListener;

public class PlayButton extends JButton {
    private ActionListener playListener;
    private ActionListener pauseListener;

    private enum State {
        NONE,
        PAUSE,
        PLAY
    }

    private State state;

    public PlayButton(ActionListener playListener, ActionListener pauseListener) {
        super();
        this.playListener = playListener;
        this.pauseListener = pauseListener;
        this.state = State.NONE;
        this.toPlayButton();
    }

    public void toPlayButton() {
        if (state != State.PLAY) {
            if (state == State.PAUSE) {
                this.removeActionListener(pauseListener);
            }
            this.addActionListener(playListener);
            this.setText("Play");
            this.state = State.PLAY;
        }
    }

    public void toPauseButton() {
        if (state != State.PAUSE) {
            if (state == State.PLAY) {
                this.removeActionListener(playListener);
            }
            this.addActionListener(pauseListener);
            this.setText("Pause");
            this.state = State.PAUSE;
        }
    }
}
