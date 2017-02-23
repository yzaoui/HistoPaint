import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class View extends JFrame implements Observer {

    private Model model;

    private JFileChooser fileChooser;
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

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Animation (*.anim)", "anim"));

        save.addActionListener((ActionEvent e) -> {
            int state = fileChooser.showSaveDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                View.this.saveModel(fileChooser.getSelectedFile());
            }
        });

        JMenuItem load = new JMenuItem("Load");
        fileMenu.add(load);

        load.addActionListener((ActionEvent e) -> {
            int state = fileChooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                View.this.loadModel(fileChooser.getSelectedFile());
            }
        });

        fileMenu.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;

        /********************
         * Set up layout
         ********************/
        this.setLayout(new BorderLayout());

        leftPanel = new JPanel();
        this.add(leftPanel, BorderLayout.WEST);
        leftPanel.setBackground(Color.darkGray);

        this.setupSubViews();

        setVisible(true);
    }

    private void replaceSubViews() {
        leftPanel.removeAll();
        this.remove(playbackPanel);
        this.remove(drawPanel);

        this.setupSubViews();
    }

    private void setupSubViews() {
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

        this.model.addObserver(this);
    }

    public void saveModel(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file + ".anim");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.model);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadModel(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.model = (Model)ois.readObject();
            this.replaceSubViews();
            this.revalidate();
            this.update(this.model);
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
