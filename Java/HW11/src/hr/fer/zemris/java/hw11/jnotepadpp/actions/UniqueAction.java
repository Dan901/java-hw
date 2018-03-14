package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * This {@link LocalizableAction} removes the duplicate lines from currently
 * selected text in a document displayed in {@link JNotepadPP}.
 * 
 * @author Dan
 *
 */
public class UniqueAction extends ModifySelectedLinesAction {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link UniqueAction} with given arguments.
	 * 
	 * @param notepad
	 *            {@link JNotepadPP} whose current document's lines are modified
	 * @param key
	 *            key for obtaining the name of this action
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the name
	 */
	public UniqueAction(JNotepadPP notepad, String key, ILocalizationProvider lp) {
		super(notepad, key, lp);
	}

	@Override
	protected List<String> processLines(String[] lines) {
		return Arrays.stream(lines).distinct().collect(Collectors.toList());
	}

}
