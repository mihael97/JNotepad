package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.DefaultSingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Class represents action for save document closing
 * 
 * @author Mihael
 *
 */
public class ExitAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Document model where active documents are stored
	 */
	private DefaultMultipleDocumentModel documentModel;
	/**
	 * {@link JFrame} from where we called action
	 */
	private JFrame frame;

	/**
	 * Reference to bridge with {@link LocalizationProvider}
	 */
	private LocalizationProviderBridge provider;

	/**
	 * Default constructor
	 * 
	 * @param documentModel
	 *            - document mode
	 * @param frame
	 *            - frame
	 * @param provider
	 *            - reference to bridge with {@link LocalizationProvider}
	 * 
	 * @throws NullPointerException
	 *             - if any of arguments is null
	 */
	public ExitAction(DefaultMultipleDocumentModel documentModel, JFrame frame, LocalizationProviderBridge provider) {
		this.documentModel = Objects.requireNonNull(documentModel);
		this.frame = Objects.requireNonNull(frame);
		this.provider = Objects.requireNonNull(provider);
	}

	/**
	 * Method implements action for document closing <br>
	 * If any of active documents is modified but not saved,user will be asked if he
	 * want to save unsaved file
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {

		for (int i = 0, length = documentModel.getNumberOfDocuments(); i < length; i++) {
			SingleDocumentModel doc = documentModel.getDocument(i);

			if (doc.isModified()) {
				int response = JOptionPane.showConfirmDialog(frame,
						provider.getString("exit1") + doc.getFilePath() + provider.getString("exit2"), "Modified file",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (response == JOptionPane.YES_OPTION) {

					Path path;

					if (doc.getFilePath() == null) {
						JFileChooser jfc = new JFileChooser();

						jfc.setDialogTitle(provider.getString("exit3"));
						if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
							JOptionPane.showMessageDialog(frame, provider.getString("exit4"),
									provider.getString("error"), JOptionPane.WARNING_MESSAGE);
							return;
						}

						path = jfc.getSelectedFile().toPath();
					} else {
						path = doc.getFilePath();
					}

					documentModel.saveDocument(new DefaultSingleDocumentModel(path, doc.getTextComponent().getText()),
							path);
				} else if (response == JOptionPane.NO_OPTION) {
					continue;
				} else {
					return;
				}
			}
		}

		provider.disconnect();
		System.exit(1);
	}
}
