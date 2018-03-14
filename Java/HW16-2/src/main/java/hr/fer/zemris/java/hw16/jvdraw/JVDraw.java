package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.objects.CircleObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircleObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObjectFactory;
import hr.fer.zemris.java.hw16.jvdraw.objects.LineObject;

/**
 * Simple swing application for drawing objects represented by
 * {@link GeometricalObject}. Objects are drawn on {@link JDrawingCanvas}. <br>
 * Graphical user interface offers color and object selection to the user.<br>
 * Drawn objects can be stored to a JVD file.
 *
 * @author Dan
 */
public class JVDraw extends JFrame {

	/**
	 * Program entry point. Creates the frame.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JVDraw().setVisible(true);
		});
	}

	/** */
	private static final long serialVersionUID = 1L;

	/** Default backgorund and foreground color. */
	private static final Color DEFAULT_COLOR = Color.BLUE;

	/** JVD {@code FileNameExtensionFilter}. */
	private static final FileNameExtensionFilter JVD_FILTER = new FileNameExtensionFilter("JVD (.jvd)", "jvd");

	/** Foreground {@code JColorArea}. */
	private JColorArea fgColorArea;

	/** Background {@code JColorArea}. */
	private JColorArea bgColorArea;

	/** {@code JLabel} that shows current selected colors. */
	private JLabel colorLabel;

