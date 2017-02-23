import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EndButton extends JButton {
    public EndButton(ActionListener listener) {
        super();
        this.addActionListener(listener);
        this.setPreferredSize(new Dimension(40, 40));
        this.setIcon(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.fillRect(30, 5, 5, 30);
                int points = 3;
                int xPoints[] = {5, 30, 5};
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
        //TODO: Add pressed icon this.setPressedIcon()
    }
}
