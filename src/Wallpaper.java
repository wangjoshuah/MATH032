import javax.imageio.*;

import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * A class demonstrating manipulation of image pixels.
 * 
 * @author Josh Wang '15
 */
public class Wallpaper {
	private DrawingFrame gui;			// handles the image display
	private BufferedImage domain;
	private BufferedImage wallpaper;		// the version that's been processed
	private BufferedImage orbifold;

	/**
	 * @param image		the original
	 */
	public Wallpaper(String filename, int width, int height, String patternType) {
		// Create a GUI element to display the image.
		gui = new DrawingFrame("Wall Paper Pattern", width, height);
		try {
			domain = ImageIO.read(new File(filename));
		}
		catch (Exception e) {
			System.err.println("Couldn't load image");
			System.exit(-1);
		}
		orbifold = createOrbifold(domain, patternType);
		tessalate();
		gui.setImage(wallpaper);
		// Hold on to a copy of the image (so can mess it up).
		wallpaper = gui.getImage();
	}

	private BufferedImage createOrbifold(BufferedImage input, String patternType) {
		BufferedImage product = null;
		switch (patternType) {
		case "o":
			//no symmetries
			product = input;
			break;
		case "2222": //2 centers of rotation
			//height is twice the height of the domain
			product = new BufferedImage(input.getWidth(), input.getHeight() * 2, input.getType());
			
			insertDomainInTopLeft(input, product);
			rotateTop180ToBottom(input, product);
			break;
		case "**": //2 parallel mirrors
			//height is twice the height of the domain
			product = new BufferedImage(input.getWidth(), input.getHeight() * 2, input.getType());
			
			insertDomainInTopLeft(input, product);
			reflectTopToBottom(input, product);
			break;
		case "xx": //a glide reflection
			//height is twice the height of the domain
			product = new BufferedImage(input.getWidth(), input.getHeight() * 2, input.getType());
			
			insertDomainInTopLeft(input, product);
			miracleDomainTopDown(input, product);
			break;
		case "*x": //glide reflection and 2 parallel mirrors
			//height and width are both twice the domain's
			product = new BufferedImage(input.getWidth() * 2, input.getHeight() * 2, input.getType());

			insertDomainInTopLeft(input, product);
			miracleDomainTopDown(input, product);
			reflectLeftToRight(input, product);
			break;
		case "*2222": //four reflections perpendicular
			//height and width are both twice the domain's
			product = new BufferedImage(input.getWidth() * 2, input.getHeight() * 2, input.getType());

			insertDomainInTopLeft(input, product);
			reflectDomainTopToBottom(input, product);
			reflectLeftToRight(input, product);
			break;
		case "22*": //reflection down and then a two 180 degree rotations to the side
			//height and width are both twice the domain's
			product = new BufferedImage(input.getWidth() * 2, input.getHeight() * 2, input.getType());

			insertDomainInTopLeft(input, product);
			rotateDomain180DegreesOnTheRight(input, product);
			reflectTopToBottom(input, product);
			break;
		case "22x": //two glide reflections and two rotations of 180 degrees
			//height and width are both twice the domain's
			product = new BufferedImage(input.getWidth() * 2, input.getHeight() * 2, input.getType());

			insertDomainInTopLeft(input, product);
			miracleDomainTopDown(input, product);
			rotateDomain180DegreesOnTheRight(input, product);
			miracleTopRightToBottomRight(input, product);
			break;
		case "2*22": //bricks on a wall pattern 
			//two order 2 reflections and one rotation of order 2
			BufferedImage preProduct = new BufferedImage(input.getWidth(), input.getHeight() * 2, input.getType());
			
			//create a top down reflected pre product that will be half the brick
			insertDomainInTopLeft(input, preProduct);
			reflectTopToBottom(input, preProduct);
			
			//now do a 22x with the preproduct
			product = new BufferedImage(preProduct.getWidth() * 2, preProduct.getHeight() * 2, preProduct.getType());
			
			insertDomainInTopLeft(preProduct, product);
			miracleDomainTopDown(preProduct, product);
			rotateDomain180DegreesOnTheRight(preProduct, product);
			miracleTopRightToBottomRight(preProduct, product);
			break;
		case "442": //two rotation centers of order four and one rotation center of order two
			//height and width are both twice the domain's
			product = new BufferedImage(input.getWidth() * 2, input.getHeight() * 2, input.getType());
			
			insertDomainInTopLeft(input, product);
			rotateDomain90DegreesToTheRight(input, product);
			rotateTop180ToBottom(input, product);
			break;
		default:
			break;
		}
		return product;
	}
	
