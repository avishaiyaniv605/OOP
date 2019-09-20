package Game;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * small class contributing the ability to create chosen backgrounds to our JComponents
 */
public class ImagePanel extends JPanel{

	private BufferedImage _image;

	public ImagePanel(String path) {
		super (new GridBagLayout());
		try {                
			_image = ImageIO.read(new File(path));
		} catch (IOException ex) {
		}
	}
	public ImagePanel(BufferedImage img) {
		super (new GridBagLayout());
		_image = img;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(_image, 0, 0, this); 
	}

}
