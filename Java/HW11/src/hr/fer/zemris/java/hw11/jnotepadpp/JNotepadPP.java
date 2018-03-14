package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.actions.ChangeCaseAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ChangeLanguageAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SortAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.StatisticsAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.UniqueAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableJToolbar;

/**
 * This is a simple text file editor with basic functionality for editing files.
 * <br>
 * {@link #main(String[])} method starts the program. <br>
 * Multiple documents can be opened at the same time. Every document is
 * represented by an {@link EditorPanel}. <br>
 * Localization is supported in three languages: English, Croatian and German.
 * 
 * @author Dan
 *
 */
public class JNotepadPP extends JFrame {

	/**
	 * Program entry point. Creates the {@link JNotepadPP} frame.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}

	/** */
	private static final long serialVersionUID = 1L;

	/** Title of this program. */
	private static final String TITLE = "JNotepad++";

	/** Title for new empty tabs. */
	private static final String NEW_TAB_TITLE = "new";

	/** {@link FormLocalizationProvider} for this frame. */
	private FormLocalizationProvider flp;

	/** {@link JTabbedPane} with all currently opened files. */
	private JTabbedPane tabbedPane;

	/** Panels with files that were somehow modified. */
	private Set<EditorPanel> changedPanels;

	/** {@link Action} for opening a file in a new tab. */
	private Action openDocumentAction;

	/** {@link Action} for opening a new empty tab. */
	private Action openEmptyTabAction;

	/** {@link Action} for saving a file. */
	private Action saveDocumentAction;

	/** {@link Action} for saving file as. */
	private Action saveAsDocumentAction;

	/** {@link Action} for closing the current tab. */
	private Action closeTabAction;

	/** {@link Action} for exiting the program. */
	private Action exitAction;

	/** {@link Action} for copying the text. */
	private Action copyAction;

	/** {@link Action} for cutting the text. */
	private Action cutAction;

	/** {@link Action} for pasting the text. */
	private Action pasteAction;

	/** {@link Action} for statistics of current document. */
	private Action statisticsAction;

	/** {@link Action} for changing the selected letters to uppercase. */
	private Action toUpperCaseAction;

	/** {@link Action} for changing the selected letters to lowercase. */
	private Action toLowerCaseAction;

	/** {@link Action} for inverting the case of the selected letters. */
	private Action invertCaseAction;

	/** {@link Action} for sorting the selected lines in ascending order. */
	private Action ascSortAction;

	/** {@link Action} for sorting the selected lines in descending order. */
	private Action descSortAction;

	/** {@link Action} for removing the duplicate lines from the selcetion. */
	private Action uniqueAction;

	/** {@link Action} for changing the language to English. */
	private Action enLanguageAction;

	/** {@link Action} for changing the language to Croatian. */
	private Action hrLanguageAction;

	/** {@link Action} for changing the language to German. */
	private Action deLanguageAction;

	/**
	 * Crates a new {@link JNotepadPP} frame and initializes everything.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(200, 200);
		setSize(800, 900);
		setTitle(TITLE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		changedPanels = new HashSet<>();
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

		initGUI();
	}

	/**
	 * Gets the currently displayed {@link EditorPanel}.
	 * 
	 * @return panel from the current tab
	 */
	public EditorPanel getCurrentPanel() {
		return (EditorPanel) tabbedPane.getSelectedComponent();
	}

	/**
	 * Registers a listener for tab change.
	 * 
	 * @param l
	 *            {@link ChangeListener} to be notified when the current tab
	 *            changes
	 */
	public void addTabChangeListener(ChangeListener l) {
		tabbedPane.addChangeListener(l);
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		add(topPanel, BorderLayout.CENTER);

		tabbedPane = new JTabbedPane();
		topPanel.add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addChangeListener(e -> {
			EditorPanel panel = getCurrentPanel();
			if (panel == null) {
				setTitle(TITLE);
				setEnabledOnActions(false);
				setEnabledOnSelectActions(false);
				return;
			}

			Path filePath = panel.getFilePath();
			String title;
			if (filePath == null) {
				title = NEW_TAB_TITLE;
			} else {
				title = filePath.toAbsolutePath().toString();
			}
			setTitle(title + " - " + TITLE);

			setEnabledOnActions(true);
			panel.getEditor().addCaretListener(e1 -> {
				updateSelectActions();
			});
			updateSelectActions();
		});

		StatusPanel statusBar = new StatusPanel(this, flp);
		topPanel.add(statusBar, BorderLayout.PAGE_END);

		createActions();
		createMenus();
		createToolbars();
	}

