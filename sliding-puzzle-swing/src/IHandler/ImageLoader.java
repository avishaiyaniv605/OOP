package IHandler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * loads a picture from user's pc to the game
 *
 */
public class ImageLoader 
{	

	public BufferedImage loadImage()
	{
		BufferedImage puzzleImage;
		JFileChooser fileChooser = new JFileChooser();
		int action = fileChooser.showOpenDialog(null);
		if(action == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			try {
				puzzleImage = ImageIO.read(file);
				return puzzleImage;
			} catch (IOException e1) {

				JOptionPane.showMessageDialog(null, "Could not upload image"
						+ '\n' + "Please choose a legal image", 
						"Image chosen is illegal", JOptionPane.CANCEL_OPTION);

			}
			return null;
		}
		return null;
	}
}
