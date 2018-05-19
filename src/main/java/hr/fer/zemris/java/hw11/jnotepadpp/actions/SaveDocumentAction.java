package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;

public class SaveDocumentAction extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (openedFilePath == null) {
			JFileChooser jfc = new JFileChooser();

			jfc.setDialogTitle("Save document");
			if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this, "Nista nije snimljeno!", "Upozorenje",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			openedFilePath = jfc.getSelectedFile().toPath();

		}

		byte[] podaci = editor.getText().getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(openedFilePath, podaci);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(JNotepadPP.this, "Prilikom zapisivanja dogodila se pogreska!", "Pogreska",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(JNotepadPP.this, "Datoteka je snimljena!", "Informacija",
				JOptionPane.INFORMATION_MESSAGE);

	}

}
