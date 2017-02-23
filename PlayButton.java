import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlayButton extends JButton {
    private ActionListener playListener;
    private ActionListener pauseListener;
    private Icon playIcon;
    private Icon pauseIcon;

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
        this.setPreferredSize(new Dimension(40, 40));
        this.playIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                int points = 3;
                int xPoints[] = {5, 35, 5};
                int yPoints[] = {5, 20, 35};
                g.fillPolygon(xPoints, yPoints, points);
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
        this.pauseIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
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
            this.state = State.PAUSE;
        }
    }
}
