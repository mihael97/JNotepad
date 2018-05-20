package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Class represents action for saving modified contest. Action supports two
 * modes,Save and Save As. Difference is if you have already saved file,you can
 * take mode Save as and after setting of saving location to different
 * position,you will have two files with same contest
 * 
 * @author Mihael
 *
 */
public class SaveDocumentAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Frame from where action is called
	 */
	private JFrame frame;
	/**
	 * Document model
	 */
	private DefaultMultipleDocumentModel documentModel;
	/**
	 * Boolean flag indicates if we want save or save as mode
	 */
	private boolean flag;

	/**
	 * Reference on bridge with {@link LocalizationProvider}
	 */
	private LocalizationProviderBridge provider;

	/**
	 * Default constructor. If third argument is <code>true</code>, mode is Save as.
	 * Otherwise mode is Save
	 * 
	 * @param frame
	 *            - frame
	 * @param documentModel
	 *            - document model
	 * @param flag
	 *            - flag which indicates mode we want
	 * @param provider
	 *            - language provider
	 * 
	 * @throws NullPointerException
	 *             - if any of arguments is null
	 */
	public SaveDocumentAction(JFrame frame, DefaultMultipleDocumentModel documentModel, boolean flag,
			LocalizationProviderBridge provider) {
		this.frame = Objects.requireNonNull(frame);
		this.documentModel = Objects.requireNonNull(documentModel);
		this.flag = flag;
		this.provider = Objects.requireNonNull(provider);
	}

	/**
	 * Method implements action for file saving on disc
	 * 
	 * @param event
	 *            - {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent event) {

		SingleDocumentModel document = documentModel.getCurrentDocument();

		if (document.getFilePath() == null || flag == true) {
			JFileChooser jfc = new JFileChooser();

			jfc.setDialogTitle("Save document");
			if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(frame, provider.getString("exit4"), provider.getString("error"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			document.setFilePath(jfc.getSelectedFile().toPath());

		}

		documentModel.saveDocument(document, document.getFilePath());
		JOptionPane.showMessageDialog(frame, provider.getString("fileStored"), provider.getString("information"),
				JOptionPane.INFORMATION_MESSAGE);

	}

}