	private void insertDomainInTopLeft(BufferedImage input, BufferedImage product) {
		for (int x = 0; x < input.getWidth(); x ++) {
			for (int y = 0; y < input.getHeight(); y ++) {
				product.setRGB(x, y, input.getRGB(x, y));
			}
		}
	}
	
	private void rotateDomain180DegreesOnTheRight(BufferedImage input, BufferedImage product) {
		for (int x = input.getWidth(); x < product.getWidth(); x ++) {
			for (int y = 0; y < input.getHeight(); y ++) {
				product.setRGB(x, y, input.getRGB(input.getWidth() * 2 - x - 1, input.getHeight() - y - 1));
			}
		}
	}
	
	private void rotateDomain90DegreesToTheRight(BufferedImage input, BufferedImage product) {
		for (int x = input.getWidth(); x < product.getWidth(); x ++) {
			for (int y = 0; y < input.getHeight(); y ++) {
				product.setRGB(x, y, input.getRGB(y, input.getHeight() * 2 - x - 1));
			}
		}
	}
	
	private void rotateTop180ToBottom(BufferedImage input, BufferedImage product) {
		for (int x = 0; x < product.getWidth(); x ++) {
			for (int y = input.getHeight(); y < product.getHeight(); y ++) {
				product.setRGB(x, y, product.getRGB(product.getWidth() - x - 1, product.getHeight() - y - 1));
			}
		}
	}
	
	private void reflectDomainTopToBottom(BufferedImage input, BufferedImage product) {
		for (int x = 0; x < input.getWidth(); x ++) {
			for (int y = input.getHeight(); y < product.getHeight(); y ++) {
				product.setRGB(x, y, input.getRGB(x, input.getHeight() - (y - input.getHeight()) - 1));
			}
		}
	}
	
	private void reflectTopToBottom(BufferedImage input, BufferedImage product) {
		for (int x = 0; x < product.getWidth(); x ++) {
			for (int y = input.getHeight(); y < product.getHeight(); y ++) {
				product.setRGB(x, y, product.getRGB(x, product.getHeight() - y - 1));
			}
		}
	}
	
	private void reflectLeftToRight(BufferedImage input, BufferedImage product) {
		for (int x = input.getWidth(); x < product.getWidth(); x ++) {
			for (int y = 0; y < product.getHeight(); y ++) {
				product.setRGB(x, y, product.getRGB(input.getWidth() * 2 - x - 1, y));
			}
		}
	}
	
	private void miracleDomainTopDown(BufferedImage input, BufferedImage product) {
		for (int x = 0; x < input.getWidth(); x ++) {
			for (int y = input.getHeight(); y < product.getHeight(); y ++) {
				product.setRGB(x, y, input.getRGB(input.getWidth() - x - 1, y%input.getHeight()));
			}
		}
	}
	
	private void miracleTopRightToBottomRight(BufferedImage input, BufferedImage product) {
		for (int x = input.getWidth(); x < product.getWidth(); x ++) {
			for (int y = input.getHeight(); y < product.getHeight(); y ++) {
				product.setRGB(x, y, product.getRGB(product.getWidth() + input.getWidth() - x - 1, y%input.getHeight()));
			}
		}
	}

	private void tessalate() {
		// Create a new image into which the resulting pixels will be stored.
		BufferedImage result = new BufferedImage(gui.getWidth(), gui.getHeight(), orbifold.getType());
		// Nested loop over every pixel

		for (int y = 0; y < gui.getHeight(); y++) {
			for (int x = 0; x < gui.getWidth(); x ++) {
				result.setRGB(x, y, orbifold.getRGB(x % orbifold.getWidth(), y % orbifold.getHeight()));
			}
		}
		// Make the current image be this new image.
		wallpaper = result;
	}

	/**
	 * Returns a value that is one of val (if it's between min or max) or min or max (if it's outside that range).
	 * @param val
	 * @param min
	 * @param max
	 * @return constrained value
	 */
	private static double constrain(double val, double min, double max) {
		if (val < min) {
			return min;
		}
		else if (val > max) {
			return max;
		}
		return val;
	}

}
