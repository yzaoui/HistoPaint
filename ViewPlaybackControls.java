import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;

public class ViewPlaybackControls extends JPanel implements Observer {
    private Model model;
    private boolean playEnabled;
    private JSlider playSlider;
    private ButtonSeekStart buttonSeekStart;
    private ButtonSeekPrevious buttonSeekPrevious;
    private ButtonPlayBackward buttonPlayBackward;
    private ButtonPlayForward buttonPlayForward;
    private ButtonSeekNext buttonSeekNext;
    private ButtonSeekEnd buttonSeekEnd;
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
        buttonSeekStart = new ButtonSeekStart((ActionEvent e) -> model.toFirstStroke());
        this.add(buttonSeekStart);

        //Previous button
        buttonSeekPrevious = new ButtonSeekPrevious((ActionEvent e) -> model.toPreviousStroke());
        this.add(buttonSeekPrevious);

        //Play backward button
        buttonPlayBackward = new ButtonPlayBackward(
                (ActionEvent e) ->  model.playBackward(),
                (ActionEvent e) ->  model.stopPlayback());
        this.add(buttonPlayBackward);

        //Play forward button
        buttonPlayForward = new ButtonPlayForward(
                (ActionEvent e) ->  model.playForward(),
                (ActionEvent e) ->  model.stopPlayback());
        this.add(buttonPlayForward);

        //Next button
        buttonSeekNext = new ButtonSeekNext((ActionEvent e) -> model.toNextStroke());
        this.add(buttonSeekNext);

        //End button
        buttonSeekEnd = new ButtonSeekEnd((ActionEvent e) -> model.toLastStroke());
        this.add(buttonSeekEnd);

        //Disable playback controls
        playEnabled = true; //This is true to ensure the toggle happens
        setPlayEnabled(false);
    }

    private void setPlayEnabled(boolean bool) {
        if (playEnabled != bool) {
            buttonSeekStart.setEnabled(bool);
            buttonSeekPrevious.setEnabled(bool);
            buttonPlayBackward.setEnabled(bool);
            buttonPlayForward.setEnabled(bool);
            buttonSeekNext.setEnabled(bool);
            buttonSeekEnd.setEnabled(bool);

            playSlider.setEnabled(bool);

            playEnabled = bool;
        }
    }

    public void update(Object observable) {
        int numLines = model.getLineCount();
        //Only update if there are strokes
        if (numLines > 0) {
            shouldCallChange = false;

            if (model.isPlayingForward()) {
                setPlayEnabled(false);
                buttonPlayForward.setEnabled(true);
                buttonPlayForward.toPauseButton();
                buttonPlayBackward.toPlayButton();
            } else if (model.isPlayingBackward()) {
                setPlayEnabled(false);
                buttonPlayBackward.setEnabled(true);
                buttonPlayForward.toPlayButton();
                buttonPlayBackward.toPauseButton();
            } else {
                setPlayEnabled(true);
                buttonPlayForward.toPlayButton();
                buttonPlayBackward.toPlayButton();
            }

            playSlider.setMaximum(numLines);
            playSlider.setValue(model.getLineIndex());
            playSlider.setMajorTickSpacing(model.getLinesPerStroke());

            shouldCallChange = true;
        }
    }
}
