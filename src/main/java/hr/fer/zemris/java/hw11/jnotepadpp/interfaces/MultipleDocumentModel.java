package hr.fer.zemris.java.hw11.jnotepadpp.interfaces;

import java.nio.file.Path;

public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	SingleDocumentModel createNewDocument();

	SingleDocumentModel getCurrentDocument();

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

	void closeDocument(SingleDocumentModel model);

	void addMultipleDocumentListener(MultipleDocumentListener l);

	void removeMultipleDocumentListener(MultipleDocumentListener l);

	int getNumberOfDocuments();

	SingleDocumentModel getDocument(int index);
}
