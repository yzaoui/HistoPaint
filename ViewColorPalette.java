import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ViewColorPalette extends JPanel implements Observer {
    private Model model;

    public ViewColorPalette(Model model) {
        this.model = model;

        this.setBackground(Color.blue);

        this.add(new ColorButton(Color.red));
        this.add(new ColorButton(Color.blue));
        this.add(new ColorButton(Color.black));
        this.add(new ColorButton(Color.yellow));
    }

    public void update(Object observable) {}

    public class ColorButton extends JButton {
        public ColorButton(Color bg) {
            this.setPreferredSize(new Dimension(20, 20));
            this.setBackground(bg);

            this.addActionListener((ActionEvent e) -> {
                ViewColorPalette.this.model.setColor(bg);
            });
        }
    }
}
