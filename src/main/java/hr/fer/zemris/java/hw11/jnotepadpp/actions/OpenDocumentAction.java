package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Public class represents action for document opening
 * 
 * @author Mihael
 *
 */
public class OpenDocumentAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to frame where action will be used
	 */
	private JNotepadPP frame;

	/**
	 * Reference to {@link MultipleDocumentModel} where all function implementations
	 * are stored
	 */
	private MultipleDocumentModel model;

	/**
	 * Reference to bridge with {@link LocalizationProvider}
	 */
	private LocalizationProviderBridge provider;

	/**
	 * Public constructor which accepts frame where action is performed
	 * 
	 * @param frame
	 *            - frame
	 * @param documentModel
	 *            - document model
	 * @param provider
	 *            - language provider
	 */
	public OpenDocumentAction(JNotepadPP frame, MultipleDocumentModel documentModel,
			LocalizationProviderBridge provider) {
		this.frame = Objects.requireNonNull(frame);
		this.model = Objects.requireNonNull(documentModel);
		this.provider = Objects.requireNonNull(provider);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser fc = new JFileChooser();

		fc.setDialogTitle("Open file");

		if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File fileName = fc.getSelectedFile();
		Path filePath = fileName.toPath();

		if (!Files.isReadable(filePath)) {
			JOptionPane.showMessageDialog(frame,
					provider.getString("exit1") + fileName.getAbsolutePath() + provider.getString("open1"),
					provider.getString("error"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			model.loadDocument(filePath);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(frame, provider.getString("pathError"), provider.getString("error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
