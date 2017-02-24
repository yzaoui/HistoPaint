import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class ViewStrokeControl extends JPanel implements Observer {
    private Model model;

    public ViewStrokeControl(Model model) {
        this.model = model;

        SpinnerNumberModel numModel = new SpinnerNumberModel(model.getStrokeWidth(), 1, 15, 1);
        JSpinner spinner = new JSpinner(numModel);
        spinner.setFont(new Font("Dialog", Font.BOLD, 20));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMinimumSize(new Dimension(80, 55));
        this.add(new Label("Stroke width:"));
        this.add(spinner);

        spinner.addChangeListener((ChangeEvent e) -> {
            int newStrokeWidth = numModel.getNumber().intValue();

            model.setStrokeWidth(newStrokeWidth);
        });
    }

    public void update(Object observable) {}
}
