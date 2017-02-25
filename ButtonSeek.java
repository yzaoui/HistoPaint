import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class ButtonSeek extends JButton {
    public ButtonSeek(ActionListener listener, Icon icon, Icon pressedIcon) {
        this.addActionListener(listener);
        this.setPreferredSize(new Dimension(40, 40));
        this.setIcon(icon);
        this.setPressedIcon(pressedIcon);
    }
}