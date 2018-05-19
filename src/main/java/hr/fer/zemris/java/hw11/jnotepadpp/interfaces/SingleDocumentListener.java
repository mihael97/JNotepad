package hr.fer.zemris.java.hw11.jnotepadpp.interfaces;

/**
 * Interface provides methods for {@link SingleDocumentListener}
 * 
 * @author Mihael
 *
 */
public interface SingleDocumentListener {
	/**
	 * Method informs listener that file modification status is changed
	 * 
	 * @param model
	 *            - changed document
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * Method informs listener that file location is changed
	 * 
	 * @param model
	 *            - changed document
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
