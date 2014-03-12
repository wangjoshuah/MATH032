import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

/**
 * Pattern Generator class
 * Generates one of the 17 wall paper patterns based on an input file
 * 
 * Written by Josh Wang '15 for MATH 032 at Dartmouth College in the 14W
 * Started 3/1/2014
 */
public class PatternGenerator {
	private static final int width = 800, height = 800;

	//instance variables of our Pattern Generator Object
	private JComponent canvas, gui;
	private Point point = null;				// initial mouse press for drawing; current position for moving

	/**
	 * Puts up a fileChooser and gets path name for file to be opened.
	 * Returns an empty string if the user clicks "cancel".
	 * @return path name of the file chosen	
	 */
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

	/**
	 * set up our canvas with images and click listeners
	 */
	private void setupCanvas() {
		canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// If there is an object, draw it; if it is selected, also put a border on it
				// YOUR CODE HERE

			}
		};

		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				point = event.getPoint();
				// In drawing mode, start a new object;
				// in editing mode, set selected according to whether the current object (if it exists) contains the point
				// YOUR CODE HERE

			}
		});		

		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				// In drawing mode, update the other corner of the object;
				// in editing mode, move the object by the difference between the current point and the previous one
				// YOUR CODE HERE
				Point p2 = event.getPoint();

			}				
		});
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filepath = getFilePath();
		System.out.println(filepath);
		Wallpaper pattern = new Wallpaper(filepath, 800, 600, "2*22");
	}
}
