
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class View extends JFrame implements Observer {

    private Model model;

    /**
     * Create a new View.
     */
    public View(Model model) {
        // Set up the window.
        this.setTitle("Paint");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /********************
         * Set up the menu bar
         ********************/
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem save = new JMenuItem("Save");
        fileMenu.add(save);

        JMenuItem load = new JMenuItem("Load");
        fileMenu.add(load);

        fileMenu.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);

        /********************
         * Set up layout
         ********************/
        this.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        this.add(leftPanel, BorderLayout.WEST);
        leftPanel.setBackground(Color.darkGray);

        /********************
         * Color Picker
         ********************/
        JPanel colorPanel = new JPanel();
        leftPanel.add(colorPanel);

        colorPanel.setBackground(Color.blue);

        colorPanel.add(new ColorButton(Color.red));
        colorPanel.add(new ColorButton(Color.blue));
        colorPanel.add(new ColorButton(Color.black));
        colorPanel.add(new ColorButton(Color.yellow));

        /********************
         * Stroke Thickness
         ********************/
        JPanel strokePanel = new JPanel();
        leftPanel.add(strokePanel);

        SpinnerNumberModel numModel = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner spinner = new JSpinner(numModel);
        strokePanel.add(spinner);

        /********************
         * Playback Controls
         ********************/
        JPanel playbackPanel = new JPanel();
        this.add(playbackPanel, BorderLayout.SOUTH);

        playbackPanel.setBackground(Color.red);

        //Play button
        JButton playButton = new JButton("Play");
        playbackPanel.add(playButton);

        //Slider
        JSlider slider = new JSlider();
        playbackPanel.add(slider);

        slider.setEnabled(false);

        //Start button
        JButton startButton = new JButton("Start");
        playbackPanel.add(startButton);

        //End button
        JButton endButton = new JButton("End");
        playbackPanel.add(endButton);

        /********************
         * Drawing Area
         ********************/

        JPanel drawPanel = new JPanel();
        this.add(drawPanel, BorderLayout.CENTER);

        drawPanel.setBackground(Color.white);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);

        setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        System.out.println("Model changed!");
    }
}
