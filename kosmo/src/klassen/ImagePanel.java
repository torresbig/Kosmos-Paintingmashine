package klassen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

public class ImagePanel  extends Panel {
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image;

	   public ImagePanel(Image image) {
	      setImage(image);
	   }

	   public void setImage(Image image) {
	      this.image = image;
	      repaint();
	   }

	   public Dimension getPreferredSize() {
	       if(image != null) {
	          return new Dimension(image.getWidth(this), image.getHeight(this));
	       }
	       return super.getPreferredSize();
	   }

	   public void paint(Graphics g) {
	      super.paint(g);
	      if(image != null) {
	         g.drawImage(image, 0, 0, this);
	      }
	   }

}
