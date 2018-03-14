package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class that implements {@link ILocalizationProvider} with implemented
 * methods for registering and removing {@link ILocalizationListener} objects.
 * It also adds a new method {@link #fire()} which informs all registered
 * listeners that a change occurred. <br>
 * Classes that need to implement {@link ILocalizationProvider} interface can
 * extend this class and only take care of the localization method while
 * listener handling is done here.
 * 
 * @author Dan
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/** List for storing currently registered listeners. */
	private List<ILocalizationListener> listeners;

	/**
	 * Creates a new {@link AbstractLocalizationProvider}.
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}

	/**
	 * Notifies all currently registered listeners that localization changed by
	 * calling their method {@link ILocalizationListener#localizationChanged()}.
	 */
	public void fire() {
		new ArrayList<>(listeners).forEach(l -> l.localizationChanged());
	}

}
