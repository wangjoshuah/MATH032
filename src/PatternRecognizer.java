import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;


public class PatternRecognizer {

	BufferedImage original;
	BufferedImage copy;
	public BufferedImage bestGuess;
	private static final int maxColorDiff = 100;				// how similar a pixel color must be to the target color, to belong to a region

	public PatternRecognizer(String filename) {
		// Hold on to a copy of the image (so can mess it up).
		try {
			original = ImageIO.read(new File(filename));
		}
		catch (Exception e) {
			System.err.println("Couldn't open file.");
		}
	}

	public static String getFilePath() {
		JFileChooser fc = new JFileChooser("."); // start at current directory

		int returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String pathName = file.getAbsolutePath();
			return pathName;
		}
		else
			return "";
	}

	public void translationTest() {
		BufferedImage subImage;
		int noRows;
		int noCols;
		BufferedImage board;
		lablOut: for (int x = 1; x < original.getWidth()/2; x ++) { //test on sub images up to half the original image's width
			for (int y = 1; y < original.getHeight()/2; y ++) { //test on sub images up to half the original's height
				subImage = original.getSubimage(0, 0, x, y); //create that subImage
				noCols = original.getWidth() / subImage.getWidth(); //number of columns in the board
				noRows = original.getHeight() / subImage.getHeight(); //number of rows in the board
				board = original.getSubimage(0, 0, subImage.getWidth() * noCols, subImage.getHeight() * noRows); //create board to test on
				if (testTile(subImage, board)) {
					System.out.println("we have a winner!");
					bestGuess = subImage;
					break lablOut;
				}
			}
		}
	}
	
	private boolean testTile(BufferedImage subImage, BufferedImage board) {
		System.out.println("subImage size is " + subImage.getWidth() + ", " + subImage.getHeight() + " board size is " + board.getWidth() + ", " + board.getHeight());
		Color subColor;
		Color boardColor;
		for (int x = 0; x < board.getWidth(); x ++) {
			for (int y = 0; y < board.getHeight(); y ++) {
				subColor = new Color(subImage.getRGB(x%subImage.getWidth(), y % subImage.getHeight()));
				boardColor = new Color(board.getRGB(x, y));
				if (!colorMatch(subColor, boardColor)) {
					System.out.println("failed at " + x + ", " + y + " modded to " + x%subImage.getWidth() + ", " + y % subImage.getHeight());
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the static threshold).
	 * @param c1 the original color
	 * @param c2 the neighbor color
	 * @return
	 */
	protected static boolean colorMatch(Color c1, Color c2) {
		// YOUR CODE HERE
		if (c2.getRed() - c1.getRed() > maxColorDiff) { //if the neighbor color is 20 greater than the original
			return false; //go away
		}
		else if (c2.getRed() - c1.getRed() < -maxColorDiff) { //if the neighbor color is 20 less than the original
			return false; //still not true
		}
		else if (c2.getBlue() - c1.getBlue() > maxColorDiff) { //repeat for blue
			return false;
		}
		else if (c2.getBlue() - c1.getBlue() < -maxColorDiff) {
			return false;
		}
		else if (c2.getGreen() - c1.getGreen() > maxColorDiff) { //repeat for green
			return false;
		}
		else if (c2.getGreen() - c1.getGreen() < -maxColorDiff) {
			return false;
		}
		else { //it completely works now and isn't outside of any bounds
			return true; // replace this -- just there so Eclipse won't complain
		}
	}

	public static void main(String[] args) { 
		String filePath = getFilePath();
		PatternRecognizer test = new PatternRecognizer(filePath);
		test.translationTest();
		DrawingFrame display = new DrawingFrame("best guess", test.bestGuess);
		try {
			ImageIO.write(test.bestGuess, "jpg", new File("Domain.jpg"));
			System.out.println("Saved a snapshot");
		}
		catch (Exception e) {
			System.err.println("Couldn't save snapshot.");
		}
	}
}
