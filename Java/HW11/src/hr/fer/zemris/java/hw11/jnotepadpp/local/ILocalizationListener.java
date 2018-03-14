package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface for listeners on {@link ILocalizationProvider} subject. It offers a
 * single method {@link #localizationChanged()} which is called by the subject
 * when localization changes.
 * 
 * @author Dan
 *
 */
public interface ILocalizationListener {

	/**
	 * Called by the {@link ILocalizationProvider} subject when localization is
	 * changed.
	 */
	void localizationChanged();
}
