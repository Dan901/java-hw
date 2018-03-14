package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * Abstract {@link LocalizableAction} that modifies the selected lines from a
 * current document displayed in {@link JNotepadPP}. <br>
 * Abstract method {@link #processLines(String[])} is called for processing of
 * selected lines. If only part of the line is selected, the whole line is
 * affected.
 * 
 * @author Dan
 *
 */
public abstract class ModifySelectedLinesAction extends LocalizableAction {

	/** */
	private static final long serialVersionUID = 1L;

	/** {@link ILocalizationProvider} for obtaining the current language. */
	protected ILocalizationProvider lp;

	/** Notepad whose current document's lines are modified. */
	private JNotepadPP notepad;

	/**
	 * Creates a new {@link ModifySelectedLinesAction} with given arguments.
	 * 
	 * @param notepad
	 *            {@link JNotepadPP} whose current document's lines are modified
	 * @param key
	 *            key for obtaining the name of this action
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the name and the
	 *            current language
	 */
	public ModifySelectedLinesAction(JNotepadPP notepad, String key, ILocalizationProvider lp) {
		super(key, lp);
		this.lp = lp;
		this.notepad = notepad;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextArea editor = notepad.getCurrentPanel().getEditor();
		Document doc = editor.getDocument();

		try {
			int start = editor.getLineStartOffset(editor.getLineOfOffset(editor.getSelectionStart()));
			int end = editor.getLineEndOffset(editor.getLineOfOffset(editor.getSelectionEnd()));
			String text = doc.getText(start, end - start);
			String[] lines = text.split("\\n");

			List<String> sortedLines = processLines(lines);

			doc.remove(start, end - start);
			for (String line : sortedLines) {
				int offset = editor.getCaretPosition();
				doc.insertString(offset, line + "\n", null);
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
			return;
		}
	}

	/**
	 * Modifies the selected lines in a current document.
	 * 
	 * @param lines
	 *            all selected lines
	 * @return {@code List} of modified lines that will replace the old ones
	 */
	abstract protected List<String> processLines(String[] lines);

}
