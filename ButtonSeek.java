import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class ButtonSeek extends JButton {
    public ButtonSeek(ActionListener listener, Icon icon) {
        this.addActionListener(listener);
        this.setPreferredSize(new Dimension(40, 40));
        this.setIcon(icon);
    }
}