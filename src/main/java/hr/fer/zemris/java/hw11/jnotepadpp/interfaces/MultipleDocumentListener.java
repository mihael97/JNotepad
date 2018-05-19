package hr.fer.zemris.java.hw11.jnotepadpp.interfaces;

/**
 * Interface provides method for {@link MultipleDocumentListener} where it is
 * informed that change has happened
 * 
 * @author Mihael
 *
 */
public interface MultipleDocumentListener {
	/**
	 * Method gives informations after current active document is modified
	 * 
	 * @param previousModel
	 *            - reference to previous state of document
	 * @param currentModel
	 *            - reference to current document
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * Method gives information when new document is added
	 * 
	 * @param model
	 *            - added document
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Method gives information that document is removed
	 * 
	 * @param model
	 *            - removed document
	 */
	void documentRemoved(SingleDocumentModel model);
}
