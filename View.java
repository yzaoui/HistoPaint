import java.awt.*;
import javax.swing.*;

public class View extends JFrame implements Observer {

    private Model model;

    private JPanel leftPanel;
    private ViewColorPalette colorPanel;
    private ViewStrokeControl strokePanel;
    private ViewPlaybackControls playbackPanel;
    private ViewCanvas drawPanel;

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

        leftPanel = new JPanel();
        this.add(leftPanel, BorderLayout.WEST);
        leftPanel.setBackground(Color.darkGray);

        /********************
         * Color Picker
         ********************/
        colorPanel = new ViewColorPalette(this.model);
        leftPanel.add(colorPanel);

        /********************
         * Stroke Thickness
         ********************/
        strokePanel = new ViewStrokeControl(model);
        leftPanel.add(strokePanel);

        /********************
         * Playback Controls
         ********************/
        playbackPanel = new ViewPlaybackControls(model);
        this.add(playbackPanel, BorderLayout.SOUTH);

        /********************
         * Drawing Area
         ********************/
        drawPanel = new ViewCanvas(model);
        this.add(drawPanel, BorderLayout.CENTER);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        this.model.addObserver(this);

        setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        colorPanel.update(observable);
        strokePanel.update(observable);
        drawPanel.update(observable);
        playbackPanel.update(observable);
    }
}
