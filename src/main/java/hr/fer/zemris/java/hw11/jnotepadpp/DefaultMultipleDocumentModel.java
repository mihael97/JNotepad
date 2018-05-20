package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Method presents set of {@link SingleDocumentModel}
 * 
 * @author Mihael
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel, SingleDocumentListener {

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
		addListeners();
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
		model.addSingleDocumentListener(this);
		model.setModified(true);
		documents.add(model);
		callListeners(model, null, 2);
		current = model;
		this.add("new", model.getTextComponent());
		this.setSelectedIndex(documents.size() - 1);
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

			throw new IllegalArgumentException(
					LocalizationProvider.getInstance().getString("errorReading") + path.toAbsolutePath());
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

		model.addSingleDocumentListener(this);
		model.setModified(false);
		documents.add(model);

		// callListeners(model, null, 2);
		// callListeners(model, current, 1);
		current = model;
		this.add(path.getFileName().toString(), new JScrollPane(current.getTextComponent()));
		this.setSelectedIndex(this.getTabCount() - 1);

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

		if (model.getFilePath() != null && model.getFilePath() != newPath) {
			throw new IllegalArgumentException(LocalizationProvider.getInstance().getString("alreadyOpened"));
		}

		try (BufferedWriter stream = new BufferedWriter(Files.newBufferedWriter(newPath))) {
			stream.write(model.getTextComponent().getText());
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		model.setModified(false);
		this.getSelectedComponent().setName(newPath.getFileName().toString());
		callListeners(model, current, 1);
		current = model;
	}

	/**
	 * Method closes document form list of active documents
	 * 
	 * @param model
	 *            - document we want to close
	 */
	@Override
	public void closeDocument(SingleDocumentModel model) {
		if (documents.size() == 0) {
			return;
		}

		documents.remove(model);
		if (documents.size() != 0)
			current = documents.get(documents.size() - 1);
		else
			current = null;
		callListeners(null, model, 3);
		callListeners(current, null, 2);
		this.remove(this.getSelectedComponent());
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
			throw new IllegalArgumentException(LocalizationProvider.getInstance().getString("document1") + index
					+ LocalizationProvider.getInstance().getString("document2") + documents.size());
		}

		return documents.get(index);
	}

	/**
	 * Method informs every listener about last change
	 * 
	 * @param current
	 *            - current document
	 * @param previous
	 *            - changed document
	 * 
	 * @param type
	 *            - type of change
	 */
	private void callListeners(SingleDocumentModel current, SingleDocumentModel previous, int type) {
		for (MultipleDocumentListener listener : listeners) {
			switch (type) {
			case 1:
				listener.currentDocumentChanged(previous, current);
			case 2:

				listener.documentAdded(current);
				break;
			case 3:
				listener.documentRemoved(previous);
			default:
				break;
			}
		}
	}

	/**
	 * Private method which adds listeners to tabbed pane
	 */
	private void addListeners() {
		this.addChangeListener(e -> {

			if (documents.size() != 0) {
				if (this.getSelectedIndex() != -1)
					callListeners(documents.get(this.getSelectedIndex()), current, 1);
				else
					callListeners(documents.get(0), current, 1);

				current = documents.get(this.getSelectedIndex());
			}
		});
	}

	/**
	 * Method is called when modification status is changed
	 * 
	 * @param model
	 *            - changed document
	 */
	@Override
	public void documentModifyStatusUpdated(SingleDocumentModel model) {
		for (SingleDocumentModel document : documents) {
			if (document.equals(model)) {
				System.out.println(model.getFilePath());
				document = model;
				document.setModified(true);
				changeListener(document, model);
			}
		}
	}

	/**
	 * Method is called when document path is changed
	 * 
	 * @param model
	 *            - changed document
	 */
	@Override
	public void documentFilePathUpdated(SingleDocumentModel model) {
		for (SingleDocumentModel document : documents) {
			if (document.equals(model)) {
				System.out.println(model.getFilePath());
				changeListener(document, model);
				callListeners(model, document, 1);
				document.setFilePath(model.getFilePath());
			}
		}
	}

	/**
	 * Method switches listener from one {@link SingleDocumentModel} to other
	 * 
	 * @param previous
	 *            - previous document
	 * @param next
	 *            - new document
	 */
	private void changeListener(SingleDocumentModel previous, SingleDocumentModel next) {
		previous.removeSingleDocumentListener(this);
		next.addSingleDocumentListener(this);
	}

}
