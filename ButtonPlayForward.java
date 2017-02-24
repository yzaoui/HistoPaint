import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPlayForward extends ButtonPlay {
    public ButtonPlayForward(ActionListener playListener, ActionListener pauseListener) {
        super(playListener, pauseListener, new Icon() {
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
        });
    }
}
