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
						"File \'" + doc.getFilePath() + "\' is modified but not saved. Do you want to save it?",
						"Modified file", JOptionPane.YES_NO_CANCEL_OPTION);
				if (response == JOptionPane.YES_OPTION) {

					Path path;

					if (doc.getFilePath() == null) {
						JFileChooser jfc = new JFileChooser();

						jfc.setDialogTitle("Save document");
						if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
							JOptionPane.showMessageDialog(frame, "No stored data", "Error",
									JOptionPane.WARNING_MESSAGE);
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
