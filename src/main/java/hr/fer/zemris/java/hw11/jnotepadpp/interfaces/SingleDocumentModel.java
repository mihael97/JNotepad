package hr.fer.zemris.java.hw11.jnotepadpp.interfaces;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Interface provides methods for single document
 * 
 * @author Mihael
 *
 */
public interface SingleDocumentModel {
	/**
	 * Method returns document content
	 * 
	 * @return document content
	 */
	JTextArea getTextComponent();

	/**
	 * Method returns file location
	 * 
	 * @return file location
	 */
	Path getFilePath();

	/**
	 * Method sets file path
	 * 
	 * @param path
	 *            - new file path
	 */
	void setFilePath(Path path);

	/**
	 * Method returns if file is modified
	 * 
	 * @return
	 */
	boolean isModified();

	/**
	 * Method sets modified status
	 * 
	 * @param modified
	 *            - new modified status
	 */
	void setModified(boolean modified);

	/**
	 * Method adds listener to file
	 * 
	 * @param l
	 *            - new listener
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Method removes listener from file
	 * 
	 * @param l
	 *            - listener for remove
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