	/**
	 * Initializes all private {@link Action} variables.
	 */
	private void createActions() {
		openDocumentAction = new LocalizableAction("open", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		};
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		openEmptyTabAction = new LocalizableAction("new", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				openTab(new EditorPanel("", null));
			}
		};
		openEmptyTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		openEmptyTabAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		saveDocumentAction = new LocalizableAction("save", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurrent();
			}
		};
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveAsDocumentAction = new LocalizableAction("saveAs", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsCurrent();
			}
		};
		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		closeTabAction = new LocalizableAction("close", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				closeCurrent();
			}
		};
		closeTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		closeTabAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		exitAction = new LocalizableAction("exit", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		};
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		copyAction = new LocalizableAction("copy", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentPanel().getEditor().copy();
			}
		};
		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		cutAction = new LocalizableAction("cut", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentPanel().getEditor().cut();
			}
		};
		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

		pasteAction = new LocalizableAction("paste", flp) {
			/**	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentPanel().getEditor().paste();
			}
		};
		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);

		statisticsAction = new StatisticsAction(this, "stat", flp);
		statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
		statisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		toUpperCaseAction = new ChangeCaseAction(this, "toUpper", flp) {
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			protected char processChar(char c) {
				return Character.toUpperCase(c);
			}
		};
		toUpperCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toUpperCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

		toLowerCaseAction = new ChangeCaseAction(this, "toLower", flp) {
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			protected char processChar(char c) {
				return Character.toLowerCase(c);
			}
		};
		toLowerCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		toLowerCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);

		invertCaseAction = new ChangeCaseAction(this, "invert", flp) {
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			protected char processChar(char c) {
				if (Character.isLowerCase(c)) {
					return Character.toUpperCase(c);
				} else {
					return Character.toLowerCase(c);
				}
			}
		};
		invertCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		invertCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

		ascSortAction = new SortAction(true, this, "asc", flp);
		ascSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt A"));
		ascSortAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		descSortAction = new SortAction(false, this, "desc", flp);
		descSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt D"));
		descSortAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

		uniqueAction = new UniqueAction(this, "unique", flp);
		uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt U"));
		uniqueAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

		enLanguageAction = new ChangeLanguageAction("en", "en", flp);
		enLanguageAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

		deLanguageAction = new ChangeLanguageAction("de", "de", flp);
		deLanguageAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);

		hrLanguageAction = new ChangeLanguageAction("hr", "hr", flp);
		hrLanguageAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		setEnabledOnActions(false);
		setEnabledOnSelectActions(false);
	}

	/**
	 * Creates all necessary {@link JMenu} items for this notepad.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new LocalizableJMenu("fileMenu", flp);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(openEmptyTabAction);
		fileMenu.add(openDocumentAction);
		fileMenu.add(saveDocumentAction);
		fileMenu.add(saveAsDocumentAction);
		fileMenu.add(closeTabAction);
		fileMenu.addSeparator();
		fileMenu.add(exitAction);
		menuBar.add(fileMenu);

		JMenu editMenu = new LocalizableJMenu("editMenu", flp);
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.add(cutAction);
		editMenu.add(copyAction);
		editMenu.add(pasteAction);
		menuBar.add(editMenu);

		JMenu toolsMenu = new LocalizableJMenu("toolsMenu", flp);
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		toolsMenu.add(statisticsAction);

		JMenu changeCase = new LocalizableJMenu("changeCase", flp);
		changeCase.setMnemonic(KeyEvent.VK_C);
		changeCase.add(toUpperCaseAction);
		changeCase.add(toLowerCaseAction);
		changeCase.add(invertCaseAction);
		toolsMenu.add(changeCase);

		JMenu sort = new LocalizableJMenu("sort", flp);
		sort.setMnemonic(KeyEvent.VK_S);
		sort.add(ascSortAction);
		sort.add(descSortAction);
		toolsMenu.add(sort);

		toolsMenu.add(uniqueAction);
		menuBar.add(toolsMenu);

		JMenu lang = new LocalizableJMenu("lang", flp);
		lang.setMnemonic(KeyEvent.VK_L);
		lang.add(enLanguageAction);
		lang.add(hrLanguageAction);
		lang.add(deLanguageAction);
		menuBar.add(lang);
	}

	/**
	 * Creates a toolbar for this notepad.
	 */
	private void createToolbars() {
		JToolBar toolbar = new LocalizableJToolbar("toolbar", flp);
		toolbar.add(openEmptyTabAction);
		toolbar.add(openDocumentAction);
		toolbar.add(saveDocumentAction);
		toolbar.add(saveAsDocumentAction);
		toolbar.add(closeTabAction);
		toolbar.addSeparator();
		toolbar.add(cutAction);
		toolbar.add(copyAction);
		toolbar.add(pasteAction);
		toolbar.addSeparator();
		toolbar.add(statisticsAction);
		toolbar.addSeparator();
		toolbar.add(exitAction);
		add(toolbar, BorderLayout.PAGE_START);
	}

	/**
	 * Opens a file that the user selects in a {@link JFileChooser} dialog and
	 * puts it a new tab.
	 */
	private void openFile() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(flp.getString("openFile"));
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		Path filePath = fc.getSelectedFile().toPath();
		if (!Files.isReadable(filePath)) {
			JOptionPane.showMessageDialog(this, flp.getString("fileDExist"), flp.getString("error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(filePath);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, flp.getString("readError"), flp.getString("error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String text = new String(bytes, StandardCharsets.UTF_8);
		EditorPanel panel = new EditorPanel(text, filePath);

		openTab(panel);
	}

	/**
	 * Opens a new tab with given {@link EditorPanel} to display.
	 * 
	 * @param panel
	 *            panel displaying a file contents
	 */
	private void openTab(EditorPanel panel) {
		tabbedPane.addTab("", panel);
		updateTab(panel);
		addDocumentChangeListener(panel);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
	}

	/**
	 * Updates the tab displaying the given {@link EditorPanel}. Icon and name
	 * of the tab are updated.
	 * 
	 * @param panel
	 *            panel whose tab should be updated
	 */
	private void updateTab(EditorPanel panel) {
		int index = tabbedPane.indexOfComponent(panel);
		Path filePath = panel.getFilePath();
		String title, tip;
		if (filePath == null) {
			title = NEW_TAB_TITLE;
			tip = NEW_TAB_TITLE;
		} else {
			title = filePath.getFileName().toString();
			tip = filePath.toAbsolutePath().toString();
		}
		tabbedPane.setTitleAt(index, title);
		tabbedPane.setToolTipTextAt(index, tip);

		Icon icon;
		try {
			if (changedPanels.contains(panel)) {
				icon = IconUtil.getIcon("red_diskette.png");
			} else {
				icon = IconUtil.getIcon("blue_diskette.png");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		tabbedPane.setIconAt(index, icon);
	}

	/**
	 * Saves the file displayed in the current tab. If the document is not
	 * linked with any file, {@link #saveAsCurrent()} is called.
	 */
	private void saveCurrent() {
		EditorPanel panel = getCurrentPanel();
		Path filePath = panel.getFilePath();
		if (filePath == null) {
			saveAsCurrent();
			return;
		}

		byte[] data = panel.getEditor().getText().getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(panel.getFilePath(), data);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, flp.getString("saveError"), flp.getString("error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		changedPanels.remove(panel);
		JOptionPane.showMessageDialog(this, flp.getString("fileSaved"), flp.getString("info"),
				JOptionPane.INFORMATION_MESSAGE);
		updateTab(panel);
		addDocumentChangeListener(panel);
	}

	/**
	 * Saves the file displayed in the current tab to a location that the user
	 * choose in a {@link JFileChooser} and updates the file path of the current
	 * {@link EditorPanel}.
	 */
	private void saveAsCurrent() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(flp.getString("saveAs"));
		if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(this, flp.getString("fileNotSaved"), flp.getString("warning"),
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Path filePath = fc.getSelectedFile().toPath();
		if (filePath.toFile().exists()) {
			int option = JOptionPane.showConfirmDialog(this, flp.getString("overwrite"), flp.getString("fileExists"),
					JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(this, flp.getString("fileNotSaved"), flp.getString("warning"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}

		getCurrentPanel().setFilePath(filePath);
		saveCurrent();
	}

	/**
	 * Closes the current tab, prompting a dialog if any unsaved modifications
	 * where made.
	 * 
	 * @return {@code true} if the tab is closed, or {@code false} if the user
	 *         decided not to close the tab
	 */
	private boolean closeCurrent() {
		EditorPanel panel = getCurrentPanel();
		if (changedPanels.contains(panel)) {
			int option = JOptionPane.showConfirmDialog(this, flp.getString("saveChangesQ"),
					flp.getString("saveChanges"), JOptionPane.YES_NO_CANCEL_OPTION);
			if (option == JOptionPane.CANCEL_OPTION) {
				return false;
			} else if (option == JOptionPane.YES_OPTION) {
				saveCurrent();
			} else {
				changedPanels.remove(panel);
			}
		}

		tabbedPane.remove(panel);
		return true;
	}

	/**
	 * Closes all the opened tabs and disposes the frame, triggering the program
	 * termination.
	 */
	private void exit() {
		int n = tabbedPane.getTabCount();
		for (int i = 0; i < n; i++) {
			tabbedPane.setSelectedIndex(0);
			if (!closeCurrent()) {
				return;
			}
		}
		flp.disconnect();
		dispose();
	}

	/**
	 * Adds a listener to the document viewed in the given {@link EditorPanel},
	 * to mark if the document was somehow changed. Listener is removed after
	 * the fisrt change.
	 * 
	 * @param panel
	 *            panel whose document change is monitored
	 */
	private void addDocumentChangeListener(EditorPanel panel) {
		Document doc = panel.getEditor().getDocument();
		doc.addDocumentListener(new AbstractDocumentListener() {
			@Override
			public void actionPerformed() {
				changedPanels.add(panel);
				updateTab(panel);
				doc.removeDocumentListener(this);
			}
		});
	}

	/**
	 * Sets the enabled status on actions that need to be disabled when no tabs
	 * are opened.
	 * 
	 * @param b
	 *            enabled status
	 */
	private void setEnabledOnActions(boolean b) {
		saveDocumentAction.setEnabled(b);
		saveAsDocumentAction.setEnabled(b);
		closeTabAction.setEnabled(b);
		statisticsAction.setEnabled(b);
		copyAction.setEnabled(b);
		cutAction.setEnabled(b);
		pasteAction.setEnabled(b);
	}

	/**
	 * Disables the actions that work only on selected text, if no text is
	 * selected, and enables them if some text is selected.
	 */
	private void updateSelectActions() {
		JTextArea editor = getCurrentPanel().getEditor();
		setEnabledOnSelectActions(editor.getSelectedText() != null);
	}

	/**
	 * Sets the enabled status on actions that need to be disabled when nothing
	 * is selected.
	 * 
	 * @param b
	 *            enabled status
	 */
	private void setEnabledOnSelectActions(boolean b) {
		toLowerCaseAction.setEnabled(b);
		toUpperCaseAction.setEnabled(b);
		invertCaseAction.setEnabled(b);
		ascSortAction.setEnabled(b);
		descSortAction.setEnabled(b);
		uniqueAction.setEnabled(b);
	}
}
