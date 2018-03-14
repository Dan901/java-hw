package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import javax.swing.JToolBar;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * This localizable {@link JToolBar} changes its name every time the
 * localization of the {@link ILocalizationProvider} given in the constructor is
 * changed.
 * 
 * @author Dan
 *
 */
public class LocalizableJToolbar extends JToolBar {
	
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link LocalizableJToolbar} with given arguments and
	 * registers itself as a listener for localization changes.
	 * 
	 * @param key
	 *            key for obtaining the name of this action
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the name in
	 *            current language
	 */
	public LocalizableJToolbar(String key, ILocalizationProvider lp) {
		super(lp.getString(key));
		lp.addLocalizationListener(() -> {
			setName(lp.getString(key));
		});
	}
}
