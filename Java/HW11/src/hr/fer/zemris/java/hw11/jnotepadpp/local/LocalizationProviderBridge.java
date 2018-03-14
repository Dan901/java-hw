package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This class acts like a decorator for another {@link ILocalizationProvider}.
 * <br>
 * Delegate provider is expected in the constructor and requests for translation
 * are delegated to the given delegate. <br>
 * After creation of this object, method {@link #connect()} should be called to
 * register a listener to the given delegate provider so it can forward all
 * notifications to the listeners registered to this object. <br>
 * {@link #disconnect()} method is also available when notifications are no
 * longer required.
 * 
 * @author Dan
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/** Another provider for delegating translation requests. */
	private ILocalizationProvider delegate;

	/**
	 * Listener that will register to the {@link #delegate} when
	 * {@link #connect()} is called.
	 */
	private ILocalizationListener listener;

	/**
	 * {@code True} if the {@link #listener} is registered to the
	 * {@link #delegate}.
	 */
	private boolean connected;

	/**
	 * Creates a new {@link LocalizationProviderBridge} with given argument.
	 * 
	 * @param delegate
	 *            another {@link ILocalizationProvider} for delegating
	 *            translation requests
	 */
	public LocalizationProviderBridge(ILocalizationProvider delegate) {
		this.delegate = delegate;
	}

	/**
	 * Connects to the {@link ILocalizationProvider} given in the constructor.
	 * When connected all notifications from the provider will be forwarded to
	 * listeners registered on this object. If it is already connected, nothing
	 * happens.
	 */
	public void connect() {
		if (connected) {
			return;
		}
		listener = () -> fire();
		delegate.addLocalizationListener(listener);
		connected = true;
	}

	/**
	 * Disconnects from the {@link ILocalizationProvider} given in the
	 * constructor. When disconnected, notifications from the provider will not
	 * be forwarded to the listeners registered on this object.
	 */
	public void disconnect() {
		if (connected) {
			delegate.removeLocalizationListener(listener);
			connected = false;
		}
	}

	@Override
	public String getString(String key) {
		return delegate.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return delegate.getCurrentLanguage();
	}

}
