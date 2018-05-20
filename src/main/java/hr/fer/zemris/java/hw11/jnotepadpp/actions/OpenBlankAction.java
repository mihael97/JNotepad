package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;

import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;

/**
 * Class represents action for new blank document opening
 * 
 * @author Mihael
 *
 */
public class OpenBlankAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link MultipleDocumentModel} where we will open our document
	 */
	private MultipleDocumentModel model;

	/**
	 * Default constructor
	 * 
	 * @param model
	 *            - {@link MultipleDocumentModel}
	 * @param provider 
	 * 
	 * @throws NullPointerException
	 *             - if argument is <code>null</code>
	 */
	public OpenBlankAction(MultipleDocumentModel model) {
		this.model = Objects.requireNonNull(model);
	}

	/**
	 * Method implements action for new blank document opening
	 * 
	 * @param event
	 *            - {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		model.createNewDocument();
	}

}
