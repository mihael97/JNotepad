package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;

/**
 * Class represents action for text cutting and copying
 * 
 * @author Mihael
 *
 */
public class CopyCutAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link MultipleDocumentModel} where current document is stored
	 */
	private MultipleDocumentModel documentModel;

	/**
	 * Boolean flag which shows if we want to cut text or just copy
	 */
	private boolean flag;

	/**
	 * Default constructor for {@link CopyCutAction} initialization. If second
	 * argument is <code>true</code>, method will perform cutting operation.
	 * Otherwise it will perform copy operation
	 * 
	 * @param model
	 * @param flag
	 */
	public CopyCutAction(MultipleDocumentModel model, boolean flag) {
		this.documentModel = Objects.requireNonNull(model);
		this.flag = flag;
	}

	/**
	 * Method implements action for text cutting and copying
	 * 
	 * @param event
	 *            - {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent event) {

		if (documentModel.getCurrentDocument() != null) {
			JTextArea area = documentModel.getCurrentDocument().getTextComponent();

			if (flag == true) {
				area.cut();
			} else {
				area.copy();
			}


		}
	}

}
