
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class View extends JFrame implements Observer {

    private Model model;
    private Graphics2D gc2;
    private BufferedImage canvas;
    private Color currentColor;
    private Stroke currentStroke;
    private int oldX, oldY;

    /**
     * Create a new View.
     */
    public View(Model model) {
        // Set up the window.
        this.setTitle("Paint");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.currentColor = Color.black;
        this.currentStroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f);

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
        canvas = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();

        JPanel drawPanel = new JPanel() {
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
                float scaleW = 400f / pW;
                float scaleH = 300f / pH;

                oldX = (int)(e.getX() * scaleW);
                oldY = (int)(e.getY() * scaleH);

                Graphics2D gc = canvas.createGraphics();
                gc.setColor(currentColor);
                gc.setStroke(currentStroke);
                gc.drawLine(oldX, oldY, oldX, oldY);
                gc.dispose();
                drawPanel.repaint();
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int pW = drawPanel.getWidth();
                int pH = drawPanel.getHeight();
                float scaleW = 400f / pW;
                float scaleH = 300f / pH;

                int newX = (int)(e.getX() * scaleW);
                int newY = (int)(e.getY() * scaleH);

                Graphics2D gc = canvas.createGraphics();
                gc.setColor(currentColor);
                gc.setStroke(currentStroke);
                gc.drawLine(oldX, oldY, newX, newY);
                gc.dispose();
                drawPanel.repaint();

                oldX = newX;
                oldY = newY;
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
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        System.out.println("Model changed!");
    }
}
