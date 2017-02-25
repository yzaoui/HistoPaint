import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPlayForward extends ButtonPlay {
    public ButtonPlayForward(ActionListener playListener, ActionListener pauseListener) {
        super(playListener, pauseListener, new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                if (c.isEnabled()) {
                    g.setColor(Color.black);
                } else {
                    g.setColor(Color.lightGray);
                }
                g.fillPolygon(new int[]{5, 35, 5}, new int[]{5, 20, 35}, 3);
            }

            @Override
            public int getIconWidth() {
                return 30;
            }

            @Override
            public int getIconHeight() {
                return 30;
            }
        }, new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(Color.gray);
                g.fillPolygon(new int[]{5, 35, 5}, new int[]{5, 20, 35}, 3);
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
