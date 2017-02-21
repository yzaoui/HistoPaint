
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;

public class View extends JFrame implements Observer {

    private Model model;
    private BufferedImage canvas;

    private static final int canvasW = 400;
    private static final int canvasH = 300;

    private JPanel drawPanel;

    private JPanel leftPanel;
    private ViewColorPalette colorPanel;
    private ViewStrokeControl strokePanel;
    private ViewPlaybackControls playbackPanel;

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
        colorPanel = new ViewColorPalette(model);
        model.addObserver(colorPanel);
        leftPanel.add(colorPanel);

        /********************
         * Stroke Thickness
         ********************/
        strokePanel = new ViewStrokeControl(model);
        model.addObserver(strokePanel);
        leftPanel.add(strokePanel);

        /********************
         * Playback Controls
         ********************/
        playbackPanel = new ViewPlaybackControls(model);
        model.addObserver(playbackPanel);
        this.add(playbackPanel, BorderLayout.SOUTH);;

        /********************
         * Drawing Area
         ********************/
        canvas = new BufferedImage(canvasW, canvasH, BufferedImage.TYPE_INT_ARGB);
        model.setGraphics(canvas.createGraphics());

        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(canvas, 0, 0, getWidth(), getHeight(), this);
            }
        };
        this.add(drawPanel, BorderLayout.CENTER);

        drawPanel.setBackground(Color.white);

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int pW = drawPanel.getWidth();
                int pH = drawPanel.getHeight();
                float scaleW = (float)canvasW / pW;
                float scaleH = (float)canvasH / pH;

                model.strokeStart((int)(e.getX() * scaleW), (int)(e.getY() * scaleH));
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int pW = drawPanel.getWidth();
                int pH = drawPanel.getHeight();
                float scaleW = (float)canvasW / pW;
                float scaleH = (float)canvasH / pH;

                model.strokeContinue((int)(e.getX() * scaleW), (int)(e.getY() * scaleH));
            }
        });

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                model.strokeEnd();
            }
        });

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
        drawPanel.repaint();
    }
}
