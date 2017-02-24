import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class ViewCanvas extends JPanel implements Observer {
    private Model model;
    private BufferedImage canvas;

    public ViewCanvas(Model model) {
        this.model = model;

        this.canvas = this.model.getBufferedImage();

        this.setBackground(Color.white);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int pW = ViewCanvas.this.getWidth();
                int pH = ViewCanvas.this.getHeight();
                float scaleW = (float)ViewCanvas.this.model.getCanvasWidth() / pW;
                float scaleH = (float)ViewCanvas.this.model.getCanvasHeight() / pH;

                ViewCanvas.this.model.strokeStart((int)(e.getX() * scaleW), (int)(e.getY() * scaleH));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ViewCanvas.this.model.strokeEnd();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int pW = ViewCanvas.this.getWidth();
                int pH = ViewCanvas.this.getHeight();
                float scaleW = (float)ViewCanvas.this.model.getCanvasWidth() / pW;
                float scaleH = (float)ViewCanvas.this.model.getCanvasHeight() / pH;

                ViewCanvas.this.model.strokeContinue((int)(e.getX() * scaleW), (int)(e.getY() * scaleH));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(canvas, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void update(Object observable) {
        this.repaint();
    }
}
