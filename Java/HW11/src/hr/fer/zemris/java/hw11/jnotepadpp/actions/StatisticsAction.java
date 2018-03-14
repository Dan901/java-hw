package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * {@link LocalizableAction} that displays statistics for the current document
 * in the {@link JNotepadPP} as a pop-up dialog.
 * 
 * @author Dan
 *
 */
public class StatisticsAction extends LocalizableAction {

	/** */
	private static final long serialVersionUID = 1L;

	/** Notepad for whose current document the statistics are displayed. */
	private JNotepadPP notepad;

	/**
	 * Creates a new {@link StatisticsAction} with given arguments.
	 * 
	 * @param notepad
	 *            {@link JNotepadPP} for whose current document the statistics
	 *            are displayed.
	 * @param key
	 *            key for obtaining the name of this action
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the name
	 */
	public StatisticsAction(JNotepadPP notepad, String key, ILocalizationProvider lp) {
		super(key, lp);
		this.notepad = notepad;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LocalizationProvider lp = LocalizationProvider.getInstance();
		JTextArea editor = notepad.getCurrentPanel().getEditor();
		String text = editor.getText();
		long nonBlank = text.chars().filter(c -> !Character.isWhitespace(c)).count();
		String message = String.format(lp.getString("statText"), text.length(), nonBlank, editor.getLineCount());

		JOptionPane.showMessageDialog(notepad, message, lp.getString("stat"), JOptionPane.INFORMATION_MESSAGE);
	}

}
