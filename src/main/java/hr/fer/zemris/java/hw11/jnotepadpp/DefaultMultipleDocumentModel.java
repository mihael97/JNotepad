package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTabbedPane;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;

/**
 * Method presents set of {@link SingleDocumentModel}
 * 
 * @author Mihael
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of all documents
	 */
	private List<SingleDocumentModel> documents;
	/**
	 * List of all listeners
	 */
	private List<MultipleDocumentListener> listeners;
	/**
	 * Reference to current opened document
	 */
	private SingleDocumentModel current;

	/**
	 * Default constructor
	 */
	public DefaultMultipleDocumentModel() {
		documents = new ArrayList<>();
		listeners = new ArrayList<>();
	}

	/**
	 * Method returns iterator for passing through all opened documents
	 * 
	 * @return iterator for passing through documents
	 */
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return null;
	}

	/**
	 * Method returns new {@link SingleDocumentModel}
	 * 
	 * @return new {@link SingleDocumentModel}
	 */
	@Override
	public SingleDocumentModel createNewDocument() {
		DefaultSingleDocumentModel model = new DefaultSingleDocumentModel(null, "");
		documents.add(model);
		this.add("new", model.getTextComponent());
		return model;
	}

	/**
	 * Method returns current active {@link SingleDocumentModel}
	 * 
	 * @return current active {@link SingleDocumentModel}
	 */
	@Override
	public SingleDocumentModel getCurrentDocument() {
		return current;
	}

	/**
	 * Method loads document from disc
	 * 
	 * @param path
	 *            - path form where we read our file
	 */
	@Override
	public SingleDocumentModel loadDocument(Path path) {

		byte[] context;

		try {
			context = Files.readAllBytes(path);
		} catch (Exception e) {

			throw new IllegalArgumentException("Error during reading of file " + path.toAbsolutePath());
		}

		String text = new String(context, StandardCharsets.UTF_8);

		DefaultSingleDocumentModel model = new DefaultSingleDocumentModel(path, text);

		for (SingleDocumentModel doc : documents) {
			if (doc.equals(model)) {
				current = model;
				this.setSelectedIndex(documents.indexOf(model));
				return doc;
			}
		}

		documents.add(model);
		current = model;

		this.add(path.getFileName().toString(), current.getTextComponent());
		this.setSelectedIndex(this.getTabCount() - 1);

		callListeners(model, 2);
		return model;

	}

	/**
	 * Method saves document to given path location
	 * 
	 * @param model
	 *            - document we want to save
	 * @param newPath
	 *            - path location where we will store our content
	 * @throws IllegalArgumentException
	 *             - if there is some data stored on path location or there is
	 *             exception during writing
	 */
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if (Files.exists(newPath)) {
			throw new IllegalArgumentException("Document on path " + newPath + " already exists!");
		}

		try (BufferedWriter stream = new BufferedWriter(Files.newBufferedWriter(newPath))) {
			stream.write(model.getTextComponent().getText());
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Method closes document form list of active documents
	 * 
	 * @param model
	 *            - document we want to close
	 */
	@Override
	public void closeDocument(SingleDocumentModel model) {
		documents.remove(model);
	}

	/**
	 * Method adds {@link MultipleDocumentListener} to list
	 * 
	 * @param l
	 *            - {@link MultipleDocumentListener} we want to add
	 */
	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Method removes {@link MultipleDocumentListener} from list
	 */
	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	/**
	 * Method returns number of stored documents
	 * 
	 * @return number of stored documents
	 */
	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	/**
	 * Method return document at specific index. Index must be between
	 * <code>0</code> and <code>content.size-1</code>
	 * 
	 * @return {@link SingleDocumentModel} at specific index
	 * 
	 * @throws IllegalArgumentException
	 *             - if index is not valid(index is valid when it is between
	 *             <code>0</code> and <code>content.size-1</code>)
	 */
	@Override
	public SingleDocumentModel getDocument(int index) {

		if (documents.size() - 1 < index) {
			throw new IllegalArgumentException("Argument is " + index + " but size is " + documents.size());
		}

		return documents.get(index);
	}

	/**
	 * Method informs every listener about last change
	 * 
	 * @param model
	 *            - changed document
	 * @param type
	 *            - type of change
	 */
	private void callListeners(DefaultSingleDocumentModel model, int type) {
		for (MultipleDocumentListener listener : listeners) {
			switch (type) {
			case 2:

				listener.documentAdded(model);
				break;

			default:
				break;
			}
		}
	}

}
