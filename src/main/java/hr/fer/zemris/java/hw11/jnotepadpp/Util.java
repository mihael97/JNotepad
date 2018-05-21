package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Abstract class which has only method for document saving
 * 
 * @author Mihael
 *
 */
public abstract class Util {
	/**
	 * Method saves document to path chosen from {@link JFileChooser}
	 * 
	 * @param frame
	 *            - frame
	 * @param doc
	 *            - document
	 * @param provider
	 *            - language provider
	 * @param documentModel
	 *            - document model
	 */
	public static void getPath(JFrame frame, SingleDocumentModel doc, LocalizationProviderBridge provider,
			DefaultMultipleDocumentModel documentModel) {
		Path path;

		if (doc.getFilePath() == null) {
			JFileChooser jfc = new JFileChooser();

			jfc.setDialogTitle(provider.getString("exit3"));
			if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(frame, provider.getString("exit4"), provider.getString("error"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			path = jfc.getSelectedFile().toPath();
		} else {
			path = doc.getFilePath();
		}

		documentModel.saveDocument(new DefaultSingleDocumentModel(path, doc.getTextComponent().getText()), path);
	}
}
