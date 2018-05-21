package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
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
	 * Reference to icon which is set when file is unmodified
	 */
	private ImageIcon saved;
	/**
	 * Refrence to icon which is set when file is modified
	 */
	private ImageIcon unsaved;

	/**
	 * Default constructor
	 */
	public DefaultMultipleDocumentModel() {
		documents = new ArrayList<>();
		listeners = new ArrayList<>();
		addListeners();
		saved = loadImages("saved.png");
		unsaved = loadImages("unsaved.png");
	}

	/**
	 * Method represents resources stream form image icon loading
	 * 
	 * @param text
	 *            - path
	 * @return new {@link ImageIcon}
	 */
	private ImageIcon loadImages(String text) {
		InputStream is = this.getClass().getResourceAsStream(text);

		if (is == null) {
			System.err.println("Stream is not opened!");
			return null;
		}

		byte[] array = readAllBytes(is);

		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ImageIcon(array);
	}

	/**
	 * Method reads all bytes form input stream
	 * 
	 * @param is
	 *            - input stream
	 * @return bytes loaded form file
	 */
	private byte[] readAllBytes(InputStream is) {
		byte[] ret = new byte[1024];
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int n = 0;
		try {
			while ((n = is.read(ret)) != -1) {
				buffer.write(ret, 0, n);
			}
		} catch (IOException e) {
		}
		return buffer.toByteArray();
	}

	/**
	 * Method returns iterator for passing through all opened documents
	 * 
	 * @return iterator for passing through documents
	 */
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	/**
	 * Method returns new {@link SingleDocumentModel}
	 * 
	 * @return new {@link SingleDocumentModel}
	 */
	@Override
	public SingleDocumentModel createNewDocument() {
		DefaultSingleDocumentModel model = new DefaultSingleDocumentModel(null, "");
		// model.addSingleDocumentListener(this);
		// documents.add(model);
		// model.setModified(true);
		// callListeners(model, null, 2);
		current = model;
		this.add("new", new JScrollPane(model.getTextComponent()));
		documents.add(model);
		this.setSelectedIndex(documents.size() - 1);
		this.setIconAt(this.getSelectedIndex(), unsaved);

		model.addSingleDocumentListener(this);
		model.setModified(true);

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
				this.setSelectedIndex(documents.indexOf(doc));
				return doc;
			}
		}

		// model.addSingleDocumentListener(this);
		// documents.add(model);
		// model.setModified(false);
		//
		documents.add(model);
		this.add(path.getFileName().toString(), new JScrollPane(model.getTextComponent()));
		this.setSelectedIndex(this.getTabCount() - 1);
		this.setIconAt(this.getSelectedIndex(), saved);

		model.addSingleDocumentListener(this);
		model.setModified(false);

		callListeners(documents.get(this.getSelectedIndex()), current, 1);
		current = model;

		// current = model;

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
		this.setTitleAt(this.getSelectedIndex(), newPath.getFileName().toString());
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

		model.removeSingleDocumentListener(this);
		documents.remove(model);

		if (documents.size() != 0) {
			current = documents.get(documents.size() - 1);
		} else {
			current = null;
		}

		this.remove(this.getSelectedComponent());
		callListeners(null, model, 3);
		callListeners(current, null, 2);
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
	 * 
	 * @param l
	 *            - listener we want to remove
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
	 * Method returns document at specific index. Index must be between
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
				if (this.getSelectedIndex() != -1) {
					callListeners(documents.get(this.getSelectedIndex()), current, 1);
				} else {
					callListeners(documents.get(0), current, 1);
				}

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
		// for (SingleDocumentModel document : documents) {
		// if (document.equals(model)) {
		// System.out.println(model.getFilePath());
		// document = model;
		// document.setModified(true);
		// changeListener(document, model);
		// }
		// }

		SingleDocumentModel doc = documents.get(documents.indexOf(model));
		changeListener(doc, model);
		this.setIconAt(documents.indexOf(doc), unsaved);

		callListeners(model, doc, 1);
	}

	/**
	 * Method is called when document path is changed
	 * 
	 * @param model
	 *            - changed document
	 */
	@Override
	public void documentFilePathUpdated(SingleDocumentModel model) {

		SingleDocumentModel doc = documents.get(documents.indexOf(model));
		System.out.println(model.getFilePath().toString());

		if (!doc.getFilePath().equals(model.getFilePath())) {
			doc.setFilePath(model.getFilePath());
			changeListener(doc, model);
			callListeners(model, doc, 1);
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
