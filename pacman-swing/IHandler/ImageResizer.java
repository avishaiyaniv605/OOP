package IHandler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * ImageResizer resizes every chosen picture to a specific width and height defined in the signature
 */
public class ImageResizer 
{
	public  BufferedImage resizeImage(BufferedImage image, int width, int height) 
	{
       int type = 0;
       type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
       BufferedImage resizedImage = new BufferedImage(width, height,type);
       Graphics2D g = resizedImage.createGraphics();
       g.drawImage(image, 0, 0, width, height, null);
       g.dispose();
       return resizedImage;
    }
}