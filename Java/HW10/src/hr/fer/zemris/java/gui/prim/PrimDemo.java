package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Demonstration for {@link PrimListModel} with two views for the same model.
 * 
 * @author Dan
 *
 */
public class PrimDemo extends JFrame {

	/**	*/
	private static final long serialVersionUID = 1L;

	/**
	 * Program entry point.
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.setVisible(true);
		});
	}
	
	/**
	 * Creates a new {@link PrimDemo} frame.
	 */
	public PrimDemo() {
		setLocation(100, 100);
		setSize(300, 400);
		setTitle("PrimDemo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		PrimListModel model = new PrimListModel();
		JPanel lists = new JPanel(new GridLayout(1, 0));
		for (int i = 0; i < 2; i++) {
			lists.add(new JScrollPane(new JList<>(model)));
		}
		
		JButton next = new JButton("sljedeći");
		next.addActionListener(e -> {
			model.next();
		});
		
		setLayout(new BorderLayout());
		add(lists, BorderLayout.CENTER);
		add(next, BorderLayout.PAGE_END);
	}

}
