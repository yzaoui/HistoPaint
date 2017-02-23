import java.awt.*;
import java.awt.datatransfer.*;

public class PaintingClipboard implements ClipboardOwner {
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}

    public void copyImage(java.awt.image.BufferedImage bImg) {
        TransferableImage tImg = new TransferableImage(bImg);
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(tImg, this);
    }

    private class TransferableImage implements Transferable {
        private Image img;

        public TransferableImage (Image img) {
            this.img = img;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, java.io.IOException {
            if (flavor == DataFlavor.imageFlavor && this.img != null) {
                return this.img;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.imageFlavor;

            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            DataFlavor[] flavors = this.getTransferDataFlavors();

            for (int i = 0; i < flavors.length; i++) {
                if (flavor == flavors[i]) {
                    return true;
                }
            }

            return false;
        }
    }
}
