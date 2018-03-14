package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * This {@link LocalizableAction} sorts the currently selected lines from a
 * document displayed in {@link JNotepadPP}. <br>
 * {@link Collator} is used for localized sorting.
 * 
 * @author Dan
 * @see ModifySelectedLinesAction
 */
public class SortAction extends ModifySelectedLinesAction {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Sort type; {@code true} if ascending sort is required; {@code false} for
	 * descending
	 */
	private boolean ascending;

	/**
	 * Creates a new {@link SortAction} with given arguments.
	 * 
	 * @param ascending
	 *            sort type; {@code true} if ascending sort is required;
	 *            {@code false} for descending
	 * @param notepad
	 *            {@link JNotepadPP} whose current document's lines are sorted
	 * @param key
	 *            key for obtaining the name of this action and the current
	 *            language
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the name
	 */
	public SortAction(boolean ascending, JNotepadPP notepad, String key, ILocalizationProvider lp) {
		super(notepad, key, lp);
		this.ascending = ascending;
	}

	@Override
	protected List<String> processLines(String[] lines) {
		Locale locale = new Locale(lp.getCurrentLanguage());
		Collator collator = Collator.getInstance(locale);
		int type = ascending ? 1 : -1;
		return Arrays.stream(lines).sorted((l1, l2) -> type * collator.compare(l1, l2)).collect(Collectors.toList());
	}

}
