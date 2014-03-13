import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Pattern Generator class
 * Generates one of the 17 wall paper patterns based on an input file
 * 
 * Written by Josh Wang '15 for MATH 032 at Dartmouth College in the 14W
 * Started 3/1/2014
 */
public class PatternGenerator {
	private JComponent canvas, gui;

	
	static String filepath;
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

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		filepath = getFilePath();
//		String orbifoldFilePath = getFilePath();
//		BufferedReader orbifoldReader = new BufferedReader(new FileReader(orbifoldFilePath));
//		String orbifoldName = orbifoldReader.readLine();
//		orbifoldReader.close();
		Wallpaper pattern = new Wallpaper(filepath, 800, 600, "442");
	}
}