	/** Listener for color change that updates the {@link #colorLabel}. */
	private ColorChangeListener colorListener = new ColorChangeListener() {
		@Override
		public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
			updateColorLabel();
		}
	};

	/** Model with all objects. */
	private DrawingModel model;

	/** Canvas where the objects are drawn. */
	private JDrawingCanvas canvas;

	/** {@code Map} with factories for specific objects. */
	private Map<String, GeometricalObjectFactory> factories;

	/** File for saving the current model. */
	private Path file;

	/** Flag indicating if the model was modified since the last save. */
	private boolean isModelModified;

	/** Listener for objects deletion. */
	private KeyListener listDeleteListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				@SuppressWarnings("unchecked")
				JList<GeometricalObject> list = (JList<GeometricalObject>) e.getSource();
				for (GeometricalObject object : list.getSelectedValuesList()) {
					model.remove(object);
				}
			}
		}
	};

	/** Listener for model modifications. */
	private DrawingModelListener modificationListener = new DrawingModelListener() {
		@Override
		public void objectsRemoved(DrawingModel source, int index0, int index1) {
			isModelModified = true;
		}

		@Override
		public void objectsChanged(DrawingModel source, int index0, int index1) {
			isModelModified = true;
		}

		@Override
		public void objectsAdded(DrawingModel source, int index0, int index1) {
			isModelModified = true;
		}
	};

	/** Saves the current model to the file. */
	private Action saveAction = new AbstractAction("Save") {
		/** */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (file == null) {
				saveAsModel();
			} else {
				JVDFileUtil.saveModelToFile(model, file);
			}
		}
	};

	/** Saves the current model to the file prompting file choosing. */
	private Action saveAsAction = new AbstractAction("Save As") {
		/** */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveAsModel();
		}
	};

	/** Loads model from the file chosen by the user. */
	private Action openAction = new AbstractAction("Open") {
		/** */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser(".");
			fc.setFileFilter(JVD_FILTER);
			if (fc.showOpenDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile().toPath();
				JVDFileUtil.loadModelFromFile(model, file);
			}
			isModelModified = false;
		}
	};

	/**
	 * Creates new {@code JVDraw}.
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		setLocation(200, 200);
		setSize(800, 600);
		setTitle("JVDraw");

		initGUI();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		setLayout(new BorderLayout());

		createToolbar();

		JPanel panel = new JPanel(new BorderLayout());
		add(panel, BorderLayout.CENTER);

		colorLabel = new JLabel();
		colorLabel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));
		bgColorArea.addColorChangeListener(colorListener);
		fgColorArea.addColorChangeListener(colorListener);
		updateColorLabel();
		panel.add(colorLabel, BorderLayout.PAGE_END);

		model = new DrawingModelImpl();
		model.addDrawingModelListener(modificationListener);
		canvas = new JDrawingCanvas(model);
		panel.add(canvas, BorderLayout.CENTER);

		JList<GeometricalObject> list = new JList<>(new DrawingObjectListModel(model));
		panel.add(new JScrollPane(list), BorderLayout.LINE_END);

		list.addKeyListener(listDeleteListener);

		createMenu();
	}

	/**
	 * Creates a menubar and adds menu items.
	 */
	private void createMenu() {
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menubar.add(fileMenu);

		openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		fileMenu.add(openAction);

		saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		fileMenu.add(saveAction);

		saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		fileMenu.add(saveAsAction);
	}

	/**
	 * Creates a toolbar and adds items to it.
	 */
	private void createToolbar() {
		JToolBar toolbar = new JToolBar();
		add(toolbar, BorderLayout.PAGE_START);
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));

		fgColorArea = new JColorArea(DEFAULT_COLOR);
		bgColorArea = new JColorArea(DEFAULT_COLOR);
		toolbar.add(fgColorArea);
		toolbar.add(bgColorArea);

		JToggleButton lineButton = new JToggleButton("Line");
		JToggleButton circleButton = new JToggleButton("Circle");
		JToggleButton fcircleButton = new JToggleButton("Filled circle");
		toolbar.add(lineButton);
		toolbar.add(circleButton);
		toolbar.add(fcircleButton);

		ButtonGroup drawingButtons = new ButtonGroup();
		drawingButtons.add(lineButton);
		drawingButtons.add(circleButton);
		drawingButtons.add(fcircleButton);

		factories = new HashMap<>();
		factories.put(lineButton.getText(),
				new GeometricalObjectFactory(LineObject::new, lineButton.getText(), fgColorArea, bgColorArea));
		factories.put(circleButton.getText(),
				new GeometricalObjectFactory(CircleObject::new, circleButton.getText(), fgColorArea, bgColorArea));
		factories.put(fcircleButton.getText(), new GeometricalObjectFactory(FilledCircleObject::new,
				fcircleButton.getText(), fgColorArea, bgColorArea));

		lineButton.addActionListener(e -> canvas.setObjectFactory(factories.get(lineButton.getText())));
		circleButton.addActionListener(e -> canvas.setObjectFactory(factories.get(circleButton.getText())));
		fcircleButton.addActionListener(e -> canvas.setObjectFactory(factories.get(fcircleButton.getText())));
	}

	/**
	 * Updates the {@link #colorLabel}.
	 */
	private void updateColorLabel() {
		Color bg = bgColorArea.getCurrentColor();
		Color fg = fgColorArea.getCurrentColor();
		String text = String.format("Foreground color: (%d, %d, %d), background color: (%d, %d, %d).", fg.getRed(),
				fg.getGreen(), fg.getBlue(), bg.getRed(), bg.getGreen(), bg.getBlue());
		colorLabel.setText(text);
	}

	/**
	 * Saves the current model to the file prompting user to choose a file.
	 * 
	 * @return {@code false} if user decided to cancel the saving; {@code true}
	 *         otherwise
	 */
	private boolean saveAsModel() {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(JVD_FILTER);

		if (fc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
			file = JVDFileUtil.addExtension(fc.getSelectedFile());
			JVDFileUtil.saveModelToFile(model, file);
			isModelModified = false;
			return true;
		}

		return false;
	}

	/**
	 * Checks if the model is changed and asks the user if the changes should be
	 * saved. <br>
	 * If user doesn't decide to abort to exiting, program is terminated.
	 */
	private void exit() {
		if (isModelModified) {
			int option = JOptionPane.showConfirmDialog(this, "Do you want to save the changes?", "Save changes",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (option == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (option == JOptionPane.YES_OPTION) {
				if (!saveAsModel()) {
					return;
				}
			}
		}
		dispose();
	}
}
