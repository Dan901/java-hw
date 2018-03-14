package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * Abstract {@link LocalizableAction} for changing the letter case of selected
 * text from the current document in {@link JNotepadPP}. <br>
 * Abstract method {@link #processChar(char)} is called on every character in
 * the text.
 * 
 * @author Dan
 *
 */
public abstract class ChangeCaseAction extends LocalizableAction {

	/** */
	private static final long serialVersionUID = 1L;

	/** Notepad whose current document's text is changed. */
	private JNotepadPP notepad;

	/**
	 * Creates a new {@link ChangeCaseAction} with given arguments.
	 * 
	 * @param notepad
	 *            {@link JNotepadPP} whose current document's text is changed
	 * @param key
	 *            key for obtaining the name of this action
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the name
	 */
	public ChangeCaseAction(JNotepadPP notepad, String key, ILocalizationProvider lp) {
		super(key, lp);
		this.notepad = notepad;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextArea editor = notepad.getCurrentPanel().getEditor();
		Document doc = editor.getDocument();

		int offset = editor.getSelectionStart();
		int len = editor.getSelectionEnd() - offset;

		try {
			String text = doc.getText(offset, len);
			char[] chars = text.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				chars[i] = processChar(chars[i]);
			}
			text = new String(chars);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Called for each character in the selected text from the document.
	 * 
	 * @param c
	 *            character
	 * @return processed character
	 */
	abstract protected char processChar(char c);
}
