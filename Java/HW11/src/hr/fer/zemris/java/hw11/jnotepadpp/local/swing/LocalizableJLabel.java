package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * This localizable {@link JLabel} changes its text every time the localization
 * of the {@link ILocalizationProvider} given in the constructor is changed.
 * 
 * @author Dan
 *
 */
public abstract class LocalizableJLabel extends JLabel {

	/** */
	private static final long serialVersionUID = 1L;

	/** Key for obtaining the text displayed by this label. */
	protected String key;

	/** {@link ILocalizationProvider} for obtaining the text */
	private ILocalizationProvider lp;

	/**
	 * Creates a new {@link LocalizableJLabel} with given arguments and
	 * registers itself as a listener for localization changes.
	 * 
	 * @param key
	 *            key for obtaining the text displayed by this label
	 * @param lp
	 *            {@link ILocalizationProvider} for obtaining the text in
	 *            current language
	 */
	public LocalizableJLabel(String key, ILocalizationProvider lp) {
		super();
		this.key = key;
		this.lp = lp;
		lp.addLocalizationListener(() -> {
			updateLabel();
		});
	}

	/**
	 * Called when text displayed by the label needs to update.
	 */
	abstract public void updateLabel();
}
