import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class ViewStrokeControl extends JPanel implements Observer {
    private Model model;
    private JSlider widthSlider;

    public ViewStrokeControl(Model model) {
        this.model = model;

        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(80, 55));
        this.add(new Label("Stroke width:"), BorderLayout.NORTH);

        widthSlider = new JSlider(JSlider.VERTICAL, 1, 15, 3);
        this.add(widthSlider, BorderLayout.CENTER);

        widthSlider.addChangeListener((ChangeEvent e) -> this.model.setStrokeWidth(widthSlider.getValue()));
        widthSlider.setMajorTickSpacing(1);
        widthSlider.setSnapToTicks(true);
        widthSlider.setPaintTicks(true);
    }

    public void update(Object observable) {
        this.widthSlider.setValue(this.model.getStrokeWidth());
    }
}
