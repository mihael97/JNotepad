package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;

import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;

/**
 * Class represents single document model with content and location
 * 
 * @author Mihael
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	/**
	 * Document content
	 */
	private JTextArea component;
	/**
	 * Path to file location
	 */
	private Path path;
	/**
	 * Indicates if file is changed
	 */
	private boolean modified;
	/**
	 * List of all file listeners
	 */
	private List<SingleDocumentListener> listeners;

	/**
	 * Constructor for initializing new document
	 * 
	 * @param path
	 *            - path to file location
	 * @param content
	 *            - file content
	 * @throws NullPointerException
	 *             - if content is null
	 */
	public DefaultSingleDocumentModel(Path path, String content) {
		this.path = path;
		component = new JTextArea(Objects.requireNonNull(content));
	}

	/**
	 * Method returns file content
	 * 
	 * @return file content in {@link JTextArea}
	 */
	@Override
	public JTextArea getTextComponent() {
		return component;
	}

	/**
	 * Method returns file location
	 * 
	 * @return path which represents file location
	 */
	@Override
	public Path getFilePath() {
		return path;
	}

	/**
	 * Method sets file location
	 * 
	 * @param path
	 *            - new file location
	 */
	@Override
	public void setFilePath(Path path) {
		this.path = Objects.requireNonNull(path);
	}

	/**
	 * Method checks if file is modified
	 * 
	 * @return <code>true</code> if file is modified,otherwise <code>false</code>
	 */
	@Override
	public boolean isModified() {
		return modified;
	}

	/**
	 * Method sets modified index of file
	 * 
	 * @param modified
	 *            - new modified index
	 */
	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * Method adds {@link SingleDocumentListener} to list of listeners if it doesn't
	 * already exist
	 * 
	 * @param new
	 *            {@link SingleDocumentListener}
	 * 
	 * @throws NullPointerException
	 *             - if argument is null
	 */
	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l);
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Method removes {@link SingleDocumentListener} from list if it exists
	 * 
	 * @param l
	 *            - {@link SingleDocumentListener} we want to remove
	 * 
	 * @throws NullPointerException
	 *             - if argument is null
	 */
	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l);
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	/**
	 * Method calculates hashCode of {@link DefaultSingleDocumentModel} instance
	 * 
	 * @return hashCode of this instance
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((listeners == null) ? 0 : listeners.hashCode());
		result = prime * result + (modified ? 1231 : 1237);
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	/**
	 * Method checks if to {@link DefaultSingleDocumentModel} are same. Two instaces
	 * are same if they have same <code>path</code>
	 * 
	 * @param obj
	 *            - object we want to check
	 * @return <code>true</code> if instances are same otherwise <code>false</code>
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DefaultSingleDocumentModel) {
			DefaultSingleDocumentModel doc = (DefaultSingleDocumentModel) obj;

			return doc.getFilePath().equals(path);
		}

		return false;
	}

}
