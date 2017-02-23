import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartButton extends JButton {
    public StartButton(ActionListener listener) {
        super();
        this.addActionListener(listener);
        this.setPreferredSize(new Dimension(40, 40));
        this.setIcon(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.fillRect(5, 5, 5, 30);
                int points = 3;
                int xPoints[] = {10, 35, 35};
                int yPoints[] = {20, 5, 35};
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
