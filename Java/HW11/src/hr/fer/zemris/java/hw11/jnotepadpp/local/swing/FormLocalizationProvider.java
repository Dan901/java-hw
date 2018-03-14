package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Modification of {@link LocalizationProviderBridge} for usage with
 * {@link JFrame} objects. <br>
 * Apart from the delegate provider, constructor expects a frame and instantly
 * registers a listener to the given provider ({@link #connect()}). <br>
 * When the given frame is about to be closed {@link #disconnect()} method is
 * called. This prevents possible memory leaks.
 * 
 * @author Dan
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Creates a new {@link FormLocalizationProvider} with given arguments.
	 * 
	 * @param delegate
	 *            another {@link ILocalizationProvider} for delegating
	 *            translation requests; one listener is registered
	 * @param frame
	 *            {@link JFrame} which holds listeners to this subject
	 */
	public FormLocalizationProvider(ILocalizationProvider delegate, JFrame frame) {
		super(delegate);
		connect();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}
		});
	}
}
