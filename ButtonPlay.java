import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class ButtonPlay extends JButton {
    protected ActionListener playListener;
    protected ActionListener pauseListener;
    protected Icon playIcon;
    protected Icon pauseIcon;

    protected enum State {
        NONE,
        PAUSE,
        PLAY
    }

    protected State state;

    public ButtonPlay(ActionListener playListener, ActionListener pauseListener, Icon playIcon, Icon pressedIcon) {
        super();
        this.playListener = playListener;
        this.pauseListener = pauseListener;
        this.state = State.NONE;
        this.setPreferredSize(new Dimension(40, 40));
        this.setPressedIcon(pressedIcon);
        this.playIcon = playIcon;
        this.pauseIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(Color.black);
                g.fillRect(5, 5, 10, 30);
                g.fillRect(25, 5, 10, 30);
            }

            @Override
            public int getIconWidth() {
                return 30;
            }

            @Override
            public int getIconHeight() {
                return 30;
            }
        };
        //TODO: Add pressed icon this.setPressedIcon()
        this.toPlayButton();
    }

    public void toPlayButton() {
        if (state != State.PLAY) {
            if (state == State.PAUSE) {
                this.removeActionListener(pauseListener);
            }
            this.addActionListener(playListener);
            this.setIcon(playIcon);
            this.setDisabledIcon(playIcon);
            this.state = State.PLAY;
        }
    }

    public void toPauseButton() {
        if (state != State.PAUSE) {
            if (state == State.PLAY) {
                this.removeActionListener(playListener);
            }
            this.addActionListener(pauseListener);
            this.setIcon(pauseIcon);
            this.setDisabledIcon(pauseIcon);
            this.state = State.PAUSE;
        }
    }
}
