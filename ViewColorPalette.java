import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewColorPalette extends JPanel implements Observer {
    private Model model;

    public ViewColorPalette(Model model) {
        this.model = model;

        this.setLayout(new GridLayout(0, 2));
        this.setPreferredSize(new Dimension(80, 160));

        this.add(new ColorButton(Color.black));
        this.add(new ColorButton(Color.gray));
        this.add(new ColorButton(Color.red));
        this.add(new ColorButton(Color.orange));
        this.add(new ColorButton(Color.yellow));
        this.add(new ColorButton(Color.green));
        this.add(new ColorButton(Color.blue));

        Button colorChooser = new Button("?");
        this.add(colorChooser);
        colorChooser.addActionListener((ActionEvent e) -> {
            Color current = ViewColorPalette.this.model.getColor();
            final JColorChooser chooser = new JColorChooser(current);

            JDialog dialog = JColorChooser.createDialog(ViewColorPalette.this, "Choose stroke color", false, chooser, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ViewColorPalette.this.model.setColor(chooser.getColor());
                }
            }, null);

            dialog.setVisible(true);
        });
    }

    public void update(Object observable) {}

    public class ColorButton extends JButton {
        public ColorButton(Color bg) {
            this.setPreferredSize(new Dimension(40, 40));
            this.setMinimumSize(new Dimension(40, 40));
            this.setBackground(bg);

            this.addActionListener((ActionEvent e) -> ViewColorPalette.this.model.setColor(bg));
        }
    }
}
