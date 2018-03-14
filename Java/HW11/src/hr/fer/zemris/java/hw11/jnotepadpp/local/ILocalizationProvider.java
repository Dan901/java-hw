package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface for localization of {@code Strings}. It offers a method
 * {@link #getString(String)} for getting locale-sensitive {@code Strings} based
 * on a key. <br>
 * This interface supports dynamic change of localization and for that purpose
 * also models a subject in observer design pattern, so
 * {@link ILocalizationListener} objects can register (and unregister) to
 * receive notifications when localization has been changed.
 * 
 * @author Dan
 *
 */
public interface ILocalizationProvider {

	/**
	 * Registers an {@link ILocalizationListener} to this subject.
	 * 
	 * @param l
	 *            listener to register
	 */
	void addLocalizationListener(ILocalizationListener l);

	/**
	 * Removes the given {@link ILocalizationListener} from this subject.
	 * 
	 * @param l
	 *            listener to remove
	 */
	void removeLocalizationListener(ILocalizationListener l);

	/**
	 * Returns a locale-sensitive {@code String} based on current localization
	 * setting and given {@code key}.
	 * 
	 * @param key
	 *            key for identifying a unique {@code String}
	 * @return a locale-sensitive {@code String}
	 */
	String getString(String key);

	/**
	 * Returns the current language.
	 * 
	 * @return current language
	 */
	String getCurrentLanguage();
}
