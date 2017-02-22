import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ViewPlaybackControls extends JPanel implements Observer {
    private Model model;
    private boolean playEnabled;
    private JButton playButton;
    private JSlider playSlider;
    private JButton startButton;
    private JButton endButton;
    private boolean shouldCallChange;

    public ViewPlaybackControls(Model model) {
        this.model = model;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.red);
        this.shouldCallChange = true;

        //Play button
        playButton = new JButton("Play");
        this.add(playButton);

        //Slider
        playSlider = new JSlider(0, 0);
        this.add(playSlider);

        playSlider.setPaintTicks(true);
        playSlider.addChangeListener((ChangeEvent e) -> {
            if (shouldCallChange) {
                model.setPointIndex(playSlider.getValue());
            }
        });

        //Start button
        startButton = new JButton("Start");
        this.add(startButton);

        startButton.addActionListener((ActionEvent e) -> model.setPointIndex(0));

        //End button
        endButton = new JButton("End");
        this.add(endButton);

        endButton.addActionListener((ActionEvent e) -> model.setPointIndex(playSlider.getMaximum()));

        //Disable playback controls
        playEnabled = true; //This is true to ensure the toggle happens
        setPlayEnabled(false);
    }

    public void setPlayEnabled(boolean bool) {
        if (playEnabled != bool) {
            playButton.setEnabled(bool);
            playSlider.setEnabled(bool);
            startButton.setEnabled(bool);
            endButton.setEnabled(bool);

            playEnabled = bool;
        }
    }

    public void update(Object observable) {
        int numPoints = model.getStrokeCount() * model.getPointsPerStroke();
        //Only update if there are strokes
        if (numPoints > 0) {
            shouldCallChange = false;

            setPlayEnabled(true);

            playSlider.setMaximum(numPoints);
            playSlider.setValue(model.getPointIndex());
            playSlider.setMajorTickSpacing(model.getPointsPerStroke());

            shouldCallChange = true;
        }
    }
}
