import javax.swing.*;
import java.awt.*;

public class ViewColorPreview extends JPanel implements Observer {
    private Model model;
    private Label colorBox;

    public ViewColorPreview(Model model) {
        this.model = model;

        this.setLayout(new BorderLayout());

        this.add(new Label("Current color:"), BorderLayout.NORTH);

        this.colorBox = new Label();
        this.add(this.colorBox, BorderLayout.CENTER);
        this.colorBox.setBackground(this.model.getColor());
    }

    @Override
    public void update(Object observable) {
        this.colorBox.setBackground(this.model.getColor());
    }
}
