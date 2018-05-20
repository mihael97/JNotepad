package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.DefaultSingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

public class SaveDocumentAction extends AbstractAction {

	private JFrame frame;
	private DefaultMultipleDocumentModel documentModel;
	private boolean flag;

	public SaveDocumentAction(JFrame frame, DefaultMultipleDocumentModel documentModel, boolean flag) {
		this.frame = Objects.requireNonNull(frame);
		this.documentModel = Objects.requireNonNull(documentModel);
		this.flag = flag;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		SingleDocumentModel document = documentModel.getCurrentDocument();

		if (document.getFilePath() == null || flag == true) {
			JFileChooser jfc = new JFileChooser();

			jfc.setDialogTitle("Save document");
			if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(frame, LocalizationProvider.getInstance().getString("exit4"),
						LocalizationProvider.getInstance().getString("error"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			document.setFilePath(jfc.getSelectedFile().toPath());

		}

		documentModel.saveDocument(document, document.getFilePath());
		JOptionPane.showMessageDialog(frame, LocalizationProvider.getInstance().getString("fileStored"),
				LocalizationProvider.getInstance().getString("Information"), JOptionPane.INFORMATION_MESSAGE);

	}

}
