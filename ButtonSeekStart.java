import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonSeekStart extends ButtonSeek {
    public ButtonSeekStart(ActionListener listener) {
        super(listener, new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                if (c.isEnabled()) {
                    g.setColor(Color.black);
                } else {
                    g.setColor(Color.lightGray);
                }
                g.fillRect(5, 5, 6, 30);
                g.fillPolygon(new int[]{23, 11, 23}, new int[]{5, 20, 35}, 3);
                g.fillPolygon(new int[]{35, 23, 35}, new int[]{5, 20, 35}, 3);
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
                g.fillRect(5, 5, 6, 30);
                g.fillPolygon(new int[]{23, 11, 23}, new int[]{5, 20, 35}, 3);
                g.fillPolygon(new int[]{35, 23, 35}, new int[]{5, 20, 35}, 3);
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
