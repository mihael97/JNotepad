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

public class ExitAction extends AbstractAction {

	private DefaultMultipleDocumentModel documentModel;
	private JFrame frame;

	public ExitAction(DefaultMultipleDocumentModel documentModel, JFrame frame) {
		this.documentModel = Objects.requireNonNull(documentModel);
		this.frame = Objects.requireNonNull(frame);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		for (int i = 0, length = documentModel.getNumberOfDocuments(); i < length; i++) {
			SingleDocumentModel doc = documentModel.getDocument(i);

			if (doc.isModified()) {
				int response = JOptionPane.showConfirmDialog(frame,
						LocalizationProvider.getInstance().getString("exit1") + doc.getFilePath()
								+ LocalizationProvider.getInstance().getString("exit2"),
						"Modified file", JOptionPane.YES_NO_CANCEL_OPTION);
				if (response == JOptionPane.YES_OPTION) {

					Path path;

					if (doc.getFilePath() == null) {
						JFileChooser jfc = new JFileChooser();

						jfc.setDialogTitle(LocalizationProvider.getInstance().getString("exit3"));
						if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
							JOptionPane.showMessageDialog(frame, LocalizationProvider.getInstance().getString("exit4"),
									LocalizationProvider.getInstance().getString("error"), JOptionPane.WARNING_MESSAGE);
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
		System.exit(1);
	}
}
