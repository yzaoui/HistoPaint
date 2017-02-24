import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonSeekNext extends ButtonSeek {
    public ButtonSeekNext(ActionListener listener) {
        super(listener, new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.fillRect(29, 5, 6, 30);
                g.fillPolygon(new int[]{5, 29, 5}, new int[]{5, 20, 35}, 3);
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
