import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Basic shape drawing GUI
 * Sample solution to SA-4, Winter 2014
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; loosely based on CS 5 code by Tom Cormen
 * @author Chris Bailey-Kellogg; lightly revised Winter 2014
 */

public class EditorOne extends JFrame {	
	private static final int width = 800, height = 800;

	// GUI components
	private JComponent canvas, gui;
	JDialog colorDialog;
	JColorChooser colorChooser;
	JLabel colorL;

	// Current settings on GUI
	private boolean drawing = true;			// adding objects vs. moving/deleting/recoloring them
	private String shape = "ellipse"; 		// type of object to add
	private Color color = Color.black;		// current drawing color

	// Drawing state
	private Point point = null;				// initial mouse press for drawing; current position for moving
	private Shape current = null;			// the one and only object on our canvas
	private boolean selected = false;		// has the object been selecting (for deleting/recoloring)?

	public EditorOne() {
		super("Graphical Editor");

		// Helpers to create the canvas and GUI (buttons, etc.)
		setupCanvas();
		setupGUI();

		// Put the buttons and canvas together into the window
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(gui, BorderLayout.NORTH);

		// Usual initialization
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Creates a panel with all the buttons, etc.
	 */
	private void setupGUI() {
		// Toggle whether drawing or editing
		JToggleButton drawingB = new JToggleButton("drawing", drawing);
		drawingB.addActionListener(new AbstractAction("drawing") {
			public void actionPerformed(ActionEvent e) {
				drawing = !drawing;
				selected = false;
			}
		});

		// Select type of shape
		String[] shapes = {"ellipse"};
		JComboBox shapeB = new JComboBox(shapes);
		shapeB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				shape = (String)((JComboBox)e.getSource()).getSelectedItem();
			}
		});

		// Select drawing/recoloring color
		// Following Oracle example
		JButton chooseColorB = new JButton("choose color");
		colorChooser = new JColorChooser();
		colorDialog = JColorChooser.createDialog(chooseColorB,
				"Pick a Color",
				true,  //modal
				colorChooser,
				new AbstractAction() { 
			public void actionPerformed(ActionEvent e) {
				color = colorChooser.getColor();
				colorL.setBackground(color); 
			} 
		}, //OK button
		null); //no CANCEL button handler
		chooseColorB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				colorDialog.setVisible(true);
			}
		});
		colorL = new JLabel();
		colorL.setBackground(Color.black);
		colorL.setOpaque(true);
		colorL.setBorder(BorderFactory.createLineBorder(Color.black));
		colorL.setPreferredSize(new Dimension(25, 25));

		// Delete object if it is selected
		JButton deleteB = new JButton("delete");
		deleteB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// YOUR CODE HERE
				if (selected) {
					current = null;
					selected = false;
					repaint();
				}
			}
		});

		// Recolor object if it is selected
		JButton recolorB = new JButton("recolor");
		recolorB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// YOUR CODE HERE
				if (selected) {
					current.color = color;
					repaint();
				}
			}
		});

		// Put all the stuff into a panel
		gui = new JPanel();
		gui.setLayout(new FlowLayout());
		gui.add(shapeB);
		gui.add(chooseColorB);
		gui.add(colorL);
		gui.add(new JSeparator(SwingConstants.VERTICAL));
		gui.add(drawingB);
		gui.add(deleteB);
		gui.add(recolorB);
	}

	private void setupCanvas() {
		canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// If there is an object, draw it; if it is selected, also put a border on it
				// YOUR CODE HERE
				if (current != null) {
					current.draw(g);
				}
				if (selected) {
					current.border(g);
				}
			}
		};

		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				point = event.getPoint();
				// In drawing mode, start a new object;
				// in editing mode, set selected according to whether the current object (if it exists) contains the point
				// YOUR CODE HERE
				if (drawing) {
					current = new Ellipse(point.x, point.y, point.x, point.y, color);
					repaint();
				}
				else if (current != null) {
					selected = current.contains(point.x, point.y);
					repaint();
				}
			}
		});		

		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				// In drawing mode, update the other corner of the object;
				// in editing mode, move the object by the difference between the current point and the previous one
				// YOUR CODE HERE
				Point p2 = event.getPoint();
				if (drawing) {
					current.setCorners(point.x, point.y, p2.x, p2.y);
					repaint();
				}
				else if (selected) {
					current.moveBy(p2.x - point.x, p2.y - point.y);
					point = event.getPoint();
					repaint();
				}
			}				
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new EditorOne();
			}
		});	
	}
}