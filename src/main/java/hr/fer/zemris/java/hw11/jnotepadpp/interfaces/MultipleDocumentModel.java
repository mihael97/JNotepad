package hr.fer.zemris.java.hw11.jnotepadpp.interfaces;

import java.nio.file.Path;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;

/**
 * Interface provides methods for {@link DefaultMultipleDocumentModel}
 * 
 * @author Mihael
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * Method creates new blank document
	 * 
	 * @return blank document
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Method returns current active document
	 * 
	 * @return current active document
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Method loads document from disc
	 * 
	 * @param path
	 *            - path from where we load document
	 * @return loaded document
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Method saves document do given path
	 * 
	 * @param model
	 *            - document
	 * @param newPath
	 *            - location
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Method closes current active document
	 * 
	 * @param model
	 *            - document we want to close
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Method adds document listener
	 * 
	 * @param l
	 *            - new document listener
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Method removes document listener
	 * 
	 * @param l
	 *            - document listener we want to remove
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Method return number of active documents
	 * 
	 * @return number of active documents
	 */
	int getNumberOfDocuments();

	/**
	 * Method returns active document on given index
	 * 
	 * @param index
	 *            - index of document we want
	 * @return document at specific index
	 */
	SingleDocumentModel getDocument(int index);
}
