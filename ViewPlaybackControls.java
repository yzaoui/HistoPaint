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
        playSlider.setMajorTickSpacing(1);
        playSlider.setSnapToTicks(true);
        playSlider.addChangeListener((ChangeEvent e) -> {
            if (shouldCallChange) {
                model.setStrokeIndex(playSlider.getValue());
            }
        });

        //Start button
        startButton = new JButton("Start");
        this.add(startButton);

        startButton.addActionListener((ActionEvent e) -> model.setStrokeIndex(0));

        //End button
        endButton = new JButton("End");
        this.add(endButton);

        endButton.addActionListener((ActionEvent e) -> model.setStrokeIndex(model.getStrokeCount()));

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
        //Only update if there are strokes
        if (model.getStrokeCount() > 0) {
            shouldCallChange = false;

            setPlayEnabled(true);

            if (model.getStrokeCount() != playSlider.getMaximum()) {
                playSlider.setMaximum(model.getStrokeCount());
            }

            playSlider.setValue(model.getStrokeIndex());

            shouldCallChange = true;
        }
    }
}
