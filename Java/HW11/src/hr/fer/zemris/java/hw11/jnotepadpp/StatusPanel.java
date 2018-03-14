package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableJLabel;

/**
 * {@link JPanel} displaying the status bar of a {@link JNotepadPP}. Consist of
 * a label with current document length, label with caret position parameters
 * and a clock. {@link LocalizableJLabel} is used for labels.
 * 
 * @author Dan
 *
 */
public class StatusPanel extends JPanel {

	/** */
	private static final long serialVersionUID = 1L;

	/** {@link ILocalizationProvider} for obtaining the text for labels. */
	private ILocalizationProvider lp;

	/** Timer for the clock. */
	private Timer timer;

	/** Panel with the document that is currently monitored. */
	private EditorPanel panel;

	/** Label with length information. */
	private LocalizableJLabel length;

	/** Listener for {@link #length} label. */
	private DocumentListener lengthListener = new AbstractDocumentListener() {
		@Override
		public void actionPerformed() {
			length.updateLabel();
		}
	};

	/** Label with caret position parameters. */
	private LocalizableJLabel position;

	/** Listener for {@link #position} label. */
	private CaretListener positionListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			position.updateLabel();
		}
	};

	/**
	 * Creates a new {@link StatusPanel} with given arguments and register
	 * necessary listeners on the {@code notepad.}
	 * 
	 * @param notepad
	 *            {@link JNotepadPP} that this status bar is on
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the text for
	 *            labels
	 */
	public StatusPanel(JNotepadPP notepad, ILocalizationProvider lp) {
		this.lp = lp;

		notepad.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				timer.stop();
			}
		});

		notepad.addTabChangeListener(e -> {
			panel = notepad.getCurrentPanel();
			if (panel != null) {
				JTextArea editor = panel.getEditor();
				editor.getDocument().addDocumentListener(lengthListener);
				editor.addCaretListener(positionListener);
			}
			length.updateLabel();
			position.updateLabel();
		});

		initGUI();
	}

	/**
	 * Initializes the GUI and creates necessary components.
	 */
	private void initGUI() {
		setLayout(new GridLayout(1, 3));
		setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));

		length = new LocalizableJLabel("length", lp) {
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			public void updateLabel() {
				int len;
				if (panel == null) {
					len = 0;
				} else {
					len = panel.getEditor().getText().length();
				}
				setText(String.format(lp.getString(key), len));
			}
		};
		length.updateLabel();
		add(length);

		position = new LocalizableJLabel("position", lp) {
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			public void updateLabel() {
				int ln, col, sel;
				if (panel == null) {
					ln = col = sel = 0;
				} else {
					JTextArea editor = panel.getEditor();
					int offset = editor.getCaretPosition();
					try {
						ln = editor.getLineOfOffset(offset);
						col = offset - editor.getLineStartOffset(ln) + 1;
						ln++;
					} catch (BadLocationException e) {
						e.printStackTrace();
						return;
					}
					sel = Math.abs(editor.getSelectionEnd() - editor.getSelectionStart());

				}
				setText(String.format(lp.getString(key), ln, col, sel));
			}
		};
		position.updateLabel();
		add(position);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		JLabel clock = new JLabel();
		timer = new Timer(500, e -> {
			clock.setText(formatter.format(LocalDateTime.now()));
		});
		timer.setInitialDelay(0);
		timer.start();
		add(clock);
	}

}
