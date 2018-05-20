package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

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
			Document doc = area.getDocument();

			int len = Math.abs(area.getCaret().getDot() - area.getCaret().getMark());

			if (len == 0)
				return;

			int offset = Math.min(area.getCaret().getDot(), area.getCaret().getMark());

			try {

				StringSelection selection = new StringSelection(doc.getText(offset, len));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, null);

				if (flag == true) {
					doc.remove(offset, len);
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

}
