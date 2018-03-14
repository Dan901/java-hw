package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Implementation of {@link ILocalizationProvider} that extends
 * {@link AbstractLocalizationProvider}. Method {@link #getString(String)} reads
 * from a file with translations for current language. <br>
 * This class is a singleton, so instances can be obtained by calling a static
 * method {@link #getInstance()}.
 * 
 * @author Dan
 * @see ResourceBundle
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/** Default language. */
	private static final String DEFAULT_LANGUAGE = "en";

	/** Instance of this class, used for Singleton design pattern. */
	private static LocalizationProvider instance;

	/**
	 * Returns an instance of {@link LocalizationProvider}.
	 * 
	 * @return an instance of this class
	 */
	public static LocalizationProvider getInstance() {
		if (instance == null) {
			instance = new LocalizationProvider();
		}
		return instance;
	}

	/** Current language. */
	private String language;

	/** {@link ResourceBundle} for current language. */
	private ResourceBundle bundle;

	/**
	 * Creates a new {@link LocalizationProvider}.
	 */
	private LocalizationProvider() {
		updateLanguage(DEFAULT_LANGUAGE);
	}

	/**
	 * Updates the {@link #language} and {@link #bundle} with new language.
	 * 
	 * @param language
	 *            new language
	 */
	private void updateLanguage(String language) {
		this.language = language;
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translations", locale);
	}

	/**
	 * Changes the localization of this {@link ILocalizationProvider} based on
	 * given language and notifies all registered {@link ILocalizationListener}
	 * objects.
	 * 
	 * @param language
	 *            new language
	 */
	public void setLanguage(String language) {
		if(this.language.equals(language)){
			return;
		}
		updateLanguage(language);
		fire();
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}

}
