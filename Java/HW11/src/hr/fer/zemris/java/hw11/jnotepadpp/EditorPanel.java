package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.nio.file.Path;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * {@link JPanel} that wraps {@link JTextArea} into a {@link ScrollPane} and
 * serves as a view for a document. <br>
 * It also has a {@link Path} property representing the file path of the
 * document.
 * 
 * @author Dan
 *
 */
public class EditorPanel extends JPanel {

	/** */
	private static final long serialVersionUID = 1L;

	/** View of the document displayed in this panel. */
	private JTextArea editor;

	/** File path of this document. */
	private Path filePath;

	/**
	 * Creates a new {@link EditorPanel} with given arguments.
	 * 
	 * @param text
	 *            {@code String} containing text that needs to be displayed
	 * @param filePath
	 *            file path if the text was read from a file; or {@code null}
	 */
	public EditorPanel(String text, Path filePath) {
		this.filePath = filePath;
		setLayout(new BorderLayout());
		editor = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(editor);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * @return the {@link JTextArea} view of the document
	 */
	public JTextArea getEditor() {
		return editor;
	}

	/**
	 * @return file path of this document; or {@code null} if no file path is
	 *         set
	 */
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path for this document.
	 * 
	 * @param filePath
	 *            new file path; {@code null} is a legal value
	 */
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

}
