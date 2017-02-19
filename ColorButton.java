import javax.swing.*;
import java.awt.*;

/**
 * Created by Light on 2017-02-19.
 */
public class ColorButton extends JButton {
    public ColorButton(Color bg) {
        this.setPreferredSize(new Dimension(20, 20));
        this.setBackground(bg);
    }
}
