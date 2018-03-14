package hr.fer.zemris.java.hw11.jnotepadpp;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

/**
 * Implementation of {@link DocumentListener} that can be used when dealing with
 * {@link PlainDocument} models. <br>
 * It has one abstract method {@link #actionPerformed()} that is called when
 * something is inserted or removed from the document. <br>
 * Method {@link #changedUpdate(DocumentEvent)} from the interface is empty.
 * 
 * @author Dan
 *
 */
public abstract class AbstractDocumentListener implements DocumentListener {

	/**
	 * Called when something is inserted or removed from the document.
	 */
	abstract public void actionPerformed();

	@Override
	public void insertUpdate(DocumentEvent e) {
		actionPerformed();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		actionPerformed();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

}
