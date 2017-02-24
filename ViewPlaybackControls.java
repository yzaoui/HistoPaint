import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ViewPlaybackControls extends JPanel implements Observer {
    private Model model;
    private boolean playEnabled;
    private PlayButton playButton;
    private JSlider playSlider;
    private StartButton startButton;
    private EndButton endButton;
    private boolean shouldCallChange;

    public ViewPlaybackControls(Model model) {
        this.model = model;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.shouldCallChange = true;

        //Slider
        playSlider = new JSlider(0, 0);
        this.add(playSlider);

        playSlider.setPaintTicks(true);
        playSlider.addChangeListener((ChangeEvent e) -> {
            if (shouldCallChange) {
                this.model.setLineIndex(playSlider.getValue());
            }
        });

        //Start button
        startButton = new StartButton((ActionEvent e) -> model.setLineIndex(0));
        this.add(startButton);

        //Play button
        playButton = new PlayButton(
                (ActionEvent e) ->  model.playForward(),
                (ActionEvent e) ->  model.stopPlayback());
        this.add(playButton);

        //End button
        endButton = new EndButton((ActionEvent e) -> model.setLineIndex(playSlider.getMaximum()));
        this.add(endButton);

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
        int numLines = model.getLineCount();
        //Only update if there are strokes
        if (numLines > 0) {
            shouldCallChange = false;

            setPlayEnabled(true);

            if (model.isPlaying()) {
                playButton.toPauseButton();
            } else {
                playButton.toPlayButton();
            }

            playSlider.setMaximum(numLines);
            playSlider.setValue(model.getLineIndex());
            playSlider.setMajorTickSpacing(model.getLinesPerStroke());

            shouldCallChange = true;
        }
    }
}
