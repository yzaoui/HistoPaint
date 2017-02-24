import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class View extends JFrame implements Observer {

    private Model model;

    private JFileChooser fileChooser;
    private JPanel leftPanel;
    private SpringLayout springLayout;
    private ViewColorPalette colorPanel;
    private ViewColorPreview colorPreview;
    private ViewStrokeControl strokePanel;
    private ViewPlaybackControls playbackPanel;
    private ViewCanvas drawPanel;

    /**
     * Create a new View.
     */
    public View(Model model) {
        // Set up the window.
        this.setTitle("Paint");
        this.setMinimumSize(new Dimension(480, 400));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /********************
         * Set up the menu bar
         ********************/
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        /********************
         * Set up File menu
         ********************/
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

        exit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        /********************
         * Set up Clipboard menu
         ********************/
        JMenu clipboardMenu = new JMenu("Clipboard");
        menuBar.add(clipboardMenu);
        JMenuItem copy = new JMenuItem("Copy");
        clipboardMenu.add(copy);

        copy.addActionListener((ActionEvent e) -> {
            PaintingClipboard pc = new PaintingClipboard();
            pc.copyImage(this.model.getBufferedImage());
        });

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
        leftPanel.setPreferredSize(new Dimension(80, 0));

        springLayout = new SpringLayout();

        leftPanel.setLayout(springLayout);
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
         * Color Preview
         ********************/
        colorPreview = new ViewColorPreview(this.model);
        leftPanel.add(colorPreview);

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

        springLayout.putConstraint(SpringLayout.NORTH, colorPanel, 0, SpringLayout.NORTH, leftPanel);
        springLayout.putConstraint(SpringLayout.EAST, colorPanel, 80, SpringLayout.WEST, leftPanel);
        springLayout.putConstraint(SpringLayout.WEST, colorPanel, 0, SpringLayout.WEST, leftPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, colorPanel, 160, SpringLayout.NORTH, leftPanel);

        springLayout.putConstraint(SpringLayout.NORTH, colorPreview, 10, SpringLayout.SOUTH, colorPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, colorPreview, -10, SpringLayout.NORTH, strokePanel);

        springLayout.putConstraint(SpringLayout.WEST, strokePanel, 0, SpringLayout.WEST, leftPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, strokePanel, 0, SpringLayout.SOUTH, leftPanel);
    }

    public void saveModel(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file + ".anim");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.model);
            oos.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found.\nCheck the file name and try again.", "File not found", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Invalid file animation. \nCheck the file and try again.", "Invalid file", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "File not found.\nCheck the file name and try again.", "File not found", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Invalid file animation. \nCheck the file and try again.", "Invalid file", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Animation not found. \nCheck the file and try again.", "Invalid file", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        colorPanel.update(observable);
        colorPreview.update(observable);
        strokePanel.update(observable);
        drawPanel.update(observable);
        playbackPanel.update(observable);
    }
}
