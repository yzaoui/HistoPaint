import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class ViewStrokeControl extends JPanel implements Observer {
    private Model model;

    public ViewStrokeControl(Model model) {
        this.model = model;

        SpinnerNumberModel numModel = new SpinnerNumberModel(model.getStrokeWidth(), 1, 15, 1);
        JSpinner spinner = new JSpinner(numModel);
        this.add(spinner);

        spinner.addChangeListener((ChangeEvent e) -> {
            int newStrokeWidth = numModel.getNumber().intValue();

            model.setStrokeWidth(newStrokeWidth);
        });
    }

    public void update(Object observable) {}
}
