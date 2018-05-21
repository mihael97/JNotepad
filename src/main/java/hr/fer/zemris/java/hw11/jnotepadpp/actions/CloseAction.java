package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.Util;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

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
	private DefaultMultipleDocumentModel documentModel;

	/**
	 * Frame from where close action is called
	 */
	private JFrame frame;

	/**
	 * Language provider
	 */
	private LocalizationProviderBridge provider;

	/**
	 * Default constructor
	 * 
	 * @param documentModel
	 *            - {@link MultipleDocumentModel}
	 * @param provider
	 *            - language provider
	 * @param frame
	 *            - frame
	 * 
	 * @throws NullPointerException
	 *             - if document model is <code>null</code>
	 */
	public CloseAction(DefaultMultipleDocumentModel documentModel, JFrame frame, LocalizationProviderBridge provider) {
		this.documentModel = Objects.requireNonNull(documentModel);
		this.frame = Objects.requireNonNull(frame);
		this.provider = Objects.requireNonNull(provider);
	}

	/**
	 * Method performs action for document closing
	 * 
	 * @param event
	 *            - {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		SingleDocumentModel doc = documentModel.getCurrentDocument();

		if (doc.isModified()) {
			int response = JOptionPane.showConfirmDialog(frame,
					provider.getString("exit1") + doc.getFilePath() + provider.getString("exit2"), "Modified file",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				Util.getPath(frame, doc, provider, documentModel);
			} else if (response == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		documentModel.closeDocument(doc);
	}

}
