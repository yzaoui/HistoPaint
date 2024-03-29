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
        buttonSeekStart.setToolTipText("Go to start of animation");

        //Previous button
        buttonSeekPrevious = new ButtonSeekPrevious((ActionEvent e) -> model.toPreviousStroke());
        this.add(buttonSeekPrevious);
        buttonSeekPrevious.setToolTipText("Go to previous stroke connection");

        //Play backward button
        buttonPlayBackward = new ButtonPlayBackward(
                (ActionEvent e) ->  model.playBackward(),
                (ActionEvent e) ->  model.stopPlayback());
        this.add(buttonPlayBackward);
        buttonPlayBackward.setToolTipText("Play animation backward");

        //Play forward button
        buttonPlayForward = new ButtonPlayForward(
                (ActionEvent e) ->  model.playForward(),
                (ActionEvent e) ->  model.stopPlayback());
        this.add(buttonPlayForward);
        buttonPlayForward.setToolTipText("Play animation forward");

        //Next button
        buttonSeekNext = new ButtonSeekNext((ActionEvent e) -> model.toNextStroke());
        this.add(buttonSeekNext);
        buttonSeekNext.setToolTipText("Go to next stroke connection");

        //End button
        buttonSeekEnd = new ButtonSeekEnd((ActionEvent e) -> model.toLastStroke());
        this.add(buttonSeekEnd);
        buttonSeekEnd.setToolTipText("Go to end of animation");

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
        int lineCount = model.getLineCount();
        //Only update if there are strokes
        if (lineCount > 0) {
            shouldCallChange = false;

            int lineIndex = model.getLineIndex();

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
                playEnabled = true;

                buttonSeekStart.setEnabled(lineIndex > 0);
                buttonSeekPrevious.setEnabled(lineIndex > 0);
                buttonPlayBackward.setEnabled(lineIndex > 0);
                buttonPlayForward.setEnabled(lineIndex < lineCount);
                buttonSeekNext.setEnabled(lineIndex < lineCount);
                buttonSeekEnd.setEnabled(lineIndex < lineCount);

                playSlider.setEnabled(true);

                buttonPlayForward.toPlayButton();
                buttonPlayBackward.toPlayButton();
            }

            playSlider.setMaximum(lineCount);
            playSlider.setValue(lineIndex);
            playSlider.setMajorTickSpacing(model.getLinesPerStroke());

            shouldCallChange = true;
        }
    }
}
