import javax.swing.*;
import java.awt.*;

public class ViewPlaybackControls extends JPanel implements Observer {
    private Model model;
    private boolean playEnabled;
    private JButton playButton;
    private JSlider playSlider;
    private JButton startButton;
    private JButton endButton;

    public ViewPlaybackControls(Model model) {
        this.model = model;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.red);

        //Play button
        playButton = new JButton("Play");
        this.add(playButton);

        //Slider
        playSlider = new JSlider(0, 0);
        this.add(playSlider);

        playSlider.setPaintTicks(true);
        playSlider.setMajorTickSpacing(1);
        playSlider.setSnapToTicks(true);

        //Start button
        startButton = new JButton("Start");
        this.add(startButton);

        //End button
        endButton = new JButton("End");
        this.add(endButton);

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

    public void updatePlaySlider(int strokeCount) {
        if (strokeCount != playSlider.getMaximum()) {
            playSlider.setMaximum(strokeCount);
            playSlider.setValue(playSlider.getMaximum());
        }
    }

    public void update(Object observable) {
        int strokeCount = model.getStrokeCount();

        setPlayEnabled(strokeCount > 0); //enable playback if there are strokes

        if (strokeCount > 0) {
            updatePlaySlider(strokeCount);
        }
    }
}
