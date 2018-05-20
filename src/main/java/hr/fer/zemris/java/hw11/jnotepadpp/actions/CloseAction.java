package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;

import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;

/**
 * Class represents action for active document closing
 * 
 * @author Mihael
 *
 */
public class CloseAction extends AbstractAction {

	/**
	 * serialVerisonUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link MultipleDocumentModel} where current document is stored
	 */
	private MultipleDocumentModel documentModel;

	/**
	 * Default constructor
	 * 
	 * @param documentModel
	 *            - {@link MultipleDocumentModel}
	 * 
	 * @throws NullPointerException
	 *             - if document model is <code>null</code>
	 */
	public CloseAction(MultipleDocumentModel documentModel) {
		this.documentModel = Objects.requireNonNull(documentModel);
	}

	/**
	 * Method performs action for document closing
	 * 
	 * @param event
	 *            - {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		documentModel.closeDocument(documentModel.getCurrentDocument());
	}

}
