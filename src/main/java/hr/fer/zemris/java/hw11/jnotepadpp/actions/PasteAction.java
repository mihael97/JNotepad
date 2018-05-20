package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

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
		Document document = model.getCurrentDocument().getTextComponent().getDocument();

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		DataFlavor flavor = DataFlavor.stringFlavor;

		if (clipboard.isDataFlavorAvailable(flavor)) {
			try {
				document.insertString(model.getCurrentDocument().getTextComponent().getCaretPosition(),
						(String) clipboard.getData(flavor), null);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedFlavorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
