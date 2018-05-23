package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;

/**
 * Class implements action for text pasting
 * 
 * @author Mihael
 *
 */
public class PasteAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link MultipleDocumentModel} where current active file is stored
	 */
	private MultipleDocumentModel model;

	/**
	 * Constructor for paste operation
	 * 
	 * @param model
	 *            - document model
	 * 
	 * @throws NullPointerException
	 *             - if argument is null
	 */
	public PasteAction(MultipleDocumentModel model) {
		this.model = Objects.requireNonNull(model);
	}

	/**
	 * Method implements action for text pasting form clipboard
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (model.getCurrentDocument() != null) {
			model.getCurrentDocument().getTextComponent().paste();
		}

	}

}
