import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class View extends JFrame implements Observer {
    private Model model;

    private JMenuBar menuBar;
        private JMenu fileMenu;
            private JMenuItem newDraw;
            private JMenuItem save, load;
                private JFileChooser fileChooser;
            private JMenuItem exit;
        private JMenu clipboardMenu;
            private JMenuItem copy;

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
        this.setMinimumSize(new Dimension(480, 450));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                View.this.exitDrawing();
            }
        });

        /********************
         * Set up the menu bar
         ********************/
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        /********************
         * Set up File menu
         ********************/
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        newDraw = new JMenuItem("New");
        fileMenu.add(newDraw);
        newDraw.setToolTipText("Create new animation");

        newDraw.addActionListener((ActionEvent e) -> this.newDrawing());

        fileMenu.addSeparator();

        save = new JMenuItem("Save");
        fileMenu.add(save);
        save.setToolTipText("Save the current animation");

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Animation (*.anim)", "anim"));

        save.addActionListener((ActionEvent e) -> this.saveDrawing());
        save.setEnabled(false);

        load = new JMenuItem("Load");
        fileMenu.add(load);
        load.setToolTipText("Load an existing animation");

        load.addActionListener((ActionEvent e) -> this.loadDrawing());

        fileMenu.addSeparator();

        exit = new JMenuItem("Exit");
        fileMenu.add(exit);
        exit.setToolTipText("Exit the application");

        exit.addActionListener((ActionEvent e) -> this.exitDrawing());

        /********************
         * Set up Clipboard menu
         ********************/
        clipboardMenu = new JMenu("Clipboard");
        menuBar.add(clipboardMenu);
        copy = new JMenuItem("Copy");
        copy.setToolTipText("Copy the current frame to clipboard");
        copy.setEnabled(false);
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
        springLayout.putConstraint(SpringLayout.EAST, colorPreview, 0, SpringLayout.EAST, leftPanel);
        springLayout.putConstraint(SpringLayout.WEST, colorPreview, 0, SpringLayout.WEST, leftPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, colorPreview, 65, SpringLayout.SOUTH, colorPanel);

        springLayout.putConstraint(SpringLayout.NORTH, strokePanel, 10, SpringLayout.SOUTH, colorPreview);
        springLayout.putConstraint(SpringLayout.EAST, strokePanel, 0, SpringLayout.EAST, leftPanel);
        springLayout.putConstraint(SpringLayout.WEST, strokePanel, 0, SpringLayout.WEST, leftPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, strokePanel, 0, SpringLayout.SOUTH, leftPanel);
    }

    private boolean confirmOverwrite() {
        if (this.model.isDirty()) {
            int answer = JOptionPane.showConfirmDialog(this, "Do you want to save your existing work?", "Confirm overwrite", JOptionPane.YES_NO_CANCEL_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                this.saveDrawing();
                return true;
            } else if (answer == JOptionPane.NO_OPTION) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void newDrawing() {
        //Prompt user before overwriting
        if (!this.confirmOverwrite()) { return; }

        this.model = new Model();
        this.replaceSubViews();
        this.revalidate();
        this.update(this.model);
    }

    private void saveDrawing() {
        int state = fileChooser.showSaveDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile() + ".anim");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this.model);
                oos.close();
                this.model.clean();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "File not found.\nCheck the file name and try again.", "File not found", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Invalid file animation. \nCheck the file and try again.", "Invalid file", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void loadDrawing() {
        //Prompt user before overwriting
        if (!this.confirmOverwrite()) { return; }

        int state = fileChooser.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            try {
                FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile());
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
    }

    private void exitDrawing() {
        //Prompt user before overwriting
        if (!this.confirmOverwrite()) { return; }

        System.exit(0);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        boolean isPlaying = model.isPlayingForward() || model.isPlayingBackward();
        save.setEnabled(!isPlaying && this.model.getLineCount() > 0);
        load.setEnabled(!isPlaying);
        copy.setEnabled(!isPlaying && this.model.getLineCount() > 0);
        //Don't allow saving or copying if the drawing is empty

        colorPanel.update(observable);
        colorPreview.update(observable);
        strokePanel.update(observable);
        drawPanel.update(observable);
        playbackPanel.update(observable);
    }
}
