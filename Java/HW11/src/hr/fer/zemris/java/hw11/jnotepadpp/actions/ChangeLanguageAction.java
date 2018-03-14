package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;

/**
 * {@link LocalizableAction} that changes the current language on the
 * {@link LocalizationProvider} to the one given in the constructor.
 * 
 * @author Dan
 *
 */
public class ChangeLanguageAction extends LocalizableAction {

	/** */
	private static final long serialVersionUID = 1L;

	/** Language that this action sets. */
	private String language;

	/**
	 * Creates a new {@link ChangeLanguageAction} with given arguments.
	 * 
	 * @param language
	 *            language that this action sets
	 * @param key
	 *            key for obtaining the name of this action
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the name
	 */
	public ChangeLanguageAction(String language, String key, ILocalizationProvider lp) {
		super(key, lp);
		this.language = language;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LocalizationProvider lp = LocalizationProvider.getInstance();
		if (lp.getCurrentLanguage().equals(language)) {
			return;
		}
		lp.setLanguage(language);
	}

}
